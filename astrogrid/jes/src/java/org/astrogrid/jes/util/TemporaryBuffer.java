/*$Id: TemporaryBuffer.java,v 1.2 2004/11/05 16:52:42 jdt Exp $
 * Created on 02-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.util;

import org.astrogrid.component.descriptor.ComponentDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import junit.framework.Test;

/** this class is an abstraction around a ByteBuffer, and is used to provide temporary working space - e.g. for marshalling / unmarshalling xml documents.
 * Idea is to reuse this buffer for many marshalling operations, rather than create a new StringReader / StringWriter pair for each. In this way the same section of allocated memory is reused, 
 * hopefully avoiding possible problems with space leaks from many readers and writers.
 * Obviously, this class is not thread-safe - however, the components of JES operate in a single-threaded manner.
 * <p>
 * need to be careful with handling of buffer overflows. Readers and Writers are unbounded in size, while Buffers have a fixed size. Which is a bit of a pain - need to 
 * catch Buffer overflows, and copy contents to a new buffer.
 * @see org.astrogrid.jes.util.TemporaryBufferTest for tests and exmaples of use.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Nov-2004
 *
 */
public class TemporaryBuffer implements ComponentDescriptor{

    /** Construct a new WorkingSpace
     * 
     */
    public TemporaryBuffer() {
        super();
    }
    
    
    protected final static int START_CAPACITY = 1024 * 20; // 10K I think. good start.
    
    protected ByteBuffer buff = ByteBuffer.allocate(START_CAPACITY);
    
    protected final InputStream is = new TemporaryBufferInputStream();
    protected final OutputStream os = new TemporaryBufferOutputStream();
    protected final Writer w = new TemporaryBufferWriter();
    protected final TemporaryBufferReader r = new TemporaryBufferReader();
    
    protected boolean writing = true;
    /** call this to place the buffer in <i>read mode</i>. After this, possible to call {@link #getContents}, {@link #getInputStream()} {@link #getReader()}
     *  */
    public void readMode() {
        //buff.limit(buff.position());
        buff.flip();
        writing = false;
    }
    
    /** call this to place the buffer in <i>write mode</i>. After this, possible to call{@link #getOutputStream()}, {@link #getWriter()} */
    public void writeMode() {
        buff.clear();
        writing = true;
    }
       
    /** access a stream from which to read the contents of the buffer
     * pre: {@link #readMode() */
    public InputStream getInputStream() {
        if (writing) {
            throw new IllegalStateException("Temporary buffer must be in read mode");
        }
        buff.rewind();
        return is;
    }
    
    /** access a reader from which to read the contents of the buffer
     * pre: {@link #readMode() }
     * @return
     * 
     */
    public Reader getReader() {
        if (writing) {
            throw new IllegalStateException("Temporary buffer must be in read mode");
        }
        buff.rewind();
        r.setCBuff(buff.asCharBuffer());        
        return r;
    }
    
    /** access an output stream whcih can be used to write to the buffer
     * pre: {@link #writeMode}
     * @return
     */
    public OutputStream getOutputStream() {
        if (!writing) {
            throw new IllegalStateException("Temporary buffer must be in write mode");
        }
        return os;
    }
    
    /** access a writer which can be used to write to the buffer
     * pre: {@link #writeMode}
     * @return
     */
    public Writer getWriter() {
        if (!writing) {
            throw new IllegalStateException("Temporary buffer must be in write mode");
        }
        return w;
    }
    
    /** access the contents of the buffer
     * pre: {@link #readMode()}
     * @return
     */
    public String getContents() {
        return buff.asCharBuffer().toString();     
    }
    /**
     * access the contents of the buffer, decoded according to a particular charset.
     * pre: {@link #readMode()}
     * @param charset the decoder to use
     * @return
     */
    public String getContents(String charset) {
        return Charset.forName(charset).decode(buff).toString();
    }
    
          
    
    /** 
     * this is simple - and works. Verified by {@link TemporaryBufferTest#testVariableAssignmentInNestedClasses()}
     *
     */
    protected void growBuffer() {
        ByteBuffer newBuff = ByteBuffer.allocate(buff.capacity() * 2);
        buff.flip();
        newBuff.put(buff);
        buff = newBuff;
    }
    /**
     * input stream for reading from the buffer.
     * tries to implement all the inptuStream methods efficiently in terms of the underlying Buffer operations.
     * There's a lot of congruence here, but there are a few irritating differences between the behaviour of buffers and streams.
     * Don't really understand why this is - think that there should be methods for creating streams for buffers already in the jdk.
     * @author Noel Winstanley nw@jb.man.ac.uk 03-Nov-2004
     *
     */
    
    protected class TemporaryBufferInputStream extends InputStream {
        
        public int available() throws IOException {
            return buff.remaining();
        }

        public synchronized void mark(int readlimit) {
            buff.mark();
        }
        public boolean markSupported() {
            return true;
        }
        /** contract is a little different here 
         * buffer will throw an exceptioin if there isn't enough bytes available to satisfy request.
         * stream expects it just to provide as many bytes as possible.*/
        public int read(byte[] b, int off, int len) throws IOException {
            if (! buff.hasRemaining()){
                return -1;
            }             
            // calculate actual number of bytes to request - 
            // if len is more than what's available, we'll request all thats available instead.
            int actualLength = len > buff.remaining() ? buff.remaining() : len;
            buff.get(b,off,actualLength);
            return actualLength;
        }

        /**
         * @see java.io.InputStream#read()
         */
        public int read() throws IOException {
            if (!buff.hasRemaining()) { // test for end of buffer
                return -1;
            } else {
                return buff.get();
            }
        }        
        
        public synchronized void reset() throws IOException {
            buff.reset();
        }
        /** think this one is right. */
        public long skip(long n) throws IOException {
            int currentPos = buff.position();
            int toSkip = ((int)n) > buff.remaining() ? buff.remaining() : ((int)n);
            buff.position(currentPos + toSkip);
            return toSkip;
        }

    }
    
    /** can use a charBuffer view for this - as each reader / input stream resets the position 
     *  - so no need for this class to share state with the parent.*/
    protected static class TemporaryBufferReader extends Reader {
        protected CharBuffer cBuff;
        public void setCBuff(CharBuffer b) {
            cBuff = b;
        }
        public void mark(int readAheadLimit) throws IOException {
            cBuff.mark();
        }
        public boolean markSupported() {
            return true;
        }
        public int read() throws IOException {
            if (! cBuff.hasRemaining()){
                return -1;
            } else {                
                return cBuff.get();
            }
        }
        public boolean ready() throws IOException {
            return cBuff.hasRemaining();
        }
        public void reset() throws IOException {
            cBuff.reset();
        }
        public long skip(long n) throws IOException {
            int currentPos = cBuff.position();            
            int toSkip = ((int)n)  > cBuff.remaining() ? cBuff.remaining() : ((int)n);
            cBuff.position(currentPos + toSkip );
            return toSkip;            
        }
/**
         * @see java.io.Reader#close()
         */
        public void close() throws IOException {
            // do nothing.
        }

        /**
         * @see java.io.Reader#read(char[], int, int)
         */
        public int read(char[] cbuf, int off, int len) throws IOException {
            if (! cBuff.hasRemaining()){
                return -1;
            } 
            // calculate actual number of bytes to request - 
            // if len is more than what's available, we'll request all thats available instead.
            int actualLength = len > cBuff.remaining() ? cBuff.remaining() : len;
            
            cBuff.get(cbuf,off,actualLength);
            return actualLength;            
        }
    }
    
    /** implemented over the byte buffer, rather than taking a view as a character buffer - as this
     * would mean that the positions, etc diverge.
     * @author Noel Winstanley nw@jb.man.ac.uk 03-Nov-2004
     *
     */
    protected class TemporaryBufferWriter extends Writer {

        /**
         * @see java.io.Writer#close()
         */
        public void close() throws IOException {
            // do nothing.
        }

        /**
         * @see java.io.Writer#flush()
         */
        public void flush() throws IOException {
            // do nothing
        }

        /**
         * @see java.io.Writer#write(char[], int, int)
         */
        public void write(char[] cbuf, int off, int len) throws IOException {
             if (len * 2 > buff.remaining()) { // char is 2 bytes
                 growBuffer();
             }
             for (int i = 0; i < len; i++) {                 
                 buff.putChar(cbuf[off+i]);
             }
        }
        
        public void write(int c) throws IOException {
            if (buff.position() +1 >= buff.limit()) { // char is 2 bytes
                growBuffer();
            }
            buff.putChar((char)c);
        }

    }
    
    protected class TemporaryBufferOutputStream extends OutputStream {
     
        public TemporaryBufferOutputStream() {
        }

        public void write(byte[] b, int off, int len) throws IOException {
            if (len > buff.remaining()) {
                growBuffer();
            }
            buff.put(b,off,len);
        }

        /**
         * @see java.io.OutputStream#write(int)
         */
        public void write(int b) throws IOException {
            if (buff.position() >= buff.limit()) {
                growBuffer();
            }
            buff.put((byte)b);
        }
        


    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[TemporaryBuffer:");
        buffer.append(" START_CAPACITY: ");
        buffer.append(START_CAPACITY);
        buffer.append(" buff: ");
        buffer.append(buff);      
        buffer.append(" writing: ");
        buffer.append(writing);
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Temporary Buffer";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return this.toString();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: TemporaryBuffer.java,v $
Revision 1.2  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.1.2.1  2004/11/05 15:45:47  nw
added static buffer impleemtation
 
*/