package org.astrogrid.jes.delegate.jobController;

import org.astrogrid.jes.beans.v1.Job;
import org.astrogrid.jes.delegate.*;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.impl.*;

import org.exolab.castor.xml.CastorException;

import java.io.StringReader;
import java.text.MessageFormat;

/**
 * The <code>JobControllerDelegate</code> class. 
 * @deprecated use {@link org.astrogrid.jes.delegate.JesDelegateFactory} instead
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.2
 */
public abstract class JobControllerDelegate extends AbstractDelegate implements org.astrogrid.jes.delegate.JobController {

    protected static final String
        JOBLIST_TEMPLATE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<joblist userid=\"{0}\" community=\"{1}\" >" +
        "   <filter>{2}</filter>" +         "   {3}" +      // community snippet goes here
        "</joblist>" ;  
        

    public static JobControllerDelegate buildDelegate( String targetEndPoint ){
        return JobControllerDelegate.buildDelegate( targetEndPoint, DEFAULT_TIMEOUT ) ;
    }
    
    
    public static JobControllerDelegate buildDelegate( String targetEndPoint
                                                     , int timeout ) { 

            if( AbstractDelegate.isTestDelegateRequired(targetEndPoint)){
                return new TestJobControllerDelegateImpl( ) ;
            } else {
                return new JobControllerDelegateImpl(targetEndPoint, timeout ) ;                
            }
    }
        
        /** @deprecated - use oo-based methods instead */
    public  void submitJob(String req) throws JesDelegateException {
        try {
        Job j = Job.unmarshal(new StringReader(req));
        this.submitJob(j);
        } catch (CastorException e) {
            throw new JesDelegateException("Could not marshall legacy document into new object model",e);
        }
    }
    
    public abstract void submitJob(Job j) throws JesDelegateException;
    
    /** @deprecated - use oo-based methods instead */
    public abstract String readJobList( String userid
                                      , String community
                                      , String communitySnippet
                                      , String filter ) throws JesDelegateException ;
     
     /** @deprecated use oo-based methods instead */
     public static String formatListRequest( String userid
                                           , String community
                                           , String communitySnippet
                                           , String filter ) {
        
        String
            retValue = null ;                                
        Object []
            inserts = new Object[4] ;
            
        inserts[0] = userid ;
        inserts[1] = community ;
        inserts[2] = filter ;
        inserts[3] = communitySnippet ;
        return MessageFormat.format( JOBLIST_TEMPLATE, inserts ) ;

                                            
     } // end of formatListRequest()
     /*
     public abstract JobList readJobList(Criteria crit) throws JesDelegateException;
*/


} // end of class JobControllerDelegate