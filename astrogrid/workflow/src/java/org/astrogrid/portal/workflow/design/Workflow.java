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
import java.util.Vector ;
import java.util.Iterator ;
import java.text.MessageFormat ;
import java.io.InputStream ;
import org.xml.sax.* ;
import java.io.StringReader ;

import org.apache.log4j.Logger ;
import org.apache.axis.utils.XMLUtils ;
import org.w3c.dom.* ;

import org.astrogrid.community.common.util.CommunityMessage ;

import org.astrogrid.i18n.*;
import org.astrogrid.AstroGridException ;
import org.astrogrid.jes.delegate.jobController.*;

import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.activity.*;
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
	
	private static final String oneStepSequenceTemplate = 		"<?xml version='1.0' encoding='UTF8'?>" +
		"<!-- | Workflow Template contains a sequence of one step =================================================== -->" +
        "<workflow name = 'OneStepJob' templateName='oneStepJob'>" +
        "<userid>userid</userid>" +
        "<community>community</community>" +
        "<description>This is a one step job</description>" +
        "<!-- | The top level structure within a workflow is | always a sequence... =============================================== -->" +
        "<sequence>" +
        " <step name='StepOne' stepNumber='1' sequenceNumber='1'>" +
        "<nulltool/>" +
        "</step>" +
        "</sequence>" +
        "</workflow>" ;
        
	private static final String twoStepSequenceTemplate =
		"<?xml version='1.0' encoding='UTF8'?>" +
        "<!-- | Workflow Template contains a sequence of two steps ==================================================== -->" +
        "<workflow name = 'TwoSequentialJobsteps' templateName='twoStepSequence'>" +
        "<userid>userid</userid>" +
        "<community>community</community>" +
        "<description>This is a two step job executed in sequence</description>" +
        "<!-- | These two steps are run in sequence because they are | enclosed within a sequence block =================================================== -->" +
        "<sequence>" +
        " <step name='StepOne' joinCondition='true' stepNumber='1' sequenceNumber='1'>" +
        "  <nulltool/>" +
        " </step>" +
        " <!-- | This step will only execute if the previous step | executed with a return code of true =============================================== -->" +
        " <step name='StepTwo' joinCondition='true' stepNumber='2' sequenceNumber='2'>" +
        "  <nulltool/>" +
        " </step>" +
        "</sequence>" +
        "</workflow>" ; 
        
    private static final String twoStepFlowTemplate = 
        "<?xml version='1.0' encoding='UTF8'?>" +
        "<!-- | Workflow Template contains a flow of two steps ================================================ -->" +
        "<workflow name = 'TwoParallelJobsteps' templateName='twoStepFlow'>" +
        "<userid>userid</userid>" +
        "<community>community</community>" +
        "<description>This is a two step job executed in parallel</description>" +
        "<!-- | Every workflow begins with a top level sequence ================================================= -->" +
        "<sequence>" +
        " <flow>" +
        "  <!-- | These two steps will be dispatched in this order | but they will execute in parallel =============================================== -->" +
        "  <step name='StepOne' stepNumber='1' sequenceNumber='1'>" +
        "   <nulltool/>" +
        "  </step>" +
        "  <step name='StepTwo' stepNumber='2' sequenceNumber='1'>" +
        "   <nulltool/>" +
        "  </step>" +
        " </flow>" +
        "</sequence>" +
        "</workflow>" ;           
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Workflow.class ) ; 
        
    private static final String
        ASTROGRIDERROR_SOMEMESSAGE = "AGWKFE00050" ; // none so far 
        
    private MySpaceManagerDelegate
        mySpace ;
    
    
    public static Workflow createWorkflow(  String userid
                                          , String community
                                          , String name
                                          , String description  ) {
        if( TRACE_ENABLED ) trace( "Workflow.createWorkflow() entry") ;   
           
        Workflow
            workflow ;
          
        debug( "userid: " + userid ) ;
        debug( "community: " + community ) ;
        debug( "name: " + name ) ;
		debug( "description: " + description ) ;          
            
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
         debug( "description: " + description ) ;          
            
         try {
                       
             InputSource
                source = new InputSource( new StringReader( retrieveTemplate(templateName) ) );
             Document doc = XMLUtils.newDocument(source) ;
             workflow = new Workflow( doc ) ;              
              
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
    
    
    
    
    
    public static Workflow readWorkflow( String userid
                                       , String community
                                       , String communitySnippet
                                       , String name ) {
        if( TRACE_ENABLED ) trace( "Workflow.readWorkflow() entry") ; 
        
        Workflow
            workflow = null;
        StringBuffer
            pathBuffer = new StringBuffer( 64 ) ;
        String
            xmlString = null ;
         
        try {
            
            MySpaceManagerDelegate
                mySpace = new MySpaceManagerDelegate( WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ) ;
                
            pathBuffer
                .append( "/")
                .append( userid )
                .append( "@")
                .append( community )
                .append( "/")
                .append( "serv1")
                .append( "/")
                .append( "workflow")
                .append( "/")
                .append( name ) ;
            
            xmlString = mySpace.getDataHolding( userid
                                              , community
                                              , pathBuffer.toString() ) ;                      

            InputSource
               source = new InputSource( new StringReader( xmlString ) );
                         
            workflow = new Workflow( XMLUtils.newDocument(source) ) ;
      
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.readWorkflow() exit") ; 
        }
       
        return workflow ;
        
    } // end of readWorkflow() 
    
    
    public static boolean deleteWorkflow( String userid
                                        , String community
                                        , String communitySnippet
                                        , String name  ) {
        if( TRACE_ENABLED ) trace( "Workflow.deleteWorkflow() entry") ; 
        
        boolean
            retValue = true ;
        StringBuffer
            pathBuffer = new StringBuffer( 64 ) ;       
         
        try {
            
            MySpaceManagerDelegate
               mySpace = new MySpaceManagerDelegate( WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ) ;
                
           pathBuffer
               .append( "/")
               .append( userid )
               .append( "@")
               .append( community )
               .append( "/")
               .append( "serv1")
               .append( "/")               
               .append( "workflow")
               .append( "/")
               .append( name ) ;
            
           mySpace.deleteDataHolding( userid
                                    , community
                                    , pathBuffer.toString() ) ;                      

        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.deleteWorkflow() exit") ; 
        }
        
        return retValue ;
        
    } // end of deleteWorkflow()
    
    
    public static boolean saveWorkflow( Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "Workflow.saveWorkflow() entry") ; 
        
     boolean
         retValue = false ;
     String
         xmlWorkflow = null,
         filePath = null ;         
         
     try {
        
        MySpaceManagerDelegate
            mySpace = new MySpaceManagerDelegate( WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ) ;
            
        mySpace.saveDataHolding( workflow.getUserid()
                               , workflow.getCommunity()
                               , workflow.getName()        // file name
                               , workflow.toXMLString()    // file contents
                               , "WF"                      // it's a workflow
                               , "Overwrite" ) ;           // overwrite it if it already exists
                        
     }
     catch( Exception ex ) {
         ex.printStackTrace() ;
     }
     finally {
         if( TRACE_ENABLED ) trace( "Workflow.saveWorkflow() exit") ; 
     }
        
     return retValue ;

    } // end of saveWorkflow()
    
    
    public static boolean submitWorkflow( Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "Workflow.submitWorkflow() entry") ; 

        boolean
            retValue = false ;
        String
            request = null,
            jesLocation = null ;
        JobControllerDelegate
            jobController = null ;
                    
        try {
            jesLocation = WKF.getProperty( WKF.JES_URL, WKF.JES_CATEGORY ) ;
            request = workflow.toJESXMLString() ;
            jobController = new JobControllerDelegate( jesLocation ) ;
            jobController.submitJob( request ) ;            
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.submitWorkflow() exit") ; 
        }
        
        return false ;

    } // end of submitWorkflow()
    
    
    public static boolean insertQueryIntoStep( Step step, String queryName ) {
            return Workflow.insertQueryIntoStep( step.getKey().toString()
                                               , queryName
                                               , step.getWorkflow() ) ;                   
    }
  
  
    public static boolean insertQueryIntoStep( String stepActivityKey
                                             , String queryName
                                             , Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "Workflow.insertQueryInStep(String,String) entry") ; 

        boolean
            retValue = false ;
        Step
            step = null ;
        Activity
            activity = null ;
        Query
            query = null ;
            
        try {
            
            activity = workflow.getActivity( stepActivityKey ) ;
            
            if( activity == null ) {
                debug( "activity not found" ) ;
            }
            else if( (activity instanceof Step) == false ) {
                debug( "activity not a Step") ;
            }
            else {
                String
                    communitySnippet 
                        = CommunityMessage.getMessage( workflow.getToken() 
                                                     , workflow.getUserid() + "@" + workflow.getCommunity()
                                                     , workflow.getGroup() ) ;
                step = (Step)activity ;
                query = Query.readQuery( workflow.getUserid()
                                       , workflow.getCommunity()
                                       , communitySnippet
                                       , queryName ) ;
                step.setTool( query ) ;
                retValue = true ;
            }
                        
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.insertQueryInStep(String,String) exit") ; 
        }
        
        return retValue ;

    } // end of insertQueryInStep(String,String) 
    
    
    /*
     * At present this returns just an Iterator of string Objects representing the names
     * of the files.
     */
    public static Iterator readWorkflowList( String userid
                                           , String community
                                           , String communitySnippet
                                           , String filter ) {
        if( TRACE_ENABLED ) trace( "Workflow.readWorkflowList() entry") ; 
        
        // JBL: For the moment we are ignoring filter.
        
        Iterator
           iterator = null ;
        java.util.Vector
           vector = null ;
        StringBuffer
           argumentBuffer = new StringBuffer( 64 ) ;
        
        try {
                
           MySpaceManagerDelegate
              mySpace = new MySpaceManagerDelegate( WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ) ;
                
           argumentBuffer
              .append( "/")
              .append( userid )
              .append( "@")
              .append( community )
              .append( "/")
              .append( "serv1")
              .append( "/" )
              .append( "workflow")
              .append( "/")
              .append( "*" ) ;
            
            vector = mySpace.listDataHoldings( userid
                                             , community
                                             , argumentBuffer.toString() ) ;
                                              
            iterator = vector.iterator() ;  
                          
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.readWorkflowList() exit") ; 
        }
       
        return iterator ;
        
    } // end of readWorkflowList()
    
     
    private String
        name,
        description,
        userid,
        community,
        token,
        group,
        templateName ;
        
    private ActivityContainer 
        child ;
        
    private Map 
        activities ;
        
    private boolean
        dirty ;
        
    /**
      * <p> 
      * Default constructor.
      * <p>
      * 
      * 
      **/           
    private Workflow() {
        super(null) ; // null because no parent 
        if( TRACE_ENABLED ) trace( "Workflow() entry/exit") ;
//        this.activities = Collections.synchronizedMap( new HashMap() ) ;   
//        this.activities.put( this.getKey(), this ) ;
//        if( TRACE_ENABLED ) trace( "Workflow() exit") ;
    }
    
    
    /**
      * <p> 
      * Constructor using Document.
      * <p>
      * 
      * 
      * 
      * @param document - Origination could be from MySpace
      * or from a template loaded from a config file.
      * 
      * @see 
      **/        
    private Workflow( Document document ) {
        super(null) ;   // null because no parent 
        if( TRACE_ENABLED ) trace( "Workflow(Document) entry") ;
        
        try{
		        	
//            this.activities = Collections.synchronizedMap( new HashMap() ) ;
//            this.activities.put( this.getKey(), this ) ;  
            
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
                        setChild(new Sequence( element, this )) ;   
                    } 
                    else if ( element.getTagName().equals( WorkflowDD.FLOW_ELEMENT ) ) {
                        setChild(new Flow( element, this )) ;   
                    } 
                                      
                    
                } // end if
                                
            } // end for        

            
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow(Document) exit") ;
        }
        
    } // end of Workflow(Document)
    
    
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
            if( TRACE_ENABLED )debug( "key: [" + key +"]" ) ;
            if( TRACE_ENABLED ) debug( "activities: " + activities.toString() ) ;
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
            
            if( this.activities == null ) {
                this.activities = Collections.synchronizedMap( new HashMap() ) ;   
            }
        
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
           response = null ;
                                     
        try {
            
            Object []
               inserts = new Object[6] ;
            inserts[0] = this.name ;
            inserts[1] = (this.templateName == null)  ?  "" :  ("templateName=\"" + this.templateName + "\"") ;
            inserts[2] = this.userid ;
            inserts[3] = this.community ;
            inserts[4] = this.description ;

            inserts[5] = getChild().toXMLString() ;
            
            response = MessageFormat.format( WorkflowDD.WORKFLOW_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.toXMLString() exit") ;    
        }       
        
        return response ;                              
        
    } // end of toXMLString()


    public String toJESXMLString() {
        if( TRACE_ENABLED ) trace( "Workflow.toJESXMLString() entry") ;  
          
        String 
           response = null,
           communitySnippet = null ;
                                     
        try {
            
            communitySnippet 
                = CommunityMessage.getMessage( this.getToken()
                                             , this.getUserid() + "@" + this.getCommunity()
                                             , this.getGroup() );
            
            Object []
               inserts = new Object[6] ;
            inserts[0] = this.name ;
            inserts[1] = this.userid ;
            inserts[2] = this.community ;
            inserts[3] = this.description ;
            inserts[4] = communitySnippet;          
            inserts[5] = getChild().toXMLString() ; // JobSteps come here
            
            response = MessageFormat.format( WorkflowDD.JOB_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.toJESXMLString() exit") ;    
        }       
        
        return response ;                              
        
    } // end of toJESXMLString()


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
//                retValue = WKF.getProperty( WKF.WORKFLOW_TEMPLATE_SINGLESTEP, WKF.WORKFLOW_CATEGORY ) ;
				  retValue = oneStepSequenceTemplate ;
            }
            else if( templateName.equals( "TwoParallelJobsteps" )  ) {
//                retValue = WKF.getProperty( WKF.WORKFLOW_TEMPLATE_TWOSTEPFLOW, WKF.WORKFLOW_CATEGORY ) ;
				  retValue = twoStepFlowTemplate ;
            }
            else if( templateName.equals( "TwoSequentialJobsteps" )  ) {
//                retValue = WKF.getProperty( WKF.WORKFLOW_TEMPLATE_TWOSTEPSEQUENCE, WKF.WORKFLOW_CATEGORY ) ;
				  retValue = twoStepSequenceTemplate ;
            }
        }
        catch(Exception ex){
        	ex.printStackTrace() ;
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

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}
    
} // end of class Workflow