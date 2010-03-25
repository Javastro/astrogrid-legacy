package org.astrogrid.dataservice.service.vosi;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.mortbay.jetty.testing.HttpTester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * An {@code HttpTester} fixture that can supply the content of the HTTP
 * response as an {@code InputSource}.
 *
 * @author Guy Rixon
 */
public class XmlTester extends HttpTester {

  public InputSource getXmlContent() {
    return new InputSource(new StringReader(getContent()));
  }

  /**
   * Validates the response body against a given schema.
   *
   * @param schemaUri The location of the schema.
   * @throws SAXException If validation fails.
   * @throws SAXException If the response body is not well formed.
   * @throws IOException If the response body cannot be read (should never happen).
   */
  public void validateWithW3cSchema(URL schemaUri) throws SAXException, IOException {
    SchemaFactory factory =
        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = factory.newSchema(schemaUri);
    Validator validator = schema.newValidator();
    validator.validate(new SAXSource(getXmlContent()));
  }

  /**
   * Validates the response body against a given schema.
   *
   * @param schemaUri The location of the schema.
   * @throws SAXException If validation fails.
   * @throws SAXException If the response body is not well formed.
   * @throws IOException If the response body cannot be read (should never happen).
   */
  public void validateWithRelaxNg(URL schemaUri) throws SAXException, IOException {
    SchemaFactory factory =
        SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
    Schema schema = factory.newSchema(schemaUri);
    Validator validator = schema.newValidator();
    validator.validate(new SAXSource(getXmlContent()));
  }

}
