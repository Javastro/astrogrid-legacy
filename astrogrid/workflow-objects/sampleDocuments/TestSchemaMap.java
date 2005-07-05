import java.util.Iterator;

import org.astrogrid.test.schema.SchemaMap;

import junit.framework.TestCase;
/*
 * $Id: TestSchemaMap.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
 * 
 * Created on 02-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

/**
 * @author Paul Harrison (pharriso@eso.org) 02-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class TestSchemaMap extends TestCase {

   public static void main(String[] args) {
      junit.textui.TestRunner.run(TestSchemaMap.class);
   }
   
   public void testMapEntries(){
      for (Iterator iter = SchemaMap.ALL.keySet().iterator(); iter.hasNext();) {
          String key = (String) iter.next();
          if(SchemaMap.ALL.get(key) == null)
          {
             fail("key "+key+ " has no associated schema");
          }
      }
   }
   
   

}


/*
 * $Log: TestSchemaMap.java,v $
 * Revision 1.2  2005/07/05 08:26:56  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.2.1  2005/06/09 07:32:18  pah
 * clean up
 *
 */
