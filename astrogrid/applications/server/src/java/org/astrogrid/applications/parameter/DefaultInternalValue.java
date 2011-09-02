/*
 * $Id: DefaultInternalValue.java,v 1.2 2011/09/02 21:55:51 pah Exp $
 * 
 * Created on 13 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import org.astrogrid.applications.description.execution.BinaryEncodings;
import org.astrogrid.io.Piper;

/**
 * An internalValue that is really stored as a string. All methods make the assumption that the values are to be interpreted as strings.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 13 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class DefaultInternalValue implements MutableInternalValue {
    
    private String value = null;

    public DefaultInternalValue(String value2) {
       this.value = value2;
    }

    public DefaultInternalValue() {
       
    }

    public String asString() {
        return value;
    }

   public URL locationOf() {
        return null;
    }

    public long size() {
       return value.length();
    }

    public BinaryEncodings getStringEncoding() {
        return BinaryEncodings.NONE;
    }

    public void setValue(InputStreamReader ir) throws ParameterStorageException {
        StringWriter sw = null;
        try {
            sw = new StringWriter();
            Piper.pipe(ir, sw);
            value=sw.toString();
        } catch (IOException e) {
           logger.error("problem setting value", e);
           throw new ParameterStorageException("problem setting value", e);
        }
        finally{
            try {
                ir.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           if(sw!=null) {
               try {
                sw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           }
        }
    }

    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(DefaultInternalValue.class);

    public void writeToStream(OutputStream os) {
       PrintWriter pw = new PrintWriter(os);
       pw.print(value);
       pw.flush();
    }

    public void setValue(String string) {
       value = string;
    }

    public InputStream getStreamFrom() {
       return new ByteArrayInputStream(value.getBytes());
    }

    public void setValue(byte[] resultData) throws ParameterStorageException {
       this.setValue(new InputStreamReader(new ByteArrayInputStream(resultData)));
    }

    public void setValue(InputStream is) throws ParameterStorageException {
        this.setValue(new InputStreamReader(is));
    }

     
}


/*
 * $Log: DefaultInternalValue.java,v $
 * Revision 1.2  2011/09/02 21:55:51  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/15 09:49:36  pah
 * redesign of parameterAdapters
 *
 */
