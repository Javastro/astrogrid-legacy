/*$Id: CEATargetIndicator.java,v 1.1 2004/07/13 17:11:09 nw Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.service.v06;

import org.astrogrid.datacenter.queriers.TargetIndicator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/** Indirection, so cea can manage the results of a query itself.
 * sets up a piped reader-writer pair - so results from querier are piped into cea system,
 * where they can be disposed of using the cea library framework.
 * @todo check pipe limits, etc - may be better to use something from the concurrent package..
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class CEATargetIndicator extends TargetIndicator {

    /** need to have a factory method - arse */
    public static CEATargetIndicator newInstance() throws IOException {
        PipedOutputStream os = new PipedOutputStream();
        return new CEATargetIndicator(os);
    }

    private CEATargetIndicator(PipedOutputStream out) throws IOException {       
        super(new OutputStreamWriter(out));
        this.is = new PipedInputStream(out);
    }
    protected final PipedInputStream is;
    public InputStream getStream() {
        return is;
    }
    
    
}


/* 
$Log: CEATargetIndicator.java,v $
Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/