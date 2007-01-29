/*$Id: MessagingInternal.java,v 1.4 2007/01/29 11:11:34 nw Exp $
 * Created on 21-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.util.Date;
import java.util.EventListener;

import org.astrogrid.acr.astrogrid.ExecutionMessage;


/** Internal interface to a messaging component.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Oct-2005
 *
 */
public interface MessagingInternal {
 
    /** inject a message into the system
     * */
    public void injectMessage(SourcedExecutionMessage m);
    /** add a consumer for messages
     * @param condition a filter for events. may be null to indicate (all events)
     * @param l a listener 
     */
    void addEventProcessor(MessageListener l); 
    /**
     * @param l
     */
    void removeEventProcessor(MessageListener l) ;
    
    public interface MessageListener extends EventListener {
        void onMessage(SourcedExecutionMessage m);
    }
    
    // wraps an execution message with details of it's source
    public class SourcedExecutionMessage {
        private final URI processId;
        private final String processName; 
        private final ExecutionMessage message;
        private final Date startTime;
        private final Date endTime;
        public URI getProcessId() {
            return this.processId;
        }
        
        public String getProcessName() {
            return this.processName;
        }      
        public ExecutionMessage getMessage() {
        	return this.message;
        }

        public Date getStartTime() {
            return this.startTime;
        }
        
        public Date getEndTime() {
            return this.endTime;
        }

		public SourcedExecutionMessage(final URI processId, final String processName, final ExecutionMessage message, final Date startTime, final Date endTime) {
			super();
			this.processId = processId;
			this.processName = processName;
			this.message = message;
			this.startTime = startTime;
			this.endTime = endTime;
		}        

    }
    
 
    

}


/* 
$Log: MessagingInternal.java,v $
Revision 1.4  2007/01/29 11:11:34  nw
updated contact details.

Revision 1.3  2006/08/02 13:28:28  nw
doc fix

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.34.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.34.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.1.34.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/