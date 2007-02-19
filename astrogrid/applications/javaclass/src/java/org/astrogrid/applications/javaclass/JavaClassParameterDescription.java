/*$Id: JavaClassParameterDescription.java,v 1.2 2007/02/19 16:20:22 gtr Exp $
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

import org.astrogrid.applications.description.base.BaseParameterDescription;

/** A {@link org.astrogrid.applications.description.ParameterDescription} for a {@link org.astrogrid.applications.javaclass.JavaClassApplication}
 * <p>
 * This paramter description extends the base one with  an extra field - the 'target-class' for this parameter - i.e. the true java-class that this parameter
 * has to be converted to when calling the application method.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class JavaClassParameterDescription extends BaseParameterDescription {
    protected Class targetClass;
    
    /** Access the target class for this parameter
     * @return the class representing the expected type for this parameter
     */
    public Class getTargetClass() {
        return targetClass;
    }
    /** See the targetClass attribute for this parameter
     * @param c the expected type for this parameter.
     */
    public void setTargetClass(Class c) {
        this.targetClass = c;
    }
}


/* 
$Log: JavaClassParameterDescription.java,v $
Revision 1.2  2007/02/19 16:20:22  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:23:34  gtr
Javaclass-application stuff is moved to a separate project.

Revision 1.3  2004/07/26 10:21:47  nw
javadoc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/