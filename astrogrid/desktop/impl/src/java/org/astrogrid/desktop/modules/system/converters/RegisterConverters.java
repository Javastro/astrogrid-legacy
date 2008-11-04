/*$Id: RegisterConverters.java,v 1.7 2008/11/04 14:35:54 nw Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system.converters;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.astrogrid.desktop.modules.system.contributions.ConverterContribution;

/**
 * startup component that  registers all the string to object converters.
 * 
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 22-Mar-2005
 *  
 */
public class RegisterConverters implements Runnable {

    /**
     * Construct a new RegisterConverters
     *  @param converters a list of {@link ConverterContribution} objects 
     */
    public RegisterConverters(final List converters) {
        super();
        this.converters = converters;
    }
    
    private final List converters;


	public void run() {
		for (final Iterator i = converters.iterator(); i.hasNext() ;) {
			final ConverterContribution c = (ConverterContribution)i.next();
			//System.err.println(c.getOutput().getName());
				ConvertUtils.register(c.getConverter(),c.getOutput());
			
		}
	}



}

/*
 * $Log: RegisterConverters.java,v $
 * Revision 1.7  2008/11/04 14:35:54  nw
 * javadoc polishing
 *
 * Revision 1.6  2008/08/21 11:39:43  nw
 * Complete - task 4: RegistryGoogle dialogue
 *
 * Revision 1.5  2007/01/29 16:45:09  nw
 * cleaned up imports.
 *
 * Revision 1.4  2007/01/29 11:11:36  nw
 * updated contact details.
 *
 * Revision 1.3  2006/06/15 10:07:18  nw
 * improvements coming from unit testingadded new convertors.
 *
 * Revision 1.2  2006/04/18 23:25:46  nw
 * merged asr development.
 *
 * Revision 1.1.2.3  2006/04/14 02:45:01  nw
 * finished code.extruded plastic hub.
 *
 * Revision 1.1.2.2  2006/04/04 10:31:26  nw
 * preparing to move to mac.
 *
 * Revision 1.1.2.1  2006/03/22 18:01:31  nw
 * merges from head, and snapshot of development
 *
 * Revision 1.5  2005/12/01 16:11:08  jdt
 * added a general Collections converter and registered it for Lists.
 *
 * Revision 1.4  2005/11/29 11:28:05  nw
 * refactored converters
 *
 * Revision 1.3  2005/09/12 15:21:16  nw
 * reworked application launcher. starting on workflow builder
 *
 * Revision 1.2  2005/08/25 16:59:58  nw
 * 1.1-beta-3
 *
 * Revision 1.1  2005/08/11 10:15:00  nw
 * finished split
 *
 * Revision 1.6  2005/08/09 17:33:07  nw
 * finished system tests for ag components.
 *
 * Revision 1.5  2005/08/05 11:46:55  nw
 * reimplemented acr interfaces, added system tests.
 *
 * Revision 1.4  2005/05/12 15:59:12  clq2
 * nww 1111 again
 *
 * Revision 1.2.20.1  2005/05/11 14:25:24  nw
 * javadoc, improved result transformers for xml
 *
 * Revision 1.2  2005/04/13 12:59:11  nw
 * checkin from branch desktop-nww-998
 *
 * Revision 1.1.2.2  2005/03/30 12:48:22  nw
 * added two more lttle modules
 *
 * Revision 1.1.2.1  2005/03/22 12:04:03  nw
 * working draft of system and ag components.
 *
 */