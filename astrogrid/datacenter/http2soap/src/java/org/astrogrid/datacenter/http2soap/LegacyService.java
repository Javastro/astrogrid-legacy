/*$Id: LegacyService.java,v 1.2 2003/11/11 14:43:33 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/** Generates java interface to a legacy web service.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class LegacyService implements InvocationHandler {
  
 /** private constructor
  * 
  * @param configFile stream containing xml definition of the legacy web service
  * @throws LegacyServiceException if can't initialize correctly.
  */
    protected LegacyService(InputStream configFile) throws LegacyServiceException {
        try {
            ServiceStoreBuilder builder = new ServiceStoreBuilder();
            store = builder.buildStore(configFile);
        } 
        catch (ParserConfigurationException e) {
            throw new LegacyServiceException("Could not initialize XML parser",e);
        }
        catch (SAXException e) {
            throw new LegacyServiceException("Could not parse configuration",e);
        }
        catch (IOException e) {
            throw new LegacyServiceException("Could not read configuration stream",e);
        }
    }
    
    protected LegacyWebMethod.Store store;
    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws LegacyServiceException {
        try {
        // find the service with the same name as the method called.
        LegacyWebMethod webMethod = store.lookupService(method.getName());
        if (webMethod == null) {
            throw new IllegalArgumentException("attempt to call method " + method.getName() + " failed - corresponding web method could not be found");
        }
        return webMethod.doCall(args);
        } catch (IOException e) { 
            throw new LegacyServiceException("Error occurred communicating to legacy web service",e);
        } 
    }
    
    /** Generate a java binding to a legacy web service.
     * 
     * @param iface - interface the service will implement
     * @param config - stream containing xml description of the web service methods.
     * @return
     */
    public static Object createImplementation(Class iface,InputStream config) throws LegacyServiceException {
        return Proxy.newProxyInstance(LegacyService.class.getClassLoader(),new Class[]{iface},new LegacyService(config));
    }



}


/* 
$Log: LegacyService.java,v $
Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/