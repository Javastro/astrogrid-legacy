/* $Id: MessageToAdminAction.java,v 1.3 2004/03/19 13:02:25 jdt Exp $
 * Created on Mar 16, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.portal.cocoon.common;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

/**
 * Send a message to the administrator 
 * @author jdt
 */
public class MessageToAdminAction extends AbstractAction {
    /**
     * Constructor
     * 
     */
    public MessageToAdminAction() {
        super();
        // TODO Auto-generated constructor stub
    }
    /* (non-Javadoc)
     * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
     */
    public Map act(
        Redirector arg0,
        SourceResolver arg1,
        Map arg2,
        String arg3,
        Parameters arg4)
        throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    public static void main(String[] args) {
    }
}


/*
 *  $Log: MessageToAdminAction.java,v $
 *  Revision 1.3  2004/03/19 13:02:25  jdt
 *  Pruned the log messages - they cause conflicts on merge, 
 *  best just to reduce them to the merge message.
 *
 *  Revision 1.2  2004/03/19 12:40:09  jdt
 *  Merge from PLGN_JDT_bz199b.
 *  Refactored log in pages to use xsp and xsl style sheets.  
 *  Added pages for requesting a login, and requesting
 *  a password reminder.
 *
 */