/*$Id: SampleJavaClassApplications.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

/** Sample class of static methods, to use as java 'applications'
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class SampleJavaClassApplications {
    /** Construct a new TestJavaApplication
     * 
     */
    public SampleJavaClassApplications() {
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
        return a + b;
    }
        
}


/* 
$Log: SampleJavaClassApplications.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/