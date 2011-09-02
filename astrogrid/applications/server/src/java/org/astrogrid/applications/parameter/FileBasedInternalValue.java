/*
 * $Id: FileBasedInternalValue.java,v 1.2 2011/09/02 21:55:51 pah Exp $
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.astrogrid.applications.description.execution.BinaryEncodings;
import org.astrogrid.io.Piper;

/**
 * A {@link MutableInternalValue} that stores the value in a file.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 14 Jul 2009
 * @version $Name:  $
 * @TODO need to get the string encoding working.
 * @since AIDA Stage 1
 */
public class FileBasedInternalValue implements MutableInternalValue {

    private File file;

    public FileBasedInternalValue(File storeLocation) {
        file = storeLocation;
    }

    public String asString() throws ParameterAdapterException {
       //FIXME - this needs to be done properly - needs knowledge of the type of the parameter. -
       // look at http://java.sun.com/products/javamail/javadocs/index.html?javax/mail/internet/MimeUtility.html
       try {
        BufferedReader fr = new BufferedReader(new FileReader(file));
        StringBuffer sb = new StringBuffer();
          
        String line;
        while ((line = fr.readLine())!= null) {
           sb.append(line);
        }
        return sb.toString();
    } catch (IOException e) {
       throw new ParameterAdapterException("cannot convert to string",e);
    }
       
    }

    public BinaryEncodings getStringEncoding() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "InternalValue.getStringEncoding() not implemented");
    }

 

    public URL locationOf() {
        try {
            return file.toURL();
        } catch (MalformedURLException e) {
            logger.fatal("file storage location bad", e);
            return null;
        }
    }

    public long size() {
        return file.length();
    }

    public void setValue(String value) throws ParameterStorageException {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(file));
            pw.println(value);
        } catch (IOException e) {
            logger.error("problem writing to file", e);
            throw new ParameterStorageException("problem writing to file", e);

        } finally {
            if (pw != null) {
                pw.close();
            }

        }

    }

    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(FileBasedInternalValue.class);

    public void setValue(InputStream is) throws ParameterStorageException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            Piper.bufferedPipe(is, os);
        } catch (IOException e) {
            logger.error("problem writing to file", e);
            throw new ParameterStorageException("problem writing to file", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("problem closing file", e);
                }
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    public void writeToStream(OutputStream os) throws IOException {
        FileInputStream is = new FileInputStream(file);
        Piper.bufferedPipe(is, os);
    }

    public InputStream getStreamFrom() {
        InputStream retval = null;

        try {
            retval = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("cannot get stream", e);
        }
        return retval;

    }

    public void setValue(byte[] resultData) throws ParameterStorageException {
       try {
        FileOutputStream fo = new FileOutputStream(file);
           fo.write(resultData);
    } catch (IOException e) {
       throw new ParameterStorageException("Cannot set the value " , e);
    }
    }

    public OutputStream getStreamTo() throws IOException {
        return new FileOutputStream(file);
    }
}

/*
 * $Log: FileBasedInternalValue.java,v $
 * Revision 1.2  2011/09/02 21:55:51  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.2  2009/07/16 19:48:05  pah
 * ASSIGNED - bug 2950: rework parameterAdapter
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950
 *
 * Revision 1.1.2.1  2009/07/15 09:49:36  pah
 * redesign of parameterAdapters
 *
 */
