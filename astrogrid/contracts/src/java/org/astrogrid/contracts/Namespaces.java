/*
 * $Id: Namespaces.java,v 1.2 2008/09/03 15:10:37 pah Exp $
 * 
 * Created on 17 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.contracts;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates the "current" namespaces. This gives symbolic names to the most up to date namespace URIs used within schema in astrogrid, as well as providing a default prefix suggestion for the namespace URI.
 * 
 * Note that this class should explictly avoid making references to obsolete namespaces.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public final class Namespaces {

    static final Map prefixes = new HashMap(); 
    static final Map namespaces = new HashMap();
    private final String prefix;
    private final String namespace;
    
    private Namespaces(String prefix, String namespace){
	this.namespace = namespace;
	this.prefix = prefix;
	Namespaces.prefixes.put(prefix, this);
	Namespaces.namespaces.put(namespace, this);
    }
// useful namespaces...   
    public static final Namespaces XSI = new Namespaces("xsi","http://www.w3.org/2001/XMLSchema-instance");
    public static final Namespaces RI = new Namespaces("ri","http://www.ivoa.net/xml/RegistryInterface/v1.0");
    public static final Namespaces VR = new Namespaces("vr","http://www.ivoa.net/xml/VOResource/v1.0");
    public static final Namespaces VS = new Namespaces("vs","http://www.ivoa.net/xml/VODataService/v1.0");
    public static final Namespaces CEA = new Namespaces("cea","http://www.ivoa.net/xml/CEA/v1.0");
    public static final Namespaces CEAB = new Namespaces("ceab","http://www.ivoa.net/xml/CEA/base/v1.1");
    public static final Namespaces CEAIMPL = new Namespaces("ceaimpl","http://www.astrogrid.org/schema/CEAImplementation/v2.0");
    public static final Namespaces CEAT = new Namespaces("ceat","http://www.ivoa.net/xml/CEA/types/v1.1");
    public static final Namespaces VA = new Namespaces("va","http://www.ivoa.net/xml/VOApplication/v1.0rc1");
    public static final Namespaces UWS = new Namespaces("uws","http://www.ivoa.net/xml/UWS/v0.9.1");
     
//TODO add the other "current" ones...         
    public static String[] getNamespaceURIs()
    {
	return (String[]) namespaces.keySet().toArray(new String[0]);
    }
    
    public static Namespaces getNameSpaceFromPrefix(String prefix)
    {
	return (Namespaces) prefixes.get(prefix);
    }
    
    public static Namespaces getNameSpaceFromURI(String uri)
    {
	return (Namespaces) namespaces.get(uri);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNamespace() {
        return namespace;
    }
    
    
}


/*
 * $Log: Namespaces.java,v $
 * Revision 1.2  2008/09/03 15:10:37  pah
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 * result of merge of pah_contracts_1611 branch
 *
 * Revision 1.1.2.4  2008/08/29 07:19:58  pah
 * UWS updates
 *
 * Revision 1.1.2.3  2008/05/13 16:08:45  pah
 * update namespaces slightly
 *
 * Revision 1.1.2.2  2008/04/11 15:44:46  pah
 * added tentative UWS schema
 *
 * Revision 1.1.2.1  2008/03/19 12:51:02  pah
 * a map of the namespaces - not yet complete only really contains those namespaces of interest to CEA
 *
 */