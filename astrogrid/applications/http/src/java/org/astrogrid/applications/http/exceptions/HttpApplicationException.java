/* $Id: HttpApplicationException.java,v 1.4 2004/09/01 15:42:26 jdt Exp $
 * Created on Jul 27, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
///CLOVER:OFF 
package org.astrogrid.applications.http.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A superinterface, just in case you want to just catch HttpApplicationExceptions
 * It mirrors all the methods in Throwable.
 * @author jdt
 */
public interface HttpApplicationException  {
    /* (non-Javadoc)
     * @see java.lang.Throwable#fillInStackTrace()
     */
    public   Throwable fillInStackTrace();
    /* (non-Javadoc)
     * @see java.lang.Throwable#getCause()
     */
    public Throwable getCause() ;
    /* (non-Javadoc)
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() ;
    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() ;
    /* (non-Javadoc)
     * @see java.lang.Throwable#getStackTrace()
     */
    public StackTraceElement[] getStackTrace();
    /* (non-Javadoc)
     * @see java.lang.Throwable#initCause(java.lang.Throwable)
     */
    public  Throwable initCause(Throwable arg0) ;
    /* (non-Javadoc)
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace() ;
    /* (non-Javadoc)
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public void printStackTrace(PrintStream arg0) ;
    /* (non-Javadoc)
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(PrintWriter arg0) ;
    /* (non-Javadoc)
     * @see java.lang.Throwable#setStackTrace(java.lang.StackTraceElement[])
     */
    public void setStackTrace(StackTraceElement[] arg0) ;
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() ;

}

/* 
 * $Log: HttpApplicationException.java,v $
 * Revision 1.4  2004/09/01 15:42:26  jdt
 * Merged in Case 3
 *
 * Revision 1.1.4.1  2004/07/27 17:20:25  jdt
 * merged from applications_JDT_case3
 *
 * Revision 1.1.2.3  2004/07/27 17:12:44  jdt
 * refactored exceptions and finished tests for HttpServiceClient
 *
 */