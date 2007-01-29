/*$Id: ApplicationDescriptionEnvironmentFactory.java,v 1.3 2007/01/29 11:11:36 nw Exp $
 * Created on 23-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

/** work-around for appDescEnv's irritating lack of no-args constructor or
 * interface.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 23-Mar-2006
 *
 */
public class ApplicationDescriptionEnvironmentFactory implements IApplicationDescriptionEnvironmentFactory {

    public ApplicationDescriptionEnvironmentFactory(IdGen id, AppAuthorityIDResolver auth, ProtocolLibrary lib) {
        
       this.desc = new ApplicationDescriptionEnvironment(id,lib,auth);
    }

    private final ApplicationDescriptionEnvironment desc;
    public ApplicationDescriptionEnvironment getInstance() {
        return desc;
    }

}


/* 
$Log: ApplicationDescriptionEnvironmentFactory.java,v $
Revision 1.3  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/03/28 13:47:35  nw
first webstartable version.
 
*/