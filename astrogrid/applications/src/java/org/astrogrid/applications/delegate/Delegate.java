/*$Id: Delegate.java,v 1.2 2004/03/23 12:51:25 pah Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.delegate;
/** Interface common to all delegate objects.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public interface Delegate {
    /** pass this as the target endpoint to return a 'test' delegate */
    public final static String TEST_URI = "urn:test";
   // public abstract void setTargetEndPoint(String targetEndPoint);
   /** access the endpoint this delegate will communicate with */
    public abstract String getTargetEndPoint();
    /** configure the timeout for communications to the service */
    public abstract void setTimeout(int timeout);
    /** retreive the timeout value for communications to the service */
    public abstract int getTimeout();
}
/* 
$Log: Delegate.java,v $
Revision 1.2  2004/03/23 12:51:25  pah
result of merge of app_pah_147

Revision 1.1.2.1  2004/03/17 17:51:53  pah
refactored the factory to follow noels model

Revision 1.4  2004/03/15 01:30:29  nw
jazzed up javadoc

Revision 1.3  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.2.2.1  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/06 18:11:21  nw
reworked the delegate classes
- introduced wrapper class and interfaces, plus separate impl
package with abstract base class. moved delegate classes into the correct
packages, deprecated old methods / classes. fitted in castor object model
 
*/