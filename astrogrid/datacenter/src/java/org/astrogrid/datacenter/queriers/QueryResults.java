/*
 * $Id: QueryResults.java,v 1.2 2003/09/04 09:23:16 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.io.InputStream;
import org.astrogrid.datacenter.service.Workspace;
import org.w3c.dom.Document;

/**
 * Not sure whether this should be an interface or a class yet.  Basically we
 * don't know what format the results will appear in (in a generalised sense)
 * so this is a temporary 'container' way of handling it, that should be fully
 * implemented by the same package that implements the DatabaseQuerier.
 *
 * @author M Hill
 */

public interface QueryResults
{
   /** Returns an inputstream to the raw binary data of the result set
    */
   public InputStream getInputStream() throws IOException;

   /** All Virtual Observatories must be able to provide the results in VOTable
    * format yeuch.  It makes sense to throw XML formatting errors here as it
    * is likely that some XML formatting will be done...
    */
   public Document toVotable(Workspace workspace) throws IOException;
}

