/*$Id: QuerierManager.java,v 1.4 2003/11/27 17:28:09 nw Exp $
 * Created on 24-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.datacenter.axisdataserver.types._QueryId;
import org.astrogrid.datacenter.queriers.spi.QuerierSPI;
import org.astrogrid.datacenter.queriers.sql.SqlQuerierSPI;
import org.astrogrid.datacenter.snippet.CommunityHelper;
import org.astrogrid.datacenter.snippet.DocHelper;
import org.astrogrid.datacenter.snippet.DocMessageHelper;
import org.astrogrid.util.Workspace;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;

/** Manages the construction and initialization of Queriers,
 * and maintains a  collection of current Queriers
 * - composed of the static methods factored out of DatabaseQuerier - trying to simplify that class.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Sep-2003
 *  @later - move from a static container to an object - can then have multiple containers if needed.
 *
 */
public class QuerierManager {
   
   private static final Log log = LogFactory.getLog(QuerierManager.class);
   /** Key to configuration files' entry that tells us what database querier
    * to use with this service   */
   public static final String QUERIER_SPI_KEY = "QuerierSPI";
   
   /** lookup table of all the current queriers indexed by their handle*/
   protected static Hashtable queriers = new Hashtable();
   
   /** temporary used for generating unique handles - see generateHandle() */
   static java.util.Random random = new java.util.Random();
   
   /** Key to configuration entry for the default target myspace for this server */
   public static final String RESULTS_TARGET_KEY = "DefaultMySpace";
   

   public static Querier getQuerier(_QueryId qid) {
      return (Querier) queriers.get(qid.getId());
   }
   
   /** Class method that returns a list of all the currently running queriers
    */
   public static Collection getQueriers() {
      return queriers.values();
   }
   
   
   /**
    * A factory method that Returns a Querier implementation based on the
    * settings in the configuration file and the document parameter.
    * @throws DatabaseAccessException on error (contains cause exception)
    * @deprecated Use createQuerier(Query);
    * @todo - move the extra bits in this method into the OO-version
    * @todo - temporarily commented out.
    */
   public static Querier createQuerier(Element rootElement)
      throws DatabaseAccessException {
      
      Querier querier = new Querier(instantiateQuerierSPI(),null,null,null); // bodge
      //assigns handle
      try {
         String handle = generateHandle();
         String resultsDestination = SimpleConfig.getProperty(QuerierManager.RESULTS_TARGET_KEY);
         // extract values from document, if present.
         if (rootElement != null) {
            String aHandle = DocHelper.getTagValue(rootElement, DocMessageHelper.ASSIGN_QUERY_ID_TAG);
            if (aHandle != null) {
               handle = aHandle;
            }
            String aResultsDestination = DocHelper.getTagValue(rootElement, DocMessageHelper.RESULTS_TARGET_TAG);
            if (aResultsDestination != null) {
               resultsDestination = aResultsDestination;
            }
         }
         if (queriers.get(handle) != null) {
            log.error( "Handle '" + handle + "' already in use");
            throw new IllegalArgumentException("Handle " + handle + " already in use");
         }
         //querier.setHandle(handle); bodge
         queriers.put(handle, querier);
         
         //querier.setWorkspace(new Workspace(handle)); bodge
         querier.setResultsDestination(resultsDestination);
         
         // finally
         if (rootElement != null) {
             // bodge
            //querier.setCertification(CommunityHelper.getCertification(rootElement));
           //Query q = (Query)Unmarshaller.unmarshal(Query.class,rootElement);
           
            //querier.setQuery(q);
            querier.registerWebListeners(rootElement);  //looks through dom for web listeners
         }
         return querier;
      }
      catch (IOException e) {
         reportException(e);
      }
      /*
      catch (MarshalException e) {
         reportException(e);
      }
      catch (ValidationException e) {
         reportException(e);
      }*/
      
      return null; // unreachable;
   }
   
   /**
    * Creates an adql querier with a generated (unique-to-this-service) handle
    */
   public static Querier createQuerier(_query q) throws DatabaseAccessException {
      return QuerierManager.createQuerier(q, generateHandle());
   }
   
 
   
   /**
    * A factory method that Returns a Querier implementation based on the
    * settings in the configuration file and the document parameter.
    * @throws DatabaseAccessException on error (contains cause exception)
    * @todo - add parsing of results target?
    */
   public static Querier createQuerier(_query query, String handle) throws DatabaseAccessException {
      
      QuerierSPI spi = instantiateQuerierSPI();
      //assigns handle
      try {
         if (queriers.get(handle) != null) {
            log.error( "Handle '" + handle + "' already in use");
            throw new IllegalArgumentException("Handle " + handle + "already in use");
         }
         Workspace workspace = new Workspace(handle);
         Querier querier = new Querier(spi,query,workspace,handle);
         queriers.put(handle, querier);

         return querier;
      }
      catch (IOException e) {
         throw new DatabaseAccessException(e,"Could not create workspace for id:"+handle);
      }
   }
   
   
   /** method that handles the business of instantiating the querier object */
   public static QuerierSPI instantiateQuerierSPI()
      throws DatabaseAccessException {
      String querierSpiClass = SimpleConfig.getProperty(QUERIER_SPI_KEY);
      
      if (querierSpiClass == null) {
         throw new DatabaseAccessException(" Querier key [" + QUERIER_SPI_KEY + "] "
                                              + "cannot be found in the configuration file(s) '"  + SimpleConfig.getLocations() + "'");
      }
      
      //create querier implementation
      try {
         Class qClass = Class.forName(querierSpiClass);
         /* NWW - interesting bug here.
          original code used class.newInstance(); this method doesn't declare it throws InvocationTargetException,
          however, this exception _is_ thrown if an exception is thrown by the constructor (as is often the case at the moment)
          worse, as InvocatioinTargetException is a checked exception, the compiler rejects code with a catch clause for
          invocationTargetExcetpion - as it thinks it cannot be thrown.
          this means the exception boils out of the code, and is unstoppable - dodgy
          work-around - use the equivalent methods on java.lang.reflect.Constructor - which do throw the correct exceptions */
         // Original Code
         //DatabaseQuerier querier = (DatabaseQuerier)qClass.newInstance();
         // safe equivalent
         Constructor constr = qClass.getConstructor(new Class[] { });
         Object unknown = constr.newInstance(new Object[] {});
         if (! (unknown instanceof QuerierSPI)) {
             log.error("Could not load Querier : incorrect class" + unknown.getClass().getName());
             throw new DatabaseAccessException("Could not load Querier: incorrect class" + unknown.getClass().getName());
         } 
         QuerierSPI spi = (QuerierSPI) unknown;
         log.info(spi.getPluginInfo());
         return spi;
         
      } catch (InvocationTargetException e) {
         // interested in the root cause here - invocation target is just a wrapper, and not meaningful in itself.
         reportException(e.getCause());
      } catch (ClassNotFoundException e) {
         reportException(e);
      } catch (IllegalAccessException e) {
         reportException(e);
      } catch (NoSuchMethodException e) {
         reportException(e);
      } catch (InstantiationException e) {
         reportException(e);
      }
      return null;
   }
   
   private static void reportException(Throwable t) throws DatabaseAccessException {
      log.error("Could not load Database Querier: " + t.getMessage(),t);
      throw new DatabaseAccessException(t,"Could not load Database Querier: " + t.getMessage());
   }
      
   
   /**
    * Generates a handle for use by a particular instance; uses the current
    * time to help us debug (ie we can look at the temporary directories and
    * see which was the last run). Later we could add service/user information
    * if available
    */
   static String generateHandle() {
      Date todayNow = new Date();
      
      return todayNow.getYear()
         + "-"
         + todayNow.getMonth()
         + "-"
         + todayNow.getDate()
         + "_"
         + todayNow.getHours()
         + "."
         + todayNow.getMinutes()
         + "."
         + todayNow.getSeconds()
         + "_"
         + (random.nextInt(8999999) + 1000000);
      //plus botched bit... not really unique
      
   }
   
}

/*
 $Log: QuerierManager.java,v $
 Revision 1.4  2003/11/27 17:28:09  nw
 finished plugin-refactoring

 Revision 1.3  2003/11/27 00:52:58  nw
 refactored to introduce plugin-back end and translator maps.
 interfaces in place. still broken code in places.

 Revision 1.2  2003/11/25 18:50:06  mch
 Abstracted Querier from DatabaseQuerier

 Revision 1.1  2003/11/25 14:17:24  mch
 Extracting Querier from DatabaseQuerier to handle non-database backends

 Revision 1.5  2003/11/25 11:57:56  mch
 Added framework for SQL-passthrough queries

 Revision 1.4  2003/11/21 17:37:56  nw
 made a start tidying up the server.
 reduced the number of failing tests
 found commented out code

 Revision 1.3  2003/11/18 11:10:16  mch
 Removed client dependencies on server

 Revision 1.2  2003/11/17 15:41:48  mch
 Package movements

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.5  2003/11/05 18:57:26  mch
 Build fixes for change to SOAPy Beans and new delegates

 Revision 1.4  2003/10/06 18:56:27  mch
 Naughtily large set of changes converting to SOAPy bean/interface-based delegates

 Revision 1.3  2003/09/26 11:38:00  nw
 improved documentation, fixed imports

 Revision 1.2  2003/09/25 01:23:28  nw
 altered visibility on generateHandle() so it can be used within DummyQuerier

 Revision 1.1  2003/09/24 21:02:45  nw
 Factored creation and management of database queriers
 into separate class. Simplifies DatabaseQuerier.

 Database Querier - added calls to timer, untagled status transitions
 
 */
