/*
 * @(#)JesException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes;


/** base exception type for the Job Execution System */
public class JesException extends Exception {

    public JesException(String s) {
        super(s);
    }
    
    public JesException(String s,Throwable e) {
        super(s,e);
    }
    
}
