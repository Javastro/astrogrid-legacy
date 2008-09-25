/*$Id: HelioScope.java,v 1.4 2008/09/25 16:02:09 nw Exp $
 * Created on 12-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ui;

/**Control  HelioScope.
 * 
 *@exclude
 * @deprecated
 * @author Kevin Benson
 */
@Deprecated
public interface HelioScope {
    /** display a new instance of helioscope */
    public void show() ;
}


/* 
$Log: HelioScope.java,v $
Revision 1.4  2008/09/25 16:02:09  nw
documentation overhaul

Revision 1.3  2007/06/27 11:08:35  nw
public apis for new ui components.

Revision 1.2  2006/03/16 09:14:21  KevinBenson
usually comment/clean up type changes such as siap to stap

Revision 1.1  2006/03/13 14:47:21  KevinBenson
This is the first rough draft of Helioscope which deals with the STAP spec.

 
*/