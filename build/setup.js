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

[ {
    name: 'ivy-configure',
    classname: 'org.apache.ivy.ant.IvyConfigure'
}, {
    name: 'ivy-resolve',
    classname: 'org.apache.ivy.ant.IvyResolve'
}, {
    name: 'ivy-retrieve',
    classname: 'org.apache.ivy.ant.IvyRetrieve'
} ].forEach(function(x) {

    ARI.taskdef(x.name, x.classname);
});

ARI.task('ivy-configure', {
    file: 'ivysettings.xml'
});

ARI.task('ivy-resolve', {
    file: 'ivy.xml',
    haltonfailure: false
});

ARI.task('delete', {
    dir: 'target/lib'
});

ARI.task('mkdir', {
    dir: 'target/lib'
});

ARI.task('ivy-retrieve', {
    conf: '*',
    pattern: 'target/lib/default/[module]-[revision].[ext]'
});
