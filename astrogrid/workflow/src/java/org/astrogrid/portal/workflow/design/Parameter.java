/*
 * @(#)Parameter.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

// import java.net.*; 
import org.w3c.dom.* ;
import org.apache.log4j.Logger ;
import java.text.MessageFormat ;
import org.apache.axis.utils.XMLUtils ;

/**
 * The <code>Parameter</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 18-Nov-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class Parameter {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Parameter.class ) ;    
    
    private String
        name = null,
        type = null,
        documentation = null ;
        
    private String
        location = "" ;
        
    private String
        contents = "" ;
        
    private Cardinality
        cardinality = null ;
        
    private Parameter() {
    }
     
    protected Parameter( String name ) {
        this.name = name ;   
    }
    
    public Parameter( Element element ) {
        if( TRACE_ENABLED ) trace( "Parameter(Element) entry") ; 
             
        try {
            
            this.name = element.getAttribute( WorkflowDD.PAREMETER_NAME_ATTR ) ;
            this.type = element.getAttribute( WorkflowDD.PAREMETER_TYPE_ATTR ) ;
//            this.location = element.getAttribute( WorkflowDD.PAREMETER_LOCATION_ATTR ) ;
            this.cardinality = 
                new Cardinality( element.getAttribute( WorkflowDD.PARAMETER_MIN_CARDINALITY ) 
                               , element.getAttribute( WorkflowDD.PARAMETER_MAX_CARDINALITY ) ) ;
            
            // If the parameter is instream, the parameter contents is given by the node value, but...
            // If the parameter is a remote reference (to a file within MySpace),
            // then the location is set by the node value...           
            //bug# 106...           
            if( this.isRemoteReference() ) {
                
               //bug# 106    
               this.location = XMLUtils.getChildCharacterData( element ) ;   
               if( location == null ) {
                   location = "" ;
               }
               else {
                   location = location.trim() ;
               }
               debug( "location: " + this.location ) ;
               this.contents = "" ;   

            }
            else {
                
               //bug# 106 
               this.contents = XMLUtils.getChildCharacterData( element ) ;
               if( contents == null ) {
                   contents = "" ;
               }
               else {
                   contents = contents.trim() ;
               }
               debug( "contents: " + this.contents ) ;
               this.location = "" ;
                
            }
                                             
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.DOCUMENTATION_ELEMENT ) ) {
                        this.documentation = XMLUtils.getChildCharacterData( element ) ;  
                    }
                    
                } // end if
                                
            } // end for       
             
        }
        finally {
            if( TRACE_ENABLED ) trace( "Parameter(Element)() exit") ;
        }

    } // end of Parameter(Element)
        
  
    public String getName() {
        return this.name ;  
    }
    
    public String getDocumentation() {
        return this.documentation ;
    }
    
    public String getType() {
        return this.type ;
    }
   
    public Cardinality getCardinality() {
        return this.cardinality ;
    }


	public String getContents() {
		return contents;
	}


	public String getLocation() {
		return location;
	}


	public void setContents( String contents ) {
		if( contents != null ) {
            this.contents = contents.trim() ;
        }
        else {
            this.contents = null ;
        } 
	}


	public void setLocation( String url ) {
        if( url != null ) {
            this.location = url.trim() ;
        }
        else {
            this.location = null ;
        } 
	}
    
    
    protected String toXMLString() {
        if( TRACE_ENABLED ) trace( "Parameter.toXMLString() entry") ;
          
      String 
         response = null ;
                                     
      try {
            
          Object []
             inserts = new Object[6] ;
          inserts[0] = this.getName() ;
          inserts[1] = this.getType() ;
//          inserts[2] = this.getLocation() ;
          inserts[2] = new Integer( this.getCardinality().getMinimum() ).toString();
          inserts[3] = new Integer( this.getCardinality().getMaximum() ).toString(); ;
          inserts[4] = ( this.getDocumentation() == null ) ? " " :  this.getDocumentation() ;
          
          if( this.isRemoteReference() ) {
              inserts[5] = ( this.getLocation() == null ) ? " " :  this.getLocation() ;
          }
          else {
              inserts[5] = ( this.getContents() == null ) ? " " :  this.getContents() ;
          }
          
          response = MessageFormat.format( WorkflowDD.PARAMETER_TEMPLATE, inserts ) ;

      }
      finally {
          if( TRACE_ENABLED ) trace( "Parameter.toXMLString() exit") ;    
      }       
        
      return response ;        
    }
    
    
    protected String toJESXMLString() {
        if( TRACE_ENABLED ) trace( "Parameter.toJESXMLString() entry") ;
        
        // Set up default response - return nothing for a given parameter
        String response = "" ;
         
         
        // This condition requires explanation.
        // There is a hopefully short-term problem with how the GUI deals with optional parameters.
        // At present we leave at least one lying around. At least then the user can see that there 
        // are optional parameters available. 
        // It is here where they can be "edited" out - at job submission time.
        // If a parameter is optional, the minimum cardinality will be 0. Then, if there is no
        // location information and no content information, we assume this is an optional parameter
        // that has been ignored, and effectively do nothing, ie: return an empty string.
        // (Of course, this itself may represent an error on the user's part).                          
        try {
            
            if( this.getCardinality().getMinimum() == 0 
                &&
                ( this.location == null || this.location.length() == 0 ) 
                &&
                ( this.contents == null || this.contents.length() == 0 ) ) {
               
               ; // Do nothing.        
                      
            }
            else {
                
                Object [] inserts = new Object[3] ;
                inserts[0] = this.getName() ;
                inserts[1] = this.getType() ;
            
                if( this.isRemoteReference() ) {
                    inserts[2] = ( this.location == null ) ? "" :  this.location ;
                }
                else {
                    inserts[2] = ( this.contents == null ) ? "" :  this.contents ;
                }          

                response = MessageFormat.format( WorkflowDD.JOBPARAMETER_TEMPLATE, inserts ) ;

            }
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "Parameter.toJESXMLString() exit") ;    
        }       
        
        return response ;    
           
    }
    
     
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

	/**
	   */
	public void setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality;
	}

	/**
	   */
	public void setDocumentation(String string) {
		documentation = string;
	}

	/**
	   */
	public void setType(String string) {
		type = string;
	}
    
    public boolean isRemoteReference() {
        boolean
            bRemoteRef = false ;
            
        if( this.type != null
            &&
            ( this.type.indexOf( "Space_FileReference") != -1 
              ||
              this.type.indexOf( "Space_VOTableReference") != -1 ) ) {
                  
            bRemoteRef = true ;   
             
        }
        return bRemoteRef ;
    }
    
    public boolean isInStream() {
        return !isRemoteReference() ;
    }
 
    
    public void setValue( String value ) {
        if( this.isRemoteReference() ) {
            this.setLocation( value ) ;
        }
        else {
            this.setContents( value ) ;
        }
    }
    
    public String getValue() {
        if( this.isRemoteReference() ) {
            return getLocation() ;
        }
        else {
            return getContents() ;
        }
    }


} // end of class Parameter
