/*
 * Created on Aug 9, 2004
 *
 * @TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.applications.http;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.MarshalException;
import javax.xml.bind.ValidationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.http.test.TestHttpApplicationLibrary;

/**
 * JUnit Tests
 *
 * @author jdt
 */
public class HttpApplicationDescriptionTest extends TestCase {
    private static final String AUTHORITY = "ivo://org.astrogrid.test"; //this is the real name that the test environment has
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(HttpApplicationDescriptionTest.class);

    private HttpApplicationDescription adderApplicationDescription;

    private CeaHttpApplicationDefinition theOneThatIWant;

    /**
     * Check that an application's metadata is correct
     * @throws ApplicationDescriptionNotFoundException
     */
    public final void testCreateMetadata() throws ApplicationDescriptionNotFoundException {
        String name = adderApplicationDescription.getId();
        assertEquals(AUTHORITY+"/Adder",name);

        ApplicationInterface[] interfaces = adderApplicationDescription.getInterfaces();
        assertEquals("Should be two interfaces", 2, interfaces.length);

        ApplicationInterface if1 = interfaces[0];
        assertEquals("TheRightInterface",if1.getId());
        assertSame(if1.getApplicationDescription(), adderApplicationDescription);
        String[] inputNames = if1.getArrayofInputs();
        assertEquals(2,inputNames.length);

        Set setOfInputs = new HashSet();
        setOfInputs.add(inputNames[0]);
        setOfInputs.add(inputNames[1]);

        assertTrue(setOfInputs.contains("x"));
        assertTrue(setOfInputs.contains("y"));
        String[] outputNames = if1.getArrayofOutputs();
        assertEquals(outputNames.length,1);
        assertEquals("sum",outputNames[0]);

        ApplicationInterface if2 = interfaces[1];
        assertEquals("TheWrongInterface",if2.getId());
        assertSame(if2.getApplicationDescription(), adderApplicationDescription);
        String[] inputNames2 = if2.getArrayofInputs();
        assertEquals(3,inputNames2.length);
        setOfInputs.clear();
        setOfInputs.add(inputNames2[0]);
        setOfInputs.add(inputNames2[1]);
        setOfInputs.add(inputNames2[2]);
        assertTrue(setOfInputs.contains("x"));
        assertTrue(setOfInputs.contains("y"));
        assertTrue(setOfInputs.contains("z"));

        String[] outputNames2 = if2.getArrayofOutputs();
        assertEquals(outputNames2.length,1);
        assertEquals("sum",outputNames2[0]);


        ParameterDescription[] ParameterDefinitions = adderApplicationDescription.getParameterDescriptions();
        assertEquals(4, ParameterDefinitions.length);

 //test that the ordering is as expected
        ParameterDescription pd1 = ParameterDefinitions[0];

        assertEquals("x",pd1.getId());
        assertEquals(ParameterTypes.TEXT, pd1.getType());


    }


    public final void testGetUrl() throws IOException {
        final String url = adderApplicationDescription.getUrl();
        assertEquals("Incorrect URL", "http://127.0.0.1:8078/add", url);
    }

    /**
     * Standard JUnit setup method
     *
     * @throws MarshalException oh dear
     * @throws ValidationException oh dear
     * @throws IOException oh dear
     * @throws ValidationException
     * @throws MarshalException
     * @throws MetadataException
     * @throws FactoryConfigurationError
     * @throws XMLStreamException
     */
    @Override
    public void setUp() {
        try {    
        TestHttpApplicationLibrary querier = new TestHttpApplicationLibrary(new MockNonSpringConfiguredConfig());
	        //note that all the
	    
		    adderApplicationDescription = (HttpApplicationDescription) querier.getDescription("ivo://org.astrogrid.test/Adder");
		} catch (ApplicationDescriptionNotFoundException e) {
		    fail(e.getMessage());
		}


	        assertNotNull("Didn't find the testapp I was looking for", adderApplicationDescription);

      }


}