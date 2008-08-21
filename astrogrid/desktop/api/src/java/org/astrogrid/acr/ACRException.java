/*$Id: ACRException.java,v 1.8 2008/08/21 11:33:50 nw Exp $
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

/** Base type for all exceptions thrown by the AR interface.
 * 
 * This type is rarely thrown - usualy more specific subtypes are thrown. However, it's convenient to catch all exceptions in one place
 * by matching against this type in a <tt>catch</tt> clause.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 26-Jul-2005
 *
 */
public class ACRException extends Exception {

    static final long serialVersionUID = 5881837879200706288L;

    /** Construct a new ACRException
     * 
     */
    public ACRException() {
        super();
    }

    /** Construct a new ACRException
     * @param message
     */
    public ACRException(final String message) {
        super(message);
    }

    /** Construct a new ACRException
     * @param cause - if cause is a standard exception (i.e. package is java.*), the embed it.
     * otherwise, it's probably a custom exception that isn't available on the client-rmi side - in which case it'll convert to standard {@link Exception}, whille still preserving the text
     */
    public ACRException(final Throwable cause) {
        
        super(convertNonStandard(cause));
    }

    /** Construct a new ACRException
     * @param message
   * @param cause - if cause is a standard exception (i.e. package is java.*), the embed it.
     * otherwise, it's probably a custom exception that isn't available on the client-rmi side - in which case it'll convert to standard {@link Exception}, whille still preserving the text
       
     */
    public ACRException(final String message, final Throwable cause) {
        super(message, convertNonStandard(cause));
        
    }
    
    /** convert client-unknown exceptions to standard exceptions */
    private static Throwable convertNonStandard(final Throwable t) {
        final Throwable newCause = t.getCause() == null ? null : convertNonStandard(t.getCause());
        Throwable newT = null;
        if (t.getClass().getName().startsWith("java.")) {
            if (newCause == t.getCause()) { // if newCause is unmodified, or null, we can return this e as-is.
                return t;
            } else { // need to return a new exception, of same class as t, because  cause has changed.                
                try {
                    final Constructor c = t.getClass().getConstructor(new Class[]{String.class,Exception.class}); // constructor that takes a message and a thowable
                    newT = (Throwable)c.newInstance(new Object[]{t.getMessage(),newCause});
                } catch (final Exception e) {
                    try { // fallback - just try to get an instance with the no-arg constructor.
                        newT = t.getClass().newInstance();
                        newT.initCause(newCause);
                    } catch (final Exception e1) {
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
Revision 1.8  2008/08/21 11:33:50  nw
doc tweaks.

Revision 1.7  2007/01/24 14:04:45  nw
updated my email address

Revision 1.6  2006/10/30 12:12:36  nw
documentation improvements.

Revision 1.5  2006/08/31 20:20:50  nw
doc fix.

Revision 1.4  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.3.6.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.3  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.2  2005/08/12 08:45:16  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/