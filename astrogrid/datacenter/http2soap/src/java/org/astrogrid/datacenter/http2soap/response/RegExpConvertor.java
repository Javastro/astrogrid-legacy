/*$Id: RegExpConvertor.java,v 1.1 2003/10/12 21:39:34 nw Exp $
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.astrogrid.datacenter.http2soap.ResponseConvertor;

/** convertor that maps a response by applying a regular expression.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class RegExpConvertor implements ResponseConvertor {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.adapter.ResponseConvertor#convertResponse(java.io.InputStream)
     */
    public void convertResponse(ReadableByteChannel cin,WritableByteChannel cout) throws IOException{
        Pattern p = Pattern.compile(exp);
        ByteBuffer bb = ByteBuffer.allocate(buffSize);
        cin.read(bb);
        bb.flip();        
        CharBuffer cb = bb.asCharBuffer();
        Matcher m = p.matcher(cb);
        String match = "";
        if (m.find()) {
            match = m.group();
        } 
        bb.clear();
        cb.clear();
        cb.put(match);
        cout.write(bb); // works as cb is a view over bb.        
    }
    
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
    protected int buffSize;

}


/* 
$Log: RegExpConvertor.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/