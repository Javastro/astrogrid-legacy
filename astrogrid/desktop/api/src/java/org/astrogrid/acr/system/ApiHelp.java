/*$Id: ApiHelp.java,v 1.3 2006/02/02 14:19:47 nw Exp $
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


/** Access documentation on the functions available in the ACR..
 * 
 * Returns type information  applicable for xmlrpc only at moment. For JavaRMI consult the javadoc (available from the workbench in-program help) 
 * @todo generalize to return full type info, etc.
 * @service system.apihelp
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Jun-2005
 *
 */
public interface ApiHelp {
    /**List all methods available in the acr
     * @return array of method names in form <tt><i>module</i>.<i>component</i>.<i>method</i></tt>
     */
    String[] listMethods();

    /** List all modules available in the acr
     * @return array of module names
     */
    String[] listModules();

    /** List the components / services within a module
     * @param moduleName
     * @return list of service names in form<tt><i>module</i>.<i>component</i></tt>
     * @throws NotFoundException if the module does not exist
     */
    String[] listComponentsOfModule(String moduleName) throws NotFoundException;

    /** Lst the methods of a service
     * @param componentName  name of the service in form <tt><i>module</i>.<i>component</i></tt>
     * @return list of method names  in form <tt><i>module</i>.<i>component</i>.<i>method</i></tt>
     * @throws NotFoundException if the service does not exist.
     */
    String[] listMethodsOfComponent(String componentName) throws NotFoundException;

    /** Access the XMLRPC type for this signature
     * @param methodName  name of the method in form <tt><i>module</i>.<i>component</i>.<i>method</i></tt>
     * @return an array of signatures for this method (allows for overloading of method name).
     * Overloading never occurs in ACR, but this form is necessary for compatability with XMLRPC standard.
     * So result will always be an array of length 1. First item in the array is an array of strings - first item in <i>this</i>
     * array is the return type; any following items are parameter types.
     * @throws NotFoundException if the method does not exist.
     */
    String[][] methodSignature(String methodName) throws NotFoundException;

    /** Access help for a module
     * @param moduleName      
     * @return documentation for this module
     * @throws NotFoundException if this module does not exist.
     */
    String moduleHelp(String moduleName) throws NotFoundException;

    /** Acccess help for a component / service
     * @param componentName name of the service in form <tt><i>module</i>.<i>component</i></tt>
     * @return documentation for this component
     * @throws NotFoundException if this component does not exist
     */
    String componentHelp(String componentName) throws NotFoundException;

    /** Access help for a method
     * @param methodName - name of the method in form <tt><i>module</i>.<i>component</i>.<i>method</i></tt>
     * @return documentation for this method
     * @throws NotFoundException if this method does not exist
     */
    String methodHelp(String methodName) throws NotFoundException;
    
    /** Return the class name of the service iterface for a particular component
     * @param componentName - service name of form <tt><i>module</i>.<i>component</i></tt>
     * @return class name of associated service interface
     * @throws NotFoundException if the componentName is not a service in this acr.
     */
    String interfaceClassName(String componentName) throws NotFoundException;
}

/* 
 $Log: ApiHelp.java,v $
 Revision 1.3  2006/02/02 14:19:47  nw
 fixed up documentation.

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.2  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.1  2005/06/23 09:08:26  nw
 changes for 1.0.3 release
 
 */