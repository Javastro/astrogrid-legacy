/*$Id: Lookout.java,v 1.7 2008/09/25 16:02:09 nw Exp $
 * Created on 27-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.ui;


/** Control the Lookout UI. (Unimplemented)

 * @exclude
 *  @deprecated
 * @service userInterface.lookout
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Oct-2005
 *
 */
@Deprecated
public interface Lookout  {
    public void show();
    public void hide();
    public void refresh();
}

/* 
 $Log: Lookout.java,v $
 Revision 1.7  2008/09/25 16:02:09  nw
 documentation overhaul

 Revision 1.6  2007/06/27 11:08:36  nw
 public apis for new ui components.

 Revision 1.5  2007/01/24 14:04:45  nw
 updated my email address

 Revision 1.4  2006/10/12 02:22:33  nw
 fixed up documentaiton

 Revision 1.3  2006/02/02 14:19:48  nw
 fixed up documentation.

 Revision 1.2  2005/11/24 01:18:42  nw
 merged in final changes from release branch.

 Revision 1.1.2.1  2005/11/23 18:07:22  nw
 improved docs.

 Revision 1.1  2005/11/11 10:08:52  nw
 added new interface

 Revision 1.1  2005/11/01 09:19:46  nw
 messsaging for applicaitons.
 
 */