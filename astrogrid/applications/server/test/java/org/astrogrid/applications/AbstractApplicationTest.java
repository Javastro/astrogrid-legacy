/*$Id: AbstractApplicationTest.java,v 1.4 2004/07/26 12:07:38 nw Exp $
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
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.Iterator;

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
       
     ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(null,null);
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

            public boolean execute() throws CeaException {
                return false;
            }
        };
    }
    protected AbstractApplication.IDs ids;    
    protected AbstractApplicationDescription description;
    protected Tool tool;
    protected AbstractApplication app;
    
    
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

}


/* 
$Log: AbstractApplicationTest.java,v $
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