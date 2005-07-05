/*
 * Created on Aug 9, 2004
 *
 * @TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.astrogrid.applications.http;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.http.test.TestRegistryQuerier;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/**
 * JUnit Tests
 * 
 * @author jdt
 */
public class HttpApplicationDescriptionTest extends TestCase {
    private static final String COMMUNITY_NAME = "org.astrogrid.test"; //this is the real name that the test environment has
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(HttpApplicationDescriptionTest.class);

    private HttpApplicationDescription adderApplicationDescription;

    private CeaHttpApplicationType theOneThatIWant;

    /**
     * Check that an application's metadata is correct
     */
    public final void testCreateMetadata() {
        String name = adderApplicationDescription.getName();
        assertEquals(COMMUNITY_NAME+"/Adder",name);
        
        ApplicationInterface[] interfaces = adderApplicationDescription.getInterfaces();
        assertEquals("Should be two interfaces", 2, interfaces.length);
        
        ApplicationInterface if1 = interfaces[0];
        assertEquals("TheRightInterface",if1.getName());
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
        assertEquals("TheWrongInterface",if2.getName());
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
        
        
        ParameterDescription[] parameterDescriptions = adderApplicationDescription.getParameterDescriptions();
        assertEquals(4, parameterDescriptions.length);
        
        ParameterDescription pd1 = parameterDescriptions[3];

        assertEquals("x",pd1.getName());
        assertEquals(ParameterTypes.TEXT, pd1.getType());

        
    }


    public final void testGetUrl() throws MarshalException, ValidationException, IOException {
        final String url = adderApplicationDescription.getUrl();
        assertEquals("Incorrect URL", "http://127.0.0.1:8078/add", url);
    }

    /**
     * Standard JUnit setup method
     * 
     * @throws MarshalException oh dear
     * @throws ValidationException oh dear
     * @throws IOException oh dear
     */
    public void setUp() throws MarshalException, ValidationException, IOException {
        TestRegistryQuerier querier = new TestRegistryQuerier(null);
        Collection allApps = querier.getHttpApplications();
        Iterator it = allApps.iterator();
        //note that all the 
        theOneThatIWant = querier.getHttpApplication("org.astrogrid.test/Adder");

        
        assertNotNull("Didn't find the testapp I was looking for", theOneThatIWant);
        IdGen id = new InMemoryIdGen();
        DefaultProtocolLibrary lib = new DefaultProtocolLibrary();
        TestAuthority resol = new TestAuthority();
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(id, lib, resol);
        adderApplicationDescription = new HttpApplicationDescription(theOneThatIWant,
                env);
    }

    public final void testGetApplication() {
        assertSame(theOneThatIWant, adderApplicationDescription.getApplication());
    }

}