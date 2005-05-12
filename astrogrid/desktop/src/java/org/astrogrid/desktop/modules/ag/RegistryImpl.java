/*$Id: RegistryImpl.java,v 1.4 2005/05/12 15:37:43 clq2 Exp $
 * Created on 02-Feb-2005
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
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.store.Ivorn;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import java.net.URISyntaxException;

/** implementation of the registry component
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Feb-2005
 *
 */
public class RegistryImpl implements Registry, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryImpl.class);

    /** Construct a new Registry
     * 
     */
    public RegistryImpl(Community community) {
        super();
        this.community = community;
        community.addUserLoginListener(this);
    }
    protected final Community community;
    private RegistryService reg;

    protected RegistryService getReg() {
        if (reg == null) {
        reg = community.getEnv().getAstrogrid().createRegistryClient();
        } 
        return reg;
    }
    
    public String resolveIdentifier(String ivorn) throws RegistryException, URISyntaxException {

        if (ivorn.startsWith("ivo://")) {
            return getReg().getEndPointByIdentifier(new Ivorn(ivorn));
        } else {
        return getReg().getEndPointByIdentifier(ivorn);
        }

    }
    

    public String getRecord(String ivorn) throws RegistryException, URISyntaxException {

        Document dom = null;
        if (ivorn.startsWith("ivo://")){
            dom = getReg().getResourceByIdentifier(new Ivorn(ivorn));
        } else {
            dom=  getReg().getResourceByIdentifier(ivorn);
        }
        return XMLUtils.DocumentToString(dom);        
    }
    

    /**
     * @see org.astrogrid.acr.astrogrid.Registry#getResourceData(java.lang.String)
     */
    public ResourceData getResourceData(String ivorn) throws RegistryException, URISyntaxException {
        if (ivorn.startsWith("ivo://")) {
            return getReg().getResourceDataByIdentifier(new Ivorn(ivorn));
        } else {
            return getReg().getResourceDataByIdentifier(new Ivorn("ivo://" + ivorn));
        }
    }
    
    

    public String search(String adql) throws RegistryException {

        Document dom = getReg().searchFromSADQL(adql);
        return XMLUtils.DocumentToString(dom);

    }

    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
    }

    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent e) {
        reg = null;
    }

    
}


/* 
$Log: RegistryImpl.java,v $
Revision 1.4  2005/05/12 15:37:43  clq2
nww 1111

Revision 1.3.8.2  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.3.8.1  2005/05/11 09:52:45  nw
exposed new registry methods

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/