
package org.astrogrid.jes.job;

import org.w3c.dom.*;
import java.util.Iterator;

public interface JobFactory {
	
    void begin() ;
    boolean end( boolean bCommit )throws JobException ;

	Job createJob( Document jobDoc, String jobXML ) throws JobException ;

	Iterator findJobsWhere( String queryString ) throws JobException ;
	Job findJob( String jobURN ) throws JobException ;
	
    String deleteJob( Job job ) throws JobException ;
    
    void updateJob( Job job ) throws JobException ;

}
