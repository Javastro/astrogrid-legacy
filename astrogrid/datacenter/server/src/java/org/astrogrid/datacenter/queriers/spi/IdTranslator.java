/*$Id: IdTranslator.java,v 1.3 2004/01/15 12:18:07 nw Exp $
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

/** The simplest possible {@link Translator} - returns its input argument unchanged
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Nov-2003
 *
 */
public class IdTranslator implements Translator {

    /**
     * 
     */
    public IdTranslator() {

    }

/**
 * Returns the input element unchanged
 * @param e the input query document
 * @return the input query document, unchanged */
    public Object translate(Element e)  {
        return e;
    }

/**  
 * returns the {@link org.w3c.dom.Element} class object 
 */
    public Class getResultType() {
        return Element.class;
    }

    /** 
     * A dummy implementation of {@link TranslatorMap} that always returns an {@link IdTranslator}, for any namespace
     * @author Noel Winstanley nw@jb.man.ac.uk 27-Nov-2003
     *
     */
    public static class DummyTranslatorMap implements TranslatorMap {

        /** @return a new {@link IdTranslator} */
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
Revision 1.3  2004/01/15 12:18:07  nw
improved documentation

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