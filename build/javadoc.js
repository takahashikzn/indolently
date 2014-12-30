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

var destdir = 'target/api';
var title = 'Indolently API Document';
var encoding = 'UTF-8';

ARI.task('mkdir', {
    dir: destdir
}).task('javadoc', {
    source: 1.8,
    encoding: encoding,
    docencoding: encoding,
    useexternalfile: true,
    sourcepath: 'src/main/java',
    destdir: destdir,
    classpathref: 'lib',
    windowtitle: title,
    doctitle: title,

    '': {
        link: {
            href: 'http://docs.oracle.com/javase/8/docs/api/'
        },
        classpath: {
            path: 'target/classes'
        }
    }
});
