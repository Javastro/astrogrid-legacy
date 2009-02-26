/*$Id: ApplicationDescriptionLibrary.java,v 1.6 2009/02/26 12:45:54 pah Exp $
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
/** A container for {@link org.astrogrid.applications.description.ApplicationDefinition} instances
 * <p>
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 *
 */
public interface ApplicationDescriptionLibrary {
    /** retrieve a named description from the library 
     * @param name the name of the application description to retreive
     * @return the description for this applicaiton.
     * @throws ApplicationDescriptionNotFoundException if the name is not present in the library.*/
    public abstract ApplicationDescription getDescription(String name) throws ApplicationDescriptionNotFoundException;
    /** list names of all application descriptons in the library 
     * @return array of names (may be 0-length)*/
    public abstract String[] getApplicationNames();
    
    public abstract ApplicationDescription getDescriptionByShortName(String name) throws ApplicationDescriptionNotFoundException;
}
/* 
$Log: ApplicationDescriptionLibrary.java,v $
Revision 1.6  2009/02/26 12:45:54  pah
separate more out into cea-common for both client and server

Revision 1.5  2008/09/03 14:18:43  pah
result of merge of pah_cea_1611 branch

Revision 1.4.166.1  2008/05/13 15:57:32  pah
uws with full app running UI is working

Revision 1.4  2005/01/23 12:52:26  jdt
merge from cea_jdt_902

Revision 1.3.102.1  2005/01/22 13:56:46  jdt
typo in comment

Revision 1.3  2004/07/26 00:57:46  nw
javadoc

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