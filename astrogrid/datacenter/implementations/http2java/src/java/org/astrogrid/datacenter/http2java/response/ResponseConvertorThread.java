/*$Id: ResponseConvertorThread.java,v 1.1 2003/11/18 11:48:14 nw Exp $
 * Created on 07-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.response;

import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.astrogrid.datacenter.http2java.Checkable;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Oct-2003
 *
 */
public class ResponseConvertorThread extends Thread implements Checkable {


    public ResponseConvertorThread(ReadableByteChannel cin,WritableByteChannel cout,ResponseConvertor rc) {
        this.cin = cin;
        this.cout = cout;
        this.rc = rc;
    }
    
    protected ReadableByteChannel cin;
    protected WritableByteChannel cout;
    protected ResponseConvertor rc;

    public void run() {
        try {
            rc.convertResponse(cin,cout);
        } catch (Throwable t) {
            this.err = t;
        }        
    }
    
    protected Throwable err;
    public Throwable getError() {
        return err;
    }
    public boolean hasError() {
        return err != null;
    }
    

}


/* 
$Log: ResponseConvertorThread.java,v $
Revision 1.1  2003/11/18 11:48:14  nw
mavenized http2java

Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/