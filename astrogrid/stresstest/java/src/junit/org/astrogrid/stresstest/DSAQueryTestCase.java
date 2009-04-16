/*
 * hacked from:
 * <meta:header>
 *	<meta:licence>
 *	  Copyright (C) AstroGrid. All rights reserved.
 *	  This software is published under the terms of the AstroGrid Software License version 1.2.
 *	  See [http://software.astrogrid.org/license.html] for details. 
 *	</meta:licence>
 *	<svn:header>
 *	  $LastChangedRevision: 1099 $
 *	  $LastChangedDate: 2008-07-29 18:56:24 +0100 (Tue, 29 Jul 2008) $
 *	  $LastChangedBy: dmorris $
 *	</svn:header>
 * </meta:header>
 *
 * THIS IS A HORRIBLE QUICK HACK FROM SOME OF DAVE'S VOSPACE TESTS
 * TO PUT TOGETHER SOME STRESSTEST RESULTS FOR KEITH.
 */
package org.astrogrid.stresstest;

import java.net.URL;
import java.net.URI;
import java.util.Vector;

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

/*
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.delegation.DelegationClient;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
*/

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
	public static final String USER_MYSPACE_LOCATION = "ivo://wfau.roe.ac.uk/KonaAndrews#/Results/textXXX_";

	/**
	* The direct MySpace location to write to.
	* This is the absolute MySpace (FileManager) location. 
	*
	*/
	public static final String DIRECT_MYSPACE_LOCATION = "ivo://wfau.roe.ac.uk/vospace#40ecf8521f28b70e011f6a451a870009/test_";

	public static final String DIRECT_VOSPACE_LOCATION = "vos://wfau.roe.ac.uk!vospace/.auto" ;

	public static final String DSA_CEA_APP = 
		 "wfau.roe.ac.uk/mysql-first-TEST/CatName_first/ceaApplication";

	public static final String DSA_TABLE = "TabNameFirst_catalogue"; 
	public static final String DSA_CATALOG = "CatName_first";

	public static final int DSA_SIM_QUERIES = 10;
	/* 10, 50, 100, 200 */

	/*
	protected Vector jobs = null;
	protected Vector startTimes = null;
	protected Vector endTimes = null;
	*/
	/**
	* Setup the test.
	*
	*/
	public void setUp() {
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
	public void testMySpaceDirectLocation()
	throws Exception 
	{
		int numQueries = 25;

		execute(USER_MYSPACE_LOCATION, 10, 1000);
		execute(USER_MYSPACE_LOCATION, 25, 1000);
		execute(USER_MYSPACE_LOCATION, 50, 1000);
		execute(USER_MYSPACE_LOCATION, 100, 1000);
		execute(USER_MYSPACE_LOCATION, 250, 1000);
		execute(USER_MYSPACE_LOCATION, 500, 1000);
		/*
		execute(USER_MYSPACE_LOCATION, 10, 1000);
		execute(USER_MYSPACE_LOCATION, 20, 1000);
		execute(USER_MYSPACE_LOCATION, 30, 1000);
		execute(USER_MYSPACE_LOCATION, 40, 1000);
		execute(USER_MYSPACE_LOCATION, 50, 1000);

		execute(USER_MYSPACE_LOCATION, 10, 10000);
		execute(USER_MYSPACE_LOCATION, 20, 10000);
		execute(USER_MYSPACE_LOCATION, 30, 10000);
		execute(USER_MYSPACE_LOCATION, 40, 10000);
		execute(USER_MYSPACE_LOCATION, 50, 10000);

		execute(USER_MYSPACE_LOCATION, 10, 100000);
		execute(USER_MYSPACE_LOCATION, 20, 100000);
		execute(USER_MYSPACE_LOCATION, 30, 100000);
		execute(USER_MYSPACE_LOCATION, 40, 100000);
		execute(USER_MYSPACE_LOCATION, 50, 100000);
		*/


		//execute(USER_MYSPACE_LOCATION, numQueries, 1000);
		/*
		jobs = new Vector(numQueries);
		startTimes = new Vector(numQueries);
		endTimes = new Vector(numQueries);

		//
		// Create our CEC delegate to the DSA.
		CommonExecutionConnectorClient delegate =
			DelegateFactory.createDelegate(
				getTestURL("dsa.adql.endpoint").toString());

		RunnerThread runnerThread= new RunnerThread(
			//execute(
				USER_MYSPACE_LOCATION,
				//DIRECT_VOSPACE_LOCATION,
				numQueries,
				1000,
				delegate
			);
		MonitorThread monitorThread= new MonitorThread(numQueries, delegate);

		runnerThread.start();
		//monitorThread.start();
		*/
	}

	public void execute(String baseLocation, int numQueries,
		 int numRows) throws Exception
	{
		int i;
		//
		// Create our CEC delegate to the DSA.
		CommonExecutionConnectorClient delegate =
		DelegateFactory.createDelegate(
			getTestURL("dsa.adql.endpoint").toString());

		Vector jobs = new Vector(numQueries);
		Vector tools = new Vector(numQueries);
		Vector startTimes = new Vector(numQueries);
		Vector endTimes = new Vector(numQueries);

		/*
		for (i = 0; i < numQueries; i++) {
			//
			// Create our tool document.	
			//tools.add(i, this.query(baseLocation, numRows));
			tools.add(i, this.makeQueryTool(baseLocation+Integer.toString(i), numRows));
			//
			// Set the job identifier.
			JobIdentifierType ident = new JobIdentifierType("foo");
			//
			// Initialize the job on the service.
			jobs.add(i,delegate.init((Tool)tools.elementAt(i), ident));
			assertNotNull("Execution ID is not null", (String)jobs.elementAt(i)); // TOFIX

			// Take a start timestamp
			startTimes.add(i,new Long(System.currentTimeMillis()));
			// Start the job running.
			delegate.execute((String)jobs.elementAt(i));
		}
		*/
		boolean done = true;
		for (i = 0; i < numQueries; i++) {
			startTimes.add(i,new Long(0));
			endTimes.add(i,new Long(0));
		}
		do {
			done = true;
			for (i = 0; i < numQueries; i++) {
				if (((Long)startTimes.elementAt(i)).longValue() == 0) {
					//
					// Create our tool document.	
					//tools.add(i, this.query(baseLocation, numRows));
					tools.add(i, this.makeQueryTool(baseLocation+Integer.toString(i), numRows));
					//
					// Set the job identifier.
					JobIdentifierType ident = new JobIdentifierType("foo");
					//
					// Initialize the job on the service.
					jobs.add(i,delegate.init((Tool)tools.elementAt(i), ident));
					assertNotNull("Execution ID is not null", (String)jobs.elementAt(i)); // TOFIX

					// Take a start timestamp
					startTimes.add(i,new Long(System.currentTimeMillis()));
					// Start the job running.
					delegate.execute((String)jobs.elementAt(i));
				}
				if (((Long)endTimes.elementAt(i)).longValue() == 0) {
					 //	
					 // Poll for completion.
					 MessageType status = null ;
					 try {
					 	status = delegate.queryExecutionStatus((String)jobs.elementAt(i));
						 log.debug("	Phase [" + status.getPhase() + "]");
						 if (
							status.getPhase() == ExecutionPhase.PENDING || 
							status.getPhase() == ExecutionPhase.INITIALIZING || 
							status.getPhase() == ExecutionPhase.RUNNING){
								done = false;
						 }
						 else if (status.getPhase() != ExecutionPhase.ERROR){
							endTimes.insertElementAt(new Long(System.currentTimeMillis()),i);
						 }
						 else if (status.getPhase() == ExecutionPhase.ERROR)
						 {
							 log.error("GOT AN EROR!");
							 endTimes.insertElementAt(new Long(-1),i);
							 // WHAT TO DO?fail(status.getContent());
						 }
						/*
						else {
							String result = delegate.getResults(job).getResult(0).getValue();
							assertEquals(
								expected,
								location
								);
						}
						*/
					 }
					 //catch (java.net.SocketTimeoutException ste) {
					 catch (Exception ioe) {
							// Skip these
							log.warn("SKipping timeout exception");
					 }
				}
			}
			Thread.currentThread().sleep(1000);
		}
		while (done == false);
		double average = 0;
		int successes = 0;
		int failures = 0;
		for (i = 0; i < numQueries; i++) {
			long runTime = ((Long)endTimes.elementAt(i)).longValue() - ((Long)startTimes.elementAt(i)).longValue();
			System.out.println("Time was " + Long.toString(runTime));
			log.debug("Time was " + Long.toString(runTime));
			if (runTime < 0) {
				failures = failures + 1;
				// Had an error
			}
			else {
				average = average + runTime;
				successes = successes + 1;
			}
		}
		average = average / (double)successes;
		log.debug("=============================");
		log.debug("DSA SIM. QUERIES: " + Integer.toString(DSA_SIM_QUERIES));
		log.debug("NUMBER OF QUERIES: " + Integer.toString(numQueries));
		log.debug("NUMBER OF ROWS: " + Integer.toString(numRows));
		log.debug("SUCCEEDED QUERIES: " + Integer.toString(successes));
		log.debug("FAILED QUERIES: " + Integer.toString(failures));
		log.debug("AVERAGE TIME: " + Double.toString(average));
		log.debug("=============================");
	}


  
/**
	* Create a DSA request containing an ADQL query.
	*
	*/
	public Tool makeQueryTool(String location, int numRows)
	{
		//
		// Create the Tool document and set the application name and interface.
		Tool tool = new Tool();
		//tool.setName("org.astrogrid.test/kona_dsa/CatName_first/ceaApplication");
		tool.setName(DSA_CEA_APP);
		tool.setInterface("ADQL");

		//
		// Add the ADQL query param.    
		ParameterValue in1 = new ParameterValue();
		in1.setName("Query");
		in1.setValue(
		 "<Select xsi:type='selectType' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://www.ivoa.net/xml/ADQL/v1.0'>"
		+ "  <Restrict xsi:type='selectionLimitType' Top='" +
				Integer.toString(numRows) + "'/>"
		+ "  <SelectionList xsi:type='selectionListType'>"
		+ "    <Item xsi:type='allSelectionItemType'/>"
		+ "  </SelectionList>"
		+ "  <From>"
		+ "    <Table xsi:type='tableType' Alias='a' Name='" 
				+ DSA_TABLE + "' Archive='" + DSA_CATALOG + "'/>"
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

	/*
	private class RunnerThread extends Thread
	{
		protected String baseLocation;
		protected int numQueries;
		protected int numRows;
		protected CommonExecutionConnectorClient delegate;

		public RunnerThread(String baseLocation, int numQueries, int numRows,
				CommonExecutionConnectorClient delegate) 
		{
			log.warn("INITING RUNNER THREAD");
			this.baseLocation = baseLocation;
			this.numQueries = numQueries;
			this.numRows = numRows;
			//this.delegate = delegate;

			try {
			delegate =
			         DelegateFactory.createDelegate(
			            getTestURL("dsa.adql.endpoint").toString());
			}
			catch (Exception e) {
				log.error("COULDN'T CREATE DELEGATE!!");
			}
			if (delegate == null) {
				log.error("DELEGATE IS NULL!!");
			}

		}

		public void run() 
		{
			log.warn("RUNNING RUNNER THREAD");
			try {
				int i;

				Vector tools = new Vector(numQueries);

				for (i = 0; i < numQueries; i++) {
					log.warn("ADDING QUEERY " +Integer.toString(i));

					//
					// Create our tool document.	
					//tools.add(i, this.query(baseLocation, numRows));
					tools.add(i, makeQueryTool(baseLocation+Integer.toString(i), numRows));
					log.warn("GOT HERE 1");
					//
					// Set the job identifier.
					JobIdentifierType ident = new JobIdentifierType("foo");
					log.warn("GOT HERE 2");
					//
					// Initialize the job on the service.
					//String jobString =delegate.init((Tool)tools.elementAt(i), ident); 
					String jobString = delegate.init(

							makeQueryTool(baseLocation+Integer.toString(i), numRows), ident);
					log.warn("GOT HERE jobsgting is " + jobString);
					//jobs.add(i,delegate.init((Tool)tools.elementAt(i), ident));
					jobs.add(i,jobString);
					log.warn("GOT HERE 3");
					//assertNotNull("Execution ID is not null", (String)jobs.elementAt(i)); // TOFIX

					// Take a start timestamp
					startTimes.add(i,new Long(System.currentTimeMillis()));
					log.warn("GOT HERE 4");
					// Start the job running.
					delegate.execute((String)jobs.elementAt(i));
					log.warn("ADDED QUEERY ONE OF " +Integer.toString(numQueries));

				}
			}
			catch (Throwable th) {
				// DO something
				log.error("Got exception : " + th.toString());
				System.out.println("GOT EXCEPTION " + th.toString());
			}
		}
	}

	private class MonitorThread extends Thread
	{
		protected int numQueries;
		protected CommonExecutionConnectorClient delegate;

		public MonitorThread(int numQueries, 
				CommonExecutionConnectorClient delegate) 
		{
			this.numQueries = numQueries;
			this.delegate = delegate;
		}

		public void run() 
		{
			try {
				boolean done = true;
				int i;
				for (i = 0; i < numQueries; i++) {
					endTimes.add(i,new Long(0));
				}
				do {
					System.out.println("LOOPING AGAIN!");
					done = true;
					for (i = 0; i < numQueries; i++) {
						if (((Long)endTimes.elementAt(i)).longValue() == 0) {
							 //	
							 // Poll for completion.
							 MessageType status = null ;
							 try {
								status = delegate.queryExecutionStatus((String)jobs.elementAt(i));
								 log.debug("	Phase [" + status.getPhase() + "]");
								 if (
									status.getPhase() == ExecutionPhase.PENDING || 
									status.getPhase() == ExecutionPhase.INITIALIZING || 
									status.getPhase() == ExecutionPhase.RUNNING){
										done = false;
								 }
								 else if (status.getPhase() != ExecutionPhase.ERROR){
									endTimes.insertElementAt(new Long(System.currentTimeMillis()),i);
								 }
								 else if (status.getPhase() == ExecutionPhase.ERROR)
								 {
									 log.error("GOT AN EROR!");
									 endTimes.insertElementAt(new Long(-1),i);
									 // WHAT TO DO?fail(status.getContent());
								 }
								 else {
										System.out.println("GOT HERE!");
								 }
								/
								else {
									String result = delegate.getResults(job).getResult(0).getValue();
									assertEquals(
										expected,
										location
										);
								}
								/
							 }
							 //catch (java.net.SocketTimeoutException ste) {
							 catch (Exception ioe) {
									// Skip these
									log.warn("SKipping timeout exception");
									done = false;
							 }
						}
					}
					Thread.currentThread().sleep(1000);
				}
				while (done == false);
				for (i = 0; i < numQueries; i++) {
					long runTime = ((Long)endTimes.elementAt(i)).longValue() - ((Long)startTimes.elementAt(i)).longValue();
					System.out.println("Time was " + Long.toString(runTime));
					log.debug("Time was " + Long.toString(runTime));
				}
			}
			catch (Exception e) {
				// Do something
			}
		}
	}
	*/
}
