package org.astrogrid.jes.delegate.jobController;

import org.astrogrid.jes.delegate.jobController.impl.* ;

import java.net.URL ;
import java.net.MalformedURLException ;
import java.rmi.RemoteException ;

import java.util.ListIterator ;
import java.text.MessageFormat ;

import org.xml.sax.* ;
import java.io.StringReader ;
import org.apache.axis.utils.XMLUtils ;
import org.w3c.dom.* ;


import org.astrogrid.jes.delegate.JesDelegateException ;

/**
 * The <code>JobControllerDelegate</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.2
 */
public abstract class JobControllerDelegate {
    
    public static final boolean
        TRACE_ENABLED = true ;
    
    protected static final String
        JOBLIST_TEMPLATE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<joblist userid=\"{0}\" community=\"{1}\" >" +
        "   <filter>{2}</filter>" +         "   {3}" +      // community snippet goes here
        "</joblist>" ;  
        
    private static final int
        DEFAULT_TIMEOUT = 60000 ;
        
        
    public static JobControllerDelegate buildDelegate( String targetEndPoint ){
        return JobControllerDelegate.buildDelegate( targetEndPoint, DEFAULT_TIMEOUT ) ;
    }
    
    
    public static JobControllerDelegate buildDelegate( String targetEndPoint
                                                     , int timeout ) { 
        if(TRACE_ENABLED) trace( "JobControllerDelegate.buildDelegate() entry" ) ;
                                                               
        JobControllerDelegate
            delegate = null ;
         
        try {  
            if( targetEndPoint != null && !targetEndPoint.trim().equals("")) {
                delegate = new JobControllerDelegateImpl(targetEndPoint, timeout ) ;
            }
            else {
                delegate = new TestDelegateImpl( targetEndPoint, timeout ) ;
            }
        }
        finally {
            if(TRACE_ENABLED) trace( "JobControllerDelegate.buildDelegate() exit" ) ;
        }

        return delegate ;
    }
    
    
    private String 
        targetEndPoint = null ;
    private int
        timeout = 60000 ;
        
    public JobControllerDelegate( String targetEndPoint ) {
      this.targetEndPoint = targetEndPoint;
    }
    
    public JobControllerDelegate( String targetEndPoint, int timeout ) {
      this.targetEndPoint = targetEndPoint ;
      this.timeout = timeout ;
    }
    
    public abstract void submitJob(String req) throws JesDelegateException ;
    
    
    public abstract String readJobList( String userid
                                      , String community
                                      , String communitySnippet
                                      , String filter ) throws JesDelegateException ;
     
     
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
        retValue = MessageFormat.format( JOBLIST_TEMPLATE, inserts ) ;
        
        debug( "List request: " + retValue ) ;
        
        return retValue  ;
                                            
     } // end of formatListRequest()


	public void setTargetEndPoint(String targetEndPoint) {
		this.targetEndPoint = targetEndPoint;
	}

	public String getTargetEndPoint() {
		return targetEndPoint;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}    
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

} // end of class JobControllerDelegate