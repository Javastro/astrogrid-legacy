/*
 * Created on 20-May-2003
 *
 * @author=Elizabeth Auden
 */
 
package org.astrogrid.registry.server;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import de.gmd.ipsi.xql.XQL;

//import org.w3c.dom.Element;
//import de.gmd.ipsi.xql.XQLResult;
//import de.gmd.ipsi.domutil.DOMUtil;
//import de.gmd.ipsi.domutil.DOMParseException;
//import de.gmd.ipsi.domutil.XMLWriter;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.InputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.io.File;
//import org.apache.axis.utils.XMLUtils;
//import java.io.Reader;
//import java.io.StringReader;
//import org.xml.sax.InputSource;

import org.astrogrid.registry.common.RegistryConfig;
import org.astrogrid.registry.common.versionNS.IRegistryInfo;


/**
 * The Registry3_0 class accepts an XQL query from the
 * QueryParser3_0 class and executes the query against the registry
 * XML file, registry_1_1.xml.  The registry response is returned in the 
 * same XML format as the registry file itself.
 * 
 * - Elizabeth Auden, 24 October 2003
 *
 */

public class Registry3_0 {

   public Document xmlQuery(String query,Document fileDocument) {
		
		/**
		 * The xmlQuery method accepts as XQL query as input.  The location
		 * of the registry file is determined, and the XQL query is executed
		 * against the registry XML file.
		 */

      String errorMessage = null;
      
      RegistryConfig.loadConfig();
      IRegistryInfo iri = RegistryConfig.loadRegistryInfo();
      Document resultDoc = iri.getDocument();

      Document doc = null;
      DocumentBuilder regBuilder = null;
      
      try {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         regBuilder = dbf.newDocumentBuilder(); 
      }catch(Exception e) {
         e.printStackTrace();
      }
      
      if(fileDocument == null) {
         errorMessage = "Could not load Registry to perform the query.";        
      }
            
      DocumentFragment df  = resultDoc.createDocumentFragment();                        
      //Document resultDoc = DOMUtil.createDocument();
		//Element root = resultDoc.createElement("queryResponse");
		//resultDoc.appendChild(root);
				
		/**
		 * Once the registry file has been successfully found and the 
		 * DOM tree built, execute the XQL query against the registry.
		 */
      if(fileDocument != null) {
      	try{
   	  	  
           XQL.execute(query, fileDocument, df);
           if(df.hasChildNodes() && df.getFirstChild().hasChildNodes()) {
              Node firstRoot = df.getFirstChild();
              NodeList nl = firstRoot.getChildNodes();
              while(nl.getLength() > 0) {           
                 resultDoc.getDocumentElement().appendChild(nl.item(0));
              }           
           }
   		}catch (Exception e){
            e.printStackTrace();
            errorMessage = "The XQuery statement seems to be malformed or incorrect, The XQL query = " + query;
         }
      }
      
      if(errorMessage != null && errorMessage.trim().length() > 0) {
         if(regBuilder != null) {
            doc = regBuilder.newDocument();
            Element elem = doc.createElement("error");
            elem.appendChild(doc.createTextNode(errorMessage));
            doc.appendChild(elem);
            return doc;
         }//if   
      }//if      

		return resultDoc;
	}

}