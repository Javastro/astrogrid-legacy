/*$Id: AbstractProgressListener.java,v 1.3 2008/09/03 14:18:58 pah Exp $
 * Created on 19-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.observer;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.description.execution.MessageType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Observable;
import java.util.Observer;

/** An abstract base class for things that want to listen to the progress of an application execution.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Jul-2004
 *
 */
public abstract class AbstractProgressListener implements Observer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AbstractProgressListener.class);

    /** Construct a new AbstractProgressListener
     * 
     */
    public AbstractProgressListener() {
        super();
    }
    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public final void update(Observable o, Object arg) {
        Application app = (Application)o;
        if (arg instanceof Status) {
            reportStatusChange(app,(Status)arg);
        } else if (arg instanceof MessageType) {
            reportMessage(app,(MessageType)arg);
            
        } else {
            logger.warn("Unknown object in update notification " + arg.getClass().getName() + " " + arg.toString());
        }
    }
    /** subclasses to implement this.
     * called when the listener encounters a message from the application.
     * @param app the applicaitoni emitting the mesage
     * @param type the message the application emitted - usually  an informative message
     */
    protected abstract void reportMessage(Application app, MessageType type);    
    /** subclasses to implement this.
     * called when the listener encounters a status change to the application
     * @param app the application who's state has changed.
     * @param status the new status of the application.
     */
    protected abstract void reportStatusChange(Application app, Status status);
    
}


/* 
$Log: AbstractProgressListener.java,v $
Revision 1.3  2008/09/03 14:18:58  pah
result of merge of pah_cea_1611 branch

Revision 1.2.266.1  2008/04/17 16:08:33  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.2  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.1  2004/07/20 02:03:08  nw
added abstract listener classes
 
*/