/*$Id: SampleJavaClassApplications.java,v 1.4 2004/07/26 10:21:47 nw Exp $
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

/** Sample class of static methods, to use as java 'applications'
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
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
        super();
    }
    /** the classic */
    public static String helloWorld() {
        return "Hello World!";
    }
    
    /** say hello to someone */
    public static String helloYou(String who) {
        return "Hello " + who;
    }
    /** add two numbers */
    public static int sum(int a, int b) {
        logger.info("Summing " + a + " and " + b);
        return a + b;
    }
        
}


/* 
$Log: SampleJavaClassApplications.java,v $
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