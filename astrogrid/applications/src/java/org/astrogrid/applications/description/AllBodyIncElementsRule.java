/*
 * $Id: AllBodyIncElementsRule.java,v 1.2 2003/12/04 13:26:25 pah Exp $
 * 
 * Created on 28-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.BeanPropertySetterRule;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;

/**
    * A rule that incorporates all of the enclosed elements into a property
    * @author Paul Harrison (pah@jb.man.ac.uk)
    * @version $Name:  $
    * @since iteration4
    */
class AllBodyIncElementsRule extends BeanPropertySetterRule {
   private static StringBuffer contents;
   private boolean needinner;

   public AllBodyIncElementsRule(String propName, boolean inner) {
      super(propName);
      needinner = inner;
   }

   /* (non-Javadoc)
    * @see org.apache.commons.digester.Rule#end(java.lang.String, java.lang.String)
    */
   public void end(String namespace, String name) throws Exception {
      //set the bodytext to the collected contents as this is what the end usually sets
      Object o = getDigester().peek();
      if (o instanceof Node) {
         String s;

         if (needinner) {
            s = XMLUtils.getInnerXMLString((Element)o);
         }
         else
         {
            s= XMLUtils.ElementToString((Element)o);
         }
         getDigester().pop(); // remove Node from the Digester stack.
         bodyText = s;
      }
      
      super.end(namespace, name);

   }

}