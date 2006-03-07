/*$Id: BaseApplicationDescriptionLibrary.java,v 1.13 2006/03/07 21:45:26 clq2 Exp $
 * Created on 17-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Basic implementation of an {@link org.astrogrid.applications.description.ApplicationDescriptionLibrary}
 * <p />
 * Unsurprisingly, based on a map. Provides methods to add descriptions to the library. 
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 *
 */
public class BaseApplicationDescriptionLibrary implements ApplicationDescriptionLibrary, ComponentDescriptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BaseApplicationDescriptionLibrary.class);
    
    /**Construct a new BaseApplicationDescriptionLibrary
    * @param env2
    */
   public BaseApplicationDescriptionLibrary(ApplicationDescriptionEnvironment env2) {
      
     this.env = env2;
   }

   /**
         * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary#getDescription(java.lang.String)
         */
    public ApplicationDescription getDescription(String name) throws ApplicationDescriptionNotFoundException {
        ApplicationDescription ad = (ApplicationDescription) descMap.get(name);
        if (ad == null) {
            throw new ApplicationDescriptionNotFoundException(name);
        }
        return ad;
    }
    /**
         * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary#getApplicationNames()
         */
    public String[] getApplicationNames() {
    return (String[])descMap.keySet().toArray(new String[0]);
      }
    private final Map descMap = new HashMap();
    
    /** add an application description to the library
     * <p> if an application with the same name already exists, it will be overridden. 
     * @param desc the application description, which will be stored under key <tt>desc.getName()</tt>*/
    public void addApplicationDescription(ApplicationDescription desc) {
       
       
        logger.info("Adding description for " + desc.getName());
        descMap.put(desc.getName(),desc);
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Basic Application Description Library";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return this.toString();
    }
     
   public String toString() {     
        StringBuffer appList = new StringBuffer();
        for (Iterator i = descMap.values().iterator(); i.hasNext(); ) {
            ApplicationDescription desc = (ApplicationDescription)i.next();
            appList.append("\n");
            appList.append(desc.toString());
            appList.append("\n");
        }
        return "Applications in Library:'"  + appList.toString();
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
          
          return new InstallationTest("testApplicationsDefined");
    }
    
    public class InstallationTest extends TestCase {
       
       /**
       * @param arg0
       */
      public InstallationTest(String arg0) {
         super(arg0);
         
      }
      public InstallationTest(){super();}
      
      public void testApplicationsDefined()
      {
         if(descMap.isEmpty())
         {
            fail("there are no applications defined in this Library");
         }
      }
      
}
    
   protected final ApplicationDescriptionEnvironment env;    
}


/* 
$Log: BaseApplicationDescriptionLibrary.java,v $
Revision 1.13  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.10.38.1  2005/12/18 14:48:24  gtr
Refactored to allow the component managers to pass their unit tests and the fingerprint JSP to work. See BZ1492.

Revision 1.10  2005/07/05 08:27:02  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.9.66.1  2005/06/09 08:47:33  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.9.52.3  2005/06/08 22:10:46  pah
make http applications v10 compliant

Revision 1.9.52.2  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.9.52.1  2005/06/02 14:57:29  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.9  2004/12/03 15:37:05  jdt
restored it how it was....change PAL instead.

Revision 1.8  2004/12/03 15:33:39  jdt
restored the default ctor, otherwise PAL breaks.

Revision 1.7  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.6.28.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.6  2004/09/07 08:06:39  pah
todo added - need to remove confusion between community and authorityid

Revision 1.5  2004/09/01 15:42:26  jdt
Merged in Case 3

Revision 1.4.2.1  2004/08/09 16:36:25  jdt
pulled up an interface so I can use it in http apps

Revision 1.4  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.3  2004/07/26 00:57:46  nw
javadoc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:42:46  nw
final version, before merge
 
*/