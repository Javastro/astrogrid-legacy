/*
 * $Id: RegistryInfo.java,v 1.1 2009/06/10 12:40:21 pah Exp $
 * 
 * Created on 9 Jun 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.astrogrid.contracts.StandardIds;

import net.ivoa.resource.Capability;
import net.ivoa.resource.registry.Registry;

/**
 * Convenience class with important registry metadata.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 Jun 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class RegistryInfo {
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(RegistryInfo.class);
    
    private Registry reg;

    public RegistryInfo(Registry regjaxb) {
        this.reg = regjaxb;
    }
    
    public String getId(){
        return reg.getIdentifier();
    }
    
    public String getName(){
        return reg.getTitle();
    }
    
    /**
     * Guess whether this is an astrogrid registry.
     * @return
     */
    public boolean isAstrogridRegistry(){
    return reg.getIdentifier().toLowerCase().contains("astrogrid");
    }
    
    public Set<String> getManagedAuthorities(){
        Set<String> retval = new HashSet<String>();
        for (JAXBElement<String> string : reg.getManagedAuthority()) {
            retval.add(string.getValue());
        }
        return retval;
    }
    
    public URL getEndpoint(){
        URL retval = null;
        for (Capability cap : reg.getCapability()) {
            if(cap.getStandardID().equals(StandardIds.REGISTRY_1_0)){
                try {
                    retval = new URL(cap.getInterface().get(0).getAccessURL().get(0).getValue());
                    break;
                } catch (MalformedURLException e) {
                    logger.error("cannot set registry endpoint", e);
                }
            }
        }
        return retval;
    }

}


/*
 * $Log: RegistryInfo.java,v $
 * Revision 1.1  2009/06/10 12:40:21  pah
 * ASSIGNED - bug 2934: Improve registration page
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2934
 *
 */
