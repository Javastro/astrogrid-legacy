/*$Id: JobStepImpl.java,v 1.1 2003/08/22 10:35:02 nw Exp $
 * Created on 22-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.impl;
 
import org.apache.log4j.Logger;
import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.impl.abstr.AbstractJobStep;
import org.astrogrid.datacenter.job.JobStep;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.RunJobRequestDD;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** implementation of a JobStep that builds its details from an xml document.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Aug-2003
 *
 */
public class JobStepImpl extends AbstractJobStep implements JobStep {
    private static final boolean   TRACE_ENABLED = true ;
    
    private static Logger logger = Logger.getLogger( JobStep.class ) ;
        
    private final static String
        SUBCOMPONENT_NAME =  Util.getComponentName( JobStep.class ) ;        
    

    public JobStepImpl( Element element ,FactoryProvider facManager) throws QueryException {
        if( TRACE_ENABLED ) logger.debug( "JobStepImpl(): entry") ; 
        
        try {
         
           name = element.getAttribute( RunJobRequestDD.JOBSTEP_NAME_ATTR ).trim() ;
           stepNumber = element.getAttribute( RunJobRequestDD.JOBSTEP_STEPNUMBER_ATTR ).trim() ;
           
           NodeList  nodeList = element.getChildNodes() ;
           Element   queryChild = null;
           Element catalogChild = null ;
               
           for( int i=0 ; i < nodeList.getLength() ; i++ ) {
               if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
                   continue ;               
               queryChild = (Element) nodeList.item(i) ;
               if( queryChild.getTagName().equals( RunJobRequestDD.QUERY_ELEMENT ) ) 
                   break ;
           }
        
           nodeList = queryChild.getElementsByTagName( RunJobRequestDD.CATALOG_ELEMENT ) ;
        
           for( int i=0 ; i < nodeList.getLength() ; i++ ) {
               if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
                   continue ;                               
               catalogChild = (Element) nodeList.item(i) ;
               if( catalogChild.getTagName().equals( RunJobRequestDD.CATALOG_ELEMENT ) ) 
                   break ;
           }    
        
           String  keyToFactory = catalogChild.getAttribute( RunJobRequestDD.CATALOG_NAME_ATTR );
                    // TODO : Error - this key has '.QUERYFACTORY' appended to it twice.
                    // i've wasted a days weork finding this stupid bug which should have been eliminated during
                    // unit testing. 
                        //    + DTC.CATALOG_DEFAULT_QUERYFACTORY ;      
         
           query = facManager.getQueryFactory( keyToFactory ).createQuery( queryChild ) ;
           
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "JobStepImpl(): exit") ;           
        }

    }
    
}


/* 
$Log: JobStepImpl.java,v $
Revision 1.1  2003/08/22 10:35:02  nw
refactored job and job step into interface, abstract base class and implementation
 
*/