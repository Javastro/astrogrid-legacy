/*
 * $Id: SqlResults.java,v 1.1 2003/08/26 16:40:54 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.w3c.dom.Document;

/**
 * Basically a wrapper around a ResultSet.  Can be used (I believe) for any
 * SQL/JDBC query results.
 *
 * @author M Hill
 */

public class SqlResults implements QueryResults
{
   private ResultSet results;

   public SqlResults(ResultSet givenResults)
   {
   }

   public Document toVotable() throws IOException
   {
      return null;
   }

   public InputStream getInputStream() throws IOException
   {
      return null;
   }


}

