/*$Id: ApplicationDescriptionLibrary.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 25-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
/** interface into a library of application descriptions.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 *
 */
public interface ApplicationDescriptionLibrary {
    /** retreive a named description from the library */
    public abstract ApplicationDescription getDescription(String name) throws ApplicationDescriptionNotFoundException;
    /** list names of all application descriptons in the library */
    public abstract String[] getApplicationNames();
}
/* 
$Log: ApplicationDescriptionLibrary.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/