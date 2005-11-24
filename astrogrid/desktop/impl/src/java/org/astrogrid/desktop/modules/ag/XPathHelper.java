/*$Id: XPathHelper.java,v 1.4 2005/11/24 01:13:24 nw Exp $
 * Created on 17-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

/** Class of Helper methods for working with XPath, namespaces, and registry documents in particular.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Aug-2005
 *
 */
public class XPathHelper {
    /** namespace for xml schema instance */
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    /** namespace for the registry inteface v0.1 */
    public final static String VOR_NS = "http://www.ivoa.net/xml/RegistryInterface/v0.1";
    
    /** namespace for voresource v0.10 */
    public final static String VR_NS = "http://www.ivoa.net/xml/VOResource/v0.10";
    /** namespace for common execution arcitecture base */
    public final static String CEAB_NS = "http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1";
    /** namespace for CEA Service */
    public final static String CEA_NS = "http://www.ivoa.net/xml/CEAService/v0.2";
    /** namespace for CEA Parameters */
    public final static String CEAPD_NS = "http://www.astrogrid.org/schema/AGParameterDefinition/v1"; 
    /** namespace for tabular database  v0.3*/
    public final static String TDB_NS = "urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3";
    /** namespace for VODataService v0.5*/
    public final static String VODS_NS = "http://www.ivoa.net/xml/VODataService/v0.5";
    /** namespace for cone search v0.3 */
    public final static String CS_NS = "http://www.ivoa.net/xml/ConeSearch/v0.3"; 
    /** namespace for siap v0.7 */
    public final static String SIA_NS = "http://www.ivoa.net/xml/SIA/v0.7";

    // list of all namespace names andurls.
    private static String[][] namespaces  = new String[][] {
            new String[] {"xsi",XSI_NS}
            ,new String[]{"vr",VR_NS}
            ,new String[]{"vor",VOR_NS}    
            ,new String[]{"ceab",CEAB_NS}
            ,new String[]{"cea",CEA_NS}
            ,new String[]{"ceapd",CEAPD_NS}
            ,new String[]{"tdb",TDB_NS}
            ,new String[]{"vods",VODS_NS}
            ,new String[]{"cs",CS_NS}
            ,new String[]{"sia",SIA_NS}
    };    
    /** Construct a new XPathHelper
     * 
     */
    private XPathHelper() {
        super();
    }

    /** Create a namespace node for use in xpath. comes pre-initialized with the common registry namespaces - vr, vor, etc.
     * <p>
     * default namespace is the vr one.
     * @return
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws DOMException
     */
    public  static Element createNamespaceNode() throws FactoryConfigurationError, ParserConfigurationException, DOMException {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        fac.setNamespaceAware(true);
        DocumentBuilder builder = fac.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();        
       Document namespaceHolder = impl.createDocument(VOR_NS, "f:namespaceMapping",null);
    
       // Document namespaceHolder = DomHelper.newDocument();
        Element namespaceNode = namespaceHolder.getDocumentElement();
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns",VR_NS);
        for (int i = 0; i < namespaces.length; i++) {
            namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:" + namespaces[i][0],namespaces[i][1]);
        }
        /*
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xsi",XSI_NS);
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vr",VR_NS);
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vor", VOR_NS);

        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:ceab", CEAB_NS);     
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:cea", CEA_NS);  
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:ceapd", CEAPD_NS);

        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:tdb", TDB_NS);     
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vods", VODS_NS);   

        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:cs", CS_NS);           
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:sia", SIA_NS);
        */ 
        return namespaceNode;
    }

    public static String[][] listDefaultNamespaces() {
        return namespaces;
    }
    


}


/* 
$Log: XPathHelper.java,v $
Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.14.1  2005/11/23 04:52:39  nw
improved namespace datastructure.

Revision 1.3  2005/10/18 16:53:13  nw
added siap and cone namespaces

Revision 1.2  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/