/*$Id: SolarEventKeys.java,v 1.1 2004/08/12 13:33:34 nw Exp $
 * Created on 12-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration.itn6.solarevent;

/**naughty interface defining key constants for the solar event science case.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Aug-2004
 *
 */
public interface SolarEventKeys {
    public static final String SEC_APP = "";
    public static final String FITS_APP = "";
    public static final String MPEG_APP = "";
    
    //

    public static final String VOTABLE_SOURCE = "org.astrogrid.localhost/something"; //@todo
    public static final String URL_LIST_SINK = "org.astrogrid.localhost/somethingelse";
}


/* 
$Log: SolarEventKeys.java,v $
Revision 1.1  2004/08/12 13:33:34  nw
added framework of classes for testing the solar event science case.
 
*/