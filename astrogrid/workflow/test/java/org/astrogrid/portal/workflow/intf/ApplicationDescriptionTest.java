/*$Id: ApplicationDescriptionTest.java,v 1.4 2004/03/17 00:32:36 nw Exp $
 * Created on 10-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.portal.workflow.impl.FileApplicationRegistry;
import org.astrogrid.portal.workflow.impl.FileApplicationRegistryTest;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import java.net.URL;

import junit.framework.TestCase;

/** test the populate and validate methods of application description
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2004
 *
 */
public class ApplicationDescriptionTest extends TestCase {
    /**
     * Constructor for ApplicationDescriptionTest.
     * @param arg0
     */
    public ApplicationDescriptionTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        // load a registry, get first tool out of it.
        URL url = FileApplicationRegistryTest.TEST_DOCUMENT_URL; 
        reg = new FileApplicationRegistry(url);
        assertNotNull(reg);
        descr = reg.getDescriptionFor("SExtractor");
    }
    protected ApplicationRegistry reg;
    protected ApplicationDescription descr;
    
    public void testGetInterfaces() {
        assertNotNull(descr.getInterfaces());
    }
    
    public void testGetName() {
        assertNotNull(descr.getName());
    } 
    
    public void testGetParameters() {
        assertNotNull(descr.getParameters());
    }
    
    public void testCreateToolFromDescription() throws Exception{
        Tool t = descr.createToolFromDefaultInterface();
        
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(t,doc);
        XMLUtils.PrettyDocumentToStream(doc,System.out);
        
        //System.out.println(t.toString());
        Interface intf = descr.getInterfaces().get_interface(0);
        assertNotNull(t);
        assertEquals(t.getName(),descr.getName());
        assertEquals(t.getInterface(),intf.getName());
        // more here.
        // check its got some inputs and outputs.
        ParameterValue[] params = t.getInput().getParameter();
        assertNotNull(params);
        assertTrue(params.length > 0);
        // check each param
        for (int i = 0; i < params.length; i++) {
            ParameterValue paramVal = params[i];
            assertNotNull(paramVal.getName());
            assertNotNull(paramVal.getValue());
            assertNotNull(paramVal.getType());
            // check we can resolve each paramVal.
            BaseParameterDefinition def = descr.getDefinitionForValue(paramVal,intf);
        }
      
    }
    
    public void testValidate() throws Exception {
        Tool t = descr.createToolFromDefaultInterface();
        descr.validate(t);
    }
    
    public void testValidateInvalidTool() throws Exception {
        Tool t = descr.createToolFromDefaultInterface();
        ParameterValue val = t.getInput().getParameter(0);
        t.getInput().removeParameter(val);
        try {
            descr.validate(t);
            fail("expected to barf");
        } catch (ToolValidationException e) {
            // ok.
        }
    }
    
        
}


/* 
$Log: ApplicationDescriptionTest.java,v $
Revision 1.4  2004/03/17 00:32:36  nw
updated to fit with altererd parameter object model

Revision 1.3  2004/03/12 14:53:51  nw
added testing of default values

Revision 1.2  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:37:52  nw
tests for impls
 
*/