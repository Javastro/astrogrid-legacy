/*$Id: ApiHelp.java,v 1.1 2005/06/23 09:08:26 nw Exp $
 * Created on 23-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.system;

import org.apache.xmlrpc.XmlRpcException;

import java.util.List;

/** Introspect into available methods in the ACR. returns
 * results applicable for xmlrpc only at moment
 * @todo generalized to return full type info, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Jun-2005
 *
 */
public interface ApiHelp {
    List listMethods();

    List listModules();

    List listComponentsOfModule(String moduleName) throws XmlRpcException;

    List listMethodsOfComponent(String componentName) throws XmlRpcException;

    List methodSignature(String methodName) throws XmlRpcException;

    String moduleHelp(String moduleName) throws XmlRpcException;

    String componentHelp(String componentName) throws XmlRpcException;

    String methodHelp(String methodName) throws XmlRpcException;
}

/* 
 $Log: ApiHelp.java,v $
 Revision 1.1  2005/06/23 09:08:26  nw
 changes for 1.0.3 release
 
 */