
package org.astrogrid.jes.job;

import org.w3c.dom.*;
import java.util.Iterator;

public interface JobFactory {
	
    void begin() ;
    void end( boolean bCommit ) ;

	Job createJob( Document jobDoc ) throws JobException ;

	Iterator findJobsWhere( String queryString ) throws JobException ;
	Job findJob( String jobURN ) throws JobException ;
	
    String deleteJob( Job job ) throws JobException ;

}
