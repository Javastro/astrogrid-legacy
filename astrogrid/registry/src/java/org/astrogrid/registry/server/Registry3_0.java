/*
 * Created on 20-May-2003
 *
 * @author=Elizabeth Auden
 */
 
package org.astrogrid.registry.server;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import de.gmd.ipsi.xql.XQL;
import de.gmd.ipsi.xql.XQLResult;
//import de.gmd.ipsi.domutil.DOMUtil;
//import de.gmd.ipsi.domutil.DOMParseException;
import de.gmd.ipsi.domutil.XMLWriter;

import java.io.InputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
//import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import org.apache.axis.utils.XMLUtils;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;


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

	String response = "";
	String queryResponse = "";
		
   public Document xmlQuery(String query,Document fileDocument) {
		
		/**
		 * The xmlQuery method accepts as XQL query as input.  The location
		 * of the registry file is determined, and the XQL query is executed
		 * against the registry XML file.
		 */
      //System.out.println("the fileDoc in regi3_0" + XMLUtils.DocumentToString(fileDocument));

        /**
         * First, determine location of the registry XML file.  It is assumed 
         * that "user.dir" is $TOMCAT_HOME/bin, and that the parameters file 
         * containing the registry URL is stored in $TOMCAT_HOME/webapps/org/
         * astrogrid. 
         */
      String registryFilename = null;
		String response = "";
		boolean goodParameters = true;
	   Document resultDoc = null;
      String vodesc = "<?xml version='1.0' encoding='UTF-8'?>" + 
      "<VODescription" + 
        " xmlns='http://www.ivoa.net/xml/VOResource/v0.9'" +
        " xmlns:vr='http://www.ivoa.net/xml/VOResource/v0.9'" +
        " xmlns:vc='http://www.ivoa.net/xml/VOCommunity/v0.2'" +
        " xmlns:vg='http://www.ivoa.net/xml/VORegistry/v0.2'" +
        " xmlns:vs='http://www.ivoa.net/xml/VODataService/v0.4'" +
        " xmlns:vt='http://www.ivoa.net/xml/VOTable/v0.1'" +
        " xmlns:cs='http://www.ivoa.net/xml/ConeSearch/v0.2'" +
        " xmlns:sia='http://www.ivoa.net/xml/SIA/v0.6'" +
        " xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'" +
        " xsi:schemaLocation='http://www.ivoa.net/xml/VOResource/v0.9" +
                            " http://www.ivoa.net/xml/VOResource/VOResource-v0.9.xsd" +
                            " http://www.ivoa.net/xml/VOCommunity/v0.2" +
                            " http://www.ivoa.net/xml/VOCommunity/VOCommunity-v0.2.xsd" +
                            " http://www.ivoa.net/xml/VORegistry/v0.2" +
                            " http://www.ivoa.net/xml/VORegistry/VORegistry-v0.2.xsd" +
                            " http://www.ivoa.net/xml/ConeSearch/v0.2" +
                            " http://www.ivoa.net/xml/ConeSearch/ConeSearch-v0.2.xsd" +
                            " http://www.ivoa.net/xml/SIA/v0.6" +
                            " http://www.ivoa.net/xml/SIA/SIA-v0.6.xsd'></VODescription>";
      
      try {
         Reader reader2 = new StringReader(vodesc);
         InputSource inputSource = new InputSource(reader2);         
         DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         resultDoc = registryBuilder.parse(inputSource);
      }catch(Exception e) {
         e.printStackTrace();
      }
      
      
      DocumentFragment df  = resultDoc.createDocumentFragment();                        
      //Document resultDoc = DOMUtil.createDocument();
		//Element root = resultDoc.createElement("queryResponse");
		//resultDoc.appendChild(root);
				
		/**
		 * Once the registry file has been successfully found and the 
		 * DOM tree built, execute the XQL query against the registry.
		 */
      System.out.println("lets try the XQL.execute query");
   	try{
	  	  //XQL.execute(query, fileDocument, root);
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
			response = "<error>Malformed XQL statement.</error>";
      }

		return resultDoc;
	}

}