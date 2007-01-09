/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.acr.ui.WorkflowBuilder;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Jun 6, 20062:30:04 AM
 */
public class WorkflowBuilderUISystemTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        builder = (WorkflowBuilder)reg.getService(WorkflowBuilder.class);
        assertNotNull(builder);
    } 

    protected void tearDown() throws Exception {
    	super.tearDown();
    	builder = null;
    }
    protected WorkflowBuilder builder;
	public void testShow() {
		builder.show();
	}
	
	public void testHide() {
		builder.hide();
	}
	
	public void testShowTranscript() {
		//@todo
	}
    public static Test suite() {
        return new ARTestSetup(new TestSuite(WorkflowBuilderUISystemTest.class),true);
    }
}
