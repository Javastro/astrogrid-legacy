/*
 * $Id: DatabaseQuerier.java,v 1.8 2003/09/05 13:20:28 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.astrogrid.datacenter.config.Configuration;
import org.w3c.dom.Node;

/**
 * The abstract class including factory, that all database queriers must implement
 *
 * @author M Hill
 */

public abstract class DatabaseQuerier
{
   /** Key to configuration files' entry that tells us what database querier
    * to use with this service
    */
   public static final String DATABASE_QUERIER = "Database Querier Class";
   /** configuration file key, that gives us the name of a datasource in JNDI to use for this database querier */
   public static final String JNDI_DATASOURCE = "Database Querier JNDI Datasource";
   /** configuration file key, stores a JDBC connection URL for tis database querier */
   public static final String JDBC_URL = "Database Querier JDBC URL";
   /** configuration file key, stores a set of properties for the connection */
   public static final String CONNECTION_PROPERTIES = "Database Querier Connection Properties";

   /**
    * Applies the given adql to the database, returning an abstract handle
    * to whatever the results are
    */
   public abstract QueryResults queryDatabase(Node n) throws DatabaseAccessException;


/** close the connection to the database - release resources
 * 
 */
   public abstract void close() throws DatabaseAccessException;
   /**
    * Returns a Querier implementation based on the settings in the configuration file.
    * <p>
    * Behaviour.
    * <ol>
    * <li>Loads class specified by {@link #DATABASE_QUERIER} in configuration, which must be a subclass of this class
    * <li>Checks JNDI for a datasource under {@link #JNDI_DATASOURCE}. If present, use this to initialize the querier
    * <li>Otherwise, checks for a database url in configuration under {@link #JDBC_URL}, and 
    * optional connection properties under {@link #CONNECTION_PROPERTIES}. If url is present, this is used to initialize the querier
    * <li>Otherwise, attempt to instantiate the querier using a default no-arg constructor
    * </ul>
  @throws DatabaseAccessException on error (contains core exception)

  *  */
   public static DatabaseQuerier createQuerier() throws DatabaseAccessException
   {
      String querierClass = Configuration.getProperty(DATABASE_QUERIER);
//       "org.astrogrid.datacenter.queriers.sql.SqlQuerier"    //default to general SQL querier

      if (querierClass == null)
      {
         throw new DatabaseAccessException("Database Querier key ["+DATABASE_QUERIER+"] cannot be found in the configuration file(s) '"+Configuration.getLocations()+"'" );
      }
        
      try
      {
         Class qClass =  Class.forName(querierClass);
         // now look for jndi link to datasource, 
         String jndiDataSourceName = null;
         if ( (jndiDataSourceName = Configuration.getProperty(JNDI_DATASOURCE)) != null) {
             // retrieve data source,
             DataSource ds = (DataSource)new InitialContext().lookup(jndiDataSourceName);
             return (DatabaseQuerier)qClass.getConstructor(new Class[]{DataSource.class}).newInstance(new Object[]{ds});
         }
         // failing that, look for a URL in configuration
         String jdbcURL = null;
         if ( (jdbcURL = Configuration.getProperty(JDBC_URL)) != null) {
             Properties p = new Properties();
             String props = Configuration.getProperty(CONNECTION_PROPERTIES,"");
             p.load(new ByteArrayInputStream(props.getBytes()));
             return (DatabaseQuerier)qClass.getConstructor(new Class[]{String.class,Properties.class}).newInstance(new Object[]{jdbcURL,p});
         }
         // try for a default constructor then, and hope for the best.
         return (DatabaseQuerier)qClass.newInstance();
                 
         
      }
      catch (ClassNotFoundException e)
      {
         throw new DatabaseAccessException(e,"Could not load DatabaseQuerier '"+querierClass+"'");
      }
      catch (IllegalAccessException e)
      {
         throw new DatabaseAccessException(e,"Could not load DatabaseQuerier '"+querierClass+"'");
      }
      catch (InstantiationException e)
      {
         throw new DatabaseAccessException(e,"Could not load DatabaseQuerier '"+querierClass+"'");
      }
      catch (NoSuchMethodException e) {
          throw new DatabaseAccessException(e,"Could not load DatabaseQuerier '" + querierClass + "'");
      } catch (InvocationTargetException e) {
          throw new DatabaseAccessException(e,"Could not load DatabaseQuerier '" + querierClass + "'");
      } catch (IOException e) {
          throw new DatabaseAccessException(e,"Could not load connection properties for Database Querier '" + querierClass + "'");
      } catch (NamingException e) {
          throw new DatabaseAccessException(e,"Could not load DataSource for Database Querier '" + querierClass + "'");
      }
   }

}

