package org.astrogrid.datacenter;

import org.w3c.dom.*;

public interface JobFactory {
	
//    Job create( String jobURN ) ;
	Job create( Document jobDoc ) throws JobException ;
    void update( Job job ) ;
    Job find( String jobURN ) ;
//    String delete( String jobURN ) ;
	String delete( Job job ) ;
    
}
