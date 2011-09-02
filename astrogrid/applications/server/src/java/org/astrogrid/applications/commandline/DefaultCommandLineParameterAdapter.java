/*
 * $Id: DefaultCommandLineParameterAdapter.java,v 1.4 2011/09/02 21:55:52 pah Exp $
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.impl.CommandLineParameterDefinition;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.AbstractParameterAdapter;
import org.astrogrid.applications.parameter.DefaultInternalValue;
import org.astrogrid.applications.parameter.FileBasedInternalValue;
import org.astrogrid.applications.parameter.MutableInternalValue;
import org.astrogrid.applications.parameter.ParameterAdapterException;
import org.astrogrid.applications.parameter.ParameterDirection;
import org.astrogrid.applications.parameter.ParameterWriteBackException;
import org.astrogrid.applications.parameter.protocol.ExternalValue;

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


   /**
    * This is the raw value of the parameter that should be passed to the
    * commandLine -i.e. after processing, but before having the commandline
    * switches added.
    */
   protected String commandLineVal;
   
 
   protected final org.astrogrid.applications.description.impl.CommandLineParameterDefinition cmdParamDesc;

   public DefaultCommandLineParameterAdapter(ApplicationInterface appInterface,
         ParameterValue pval, CommandLineParameterDefinition desc, ParameterDirection dir,
          ApplicationEnvironment env) {
      super(pval, desc, dir, env);
      this.cmdParamDesc = desc;
      if (cmdParamDesc.getLocalFileName() == null) {
         this.referenceFile = env.getTempFile(desc.getId());
      }
      else // use the file name that the application generates
      {
         this.referenceFile = new File(env.getExecutionDirectory(),
               cmdParamDesc.getLocalFileName());
      }
      this.isOutputOnly = direction
           .equals(ParameterDirection.OUTPUT);
   }

   public File getReferenceFile() {
      return referenceFile;
   }

   /**
    * copies the parameter value (either direct, or indirect) into a file (or
    * onto the command line) in the commandline environment
    * 
    * @return the name of the file the value has been written to.
    * @see org.astrogrid.applications.parameter.ParameterAdapter#getInternalValue()
    */
   @Override
public MutableInternalValue getInternalValue() throws CeaException {

       
      if (internalVal == null) { //if the internal value has not already been retrieved.
        commandLineVal = val.getValue();
        if (logger.isDebugEnabled()) {
            logger.debug("process() - start " + val.getId() + "="
                    + val.getValue());
        }
        if (!isOutputOnly) {
            InputStreamReader ir = null;
            InputStream is = null;
            try {

                if (!val.isIndirect()) {
                    if (cmdParamDesc.isFileRef()) // but the commandline app expects
                    // a
                    // file
                    {
                        FileBasedInternalValue localval = new FileBasedInternalValue(
                                referenceFile);
                        internalVal = localval;
                        localval.setValue(commandLineVal);

                        if (logger.isDebugEnabled()) {
                            logger.debug("process() local copied - "
                                    + val.getId() + "=" + val.getValue()
                                    + " direct, fileref="
                                    + referenceFile.getName());
                        }
                        commandLineVal = referenceFile.getName();

                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("process() - local copied - "
                                    + val.getId() + "=" + val.getValue()
                                    + " direct");
                        }
                        commandLineVal = val.getValue();

                    }
                } else {
                    ExternalValue externalVal = getProtocolLib()
                            .getExternalValue(val, env.getSecGuard());
                    if (cmdParamDesc.isFileRef()) { // a externalVal param/ file ref
                        // cmdline

                        FileBasedInternalValue localval = new FileBasedInternalValue(
                                referenceFile);
                        internalVal = localval;
                        is = externalVal.read();
                        localval.setValue(is);

                        if (logger.isDebugEnabled()) {
                            logger.debug("process() - local copied - "
                                    + val.getId() + "=" + val.getValue()
                                    + " externalVal, fileref="
                                    + referenceFile.getName());
                        }
                        commandLineVal = referenceFile.getName();
                    } else // an externalVal param/ direct cmdline
                    {
                        // TODO - does this deal with new lines in the way that we
                        // might want?

                        DefaultInternalValue localval = new DefaultInternalValue();
                        internalVal = localval;
                        ir = new InputStreamReader(externalVal.read());
                        localval.setValue(ir);

                        val.setValue(internalVal.asString()); // TODO do we really want to set
                        // this - or is just
                        if (logger.isDebugEnabled()) {
                            logger.debug("process() - local copied - "
                                    + val.getId() + "=" + val.getValue()
                                    + " externalVal");
                        }

                        commandLineVal = val.getValue();

                    }
                }

                // special treatment for booleans
                if (cmdParamDesc.getType() == ParameterTypes.BOOLEAN) {
                    commandLineVal = BooleanNormalizer.normalize(
                            commandLineVal, cmdParamDesc.getDefaultValue().get(
                                    0));
                }
            }

            catch (Exception e) {
                throw new ParameterAdapterException(
                        "Could not process parameter " + val.getId(), e);
            }
        } else {
            /* this is an output parameter - must write to a file */
            commandLineVal = referenceFile.getName();
            internalVal = new FileBasedInternalValue(referenceFile);
            if (logger.isDebugEnabled()) {
                logger.debug("process() - local copied - " + val.getId() + "="
                        + val.getValue() + " output fileref=" + commandLineVal);
            }

        }
    }
    return internalVal;
   }

   /**
    * ignore the value passed , copy contents of reference file back to the
    * original parameter (direct or externalVal)
    * 
    * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
    */
   @Override
public void writeBack() throws CeaException {
       
      
      OutputStream os = null;
      try {
         if (cmdParamDesc.isFileRef()) {
            if (!val.isIndirect()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("writeBack() - direct/fileref "
                          + cmdParamDesc.getId() + "=" + val.getValue()
                          + " from file " + referenceFile.getAbsolutePath());
                 }
                 val.setValue(internalVal.asString());
             }
            else {
                if (logger.isDebugEnabled()) {
                  logger.debug("writeBack() - externalVal "
                        + cmdParamDesc.getId() + "=" + val.getValue()
                        + " from file " + referenceFile.getAbsolutePath());
               }
               os = getProtocolLib().getExternalValue(val, env.getSecGuard()).write(); 
               internalVal.writeToStream(os);
            }
         }
         else {
            // this is not possible in command-line as the app has to write
            // output to a file. Someone could try to configure such a thing
            // though.
            throw new ParameterWriteBackException(
                  "configuration error - attempting a writeback of non file-referenced parameter "
                        + cmdParamDesc.getId());
         }

      }
      catch (IOException e) {
         throw new ParameterWriteBackException("Could not write back parameter"
               + val.getId(), e);
      }
      finally {
          if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
