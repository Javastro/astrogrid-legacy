/*$Id: GroovyTransformersTest.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 27-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import javax.xml.transform.TransformerConfigurationException;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2004
 *
 */
public class GroovyTransformersTest extends TestCase {
    protected void setUp() throws TransformerConfigurationException  {
        trans = new GroovyTransformers();
    }
    
    protected GroovyTransformers trans;

    public void testGetCompiler() throws TransformerConfigurationException {
        assertNotNull(trans.getCompiler());
    }
    
    public void testGetAnnotator() throws TransformerConfigurationException {
        assertNotNull(trans.getWorkflowAnnotator());
    }
}






/* 
$Log: GroovyTransformersTest.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.
 
*/