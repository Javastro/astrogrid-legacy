/*
 * $Id: DataQueryService.java,v 1.3 2003/08/28 17:28:10 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;
import org.astrogrid.datacenter.servicestatus.ServiceStatus;

/**
 * Manages a single query being made on the dataset.  Made from a combination
 * of It02 Job/Jobstep and the Ace instance managers (eg TempDirManager).
 *
 * @author M Hill
 */

public class DataQueryService implements ServiceStatus
{
   /** temporary used for generating unique handles - see generateHandle() */
   private static java.util.Random random = new java.util.Random();

   private Vector serviceListeners = new Vector();

   private String status = UNKNOWN;

   /**
    * A handle is used to identify a particular service.  It is also used as the
    * basis for any temporary storage.
    */
   String handle = null;

   /**
    * Constructor - creates a handle to identify this instance
    */
   public DataQueryService()
   {
      handle = generateHandle();
   }

   /**
    * Returns this instances handle
    */
   public String getHandle()
   {
      return handle;
   }

   /**
    * Generates a handle for use by a particular instance; uses the current
    * time to help us debug (ie we can look at the temporary directories and
    * see which was the last run). Later we could add service/user information
    * if available
    */
   private static String generateHandle()
   {
      Date todayNow = new Date();

      return todayNow.getYear()+"-"+todayNow.getMonth()+"-"+todayNow.getDate()+"_"+
               todayNow.getHours()+"."+todayNow.getMinutes()+"."+todayNow.getSeconds()+
               "_"+(random.nextInt(8999999) + 1000000); //plus botched bit... not really unique

   }

   /**
    * Runs a blocking query.  NB the given DOM document may include other tags, so we need
    * to extract the right elements
    */
   public Element runQuery(Element domContainingQuery) throws QueryException, DatabaseAccessException, IOException
   {
      fireStatusChanged(STARTING);
      Query query = new Query(domContainingQuery);

      DatabaseQuerier querier = DatabaseQuerier.createQuerier(query);

      QueryResults results = querier.queryDatabase(query);

      return results.toVotable().getDocumentElement();
   }

   /**
    * Returns the current status
    */
   public String getStatus()
   {
      return status;
   }

   /**
    * Register a status listener.  This will be informed of changes in status
    * to the service - IF the service supports such info.  Otherwise it will
    * just get 'starting', 'working' and 'completed' messages based around the
    * basic http exchange.
    */
   public void registerServiceListener(ServiceListener aListener)
   {
      serviceListeners.add(aListener);
   }

   /** informs all listeners of the new status change. Not threadsafe...
    */
   protected void fireStatusChanged(String newStatus)
   {
      status = newStatus;

      for (int i=0;i<serviceListeners.size();i++)
      {
         ((ServiceListener) serviceListeners.get(i)).serviceStatusChanged(newStatus);
      }
   }

}

