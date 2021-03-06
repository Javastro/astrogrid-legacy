/*$Id: JavaClassApplicationDescription.java,v 1.12 2011/09/02 21:55:55 pah Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.javaclass;

import java.lang.reflect.Method;
import java.util.Map;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.AbstractApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ServiceDefinitionFactory;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.security.SecurityGuard;

/** A description for an application that is implemented as a static java method.
 * <p>
 * For a method foo(), this class will create an {@link org.astrogrid.applications.description.ApplicationDefinition} for <tt><i>authorityName</i>/foo</tt>,
 * where the authority name is specified in the constructor.
 * <p>
 * Constructs all the metadata for the application via reflection on the static method.
 * @todo improve definition of types
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 18 Mar 2008
 *
 */
public class JavaClassApplicationDescription extends AbstractApplicationDescription {
    
    final Map<String, Method>  methods; 
    /** Construct a new JavaClassApplicationDescription
     * @param implClass the class that is the implementation of the service 
     * @param config 
     * @param sf the curation information is taken from the service definition.
     * @throws ClassNotFoundException 
     */
    public JavaClassApplicationDescription(String implClass, Configuration config, ServiceDefinitionFactory sf) throws ClassNotFoundException {
        super(new JavaClassMetadataAdapter(implClass, sf), config);
        methods = ((JavaClassMetadataAdapter)metadataAdapter).getMethodMap();
     }
    
    
    /**
     * @see org.astrogrid.applications.description.ApplicationDefinition#initializeApplication(java.lang.String, SecurityGuard, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String jobStepID, SecurityGuard secGuard, Tool tool) throws Exception {
        ApplicationInterface interf = this.getInterface(tool.getInterface());
        ApplicationEnvironment env = new ApplicationEnvironment(jobStepID, secGuard, getInternalComponentFactory().getIdGenerator(), conf);
	return new JavaClassApplication(tool,interf, env, getInternalComponentFactory().getProtocolLibrary());
    }
}


/* 
$Log: JavaClassApplicationDescription.java,v $
Revision 1.12  2011/09/02 21:55:55  pah
result of merging the 2931 branch

Revision 1.11.2.1  2009/07/15 09:55:48  pah
redesign of parameterAdapters

Revision 1.11  2009/04/04 20:41:53  pah
ASSIGNED - bug 2113: better configuration for java CEC
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2113
Introduced annotations

Revision 1.10  2009/02/26 12:45:56  pah
separate more out into cea-common for both client and server

Revision 1.9  2008/09/24 13:40:49  pah
package naming changes

Revision 1.8  2008/09/13 09:51:03  pah
code cleanup

Revision 1.7  2008/09/10 23:27:19  pah
moved all of http CEC and most of javaclass CEC code here into common library

Revision 1.6  2008/09/04 19:10:53  pah
ASSIGNED - bug 2825: support VOSpace
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
Added the basic implementation to support VOSpace  - however essentially untested on real deployement

Revision 1.5  2008/09/03 14:18:44  pah
result of merge of pah_cea_1611 branch

Revision 1.4.262.7  2008/09/03 12:22:55  pah
improve unit tests so that they can run in single eclipse gulp

Revision 1.4.262.6  2008/08/29 07:28:26  pah
moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration

Revision 1.4.262.5  2008/08/02 13:33:56  pah
safety checkin - on vacation

Revision 1.4.262.4  2008/06/11 14:31:42  pah
merged the ids into the application execution environment

Revision 1.4.262.3  2008/05/01 15:22:48  pah
updates to tool

Revision 1.4.262.2  2008/04/17 16:08:33  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.4.262.1  2008/03/19 23:10:55  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.4  2004/07/30 14:54:47  jdt
merges in from case3 branch

Revision 1.3.4.1  2004/07/29 09:11:54  jdt
can't be too careful

Revision 1.3  2004/07/26 10:21:47  nw
javadoc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/