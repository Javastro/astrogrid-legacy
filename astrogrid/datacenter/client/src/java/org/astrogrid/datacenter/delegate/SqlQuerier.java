/*
 * $Id: SqlQuerier.java,v 1.2 2004/01/13 00:32:47 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;

/**
 * A debugging delegate really, provides a direct route to the backend database (when it exists!) for
 * submitting SQL directly.  Provided also so that astronomers can 'gain confidence' in using services, with
 * whatever stored-procedures might already exist.
 *
 * @author M Hill
 * @deprecated use {@link FullSearcher} instead. This interface will disappear very soon.
 */

public interface SqlQuerier
{
   /**
    * Simple blocking query - takes SQL and submits to the backend database, returning results
    */
   public DatacenterResults doSqlQuery(String resultsFormat,  String sql) throws IOException;
      
      
}

/*
$Log: SqlQuerier.java,v $
Revision 1.2  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.1.10.2  2004/01/12 16:44:07  nw
Reintroduced deprecated interfaces for backwards compatability

sql passthru finished and tested

Revision 1.1  2003/11/25 14:20:05  mch
New SQL-passthrough interface

 */

