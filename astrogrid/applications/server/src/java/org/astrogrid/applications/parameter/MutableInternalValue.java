/*
 * $Id: MutableInternalValue.java,v 1.2 2011/09/02 21:55:51 pah Exp $
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

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An internal value that can be changed.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 13 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public interface MutableInternalValue extends InternalValue {
    
      public void setValue(String val) throws ParameterStorageException;
      public void setValue(byte[] resultData) throws ParameterStorageException;
      public void setValue(InputStream is) throws ParameterStorageException;
}


/*
 * $Log: MutableInternalValue.java,v $
 * Revision 1.2  2011/09/02 21:55:51  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/15 09:49:36  pah
 * redesign of parameterAdapters
 *
 */
