/*$Id: JavaClassApplicationDescriptionLibrary.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

/** library of java class application descriptions.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class JavaClassApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary implements  ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JavaClassApplicationDescriptionLibrary.class);


    /** configuration interface - defines the name of the community the applications will be added to. */
    public interface Community {
        String getCommunity();
    }

    /** Construct a new JavaClassApplicationDescriptions, based on methods of parameter class
     * @param implClass - class of static methods, each of which provides an applications for the library.
     * 
     */
    public JavaClassApplicationDescriptionLibrary(Class implClass, Community community,ApplicationDescriptionEnvironment env) {
        super();
        this.implClass= implClass;
        this.env = env;
        this.community = community;
        populate(implClass,env.getIdGen());
    }
    protected final Community community;
    protected final Class implClass;
    protected final ApplicationDescriptionEnvironment env;
    /** populates the library using reflection on the methods of the parameter class
     * @param imp
     */
    protected final void populate(Class imp,IdGen idgen) {
        String communityName = community.getCommunity();
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