/*
 * $Id $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.spi;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.QueryStatus;
import org.w3c.dom.Element;

/** Back-end Plugin Adapter - loads & wraps a {@link QuerierSPI} so that it appears as a {@link org.astrogrid.datacenter.queriers.Querier}
 * 
 * <p>
 *  This class extends{@link org.astrogrid.datacenter.queriers.Querier} and  acts as an
 * adapter between the structured plugin system provided by this package, and the 
 * monolithic plugin system provided by extending <tt>Querier</tt> 
 * <p>
 * Loads a selected <tt>QuerierSPI</tt> from classpath. Plugin to load is determined by value of {@link #QUERIER_SPI_KEY} in the 
 * system configuration.
 * 
 *  
 * @see package documentation
 * @author N Winstanley
 * @todo give it a better name?
 */

public class PluginQuerier extends Querier {
   
   /** the plugin we're managing */
   protected final QuerierSPI spi;
      
   /** Key to configuration files' entry that tells us what database querier
    * to use with this service   */
   public static final String QUERIER_SPI_KEY = "QuerierSPI";

   
   /**
    * Standard Querier constructor */
   public PluginQuerier(String queryId, Query query) throws IOException {
      super(queryId, query);
      this.spi =  instantiateQuerierSPI();
   }
   
   /** Quickfix so that plugin tests work after my abstracting Querier.  spi
    * should really be set using the configuration files - MCH, 1/12/2003 */
   public PluginQuerier(QuerierSPI givenSpi, String queryId, Query query) throws IOException
   {
      super(queryId, query);
      this.spi = givenSpi;
   }
   

   /** Performs a query
    * <p>
    * Extracts the namespace attribute of the query document, and uses this to select the appropriate language translator from the plugin.
    * After applying a language translator, the resulting object is passed to the plugin's <tt>doQuery</tt> method.
    * <p>
    * Meanwhile, it ensures that the right status-change events are fired, times the execution, and catches and logs all errors raised by the plugin. 
     */
    public QueryResults doQuery() throws DatabaseAccessException {
        // initialize the spi.
        spi.setWorkspace(workspace);
        setStatus(QueryStatus.CONSTRUCTED);
        
        // find the translator
        Element queryBody = query.getQueryBody();
        String namespaceURI = queryBody.getNamespaceURI();
        if (namespaceURI == null) {
            // maybe not using namespace aware parser - see if we can find an xmlns attribute instead
            namespaceURI = queryBody.getAttribute("xmlns");
        }
        if (namespaceURI == null) {
            XMLUtils.PrettyElementToStream(queryBody,System.out);
            throw new DatabaseAccessException("Query body has no namespace - cannot determine language");
        }
        Translator trans = spi.getTranslatorMap().lookup(namespaceURI);
        if (trans == null) {
            throw new DatabaseAccessException("No translator available for namespace: " + namespaceURI);
        }
        // do the translation
        Object intermediateRep = null;
        Class expectedType = null;
        try { // don't trust it.
            intermediateRep = trans.translate(queryBody);
            expectedType = trans.getResultType();
            if (! expectedType.isInstance(intermediateRep)) { // checks result is non-null and the right type.
                throw new DatabaseAccessException("Translation result " + intermediateRep.getClass().getName() + " not of expected type " + expectedType.getName());
            }
        } catch (Throwable t) {
            throw new DatabaseAccessException(t,"Translation phase failed:" + t.getMessage());
        }

        
        //do the query.
        setStatus(QueryStatus.RUNNING_QUERY);
        setStartTime(new Date());
        try {
            QueryResults results = spi.doQuery(intermediateRep,expectedType);
            setCompletedTime(new Date());
            setStatus(QueryStatus.QUERY_COMPLETE);
            return results;
        } catch (Throwable t) {
            throw new DatabaseAccessException(t,"Query phase failed:" + t.getMessage());
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
      
       
}
/*
 $Log: PluginQuerier.java,v $
 Revision 1.3  2004/01/15 12:38:40  nw
 improved documentation

 Revision 1.2  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.1.10.2  2004/01/08 09:43:41  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.1.10.1  2004/01/07 11:51:07  nw
 found out how to get wsdl to generate nice java class names.
 Replaced _query with Query throughout sources.

 Revision 1.1  2003/12/01 20:59:05  mch
 Abstracting coarse-grained plugin

 Revision 1.5  2003/11/28 16:10:30  nw
 finished plugin-rewrite.
 added tests to cover plugin system.
 cleaned up querier & queriermanager. tested

 Revision 1.4  2003/11/27 17:28:09  nw
 finished plugin-refactoring

 Revision 1.3  2003/11/27 00:52:58  nw
 refactored to introduce plugin-back end and translator maps.
 interfaces in place. still broken code in places.

 Revision 1.2  2003/11/25 18:50:06  mch
 Abstracted Querier from DatabaseQuerier

 Revision 1.1  2003/11/25 14:17:24  mch
 Extracting Querier from DatabaseQuerier to handle non-database backends

 Revision 1.7  2003/11/25 11:57:32  mch
 Added framework for SQL-passthrough queries

 Revision 1.6  2003/11/21 17:37:56  nw
 made a start tidying up the server.
 reduced the number of failing tests
 found commented out code

 Revision 1.5  2003/11/18 14:36:21  nw
 temporarily commented out references to MySpaceDummyDelegate, so that the sustem will build

 Revision 1.4  2003/11/18 11:10:16  mch
 Removed client dependencies on server

 Revision 1.3  2003/11/17 15:41:48  mch
 Package movements

 Revision 1.2  2003/11/17 12:16:33  nw
 first stab at mavenizing the subprojects.

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.37  2003/11/06 22:06:00  mch
 Reintroduced credentials (removed them due to out of date myspace delegate)

 Revision 1.36  2003/11/06 11:38:48  mch
 Introducing SOAPy Beans

 */


