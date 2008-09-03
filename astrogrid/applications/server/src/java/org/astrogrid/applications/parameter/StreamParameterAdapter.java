/*
 * $Id: StreamParameterAdapter.java,v 1.5 2008/09/03 14:18:57 pah Exp $
 * 
 * Created on 09-Nov-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.parameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.io.Piper;

/**
 * A @link org.astrogrid.applications.parameter.ParameterAdapter that creates and consumes streams.
 * @todo @TODO test me.
 * @TODO should check that buffered input streams are being used....
 * @author Paul Harrison (pah@jb.man.ac.uk) 09-Nov-2004
 * @version $Name:  $
 * @since iteration6
 */
public class StreamParameterAdapter extends AbstractParameterAdapter {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(StreamParameterAdapter.class);

   /**
    * @param val
    * @param description
    * @param externalVal
    */
   public StreamParameterAdapter(ParameterValue val,
         ParameterDescription description, ExternalValue externalVal) {
      super(val, description, externalVal);

   }

   /*
    * (non-Javadoc)
    * 
    * @see org.astrogrid.applications.parameter.ParameterAdapter#process()
    */
   public Object process() throws CeaException {
      if (externalVal == null) {
         return new ByteArrayInputStream(val.getValue().getBytes());
      }
      else {
         return externalVal.read();
      }
   }

   /**
    * 
    * @param o this should be an @link InputStream.
    * 
    * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
    */
   public void writeBack(Object o) throws CeaException {
      OutputStream os = null;
      if (!(o instanceof InputStream)) {
         logger.error("programming error the method argument should be an InputStream");
      }

      if (externalVal == null) {
         os = new ByteArrayOutputStream();
      }
      else {
         os = externalVal.write();
      }
      try {
         InputStream is = null;
         
            try {
               is = (InputStream)o;
               Piper.pipe(is, os); // hope this doesn't close the os.
            }
            finally {
               if (is != null) {
                  try {
                     is.close();
                  }
                  catch (IOException e) {
                     logger.warn("failed to close input stream", e);
                  }
               }
            }
         
      }
      catch (IOException e) {
         throw new CeaException("Faled to write back", e);
      }
      finally {
         if (os != null) {
            try {
               os.close();
            }
            catch (IOException e) {
               logger.warn("failed to close output stream", e);
            }
         }
      }
      if (externalVal == null) {
         val.setValue(os.toString()); // uses byteArrayOutputStream's overloaded
                                      // toString().
      }
   }

}