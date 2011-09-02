/*
 * $Id: StreamBasedInternalValue.java,v 1.2 2011/09/02 21:55:51 pah Exp $
 * 
 * Created on 14 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.parameter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.execution.BinaryEncodings;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

/**
 * A {@link MutableInternalValue} that works with {@link InputStream}s. It avoids reading values until absolutely necessary.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 14 Jul 2009
 * @TODO this class is unfinished - perhaps not necessary either
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class StreamBasedInternalValue implements MutableInternalValue {

    private ParameterValue val;
    private ProtocolLibrary lib;
    private ApplicationEnvironment env;
    
    public StreamBasedInternalValue(ParameterValue val, ProtocolLibrary lib, ApplicationEnvironment env) {
        this.val = val;
        this.lib = lib;
        this.env = env;
    }
    public InputStream getStreamFrom() throws CeaException {
        if(val.isIndirect()){
            return lib.getExternalValue(val, env.getSecGuard()).read();
        }
        else {
            return new ByteArrayInputStream(val.getValue().getBytes());
        }
    }

    public void setValue(String val) throws ParameterStorageException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "StreamBasedInternalValue.setValue() not implemented");
    }

    public String asString() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "StreamBasedInternalValue.asString() not implemented");
    }

    public BinaryEncodings getStringEncoding() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "StreamBasedInternalValue.getStringEncoding() not implemented");
    }

    public URL locationOf() {
        if (val.isIndirect()) {
            try {
                return new URL(val.getValue());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        } else {
            
       throw new UnsupportedOperationException(
                "StreamBasedInternalValue.locationOf() not allowed for direct parameter");
        }
            }

    public long size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "StreamBasedInternalValue.size() not implemented");
    }

    public void writeToStream(OutputStream os) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "StreamBasedInternalValue.writeToStream() not implemented");
    }
    public OutputStream getStreamTo() throws ParameterAdapterException {
        if(val.isIndirect())
        {
            return lib.getExternalValue(val, env.getSecGuard()).write();
        }
        else {
        throw new  UnsupportedOperationException("MutableInternalValue.getStreamTo() not possible for direct values");
        }
    }
    public void setValue(byte[] resultData) throws ParameterStorageException {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("MutableInternalValue.setValue() not implemented");
    }
    public void setValue(InputStream is) throws ParameterStorageException {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("MutableInternalValue.setValue() not implemented");
    }

}


/*
 * $Log: StreamBasedInternalValue.java,v $
 * Revision 1.2  2011/09/02 21:55:51  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/15 09:49:36  pah
 * redesign of parameterAdapters
 *
 */
