/*
 * @(#)Allocation.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.config.ConfigurationKeys;
import org.astrogrid.datacenter.job.Job;
import org.astrogrid.datacenter.myspace.Allocation;
import org.astrogrid.datacenter.myspace.AllocationException;
import org.astrogrid.i18n.AstroGridMessage;

/**
 * Implementation of the Allocation interface, that intefaces to storage space in the MySpace system

 */
public class AllocationImpl implements Allocation {

    private static final boolean TRACE_ENABLED = true;

    private static Logger logger = Logger.getLogger(AllocationImpl.class);

    public final static String SUBCOMPONENT_NAME = Util.getComponentName(AllocationImpl.class);

    private static String ASTROGRIDERROR_MYSPACEMANAGER_RETURNED_AN_ERROR ="AGDTCE00300",
        ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_MYSPACEMANAGER = "AGDTCE00310",
        ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE = "AGDTCE00320";

    private File filePath ;
    protected OutputStream outputStream ;
    // delegate for talking to mySpace.
    protected MySpaceHelper msDelegate = new MySpaceHelper();

    // Compression constants.
    public static final String GZIP_COMPRESSION = "gzip";

    public static final String ZIP_COMPRESSION = "zip";

    public static final String NO_COMPRESSION = "uncompressed";
        
    public AllocationImpl(File filePath, String compression) throws IOException {
        this.filePath = filePath;
        this.outputStream = createCompressedOutputStream(filePath,compression);
    }
   
/** depending on the compression code, create an uncompressed, gzipped or zipped output stream */
    protected final OutputStream createCompressedOutputStream(
        File outputfile, String compression)
        throws IOException, FileNotFoundException {

        OutputStream out =
            new BufferedOutputStream(new FileOutputStream(outputfile));

        if (compression.equalsIgnoreCase(AllocationImpl.GZIP_COMPRESSION)) {
            out = new GZIPOutputStream(out);
        } else if (compression.equalsIgnoreCase(AllocationImpl.ZIP_COMPRESSION)) {
            String internalfilename = "tmp.xml";

            int index = outputfile.getName().indexOf('.');
            if (index > -1) {
                internalfilename = outputfile.getName().substring(0, index) + ".xml";
            }
            out = new SingleEntryZipOutputStream(out,internalfilename);
        }
        return out;
    }


    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void close() {
        if (TRACE_ENABLED)  logger.debug("close(): entry");

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (java.io.IOException ex) {
                ;
            } finally {
                outputStream = null;
                if (TRACE_ENABLED)
                    logger.debug("close(): exit");
            }
        }
    } // end of close()

    /** tell mySpace about the data waiting to be uploaded,
     * and check for a correct reponse back.
     * 
     */
    public void informMySpace(Job job) throws AllocationException {
        String mySpaceResponse = null;

            String requestTemplate =  job.getConfiguration().getProperty(
                    ConfigurationKeys.MYSPACE_REQUEST_TEMPLATE,
                    ConfigurationKeys.MYSPACE_CATEGORY);
                    
            Object[] inserts = new Object[] {
                job.getUserId()
                , job.getCommunity()
                , job.getId()
                , filePath.getAbsolutePath()
            };
            String parms = MessageFormat.format(requestTemplate, inserts);
        try {            
            URL addr = new URL( job.getConfiguration().getProperty(
                                    ConfigurationKeys.MYSPACE_URL,
                                    ConfigurationKeys.MYSPACE_CATEGORY));              
            mySpaceResponse = msDelegate.doMySpaceCall(addr, parms);
        } catch (Exception af) {
            AstroGridMessage message =   new AstroGridMessage(
                    ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_MYSPACEMANAGER,
                    SUBCOMPONENT_NAME);
            logger.error(message.toString(), af);
            throw new AllocationException(message, af);
        } 
        
        logger.debug(mySpaceResponse);
        boolean succeeded = false;
        try {
            succeeded = msDelegate.checkResponse(mySpaceResponse);
        } catch (Exception pe) {
            AstroGridMessage message =  new AstroGridMessage(
                        ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE, SUBCOMPONENT_NAME);
            logger.error(message.toString(), pe);
            throw new AllocationException(message, pe);
        }
        if (!succeeded) {
            AstroGridMessage message =
                new AstroGridMessage(
                    ASTROGRIDERROR_MYSPACEMANAGER_RETURNED_AN_ERROR,  SUBCOMPONENT_NAME,
                    msDelegate.getStatus(),  msDelegate.getErrorDetails());
            logger.error(message.toString());
            throw new AllocationException(message);
        }
    } // end of diagnoseResponse()
    
    /**
     * extension of the standard zip output stream that simplifies zippiing of a single file (rather than an archive of files),
     * and ensures stream, and single entry within it, are closed correctly
     * @author Noel Winstanley nw@jb.man.ac.uk 22-Aug-2003
     *
     */
    public static class SingleEntryZipOutputStream extends ZipOutputStream {
        public SingleEntryZipOutputStream(OutputStream os,String internalFilename) throws IOException {
            super(os);
            putNextEntry(new ZipEntry(internalFilename));
        }
    
        /** Extended to close the single entry in the zip archive, before closing stream itself.
         */
        public void close() throws IOException {
            closeEntry();
            super.close();
        }

    }
} // end of class Allocation