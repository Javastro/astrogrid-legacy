/*$Id: XPathHelper.java,v 1.11 2008/11/04 14:35:47 nw Exp $
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


/**Helpers for working with XPath, namespaces, and registry documents.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Aug-2005
 *
 */
public class XPathHelper {
    /** namespace for xml schema instance */
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    /** namespace for xmlns */
    public static final String XMLNS_NS = "http://www.w3.org/2000/xmlns/";
    /** namespace for the registry inteface v1.0 */
    public final static String VOR_NS = "http://www.ivoa.net/xml/RegistryInterface/v1.0";
    
    /** namespace for voresource v1.0 */
    public final static String VR_NS = "http://www.ivoa.net/xml/VOResource/v1.0";
    
// not required / obsolete.    
//    /** namespace for common execution arcitecture base */
//    public final static String CEAB_NS = "http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1";
//    /** namespace for CEA Service */
//    public final static String CEA_NS = "http://www.ivoa.net/xml/CEAService/v0.2";
//    /** namespace for CEA Parameters */
//    public final static String CEAPD_NS = "http://www.astrogrid.org/schema/AGParameterDefinition/v1"; 
//    /** namespace for tabular database  v0.3*/
//    public final static String TDB_NS = "urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3";
    /** namespace for VODataService v1.0 */
    public final static String VODS_NS = "http://www.ivoa.net/xml/VODataService/v1.0";
    /** namespace for cone search v1.0 */
    public final static String CS_NS = "http://www.ivoa.net/xml/ConeSearch/v1.0"; 
    /** namespace for siap v1.0 */
    public final static String SIA_NS = "http://www.ivoa.net/xml/SIA/v1.0";

    /** namespace for adql v1.0 */
    public final static String ADQL_NS = "http://www.ivoa.net/xml/ADQL/v1.0";

    // list of all namespace names andurls.
    private static String[][] namespaces  = new String[][] {
            new String[] {"xsi",XSI_NS}
            ,new String[]{"vr",VR_NS}
            ,new String[]{"vor",VOR_NS}    
//            ,new String[]{"ceab",CEAB_NS}
//            ,new String[]{"cea",CEA_NS}
//            ,new String[]{"ceapd",CEAPD_NS}
//            ,new String[]{"tdb",TDB_NS}
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

//    /** Create a namespace node for use in xpath. comes pre-initialized with the common registry namespaces - vr, vor, etc.
//     * <p>
//     * default namespace is the vr one.
//     * @return a namespace node
//     * @throws FactoryConfigurationError
//     * @throws ParserConfigurationException
//     * @throws DOMException
//     */
    // unused
//    public  static Element createNamespaceNode() throws FactoryConfigurationError, ParserConfigurationException, DOMException {
//        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
//        fac.setNamespaceAware(true);
//        DocumentBuilder builder = fac.newDocumentBuilder();
//        DOMImplementation impl = builder.getDOMImplementation();        
//       Document namespaceHolder = impl.createDocument(VOR_NS, "f:namespaceMapping",null);
//    
//       // Document namespaceHolder = DomHelper.newDocument();
//        Element namespaceNode = namespaceHolder.getDocumentElement();
//        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns",VR_NS);
//        for (int i = 0; i < namespaces.length; i++) {
//            namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:" + namespaces[i][0],namespaces[i][1]);
//        }
//
//        return namespaceNode;
//    }

    public static String[][] listDefaultNamespaces() {
        return namespaces;
    }
    


}


/* 
$Log: XPathHelper.java,v $
Revision 1.11  2008/11/04 14:35:47  nw
javadoc polishing

Revision 1.10  2008/02/01 07:55:51  nw
commented out obsolebits

Revision 1.9  2008/01/21 09:53:58  nw
Incomplete - task 134: Upgrade to reg v1.0

Revision 1.8  2007/06/18 16:27:15  nw
javadoc

Revision 1.7  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.6  2006/08/31 21:28:59  nw
doc fix.

Revision 1.5  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.4.30.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.4.30.1  2006/03/28 13:47:35  nw
first webstartable version.

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