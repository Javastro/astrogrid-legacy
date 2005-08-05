/*$Id: DocumentResultTransformerSet.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 04-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.TransformerUtils;
import org.w3c.dom.Document;

/** set of transformers for DOM Document objects
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Aug-2005
 *
 */
public class DocumentResultTransformerSet extends DefaultResultTransformerSet {

    /** Construct a new DomResultTransformerSet
     * 
     */
    public DocumentResultTransformerSet() {
        super();
        setXmlrpcTransformer(DocumentTransformer.getInstance());
        setXmlTransformer(DocumentTransformer.getInstance());
        setPlainTransformer(DocumentTransformer.getInstance());
        setHtmlTransformer(
                TransformerUtils.chainedTransformer(
                        DocumentTransformer.getInstance()
                        ,Xml2XhtmlTransformer.getInstance()
                        )
                );
    }
    
    public static class DocumentTransformer implements Transformer {
        private DocumentTransformer() {}
        /**
         * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
         */
        public Object transform(Object arg0) {
            return XMLUtils.DocumentToString((Document)arg0);
        }
        private static Transformer theInstance;
        public static Transformer getInstance() {
            if (theInstance == null) {
                theInstance = new DocumentTransformer();
            }
            return theInstance;
        }
    }

}


/* 
$Log: DocumentResultTransformerSet.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/