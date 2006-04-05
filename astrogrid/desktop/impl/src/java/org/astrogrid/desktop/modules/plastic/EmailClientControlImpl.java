/*$Id$
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.plastic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;
import org.jdesktop.jdic.desktop.Message;

/** 
 * Control the system email client using jdic
 * @author John Taylor jdt@roe.ac.uk
 *
 */
public class EmailClientControlImpl implements EmailClientControlInternal  {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(EmailClientControlImpl.class);


    public EmailClientControlImpl() {}
    
    /* (non-Javadoc)
	 * @see org.astrogrid.desktop.modules.plastic.EmailClientControlI#send(org.jdesktop.jdic.desktop.Message)
	 */
    public void send(Message message) throws DesktopException {
    	logger.debug("Sending message "+message.getSubject());
    	Desktop.mail(message);
    }
}
