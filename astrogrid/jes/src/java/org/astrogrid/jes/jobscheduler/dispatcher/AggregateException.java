/*$Id: AggregateException.java,v 1.1 2005/04/01 10:24:19 nw Exp $
 * Created on 01-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import java.io.PrintStream;
import java.io.PrintWriter;

/** An exception that aggregates a set of causal exceptions.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Apr-2005
 *
 */
public class AggregateException  extends Exception {

    public String getLocalizedMessage() {
        return this.getMessage();
    }
    public String getMessage() {
        StringBuffer msg = new StringBuffer();
        msg.append("Multiple Causes:\n");
        for (int i = 0; i < causes.length; i++) {
            msg.append(causes[i].getMessage());
            msg.append("\n");
        }
        return msg.toString();
    }

    public void printStackTrace(PrintStream s) {
        //synchronized(s) {
            s.println(this);
            for (int i = 0; i < causes.length; i++) {
                causes[i].printStackTrace(s);
            }
        //}
    }
    public void printStackTrace(PrintWriter s) {
        //synchronized(s) {
        s.println(this);
        for (int i = 0; i < causes.length; i++) {
            causes[i].printStackTrace(s);
        }
    //}
    }
    /** Construct a new AggregateException
     * 
     */
    public AggregateException(Throwable[] causes) {
        super();
        this.causes = causes;
    }
    
    protected final Throwable[] causes;

}


/* 
$Log: AggregateException.java,v $
Revision 1.1  2005/04/01 10:24:19  nw
improved logging of cea connect failures
 
*/