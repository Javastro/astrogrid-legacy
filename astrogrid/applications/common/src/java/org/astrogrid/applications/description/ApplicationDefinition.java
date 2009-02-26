/*$Id: ApplicationDefinition.java,v 1.1 2009/02/26 12:25:47 pah Exp $
 * Created on 25-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
/** Description of a CEA Application.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 12 Mar 2008
 *
 */
public interface ApplicationDefinition extends Identify { 
    
    /** Access the name of the application that is to be used for display purposes.
    * @return a human friendly name for the application.
    */
   public String getName();
    
    /**
     * Access a short description of the purpose of the application.
    * @return the description.
    */
   public String getDescription();
    
   /**
    * Access a URL that acts as a reference to a (non-CEA) humanreadable description.
    * @return
    */
    public String getReferenceURL();
    
    /**
     * Access the list of all parameters defined for this application (no matter what interface they occur in).
     * @return the array of parameter definitions.
     */
   public ParameterDescription[] getParameterDescriptions();
   
    /**
    * Access the description for a named parameter.
    * @param name the parameter to look up
    * @return the associated description
    * @throws ParameterDescriptionNotFoundException if the application does not recognize the parameter name
    */
    public ParameterDescription getParameterDescription(String name) throws ParameterDescriptionNotFoundException;
    
    /**
    * Gets the named interface.
    * @param name the name of the interface to look up.
    * @return the associated <tt>ApplicationInterface</tt>
    * @throws InterfaceDescriptionNotFoundException if the application does not provide an interface of this name.
    */
    public ApplicationInterface getInterface(String name) throws InterfaceDescriptionNotFoundException;
    
    /** 
     * list all the interfaces supported by this application.
     */
    public ApplicationInterface[] getInterfaces();
    
  
    /**
     * get the underlying description of the application.
     * @TODO should this really be exposed?
     * @return
     */
    public MetadataAdapter getMetadataAdapter();

 }
/* 
$Log: ApplicationDefinition.java,v $
Revision 1.1  2009/02/26 12:25:47  pah
separate more out into cea-common for both client and server

Revision 1.6  2008/09/03 14:18:43  pah
result of merge of pah_cea_1611 branch

Revision 1.5.182.5  2008/08/02 13:33:56  pah
safety checkin - on vacation

Revision 1.5.182.4  2008/06/10 20:01:39  pah
moved ParameterValue and friends to CEATypes.xsd

Revision 1.5.182.3  2008/04/17 16:08:32  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.5.182.2  2008/03/26 17:15:38  pah
Unit tests pass

Revision 1.5.182.1  2008/03/19 23:10:53  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.5  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.4.38.1  2004/11/04 16:58:33  pah
improve registry entry building

Revision 1.4  2004/08/28 07:17:34  pah
commandline parameter passing - unit tests ok

Revision 1.3  2004/07/26 00:57:46  nw
javadoc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.7.8.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/