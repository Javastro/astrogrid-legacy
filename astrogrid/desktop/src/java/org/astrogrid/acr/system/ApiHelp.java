/*$Id: ApiHelp.java,v 1.2 2005/08/05 11:46:55 nw Exp $
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

import org.astrogrid.acr.NotFoundException;


/** Introspect into available methods in the ACR. returns
 * results applicable for xmlrpc only at moment
 * @todo generalized to return full type info, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Jun-2005
 *
 */
public interface ApiHelp {
    String[] listMethods();

    String[] listModules();

    String[] listComponentsOfModule(String moduleName) throws NotFoundException;

    String[] listMethodsOfComponent(String componentName) throws NotFoundException;

    String[][] methodSignature(String methodName) throws NotFoundException;

    String moduleHelp(String moduleName) throws NotFoundException;

    String componentHelp(String componentName) throws NotFoundException;

    String methodHelp(String methodName) throws NotFoundException;
}

/* 
 $Log: ApiHelp.java,v $
 Revision 1.2  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.1  2005/06/23 09:08:26  nw
 changes for 1.0.3 release
 
 */