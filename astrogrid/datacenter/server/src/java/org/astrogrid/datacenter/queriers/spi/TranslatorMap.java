/*$Id: TranslatorMap.java,v 1.1 2003/11/27 00:52:58 nw Exp $
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

import org.astrogrid.datacenter.axisdataserver.types._language;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2003
 *
 */
public interface TranslatorMap {
    
    public Translator lookup(String namespace);
    
    /** output the contents of the translator map - supported namespaces, schemas and implementing classes */
    public _language[] list();
    
 

}


/* 
$Log: TranslatorMap.java,v $
Revision 1.1  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.
 
*/