/*
 * $Id: SqlResults.java,v 1.4 2003/09/08 16:44:53 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.service.Workspace;
import org.objectwiz.votable.ResultSetConverter;
import org.objectwiz.votable.ResultSetToSimpleVOTable;
import org.objectwiz.votable.exception.ConverterException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Basically a wrapper around a ResultSet.  Can be used (I believe) for any
 * SQL/JDBC query results.
 *
 * @author M Hill
 */

public class SqlResults implements QueryResults
{
   protected ResultSet results;
   protected ResultSetConverter converter;
   protected Workspace workspace = null;

   /**
    * Construct this wrapper around the given JDBC/SQL ResultSet.  We don't
    * know how big this result set will be, so it's likely we'll need a workspace
    * for any temporary files created when doing conversions
    */
   public SqlResults(ResultSet givenResults, Workspace givenWorkspace)
   {
      this.results = givenResults;
      this.converter = new ResultSetToSimpleVOTable("","","","","");
      this.workspace = givenWorkspace;
   }
   
   /**
    * Converts the resultset to VOTable Document.
    */
   public Document toVotable() throws IOException
   {
      try
      {
         //don't know how big the result set is so use the workspace - unless
         //it's null, in which case work from memory
         if (workspace == null)
         {
            File workfile = workspace.makeWorkFile("votableResults.vot.xml"); //should go into workspace...
   
            PrintStream out = new PrintStream( new FileOutputStream(workfile) );
            converter.serialize( results, out );
            out.close();
            return XMLUtils.newDocument(new FileInputStream(workfile));
         }
         else
         {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(ba);
            converter.serialize(results,ps);
            ps.close();
            return XMLUtils.newDocument(new ByteArrayInputStream(ba.toByteArray()));
         }
      }
      catch (SAXException e)
      {
         IOException ioe = new IOException("Error in generated votable xml: "+e.toString());
         ioe.setStackTrace(e.getStackTrace());
         throw ioe;

      }
      catch (ParserConfigurationException e)
      {
         IOException ioe = new IOException("Error in program configuration: "+e.toString());
         ioe.setStackTrace(e.getStackTrace());
         throw ioe;
      }
      catch (SQLException e)
      {
         IOException ioe = new IOException("Error in result set: "+e.toString());
         ioe.setStackTrace(e.getStackTrace());
         throw ioe;
      }
      catch (ConverterException e)
      {
         IOException ioe = new IOException("Error in votable generator: "+e.toString());
         ioe.setStackTrace(e.getStackTrace());
         throw ioe;
      }
   }

   public InputStream getInputStream() throws IOException
   {
       ByteArrayOutputStream ba = new ByteArrayOutputStream();
       PrintStream ps = new PrintStream(ba);
       try {
        converter.serialize(results,ps);
        ps.close();
        ba.close();
        return new ByteArrayInputStream(ba.toByteArray());
       } catch (ConverterException e) {
           IOException ioe = new IOException("Unable to convert results to VOTable");
           ioe.initCause(e);
           throw ioe;
       } catch (SQLException e) {
           IOException ioe = new IOException("Error reading results from database");
           ioe.initCause(e);
           throw ioe;
       }
   }


}

