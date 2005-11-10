/*$Id: MessageUtils.java,v 1.1 2005/11/10 12:05:43 nw Exp $
 * Created on 07-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

/** Constants and static methods to simplify working with messages
 * 
 * - attempts to keep producers and consumers of mesages in-synch, by factoring out common definitions.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Nov-2005
 *
 */
public class MessageUtils {



    /*
     * notification
     * id - unqique id for this task
     * name - human readable descripiton of this task.
     * jid - caller-assigned id for this task.
     * type - status-change | information | results
     * all are text messages. status-change just contains new status, message and results
     * are serialized documents - got the castor classes to do this somewhere.
     */
    public static final String PROCESS_NAME_PROPERTY = "process_name";
    public static final String PROCESS_ID_PROPERTY = "process_id";
    public static final String CLIENT_ASSIGNED_ID_PROPERTY = "client_assigned_id";
    public static final String MESSAGE_TYPE_PROPERTY = "message_type";

    
    /** also possible that these properties may be present */
    public  static final String START_TIME_PROPERTY = "start_time";
    public static final String END_TIME_PROPERTY =  "end_time";
    
    /*
     * alerts - have two mandatory properties - type and summary
     * type is 'warning', 'urgent', 'information
     * summary is human-readable text.
     * 
     * body of alert is an xml 'message' object - whose content may contain html.
     * 
     */
    
    public static final String SUMMARY_PROPERTY = "summary"  ;  


    /** message indicating the sender has changed status
     */
    public static final String STATUS_CHANGE_MESSAGE = "Status Change";        
    /** message containing results of a computation*/
    public static final String RESULTS_MESSAGE = "Results";      
    public static final String INFORMATION_MESSAGE = "Information";
    
    private MessageUtils() {
        super();
    }

}


/* 
$Log: MessageUtils.java,v $
Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/