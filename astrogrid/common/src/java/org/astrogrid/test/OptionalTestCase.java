/*$Id: OptionalTestCase.java,v 1.2 2004/01/23 11:01:03 nw Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.test;
import java.util.Iterator;
import java.util.Properties;

import junit.framework.TestCase;
import junit.framework.TestResult;

/** Extension of the standard junit TestCase that allows tests to be enabled / disabled.
 * <p>
 * Class provides static methods to turn individual tests cases on and off. 
 * <p>
 * Works from settings in System.properties (which can be set on the command-line using the <tt>-D<i>key</i>=<i>value</i></tt>)
 * <p>
 * Setting the system property <tt>optional.test.skip.ALL</tt> to <tt>true</tt> will cause the execution of all optional test cases to be skipped<br>
 * An individual test case named <tt>org.foo.wibbleTest</tt> can be disabled by setting the system property
 * <tt>optional.test.skip.org.foo.wibbleTest</tt> to <tt>true</tt>
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Jan-2004
 *
 */
public class OptionalTestCase extends TestCase {
   /**
    * 
    */
   public OptionalTestCase() {
      super();
   }
   /**
    * @param arg0
    */
   public OptionalTestCase(String arg0) {
      super(arg0);
   }

   /**
    * overridden - only runs a test if this optional test case is enabled.
    */
   public void run(TestResult tr) {
      if (this.isDisabled()) {
         System.out.println("Optional test " + this.getClass().getName() + "#" + this.getName() + " is disabled - Skipping..");
         tr.endTest(this);
      } else {
         super.run(tr);
      }
   }   
   
   /** check whether this test case is disabled */
   public final boolean isDisabled() {
      String val = System.getProperty(mkKey(this.getClass().getName()));
      if (val == null) {
         return Boolean.getBoolean(DISABLED_BY_DEFAULT);
      } else {
         return Boolean.valueOf(val).booleanValue();
      }

   }
   /** set enablement for this test */
   public final void setDisabled(boolean isDisabled) {
      OptionalTestCase.setTestDisabled(this.getClass().getName(),isDisabled);
   }
   /** if true, by default disable optional test cases, otherwise enable by default. */
   public final  static void setDisabledByDefault(boolean isDisabled) {
      System.setProperty(DISABLED_BY_DEFAULT,Boolean.toString(isDisabled));
   }
   /** set enablement for a named test */
   public final static void setTestDisabled(String classname, boolean isDisabled) {
      System.setProperty(mkKey(classname),Boolean.toString(isDisabled));
   }
   
   private final static String mkKey(String classname) {
      return "optional.test.skip" + classname;
   }

   /** key used in System.properties. If true, by default disable optional test cases */ 
   public  final static String DISABLED_BY_DEFAULT="optional.test.skip.ALL";
   
   /** reset enablements back to original settings - all optional tests enabled */
   public final static void reset() {
      // remove any per-class settings
      Properties sysProps = System.getProperties();
      for (Iterator i = sysProps.keySet().iterator();i.hasNext(); ) {
         String key = (String)i.next();
         if (key.startsWith("optional.test.skip")) {
            i.remove();
         }
      }
      System.setProperties(sysProps);  
   }

}


/* 
$Log: OptionalTestCase.java,v $
Revision 1.2  2004/01/23 11:01:03  nw
minor change

Revision 1.1  2004/01/23 10:54:52  nw
added base test case that allows tests to be skipped
according to settings in system.properties
 
*/