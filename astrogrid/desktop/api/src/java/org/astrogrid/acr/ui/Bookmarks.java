/*$Id: Bookmarks.java,v 1.1 2005/09/12 15:21:43 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ui;

/** Interface to an acr-wide bookmarking system.
 * allows resource references to resources to be bookmarked, listed, organized.
 * @todo implement.
 * 
 * need to think about this - stores beans of uri/name - but what good is this? need to store handler too - ie. what can open this resource.
 * or do we not maintain this info here? hard to tell .
 * 
 * lot depends on what we allow to be stored in bookmarks - is it any uri or just registry entries?
 * problematic that myspace references aren't easily distinguished (or cea app references).
 * other things useful to store - ftp server urls, endpoints of non-registered cone searches..
 * 
 * need to maintain a 'type' indicator  -  that is provided when storing the resource...
 * for registry entries, can get this from xsi:type. myspace browser - need to fudge slightly.
 * 
 * other kinds of reference - running app references are in the job monitor. nowhere for myspace references - but can all be got from myspace.
 * could add a 'favourite places' to the myspace browser? - nice. would make sense to base this on the same system? or just too confusing.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 *
 */
public interface Bookmarks {

}


/* 
$Log: Bookmarks.java,v $
Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/