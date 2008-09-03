/*
 * $Id: IvornUtil.java,v 1.3 2008/09/03 14:19:02 pah Exp $
 * 
 * Created on 30-May-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Useful utilities for Ivorns.
 * @TODO should probably be put into common ivorn class.
 * @author Paul Harrison (pharriso@eso.org) 30-May-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class IvornUtil {
   private IvornUtil(){}
   /** the regular expression for extracting the authorityID and resource name from ivorn */
   private static final String ivornRegexp = "(ivo://)?(([^/]+)/(.+))";
   private static final Pattern pattern = Pattern.compile(ivornRegexp);
  /**
   * Returns the ID part of an ivorn as a string.
   * @param s
   * @return
   */
   public static String extractIDFragment(String s) {
      Matcher matcher = pattern.matcher(s);
      if(matcher.find())
      {
      return matcher.group(4);
      }
      else
      {
         return null;
      }
   }
   /**
    * returns the AuthorityID part of an ivorn as a string.
    * @param s
    * @return
    */
   public static String extractAuthorityFragment(String s)
   {
      Matcher matcher = pattern.matcher(s);
      if(matcher.find())
      {
      return matcher.group(3);
      }
      else
      {
         return null;
      }
    }
   
   /**
 * @param s
 * @return
 * @deprecated all uses of the ivorn should include the ivo:// protocol part
 */
public static String removeProtocol(String s)
   {
      Matcher matcher = pattern.matcher(s);
      if(matcher.find())
      {
      return matcher.group(2);
      }
      else
      {
         return null;
      }
      
   }
   


}


/*
 * $Log: IvornUtil.java,v $
 * Revision 1.3  2008/09/03 14:19:02  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.2.102.1  2008/04/01 13:50:07  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.2  2005/07/05 08:26:57  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/05/31 12:58:25  pah
 * moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"
 *
 */
