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
    
    protected static final String
        JOBLIST_TEMPLATE =
        "<?xml version='1.0' encoding='UTF-8'?>" +
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
        JobControllerDelegate
            delegate = null ;
            
        if( targetEndPoint != null ){
            delegate = new JobControllerDelegateImpl(targetEndPoint, timeout ) ;
        }
        else {
            delegate = null ;
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
            response = null ;                                
        Object []
            inserts = new Object[4] ;
            
        inserts[0] = userid ;
        inserts[1] = community ;
        inserts[2] = filter ;
        inserts[3] = communitySnippet ;
        response = MessageFormat.format( JOBLIST_TEMPLATE, inserts ) ;
        
        return response  ;
                                            
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

} // end of class JobControllerDelegate