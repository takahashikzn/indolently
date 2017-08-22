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
"use strict";

var targets = [{
    srcdir: 'src/main/java',
    destdir: 'target/classes',
    classpath: []
}];

if (param.test != false) {
    targets.push({
        srcdir: 'src/test/java',
        destdir: 'target/test-classes',
        classpath: [{
            path: 'target/classes'
        }]
    });
}

targets.forEach(function(x) {

    ARI.task('mkdir', {
        dir: x.destdir
    }).task('javac', {
        encoding: 'UTF-8',
        source: 1.8,
        target: 1.8,
        debug: true,
        srcdir: x.srcdir,
        destdir: x.destdir,

        '': {
            compilerarg: {
                value: ['-Xlint:all', '-rawtypes', '-unchecked'].join(',')
            },
            classpath: [{
                refid: 'lib'
            }].concat(x.classpath)
        }
    });
});
