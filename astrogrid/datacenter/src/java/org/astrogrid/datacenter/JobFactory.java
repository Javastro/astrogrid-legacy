package org.astrogrid.datacenter;

import org.w3c.dom.*;

public interface JobFactory {
	
//    Job createJob( String jobURN ) throws JobException ;
	Job createJob( Document jobDoc ) throws JobException ;

    Job findJob( String jobURN ) throws JobException ;
    String deleteJob( String jobURN ) throws JobException ;
	String deleteJob( Job job ) throws JobException ;
    
}
