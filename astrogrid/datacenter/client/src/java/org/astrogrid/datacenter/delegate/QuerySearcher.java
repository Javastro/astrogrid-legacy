/*
 * $Id: QuerySearcher.java,v 1.1 2004/03/12 20:00:11 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;
import java.io.InputStream;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.store.Agsl;

/**
 * Defines the interface for clients that want to query using complex query languages
 *
 * @author nww
 * @see org.astrogrid.datacenter.adql.ADQLUtils
 * @see org.astrogrid.datacenter.sql.SQLUtils
 */

public interface QuerySearcher extends ConeSearcher
{
   public final static String VOTABLE = "Votable"; //results format
   
   /**
    * Simple blocking query; submit Query, get stream to results
    */
   public InputStream askQuery(Query query, String resultsFormat) throws IOException;

   /**
    * Submits a query (asynchronous), returning a string identifying the query
    */
   public String submitQuery(Query query, Agsl resultsTarget, String resultsFormat) throws IOException;
   
   /**
    * Attempt to stop a query
    */
   public void abortQuery(String id) throws IOException;
}

/*
$Log: QuerySearcher.java,v $
Revision 1.1  2004/03/12 20:00:11  mch
It05 Refactor (Client)

Revision 1.6  2004/03/08 15:54:57  mch
Better exception passing, removed Metdata

Revision 1.5  2004/03/06 19:34:21  mch
Merged in mostly support code (eg web query form) changes

Revision 1.3  2004/01/14 13:12:47  nw
improved documentation

Revision 1.2  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.1.2.1  2004/01/07 15:25:44  nw
removed adql-specific nature.

Revision 1.3  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.2  2003/11/25 11:54:41  mch
Added framework for SQL-passthrough queries

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.2  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/


