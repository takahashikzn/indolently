(function() {

    var ARI = {};

    var convertToJava = function(cls, val) {

        if (typeof cls === 'string') {
            cls = Java.type(cls);
        }

        if (val instanceof cls) {
            return path;
        } else {
            return this.neo(cls, val);
        }
    }.bind(ARI);

    var extend = function(obj, attrs) {

        attrs && Object.keys(attrs).forEach(function(x) {
            if (attrs[x]) {
                obj[x] = attrs[x];
            }
        });

        return obj;
    };

    /**
     * @private
     * @param {!reentrantEnv} env
     * @param {!string} methodName
     * @param {!function()} superMethod
     * @param {!function()} thisMethod
     */
    var reentrant = function(env, methodName, superMethod, thisMethod) {

        return function() {
            try {
                env.stackDepth++;

                if (1 < env.stackDepth) {
                    return superMethod.apply(this, arguments);
                } else {
                    return thisMethod.apply(this, arguments);
                }
            } finally {
                env.stackDepth--;
            }
        };
    };

    /**
     * @constructor
     * @param {!Object=} opts
     */
    var reentrantEnv = function(opts) {

        opts = opts || {};

        /** @type {!number} */
        this.stackDepth = opts.stackDepth || 0;
    };

    ARI.prop = function(name) {
        if (1 < arguments.length) {
            ARI.echo('define property: ' + name + ' = ' + arguments[1])
            project.setProperty(arguments[0], arguments[1]);
        }

        return project.getProperty(arguments[0]);
    };

    ARI.struct = function(name, attrs) {

        switch (name) {

            case 'file':
            case 'File':
                return convertToJava('java.io.File', attrs);

            case 'url':
            case 'URL':
                return convertToJava('java.net.URL', attrs);

            default:
                var data = project.createDataType(name);

                if (attrs instanceof Function) {
                    attrs.apply(data);
                } else {
                    extend(data, attrs);
                }

                return data;
        }
    };

    ARI.antcallable = function(taskName, attrs) {
        return function() {
            var task = project.createTask(taskName);

            if (attrs instanceof Function) {
                attrs.apply(task, arguments);
            } else {
                extend(task, attrs);
            }

            return task.perform();
        };
    };

    ARI.antcall = function(taskName, attrs) {
        return this.antcallable(taskName, attrs)();
    };

    ARI.taskdef = function(name, classname, attrs) {
        return this.antcall('taskdef', function() {
            extend(this, extend({
                name: name,
                classname: classname
            }, attrs));

            this.antlibClassLoader = Java.type('org.apache.tools.ant.Main').class.classLoader;
            ARI.echo('cl: ' + this.antlibClassLoader);
        });
    };

    ARI.echo = function(msg, level) {
        return this.antcall('echo', {
            message: msg,
            level: (function() {
                if (level) {
                    return Java.type('org.apache.tools.ant.types.LogLevel')[level.toUpperCase()];
                }
            }())
        });
    };

    ARI.error = function(msg) {
        return this.echo(msg, 'error');
    };

    ARI.loadClass = function(fqcn) {
        return Java.type('java.lang.Class').forName(fqcn);
    };

    ARI.classExists = function(fqcn) {
        try {
            return !!Java.type(fqcn);
        } catch (e) {
            return false;
        }
    };

    ARI.neo = function(clazz) {
        var cls = (typeof clazz === 'string') ? Java.type(clazz) : clazz;
        return new cls([].slice.call(arguments, 1));
    };

    Object.keys(ARI).forEach(function(x) {
        if (/^(echo|error)$/.test(x)) {
            return;
        }

        var env = this;
        var superMethod = ARI[x];
        var thisMethod = function() {
            try {
                return superMethod.apply(this, arguments);
            } catch (e) {
                ARI.error(x + ': ' + e.message);
                throw e;
            }
        };

        ARI[x] = reentrant(env, x, superMethod, thisMethod);
    }.bind(new reentrantEnv()));

    return ARI;
}());
