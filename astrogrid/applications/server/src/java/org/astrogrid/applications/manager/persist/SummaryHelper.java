/*$Id: SummaryHelper.java,v 1.4 2008/09/03 14:18:48 pah Exp $
 * Created on 17-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.persist;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


import org.astrogrid.applications.Application;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.InputListType;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.joda.time.DateTime;

/**
 * Helper class for  building execution summaries from applications 
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 7 May 2008
 *
 */
public class SummaryHelper {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SummaryHelper.class);

     private static  DatatypeFactory dataFactory; //IMPL would be nice for it to be final, but not possible with the exception.
     static {
	 try {
	    dataFactory = DatatypeFactory.newInstance();
	} catch (DatatypeConfigurationException e) {
	    
	    logger.fatal("data factory not properly configured - dates will not be correct in execution summaries", e);
	}
     }
    private SummaryHelper(){
     }
      
/** Build a summary for an application.
 * @param execID unique identifier for the application
 * @param app the application to summarize
 * @return a summary object for this application
 */
    public static ExecutionSummaryType summarize(String execID, Application app) {
     ExecutionSummaryType summary = new ExecutionSummaryType();
        summary.setApplicationName(app.getApplicationDescription().getId());
        summary.setJobId(execID);
        summary.setPhase(app.getStatus().toExecutionPhase());
        if(app.getStartInstant() != null){
           summary.setStartTime(new DateTime(app.getStartInstant()));
            }
        if(app.getEndInstant() != null){
            summary.setEndTime(new DateTime(app.getEndInstant()));
            }
        if(app.getDestruction() != null){
            summary.setDestruction(new DateTime(app.getDestruction()));
            }
        if(app.getDeadline() != null){
            summary.setTermination(new DateTime(app.getDeadline()));
            }
        InputListType inputlist = new InputListType(); 
        summary.setInputList(inputlist );
        //IMPL the summary contains only the list of input and results - not taking into account any of the grouping - note the jaxb style of assigning...
        List<ParameterValue>  inpars = summary.getInputList().getParameter();
        for (int i = 0; i < app.getInputParameters().length; i++) {
	    ParameterValue par = app.getInputParameters()[i];
	    inpars.add(par);
	}
        
        summary.setResultList(app.getResult());
    return summary;
}
 }

/* 
$Log: SummaryHelper.java,v $
Revision 1.4  2008/09/03 14:18:48  pah
result of merge of pah_cea_1611 branch

Revision 1.3.266.7  2008/06/16 21:58:58  pah
altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.

Revision 1.3.266.6  2008/06/10 20:01:39  pah
moved ParameterValue and friends to CEATypes.xsd

Revision 1.3.266.5  2008/05/08 22:40:53  pah
basic UWS working

Revision 1.3.266.4  2008/04/23 14:14:30  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.3.266.3  2008/04/17 16:08:32  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.3.266.2  2008/03/26 17:15:39  pah
Unit tests pass

Revision 1.3.266.1  2008/03/19 23:10:54  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.3  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/