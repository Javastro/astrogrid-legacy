/*$Id: Splasher.java,v 1.4 2008/11/04 14:35:52 nw Exp $
 * Created on 04-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop;

import org.astrogrid.VODesktop;
import org.astrogrid.Workbench;

/** Bootstrap class - displays splashcreen before starting off main application.
 * @deprecated - call {@link VODesktop} instead.
 * .
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 04-Jul-2005
 */
@Deprecated
public class Splasher {

    /** Construct a new Workbench
     * 
     */
    public Splasher() {
        super();
    }
    
    public static void main(final String[] args) {
    	Workbench.main(args);
    }

}


/* 
$Log: Splasher.java,v $
Revision 1.4  2008/11/04 14:35:52  nw
javadoc polishing

Revision 1.3  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.68.4  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.68.3  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.68.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.1.68.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/07/08 11:08:02  nw
bug fixes and polishing for the workshop
 
*/