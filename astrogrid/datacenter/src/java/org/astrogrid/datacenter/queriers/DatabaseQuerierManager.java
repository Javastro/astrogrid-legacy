/*$Id: DatabaseQuerierManager.java,v 1.2 2003/09/25 01:23:28 nw Exp $
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
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.common.CommunityHelper;
import org.astrogrid.datacenter.common.DocHelper;
import org.astrogrid.datacenter.common.DocMessageHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.service.Workspace;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;

/** Manages the construction and initialization of DatabaseQueriers,
 * and maintains a  collection of current Database Queriers
 * - composed of the static methods factored out of DatabaseQuerier - trying to simplify that class.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Sep-2003
 *  @later - move from a static container to an object - can then have multiple containers if needed.
 *
 */
public class DatabaseQuerierManager {

    /** Key to configuration files' entry that tells us what database querier
        * to use with this service   */
    public static final String DATABASE_QUERIER_KEY = "DatabaseQuerierClass";

    /** lookup table of all the current queriers indexed by their handle*/
    protected static Hashtable queriers = new Hashtable();

    /** temporary used for generating unique handles - see generateHandle() */
    static java.util.Random random = new java.util.Random();

    /** Key to configuration entry for the default target myspace for this server */
    public static final String RESULTS_TARGET_KEY = "DefaultMySpace";

    /** Class method that returns the querier instance with the given handle
        */
    public static DatabaseQuerier getQuerier(String aHandle) {
        return (DatabaseQuerier) queriers.get(aHandle);
    }

    /** Class method that returns a list of all the currently running queriers
        */
    public static Collection getQueriers() {
        return queriers.values();
    }

    public static DatabaseQuerier createQuerier()
        throws DatabaseAccessException {
        return createQuerier(null);
    }

    /**
        * A factory method that Returns a Querier implementation based on the
        * settings in the configuration file and the document parameter.
        * @throws DatabaseAccessException on error (contains cause exception)
        *
        */
    public static DatabaseQuerier createQuerier(Element rootElement)
        throws DatabaseAccessException {
        DatabaseQuerier querier = instantiateQuerier();
        //assigns handle
        try {
            String handle = generateHandle();
            String resultsDestination = Configuration.getProperty(DatabaseQuerierManager.RESULTS_TARGET_KEY);
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
            Log.affirm(queriers.get(handle) == null, "Handle '" + handle + "' already in use");
            querier.setHandle(handle);
            queriers.put(handle, querier);
    
            querier.setWorkspace(new Workspace(handle));
            querier.setResultsDestination(resultsDestination);
    
            // finally 
            if (rootElement != null) {
                querier.setUserId(CommunityHelper.getUserId(rootElement));
                querier.setCommunityId(CommunityHelper.getCommunityId(rootElement));
                querier.setQuery(rootElement);
                querier.registerWebListeners(rootElement);  //looks through dom for web listeners
            }
            return querier;
        } 
        catch (IOException e) {
            reportException(e);
        }
        catch (ADQLException e) {
            reportException(e);
        }
        catch (QueryException e) {
            reportException(e);
        }
        return null; // unreachable;
    }

    /** method that handles the business of instantiating the querier object */
    private static DatabaseQuerier instantiateQuerier()
        throws DatabaseAccessException {
        String querierClass = Configuration.getProperty(DATABASE_QUERIER_KEY);
        //       "org.astrogrid.datacenter.queriers.sql.SqlQuerier"    //default to general SQL querier

        if (querierClass == null) {
            throw new DatabaseAccessException("Database Querier key [" + DATABASE_QUERIER_KEY + "] "
                    + "cannot be found in the configuration file(s) '"  + Configuration.getLocations() + "'");
        }

        //create querier implementation              
        try {
            Class qClass = Class.forName(querierClass);
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
            return (DatabaseQuerier) constr.newInstance(new Object[] {});

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
        //Log.logWarning() - log a message here.
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
$Log: DatabaseQuerierManager.java,v $
Revision 1.2  2003/09/25 01:23:28  nw
altered visibility on generateHandle() so it can be used within DummyQuerier

Revision 1.1  2003/09/24 21:02:45  nw
Factored creation and management of database queriers
into separate class. Simplifies DatabaseQuerier.

Database Querier - added calls to timer, untagled status transitions
 
*/