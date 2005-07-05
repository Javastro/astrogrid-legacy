/*
 * $Id: NonPoppingNodeCreateRule.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 * 
 * Created on May 23, 2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.digester;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.digester.NodeCreateRule;

/**
 * A special node create rule that overrides the end method to stop popping of the elements created - this has to be used in combination with a @link org.astrogrid.applications.commandline.digester.AllBodyIncElementsRule which does do the popping
 * Needed with digester 1.7 (was not necessary with 1.6)
 * @TODO would probably be better to combine this class and the AllBodyIncElementRule class - would need more code in the classes, as the current ugly method reuses (by derivation) much digester functionality)
 * @author Paul Harrison (pharriso@eso.org) May 23, 2005
 * @version $Name:  $
 * @since initial Coding
 */
public class NonPoppingNodeCreateRule extends NodeCreateRule {

   /**
    * @param arg0
    * @throws javax.xml.parsers.ParserConfigurationException
    */
   public NonPoppingNodeCreateRule(int arg0)
         throws ParserConfigurationException {
      super(arg0);
   }
   /**
    * @param arg0
    * @param arg1
    */
   public NonPoppingNodeCreateRule(int arg0, DocumentBuilder arg1) {
      super(arg0, arg1);
   }
   /**
    * @param arg0
    */
   public NonPoppingNodeCreateRule(DocumentBuilder arg0) {
      super(arg0);
    }
   /**
    * explicitly overridden to do nothing.
    * @see org.apache.commons.digester.Rule#end()
    */
   public void end() throws Exception {
      //don't pop the stack which is the default behaviour.
  }
}


/*
 * $Log: NonPoppingNodeCreateRule.java,v $
 * Revision 1.2  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:33  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/05/24 13:15:50  pah
 * updated to digester 1.7 to use the limited namespace support...
 *
 */
