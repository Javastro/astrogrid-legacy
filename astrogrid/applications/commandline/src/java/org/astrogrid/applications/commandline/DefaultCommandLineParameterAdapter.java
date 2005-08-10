/*
 * $Id: DefaultCommandLineParameterAdapter.java,v 1.5 2005/08/10 14:45:37 clq2 Exp $
 * 
 * Created on 20-Aug-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.commandline;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDirection;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.parameter.AbstractParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapterException;
import org.astrogrid.applications.parameter.ParameterWriteBackException;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.io.Piper;

/**
 * Does the specialized job for the CommandLine parameters. The Main differences
 * from the
 * 
 * @link org.astrogrid.applications.parameter.DefaultParameterAdapter are
 *       <ul>
 *       <li>need to be able to deal with the extra possible "indirection"
 *       level that the command line parameter can appear as a file for the
 *       command line application, even when presented as a direct parameter to
 *       the
 * @link org.astrogrid.applications.service.v1.cea.CommonExecutionConnector
 *       interface.
 *       <li>needs to be able to activate the "command line decoration" -
 *       switches etc.
 *       </ul>
 * @author Paul Harrison (pah@jb.man.ac.uk) 20-Aug-2004
 * @version $Name:  $
 * @since iteration6
 */
public class DefaultCommandLineParameterAdapter extends
      AbstractParameterAdapter implements CommandLineParameterAdapter {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(DefaultCommandLineParameterAdapter.class);

   protected boolean isOutputOnly;

   protected File referenceFile = null;

   protected final CommandLineApplicationEnvironment env;

   /**
    * This is the raw value of the parameter that should be passed to the
    * commandLine -i.e. after processing, but before having the commandline
    * switches added.
    */
   protected String commandLineVal;

   protected final CommandLineParameterDescription cmdParamDesc;

   public DefaultCommandLineParameterAdapter(ApplicationInterface appInterface,
         ParameterValue pval, CommandLineParameterDescription desc,
         ExternalValue indirect, CommandLineApplicationEnvironment env) {
      super(pval, desc, indirect);
      this.cmdParamDesc = desc;
      this.env = env;
      if (cmdParamDesc.getLocalFileName() == null) {
         this.referenceFile = env.getTempFile(desc.getName());
      }
      else // use the file name that the application generates
      {
         this.referenceFile = new File(env.getExecutionDirectory(),
               cmdParamDesc.getLocalFileName());
      }
      try {
         this.isOutputOnly = appInterface.getParameterDirection(val.getName())
               .equals(ParameterDirection.OUTPUT);
      }
      catch (ParameterNotInInterfaceException e) {
         // very unlikely.
         this.isOutputOnly = false;
      }
   }

   public File getReferenceFile() {
      return referenceFile;
   }

   /**
    * copies the parameter value (either direct, or indirect) into a file (or
    * onto the command line) in the commandline environment
    * 
    * @return the name of the file the value has been written to.
    * @see org.astrogrid.applications.parameter.ParameterAdapter#process()
    */
   public Object process() throws CeaException {

      commandLineVal = val.getValue();
      if (logger.isDebugEnabled()) {
         logger.debug("process() - start " + val.getName() + "="
               + val.getValue());
      }

      if (!isOutputOnly) {
         InputStreamReader ir = null;
         PrintWriter pw = null;
         InputStream is = null;
         OutputStream os = null;
         try {

            if (externalVal == null) {
               if (cmdParamDesc.isFileRef()) // but the commandline app expects
               // a
               // file
               {

                  String value = val.getValue();
                  pw = new PrintWriter(new FileWriter(referenceFile));
                  pw.println(value);
                  pw.close();

                  if (logger.isDebugEnabled()) {
                     logger.debug("process() local copied - " + val.getName()
                           + "=" + val.getValue() + " direct, fileref="
                           + referenceFile.getName());
                  }
                  commandLineVal = referenceFile.getName();

               }
               else {
                  if (logger.isDebugEnabled()) {
                     logger.debug("process() - local copied - " + val.getName()
                           + "=" + val.getValue() + " direct");
                  }
                  commandLineVal = val.getValue();

               }
            }
            else {
               if (cmdParamDesc.isFileRef()) { // a externalVal param/ file ref
                  // cmdline
                  is = externalVal.read();
                  os = new FileOutputStream(referenceFile);
                  Piper.bufferedPipe(is, os);
                  is.close();
                  os.close();
                  if (logger.isDebugEnabled()) {
                     logger.debug("process() - local copied - " + val.getName()
                           + "=" + val.getValue() + " externalVal, fileref="
                           + referenceFile.getName());
                  }
                  commandLineVal = referenceFile.getName();
               }
               else // an externalVal param/ direct cmdline
               {
                  // TODO - does this deal with new lines in the way that we
                  // might want?
                  ir = new InputStreamReader(externalVal.read());
                  StringWriter sw = new StringWriter();
                  Piper.pipe(ir, sw);
                  ir.close();
                  sw.close();
                  val.setValue(sw.toString()); // TODO do we really want to set
                  // this - or is just
                  if (logger.isDebugEnabled()) {
                     logger.debug("process() - local copied - " + val.getName()
                           + "=" + val.getValue() + " externalVal");
                  }

                  commandLineVal = val.getValue();

               }
            }
         }

         catch (IOException e) {
            throw new ParameterAdapterException("Could not process parameter "
                  + val.getName(), e);
         }
         finally // close all open resources
         {
            closeIO(pw, ir, is, os);
         }
      }
      else {
         /* this is an output parameter - must write to a file */
         commandLineVal = referenceFile.getName();
         if (logger.isDebugEnabled()) {
            logger.debug("process() - local copied - " + val.getName() + "="
                  + val.getValue() + " output fileref=" + commandLineVal);
         }

      }
      return commandLineVal;
   }

   /**
    * ignore the value passed , copy contents of reference file back to the
    * original parameter (direct or externalVal)
    * 
    * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
    */
   public void writeBack(Object arg0) throws CeaException {
      StringWriter sw = null;
      Reader r = null;
      InputStream is = null;
      OutputStream os = null;
      assert arg0 == null : "the writeback parameter is ignored in DefaultCommandLineParameterAdapter - it should be null";
      try {
         if (cmdParamDesc.isFileRef()) {
            if (externalVal == null) {
               sw = new StringWriter();
               r = new FileReader(referenceFile);

               Piper.pipe(r, sw);
               r.close();
               sw.close();
               val.setValue(sw.toString());
               if (logger.isDebugEnabled()) {
                  logger.debug("writeBack() - direct/fileref "
                        + cmdParamDesc.getName() + "=" + val.getValue()
                        + " from file " + referenceFile.getAbsolutePath());
               }
            }
            else {
               is = new FileInputStream(referenceFile);
               if (logger.isDebugEnabled()) {
                  logger.debug("writeBack() - externalVal "
                        + cmdParamDesc.getName() + "=" + val.getValue()
                        + " from file " + referenceFile.getAbsolutePath());
               }

               os = externalVal.write();
               Piper.bufferedPipe(is, os);
               is.close();
               os.close();
            }
         }
         else {
            // this is not possible in command-line as the app has to write
            // output to a file. Someone could try to configure such a thing
            // though.
            new ParameterWriteBackException(
                  "configuration error - attempting a writeback of non file-referenced parameter "
                        + cmdParamDesc.getName());
         }

      }
      catch (IOException e) {
         throw new ParameterWriteBackException("Could not write back parameter"
               + val.getName(), e);
      }
      finally // ensure that all the streams have been closed
      {
         closeIO(sw, r, is, os);

      }
   }

   /**
    * Close any streams and readers/writers that might have been left open.
    * 
    * @param sw
    * @param r
    * @param is
    * @param os
    */
   private void closeIO(Writer sw, Reader r, InputStream is, OutputStream os) {
      if (r != null) {
         try {
            r.close();
         }
         catch (IOException e1) {
            logger.warn("cannot close reader", e1);
         }
      }
      if (sw != null) {
         try {
            sw.close();
         }
         catch (IOException e1) {
            logger.warn("could not close string writer", e1);
         }
      }
      if (is != null) {
         try {
            is.close();
         }
         catch (IOException e1) {
            logger.warn("could not close input stream", e1);
         }

      }
      if (os != null) {
         try {
            os.close();
         }
         catch (IOException e1) {
            logger.warn("could not close output stream", e1);
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.astrogrid.applications.parameter.CommandLineParameterAdapter#addSwitches()
    */
   public List addSwitches() throws CeaException {
      return cmdParamDesc.addCmdlineAdornment(commandLineVal);
   }

}
