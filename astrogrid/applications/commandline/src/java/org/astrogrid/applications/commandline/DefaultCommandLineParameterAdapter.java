/*
 * $Id: DefaultCommandLineParameterAdapter.java,v 1.1 2004/09/23 22:44:23 pah Exp $
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
import java.util.List;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDirection;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
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
public class DefaultCommandLineParameterAdapter implements CommandLineParameterAdapter {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(DefaultCommandLineParameterAdapter.class);

    protected boolean isOutputOnly;

    protected final ParameterValue pval;

    protected final CommandLineParameterDescription desc;

    protected final ExternalValue indirect;

    protected File referenceFile = null;

    protected final CommandLineApplicationEnvironment env;

    /** This is the raw value of the parameter that should be passed to the commandLine -i.e. after processing, but before having the commandline switches added. 
     */
    protected String commandLineVal;

    public DefaultCommandLineParameterAdapter(ApplicationInterface appInterface,
            ParameterValue pval, CommandLineParameterDescription desc,
            ExternalValue indirect, CommandLineApplicationEnvironment env) {
        this.pval = pval;
        this.desc = desc;
        this.indirect = indirect;
        this.env = env;
        this.referenceFile = env.getTempFile();
        try {
            this.isOutputOnly = appInterface.getParameterDirection(
                    pval.getName()).equals(ParameterDirection.OUTPUT);
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
        
        commandLineVal = pval.getValue();
        if (logger.isDebugEnabled()) {
            logger.debug("process() - start "+pval.getName()+"="+pval.getValue());
        }

        if (!isOutputOnly) {
            try {

                if (indirect == null) {
                    if (desc.isFileRef()) // but the commandline app expects a
                    // file
                    {

                        String value = pval.getValue();
                        PrintWriter pw = new PrintWriter(new FileWriter(
                                referenceFile));
                        pw.println(value);
                        pw.close();

                        if (logger.isDebugEnabled()) {
                            logger.debug("process() local copied - " + pval.getName() + "="
                                    + pval.getValue() + " direct, fileref="
                                    + referenceFile.getAbsolutePath());
                        }
                        commandLineVal = referenceFile.getAbsolutePath();

                    }
                    else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("process() - local copied - " + pval.getName() + "="
                                    + pval.getValue() + " direct");
                        }
                        commandLineVal = pval.getValue();

                    }
                }
                else {
                    if (desc.isFileRef()) { //a indirect param/ file ref cmdline 
                        InputStream is = indirect.read();
                        OutputStream os = new FileOutputStream(referenceFile);
                        Piper.bufferedPipe(is, os);
                        is.close();
                        os.close();
                        if (logger.isDebugEnabled()) {
                            logger.debug("process() - local copied - " + pval.getName() + "="
                                    + pval.getValue() + " indirect, fileref="
                                    + referenceFile.getAbsolutePath());
                        }
                        commandLineVal = referenceFile.getAbsolutePath();
                    }
                    else // an indirect param/ direct cmdline
                    {
                        //TODO - does this deal with new lines in the way that we might want?
                        InputStreamReader ir = new InputStreamReader(indirect
                                .read());
                        StringWriter sw = new StringWriter();
                        Piper.pipe(ir, sw);
                        ir.close();
                        sw.close();
                        pval.setValue(sw.toString()); //TODO do we really want to set this - or is just 
                        if (logger.isDebugEnabled()) {
                            logger.debug("process() - local copied - " + pval.getName() + "="
                                    + pval.getValue() + " indirect");
                        }

                        commandLineVal = pval.getValue();

                    }
                }
            }

            catch (IOException e) {
                throw new ParameterAdapterException(
                        "Could not process parameter " + pval.getName(), e);
            }
        }
        else {
            /* this is an output parameter - must write to a file*/
            commandLineVal = referenceFile.getAbsolutePath();
            if (logger.isDebugEnabled()) {
                logger.debug("process() - local copied - " + pval.getName() + "="
                        + pval.getValue() + " output fileref="+commandLineVal);
            }

            
        }
        return commandLineVal;
    }

    /**
     * ignore the value passed , copy contents of reference file back to the
     * original parameter (direct or indirect)
     * 
     * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
     */
    public void writeBack(Object arg0) throws CeaException {
        try {
            if (desc.isFileRef()) {
                if (indirect == null) {
                    StringWriter sw = new StringWriter();
                    Reader r = new FileReader(referenceFile);

                    Piper.pipe(r, sw);
                    r.close();
                    sw.close();
                    pval.setValue(sw.toString());
                    if (logger.isDebugEnabled()) {
                        logger.debug("writeBack() - direct/fileref "
                                + desc.getName() + "=" + pval.getValue()
                                + " from file "
                                + referenceFile.getAbsolutePath());
                    }
                }
                else {
                    InputStream is = new FileInputStream(referenceFile);
                    if (logger.isDebugEnabled()) {
                        logger.debug("writeBack() - indirect " + desc.getName()
                                + "="+pval.getValue()+" from file "
                                + referenceFile.getAbsolutePath());
                    }

                    OutputStream os = indirect.write();
                    Piper.bufferedPipe(is, os);
                    is.close();
                    os.close();
                }
            }
            else {
                //this is not possible in command-line as the app has to write output to a file. Someone could try to configure such a thing though.
                new ParameterWriteBackException(
                        "configuration error - attempting a writeback of non file-referenced parameter "
                                + desc.getName());
            }

        }
        catch (IOException e) {
            throw new ParameterWriteBackException(
                    "Could not write back parameter" + pval.getName(), e);
        }
    }

    /**
     * @see org.astrogrid.applications.parameter.ParameterAdapter#getWrappedParameter()
     */
    public ParameterValue getWrappedParameter() {
        return pval;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.applications.parameter.CommandLineParameterAdapter#addSwitches()
     */
    public List addSwitches() throws CeaException {
       return desc.addCmdlineAdornment(commandLineVal);
    }
    
    

}