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

var resultDir = 'target/test-result';
var targetJar = 'target/indolently.jar';
var testClassDir = 'target/test-classes';

ARI.task('delete', {
    dir: resultDir
}).task('mkdir', {
    dir: resultDir
}).task('junit', {
    printsummary: true,
    fork: true,
    forkmode: 'once',

    '': {
        classpath: [ {
            refid: 'lib'
        }, {
            path: targetJar
        }, {
            path: testClassDir
        } ],
        batchtest: {
            todir: resultDir,

            '': {
                fileset: {
                    dir: testClassDir,
                    includes: '**/*Test.class'
                },
                formatter: {
                    type: 'plain',
                    usefile: false
                }
            }
        }
    }
});
