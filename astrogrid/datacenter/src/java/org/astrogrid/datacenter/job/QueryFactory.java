

package org.astrogrid.datacenter.job;


import org.w3c.dom.Element ;

public interface QueryFactory {
	
    Query createQuery( Element queryElement ) throws QueryException ;

    public void execute(Query query) throws QueryException ;
    
    public void end() ;
    
    public Object getImplementation() ;
    
} // end of interface QueryFactory
