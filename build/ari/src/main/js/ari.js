/*
 * Copyright 2014 takahashikzn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
(function() {
    "use strict";

    var ARI = {};

    var cout = function(msg) {
        Java.type('java.lang.System').out.println(msg);
    };

    var cerr = function(msg) {
        Java.type('java.lang.System').err.println(msg);
    };

    var array = function(ary) {
        return [].slice.apply(ary);
    };

    (function() {
        if (!String.prototype.capitalize) {
            String.prototype.capitalize = function() {
                return this[0].toUpperCase() + this.substring(1);
            };
        }

        if (!String.prototype.uncapitalize) {
            String.prototype.uncapitalize = function() {
                return this[0].toLowerCase() + this.substring(1);
            };
        }
    }());

    var javaOps = {

        nativeClass: (function() {

            var classFacade = Java.type('java.lang.Class');

            var isNativeClass = function(o) {
                return o['class'] && (o['class'].name === 'java.lang.Class');
            };

            var forName = function(clazz) {

                if (typeof clazz === 'string') {

                    return classFacade.forName(clazz);
                } else if (isNativeClass(clazz)) {

                    return clazz;
                } else if (clazz.getName) {

                    return classFacade.forName(clazz.getName());
                } else {

                    throw new Error('unrecognizable type: ' + clazz);
                }
            };

            var javaPkg = (function(java) {

                java.lang = {
                    Class: classFacade,
                    Enum: forName('java.lang.Enum'),
                    System: forName('java.lang.System'),

                    String: forName('java.lang.String'),
                    Boolean: forName('java.lang.Boolean'),
                    Integer: forName('java.lang.Integer'),
                    Long: forName('java.lang.Long'),
                    Short: forName('java.lang.Short'),
                    Byte: forName('java.lang.Byte'),
                    Character: forName('java.lang.Character'),
                    Double: forName('java.lang.Double'),
                    Float: forName('java.lang.Float'),
                    Void: forName('java.lang.Void')
                };

                java.lang.primitive = {
                    int: java.lang.Integer,
                    long: java.lang.Long,
                    short: java.lang.Short,
                    byte: java.lang.Byte,
                    float: java.lang.Float,
                    double: java.lang.Double,
                    char: java.lang.Character,
                    boolean: java.lang.Boolean,
                    'void': java.lang.Void
                };

                return java;
            }({}));

            return {
                matches: isNativeClass,
                of: forName,
                java: javaPkg
            };
        }()),

        isInstance: function(cls, obj) {

            return (this.nativeClass.java.lang.primitive[cls.name] || cls).isInstance(obj);
        },

        newInstance: function(clazz) {

            var javaType = (clazz == 'string') ? Java.type(clazz) : clazz;

            return new javaType(array(arguments).slice(1));
        },

        propType: function(clazz, name) {

            return array(this.nativeClass.of(clazz).getMethods()) //
            .filter(function(m) {
                return m.name === ('set' + name.capitalize());
            }) //
            .map(function(m) {
                return m.parameterTypes[0];
            })[0];
        },

        toJava: function(cls, val) {

            var typeFacade;
            var nativeType;

            if (typeof cls === 'string') {
                typeFacade = Java.type(cls);
                nativeType = this.nativeClass.of(cls);
            } else if (this.nativeClass.matches(cls)) {
                typeFacade = Java.type(cls.name);
                nativeType = cls;
            } else {
                typeFacade = cls;
                nativeType = this.nativeClass.of(cls.getName());
            }

            if (this.isInstance(nativeType, val)) {
                return val;
            } else {
                return this.newInstance(typeFacade, val);
            }
        },

        isNativeJavaObj: function(o) {
            return o['class'] && this.nativeClass.matches(o['class']);
        },

        populate: function(obj, attrs) {

            var isJavaObj = this.isNativeJavaObj(obj);
            var nativeClass = obj['class'];

            attrs && Object.keys(attrs).forEach(function(x) {
                if (attrs[x]) {
                    if (isJavaObj) {
                        obj[x] = this.toJava(javaOps.propType(nativeClass, x), attrs[x]);
                    } else {
                        obj[x] = attrs[x];
                    }
                }
            }.bind(this));

            return obj;
        }
    }

    var reentrant = {

        /**
         * @private
         * @param {!reentrantEnv} env
         * @param {!string} methodName
         * @param {!function()} superMethod
         * @param {!function()} thisMethod
         */
        wrap: function(env, methodName, superMethod, thisMethod) {

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
        },

        /**
         * @constructor
         * @param {!Object=} opts
         */
        Env: function(opts) {

            opts = opts || {};

            /** @type {!number} */
            this.stackDepth = opts.stackDepth || 0;
        }
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
                return javaOps.toJava('java.io.File', attrs);

            case 'url':
            case 'URL':
                return javaOps.toJava('java.net.URL', attrs);

            default:
                var data = project.createDataType(name);

                if (attrs instanceof Function) {
                    attrs.apply(data);
                } else {
                    javaOps.populate(data, attrs);
                }

                return data;
        }
    };

    /**
     * @private
     * @param {!string} taskName
     * @param {!(Object|function())=} attrs
     */
    ARI._callableTask = function(taskName, attrs) {

        return function() {
            var task = project.createTask(taskName);

            if (attrs instanceof Function) {
                attrs.apply(task, arguments);
            } else {
                javaOps.populate(task, attrs);
            }

            task.perform();
        };
    };

    /**
     * @param {!string} taskName
     * @param {!(Object|function())=} attrs
     */
    ARI.task = function(taskName, attrs) {
        this._callableTask(taskName, attrs)();
        return this;
    };

    /**
     * @param {!string} name
     * @param {!string} classname
     * @param {!(Object|function())=} attrs
     */
    ARI.taskdef = function(name, classname, attrs) {

        return this.task('taskdef', function() {
            javaOps.populate(this, javaOps.populate({
                name: name,
                classname: classname
            }, attrs));

            this.antlibClassLoader = Java.type('org.apache.tools.ant.Main').class.classLoader;
        });
    };

    /**
     * @enum {!string}
     */
    ARI.LogLevel = {
        DEBUG: 'debug',
        INFO: 'info',
        WARNING: 'warning',
        ERROR: 'error'
    };

    /**
     * @param {!string} msg
     * @param {!ARI.LogLevel=} level
     */
    ARI.echo = function(msg, level) {

        return this.task('echo', {
            message: msg,
            level: level && Java.type('org.apache.tools.ant.types.LogLevel')[level.toUpperCase()]
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
        return javaOps.newInstance.apply(arguments);
    };

    ARI.run = function(code) {
        eval('function(ARI) { ' + code + ' }')(this);
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
                ARI.error(x + ': ' + e.message + '\n' + e.stack);
                throw e;
            }
        };

        ARI[x] = reentrant.wrap(env, x, superMethod, thisMethod);
    }.bind(new reentrant.Env()));

    return ARI;
}());
