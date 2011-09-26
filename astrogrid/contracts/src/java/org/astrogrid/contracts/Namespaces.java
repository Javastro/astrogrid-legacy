/*
 * $Id: Namespaces.java,v 1.9 2011/09/26 11:36:59 KevinBenson Exp $
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
 * Enumerates the "current" namespaces. This gives symbolic names to the most up to date namespace URIs
 * used within schema in astrogrid, as well as providing a default prefix suggestion for the namespace URI.
 * 
 * Note that this class should explictly avoid making references to obsolete namespaces.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public final class Namespaces {

    static final Map<String, Namespaces> prefixes = new HashMap<String, Namespaces>(); 
    static final Map<String, Namespaces> namespaces = new HashMap<String, Namespaces>();
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
    public static final Namespaces XSD = new Namespaces("xsd","http://www.w3.org/2001/XMLSchema");
    public static final Namespaces RI = new Namespaces("ri","http://www.ivoa.net/xml/RegistryInterface/v1.0");
    public static final Namespaces REG = new Namespaces("reg","http://www.ivoa.net/xml/VORegistry/v1.0");
    public static final Namespaces VR = new Namespaces("vr","http://www.ivoa.net/xml/VOResource/v1.0");
    public static final Namespaces VS = new Namespaces("vs","http://www.ivoa.net/xml/VODataService/v1.1");
    public static final Namespaces CEA = new Namespaces("cea","http://www.ivoa.net/xml/CEA/v1.0");
    public static final Namespaces CEAB = new Namespaces("ceab","http://www.ivoa.net/xml/CEA/base/v1.1");
    public static final Namespaces CEAIMPL = new Namespaces("ceaimpl","http://www.astrogrid.org/schema/CEAImplementation/v2.1");
    public static final Namespaces CEAT = new Namespaces("ceat","http://www.ivoa.net/xml/CEA/types/v1.2");
    public static final Namespaces VA = new Namespaces("va","http://www.ivoa.net/xml/VOApplication/v1.0rc1");
    public static final Namespaces UWS = new Namespaces("uws","http://www.ivoa.net/xml/UWS/v1.0");
    public static final Namespaces XLINK = new Namespaces("xlink","http://www.w3.org/1999/xlink");
    public static final Namespaces STC = new Namespaces("stc","http://www.ivoa.net/xml/STC/stc-v1.30.xsd");
    public static final Namespaces TX = new Namespaces("tx","http://www.vamdc.eu/xml/TAPXSAMS/v1.0");
    public static final Namespaces VT = new Namespaces("tx","http://www.vamdc.org/xml/VAMDC-TAP/v1.0");
    public static final Namespaces TXS = new Namespaces("tx","http://www.vamdc.eu/xml/TAPXSAMS/v1.01");
    public static final Namespaces TR = new Namespaces("tr","http://www.ivoa.net/xml/TAP/v0.1");
    public static final Namespaces TAP = new Namespaces("tap","http://www.ivoa.net/xml/TAP/v0.1");  
    public static final Namespaces CS = new Namespaces("cs","http://www.ivoa.net/xml/ConeSearch/v1.0");  
    public static final Namespaces SIA = new Namespaces("sia","http://www.ivoa.net/xml/SIA/v1.0");  
    public static final Namespaces VSTD = new Namespaces("vstd","http://www.ivoa.net/xml/StandardsRegExt/v1.0");  
    public static final Namespaces VOSI_A = new Namespaces("vosi","http://www.ivoa.net/xml/VOSIAvailability/v1.0");
     
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
 * Revision 1.9  2011/09/26 11:36:59  KevinBenson
 * Namespace issue resolved .eu now .org for a vamdc schema.
 *
 * Revision 1.8  2011/09/01 14:03:58  pah
 * result of merge of contracts_pah_2931 branch
 *
 * Revision 1.7  2011/06/08 09:53:49  gtr
 * 2011.2-SNAPSHOT. New VAMDC-TAP schema, taking over from TAPXSAMS.
 *
 * Revision 1.6  2011/06/08 09:41:45  KevinBenson
 * updated to a new tapxsams v1.01 schema
 *
 * Revision 1.5  2011/03/24 12:21:13  KevinBenson
 * New TAPXSAMS and TapRegExt schemas to go into the contracts.
 *
 * Revision 1.4.2.4  2011/09/01 11:40:19  pah
 * add registry SIA and cone namespaces
 *
 * Revision 1.4.2.3  2011/05/17 16:42:47  pah
 * add vosi availability
 *
 * Revision 1.4.2.2  2011/04/08 14:48:02  pah
 * bring StandardRegExt up to date
 *
 * Revision 1.4.2.1  2009/07/15 08:04:47  pah
 * updated ceatypes and cea implementation
 *
 * Revision 1.4  2009/06/20 14:33:32  pah
 * added xlink stc
 *
 * Revision 1.3  2009/06/03 16:57:02  pah
 * correct UWS schema to 0.9.2
 *
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
