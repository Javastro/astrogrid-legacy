/*$Id: ACR.java,v 1.6 2007/11/12 13:36:27 pah Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.builtin;


import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;

/** Interface into a running AR daemon, from which services provided by the AR API can be accessed..
 * 
 * Note that this isn't a component interface itself.
 *
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 15-Mar-2005
 * @see org.astrogrid.acr.Finder How to create an instance of ACR
 *
 */
public interface ACR { 
   
    /** Retreive an instance of an AR API service.
     * 
     * @param interfaceClass the interface class for a service
     * @return an implementation of the service interface
     * @throws InvalidArgumentException if the parameter class is not an interface
     * @throws NotFoundException if the parameter class is not an available service in the ACR
     * @throws ACRException if another error occurs while retreiving the service instance
     * @example
     * <pre>
     * import org.astrogrid.acr.system.Configuration;
     * import org.astrogrid.acr.builtin.ACR;
     * import org.astrogrid.acr.Finder;
     * Finder f = new Finder();
     * ACR acr = f.find(); 
     * Configuration c = (Configuration) acr.getService(Configuration.class)
     * </pre>
     * */
    public Object getService(Class interfaceClass) throws ACRException, InvalidArgumentException, NotFoundException;
    
    /** Retreive an instance of an AR API serivce.
     * 
     * 
     * This method is a less-well-typed equvalent to {@link #getService(Class)} - preferably use that method. However, this method is handy when it isn't easy to get hold of 
     * class objects - e.g. from in-java scripting languages. 
     * @param componentName the name of the service - (as defined by<tt>Service</tt> tags in javadoc). These are the same component names as used in the XMLRPC interface - all follow the form
     * <tt><i>moduleName</i>.<i>componentName</i></tt>
     * @return an implementation of the service interface.
     * @throws InvalidArgumentException if the componentName does not follow the form <tt><i>moduleName</i>.<i>componentName</i></tt>
     * @throws NotFoundException if this componentName is not an available service in the ACR
     * @throws ACRException if another error occurs while retreiving the service instance
     * @example
     * <pre>
     * import org.astrogrid.acr.system.Configuration;
     * import org.astrogrid.acr.builtin.ACR;
     * import org.astrogrid.acr.Finder;
     * Finder f = new Finder();
     * ACR acr = f.find(); 
     * Configuration c = (Configuration) acr.getService("system.configuration")
     * </pre>
     */
    public Object getService(String componentName) throws ACRException, InvalidArgumentException,NotFoundException;
}

/* 
 $Log: ACR.java,v $
 Revision 1.6  2007/11/12 13:36:27  pah
 change parameter name to structure

 Revision 1.5  2007/01/24 14:04:46  nw
 updated my email address

 Revision 1.4  2006/10/30 12:12:36  nw
 documentation improvements.

 Revision 1.3  2006/02/02 14:19:48  nw
 fixed up documentation.

 Revision 1.2  2005/08/12 08:45:16  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.1  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.4  2005/05/12 15:59:09  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2.2.1  2005/04/22 15:59:26  nw
 made a star documenting desktop.

 Revision 1.2  2005/04/13 12:59:12  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.1  2005/03/18 12:09:31  nw
 got framework, builtin and system levels working.
 
 */