/*$Id: WorkflowLogger.java,v 1.3 2004/12/03 14:47:41 jdt Exp $
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
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/** Implememtation of standard log interface, but logs messages to workflow document.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Aug-2004
 * @modified attempted to separate stack-treaces from user-readable messages
 * @script-summary allows workflow scripts to emit log messages
 * @script-doc methods in this class allow a script to add messages to the execution record for the workflow, at different log levels.
 * At moment, debug and info messages are both logged at 'info' level. Error and fatal messages are both logged at 'error' level
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


    private void addMessage(MessageType m) {
        wf.getJobExecutionRecord().addMessage(m);
    }

    /** log a debug message to both the main record and a step record.
     * @script-doc-omit*/
    public void debug(Object arg0,StepExecutionRecord record) {
        this.logger.debug(arg0);
        MessageType m = buildMessage(arg0);
        addMessage(m);
        if (record != null) {
            record.addMessage(m);
        }
    }
    
    public void debug(Object arg0) {        
        debug(arg0,(StepExecutionRecord)null);

    }
    /** @script-doc-omit */
    public void debug(Object arg0, Throwable arg1, StepExecutionRecord record) {
        this.logger.debug(arg0, arg1);
        MessageType m = buildDebugMessage(arg0,arg1);
        addMessage(m);        
        if (record != null) {
            record.addMessage(m);
        }
    }
    

    public void debug(Object arg0, Throwable arg1) {
        debug(arg0,arg1,(StepExecutionRecord)null);
    }
    //error
    public void error(Object arg0) {
        error(arg0,(StepExecutionRecord)null);
    }
    /** @script-doc-omit */
    public void error(Object arg0,StepExecutionRecord record) {
        this.logger.error(arg0);
        MessageType m = buildMessage(arg0);
        m.setLevel(LogLevel.ERROR);
        addMessage(m);        
        if (record != null) {
            record.addMessage(m);
        }
    }
    public void error(Object arg0, Throwable arg1) {
        error(arg0,arg1,(StepExecutionRecord)null);
    }
    /** @script-doc-omit */    
    public void error(Object arg0, Throwable arg1,StepExecutionRecord record) {
        this.logger.error(arg0, arg1);
        MessageType m = buildMessage(arg0,arg1);
        m.setLevel(LogLevel.ERROR);
        addMessage(m);           
        if (record != null) {
            record.addMessage(m);
        }
    }
        //fatal
        public void fatal(Object arg0) {
            fatal(arg0,(StepExecutionRecord)null);
        }
        /** @script-doc-omit */        
        public void fatal(Object arg0,StepExecutionRecord record) {
            this.logger.fatal(arg0);
            MessageType m = buildMessage(arg0);
            m.setLevel(LogLevel.ERROR);
            addMessage(m);        
            if (record != null) {
                record.addMessage(m);
            }
        }
        public void fatal(Object arg0, Throwable arg1) {
            fatal(arg0,arg1,(StepExecutionRecord)null);
        }
        /** @script-doc-omit */        
        public void fatal(Object arg0, Throwable arg1,StepExecutionRecord record) {
            this.logger.fatal(arg0, arg1);
            MessageType m = buildMessage(arg0,arg1);
            m.setLevel(LogLevel.ERROR);
            addMessage(m);           
            if (record != null) {
                record.addMessage(m);
            }
        }
            //error
            public void warn(Object arg0) {
                warn(arg0,(StepExecutionRecord)null);
            }
            /** @script-doc-omit */            
            public void warn(Object arg0,StepExecutionRecord record) {
                this.logger.warn(arg0);
                MessageType m = buildMessage(arg0);
                m.setLevel(LogLevel.WARN);
                addMessage(m);        
                if (record != null) {
                    record.addMessage(m);
                }
            }
            public void warn(Object arg0, Throwable arg1) {
                warn(arg0,arg1,(StepExecutionRecord)null);
            }
            /** @script-doc-omit */            
            public void warn(Object arg0, Throwable arg1,StepExecutionRecord record) {
                this.logger.warn(arg0, arg1);
                MessageType m = buildMessage(arg0,arg1);
                m.setLevel(LogLevel.WARN);
                addMessage(m);           
                if (record != null) {
                    record.addMessage(m);
                }
            }
                //error
                public void info(Object arg0) {
                    info(arg0,(StepExecutionRecord)null);
                }
                /** @script-doc-omit */                
                public void info(Object arg0,StepExecutionRecord record) {
                    this.logger.info(arg0);
                    MessageType m = buildMessage(arg0);
                    m.setLevel(LogLevel.INFO);
                    addMessage(m);        
                    if (record != null) {
                        record.addMessage(m);
                    }
                }
                public void info(Object arg0, Throwable arg1) {
                    info(arg0,arg1,(StepExecutionRecord)null);
                }
                /** @script-doc-omit */                
                public void info(Object arg0, Throwable arg1,StepExecutionRecord record) {
                    this.logger.info(arg0, arg1);
                    MessageType m = buildMessage(arg0,arg1);
                    m.setLevel(LogLevel.INFO);
                    addMessage(m);           
                    if (record != null) {
                        record.addMessage(m);
                    }
                }
        // message builders
    protected MessageType buildMessage(Object content) {
        MessageType  message = new MessageType();
        message.setContent(content.toString());
        message.setSource("JES");
        message.setPhase(ExecutionPhase.RUNNING);
        message.setTimestamp(new Date())   ; 
        return message;
    }
    
    private MessageType buildDebugMessage(Object content) {
        MessageType  message = new MessageType();
        message.setContent(content.toString());
        message.setSource("JES");
        message.setPhase(ExecutionPhase.RUNNING);
        message.setTimestamp(new Date());
        message.setLevel(LogLevel.INFO);// would like debug here really.        
        return message;
    }    
    
    /** build a user-level message, containinig a summary of the exception
     * @param t
     * @return
     */
    private MessageType buildMessage(Object msg,Throwable t) {
        StringBuffer buff = new StringBuffer();
        
            buff
           .append(msg)
           .append("\n")
           .append(JesUtil.getMessageChain(t));
        return buildMessage(buff.toString());

    }
    
    /** build a debug level message, containing a full stack trace */
    private MessageType buildDebugMessage(Object msg,Throwable t) {
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
        return buildDebugMessage(buff);  

    }
}


/* 
$Log: WorkflowLogger.java,v $
Revision 1.3  2004/12/03 14:47:41  jdt
Merges from workflow-nww-776

Revision 1.2.2.1  2004/12/01 21:49:16  nw
script documentation

Revision 1.2  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.1.58.1  2004/11/25 23:34:34  nw
improved error messages reported from jes

Revision 1.1  2004/08/18 21:50:15  nw
improved error propagation and reporting.
messages are now logged to workflow document
 
*/