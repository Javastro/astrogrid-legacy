/*$Id: HttpParameterDescription.java,v 1.2 2004/07/27 17:49:57 jdt Exp $
 * Created on Jul 24, 2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.http;

import org.astrogrid.applications.description.base.BaseParameterDescription;

/** JavaClass parameter description. 
 * contains an extra field - the 'target-class' for this parameter - i.e. the true java-class that this parameter
 * has to be converted to when calling the application method.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class HttpParameterDescription extends BaseParameterDescription {
    protected Class targetClass;
    public Class getTargetClass() {
        return targetClass;
    }
    public void setTargetClass(Class c) {
        this.targetClass = c;
    }
}


/* 
$Log: HttpParameterDescription.java,v $
Revision 1.2  2004/07/27 17:49:57  jdt
merges from case3 branch

Revision 1.1.4.1  2004/07/27 17:20:25  jdt
merged from applications_JDT_case3

Revision 1.1.2.1  2004/07/24 17:16:16  jdt
Borrowed from javaclass application....stealing is always quicker.

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/