/*
 * @(#)MySpaceFactoryImpl.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 * 
 */
package org.astrogrid.datacenter.impl;


import java.io.File;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.config.ConfigurableImpl;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.config.ConfigurationKeys;
import org.astrogrid.datacenter.myspace.Allocation;
import org.astrogrid.datacenter.myspace.AllocationException;
import org.astrogrid.datacenter.myspace.MySpaceFactory;
import org.astrogrid.i18n.AstroGridMessage;

/** implementation of MySpaceFactory 
 * each allocation is a file in a specified myspace cache directory. 
 */
public class MySpaceFactoryImpl
    extends ConfigurableImpl
    implements MySpaceFactory {

    private static final boolean TRACE_ENABLED = true;

    private static Logger logger = Logger.getLogger(MySpaceFactoryImpl.class);

    private final static String SUBCOMPONENT_NAME =  Util.getComponentName(MySpaceFactoryImpl.class);

    private static String ASTROGRIDERROR_COULD_NOT_CREATE_ALLOCATION =   "AGDTCE00100";
 
    /** directory to place query results in, to be accesed by myspace */
    private File baseDir;

    public Allocation allocateCacheSpace(String jobID)   throws AllocationException {
        if (TRACE_ENABLED) logger.debug("allocateCacheSpace(): entry");
        try {
            File fileName = produceFile(jobID);
            return new AllocationImpl(fileName, AllocationImpl.NO_COMPRESSION);

        } catch (Exception ex) {
            AstroGridMessage message =     new AstroGridMessage(
                    ASTROGRIDERROR_COULD_NOT_CREATE_ALLOCATION,
                    SUBCOMPONENT_NAME, jobID);
            logger.error(message.toString(), ex);
            throw new AllocationException(message, ex);
        } finally {
            if (TRACE_ENABLED)
                logger.debug("allocateCacheSpace(): exit");
        }
    } // end of allocateCacheSpace()



    /** @deprecated - use produceFile instead. Just kept around to compare how things were done -
     * dunno if there's any MySpace specific bits here..
     * @param jobID
     * @return
     */
    private String produceFileName(String jobID) {

        StringBuffer buffer = new StringBuffer(64);

        buffer
        //JBL Note: .append( "file://" )  // JBL Note: this is a quick fix for AstroGrid iteration 2
        .append(
            getConfiguration().getProperty(
                ConfigurationKeys.MYSPACE_CACHE_DIRECTORY,
                ConfigurationKeys.MYSPACE_CATEGORY))
        .append(System.getProperty("file.separator"))
        .append(jobID.replace(':', '.'))
        .append(".xml");

        return buffer.toString();

    } // end of produceFileName()
    
    protected File produceFile(String jobID) {
        String filename = jobID.replace(':','.') + ".xml";
        return new File(baseDir,filename);
    }

    /** Extended to initialize value of {@link #baseDir}
     * 
     * @see org.astrogrid.datacenter.config.Configurable#setConfiguration(org.astrogrid.datacenter.config.Configuration)
     */
    public void setConfiguration(Configuration conf) {
        super.setConfiguration(conf);
        // initialize our own fields here.
        baseDir = new File (getConfiguration().getProperty(
                    ConfigurationKeys.MYSPACE_CACHE_DIRECTORY,ConfigurationKeys.MYSPACE_CATEGORY));

        // should check whetehr this exists, and is writable.
        if ( ! ( baseDir.exists() && baseDir.isDirectory() && baseDir.canRead()) ) {
            throw new IllegalArgumentException("MYSPACE cache Directory does not exist");
        }                    
    }
    
 

} // end of class MySpaceFactoryImpl
