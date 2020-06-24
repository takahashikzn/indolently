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
package jp.root42.indolently.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import jp.root42.indolently.bridge.ObjFactory;
import org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream;


/**
 * @author takahashikzn
 */
@SuppressWarnings("NonSynchronizedMethodOverridesSynchronizedMethod")
public class BytesOutputStream
    extends ByteArrayOutputStream {

    public static boolean isAvailable() {
        return ObjFactory.isPresent("org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream");
    }

    private final UnsynchronizedByteArrayOutputStream baos;

    public BytesOutputStream() { this.baos = new UnsynchronizedByteArrayOutputStream(); }

    public BytesOutputStream(final int len) { this.baos = new UnsynchronizedByteArrayOutputStream(len); }

    @Override
    public void write(final byte[] b, final int off, final int len) { this.baos.write(b, off, len); }

    @Override
    public void write(final int b) { this.baos.write(b); }

    public int write(final InputStream in) throws IOException { return this.baos.write(in); }

    @Override
    public int size() { return this.baos.size(); }

    @Override
    public void reset() { this.baos.reset(); }

    @Override
    public void writeTo(final OutputStream out) throws IOException { this.baos.writeTo(out); }

    public static InputStream toBufferedInputStream(final InputStream input)
        throws IOException { return UnsynchronizedByteArrayOutputStream.toBufferedInputStream(input); }

    public static InputStream toBufferedInputStream(final InputStream input, final int size)
        throws IOException { return UnsynchronizedByteArrayOutputStream.toBufferedInputStream(input, size); }

    public InputStream toInputStream() { return this.baos.toInputStream(); }

    @Override
    public byte[] toByteArray() { return this.baos.toByteArray(); }

    @Override
    public void close() {
        try { this.baos.close(); } //
        catch (final IOException e) { throw new RuntimeException(e); }
    }

    @Override
    @Deprecated
    public String toString() { return this.baos.toString(); }

    @Override
    public String toString(final String enc) throws UnsupportedEncodingException { return this.baos.toString(enc); }

    @Override
    public String toString(final Charset charset) { return this.baos.toString(charset); }

    @Override
    public void write(final byte[] b) {
        try { this.baos.write(b); } //
        catch (final IOException e) { throw new RuntimeException(e); }
    }

    @Override
    public void flush() {
        try { this.baos.flush(); } //
        catch (final IOException e) { throw new RuntimeException(e); }
    }
}
