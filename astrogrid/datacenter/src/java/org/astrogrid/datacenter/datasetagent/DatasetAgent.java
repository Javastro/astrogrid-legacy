/*
 * $Id: DatasetAgent.java,v 1.18 2003/08/26 16:50:49 mch Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.datasetagent;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.astrogrid.AstroGridException;
import org.astrogrid.datacenter.config.ConfigurationDefaultImpl;
import org.astrogrid.datacenter.config.ConfigurationKeys;
import org.astrogrid.datacenter.impl.QueryFactoryImpl;
import org.astrogrid.datacenter.job.Job;
import org.astrogrid.datacenter.query.QueryFactory;
import org.astrogrid.datacenter.job.RunJobRequestDD;
import org.astrogrid.i18n.AstroGridMessage;
import org.objectwiz.votable.ResultSetConverter;
import org.objectwiz.votable.ResultSetToSimpleVOTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * The <code>DatasetAgent</code> class is the top level AstroGrid
 * component in a data center.It contains the workflow for executing a job,
 * usually a query against an astronomical catalog. The catalog is likely held
 * within an SQL/RDBMS style database that is JDBC compliant. However, this is
 * not an absolute restriction, and other sources can be utilized at the cost
 * of the datacenter writing an implementation factory to support their own
 * special query processing.
 * <p>
 * The mainline argument (the workflow) is held within the method runQuery(),
 * which should be referred to for further detail. The basic workflow is:
 *      1. Load the datacenter properties (if not already loaded).
 *      2. Analyse the query and create appropriate structures.
 *      3  Execute the query.
 *      4. Allocate temporary file space within the local file system.
 *      5. Convert the query into VOTable format and stream it to
 *         the allocated file.
 *      6. Inform the MySpace facility that there is a file to
 *         pick up, and give it the file location.
 *      7. Inform the JobMonitor of successful completion.
 * <p>
 * The above does not cover use cases where errors occur.
 * <p>
 * An instance of a DatasetAgent is stateless, with some provisos:
 * 1. The datacenter is driven by a properties file, held at class level.
 * 2. AstroGrid messages are held in a manner amenable to internationalization.
 * These are also loaded from a properties file, held at class level.
 * 3. Finally, and importantly, the DatasetAgent utilizes (but does not hold it
 * as an instance variable) an entity which does contain state - the Job entity,
 * which currently represents one table held in any suitable JDBC compliant database.
 * However, and again, this is not an absolute restriction.
 *
 * @author  Jeff Lusted
 * @author  Phil Nicolson
 * @version 1.0 28-May-2003
 * @since   AstroGrid 1.2
 */
public class DatasetAgent {

   /** Compile-time switch used to turn tracing on/off.
     * Set this to false to eliminate all trace statements within the byte code.*/
   private static final boolean
      TRACE_ENABLED = true ;

   private static final String
       ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGDTCE00030",
       ASTROGRIDERROR_ULTIMATE_QUERYFAILURE        = "AGDTCE00040";

   /** Log4J logger for this class. */
   private static Logger
      logger = Logger.getLogger( DatasetAgent.class ) ;

   protected final ConfigurationDefaultImpl config;
   protected final DynamicFactoryManager fac;

   /**
     *
     * Default constructor.
     *
     **/
   public DatasetAgent()
   {
      if( TRACE_ENABLED ) logger.debug( "DatasetAgent(): entry") ;
      config = new ConfigurationDefaultImpl();
      fac = new DynamicFactoryManager(config);
   }

   /** verify that all configuration files are present,
    * load factories from config file if not already loaded.
    * @throws AstroGridException
    */
   protected void checkResources() throws Exception
   {
      // If properties file is not loaded, we bail out...
      // Each DatasetAgent MUST be properly initialized!
      config.checkPropertiesLoaded() ;
      // now each of the factories..
      fac.verify();
   }


   /**
     * Represents the mainline workflow argument for the DatasetAgent.
     * <p>
     * Shows the DatasetAgent to be a stateless component.
     * It neither uses nor creates instance variables. In the EJB model
     * it would be considered a stateless session bean.
     *
     * The workflow:
      *
      * 1. Load the datacenter properties (if not already loaded).
      *    This includes the message resource bundle, which determines
      *    the default language for a datacenter. This step is
      *    a precondition of correct working.
      *
      * 2. Parse the request XML into a document.
      *
      * 3  Acquire a factory for the Job data structures and use it
      *    to fill out all the appropriate job objects. This is a
      *    major structural step and is partly parameterized by the
      *    fact that the factory is dynamically loaded, something
      *    controlled by the DatasetAgent's properties' file. The
      *    factory takes care of all persistence as related to Job.
      *    It is conceivable that a data center might write an
      *    alternative implementation if it were desired to hold
      *    job information in a form other than a JDBC compliant
      *    database.
      *
      * 4  Execute the query. The implementation of this aspect
      *    is held in a dynamically loaded class controlled by
      *    the DatasetAgent's properties' file. It is conceivable
      *    that a data center might write an alternative implementation
      * .
      * 5  Allocate temporary file space within the local file system.
      *
      * 6. Convert the query into VOTable format and stream it to
      *    the allocated file.
      *
      * 7. Inform the MySpace facility that there is a file to
      *    pick up, and give it the file location.
      *
      * 8. Inform the JobMonitor of completion, successful or
      *    otherwise.
      *
      * Throughout the above process, the DatasetAgent attempts to
      * update the job status to appropriate values and persist this
      * information to the Job database.
     *
     * @param jobXML - The service request XML received as a String.
     * @return A String used for testing purposes only. This service
     * is presented as a one-way call..
     *
     * @see RunJobRequest.xsd in CVS
     **/
   public String runQuery( String jobXML )
   {
      if( TRACE_ENABLED ) logger.debug( "runQuery() entry") ;

      String response = null ;
      Job job = null ;
      QueryFactory queryFactory = null;

      try
      {
         checkResources();

         // Parse the request and create the necessary Job structures, including Query...
         Document queryDoc = parseXML( new StringReader(jobXML )) ;

         job = fac.getJobFactory().create( queryDoc,fac ) ;

         // Execute the Query...
         job.setStatus( Job.STATUS_RUNNING ) ;
         fac.getJobFactory().update( job );
         queryFactory = fac.getQueryFactory(getCatalogueName(  queryDoc.getDocumentElement()));
         queryFactory.execute(job.getJobStep().getQuery());

         // convert results to VOTable
         convertResultsToVotable(queryFactory, "tempResults.vot");
         Document voResults = parseXML( new FileReader("tempResults.vot"));

         job.setStatus( Job.STATUS_COMPLETED ) ;
         fac.getJobFactory().update( job );

         return response ;
      }
      catch( Exception dex )
      {
         if (dex instanceof AstroGridMessage)
         {
            logger.error( ((AstroGridMessage) dex).toString(), dex ) ;
         }

         AstroGridMessage generalMessage = new AstroGridMessage( ASTROGRIDERROR_ULTIMATE_QUERYFAILURE
                                                     , this.getClass().getName()) ;
         logger.error( generalMessage.toString() ) ;

         job.setStatus( Job.STATUS_IN_ERROR ) ;
         try{ fac.getJobFactory().update( job ); } catch( Exception ex ) {;}

         dex.printStackTrace();
         return dex.toString();
      }
      finally {
         // Inform JobMonitor within JES of jobstep completion...
         job.informJobMonitor() ;
         if (queryFactory != null) queryFactory.end();
         if( TRACE_ENABLED ) logger.debug( "runQuery() exit") ;
      }

   } // end runQuery()

   /**
    * Temporarily here until we sort out what to do with plugging in database accessors
    */
   public String getCatalogueName( Element adql)
   {
           NodeList  nodeList = adql.getChildNodes() ;
           Element   queryChild = null;
           Element catalogChild = null ;

           for( int i=0 ; i < nodeList.getLength() ; i++ ) {
               if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
                   continue ;
               queryChild = (Element) nodeList.item(i) ;
               if( queryChild.getTagName().equals( RunJobRequestDD.QUERY_ELEMENT ) )
                   break ;
           }

           nodeList = queryChild.getElementsByTagName( RunJobRequestDD.CATALOG_ELEMENT ) ;

           for( int i=0 ; i < nodeList.getLength() ; i++ ) {
               if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
                   continue ;
               catalogChild = (Element) nodeList.item(i) ;
               if( catalogChild.getTagName().equals( RunJobRequestDD.CATALOG_ELEMENT ) )
                   break ;
           }

           String  keyToFactory = catalogChild.getAttribute( RunJobRequestDD.CATALOG_NAME_ATTR );

           return keyToFactory;

   }


   /**
    * Temporarily here until we sort out what to do with results conversions etc
    */
   public void convertResultsToVotable( QueryFactory queryFactory, String filePath) throws AstroGridException
   {
    //
    // JBL Note: For the moment do not know where these settings should be coming from...
    // (However, these may produce a suitable default pattern for a locale)
    //
      String
         doubleOutputPattern = "",
         floatOutputPattern = "",
         dateOutputPattern = "",
         timeOutputPattern = "",
         timestampOutputPattern = "" ;

      String
      ASTROGRIDERROR_VOTABLE_CONVERSION_EXCEPTION = "AGDTCE00130" ;

      ResultSetConverter
          converter = new ResultSetToSimpleVOTable( doubleOutputPattern
                                                  , floatOutputPattern
                                                  , dateOutputPattern
                                                  , timeOutputPattern
                                                  , timestampOutputPattern ) ;
      PrintStream
         out = null ;

      try{
         out = new PrintStream( new FileOutputStream(filePath) );
         converter.serialize( ((QueryFactoryImpl) queryFactory.getImplementation()).getResultSet(), out ) ;
      }
      catch( Exception ex ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_VOTABLE_CONVERSION_EXCEPTION, this.getClass().getName());
         logger.error( message.toString(), ex ) ;
         throw new AstroGridException( message, ex );
      }
      finally {
         if( out != null ) {
             try {
               out.flush();
               out.close();
             }
             catch( Exception ex ) {
                // JBL Note: a log entry might be appropriate here...
               ;
             }
         }
         out = null ;
         if( TRACE_ENABLED ) logger.debug( "VOTableFactoryImpl.stream(): exit") ;
      }

    } // end of stream()


   /** test method - check everything gets initialized correctly */
   public String selfTest() {
      java.io.StringWriter sout = null;
      java.io.PrintWriter wout = null;
      try {
         // If properties file is not loaded, we bail out...
         // Each DatasetAgent MUST be properly initialized!
         sout = new java.io.StringWriter();
         wout = new java.io.PrintWriter(sout);

         config.checkPropertiesLoaded() ;
         fac.verify();
         wout.println(config); // hopefully this does something nice.
         wout.println(fac);
         return sout.toString();
      } catch (Throwable t) {
         t.printStackTrace(wout);
         return "Failed " + t.getMessage() + sout.toString();
      }
   }


   /**
    * takes the ADQL string and parses it into a DOM.
    */
   private Document parseXML( Reader xmlSource ) throws IOException, SAXException, ParserConfigurationException
   {
      DocumentBuilderFactory  factory = DocumentBuilderFactory.newInstance();
      factory.setValidating( Boolean.getBoolean( config.getProperty( ConfigurationKeys.DATASETAGENT_PARSER_VALIDATION
                                                                       , ConfigurationKeys.DATASETAGENT_CATEGORY )  )  ) ;

      DocumentBuilder builder = factory.newDocumentBuilder();
      logger.debug( xmlSource ) ;
      InputSource jobSource = new InputSource( xmlSource );

      return builder.parse( jobSource );
   }

} // end of class DatasetAgent
