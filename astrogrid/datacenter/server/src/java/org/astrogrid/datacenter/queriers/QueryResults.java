/*
 * $Id: QueryResults.java,v 1.7 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** A container interface that holds the results of a query until needed.
 * <p>
 *   Basically we
 * don't know what format the raw results will be in (eg, they may be SqlResults for
 * an JDBC connection, but something else altogether for other catalogue formats)
 * so this is a 'container' to hold those results until it needs to be
 * translated. It would be fully
 * implemented by the same package that implements the DatabaseQuerier and
 * QueryTranslater.
 *
 * @author M Hill
 */

public interface QueryResults
{
   public static final String FORMAT_VOTABLE = "VOTABLE"; //require results to be in votable format
   
   /** Returns an inputstream to the raw binary data of the result set
    *
   public InputStream getInputStream() throws IOException;
    don't really know what we want to do with this yet */

   /** All Virtual Observatories must be able to provide the results in VOTable
    * format.  It makes sense to throw XML formatting errors here as it
    * is likely that some XML formatting will be done.
    * <p>
    * This method returns a dom instance of the votable.  This is not
    * always sensible, as it may be very large, but is used generally for
    * stateless http blocking queries. For general streaming output, see
    * toVotable(OutputStream)
    *
    */
   public Document toVotable(QuerierProcessingResults statusToUpdate) throws IOException, SAXException;
   
   /** All Virtual Observatories must be able to provide the results in VOTable
    * format.  It makes sense to throw XML formatting errors here as it
    * is likely that some XML formatting will be done.
    * <p>
    * This method streams the votable to the given output stream as it is
    * formed from the results.
    */
   public void toVotable(OutputStream out, QuerierProcessingResults statusToUpdate) throws IOException;

   /** As above */
   public void toVotable(Writer out, QuerierProcessingResults statusToUpdate) throws IOException;
   
   /** Returns the number of results - or -1 if unknown */
   public int getCount() throws IOException;
}

