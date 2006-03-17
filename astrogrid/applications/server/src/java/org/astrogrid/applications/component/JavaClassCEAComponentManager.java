/*
 * $Id: JavaClassCEAComponentManager.java,v 1.12 2006/03/17 17:50:58 clq2 Exp $
 * Created on 10-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.javaclass.JavaClassApplicationDescriptionLibrary;
import org.astrogrid.applications.javaclass.BaseJavaClassConfiguration;
import org.astrogrid.applications.javaclass.JavaClassConfiguration;
import org.astrogrid.applications.javaclass.SampleJavaClassApplications;
import org.picocontainer.MutablePicoContainer;

/** 
 * Simple component manager that defines a  standalone JavaClass CEA server
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Jun-2004
 * @see org.astrogrid.applications.javaclass
 * @todo factor into the javaclass package?
 */
public class JavaClassCEAComponentManager extends EmptyCEAComponentManager {
  
  private static final Log logger
      = LogFactory.getLog(JavaClassCEAComponentManager.class);
    

   /** 
    * Constructs a new JavaClassCEAComponentManger, 
    * with all necessary components registered.
    */
   public JavaClassCEAComponentManager() {
     super();
     registerDefaultServices(pico);
     EmptyCEAComponentManager.registerDefaultRegistryUploader(pico);
     registerJavaClassProvider(pico);
   }
    
   /**
    * Registers components peculiar to the JC-CEC.
    */
   public static final void registerJavaClassProvider(MutablePicoContainer pico){
      
     // The configuration has two interfaces, one generic and one specific
     // to the JC-CEC so it has to be registered twice.
     pico.registerComponentImplementation(Configuration.class, 
                                          BaseJavaClassConfiguration.class);
     pico.registerComponentImplementation(JavaClassConfiguration.class, 
                                          BaseJavaClassConfiguration.class);
      
     // This component will instantiate the class implementing the
     // application and will derive from it, by reflection, the
     // application description.
     pico.registerComponentImplementation(JavaClassApplicationDescriptionLibrary.class,
                                          JavaClassApplicationDescriptionLibrary.class);

     // The class implementing the application isn't registered with 
     // Picocontainer. Instead, the Class object is made available to
     // the JavaclassApplicationDescriptionLibrary by the 
     // JavaClassConfiguration.assApplications.class.getName());
   }
}


/* 
$Log: JavaClassCEAComponentManager.java,v $
Revision 1.12  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.10  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.6.20.3  2006/02/01 12:09:54  gtr
Refactored and fixed to allow the tests to work with the new configuration.

Revision 1.6.20.2  2006/01/31 21:39:07  gtr
Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.

Revision 1.6.20.1  2005/12/18 14:48:24  gtr
Refactored to allow the component managers to pass their unit tests and the fingerprint JSP to work. See BZ1492.

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