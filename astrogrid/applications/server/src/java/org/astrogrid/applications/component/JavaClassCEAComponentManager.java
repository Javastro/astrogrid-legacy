/*$Id: JavaClassCEAComponentManager.java,v 1.6 2005/08/10 14:45:37 clq2 Exp $
 * Created on 10-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.component;

import org.astrogrid.applications.javaclass.JavaClassApplicationDescriptionLibrary;
import org.astrogrid.applications.javaclass.SampleJavaClassApplications;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.MutablePicoContainer;

/** Simple component manager that defines a  standalone JavaClass CEA server
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Jun-2004
 *@see org.astrogrid.applications.javaclass
 *@todo factor into the javaclass package?
 */
public class JavaClassCEAComponentManager extends EmptyCEAComponentManager {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JavaClassCEAComponentManager.class);
    /** key to look in config under for the name of the java class to expose as cea applications (optional, defaulrs to {@link SampleJavaClassApplications})
     * @see #registerJavaClassProvider(MutablePicoContainer, Config)*/
    public final static String SERVER_CLASS_NAME = "cea.javaclass.server.class";
    /** Construct a new JavaClassCEAComponentManger, with all necessary components registered
     * <p />
     * registers the java class provider, plus all the standard services defined in {@link EmptyCEAComponentManager}
     */
    public JavaClassCEAComponentManager() {
        super();
        final Config config = SimpleConfig.getSingleton();
        // controller & queriers        
        registerDefaultServices(pico);
        // store
        EmptyCEAComponentManager.registerDefaultPersistence(pico,config);
        // metadata
        EmptyCEAComponentManager.registerDefaultVOProvider(pico,config);
        //registry uploader
        EmptyCEAComponentManager.registerDefaultRegistryUploader(pico);
        // now hook in out own implementation
        registerJavaClassProvider(pico,config);
    }
    /** just register the components specific to the java-class provider, but none of the generic components 
     * @see {@link #SERVER_CLASS_NAME}
     * @see {@link #COMMUNITY_KEY} */       
    public static final void registerJavaClassProvider(MutablePicoContainer pico, final Config config){    
        pico.registerComponentImplementation(JavaClassApplicationDescriptionLibrary.class,JavaClassApplicationDescriptionLibrary.class);

        // implementation class for the java cea server
        final String classname = config.getString(SERVER_CLASS_NAME,SampleJavaClassApplications.class.getName());
        Class serverClass = SampleJavaClassApplications.class;
        try {
            serverClass = Class.forName(classname);
        } catch (Exception e) {
            logger.fatal("Could not instantiate required server class '" + classname + "' falling back to sample java applications");
        }
        pico.registerComponentInstance(Class.class,serverClass);
    }
}


/* 
$Log: JavaClassCEAComponentManager.java,v $
Revision 1.6  2005/08/10 14:45:37  clq2
cea_pah_1317

Revision 1.5.86.1  2005/07/21 15:10:22  pah
changes to acommodate contol component, and starting to change some of the static methods to dynamic

Revision 1.5  2004/11/27 13:20:03  pah
result of merge of pah_cea_bz561 branch

Revision 1.4.70.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.4  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.3  2004/07/23 13:21:21  nw
Javadocs

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