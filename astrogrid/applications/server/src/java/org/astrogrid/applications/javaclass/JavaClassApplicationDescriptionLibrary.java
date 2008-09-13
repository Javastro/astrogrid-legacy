/*$Id: JavaClassApplicationDescriptionLibrary.java,v 1.14 2008/09/13 09:51:03 pah Exp $
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
import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ServiceDefinitionFactory;
import org.astrogrid.applications.description.ServiceDescriptionException;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.component.descriptor.ComponentDescriptor;

/** A library of java class application descriptions.
 * <p>
 * This class constructs {@link org.astrogrid.applications.javaclass.JavaClassApplicationDescription} for each static method in its parameter class,
 * and then collects them as an {@link org.astrogrid.applications.description.ApplicationDescriptionLibrary}
 * @TODO would be nice if this library read a file/property list to allow multiple classes to be registered.
 * @TODO would be good to be able to use annotation to provide nore of the documenation that is needed for a full registry entry.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @see org.astrogrid.applications.javaclass.JavaClassApplicationDescription
 * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary
 *
 */
public class JavaClassApplicationDescriptionLibrary 
    extends BaseApplicationDescriptionLibrary 
    implements ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JavaClassApplicationDescriptionLibrary.class);



    /** Construct a new JavaClassApplicationDescriptionLibrary, based on static methods of parameter class
     * @param implClass - class of static methods, each of which will provide an application for the library.
     * @param authidResolver configuration object specifiying under which community (authority?) the applications are to be placed 
     * @param env standard container object for helper code.
     * @param servDefFac 
     * @throws ServiceDescriptionException 
     * 
     */
    public JavaClassApplicationDescriptionLibrary(JavaClassConfiguration config, 
                                                 ServiceDefinitionFactory servDefFac) throws ServiceDescriptionException {
        super( config);
        this.implClass = config.getApplicationClass();
        this.sf = servDefFac;
        populate(implClass, InternalCeaComponentFactory.getInstance().getAuthIDResolver());
    }
    
    protected final Class implClass;
    private final ServiceDefinitionFactory sf;
    /** populates the library using reflection on the methods of the parameter class
     * @param imp
     * @param authidresolver
     * @throws ServiceDescriptionException 
     */
    protected final void populate(Class imp,AppAuthorityIDResolver authidresolver) throws ServiceDescriptionException {
        String communityName = authidresolver.getAuthorityID();
        JavaClassApplicationMetadataFactory appFac = new JavaClassApplicationMetadataFactory(communityName,  sf);
        Method[] methods = imp.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            int code = m.getModifiers();
            if (Modifier.isStatic(code) && Modifier.isPublic(code)) {
                super.addApplicationDescription(new JavaClassApplicationDescription(appFac.createMetadata(m),m, conf));            
                } 
        } 
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    @Override
    public String getDescription() {
        return "Implementation class: " + implClass.getName() + "\n" + super.getDescription();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    @Override
    public String getName() {
        return "Java Class Application Library";
    }

}

/* 
$Log: JavaClassApplicationDescriptionLibrary.java,v $
Revision 1.14  2008/09/13 09:51:03  pah
code cleanup

Revision 1.13  2008/09/03 14:18:44  pah
result of merge of pah_cea_1611 branch

Revision 1.12.54.4  2008/08/29 07:28:26  pah
moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration

Revision 1.12.54.3  2008/06/16 21:58:58  pah
altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.

Revision 1.12.54.2  2008/06/11 14:31:42  pah
merged the ids into the application execution environment

Revision 1.12.54.1  2008/03/19 23:10:55  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.12  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.10  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.7.20.2  2006/02/01 12:09:54  gtr
Refactored and fixed to allow the tests to work with the new configuration.

Revision 1.7.20.1  2005/12/18 14:48:25  gtr
Refactored to allow the component managers to pass their unit tests and the fingerprint JSP to work. See BZ1492.

Revision 1.7  2005/08/10 14:45:37  clq2
cea_pah_1317

Revision 1.6.6.1  2005/07/19 11:58:05  pah
added comment that it would be good if the library could accommodate multiple classes as "application" implementations - would be nice to use annotations also to provide more of the documentation that is needed.

Revision 1.6  2005/07/05 08:27:01  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.5.68.1  2005/06/09 08:47:33  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.5.54.1  2005/06/02 14:57:29  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.5  2004/11/27 13:20:03  pah
result of merge of pah_cea_bz561 branch

Revision 1.4.36.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.4  2004/09/01 15:42:26  jdt
Merged in Case 3

Revision 1.3.4.1  2004/08/09 16:36:25  jdt
pulled up an interface so I can use it in http apps

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