/*
 * @(#)Workflow.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
 
package org.astrogrid.portal.workflow.design;

import java.util.HashMap ;
import java.util.ListIterator;
import java.util.Map ;
import java.util.Collections ;
import java.text.MessageFormat ;
import java.io.InputStream ;
import org.xml.sax.* ;
import java.io.StringReader ;

import org.apache.log4j.Logger ;
import org.apache.axis.utils.XMLUtils ;
import org.w3c.dom.* ;

import org.astrogrid.i18n.*;
import org.astrogrid.AstroGridException ;

import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.activity.*;
import org.astrogrid.portal.workflow.design.myspace.*;
import org.w3c.dom.Document ;

/**
 * The <code>Workflow</code> class represents a complex tree of Activities.
 * The following is a crude representation of the inheritance hierarchy.
 * Once a Workflow is instantiated, it forms the root of an arbitrarilly
 * complex tree of Steps, Flows and Sequences.
 * 
 * 
 * Activity ( Abstract )
 * |
 * |
 * |__Step ( Can only be a bottom leaf )
 * |   
 * |
 * |__ActivityContainer ( Abstract. Can contain any number of any instances
 * |  |                   of an Activity apart from a Workflow itself )
 * |  |
 * |  |__Flow
 * |  |
 * |  |
 * |  |__Sequence 
 * |      
 * |      
 * |__Workflow
 * 
 * 
 * Workflow contains some static factory methods for manipulating Workflows
 * on a persistent basis.
 * 
 * A Workflow instance contains sufficient methods to manipulate its internal 
 * structure (adding and subtracting an Activity, and so on).
 * 
 * There is one highly significant fact that is critical to navigating around
 * Workflow from the top level. Each instance of an Activity has a unique 
 * key and Workflow maintains a collection that maps key to Activity. If you
 * know the key, you can navigate to the Activity straight from the top level
 * irrespective of the complexity of the tree. @see getActivity() method. 
 * From an Activity it is relatively trivial to find parent and children in context.  
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 21-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class Workflow extends Activity {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Workflow.class ) ; 
        
    private static final String
        ASTROGRIDERROR_SOMEMESSAGE = "AGWKFE00050" ; // none so far 
        
    private static MySpaceManagerDelegate
        mySpace ;
        
    static {
        
        try { 
            debug( "Workflow static initializer: entry") ;
            // Loads the workflow config file and messages...
            WKF.getInstance().checkPropertiesLoaded() ;
            debug( "Workflow static initializer: loading of properties suceeded") ;
        }
        catch ( AstroGridException agex ) {
            debug( "Workflow static initializer: loading of properties failed...") ;
            debug( agex.toString() ) ;
        }
        finally {
            debug( "Workflow static initializer: exit") ;
        }
        
    }
    
    
    public static Workflow createWorkflow(  String userid, String community, String name, String description  ) {
        if( TRACE_ENABLED ) trace( "Workflow.createWorkflow() entry") ;   
           
        Workflow
            workflow ;
          
        debug( "userid: " + userid ) ;
        debug( "community: " + community ) ;
        debug( "name: " + name ) ;          
            
        try {
            workflow = new Workflow() ;
            workflow.setUserid( userid) ;
            workflow.setCommunity( community ) ;
            workflow.setName( name ) ;
            workflow.setDescription( description ) ; 
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.createWorkflow() exit") ; 
        }
 
        return workflow ;
        
    } // end createWorkflow()
    
    
    public static Workflow createWorkflowFromTemplate(  String userid
                                                      , String community
                                                      , String name
                                                      , String description
                                                      , String templateName  ) {
         if( TRACE_ENABLED ) trace( "Workflow.createWorkflowFromTemplate() entry") ;   
           
         Workflow
             workflow = null ;
         String
            templateString = null ;
          
         debug( "userid: " + userid ) ;
         debug( "community: " + community ) ;
         debug( "name: " + name ) ;          
            
         try {
                        
             InputSource
                source = new InputSource( new StringReader( retrieveTemplate(templateName) ) );
             workflow = new Workflow( XMLUtils.newDocument(source) ) ;  
              
 //JBL?            workflow = new Workflow( XMLUtils.newDocument( retrieveTemplate(templateName) ) ) ;
             workflow.setUserid( userid) ;
             workflow.setCommunity( community ) ;
             workflow.setName( name ) ;
             workflow.setDescription( description ) ; 
             workflow.setTemplateName( templateName ) ;
         }
         catch( Exception ex ) {
             debug( "Exception: " + ex.getLocalizedMessage() );
         }
         finally {
             if( TRACE_ENABLED ) trace( "Workflow.createWorkflowFromTemplate() exit") ; 
         }
 
         return workflow ;
        
     } // end createWorkflowFromTemplate()
    
    
    
    
        
        
    public static Workflow readWorkflow( String userid, String community, String name ) {
        if( TRACE_ENABLED ) trace( "Workflow.readWorkflow() entry") ; 
        
        Workflow
            workflow = null;
         
        try {
            String
               sourceString = MySpaceHelper.readWorkflow(  userid, community, name ) ; 
                
            InputSource
               source = new InputSource( new StringReader( retrieveTemplate(sourceString) ) );
                         
            workflow = new Workflow( XMLUtils.newDocument(source) ) ;
      
        }
        catch ( Exception ex ) {
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.readWorkflow() exit") ; 
        }
       
        return workflow ;
        
    }
    
    
    public static boolean deleteWorkflow( String userid, String community, String name  ) {
        if( TRACE_ENABLED ) trace( "Workflow.deleteWorkflow() entry") ; 
        
        boolean
            retValue = true ;
         
        try {
            
            MySpaceHelper.deleteWorkflow(  userid, community, name ) ; 

        }
        catch( Exception ex ) {
            debug( "Exception" ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.deleteWorkflow() exit") ; 
        }
        
        return retValue ;
        
    }
    
    
    public static boolean saveWorkflow( Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "Workflow.saveWorkflow() entry") ; 
        
     boolean
         retValue = true ;
         
     try {
        
         MySpaceHelper.saveWorkflow( workflow ) ;
               
     }
     catch( Exception ex ) {
         debug( "Exception" ) ;
     }
     finally {
         if( TRACE_ENABLED ) trace( "Workflow.saveWorkflow() exit") ; 
     }
        
     return retValue ;

    } // end of saveWorkflow()
    
    
    //JBL Note: Is this a misnomer on my part? Should we be attempting this? Is it required?  
    public static ListIterator readWorkflowList( String userid, String community, String name) {
        if( TRACE_ENABLED ) trace( "Workflow.readWorkflowList() entry") ; 
        
            ListIterator
                iterator = null;
         
            try {
               iterator = MySpaceHelper.readWorkflowList(  userid, community ) ;       
            }
            catch ( Exception ex ) {
            }
            finally {
               if( TRACE_ENABLED ) trace( "Workflow.readWorkflowList() exit") ; 
            }
       
            return iterator ;
        
    }
    
     
    private String
        name,
        description,
        userid,
        community,
        templateName ;
        
    private ActivityContainer 
        child = null ;
        
    private Map 
        activities = null ;
        
    private boolean
        dirty ;
        
    /**
      * <p> 
      * Default constructor.
      * <p>
      * Gives a workflow with an empty sequence.
      * 
      **/           
    private Workflow() {
        super() ;
        if( TRACE_ENABLED ) trace( "Workflow() entry") ;
        this.activities = Collections.synchronizedMap( new HashMap() ) ;   
        this.activities.put( this.getKey(), this ) ;
        if( TRACE_ENABLED ) trace( "Workflow() exit") ;
    }
    
    
    /**
      * <p> 
      * Constructor using XML.
      * <p>
      * Gives a workflow as described by the XML
      * 
      * 
      * @param workflowXML - An XML description. Could be from MySpace
      * or from a template loaded from a config file.
      * 
      * @see 
      **/        
    private Workflow( Document document ) {
        super() ;
        if( TRACE_ENABLED ) trace( "Workflow(String) entry") ;
        
        try{
            this.activities.put( this.getKey(), this ) ;
            
            Element
               element = document.getDocumentElement() ;   
               
            name = element.getAttribute( WorkflowDD.WORKFLOW_NAME_ATTR ) ;
            templateName = element.getAttribute( WorkflowDD.WORKFLOW_TEMPLATENAME_ATTR ) ;
                       
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.USERID_ELEMENT ) ) {
                        this.userid = element.getFirstChild().getNodeValue().trim() ;  
                    }  
                    else if( element.getTagName().equals( WorkflowDD.COMMUNITY_ELEMENT ) ) {
                        this.community = element.getFirstChild().getNodeValue().trim() ;
                    }  
                    else if( element.getTagName().equals( WorkflowDD.DESCRIPTION_ELEMENT ) ) {
                        this.description = element.getFirstChild().getNodeValue().trim() ;
                    }  
                    else if ( element.getTagName().equals( WorkflowDD.SEQUENCE_ELEMENT ) ) {
                        setChild(new Sequence( element )) ;   
                    } 
                    else if ( element.getTagName().equals( WorkflowDD.FLOW_ELEMENT ) ) {
                        setChild(new Flow( element )) ;   
                    } 
                                      
                    
                } // end if
                                
            } // end for        

            
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow(String) exit") ;
        }
        
    }
    
    
    /**
      * <p> 
      * A navigational aid. Navigates straight to the Activity 
      * given a key without having to know how to traverse the Worlflow.
      * In effect, this gets you straight into context.
      * <p>
      * 
      * @param String key - the key of the activity
      * 
      * @see 
      **/     
    public Activity getActivity( String key ) {
        if( TRACE_ENABLED ) trace( "Workflow.getActivity() entry") ; 
        try { 
            return (Activity)activities.get( new ActivityKey( key ) ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.getActivity() exit") ;  
        }
    }
    
     
    public boolean putActivity( Activity activity ) {
        if( TRACE_ENABLED ) trace( "Workflow.putActivity() entry") ;  
               
        boolean
            retValue = false ;
        
        try {    
        
            if( !activities.containsKey( activity.getKey() ) ) {
                this.activities.put( activity.getKey(), activity ) ;
                retValue  = true ;
            }
       
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.putActivity() exit") ;  
        }
       
        return retValue ;
        
    } // end putActivity() 
   

    public boolean removeActivity( Activity activity ) {
        return ( activities.remove( activity.getKey() ) == null ? false : true ) ;
    }
    
    
    public String toXMLString() {
        if( TRACE_ENABLED ) trace( "Workflow.toXMLString() entry") ;  
          
        String 
           xmlTemplate = WorkflowDD.WORKFLOW_TEMPLATE,
           response = null ;
                                     
        try {
            
            Object []
               inserts = new Object[5] ;
            inserts[0] = this.name ;
            inserts[1] = (this.templateName == null)  ?  "" :  ("templateName=\"" + this.templateName + "\"") ;
            inserts[2] = this.userid ;
            inserts[3] = this.community ;
            inserts[4] = this.description ;

            inserts[5] = getChild().toXMLString() ;
            
            response = MessageFormat.format( response, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.toXMLString() exit") ;    
        }       
        
        return response ;                              
        
    } // end of toXMLString()


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getCommunity() {
		return community;
	}
    
    private static String locateMySpace( String userid, String community ) {
        if( TRACE_ENABLED ) trace( "Workflow.locateMySpace() entry") ;
        try {
            return WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.locateMySpace() exit") ;
        }
    }
    
    private static String retrieveTemplate( String templateName ) { 
        if( TRACE_ENABLED ) trace( "Workflow.retrieveTemplate() entry") ;
        
        String
            retValue = null;
        
        try {
            
            if( templateName.equals( "OneStepJob" )  ) {
                retValue = WKF.getProperty( WKF.WORKFLOW_TEMPLATE_SINGLESTEP, WKF.WORKFLOW_CATEGORY ) ;
            }
            else if( templateName.equals( "TwoParallelJobsteps" )  ) {
                retValue = WKF.getProperty( WKF.WORKFLOW_TEMPLATE_TWOSTEPFLOW, WKF.WORKFLOW_CATEGORY ) ;
            }
            else if( templateName.equals( "TwoSequentialJobsteps" )  ) {
                retValue = WKF.getProperty( WKF.WORKFLOW_TEMPLATE_TWOSTEPSEQUENCE, WKF.WORKFLOW_CATEGORY ) ;
            }

        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.retrieveTemplate() exit") ;
        }
        
        return retValue ;
        
    }
    

	private void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return dirty;
	}
   
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    } 
     
     
	public void setChild( Activity child ) {
		this.child = (ActivityContainer)child;
	}

	public Activity getChild() {
		return child;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return templateName;
	}
    
      
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }
} // end of class Workflow