/*$Id: JavaClassApplicationDescriptionLibrary.java,v 1.5 2004/11/27 13:20:03 pah Exp $
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

import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** A library of java class application descriptions.
 * <p>
 * This class constructs {@link org.astrogrid.applications.javaclass.JavaClassApplicationDescription} for each static method in its parameter class,
 * and then collects them as an {@link org.astrogrid.applications.description.ApplicationDescriptionLibrary}
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @see org.astrogrid.applications.javaclass.JavaClassApplicationDescription
 * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary
 *
 */
public class JavaClassApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary implements  ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JavaClassApplicationDescriptionLibrary.class);



    /** Construct a new JavaClassApplicationDescriptionLibrary, based on static methods of parameter class
     * @param implClass - class of static methods, each of which will provide an application for the library.
     * @param authidResolver configuration object specifiying under which community (authority?) the applications are to be placed 
     * @param env standard container object for helper code.
     * 
     */
    public JavaClassApplicationDescriptionLibrary(Class implClass, ApplicationDescriptionEnvironment env) {
        super(env);
        this.implClass= implClass;
        populate(implClass,env.getIdGen(), env.getAuthIDResolver());
    }
    protected final Class implClass;
    /** populates the library using reflection on the methods of the parameter class
     * @param imp
    * @param authidresolver
     */
    protected final void populate(Class imp,IdGen idgen, BaseApplicationDescriptionLibrary.AppAuthorityIDResolver authidresolver) {
        String communityName = authidresolver.getAuthorityID();
        Method[] methods = imp.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            int code = m.getModifiers();
            if (Modifier.isStatic(code) && Modifier.isPublic(code)) {
                super.addApplicationDescription(new JavaClassApplicationDescription(m,communityName,env));            
                } 
        } 
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Implementation class: " + implClass.getName() + "\n" + super.getDescription();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Java Class Application Library";
    }

}

/* 
$Log: JavaClassApplicationDescriptionLibrary.java,v $
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