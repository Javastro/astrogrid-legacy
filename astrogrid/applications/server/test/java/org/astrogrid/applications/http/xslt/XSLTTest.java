/*
 * Created on Aug 6, 2004
 *
 */
package org.astrogrid.applications.http.xslt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.applications.description.registry.NamespacePrefixMapperImpl;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Sandbox to play around with xslt ideas
 *
 * @author jdt
 */
public class XSLTTest extends TestCase {
    public void testMarshall() throws  ParserConfigurationException, IOException,
            TransformerException, JAXBException, MetadataException, SAXException {
	JAXBContext jc = CEAJAXBContextFactory.newInstance();
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        //load up test docs
        InputStream toolFileStream = this.getClass().getResourceAsStream("tool-eg.xml");
        Reader reader = new InputStreamReader(toolFileStream);
        Tool tool = CEAJAXBUtils.unmarshall(reader, Tool.class);

        InputStream appFileStream = this.getClass().getResourceAsStream("webapp-eg.xml");
        Reader reader2 = new InputStreamReader(appFileStream);
        Unmarshaller um = jc.createUnmarshaller();
        Source source = new StreamSource(reader2);
	JAXBElement<?> o = um.unmarshal(source ,CeaHttpApplicationDefinition.class);
        org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition app =  (CeaHttpApplicationDefinition) o.getValue();


        //Construct a document combining the Tool and WebHttpApplication
        // documents
        Writer pretransWriter = new PrintWriter(new FileOutputStream("pretrans.xml"));
        new PrintWriter(new FileOutputStream("posttranstrans.xml"));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document callingDocument = builder.getDOMImplementation().createDocument(null, "http-app", null);


        callingDocument.getDocumentElement();

     //   Attr namespace = callingDocument.createAttribute("xmlns:agpd");
     //   namespace.setNodeValue("http://www.astrogrid.org/schema/AGParameterDefinition/v1");
     //   root.setAttributeNode(namespace);


    //    callingDocument.insertBefore(namespace, root);



        Marshaller marshaller = jc.createMarshaller();
	    marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
		    	new NamespacePrefixMapperImpl());
        marshaller.marshal(app, callingDocument);
        marshaller.marshal(tool,callingDocument);

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