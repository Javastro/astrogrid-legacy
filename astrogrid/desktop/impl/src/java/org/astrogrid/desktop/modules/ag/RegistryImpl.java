/*$Id: RegistryImpl.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.registry.NoResourcesFoundException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.store.Ivorn;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

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
    public RegistryImpl(CommunityInternal community) {
        super();
        this.community = community;
        community.addUserLoginListener(this);
    }
   protected final CommunityInternal community;
    private RegistryService reg;

    protected RegistryService getReg() {
        if (reg == null) {
        reg = community.getEnv().getAstrogrid().createRegistryClient();
        } 
        return reg;
    }
    
    public URL resolveIdentifier(URI ivorn) throws NotFoundException, ServiceException{
            try {
                return new URL(getReg().getEndPointByIdentifier(new Ivorn(ivorn.toString())));
            } catch (MalformedURLException e) {
                throw new NotFoundException(e);
            } catch (NoResourcesFoundException e) {
                throw new NotFoundException(e);
            } catch (RegistryException e) {
                throw new ServiceException(e); 
            } catch (URISyntaxException e) {
                throw new ServiceException(e); // wonder what would cause this?
            }
 

    }
    

    public Document getRecord(URI ivorn) throws NotFoundException, ServiceException {
    
            try {
                return getReg().getResourceByIdentifier(new Ivorn(ivorn.toString() ));
            } catch (NoResourcesFoundException e) {
                throw new NotFoundException(e);
            } catch (RegistryException e) {
                throw new ServiceException(e);
            } catch (URISyntaxException e) {
                throw new ServiceException(e);
            }
    }
    

    /**
     * @see org.astrogrid.acr.astrogrid.Registry#getResourceData(java.lang.String)
     */
    public ResourceInformation getResourceInformation(URI ivorn) throws  NotFoundException, ServiceException {
            try {
                ResourceData rd =  getReg().getResourceDataByIdentifier(new Ivorn(ivorn.toString()));
                return new ResourceInformation(
                        new URI(rd.getIvorn().toString())
                        ,rd.getTitle()
                        ,rd.getDescription()
                        ,rd.getAccessURL());
            } catch (NoResourcesFoundException e) {
                throw new NotFoundException(e);
            } catch (RegistryException e) {
                throw new ServiceException(e);
            } catch (URISyntaxException e) {
                throw new ServiceException(e);
            }
    }
    
    

    public Document searchForRecords(String adql) throws ServiceException  {

        try {
            return getReg().searchFromSADQL(adql);
        } catch (NoResourcesFoundException e) {
            // shouldn't ever return this - should return an empty result document instead.
            logger.fatal("search is never expected to return a 'NoResourcesFoundException'",e);
            try {
                return XMLUtils.newDocument(); // @todo initialize this object.
            } catch (ParserConfigurationException e1) {
                throw new ServiceException(e); //@todo could do wtih a different exception type here?
            }
        } catch (RegistryException e) {
            throw new ServiceException(e);
        }

    }
    
    /**@todo add declaration of common prefixes to front of query?
     * @see org.astrogrid.acr.astrogrid.Registry#xquery(java.lang.String)
     */
    public Document xquery(String xquery) throws ServiceException {
        
            try {
                return getReg().xquerySearch(xquery);
            } catch (RegistryException e) {
                throw new ServiceException(e);
            }
            
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
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.7  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/05/12 15:59:11  clq2
nww 1111 again

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