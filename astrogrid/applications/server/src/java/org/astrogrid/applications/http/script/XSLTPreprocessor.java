/* $Id: XSLTPreprocessor.java,v 1.2 2008/09/13 09:51:04 pah Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 * Created on Aug 10, 2004
 */
///CLOVER:OFF 
//@TODO - remove CLOVER:OFF once this class is complete
package org.astrogrid.applications.http.script;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.WebHttpApplicationSetup;
import org.astrogrid.applications.description.impl.WebHttpCall;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.http.exceptions.HttpParameterProcessingException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * A Preprocessor that processes using xslt. If no xslt code is supplied, then a
 * default xslt script is used.
 * @TODO this needs refactoring so that pre and post-processing can be handled by
 * the same classes
 * 
 * @author jdt
 */
public final class XSLTPreprocessor implements Preprocessor {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(XSLTPreprocessor.class);

    public static final String xmlName = "xslt";

    private Source xslSource;

    private Transformer stylesheet;

    /**
     * @param code
     */
    public XSLTPreprocessor(String code) {
        if (log.isTraceEnabled()) {
            log.trace("XSLTPreprocessor(String code = " + code + ") - start");
        }

        // @TODO Auto-generated constructor stub

        if (log.isTraceEnabled()) {
            log.trace("XSLTPreprocessor(String) - end");
        }
    }

    /**
     * Ctor based on the default xslt in the DefaultInputParameters.xsl file
     */
    public XSLTPreprocessor() {
        if (log.isTraceEnabled()) {
            log.trace("XSLTPreprocessor() - start");
        }

        try {
            final InputStream xslFileStream = this.getClass().getResourceAsStream("DefaultInputParameters.xsl");
            final Source xslSource = new StreamSource(new BufferedReader(new InputStreamReader(xslFileStream)));
            final TransformerFactory xformFactory = TransformerFactory.newInstance();
            stylesheet = xformFactory.newTransformer(xslSource);

            xslFileStream.close();

        } catch (TransformerConfigurationException e) {
            // Just log it for now - we'll squeak later when process gets called
            log.error("XSLTPreprocessor(): failed to set up Transformer", e);
        } catch (TransformerFactoryConfigurationError e) {
            // Just log it for now - we'll squeak later when process gets called
            log.error("XSLTPreprocessor(): failed to set up Transformer", e);
        } catch (IOException e) {
            //unlikely
            log.info("XSLTPreprocessor(): failed to close xslt input stream - it probably doesn't matter", e);
        }

        if (log.isTraceEnabled()) {
            log.trace("XSLTPreprocessor() - end");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.applications.http.script.Preprocessor#process(org.astrogrid.workflow.beans.v1.Tool,
     *      org.astrogrid.registry.beans.cea.CeaHttpApplicationDefinition)
     */
    public WebHttpCall process(Tool tool, WebHttpApplicationSetup appSetup) throws HttpParameterProcessingException {
        if (log.isTraceEnabled()) {
            log.trace("process(Tool tool = " + tool + ", CeaHttpApplicationDefinition app = " + appSetup + ") - start");
        }

       try {
         //FIXME must be able to do xsl transformation in a more flexible way.
            Writer writer3 = new PrintWriter(new FileOutputStream("tool-eg.xml"));
            Writer writer4 = new PrintWriter(new FileOutputStream("webapp-eg.xml"));
            JAXBContext confac = CEAJAXBContextFactory.newInstance();
	    javax.xml.bind.Marshaller jbmar = confac.createMarshaller();
            jbmar.marshal(tool, writer3);
            jbmar.marshal(appSetup, writer4);

            //Construct a document combining the Tool and WebHttpApplication
            // documents
            Writer writer = new PrintWriter(new FileOutputStream("jdt-tool.xml"));
            Writer writer2 = new PrintWriter(new FileOutputStream("jdt-tool-trans.xml"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document callingDocument = builder.getDOMImplementation().createDocument(null, "http-app", null);
            Node root = callingDocument.getFirstChild();
            Marshaller marshaller = confac.createMarshaller(); // FIXME need to marshall properly??
            marshaller.marshal(appSetup, root);
            root=callingDocument.getLastChild();
            marshaller.marshal(tool, root);

            OutputFormat format = new OutputFormat(callingDocument);
            XMLSerializer output = new XMLSerializer(writer, format);
            output.serialize(callingDocument);

            TransformerFactory xformFactory = TransformerFactory.newInstance();
            InputStream xslFileStream = this.getClass().getResourceAsStream("DefaultInputParameters.xsl");
            Source xsl = new StreamSource(new BufferedReader(new InputStreamReader(xslFileStream)));
            Transformer stylesheet = xformFactory.newTransformer(xsl);

            // Document callingDocument2 =
            // builder.getDOMImplementation().createDocument(null, "jdt-app",
            // null);

            Source request = new DOMSource(callingDocument);
            DOMResult response = new DOMResult();
            stylesheet.transform(request, response);

            Node node = response.getNode();
            Document callingDocument2 = (Document) node;
            //    callingDocument2.appendChild(node);
            OutputFormat format2 = new OutputFormat(callingDocument2);
            XMLSerializer output2 = new XMLSerializer(writer2, format2);
            output2.serialize(callingDocument2);

        } catch (Exception e) {
            log.error("process(Tool, CeaHttpApplicationDefinition)", e);

            throw new HttpParameterProcessingException("problem with xsl transform of parameters",e);
        } catch (FactoryConfigurationError e) {
            log.error("process(Tool, CeaHttpApplicationDefinition)", e);

            // TODO Auto-generated catch block
            e.printStackTrace();
         }

        if (log.isTraceEnabled()) {
            log.trace("process(Tool, CeaHttpApplicationDefinition) - end - return value = " + null);
        }
        return null;
    }

}