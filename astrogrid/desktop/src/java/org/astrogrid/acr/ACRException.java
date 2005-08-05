/*$Id: ACRException.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 26-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

import java.lang.reflect.Constructor;

/** base exception type for all acr exceptions.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2005
 *
 */
public class ACRException extends Exception {

    /** Construct a new ACRException
     * 
     */
    public ACRException() {
        super();
    }

    /** Construct a new ACRException
     * @param message
     */
    public ACRException(String message) {
        super(message);
    }

    /** Construct a new ACRException
     * @param cause - if cause is a standard exception (i.e. package is java.*), the embed it.
     * otherwise, it's probably a custom exception that isn't available on the client side - in which case convert to standard exception.
     */
    public ACRException(Throwable cause) {
        
        super(convertNonStandard(cause));
    }

    /** Construct a new ACRException
     * @param message
     * @param cause
     */
    public ACRException(String message, Throwable cause) {
        super(message, convertNonStandard(cause));
        
    }
    
    /** convert client-unknown exceptions to standard exceptions */
    private static Throwable convertNonStandard(Throwable t) {
        Throwable newCause = t.getCause() == null ? null : convertNonStandard(t.getCause());
        Throwable newT = null;
        if (t.getClass().getName().startsWith("java.")) {
            if (newCause == t.getCause()) { // if newCause is unmodified, or null, we can return this e as-is.
                return t;
            } else { // need to return a new exception, of same class as t, because  cause has changed.                
                try {
                    Constructor c = t.getClass().getConstructor(new Class[]{String.class,Exception.class}); // constructor that takes a message and a thowable
                    newT = (Throwable)c.newInstance(new Object[]{t.getMessage(),newCause});
                } catch (Exception e) {
                    try { // fallback - just try to get an instance with the no-arg constructor.
                        newT = (Throwable)t.getClass().newInstance();
                        newT.initCause(newCause);
                    } catch (Exception e1) {
                        //rats - just return t as is, and hope it works out.
                        return t;
                    }
                }                 
            }
        } else { // not a standard exception
            newT = new Exception(t.getClass().getName() + " " + t.getMessage(),newCause);
        }
        newT.setStackTrace(t.getStackTrace());
        return newT;
    }
    
    
    

}


/* 
$Log: ACRException.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/