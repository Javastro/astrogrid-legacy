/*$Id: ApiHelp.java,v 1.6 2008/09/25 16:02:03 nw Exp $
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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ModuleDescriptor;


/** AR Service: Documents and provides access to component and functions of AstroRuntime.
 * 
 * This service provides methods to get general metadata and XMLRPC-specific type information.
 * It also provides a technique for dynamically calling AR methods, which is useful in some situations.
 * <p />
 * The diagram below shows the structure of metadata returned from the {@link #listModuleDescriptors()} function - which returns a 
 * tree of metadata that describes all modules, components and functions provided by the AstroRuntime.
 * <br />
 * {@textdiagram apihelp 
   +-----------+
   |           |
   |  ApiHelp  |-------+
   |           |       :listModuleDecriptors()
   +-----------+       |
                       v
              +------------------+
              | ModuleDescriptor |------------------+
              +--------+---------+                  |
                       | 1..*                       v
            +----------+----------+           +--------------+
            | ComponentDescriptor +---------->|              |
            +----------+----------+           |  (Abstract)  |
                       | 1..*            +--->|  Descriptor  |
              +--------+---------+       |    |              |
              | MethodDescriptor |-------+    +--------------+
              +--------+---------+                  ^
                       | 1..*                       |
              +--------+--------+                   |
              | ValueDescriptor |-------------------+
              +-----------------+

 * 
 * }
 * @service system.apihelp
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 23-Jun-2005
 *
 */
public interface ApiHelp {
	
	/** List all the module descriptors available in the AstroRuntime. 

	 * */
	ModuleDescriptor[] listModuleDescriptors();
	
	
    /**List all methods available in the AstroRuntime.
     * @return array of method names in form <tt><i>module</i>.<i>component</i>.<i>method</i></tt>
     */
    String[] listMethods();

    /** List all modules available in the AstroRuntime.
     * @return array of module names
     */
    String[] listModules();

    /** List the components / services within a module
     * @param moduleName
     * @return list of service names in form<tt><i>module</i>.<i>component</i></tt>
     * @throws NotFoundException if the module does not exist
     */
    String[] listComponentsOfModule(String moduleName) throws NotFoundException;

    /** Lst the methods (functions) of a service.
     * @param componentName  name of the service in form <tt><i>module</i>.<i>component</i></tt>
     * @return list of method names  in form <tt><i>module</i>.<i>component</i>.<i>method</i></tt>
     * @throws NotFoundException if the service does not exist.
     */
    String[] listMethodsOfComponent(String componentName) throws NotFoundException;

    /** Access the XMLRPC type of an AstroRuntime method (function).
     * @param methodName  name of the method in form <tt><i>module</i>.<i>component</i>.<i>method</i></tt>
     * @return an array of signatures for this method (allows for overloading of method name).
     * {@stickyNote Overloading never occurs in ACR, but this form is necessary for compatability with XMLRPC standard.
     * So result will always be an array of length 1. First item in the array is an array of strings - first item in <i>this</i>
     * array is the return type; any following items are parameter types.}
     * @throws NotFoundException if the method does not exist.
     */
    String[][] methodSignature(String methodName) throws NotFoundException;

    /** Access help for a module.
     * @param moduleName      
     * @return documentation for this module
     * @throws NotFoundException if this module does not exist.
     */
    String moduleHelp(String moduleName) throws NotFoundException;

    /** Acccess help for a component / service.
     * @param componentName name of the service in form <tt><i>module</i>.<i>component</i></tt>
     * @return documentation for this component
     * @throws NotFoundException if this component does not exist
     */
    String componentHelp(String componentName) throws NotFoundException;

    /** Access help for a method.
     * @param methodName - name of the method in form <tt><i>module</i>.<i>component</i>.<i>method</i></tt>
     * @return documentation for this method
     * @throws NotFoundException if this method does not exist
     */
    String methodHelp(String methodName) throws NotFoundException;
    
    /** Return the class name of the service interface for a particular component
     * @param componentName - service name of form <tt><i>module</i>.<i>component</i></tt>
     * @return class name of associated service interface
     * @throws NotFoundException if the componentName is not a service in this acr.
     */
    String interfaceClassName(String componentName) throws NotFoundException;
    

    /** Directly call a function on an AR service.
     * 
     * The recommended method of working is to get an instance of a service interface, and call it's strongly-typed methods,
     * However, <tt>callFunction</tt> can be useful if working with loosely-typed parameters (e.g. inputs from scripts). This method will 
     * convert string parameters to the correct types as needed, and can return the result in a variety of forms
     * @param functionName fully qualified name of the function to call - e.g. <tt>system.configuration.getValue</tt>
     * @param returnKind An enumeration that controls what kind of object is returned as the result
     * <ul>
     * <li>ORIGINAL - the original return type</li>
     * <li>DATASTRUCTURE - datastructure view of the return type</li>
     * <li>STRING - stringified view of the return type</li>
     * @param args argument list - may be correct object types, or strings that can be converted into the correct types.
     * @return the result of invoking the method.
     * The result of invoking a void-return method will be a <tt>java.lang.VOID</tt>
     * @throws ACRException if an exception was thrown when invoking the function
     * @throws InvalidArgumentException if the arguments are incorrect (wrong number, inconvertable types) for the specified function.
     * @throws NotFoundException if the function does not exist

     */
    public Object callFunction(String functionName,int returnKind, Object[] args) throws ACRException, InvalidArgumentException, NotFoundException;
    
    public static final int ORIGINAL = 0;
    public static final int DATASTRUCTURE = 1;
    public static final int STRING = 2;
}

/* 
 $Log: ApiHelp.java,v $
 Revision 1.6  2008/09/25 16:02:03  nw
 documentation overhaul

 Revision 1.5  2007/01/24 14:04:44  nw
 updated my email address

 Revision 1.4  2006/06/02 00:18:10  nw
 Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

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