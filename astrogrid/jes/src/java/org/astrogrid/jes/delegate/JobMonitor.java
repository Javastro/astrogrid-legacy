/*$Id: JobMonitor.java,v 1.5 2004/03/15 01:30:30 nw Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate;



import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

/** Interface to a service that should be called when execution of a job step is completed.
 * <p>
 * should rename this as some kind of *Listener interface  - as it is just a callback, albeit as a webservice
 * <p>
 * generalize - from a monitor of completion to a listener to job execution progress.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public interface JobMonitor extends Delegate{
    /**
     * notify an interested party of the progress of job execution
     * @param id unique identifier for the job and step.
     * @param info record of information about a job step execution
     * @throws JesDelegateException
     */
    public abstract void monitorJob(JobIdentifierType id,MessageType info) throws JesDelegateException;
}
/* 
$Log: JobMonitor.java,v $
Revision 1.5  2004/03/15 01:30:30  nw
jazzed up javadoc

Revision 1.4  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.3  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.2.2.3  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.2.2.2  2004/02/17 11:00:15  nw
altered delegate interfaces to fit strongly-types wsdl2java classes

Revision 1.2.2.1  2004/02/11 16:09:10  nw
refactored delegates (again)

Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/06 18:11:21  nw
reworked the delegate classes
- introduced wrapper class and interfaces, plus separate impl
package with abstract base class. moved delegate classes into the correct
packages, deprecated old methods / classes. fitted in castor object model
 
*/