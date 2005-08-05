/*$Id: XmlDocumentResultTransformerSet.java,v 1.5 2005/08/05 11:46:55 nw Exp $
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


/** set of transformers for (String) xml documents
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
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
        setHtmlTransformer(Xml2XhtmlTransformer.getInstance());
    }
    

}


/* 
$Log: XmlDocumentResultTransformerSet.java,v $
Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/05/12 15:59:12  clq2
nww 1111 again

Revision 1.2.20.1  2005/05/11 14:25:23  nw
javadoc, improved result transformers for xml

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/