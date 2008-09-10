/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.description;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.component.descriptor.ComponentDescriptor;

/** Basic implementation of an {@link org.astrogrid.applications.description.ApplicationDescriptionLibrary}
 * <p />
 * Unsurprisingly, based on a map. Provides methods to add descriptions to the library. 
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Sep 2008
 *
 */
public class BaseApplicationDescriptionLibrary implements ApplicationDescriptionLibrary, ComponentDescriptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BaseApplicationDescriptionLibrary.class);

    protected Configuration conf;
    
  /**
   * Constructs a new BaseApplicationDescriptionLibrary
   * @param env2
 * @param conf 
   */
  public BaseApplicationDescriptionLibrary(Configuration conf) {
    this.descMap =  new HashMap<String, ApplicationDescription >();
    this.conf = conf;
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
    private final Map<String, ApplicationDescription> descMap;
    
    /** map keyed on the short name with all spaces removed - this is because the short name is used in some URIs in the UWS interfaces
     */
    private final Map<String, ApplicationDescription> shortMap = new HashMap<String, ApplicationDescription >();;
    
    /** add an application description to the library
     * <p> if an application with the same name already exists, it will be overridden. 
     * @param desc the application description, which will be stored under key <tt>desc.getName()</tt>*/
    public void addApplicationDescription(ApplicationDescription desc) {
       
        String shortname = desc.getName().replaceAll("\\s", "");
        logger.info("Adding description for " + desc.getId() + " with shortname="+shortname+" type="+desc.getClass().getCanonicalName());
        descMap.put(desc.getId(),desc);
        shortMap.put(shortname, desc);
        
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
    
   public ApplicationDescription getDescriptionByShortName(String name) throws ApplicationDescriptionNotFoundException{
       ApplicationDescription ad = (ApplicationDescription) shortMap.get(name);
       if (ad == null) {
	   throw new ApplicationDescriptionNotFoundException(name);
       }
       return ad;
   }
}
