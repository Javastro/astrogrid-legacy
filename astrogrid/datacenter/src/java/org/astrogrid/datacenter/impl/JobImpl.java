/*
 * @(#)JobImpl.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.impl;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;
import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.config.ConfigurationKeys;
import org.astrogrid.datacenter.impl.abstr.AbstractJob;
import org.astrogrid.datacenter.job.Job;
import org.astrogrid.datacenter.job.JobException;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.job.RunJobRequestDD;
import org.astrogrid.i18n.AstroGridMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/** implementation of Job
 * TODO - feel there's way too much going on in this class - way is i handline datasources and copnnections for instance.
 * suggests a separate JobStore class is needed for this.
 */

public class JobImpl extends AbstractJob implements Job {

   private static final boolean
      TRACE_ENABLED = true ;

   private static Logger
      logger = Logger.getLogger( JobImpl.class ) ;

    private final static String
        SUBCOMPONENT_NAME = Util.getComponentName( JobImpl.class ) ;

   private static final String
       ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION            = "AGDTCE00160" ,
       ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT = "AGDTCE00180" ,
        ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_JOBMONITOR        = "AGDTCE00185" ;

   private static JobFactoryImpl
      factoryImpl = null ;

    private final String
        subsystemAcronym ;

   private Connection
      connection = null ;
   private PreparedStatement
      preparedStatement = null ;
   private ResultSet
      resultSet = null ;

   public JobImpl() {
        subsystemAcronym = "DTC" ;
    }


   public JobImpl( Element jobElement ,FactoryProvider facMan) throws JobException {
      if( TRACE_ENABLED ) logger.debug( "JobImpl(): entry") ;

        subsystemAcronym = "DTC" ;

      try {

         date = new Date() ;
         name = jobElement.getAttribute( RunJobRequestDD.JOB_NAME_ATTR ).trim() ;
         jobURN = jobElement.getAttribute( RunJobRequestDD.JOB_URN_ATTR ).trim() ;
         jobMonitorURL = jobElement.getAttribute( RunJobRequestDD.JOB_MONITOR_URL_ATTR ).trim() ;

         NodeList  nodeList = jobElement.getChildNodes() ;

         for( int i=0 ; i < nodeList.getLength() ; i++ ) {

            if ( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    scanElement((Element) nodeList.item(i), facMan);
             } // end if

         } // end for

         this.setStatus( Job.STATUS_INITIALIZED ) ;

      }
      catch( Exception ex ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
                                              , SUBCOMPONENT_NAME ) ;
         this.setComment( message.toString() ) ;
         this.setStatus( Job.STATUS_IN_ERROR ) ;
         logger.error( message.toString(), ex ) ;
         // We must have at least the semblance of a primary key for the Job entity...
         if( (this.jobURN == null) || (this.jobURN.equals("")) ) {
            throw new JobException( message ) ;
         }
      }
      finally {
         if( TRACE_ENABLED ) logger.debug( "JobImpl(): exit") ;
      }

   }

    /** scan an element, extract information depending on the kind of element */
    private void scanElement(Element element, FactoryProvider facMan)
        throws QueryException {
        if(element.getTagName().equals( RunJobRequestDD.USERID_ELEMENT ) ) {
            //userId = element.getNodeValue().trim() ;
            userId = element.getFirstChild().getNodeValue().trim() ;  //toString().trim();
        }

        else if( element.getTagName().equals( RunJobRequestDD.COMMUNITY_ELEMENT ) ) {
         //community = element.getNodeValue().trim() ;
         community = element.getFirstChild().getNodeValue().trim() ;  //toString().trim();
        }

        else if( element.getTagName().equals( RunJobRequestDD.JOBSTEP_ELEMENT ) ) {
         jobStep = new JobStepImpl( element,facMan ) ;
        }
    } // end of JobImpl()


   /* (non-Javadoc)
    * @see org.astrogrid.datacenter.Job#informJobMonitor()
    */
   public void informJobMonitor() {
      if( TRACE_ENABLED ) logger.debug( "informJobMonitor(): entry") ;

      try {
         String
            requestTemplate = getConfiguration().getProperty( ConfigurationKeys.MONITOR_REQUEST_TEMPLATE
                                                , ConfigurationKeys.MONITOR_CATEGORY ) ;
         Object []
            inserts = new Object[8] ;
         inserts[0] = this.getName() ;
         inserts[1] = this.getUserId() ;
         inserts[2] = this.getCommunity() ;
         inserts[3] = this.getId() ;
         inserts[4] = this.getDate() ;
         inserts[5] = this.getJobStep().getName() ;
         inserts[6] = this.getJobStep().getStepNumber() ;
         inserts[7] = this.getStatus() ;
         Object []
            parms = new Object[] { MessageFormat.format( requestTemplate, inserts ) } ;

         Call
            call = (Call) new Service().createCall() ;

         call.setTargetEndpointAddress( new URL( this.getJobMonitorURL() ) ) ;
         call.setOperationName( "monitorJob" ) ;  // Set method to invoke
         call.addParameter("monitorJobXML", XMLType.XSD_STRING,ParameterMode.IN);
         call.setReturnType(XMLType.XSD_STRING);

         call.invokeOneWay( parms ) ;

      }
      catch ( Exception ex ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_JOBMONITOR
                                              , SUBCOMPONENT_NAME ) ;
         logger.error( message.toString(), ex ) ;
      }
      finally {
         if( TRACE_ENABLED ) logger.debug( "informJobMonitor(): exit") ;
      }

   } // end of informJobMonitor()


   public Connection getConnection() throws JobException {
      if( TRACE_ENABLED ) logger.debug( "getConnection(): entry") ;

      try{
         if( connection == null ) {
            logger.debug( "connection is null" ) ;
            DataSource
               dataSource ;
            logger.debug( "about to acquire datasource..." ) ;
            dataSource = getDataSource() ;
            logger.debug( "datasource acquired!" ) ;
            logger.debug( "about to acquire connection..." ) ;
            connection = dataSource.getConnection() ;
            logger.debug( "connection acquired!" ) ;
            logger.debug( "connection: " + connection.toString() ) ;
         }
         else {
            logger.debug( "connection is not null" ) ;
         }
      }
      catch( SQLException e ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION
                                              , SUBCOMPONENT_NAME ) ;
         logger.error( message.toString(), e ) ;
         throw new JobException( message, e );
      }
      finally{
         if( TRACE_ENABLED ) logger.debug( "getConnection(): exit") ;
      }

      return connection ;

   } // end of getConnection()

   /**  The job database, or - in JDBC terms - its DataSource */
   private static DataSource
      datasource = null ;
    public final static String      ASTROGRIDERROR_COULD_NOT_CREATE_JOB_DATASOURCE            = "AGDTCE00150";
   /**
     *
     * Returns the <code>DataSource</code> for the Job databasee.
     * <p>
     * This is a getter which constitutes a lazey initialization
     * routine for the Job DataSource. The DataSource is a static
     * object which is itself threadsafe, but the initialization routine
     * is synchronized and double locked to prevent problems
     * during initialization.
     *
     * @return The Job DataSource
     * @throws org.astrogrid.JobException
     *
     **/


   public  DataSource getDataSource() throws JobException {
      if( TRACE_ENABLED ) logger.debug( "JobImpl.getDataSource(): entry") ;

      String
         datasourceName = null ;

      try{

         // Note the double lock strategy
         if( datasource == null ){
            logger.debug("datasource is null") ;
            synchronized ( JobFactoryImpl.class ) {
               if( datasource == null ){
                  logger.debug( "about to acquire InitialContext..." ) ;
                  InitialContext
                    initialContext = new InitialContext() ;
                  logger.debug( "acquired InitialContext!" ) ;
                  logger.debug( "about to acquire datasource name/location..." ) ;
                  datasourceName = getConfiguration().getProperty( ConfigurationKeys.JOB_DATASOURCE_LOCATION
                                          , ConfigurationKeys.JOB_CATEGORY ) ;
                  logger.debug( "datasource name/location = " + datasourceName ) ;
                  logger.debug( "about to do lookup..." ) ;
                  datasource = (DataSource) initialContext.lookup( datasourceName ) ;
                  logger.debug( "datasource acquired!" ) ;
                  logger.debug( "datasource: " + datasource.toString() ) ;
               }
            } // end synchronized
         }
         else {
            logger.debug("datasource is not null") ;
         }

      }
      catch( NamingException ne ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_JOB_DATASOURCE
                                   , SUBCOMPONENT_NAME
                                   , (datasourceName != null ? datasourceName : "") ) ;
         logger.error( message.toString(), ne ) ;
         throw new JobException( message, ne );
      }
      finally{
         if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.getDataSource(): exit") ;
      }

      return datasource ;

   } // end of getDataSource()
   public PreparedStatement getPreparedStatement() throws JobException, SQLException {
      if( TRACE_ENABLED ) logger.debug( "getPreparedStatement(): entry") ;

      try {

         if( preparedStatement == null ) {
            Object[]
              inserts = new Object[1] ;
            inserts[0] = getConfiguration().getProperty( ConfigurationKeys.JOB_TABLENAME
                                           , ConfigurationKeys.JOB_CATEGORY ) ;
            String
              updateString = MessageFormat.format( JobFactoryImpl.UPDATE_TEMPLATE, inserts ) ;
            preparedStatement = getConnection().prepareStatement( updateString ) ;
         }

      }
      finally {
         if( TRACE_ENABLED ) logger.debug( "getPreparedStatement(): exit") ;
      }

      return preparedStatement ;

   }// end of getPreparedStatement()


   public static void setFactoryImpl(JobFactoryImpl someFactoryImpl) { factoryImpl = someFactoryImpl ; }
   public static JobFactoryImpl getFactoryImpl() { return factoryImpl ; }


    public void setDirty(boolean dirty) { this.dirty = dirty ; }


    public boolean isDirty() {return this.dirty ; }

} // end of class JobImpl
