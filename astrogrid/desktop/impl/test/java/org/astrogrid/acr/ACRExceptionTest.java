/*$Id: ACRExceptionTest.java,v 1.2 2006/04/18 23:25:47 nw Exp $
 * Created on 29-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;

import java.io.IOException;
import java.rmi.UnexpectedException;

import junit.framework.TestCase;

/** tests the cause-exception converting behaviour of acr exception.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2005
 *
 */
public class ACRExceptionTest extends TestCase {

    
    public void testPlain() {
        ACRException e = new ACRException("message");
        assertNotNull(e);
        assertNull(e.getCause());
    }
    
    public void testJavaCause() {
        Exception cause = new IOException("a message");
        ACRException e = new ACRException("message",cause);
        assertNotNull(e.getCause());
        assertNull(e.getCause().getCause());
        assertSame(cause,e.getCause());
    }
    
    public void testUnknownCause() {
        Exception cause = new WorkflowInterfaceException("a message");
        ACRException e = new ACRException("message",cause);
        assertNotNull(e.getCause());
        assertNull(e.getCause().getCause());
        assertNotSame(cause,e.getCause());
        assertTrue(e.getCause().getMessage().indexOf(cause.getMessage()) != -1);
        assertTrue(e.getCause().getMessage().indexOf(cause.getClass().getName()) != -1);
        //assertEquals(cause.getStackTrace(),e.getCause().getStackTrace());
    }
    
    public void testJavaUnknownCause() {
        Exception innermost = new WorkflowInterfaceException(" a message");
        Exception cause = new UnexpectedException("msg",innermost);
        ACRException e = new ACRException("wibble",cause);
        assertNotNull(e.getCause());
        assertNotNull(e.getCause().getCause());
        assertNull(e.getCause().getCause().getCause());
        assertNotSame(cause,e.getCause());
        assertEquals(cause.getClass(),e.getCause().getClass());
        assertNotSame(innermost,e.getCause().getCause());
        assertEquals(Exception.class,e.getCause().getCause().getClass());
    }
    
    public void testUnknownJavaCause() {
        Exception innermost = new UnexpectedException(" a message");
        Exception cause = new WorkflowInterfaceException("msg",innermost);
        ACRException e = new ACRException("wibble",cause);
        assertNotNull(e.getCause());
        assertNotNull(e.getCause().getCause());
        assertNull(e.getCause().getCause().getCause());
        assertNotSame(cause,e.getCause());
        assertEquals(Exception.class,e.getCause().getClass());
        assertSame(innermost,e.getCause().getCause());  
    }
    
    
    public void testUnknownUnknownCause() {
        Exception innermost = new WorkflowInterfaceException(" a message");
        Exception cause = new WorkflowInterfaceException("msg",innermost);
        ACRException e = new ACRException("wibble",cause);
        assertNotNull(e.getCause());
        assertNotNull(e.getCause().getCause());
        assertNull(e.getCause().getCause().getCause());
        assertNotSame(cause,e.getCause());
        assertNotSame(innermost,e.getCause().getCause());
        assertEquals(Exception.class,e.getCause().getClass());
        assertEquals(Exception.class,e.getCause().getCause().getClass());
    }
    
    public void testJavaJavaCause() {
        Exception innermost = new UnexpectedException(" a message");
        Exception cause = new UnexpectedException("msg",innermost);
        ACRException e = new ACRException("wibble",cause);
        assertNotNull(e.getCause());
        assertNotNull(e.getCause().getCause());
        assertNull(e.getCause().getCause().getCause());
        assertSame(cause,e.getCause());
        assertSame(innermost,e.getCause().getCause());
    }
    
    
}


/* 
$Log: ACRExceptionTest.java,v $
Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:01  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/