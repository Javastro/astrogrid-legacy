/*$Id: RemoteResultsListener.java,v 1.7 2008/09/13 09:51:05 pah Exp $
 * Created on 17-Jun-2004
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
import org.astrogrid.common.beanjaxb.JAXB2Axis;
import org.astrogrid.jes.service.v1.cearesults.ResultsListener;
import org.astrogrid.jes.service.v1.cearesults.ResultsListenerService;
import org.astrogrid.jes.service.v1.cearesults.ResultsListenerServiceLocator;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

/** ResultsListener that relays results of application execution back to remote service.
 * @todo what happens when application ends in error? this listener never gets told that..
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 *
 */
public class RemoteResultsListener extends AbstractResultsListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RemoteResultsListener.class);

    /** Construct a new RemoteResultsListener
     * @param endpoint the url of the results listener web service to relay messages to.
     * @throws MalformedURLException if endpoint is not valid.
     * @throws ServiceException if the service could not be connected to
     * 
     */
    public RemoteResultsListener(URI endpoint) throws MalformedURLException, ServiceException {
        super();
        ResultsListenerService serviceLocator = new ResultsListenerServiceLocator(); 
        delegate = serviceLocator.getResultListener(endpoint.toURL());
        this.endpoint = endpoint;
    }
    protected final ResultsListener delegate;
    protected final URI endpoint;

    /**pass on the results notification to the remote web service.
     * 
     * passes the results values too - to save an additional call<p>
     * logs all communication failures. 
     * 
     * @see org.astrogrid.applications.manager.observer.AbstractResultsListener#notifyResultsAvailable(org.astrogrid.applications.Application)
     */
    @Override
    protected void notifyResultsAvailable(Application app)  {
           try {
            delegate.putResults(new JobIdentifierType(app.getJobStepID()),JAXB2Axis.convert( app.getResult()));
        }
        catch (RemoteException e) {
            logger.warn("Could not notify remote listener at " + endpoint,e);
        }
        
    }
}


/* 
$Log: RemoteResultsListener.java,v $
Revision 1.7  2008/09/13 09:51:05  pah
code cleanup

Revision 1.6  2008/09/03 14:18:58  pah
result of merge of pah_cea_1611 branch

Revision 1.5.266.1  2008/04/17 16:08:33  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.5  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.4  2004/07/20 02:03:08  nw
added abstract listener classes

Revision 1.3  2004/07/02 09:11:13  nw
improved logging

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/