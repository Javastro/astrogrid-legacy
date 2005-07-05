/* $Id: XSLTPreprocessor.java,v 1.3 2005/07/05 08:27:00 clq2 Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
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

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.astrogrid.applications.beans.v1.WebHttpCall;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.DOMException;
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
     *      org.astrogrid.registry.beans.cea.CeaHttpApplicationType)
     */
    public WebHttpCall process(Tool tool, CeaHttpApplicationType app) {
        if (log.isTraceEnabled()) {
            log.trace("process(Tool tool = " + tool + ", CeaHttpApplicationType app = " + app + ") - start");
        }

        // @TODO Auto-generated method stub
        try {

            Writer writer3 = new PrintWriter(new FileOutputStream("tool-eg.xml"));
            Writer writer4 = new PrintWriter(new FileOutputStream("webapp-eg.xml"));
            tool.marshal(writer3);
            app.marshal(writer4);

            //Construct a document combining the Tool and WebHttpApplication
            // documents
            Writer writer = new PrintWriter(new FileOutputStream("jdt-tool.xml"));
            Writer writer2 = new PrintWriter(new FileOutputStream("jdt-tool-trans.xml"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document callingDocument = builder.getDOMImplementation().createDocument(null, "http-app", null);
            Node root = callingDocument.getFirstChild();
            Marshaller marshaller = new Marshaller(root);
            marshaller.marshal(app);
            marshaller.marshal(tool);

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

        } catch (MarshalException e) {
            log.error("process(Tool, CeaHttpApplicationType)", e);

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            log.error("process(Tool, CeaHttpApplicationType)", e);

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DOMException e) {
            log.error("process(Tool, CeaHttpApplicationType)", e);

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ValidationException e) {
            log.error("process(Tool, CeaHttpApplicationType)", e);

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            log.error("process(Tool, CeaHttpApplicationType)", e);

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            log.error("process(Tool, CeaHttpApplicationType)", e);

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            log.error("process(Tool, CeaHttpApplicationType)", e);

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            log.error("process(Tool, CeaHttpApplicationType)", e);

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (log.isTraceEnabled()) {
            log.trace("process(Tool, CeaHttpApplicationType) - end - return value = " + null);
        }
        return null;
    }

}