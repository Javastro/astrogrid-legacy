/*$Id: IdTranslator.java,v 1.2 2004/01/13 00:33:14 nw Exp $
 * Created on 27-Nov-2003
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
import org.w3c.dom.Element;

/** Simplest possible translator - returns its Element argument unchanged
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Nov-2003
 *
 */
public class IdTranslator implements Translator {

    /**
     * 
     */
    public IdTranslator() {

    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.Translator#translate(org.w3c.dom.Element)
     */
    public Object translate(Element e) throws Exception {
        return e;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.Translator#getResultType()
     */
    public Class getResultType() {
        return Element.class;
    }

    /** 
     * Dummy translator map - always returns an Id translator, for any namespace
     * @author Noel Winstanley nw@jb.man.ac.uk 27-Nov-2003
     *
     */
    public static class DummyTranslatorMap implements TranslatorMap {

        /** always retirns an Idtranslator */
        public Translator lookup(String namespace) {
            return new IdTranslator();
        }

        /** returns an empty array */
        public Language[] list() {
            return new Language[0];
        }
    }

}


/* 
$Log: IdTranslator.java,v $
Revision 1.2  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.1.10.1  2004/01/07 11:51:07  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.1  2003/11/27 17:28:09  nw
finished plugin-refactoring
 
*/