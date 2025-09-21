// Copyright 2025 takahashikzn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package jp.root42.indolently.conc.exec;

import java.util.concurrent.Executor;

import jp.root42.indolently.conc.PrioritySemaphore;


/**
 * @author takahashikzn
 */
public interface PriorityExecutor
    extends Executor {

    void execute(Runnable task, int nice);

    interface PriorityRunnable
        extends Runnable {

        int nice();
    }

    @Override
    default void execute(final Runnable task) { this.execute(task, task instanceof PriorityRunnable pr ? pr.nice() : PrioritySemaphore.NO_NICE); }
}
