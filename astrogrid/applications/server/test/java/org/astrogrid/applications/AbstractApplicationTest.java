/*$Id: AbstractApplicationTest.java,v 1.7 2007/09/28 18:03:36 clq2 Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapterException;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;


import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import junit.framework.TestCase;

/** Test to exercise the basic functionality of abstract appliction
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class AbstractApplicationTest extends TestCase {
    /**
     * Constructor for AbstractApplicationTest.
     * @param arg0
     */
    public AbstractApplicationTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ids = new AbstractApplication.IDs() {

            public String getId() {
                return "id";
            }

            public String getJobStepId() {
                return "jobStepId";
            }
            public User getUser() {
                return new User();
            }
        };
       
     ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(null,null,null);
    description = new AbstractApplicationDescription(env) {
        public Application initializeApplication(String jobStepID, User user, Tool tool) throws Exception {
            return null;
        }        
    };
    
    BaseParameterDescription x = new BaseParameterDescription();
    x.setName("x");
    BaseParameterDescription y = new BaseParameterDescription();
    y.setName("y");
    
    description.addParameterDescription(x);
    description.addParameterDescription(y);
    
    BaseApplicationInterface interf = new BaseApplicationInterface("onlyInterface",description);
    description.addInterface(interf);
    interf.addInputParameter("x");
    interf.addOutputParameter("y");    
    
    //
    tool = new Tool();
    tool.setInterface("onlyInterface");
    Input input = new Input();
    tool.setInput(input);    
    Output output = new Output();
    tool.setOutput(output);
    ParameterValue xVal = new ParameterValue();
    xVal.setName(x.getName());
    xVal.setValue("fred");
    input.addParameter(xVal);
    
    ParameterValue yVal = new ParameterValue();
    yVal.setName(y.getName());
    yVal.setValue("barney");
    output.addParameter(yVal);
    
    //
    app = new AbstractApplication(ids,tool,interf,null) {

            public ResultListType retrieveResult() {
                return null;
            }

            public Runnable createExecutionTask() throws CeaException {
                return null;
            }
        };
    obs = new TestObserver();           
    app.addObserver(obs);
    }
    protected AbstractApplication.IDs ids;    
    protected AbstractApplicationDescription description;
    protected Tool tool;
    protected AbstractApplication app;
    protected TestObserver obs; 
    
    public void testGetID() {
        assertEquals(app.getID(),ids.getId());
    }
    public void testGetApplicationInterface() throws Exception{
        ApplicationInterface intf = app.getApplicationInterface();
        assertEquals(intf.getName(),tool.getInterface());
    }
    public void testGetApplicationDescription() {
        ApplicationDescription descr = app.getApplicationDescription();
        assertSame(description,descr);
    }



    public void testGetJobStepID() {
        assertEquals(ids.getJobStepId(),app.getJobStepID());
    }
    public void testStatus() {
        assertEquals(Status.NEW,app.getStatus()); // fresh apps have new status.
        app.setStatus(Status.RUNNING);
        assertEquals(Status.RUNNING,app.getStatus());
        assertTrue(obs.called);
        assertNotNull(obs.arg);
        assertEquals(Status.RUNNING,obs.arg);
    }

    public void testFindInputParameter() {
        ParameterValue x1 =  app.findParameter("x");
        assertNotNull(x1);
        assertEquals("x",x1.getName());       
    }

    public void testFindOutputParameter() {
        ParameterValue y1 =  app.findParameter("y");
        assertNotNull(y1);
        assertEquals("y",y1.getName());       
    }    
    
    public void testFindUnknownParameter() {
        assertNull(app.findParameter("unknown"));
    }
    
    public void testParameterValues() {
        Iterator i = app.parameterValues();
        assertNotNull(i);
        assertTrue(i.hasNext());
        assertTrue(i.next() instanceof ParameterValue);
        assertTrue(i.hasNext());
        assertTrue(i.next() instanceof ParameterValue);
    }
    public void testCreateAdaptersAndFindParameterAdapter() throws Exception {
        assertFalse(app.inputParameterAdapters().hasNext());
        assertFalse(app.outputParameterAdapters().hasNext());        
        app.createAdapters();
        // check each now has one adapater.
        Iterator i = app.inputParameterAdapters();
        assertTrue(i.hasNext());
        i.next();
        assertFalse(i.hasNext());
        i = app.outputParameterAdapters();
        assertTrue(i.hasNext());
        i.next();
        assertFalse(i.hasNext());           
        // now find some adapters.
        ParameterAdapter pA = app.findParameterAdapter("y");
        assertNotNull(pA);
        assertEquals("y",pA.getWrappedParameter().getName());
    }
    
    public void testParameterAdapterIterators() throws ParameterDescriptionNotFoundException, ParameterAdapterException {
        app.createAdapters();
        Iterator all = app.parameterAdapters();
        Iterator inputs = app.inputParameterAdapters();
        Iterator outputs = app.outputParameterAdapters();
        assertTrue(all.hasNext());
        assertTrue(inputs.hasNext());
        assertTrue(outputs.hasNext());
        while(all.hasNext()) {
            if (inputs.hasNext()) {
                assertEquals(inputs.next(),all.next());
            } else {
                assertEquals(outputs.next(),all.next());
            }
        }
        assertFalse(inputs.hasNext());
        assertFalse(outputs.hasNext());
    }
    
    public void testCreateTemplateMessage() {
        MessageType mt = app.createTemplateMessage();
        assertNotNull(mt);
        assertEquals(mt.getPhase(),app.getStatus().toExecutionPhase());
    }
    
    public void testReportError() {
        app.reportError("test error");
        assertTrue(obs.called);
        assertNotNull(obs.arg);  
        System.out.println(obs.arg.getClass().getName());
        MessageType mt = (MessageType)obs.arg;
        assertEquals(LogLevel.ERROR,mt.getLevel());
        assertEquals("test error",mt.getContent());
        assertEquals(Status.ERROR,app.getStatus());
        
    }
    
    public void testReportWarning() {
        Status orig = app.getStatus();
        app.reportWarning("test warning");
        assertTrue(obs.called);
        assertNotNull(obs.arg);  
        assertTrue(obs.arg instanceof MessageType);
        MessageType mt = (MessageType)obs.arg;
        assertEquals(LogLevel.WARN,mt.getLevel());
        assertEquals("test warning",mt.getContent());
        assertEquals(orig,app.getStatus());      // app status remains unchanged   
    }
    
    public void testReportMessage() {
        Status orig = app.getStatus();
        app.reportMessage("test warning");
        assertTrue(obs.called);
        assertNotNull(obs.arg);  
        assertTrue(obs.arg instanceof MessageType);
        MessageType mt = (MessageType)obs.arg;
        assertEquals(LogLevel.INFO,mt.getLevel());
        assertEquals("test warning",mt.getContent());
        assertEquals(orig,app.getStatus());    // app status remains unchanged   
       
    }
       
    
    public void testReportErrorException() {
        Exception e = new Exception();
        app.reportError("test error",e);
        assertTrue(obs.called);
        assertNotNull(obs.arg);  
        System.out.println(obs.arg.getClass().getName());
        MessageType mt = (MessageType)obs.arg;
        assertEquals(LogLevel.ERROR,mt.getLevel());
        assertTrue(mt.getContent().startsWith("test error"));
        assertEquals(Status.ERROR,app.getStatus());        
    }
    
    public void testReportWarningException() {
        Status orig = app.getStatus();
        Exception e= new Exception();
        app.reportWarning("test warning",e);
        assertTrue(obs.called);
        assertNotNull(obs.arg);  
        assertTrue(obs.arg instanceof MessageType);
        MessageType mt = (MessageType)obs.arg;
        assertEquals(LogLevel.WARN,mt.getLevel());
        assertTrue(mt.getContent().startsWith("test warning"));
        assertEquals(orig,app.getStatus());      // app status remains unchanged           
    }
    
    public void testEnqueue() throws Exception {
      app.enqueue();
      assertEquals(Status.QUEUED, app.getStatus());
    }

    protected static class TestObserver implements Observer {
        public boolean called;
        public Object arg;
        /**
         * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
         */
        public void update(Observable o, Object arg) {
            if (!called) { // only preserve first message
                called = true;
                this.arg = arg;
            }
        }
    }
    
}


/* 
$Log: AbstractApplicationTest.java,v $
Revision 1.7  2007/09/28 18:03:36  clq2
apps_gtr_2303

Revision 1.6.82.1  2007/09/25 08:29:03  gtr
I added a test for enqueue(). The app in setUp() now implements createExecutionTask() instead of execute().

Revision 1.6  2005/08/10 17:45:10  clq2
cea-server-nww-improve-tests

Revision 1.5.88.1  2005/07/21 18:12:38  nw
fixed up tests - got all passing, improved coverage a little.
still could do with testing the java apps.

Revision 1.5  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.4.70.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.4  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.3  2004/07/22 16:34:48  nw
cleaned up application / parameter adapter interface.

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/