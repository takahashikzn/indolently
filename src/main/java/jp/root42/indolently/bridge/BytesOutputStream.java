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
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public abstract class BytesOutputStream
    extends ByteArrayOutputStream {

    final OutputStream sink;

    protected BytesOutputStream(final OutputStream sink) { this.sink = sink; }

    @Override
    public void write(final byte[] b, final int off, final int len) { let(() -> this.sink.write(b, off, len)); }

    @Override
    public void write(final int b) { let(() -> this.sink.write(b)); }

    @Override
    public void close() { let(() -> this.sink.close()); }

    @Override
    public void write(final byte[] b) { let(() -> this.sink.write(b)); }

    @Override
    public void flush() { let(() -> this.sink.flush()); }

    @Override
    @Deprecated
    public String toString() { return this.sink.toString(); }

    public abstract int write(final InputStream in) throws IOException;

    public abstract InputStream toInputStream();

    public static BytesOutputStream create() {
        return CommonsIOImpl.isAvailable() ? new CommonsIOImpl() : new JDKImpl();
    }

    public static BytesOutputStream create(final int len) {
        return CommonsIOImpl.isAvailable() ? new CommonsIOImpl(len) : new JDKImpl(len);
    }

    @SuppressWarnings("NonSynchronizedMethodOverridesSynchronizedMethod")
    private static class JDKImpl
        extends BytesOutputStream {

        private final ByteArrayOutputStream baos;

        public JDKImpl() {
            super(new ByteArrayOutputStream());
            this.baos = cast(this.sink);
        }

        public JDKImpl(final int len) {
            super(new ByteArrayOutputStream(len));
            this.baos = cast(this.sink);
        }

        private byte[] buf;

        @Override
        public int write(final InputStream in) throws IOException { return (int) in.transferTo(this); }

        @Override
        public void writeTo(final OutputStream out) throws IOException { this.baos.writeTo(out); }

        @Override
        public int size() { return this.baos.size(); }

        @Override
        public InputStream toInputStream() { return new ByteArrayInputStream(this.toByteArray()); }

        @Override
        public byte[] toByteArray() { return this.baos.toByteArray(); }

        @Override
        public void reset() { this.baos.reset(); }

        @Override
        public String toString(final String enc) throws UnsupportedEncodingException { return this.baos.toString(enc); }

        @Override
        public String toString(final Charset charset) { return this.baos.toString(charset); }
    }

    @SuppressWarnings("NonSynchronizedMethodOverridesSynchronizedMethod")
    private static class CommonsIOImpl
        extends BytesOutputStream {

        private static final boolean avail =
            ObjFactory.isPresent("org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream");

        public static boolean isAvailable() { return avail; }

        private final UnsynchronizedByteArrayOutputStream baos;

        public CommonsIOImpl() {
            super(new UnsynchronizedByteArrayOutputStream());
            this.baos = cast(this.sink);
        }

        public CommonsIOImpl(final int len) {
            super(new UnsynchronizedByteArrayOutputStream(len));
            this.baos = cast(this.sink);
        }

        @Override
        public int write(final InputStream in) throws IOException { return this.baos.write(in); }

        @Override
        public void writeTo(final OutputStream out) throws IOException { this.baos.writeTo(out); }

        @Override
        public int size() { return this.baos.size(); }

        @Override
        public void reset() { this.baos.reset(); }

        @Override
        public InputStream toInputStream() { return this.baos.toInputStream(); }

        @Override
        public byte[] toByteArray() { return this.baos.toByteArray(); }

        @Override
        public String toString(final String enc) throws UnsupportedEncodingException { return this.baos.toString(enc); }

        @Override
        public String toString(final Charset charset) { return this.baos.toString(charset); }
    }
}
