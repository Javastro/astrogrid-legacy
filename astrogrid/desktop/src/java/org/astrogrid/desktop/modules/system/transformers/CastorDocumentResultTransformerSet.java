/*$Id: CastorDocumentResultTransformerSet.java,v 1.6 2005/08/05 11:46:55 nw Exp $
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

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.TransformerUtils;
import org.exolab.castor.xml.Marshaller;

import java.io.StringWriter;


/** 
 * Generate resulls ffrom a castor object
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public class CastorDocumentResultTransformerSet extends DefaultResultTransformerSet {

    /** Construct a new CastorDocumentResultTransformerSet
     * 
     */
    public CastorDocumentResultTransformerSet() {
        super();     
       // setPlainTransformer(TypeStructureTransformer.getInstance());
        setPlainTransformer(CastorDocumentXmlTransformer.getInstance());
        setXmlrpcTransformer(CastorDocumentXmlTransformer.getInstance());
        setXmlTransformer(CastorDocumentXmlTransformer.getInstance());
        setHtmlTransformer(
                TransformerUtils.chainedTransformer(
                        CastorDocumentXmlTransformer.getInstance()
                        ,Xml2XhtmlTransformer.getInstance())); 

    }
    
    public static class CastorDocumentXmlTransformer implements Transformer {

        private CastorDocumentXmlTransformer() { }
        private static Transformer theInstance = new CastorDocumentXmlTransformer();
        public  static Transformer getInstance() { return  theInstance;}
        
        public Object transform(Object arg0) {
            try {
            StringWriter sw = new StringWriter();
           Marshaller.marshal(arg0,sw);
           return sw.toString();
            } catch (Exception e) {
                throw new RuntimeException("Could not marshal castor document",e);
            }
        }
    }
    

}


/* 
$Log: CastorDocumentResultTransformerSet.java,v $
Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/06/08 14:51:59  clq2
1111

Revision 1.2.20.3  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.2.20.2  2005/05/12 12:42:48  nw
finished core applications functionality.

Revision 1.2.20.1  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/