/* $Id: FileUnmarshaller.java,v 1.2 2004/09/01 15:42:26 jdt Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 * Created on Aug 11, 2004
 */
package org.astrogrid.applications.http.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Utility class to unmarshal a castor object from a filename relative to this class
 * @author jdt
 */
public class FileUnmarshaller {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(FileUnmarshaller.class);

    /**
     * The class to be unmarshalled
     */
    private Class clazz;
    /**
     * Ctor
     * @param clazz the class to be unmarshalled
     */
    public FileUnmarshaller(final Class clazz) {
        if (log.isTraceEnabled()) {
            log.trace("FileUnmarshaller(Class clazz = " + clazz + ") - start");
        }

        this.clazz = clazz;

        if (log.isTraceEnabled()) {
            log.trace("FileUnmarshaller(Class) - end");
        }
    }
    /**
     * Unmarshalls a given class from an xml file
     * @param file filename relative to the location of this class
     * @return an object that you can cast to your Class clazz
     * @throws MarshalException see castor
     * @throws ValidationException see castor
     */
    public Object unmarshallFromFile(final String file) throws MarshalException, ValidationException {
        if (log.isTraceEnabled()) {
            log.trace("unmarshallFromFile(String file = " + file + ") - start");
        }

        InputStream is = this.getClass().getResourceAsStream(file);
        Object testApplication = Unmarshaller.unmarshal(clazz, new BufferedReader(new InputStreamReader(is)));
       
        try {
            is.close();
        } catch (IOException e) {
            log.debug("unmarshallFromFile(String): failed to close source file, but it doesn't really matter", e);
        }

        if (log.isTraceEnabled()) {
            log.trace("unmarshallFromFile(String) - end - return value = " + testApplication);
        }
        return testApplication;
    }
}
