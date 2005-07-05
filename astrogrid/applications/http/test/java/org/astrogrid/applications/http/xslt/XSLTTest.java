/*
 * Created on Aug 6, 2004
 *
 */
package org.astrogrid.applications.http.xslt;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Sandbox to play around with xslt ideas
 * 
 * @author jdt
 */
public class XSLTTest extends TestCase {
    public void testMarshall() throws MarshalException, ValidationException, ParserConfigurationException, IOException,
            TransformerException {
        //load up test docs
        InputStream toolFileStream = this.getClass().getResourceAsStream("tool-eg.xml");
        Reader reader = new InputStreamReader(toolFileStream);
        Tool tool = Tool.unmarshalTool(reader);

        InputStream appFileStream = this.getClass().getResourceAsStream("webapp-eg.xml");
        Reader reader2 = new InputStreamReader(appFileStream);
        CeaHttpApplicationType app = CeaHttpApplicationType.unmarshalCeaHttpApplicationType(reader2);


        //Construct a document combining the Tool and WebHttpApplication
        // documents
        Writer pretransWriter = new PrintWriter(new FileOutputStream("pretrans.xml"));
        Writer postTransWriter = new PrintWriter(new FileOutputStream("posttranstrans.xml"));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document callingDocument = builder.getDOMImplementation().createDocument(null, "http-app", null);
        
        
        Element root = callingDocument.getDocumentElement();
        
     //   Attr namespace = callingDocument.createAttribute("xmlns:agpd");
     //   namespace.setNodeValue("http://www.astrogrid.org/schema/AGParameterDefinition/v1");
     //   root.setAttributeNode(namespace);

        
    //    callingDocument.insertBefore(namespace, root);
        
        
        Marshaller marshaller = new Marshaller(root);
        //marshaller.setNSPrefixAtRoot(true);
        marshaller.setNamespaceMapping("ceas","http://www.ivoa.net/xml/CEAService/v0.2");
        marshaller.setNamespaceMapping("vr","http://www.ivoa.net/xml/VOResource/v0.10");
        marshaller.setNamespaceMapping("ceab","http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1");
        marshaller.setNamespaceMapping("agw","http://www.astrogrid.org/schema/AGWorkflow/v1");
        marshaller.setNamespaceMapping("agpd","http://www.astrogrid.org/schema/AGParameterDefinition/v1");
        marshaller.marshal(app);
        marshaller.marshal(tool);

        OutputFormat format = new OutputFormat(callingDocument);
        XMLSerializer output = new XMLSerializer(pretransWriter, format);
        //output.setNamespaces(false);
        output.serialize(callingDocument);

       

    }

    /**
     * @param postTransWriter
     * @param callingDocument
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    public void testTransform() {/*
        TransformerFactory xformFactory = TransformerFactory.newInstance();
        InputStream xslFileStream = this.getClass().getResourceAsStream("DefaultInputParameters.xsl");
        Source xsl = new StreamSource(new BufferedReader(new InputStreamReader(xslFileStream)));
        Transformer stylesheet = xformFactory.newTransformer(xsl);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document callingDocument = builder.getDOMImplementation().createDocument(null, "http-app", null);
 
        
        Source request = new DOMSource(callingDocument);
        DOMResult response = new DOMResult();
        stylesheet.transform(request, response);

        Node node = response.getNode();
        Document callingDocument2 = (Document) node;
        OutputFormat format2 = new OutputFormat(callingDocument2);
        XMLSerializer output2 = new XMLSerializer(postTransWriter, format2);
        output2.serialize(callingDocument2);*/
    }

}