/*
 * $Id: InternalValue.java,v 1.2 2011/09/02 21:55:51 pah Exp $
 * 
 * Created on 11 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.execution.BinaryEncodings;

/**
 * How to access the internal value of a parameter.
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 11 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public interface InternalValue {

    /**
     * Return the internal value as a string. If the internal value is actually
     * binary in nature then the value should be text encoded in some fashion.
     * 
     * @return
     * @throws ParameterAdapterException 
     */
    public String asString() throws ParameterAdapterException;

    /**
     * Return the type of the binary encoding.
     * 
     * @return
     */
    public BinaryEncodings getStringEncoding();

    /**
     * Return the location where the internal value is stored.
     * 
     * @return null if the value is not stored somewhere, but is just in memory.
     */
    public URL locationOf();

    /**
     * The size of the value in bytes.
     * 
     * @return
     */
    public long size();

    /**
     * Write the value to the output stream.
     * 
     * @param os
     *            the outputstream to write the value to. Note that the stream
     *            is not closed.
     * @throws IOException
     */
    public void writeToStream(OutputStream os) throws IOException;
    
    
    /**
     * Get a stream to read the internal value;
     * @TODO do we really want to support this in the interface - not possible for String based internal values.
     * @return
     * @throws CeaException
     */
    public InputStream getStreamFrom() throws CeaException;


}

/*
 * $Log: InternalValue.java,v $
 * Revision 1.2  2011/09/02 21:55:51  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/15 09:49:36  pah
 * redesign of parameterAdapters
 *
 */
