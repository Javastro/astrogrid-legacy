/*
 * @(#)WorkflowHelper.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.cocoon.workflow;

import org.apache.log4j.Logger ;

import org.astrogrid.portal.workflow.intf.*;

import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.Interface;

import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.Enumeration;

/**
 * The <code>WorkflowHelper</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 12-Mar-2004
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class WorkflowHelper {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Workflow.class ) ; 
        
    /** 
      * A helper method that inserts contents into a parameter for
      * a given tool. The old contents are used to identify the 
      * suitable parameter as target. This can also be used to establish
      * new contents in a virgin set of new, empty parameters by setting 
      * the old contents to null or the empty string.
      * <p>
      * Note. If the contents to be inserted is a parameter with no upper cardinality  
      * and it is not a replacement for old contents (ie: it is a new insert), then
      * as a convenience to gui development, we examine how many empty ones are still
      * left, and insert a new empty parameter if none are found.
      *  
      * @param applDescription the application description
      * @param tool       the owning tool
      * @param paramName  the name of the parameter
      * @param oldContents   the contents used to identify the target parameter.
      *                   Can be null or the empty string, in which case the first empty 
      *                   candidate will be used.
      * @param newContents  the contents to be inserted. 
      * @param bInParam  true for input, false for output.
      * @return  boolean indicating success or failure.
      * 
      * 
      **/      
    private static boolean insertParameterValue( ApplicationDescription applDescription
                                               , Tool tool
                                               , String paramName
                                               , String oldContents
                                               , String newContents
                                               , boolean bInParam ) {
    
        if( TRACE_ENABLED ) trace( "entry: WorkflowHelper.insertParameterValue(applDescription,tool,paramName,oldValue,newValue,direction)") ; 
        
            boolean retValue = false ;
            Enumeration iterator = null ;
            ParameterValue 
                p = null,
                savedNewInsertTarget = null ;
            int countOfEmptyParams = 0 ;
     
        try {
            
            if( bInParam ){
                iterator = tool.getInput().enumerateParameter() ;
            }
            else {
                iterator = tool.getOutput().enumerateParameter() ;
            }
            
            while( iterator.hasMoreElements() ) {
                p = (ParameterValue)iterator.nextElement() ;
                if( p.getName().equals( paramName ) ) {
                    // OK, we've identified the parameter...
                    
                    if( (oldContents == null || oldContents.equals("") )
                        &&
                        (p.getContent() == null || p.getContent().equals("") ) ) {
                          
                        // OK, we've identified that we need to insert
                        // into an empty parameter...    
                        p.setContent( newContents ) ;
                        savedNewInsertTarget = p ;
                        retValue = true ;
                        break ;
                    }
                    else if( (oldContents != null)
                             &&
                             (oldContents.equals( p.getContent() )) ) {
                        
                        // OK, we've identified that we wish to replace
                        // existing contents with new contents...         
                        p.setContent( newContents ) ;
                        retValue = true ;
                        break ;
                    }
                } // end if
            } // end while
            
            // If we have inserted a brand new value and the cardinality is unbounded
            if( savedNewInsertTarget != null ) {
                
                ParameterRef pRef = WorkflowHelper.getParameterRef(applDescription,tool,savedNewInsertTarget);                 
                
                if( pRef.getMaxoccurs() <= -1) {
                  
                    // First, count up the empty parameters of this type still left...
                     while( iterator.hasMoreElements() ) {
                         p = (ParameterValue)iterator.nextElement() ;
                         if( (p.getName().equals( paramName )) 
                             &&
                             (p.getContent() == null || p.getContent().equals("") ) ) {
                            
                             countOfEmptyParams++ ;
                         }
                    
                     } // endwhile
                 
                     // If the count of empty parameters (of this type )is now zero
                     // then for the convenience of gui gui development we create a further empty 
                     // parameter... 
                     if( countOfEmptyParams == 0 ) { 
                         BaseParameterDefinition paramDef = applDescription.getDefinitionForReference(pRef);
                         ParameterValue paramVal = applDescription.createValueFromDefinition(paramDef);
                         if( bInParam ){
                             tool.getInput().addParameter( paramVal );
                         }
                         else {
                             tool.getOutput().addParameter( paramVal );
                         }
                         
                     }
                     
                } // endif
                           
                                 
            } // endif
             
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: WorkflowHelper.insertParameterValue(applDescription,tool,paramName,oldValue,newValue,direction)") ; 
        }
        
        return retValue ;
                                                          
    } // end of WorkflowHelper.insertParameterValue(applDescription,tool,paramName,oldValue,newValue,direction)


	/** 
	  * A helper method that inserts contents into a parameter for
	  * a given tool. The old contents are used to identify the 
	  * suitable parameter as target. This can also be used to establish
	  * new contents in a virgin set of new, empty parameters by setting 
	  * the old contents to null or the empty string.
	  * <p>
	  * Note. If the contents to be inserted is a parameter with no upper cardinality  
	  * and it is not a replacement for old contents (ie: it is a new insert), then
	  * as a convenience to gui development, we examine how many empty ones are still
	  * left, and insert a new empty parameter if none are found.
	  *  
	  * @param applDescription the application description
	  * @param tool       the owning tool
	  * @param paramName  the name of the parameter
	  * @param oldContents   the contents used to identify the target parameter.
	  *                   Can be null or the empty string, in which case the first empty 
	  *                   candidate will be used.
	  * @param newContents  the contents to be inserted. 
	  * @return  boolean indicating success or failure.
	  * 
	  * 
	  **/      
	public static boolean insertInputParameterValue( ApplicationDescription applDescription
	                                               , Tool tool
	                                               , String paramName
	                                               , String oldContents
	                                               , String newContents ) {
	    
	    return insertParameterValue(applDescription,tool,paramName,oldContents,newContents,true) ;
	                                                      
	}

	/** 
	  * A helper method that inserts contents into a parameter for
	  * a given tool. The old contents are used to identify the 
	  * suitable parameter as target. This can also be used to establish
	  * new contents in a virgin set of new, empty parameters by setting 
	  * the old contents to null or the empty string.
	  * <p>
	  * Note. If the contents to be inserted is a parameter with no upper cardinality  
	  * and it is not a replacement for old contents (ie: it is a new insert), then
	  * as a convenience to gui development, we examine how many empty ones are still
	  * left, and insert a new empty parameter if none are found.
	  *  
	  * @param applDescription the application description
	  * @param tool       the owning tool
	  * @param paramName  the name of the parameter
	  * @param oldContents   the contents used to identify the target parameter.
	  *                   Can be null or the empty string, in which case the first empty 
	  *                   candidate will be used.
	  * @param newContents  the contents to be inserted. 
	  * @return  boolean indicating success or failure.
	  * 
	  * 
	  **/      
	public static boolean insertOutputParameterValue( ApplicationDescription applDescription
	                                                , Tool tool
	                                                , String paramName
	                                                , String oldContents
	                                                , String newContents ) {
	    
	    return insertParameterValue(applDescription,tool,paramName,oldContents,newContents,false) ;
	                                                      
	}  
    
    
    
    
    
    
    
    
    
    
    /**> 
      * A helper method that deletes a parameter from the given tool.
      * The parameter value is used to identify the parameter as target. 
      * Can be used to delete redundant empty parameters by setting the value
      * to null or the empty string.
      * <p>
      * This is by far the easiest way of deleting parameters! We simply locate
      * the parameter with the given name and given value, and delete it.
      *  
      * @param tool       the tool that owns the relevant parameter
      * @param paramName  the name of the parameter
      * @param value      the value used to identify the target parameter.
      *                   Can be null or the empty string, in which case the first empty 
      *                   candidate will be used. 
      * @param bInParameter  true for input, false for output.
      * @return boolean indicating success or failure.
      * 
      **/      
    private static boolean deleteParameter( Tool tool
                                          , String paramName
                                          , String content
                                          , boolean bInParameter ) {
    
        if( TRACE_ENABLED ) trace( "entry: WorkflowHelper.deleteParameter(tool,paramName,value,direction)") ; 
        
            boolean retValue = false ;
            Enumeration iterator = null ;
            ParameterValue p = null;
     
        try {
            
            if( bInParameter ){
                iterator = tool.getInput().enumerateParameter() ;
            }
            else {
                iterator = tool.getOutput().enumerateParameter() ;
            }
            
            while( iterator.hasMoreElements() ) {
                p = (ParameterValue)iterator.nextElement() ;
                if( p.getName().equals( paramName ) ) {
                    if( (content == null || content.equals("") )
                        &&
                        (p.getContent() == null || p.getContent().equals("") ) ) {
                        retValue = true ;
                        break ;
                    }
                    else if( (content != null)
                             &&
                             (content.equals( p.getContent() )) ) {
                        retValue = true ;
                        break ;
                    }
                } // end if
            } // end while 
            
            if( retValue == true ) {
                
                if( bInParameter ){
                    tool.getInput().removeParameter(p) ;
                }
                else  {
                    tool.getOutput().removeParameter(p) ;
                }
                
            }
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: WorkflowHelper.deleteParameter(tool,paramName,value,direction)") ; 
        }
        
        return retValue ;
                                                          
    } // end of WorkflowHelper.deleteParameter(tool,paramName,value,direction)


	/**> 
	  * A helper method that deletes a parameter from the given tool.
	  * The parameter value is used to identify the parameter as target. 
	  * Can be used to delete redundant empty parameters by setting the value
	  * to null or the empty string.
	  * <p>
	  * This is by far the easiest way of deleting parameters! We simply locate
	  * the parameter with the given name and given value, and delete it.
	  *  
	  * @param tool       the tool that owns the relevant parameter
	  * @param paramName  the name of the parameter
	  * @param value      the value used to identify the target parameter.
	  *                   Can be null or the empty string, in which case the first empty 
	  *                   candidate will be used. 
	  * @return boolean indicating success or failure.
	  * 
	  **/      
	public static boolean deleteInputParameter( Tool tool
	                                          , String paramName
	                                          , String content ) {
        return WorkflowHelper.deleteParameter( tool, paramName, content, true );            
	}

	/**> 
	  * A helper method that deletes a parameter from the given tool.
	  * The parameter value is used to identify the parameter as target. 
	  * Can be used to delete redundant empty parameters by setting the value
	  * to null or the empty string.
	  * <p>
	  * This is by far the easiest way of deleting parameters! We simply locate
	  * the parameter with the given name and given value, and delete it.
	  *  
	  * @param tool       the tool that owns the relevant parameter
	  * @param paramName  the name of the parameter
	  * @param value      the value used to identify the target parameter.
	  *                   Can be null or the empty string, in which case the first empty 
	  *                   candidate will be used. 
	  * @return boolean indicating success or failure.
	  * 
	  **/      
	public static boolean deleteOutputParameter( Tool tool
	                                           , String paramName
	                                           , String content ) {
	    return WorkflowHelper.deleteParameter( tool, paramName, content, false );            
	}    
    
	public static ParameterRef getParameterRef( ApplicationDescription applDescription, Tool tool, ParameterValue pv ) {
	    
	    int[] cardinality = new int[2] ; 
	    
	    Interface[] intfs = applDescription.getInterfaces().get_interface();
	    Interface intf = null;
	    for (int i = 0; i < intfs.length; i++) {
	        if (intfs[i].getName().equals(tool.getInterface())) {
	            intf = intfs[i];
	            break;
	        }            
	    }
	            
	    ParameterRef pRef = applDescription.getReferenceForValue( pv, intf );
	    
	    return pRef;
        
	}

                                
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

}// end of class WorkflowHelper
