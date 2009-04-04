/*$Id: SampleJavaClassApplications.java,v 1.7 2009/04/04 20:41:54 pah Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.javaclass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.description.annotation.CEAInterface;
import org.astrogrid.applications.description.annotation.CEAApplication;
import org.astrogrid.applications.description.annotation.CEAParameter;
import org.astrogrid.applications.service.v1.cea.CeaSecurityGuard;


/** Sample class of static methods, to use as java 'application' - each method will appear as a different interface to the main application
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 3 Apr 2009 refactor to new behaviour with annotations.
 *
 */
@CEAApplication(id="ivo://local.test/sampleJavaApp", description="an example of a java application", referenceURL="http://www.astrogrid.org/")
public class SampleJavaClassApplications {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(SampleJavaClassApplications.class);

    /** Static methods - so no public constructor
     * 
     */
    private SampleJavaClassApplications() {
    }
    /** the classic */
   @CEAInterface(description="the classic hello world")
   public static String helloWorld() {
        return "Hello World!";
    }
    
   /** say hello to someone */
   @CEAInterface(description="say hello to someone")
    public static String helloYou(@CEAParameter(id="who", description="the person to say hello to")String who) {
        return "Hello " + who;
    }
   /** echo args */
   @CEAInterface(description="echo different args")
    public static String echoDifferent(@CEAParameter(id="s")String who, int i) {
        return who + i;
    }
    /** add two numbers */
    @CEAInterface(description="add two numbers")
    public static int sum(@CEAParameter(id="a") int a,
                          @CEAParameter(id="b") int b) {
        logger.info("Summing " + a + " and " + b);
        return a + b;
    }
    
    /**
     * Uses security facade to find the caller's identity.
     */
    @CEAInterface(description="Uses security facade to find the caller's identity.")
    public static String whoAmI() {
      CeaSecurityGuard g = CeaSecurityGuard.getInstanceFromContext();
      if (g.isAuthenticated()) {
        return g.getX500Principal().getName();
      }
      else {
        return "not authenticated";
      } 
    }
        
}


/* 
$Log: SampleJavaClassApplications.java,v $
Revision 1.7  2009/04/04 20:41:54  pah
ASSIGNED - bug 2113: better configuration for java CEC
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2113
Introduced annotations

Revision 1.6  2006/06/13 20:33:13  clq2
pal_gtr_1671

Revision 1.5.50.1  2006/06/09 17:49:07  gtr
I added security features.

Revision 1.5  2005/07/14 13:36:11  jdt
merge from  Branch aginab_jdt_1182

Revision 1.4.164.1  2005/07/08 17:43:38  jdt
added a method to illustrate the parameter ordering bug

Revision 1.4  2004/07/26 10:21:47  nw
javadoc

Revision 1.3  2004/07/22 16:32:54  nw
cleaned up application / parameter adapter interface.

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/