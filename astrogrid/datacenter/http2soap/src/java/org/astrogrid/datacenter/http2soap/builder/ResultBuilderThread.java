/*$Id: ResultBuilderThread.java,v 1.1 2003/11/11 14:43:33 nw Exp $
 * Created on 10-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.builder;

import java.nio.channels.ReadableByteChannel;

import org.astrogrid.datacenter.http2soap.Checkable;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2003
 *
 */
public class ResultBuilderThread extends Thread implements Checkable {

    /**
     * 
     */
    public ResultBuilderThread(ResultBuilder rb,ReadableByteChannel rin) {
        super();
        this.rb = rb;
        this.rin = rin;
    }
    protected ResultBuilder rb;
    protected ReadableByteChannel rin;
    protected Object result;
    protected boolean done = false;
    
    public void run() {
        try {
           this.result =  rb.build(rin);
        } catch (Throwable t) {
            err = t;
        } finally {
            this.done = true;
        }
    }
    
    public boolean isDone() {
        return this.done;
    }
    
    public Object getResult() {
        return this.result;
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
$Log: ResultBuilderThread.java,v $
Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version
 
*/