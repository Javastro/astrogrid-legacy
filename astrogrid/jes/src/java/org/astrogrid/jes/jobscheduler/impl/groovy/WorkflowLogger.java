/*$Id: WorkflowLogger.java,v 1.1 2004/08/18 21:50:15 nw Exp $
 * Created on 18-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/** Implememtation of standard log interface, but logs messages to workflow document.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Aug-2004
 *
 */
public class WorkflowLogger  {


    protected final Workflow wf;
    protected final Log logger;
    /** Construct a new WorkflowLogger
     * 
     */
    public WorkflowLogger(Workflow wf) {
        this.wf = wf;
        logger = LogFactory.getLog("Workflow " + wf.getName());
    }


    protected void addMessage(MessageType m) {
        wf.getJobExecutionRecord().addMessage(m);
    }

    public void debug(Object arg0) {        
        this.logger.debug(arg0);
        MessageType m = buildMessage(arg0);
        m.setLevel(LogLevel.INFO);
        addMessage(m);
    }
    public void debug(Object arg0, Throwable arg1) {
        this.logger.debug(arg0, arg1);
        MessageType m = buildMessage(arg0,arg1);
        m.setLevel(LogLevel.INFO);
        addMessage(m);        
    }
    public void error(Object arg0) {
        this.logger.error(arg0);
        MessageType m = buildMessage(arg0);
        m.setLevel(LogLevel.ERROR);
        addMessage(m);        
    }
    public void error(Object arg0, Throwable arg1) {
        this.logger.error(arg0, arg1);
        MessageType m = buildMessage(arg0,arg1);
        m.setLevel(LogLevel.ERROR);
        addMessage(m);           
    }
    public void fatal(Object arg0) {
        this.logger.fatal(arg0);
        MessageType m = buildMessage(arg0);
        m.setLevel(LogLevel.ERROR);
        addMessage(m);           
    }
    public void fatal(Object arg0, Throwable arg1) {
        this.logger.fatal(arg0, arg1);
        MessageType m = buildMessage(arg0,arg1);
        m.setLevel(LogLevel.ERROR);
        addMessage(m);                 
    }
    public void info(Object arg0) {
        this.logger.info(arg0);
        MessageType m = buildMessage(arg0);
        m.setLevel(LogLevel.INFO);
        addMessage(m);        
    }
    public void info(Object arg0, Throwable arg1) {
        this.logger.info(arg0, arg1);
        MessageType m = buildMessage(arg0,arg1);
        m.setLevel(LogLevel.INFO);
        addMessage(m);        
    }

    public void warn(Object arg0) {
        this.logger.warn(arg0);
        MessageType m = buildMessage(arg0);
        m.setLevel(LogLevel.WARN);
        addMessage(m);        
    }
    public void warn(Object arg0, Throwable arg1) {
        this.logger.warn(arg0, arg1);
        MessageType m = buildMessage(arg0,arg1);
        m.setLevel(LogLevel.WARN);
        addMessage(m);        
    }
    
    
    protected MessageType buildMessage(Object content) {
        MessageType  message = new MessageType();
        message.setContent(content.toString());
        message.setSource("JES");
        message.setPhase(ExecutionPhase.RUNNING);
        message.setTimestamp(new Date());
        return message;
    }
    
    /**
     * @param t
     * @return
     */
    protected MessageType buildMessage(Object msg,Throwable t) {
        StringBuffer buff = new StringBuffer();
        
            buff
           .append(msg)
           .append('\n')
           .append("Failed: ")
           .append( t.getClass().getName())
           .append('\n')
           .append( t.getMessage())
           .append('\n');
        StringWriter writer = new StringWriter();                            
           t.printStackTrace(new PrintWriter(writer));
         buff.append(writer.toString());
        return buildMessage(buff);  

    }

}


/* 
$Log: WorkflowLogger.java,v $
Revision 1.1  2004/08/18 21:50:15  nw
improved error propagation and reporting.
messages are now logged to workflow document
 
*/