/*$Id: SimpleTranslatorMap.java,v 1.3 2004/01/15 12:27:34 nw Exp $
 * Created on 26-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.spi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axis.types.URI;
import org.astrogrid.datacenter.axisdataserver.types.Language;

/** Obvious implementation of a {@link TranslatorMap}
 * <p>
 * Should be sufficient for most cases.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2003
 *
 */
public class SimpleTranslatorMap implements TranslatorMap {

    /**
     * 
     */
    public SimpleTranslatorMap() {
        codeMap = new HashMap();
        schemaMap = new HashMap();
    }
    
    /** map that contains all our translators */
    protected Map codeMap;
    /** map that contains any schemas */
    protected Map schemaMap;

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.TranslatorMap#lookup(java.lang.String)
     */
    public Translator lookup(String namespace) {
        return (Translator)codeMap.get(namespace);
    }

    /** add a translator mapping to the store */
    public void add(String namespace, String schemaURL, Translator trans) {
        if (namespace == null) {
            throw new IllegalArgumentException("namespace cannot be null");
        }
        if (trans == null) {
            throw new IllegalArgumentException("translator cannot be null");
        }
        codeMap.put(namespace,trans);
        if (schemaURL != null) {
            schemaMap.put(namespace,schemaURL);
        }
        
    }
    
    /** add a translator mapping to the store */
    public void add(String namespace, Translator trans) {
        this.add(namespace,null,trans);
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.TranslatorMap#list()
     */
    public Language[] list() {
        try {
            int size = codeMap.size();
            Language[] result = new Language[size];
            int pos = 0;
            for (Iterator i = codeMap.entrySet().iterator();
                i.hasNext();
                pos++) {
                Map.Entry e = (Map.Entry) i.next();
                Language lang = new Language();
                lang.setNamespace(new URI((String) e.getKey()));
                lang.setImplementingClass(e.getValue().getClass().getName());
                String schema = (String) schemaMap.get(e.getKey());
                if (schema != null) {
                    lang.setSchemaLocation(new URI((String) schema));
                }

                result[pos] = lang;
            }
            return result;
        }catch (URI.MalformedURIException e) {
            throw new IllegalStateException("URI format exception: shouldn't really happen: " + e.getMessage());
        }
    }

}


/* 
$Log: SimpleTranslatorMap.java,v $
Revision 1.3  2004/01/15 12:27:34  nw
improved documentation

Revision 1.2  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.1.10.1  2004/01/07 11:51:07  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.1  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.
 
*/