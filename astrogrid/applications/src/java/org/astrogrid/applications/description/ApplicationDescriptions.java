/*
 * $Id: ApplicationDescriptions.java,v 1.4 2003/12/09 23:01:15 pah Exp $
 * 
 * Created on 26-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;

/**
 * A simple holder for the {@link ApplicationDescription} objects.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationDescriptions {

   private Map applications;
   
   public ApplicationDescriptions()
   {
      applications = new HashMap();
      
   }
   public void addDescription(ApplicationDescription d)
   {
      applications.put(d.getName(), d);
      
   }
   public void removeDescription(ApplicationDescription d)
   {
      applications.remove(d.getName());
   }
   
   public ApplicationDescription getDescription(String name) throws ApplicationDescriptionNotFoundException
   {
      Object o  = null;
      if (applications.containsKey(name)) {
          o = applications.get(name);
      }
      else {
         throw new ApplicationDescriptionNotFoundException(name);
      }
      return (ApplicationDescription)o;
      
   }
   public String[] getApplicationNames()
   {
      Set nameset = applications.keySet();
      return (String[])nameset.toArray(new String[0]);
   }
}
