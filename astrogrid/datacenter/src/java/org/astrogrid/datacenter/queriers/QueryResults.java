/*
 * $Id: QueryResults.java,v 1.3 2003/09/08 16:34:31 mch Exp $
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
 * so this is a 'container' to hold those results until it needs to be
 * translated. It whoule be fully
 * implemented by the same package that implements the DatabaseQuerier and
 * querytranslater.
 *
 * @author M Hill
 */

public interface QueryResults
{
   /** Returns an inputstream to the raw binary data of the result set
    *
   public InputStream getInputStream() throws IOException;
    don't really know what we want to do with this yet */

   /** All Virtual Observatories must be able to provide the results in VOTable
    * format yeuch.  It makes sense to throw XML formatting errors here as it
    * is likely that some XML formatting will be done...
    */
   public Document toVotable() throws IOException;
}

