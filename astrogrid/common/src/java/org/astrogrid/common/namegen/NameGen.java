/*$Id: NameGen.java,v 1.2 2005/04/25 12:13:30 clq2 Exp $
 * Created on 18-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.common.namegen;


/** Interface to a unique name generator
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2005
 *
 */
public interface NameGen {
    /** return a new unique identifier.*/
    String next() throws Exception;
    
}


/* 
$Log: NameGen.java,v $
Revision 1.2  2005/04/25 12:13:30  clq2
common-nww-1035

Revision 1.1.2.1  2005/04/11 11:03:21  nw
moved name generator into common from filemanager - can then
reuse in jes too.

Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:35  nw
split code inito client and server projoects again.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/