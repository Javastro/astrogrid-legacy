/*$Id: Registry.java,v 1.3 2005/03/14 11:59:04 nw Exp $
 * Created on 02-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import java.net.URISyntaxException;

import org.astrogrid.desktop.service.conversion.*;
import org.astrogrid.desktop.service.annotation.*;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.store.Ivorn;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Feb-2005
 * @todo add more operations, and more useful types..
 * @@ServiceDoc("registry","interrogate and browse the registry")
 *
 */
public class Registry {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Registry.class);

    /** Construct a new Registry
     * 
     */
    public Registry(Community community) {
        super();
        this.community = community;
    }
    protected final Community community;

    protected RegistryService getReg() {
        return community.getEnv().getAstrogrid().createRegistryClient();
    }
    
    /** @@MethodDoc("resolveIdentifier","Resolve a service ivorn to a web service endpoint")
     * @@.return ReturnDoc("url of endpoint")
     * @@.ivorn ParamDoc("ivorn","identifier of the application to resolve")
     * @param ivorn
     * @return
     * @throws RegistryException
     * @throws URISyntaxException
     */
    public String resolveIdentifier(String ivorn) throws RegistryException, URISyntaxException {
        if (ivorn.startsWith("ivo://")) {
            return getReg().getEndPointByIdentifier(new Ivorn(ivorn));
        } else {
        return getReg().getEndPointByIdentifier(ivorn);
        }
    }
    
    /** @@MethodDoc("getRecord","Retreive the registry entry for an ivorn")
     *  @@.return ReturnDoc("XML of registry entry",rts=XmlDocumentResultTransformerSet.getInstance())
     * @@.ivorn ParamDoc("ivorn","identifier of the registry entry to return")
     * @param ivorn
     * @return
     * @throws RegistryException
     * @throws URISyntaxException
     */
    public String getRecord(String ivorn) throws RegistryException, URISyntaxException {
        Document dom = null;
        if (ivorn.startsWith("ivo://")){
            dom = getReg().getResourceByIdentifier(new Ivorn(ivorn));
        } else {
            dom=  getReg().getResourceByIdentifier(ivorn);
        }
        return XMLUtils.DocumentToString(dom);
    }
    /** @@MethodDoc("search","Query the registry")
     * @@.return ReturnDoc("XML query results",rts=XmlDocumentResultTransformerSet.getInstance())
     * @@.xadql ParamDoc("xadql","query to execute (XML form of ADQL)")
     * @param xadql
     * @return
     * @throws RegistryException
     */
    public String search(String xadql) throws RegistryException {
        Document dom = getReg().search(xadql);
        return XMLUtils.DocumentToString(dom);
    }
    
    /** @@MethodDoc("list","list identifiers in the registry")
   * @@.return ReturnDoc("XML query results",rts=XmlDocumentResultTransformerSet.getInstance())
       
     * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
     *@todo make this a list of strings..
     */
  /*  public String list() throws RegistryException {
        Document dom = getReg().listIdentifiers();
        return XMLUtils.DocumentToString(dom);
    }
    */
}


/* 
$Log: Registry.java,v $
Revision 1.3  2005/03/14 11:59:04  nw
fixed code to fit with latest reg and myspace changes.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/