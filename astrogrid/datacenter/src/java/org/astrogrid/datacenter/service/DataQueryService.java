/*
 * $Id: DataQueryService.java,v 1.1 2003/08/27 09:58:07 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.util.Date;
import org.w3c.dom.Element;

/**
 * Manages a single query being made on the dataset.  Made from a combination
 * of It02 Job/Jobstep and the Ace instance managers (eg TempDirManager).
 *
 * @author M Hill
 */

public class DataQueryService
{
   /** temporary used for generating unique handles - see generateHandle() */
   protected static java.util.Random random = new java.util.Random();

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
   public Element runQuery(Element domContainingQuery)
   {
      return null;
   }
}

