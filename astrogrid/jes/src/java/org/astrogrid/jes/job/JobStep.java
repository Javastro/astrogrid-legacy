
package org.astrogrid.jes.job;

import org.w3c.dom.* ;
import org.apache.log4j.Logger;
// import org.astrogrid.jes.i18n.*;


public class JobStep {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobStep.class ) ;
	 
	private String
		name = null ;
	    
	private Query
		query = null ;
		
	private Job
	    parent ;
		
		
	public JobStep( Job parent, Element element ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "JobStep(): entry") ; 
		
		this.parent = parent ;
		
		try {
		 
		   setName(element.getAttribute( JobDocDescriptor.JOBSTEP_NAME_ATTR )) ;
		
		   NodeList
			  nodeList = element.getChildNodes() ;
		   Element
			  queryChild = null,
			  catalogChild = null ;
			   
		   for( int i=0 ; i < nodeList.getLength() ; i++ ) {
		   				
		   	   if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {	
		   	   
			       queryChild = (Element) nodeList.item(i) ;
			       if( queryChild.getTagName().equals( JobDocDescriptor.QUERY_ELEMENT ) ) 
			           query = new Query( this, queryChild ) ;
			           
			   }
			   
		   } // end for
		   
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "JobStep(): exit") ; 			 
		}

	} // end of constructor JobStep(Element)
		
    public Query getQuery(){ return this.query ; }
    public void setQuery(Query query){ this.query = query ; }

	public void setName(String name) { this.name = name; }
	public String getName() { return name; }

	public void setParent(Job parent) { this.parent = parent; }
	public Job getParent() { return parent; }

    
} // end of class JobStep 
