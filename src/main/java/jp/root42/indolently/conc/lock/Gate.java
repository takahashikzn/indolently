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
package jp.root42.indolently.conc.lock;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import jp.root42.indolently.ref.$;

import static java.util.Objects.requireNonNull;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public interface Gate {

    default void acquire() throws InterruptedException { this.acquire(1); }

    default void acquire(final int permits) throws InterruptedException {
        if (!this.tryAcquire(permits, Long.MAX_VALUE, TimeUnit.SECONDS)) throw new IllegalStateException("acquire should not fail with Long.MAX_VALUE timeout");
    }

    default boolean tryAcquire() { return this.tryAcquire(1); }

    boolean tryAcquire(int permits);

    default boolean tryAcquire(final long timeout, final TimeUnit unit) throws InterruptedException { return this.tryAcquire(1, timeout, unit); }

    boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException;

    default void release() { this.release(1); }

    void release(int permits);

    int drainPermits();

    int availablePermits();

    interface Permit
        extends AutoCloseable {

        @Override
        void close();
    }

    private Permit permit(final int permits) {
        final var closed = new AtomicBoolean();
        return () -> { if (closed.compareAndSet(false, true)) this.release(permits); };
    }

    default Permit lease() throws InterruptedException { return this.lease(1); }

    default Permit lease(final int permits) throws InterruptedException {
        this.acquire(permits);
        return this.permit(permits);
    }

    default $<Permit> tryLease(final long timeout, final TimeUnit unit) { return this.tryLease(1, timeout, unit); }

    default $<Permit> tryLease() { return this.tryLease(1); }

    default $<Permit> tryLease(final int permits) { return this.tryLease(permits, 0, TimeUnit.SECONDS); }

    default $<Permit> tryLease(final int permits, final long timeout, final TimeUnit unit) {

        try {
            if ((timeout == 0) ? this.tryAcquire(permits) : this.tryAcquire(permits, timeout, unit)) //
                return just(this.permit(permits));
        } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }

        return none();
    }

    static Gate of(final int capacity) { return of(capacity, true); }

    static Gate of(final int capacity, final boolean fair) { return of(new Semaphore(capacity, fair)); }

    static Gate of(final Semaphore sem) {
        requireNonNull(sem, "sem is null");

        return new Gate() {

            @Override
            public boolean tryAcquire(final int permits) { return sem.tryAcquire(permits); }

            @Override
            public boolean tryAcquire(final int permits, final long timeout, final TimeUnit unit) throws InterruptedException {
                return sem.tryAcquire(permits, timeout, unit);
            }

            @Override
            public void release(final int permits) { sem.release(permits); }

            @Override
            public int drainPermits() { return sem.drainPermits(); }

            @Override
            public int availablePermits() { return sem.availablePermits(); }
        };
    }

    abstract class Delegate<G extends Gate>
        implements Gate {

        protected final G gate;

        protected Delegate(final G gate) { this.gate = requireNonNull(gate); }

        @Override
        public final void acquire() throws InterruptedException { Gate.super.acquire(); }

        @Override
        public void acquire(final int permits) throws InterruptedException { this.gate.acquire(permits); }

        @Override
        public final boolean tryAcquire() { return Gate.super.tryAcquire(); }

        @Override
        public boolean tryAcquire(final int permits) { return this.gate.tryAcquire(permits); }

        @Override
        public final boolean tryAcquire(final long timeout, final TimeUnit unit) throws InterruptedException { return Gate.super.tryAcquire(timeout, unit); }

        @Override
        public boolean tryAcquire(final int permits, final long timeout, final TimeUnit unit) throws InterruptedException {
            return this.gate.tryAcquire(permits, timeout, unit);
        }

        @Override
        public final void release() { Gate.super.release(); }

        @Override
        public void release(final int permits) { this.gate.release(permits); }

        @Override
        public int drainPermits() { return this.gate.drainPermits(); }

        @Override
        public int availablePermits() { return this.gate.availablePermits(); }

        @Override
        public final Permit lease() throws InterruptedException { return Gate.super.lease(); }

        @Override
        public Permit lease(final int permits) throws InterruptedException { return this.gate.lease(permits); }

        @Override
        public final $<Permit> tryLease(final long timeout, final TimeUnit unit) { return Gate.super.tryLease(timeout, unit); }

        @Override
        public final $<Permit> tryLease() { return Gate.super.tryLease(); }

        @Override
        public $<Permit> tryLease(final int permits) { return this.gate.tryLease(permits); }

        @Override
        public $<Permit> tryLease(final int permits, final long timeout, final TimeUnit unit) { return this.gate.tryLease(permits, timeout, unit); }
    }
}
