/*$Id: RegExpConvertor.java,v 1.2 2003/11/11 14:43:33 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.response;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 * @todo problem with patterns exceeding chunk boundaries  - will fail to match
 * @todo - only returns first result? - is this the behaviour we want?
 * (or is this something to do with the resultBuilder stage of the pipeline?)
 *
 */
public class RegExpConvertor implements ResponseConvertor {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.adapter.ResponseConvertor#convertResponse(java.io.InputStream)
     */
    public void convertResponse(ReadableByteChannel cin,WritableByteChannel cout) throws IOException{
        Pattern p = Pattern.compile(exp);
        ByteBuffer bb = ByteBuffer.allocate(buffSize);
        for (int count = 0; count != -1; count =  cin.read(bb)) {
            //match on the chunk we've read in.
            // 

            bb.flip();        
            CharBuffer cb =charset.decode(bb);
            Matcher m = p.matcher(cb);
            String match = "";
            if (m.find()) {
                match = m.group(1);
            } 
            cout.write(ByteBuffer.wrap(match.getBytes()));      
            bb.clear();   // reset ready for next iteration.
        }
        cout.close(); // important, otherwise chan wil never terminate.
    }
    
    
   
 public void setEncoding(String encoding) {
     this.charset = Charset.forName(encoding);
 }
 // the encoding charset.
 protected Charset charset = Charset.forName("UTF-8");
 
    protected String exp;
    
    public void setExp(String exp) {
        this.exp = exp;
    }
    public String getExp(){
        return exp;
    }
    
    public void setBuffSize(int buffSize) {
        this.buffSize = buffSize;
    }
    public int getBuffSize() {
        return this.buffSize;
    }
    protected int buffSize = DEFAULT_BUFFER_SIZE;
    public final static int DEFAULT_BUFFER_SIZE = 1000;

}


/* 
$Log: RegExpConvertor.java,v $
Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/