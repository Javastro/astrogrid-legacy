/*$Id: MockServer.java,v 1.6 2004/01/13 00:32:47 nw Exp $
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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;

import junit.framework.Assert;

import org.apache.axis.types.URI;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.axisdataserver.types.Language;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        } else if (retType.isArray() && retType.getComponentType().equals(Language.class)) {
           Language lang = new Language();
           lang.setNamespace(new URI("urn:none"));
           lang.setImplementingClass(Void.TYPE.getName());
           return new Language[]{lang}; // strange that this fails at the moment.
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
Revision 1.6  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.5.6.2  2004/01/08 09:42:26  nw
tidied imports

Revision 1.5.6.1  2004/01/07 11:50:23  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.5  2003/12/09 11:18:39  nw
fixed failing test

Revision 1.4  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.3  2003/11/19 18:48:47  nw
fixed transport bug

Revision 1.2  2003/11/18 14:27:39  nw
code to test the serialization and deserialization mechanism

Revision 1.1  2003/11/17 12:12:28  nw
first stab at mavenizing the subprojects.
 
*/