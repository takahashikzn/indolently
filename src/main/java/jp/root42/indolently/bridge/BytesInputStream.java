// Copyright 2020 takahashikzn
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
package jp.root42.indolently.bridge;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.input.UnsynchronizedByteArrayInputStream;

import static jp.root42.indolently.Expressive.*;


/**
 * @author takahashikzn
 */
@SuppressWarnings("NonSynchronizedMethodOverridesSynchronizedMethod")
public abstract class BytesInputStream
    extends ByteArrayInputStream {

    protected BytesInputStream() { super(new byte[0]); }

    public static BytesInputStream create(final byte[] bin) {
        return CommonsIOImpl.isAvailable() ? new CommonsIOImpl(bin) : new JDKImpl(bin);
    }

    public static InputStream create(final ByteArrayOutputStream baos) {
        return (baos instanceof BytesOutputStream) ? ((BytesOutputStream) baos).toInputStream() : create(baos.toByteArray());
    }

    private static class JDKImpl
        extends BytesInputStream {

        private final ByteArrayInputStream bais;

        public JDKImpl(final byte[] bin) { this.bais = new ByteArrayInputStream(bin); }

        @Override
        public int available() { return this.bais.available(); }

        @Override
        public int read() { return this.bais.read(); }

        @Override
        public int read(final byte[] b) { return eval(() -> this.bais.read(b)); }

        @Override
        public int read(final byte[] b, final int off, final int len) { return this.bais.read(b, off, len); }

        @Override
        public long skip(final long n) { return this.bais.skip(n); }

        @Override
        public boolean markSupported() { return this.bais.markSupported(); }

        @Override
        public void mark(final int readlimit) { this.bais.mark(readlimit); }

        @Override
        public void reset() { this.bais.reset(); }

        @Override
        public byte[] readAllBytes() { return this.bais.readAllBytes(); }

        @Override
        public byte[] readNBytes(final int len) {
            try { return this.bais.readNBytes(len); } //
            catch (final IOException e) { throw new RuntimeException(e); }
        }

        @Override
        public int readNBytes(final byte[] b, final int off, final int len) {
            return this.bais.readNBytes(b, off, len);
        }

        @Override
        public long transferTo(final OutputStream out) throws IOException { return this.bais.transferTo(out); }

        @Override
        public void close() { }
    }

    private static class CommonsIOImpl
        extends BytesInputStream {

        private static final boolean avail = ObjFactory.isPresent("org.apache.commons.io.output.UnsynchronizedByteArrayInputStream");

        public static boolean isAvailable() { return avail; }

        private final UnsynchronizedByteArrayInputStream bais;

        public CommonsIOImpl(final byte[] bin) { this.bais = new UnsynchronizedByteArrayInputStream(bin); }

        @Override
        public int available() { return this.bais.available(); }

        @Override
        public int read() { return this.bais.read(); }

        @Override
        public int read(final byte[] b) { return this.bais.read(b); }

        @Override
        public int read(final byte[] b, final int off, final int len) { return this.bais.read(b, off, len); }

        @Override
        public long skip(final long n) { return this.bais.skip(n); }

        @Override
        public boolean markSupported() { return this.bais.markSupported(); }

        @Override
        public void mark(final int readlimit) { this.bais.mark(readlimit); }

        @Override
        public void reset() { this.bais.reset(); }

        @Override
        public byte[] readAllBytes() {
            try { return this.bais.readAllBytes(); } //
            catch (final IOException e) { throw new RuntimeException(e); }
        }

        @Override
        public byte[] readNBytes(final int len) {
            try { return this.bais.readNBytes(len); } //
            catch (final IOException e) { throw new RuntimeException(e); }
        }

        @Override
        public int readNBytes(final byte[] b, final int off, final int len) {
            try { return this.bais.readNBytes(b, off, len); } //
            catch (final IOException e) { throw new RuntimeException(e); }
        }

        @Override
        public long transferTo(final OutputStream out) throws IOException { return this.bais.transferTo(out); }

        @Override
        public void close() { }
    }
}
