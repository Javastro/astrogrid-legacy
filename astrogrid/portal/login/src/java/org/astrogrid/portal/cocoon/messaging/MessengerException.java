/* $Id: MessengerException.java,v 1.2 2004/03/19 12:40:09 jdt Exp $
 * Created on Mar 14, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.portal.cocoon.messaging;

/**
 * Exception that might be thrown by a Messenger
 * 
 * @author jdt
 */
public class MessengerException extends Exception {
    /**
     * Constructor
     * 
     */
    public MessengerException() {
        super();
    }
    /**
     * Constructor
     * @param arg0 description
     */
    public MessengerException(final String arg0) {
        super(arg0);
    }
    /**
     * Constructor
     * @param arg0 cause
     */
    public MessengerException(final Throwable arg0) {
        super(arg0);
    }
    /**
     * Constructor
     * @param arg0 description
     * @param arg1 cause
     */
    public MessengerException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }
}


/*
 *  $Log: MessengerException.java,v $
 *  Revision 1.2  2004/03/19 12:40:09  jdt
 *  Merge from PLGN_JDT_bz199b.
 *  Refactored log in pages to use xsp and xsl style sheets.  
 *  Added pages for requesting a login, and requesting
 *  a password reminder.
 *
 *  Revision 1.1.2.1  2004/03/16 10:50:33  jdt
 *  Added email messenging classes
 *
 */