/*$Id: MockServer.java,v 1.3 2003/11/19 18:48:47 nw Exp $
 * Created on 16-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.axisdataserver;
import java.lang.reflect.*;
import java.rmi.RemoteException;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import junit.framework.Assert;

/** Mock Server implementation
 * Given an interface, will provide an implementation that checks parameters passed in are non-null,
 * and returns non-null results.
 * Implemented using java.lang.reflect.Proxy;
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Nov-2003
 *
 */
public class MockServer implements InvocationHandler {

    private MockServer(Class iface) {
        this.iface = iface;
    }
    protected Class iface;
    
    public static Object createMockServer(Class iface) {
        return Proxy.newProxyInstance(MockServer.class.getClassLoader(),new Class[]{iface},new MockServer(iface));
    }

    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // check each of object args is not-null and of correct type.
        Class[] formalParams = method.getParameterTypes();
        Assert.assertEquals(formalParams.length,args.length);
        for (int i = 0; i < args.length; i++) {
            //Assert.assertNotNull(args[i]);
            //Assert.assertTrue(formalParams[i].isAssignableFrom(args[i].getClass()));
            // can't use assertions - need to throw a remote exception instead.
            if (args[i] == null) {
                throw new RemoteException("param " + formalParams[i].getName() + " is null");
            }   
            if(! formalParams[i].isAssignableFrom(args[i].getClass()) ) {
                throw new RemoteException("type mismatch " 
                    + args[i].getClass().getName() 
                    + ", " + formalParams[i].getName() 
                    + " " + formalParams[i].getClass().getName());
            }                                 
        }
        Class retType = method.getReturnType();
        if (retType.equals(Void.TYPE)) {
            return null;
        }else  if (! retType.isInterface()) {
            return retType.newInstance();
        } else if ( retType.equals(Element.class)) {
            Document doc = XMLUtils.newDocument();
            return doc.getDocumentElement();
        } else if ( retType.equals(Document.class)) {
            return XMLUtils.newDocument();
        } else {
            throw new RemoteException("Don't know how to create return type " + retType.getName() + " for method " + method.getName());
        }
        
    }

}


/* 
$Log: MockServer.java,v $
Revision 1.3  2003/11/19 18:48:47  nw
fixed transport bug

Revision 1.2  2003/11/18 14:27:39  nw
code to test the serialization and deserialization mechanism

Revision 1.1  2003/11/17 12:12:28  nw
first stab at mavenizing the subprojects.
 
*/