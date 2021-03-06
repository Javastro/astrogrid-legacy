/*
 * <meta:header>
 *     <meta:licence>
 *         Copyright (C) AstroGrid. All rights reserved.
 *         This software is published under the terms of the AstroGrid Software License version 1.2.
 *         See [http://software.astrogrid.org/license.html] for details. 
 *     </meta:licence>
 *     <svn:header>
 *         $LastChangedRevision: 1099 $
 *         $LastChangedDate: 2008-07-29 18:56:24 +0100 (Tue, 29 Jul 2008) $
 *         $LastChangedBy: dmorris $
 *     </svn:header>
 * </meta:header>
 *
 */
package org.astrogrid.vospace.client.delegate ;

import java.net.URL;
import java.net.URI;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Tool;

import org.astrogrid.config.SimpleConfig;

import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.delegation.DelegationClient;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.astrogrid.vospace.v02.junit.TestBase ;

/**
 * A test case which uses a CommonExecutionConnectorClient to send a query direct to the test DSA.
 * Based on CEA DefaultApplicationTest integration test.
 *
 */
public class DSAQueryTestCase
extends TestBase
    {
    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(DSAQueryTestCase.class);

    /**
     * The community based MySpace location to write to.
     * The DSA calls the users community service to resolve this relative to their home space location. 
     *
     */
    public static final String USER_MYSPACE_LOCATION = "ivo://uk.ac.le.star/DaveMorris#/20080902-171758" ;

    /**
     * The direct MySpace location to write to.
     * This is the absolute MySpace (FileManager) location. 
     *
     */
    public static final String DIRECT_MYSPACE_LOCATION = "ivo://uk.ac.le.star/vospace-service#000000001832417f0118559ee302714c/20080902-171758" ;

    /**
     * The direct VOSpace location to write to.
     *
     */
    public static final String DIRECT_VOSPACE_LOCATION = "vos://org.astrogrid.test!vospace-service-02/.auto" ;

    /**
     * Setup the test.
     *
     */
    public void setUp()
        {
        //
        // Setup the test registry URL.
        SimpleConfig.getSingleton().setProperty(
            "org.astrogrid.registry.query.endpoint",
             getTestProperty(
                "registry.query.endpoint"                
                )
            );
        }

    /**
     * Check we can send the results to MySpace.
     *
     */
    public void XtestMySpaceUserLocation()
    throws Exception
        {
        execute(
            USER_MYSPACE_LOCATION,
            USER_MYSPACE_LOCATION
            );
        }

    /**
     * Check we can send the results to MySpace.
     *
     */
    public void XtestMySpaceDirectLocation()
    throws Exception
        {
        execute(
            DIRECT_MYSPACE_LOCATION,
            DIRECT_MYSPACE_LOCATION
            );
        }

    /**
     * Check we can send the results to VOSpace.
     *
     */
    public void XtestVOSpaceDirectLocation()
    throws Exception
        {
        execute(
            DIRECT_VOSPACE_LOCATION,
            DIRECT_VOSPACE_LOCATION
            );
        }

    /**
     * Execute an ADQL query.
     *
     */
    public void execute(String location, String expected)
    throws Exception
        {
        execute(
            location,
            expected,
            null
            );
        }  

    /**
     * Execute an ADQL query.
     *
     */
    public void execute(String location, String expected, SecurityGuard guard)
    throws Exception
        {
        log.debug("Executing query");
        log.debug("  Principal [" + ((null != guard) ? guard.getX500Principal() : null) + "]");
        log.debug("  Location  [" + location + "]");

        //
        // Create our CEC delegate to the DSA.
        log.debug("Creating delegate");
        CommonExecutionConnectorClient delegate =
            DelegateFactory.createDelegate(
                getTestURL(
                    "dsa.adql.endpoint"
                    ).toString()
                );
        //
        // Add our SecurityGuard.
        if (null != guard)
            {
            log.debug("Adding credentials");
            delegate.setCredentials(
                guard
                );
            }
        //
        // Create our tool document.    
        log.debug("Creating tool document");
        Tool tool = this.query(
            location
            );
        //
        // Set the job identifier.
        JobIdentifierType ident = new JobIdentifierType("foo");
        //
        // Initialize the job on the service.
        log.debug("Sending tool document");
        String job = delegate.init(
            tool,
            ident
            );
        assertNotNull("Execution ID is not null", job);
        //
        // Start the job running.
        log.debug("Executing job");
        delegate.execute(job);
        //    
        // Poll for completion.
        MessageType status = null ;
        do {
            log.debug("Checking status");
            status = delegate.queryExecutionStatus(job);
            log.debug("    Phase [" + status.getPhase() + "]");
            }
        while (status.getPhase() == ExecutionPhase.PENDING || status.getPhase() == ExecutionPhase.INITIALIZING || status.getPhase() == ExecutionPhase.RUNNING);
        //
        // Check the results.
        if (status.getPhase() == ExecutionPhase.ERROR)
            {
            fail(status.getContent());
            }
        else {
            String result = delegate.getResults(job).getResult(0).getValue();
log.debug("Checking result location");
log.debug("  Result [" + result + "]");
            assertEquals(
                expected,
                location
                );
            }
        }
  
    /**
     * Create a DSA request containing an ADQL query.
     *
     */
    public Tool query(String location)
        {
        //
        // Create the Tool document and set the application name and interface.
        Tool tool = new Tool();
        tool.setName("org.astrogrid.test/kona_dsa/CatName_first/ceaApplication");
        tool.setInterface("ADQL");

        //
        // Add the ADQL query param.    
        ParameterValue in1 = new ParameterValue();
        in1.setName("Query");
        in1.setValue(
              "<Select xsi:type='selectType' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://www.ivoa.net/xml/ADQL/v1.0'>"
            + "  <Restrict xsi:type='selectionLimitType' Top='10000'/>"
            + "  <SelectionList xsi:type='selectionListType'>"
            + "    <Item xsi:type='allSelectionItemType'/>"
            + "  </SelectionList>"
            + "  <From>"
            + "    <Table xsi:type='tableType' Alias='a' Name='TabNameSecond_catalogue' Archive='CatName_SECOND'/>"
            + "  </From>"
            + "</Select>"
            );

        //
        // Set the format param.    
        ParameterValue in2 = new ParameterValue();
        in2.setName("Format");
        in2.setValue("VOTABLE");

        //
        // Set the result location.    
        ParameterValue out1 = new ParameterValue();
        out1.setName("Result");
        out1.setValue(location);
        out1.setIndirect(true);

        //
        // Wrap the inputs and add them to the tool document.
        Input in = new Input();
        in.addParameter(in1);
        in.addParameter(in2);
        tool.setInput(in);

        //
        // Wrap the outputs and add them to the tool document.
        Output out = new Output();
        out.addParameter(out1);
        tool.setOutput(out);

        return tool;
        }

    /**
     * Login to our community and get a certificate.
     * Looks for the account ivorn and password in the test properties.
     #
     # Community credentials.
     # These should be added to build.properties in your home directory.
     #org.astrogrid.junit.community.account=ivo://name@community
     #org.astrogrid.junit.community.password=########
     *
     */
    public SecurityGuard login()
    throws Exception
        {
        log.debug("Login to community");
        String account = getTestProperty(
            "community.account"                
            );
        assertNotNull(
            "Community account ivorn required",
            account
            );
        String password = getTestProperty(
            "community.password"
            );
        assertNotNull(
            "Community account password required",
            password
            );
        return login(
            account,
            password
            );
        }

    /**
     * Login to our community and get a certificate.
     *
     */
    public SecurityGuard login(String account, String password)
    throws Exception
        {
        log.debug("Login to community");
        log.debug("  Account [" + account + "]");
        //
        // Initialise the security provider.
        Security.addProvider(
            new BouncyCastleProvider()
            );
        //
        // Create our SecurityGuard.
        SecurityGuard guard = new SecurityGuard();
        //
        // Login to our community.
        guard.signOn(
            account,
            password,
            3600
            );

        log.debug("Logged in");
        log.debug("  Principal [" + guard.getX500Principal() + "]");

        //
        // Return the SecurityGuard with credentials.
        return guard ;
        }

    /**
     * Test the community login.
     *
     */
    public void XtestLogin()
    throws Exception
        {
        assertNotNull(
            this.login()
            );
        }

    /**
     * Delegate our credentials to the DSA service.
     *
     */
    public void delegation(SecurityGuard guard, URI endpoint)
    throws Exception
        {
        log.debug("Delegating credentials");
        log.debug("  Principal [" + guard.getX500Principal() + "]");
        log.debug("  Endpoint  [" + endpoint + "]");

        DelegationClient delegator = new DelegationClient(endpoint, guard);
        delegator.delegate();

        }

    /**
     * Test the DSA delegation.
     *
     */
    public void XtestDelegation()
    throws Exception
        {
        delegation(
            this.login(),
            getTestURI(
                "dsa.cert.endpoint"
                )
            );
        }

    /**
     * Check we can send the results to VOSpace using our credentials.
     *
     */
    public void testVOSpaceDelegated()
    throws Exception
        {
        //
        // Login and get our credentials.
        SecurityGuard guard = this.login();
        //
        // Setup our delegated credentials.
        this.delegation(
            guard,
            getTestURI(
                "dsa.cert.endpoint"
                )
            );
        //
        // Execute the query.
        this.execute(
            DIRECT_VOSPACE_LOCATION,
            DIRECT_VOSPACE_LOCATION,
            guard
            );
        }

    }
