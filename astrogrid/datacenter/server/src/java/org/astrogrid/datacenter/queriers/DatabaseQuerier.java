/*
 * $Id $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.service.JobNotifyServiceListener;
import org.astrogrid.datacenter.service.WebNotifyServiceListener;
import org.astrogrid.datacenter.snippet.DocMessageHelper;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceDummyDelegate;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.util.Workspace;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Querier classes handle a single, individual query to an individual
 * database.  If two queries will be made on a database, two Querier instances
 * will be required.  A 'database' here is a database management system; eg
 * MySQL, Microsoft's SQL server, etc.
 * <p>
 * @see package documentation
 * <p>
 * This class provides factory methods for both blocking and non-blocking
 * queries, hopefully completely hiding the two-way translation process
 *
 * @see doQueryGetVotable()
 * @see spawnQuery
 * <p>
 * @author M Hill
 * @todo reinstate reference to MyspaceDummyDelegate, once myspace delegate is built
 */

public abstract class DatabaseQuerier extends Querier {
   
   /** The query object model */
   private Query query = null;
   
   
   /** sets the query model */
   public void setQuery(Query q) {
      this.query =q;
      this.setStatus(QueryStatus.CONSTRUCTED);
   }
   
   
   
   /** Updates the status and does the query (by calling the abstract
    * queryDatabase() overridden by subclasses) and returns the results.
    * Use by both synchronous (blocking) and asynchronous (threaded) querying
    */
   public QueryResults doQuery() throws DatabaseAccessException {
      setStatus(QueryStatus.RUNNING_QUERY);
      setStartTime(new Date());
      QueryResults results = queryDatabase(query);
      setCompletedTime(new Date());
      setStatus(QueryStatus.QUERY_COMPLETE);
      
      return results;
   }
   
   
   /**
    * Applies the given query, to the database,
    * returning the results wrapped in QueryResults.
    */
   public abstract QueryResults queryDatabase(Query aQuery) throws DatabaseAccessException;
   
   
}
/*
 $Log: DatabaseQuerier.java,v $
 Revision 1.8  2003/11/25 14:17:24  mch
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

