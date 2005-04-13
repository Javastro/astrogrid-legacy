/*$Id: XmlDocumentResultTransformerSet.java,v 1.2 2005/04/13 12:59:11 nw Exp $
 * Created on 21-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *@todo add stlylesheet to render xml into html.
 */
public class XmlDocumentResultTransformerSet extends DefaultResultTransformerSet {

    /** Construct a new XmlDocumentResultTransformerSet
     * 
     */
    public XmlDocumentResultTransformerSet() {
        super();
        setXmlrpcTransformer(IDTransformer.getInstance());
        setXmlTransformer(IDTransformer.getInstance());
        setPlainTransformer(IDTransformer.getInstance());
        setHtmlTransformer(IDTransformer.getInstance());
    }
    

}


/* 
$Log: XmlDocumentResultTransformerSet.java,v $
Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/