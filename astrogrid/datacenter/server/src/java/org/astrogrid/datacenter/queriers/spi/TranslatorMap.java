/*$Id: TranslatorMap.java,v 1.3 2004/01/15 12:00:55 nw Exp $
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

import org.astrogrid.datacenter.axisdataserver.types.Language;

/** A container for language translators
 * <p>
 * Allows access to translators keyed by xmlns namespace IDs.
 * @see Translator
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2003
 *
 */
public interface TranslatorMap {
    
    /** 
     * Select an appropriate translator to convert a query document, based on its namespace id. 
     * @param namespace the xmlns namespace of the query document
     * @return a translator that will convert from this form of query document to the required intermediate representation.
     */
    public Translator lookup(String namespace);
    
    /** output the contents of the translator map - supported namespaces, schemas and implementing classes */
    public Language[] list();
    
 

}


/* 
$Log: TranslatorMap.java,v $
Revision 1.3  2004/01/15 12:00:55  nw
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