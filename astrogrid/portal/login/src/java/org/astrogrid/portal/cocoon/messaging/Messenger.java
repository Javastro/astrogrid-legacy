/* $Id: Messenger.java,v 1.2 2004/03/19 12:40:09 jdt Exp $
 * Created on Mar 14, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.portal.cocoon.messaging;

/**
 * Generic messenger interface to send emails or whatever 
 * @author jdt
 */
public interface Messenger {
    /**
     * All messengers, whether email, jabber, whatever simply send a text message.
     * @param subject subject of message
     * @param message message body
     * @throws MessengerException if there's a wee problem
     */
    void sendMessage(String subject, String message) throws MessengerException;
}


/*
 *  $Log: Messenger.java,v $
 *  Revision 1.2  2004/03/19 12:40:09  jdt
 *  Merge from PLGN_JDT_bz199b.
 *  Refactored log in pages to use xsp and xsl style sheets.  
 *  Added pages for requesting a login, and requesting
 *  a password reminder.
 *
 *  Revision 1.1.2.1  2004/03/16 10:50:33  jdt
 *  Added email messenging classes
 *
 *
 */