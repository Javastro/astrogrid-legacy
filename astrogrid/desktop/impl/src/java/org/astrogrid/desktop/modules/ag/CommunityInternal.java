/*$Id: CommunityInternal.java,v 1.4 2006/04/18 23:25:44 nw Exp $
 * Created on 26-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.ui.script.ScriptEnvironment;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2005
 *other clients of this interface (e.g. ui code) should move to Community, and use the acr interfaces to access functionality previously called in getEnv
 */
interface CommunityInternal  extends Community{
    /** retreive the scripting environment for a user
     * 
     * <p> this is a powerful toolkit containing pre-created delegates and environment objects (e.g. user 
     * home ivorn). Very useful for implementing further plugins.
     * <p>
     * If the user is not logged in when this method is called, {@link #guiLogin()} is called to force the user to login before this method returns
     * if the user fails to login, this method returns null. 
     * @return a script environment, which may be null if the user fails to login.
     * @deprecated. don't use this if it can be helped.
     */ 
   public abstract ScriptEnvironment getEnv();
    


}


/* 
$Log: CommunityInternal.java,v $
Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.56.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.3  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/