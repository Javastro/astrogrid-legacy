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
//import java.util.ListIterator;
import java.util.Map ;
import java.util.Collections ;
import java.util.Vector ;
import java.util.Iterator ;
import java.util.ArrayList ;
import java.text.MessageFormat ;
//import java.io.InputStream ;
import org.xml.sax.* ;
import java.io.StringReader ;

import org.apache.log4j.Logger ;
import org.apache.axis.utils.XMLUtils ;
import org.w3c.dom.* ;

import org.astrogrid.community.common.util.CommunityMessage ;

//import org.astrogrid.i18n.*;
//import org.astrogrid.AstroGridException ;
import org.astrogrid.jes.delegate.jobController.*;
import org.astrogrid.jes.delegate.JesDelegateException;

// import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;

import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.activity.*;
import org.w3c.dom.Document ;
import java.util.ListIterator ;
import org.astrogrid.portal.workflow.design.unittest.* ;

/**
 * The <code>Workflow</code> class represents a complex tree of Activities.
 * The following is a crude representation of the inheritance hierarchy.
 * Once a Workflow is instantiated, it forms the root of an arbitrarilly
 * complex tree of Steps, Flows and Sequences.
 * 
 * <p><blockquote><pre>
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
 * </pre></blockquote><p>
 * 
 * Workflow contains some static factory methods for manipulating Workflows
 * on a persistent basis.
 * <p>
 * A Workflow instance contains sufficient methods to manipulate its internal 
 * structure (adding and subtracting an Activity, and so on).
 * <p>
 * There is one highly significant fact that is critical to navigating around
 * Workflow from the top level. Each instance of an Activity has a unique 
 * key and Workflow maintains a collection that maps key to Activity. If you
 * know the key, you can navigate to the Activity straight from the top level
 * irrespective of the complexity of the tree. @see getActivity() method. 
 * From an Activity it is relatively trivial to find parent and children in context.  
 * 
 * @author  Jeff Lusted
 * @version 1.0 21-Aug-2003    
 * @since   AstroGrid 1.3
 */
public class Workflow extends Activity {
	
	private static final String oneStepSequenceTemplate = 		"<?xml version=\"1.0\" encoding=\"UTF8\"?>" +
		"<!-- Workflow Template contains a sequence of one step ======================================== -->" +
        "<workflow name = \"OneStepJob\" templateName=\"oneStepJob\">" +
        "<description>This is a one step job</description>" +
        "<!-- | The top level structure within a workflow is | always a sequence... ==================== -->" +
        "<sequence>" +
        " <step name=\"StepOne\" stepNumber=\"1\" sequenceNumber=\"1\">" +
        "</step>" +
        "</sequence>" +
        "</workflow>" ;
        
	private static final String twoStepSequenceTemplate =
		"<?xml version=\"1.0\" encoding=\"UTF8\"?>" +
        "<!-- Workflow Template contains a sequence of two steps ======================================= -->" +
        "<workflow name = \"TwoSequentialJobsteps\" templateName=\"twoStepSequence\">" +
        "<description>This is a two step job executed in sequence</description>" +
        "<!-- These two steps are run in sequence because they are enclosed within a sequence block ==== -->" +
        "<sequence>" +
        " <step name=\"StepOne\" joinCondition=\"true\" stepNumber=\"1\" sequenceNumber=\"1\">" +
        " </step>" +
        " <!-- This step will only execute if the previous step executed with a return code of true ==== -->" +
        " <step name=\"StepTwo\" joinCondition=\"true\" stepNumber=\"2\" sequenceNumber=\"2\">" +
        " </step>" +
        "</sequence>" +
        "</workflow>" ; 
        
    private static final String twoStepFlowTemplate = 
        "<?xml version=\"1.0\" encoding=\"UTF8\"?>" +
        "<!-- Workflow Template contains a flow of two steps =========================================== -->" +
        "<workflow name = \"TwoParallelJobsteps\" templateName=\"twoStepFlow\">" +
        "<description>This is a two step job executed in parallel</description>" +
        "<!-- Every workflow begins with a top level sequence ========================================== -->" +
        "<sequence>" +
        " <flow>" +
        "  <!-- These two steps will be dispatched in this order but they will execute in parallel ===== -->" +
        "  <step name=\"StepOne\" stepNumber=\"1\" sequenceNumber=\"1\">" +
        "  </step>" +
        "  <step name=\"StepTwo\" stepNumber=\"2\" sequenceNumber=\"1\">" +
        "  </step>" +
        " </flow>" +
        "</sequence>" +
        "</workflow>" ;  
        
    private static final String twoStepFlowAndMergeTemplate = 
        "<?xml version=\"1.0\" encoding=\"UTF8\"?>" +
        "<!-- Workflow Template contains a flow of two steps followed sequentially by a third ========== -->" +
        "<workflow name = \"FlowAndMerge\" templateName=\"twoStepFlow\">" +
        "<description>This is a two step job executed in parallel</description>" +
        "<!-- Every workflow begins with a top level sequence ========================================== -->" +
        "<sequence>" +
        " <flow>" +
        "  <!-- These two steps will be dispatched in this order but they will execute in parallel ===== -->" +
        "  <step name=\"StepOne\" stepNumber=\"1\" sequenceNumber=\"1\">" +
        "  </step>" +
        "  <step name=\"StepTwo\" stepNumber=\"2\" sequenceNumber=\"1\">" +
        "  </step>" +
        " </flow>" +
        "  <!-- This step will be dispatched only when steps one and two have finished correctly ======= -->" +
        "  <step name=\"StepThree\" joinCondition=\"true\" stepNumber=\"3\" sequenceNumber=\"2\">" +
        "  </step>" +
        "</sequence>" +
        "</workflow>" ;           
        
    private static final String complexWorkflowTemplate = 
        "<?xml version=\"1.0\" encoding=\"UTF8\"?>" +
        "<!-- Workflow Template contains a flow of four steps followed sequentially by two more steps -->" +
        "<workflow name = \"FlowPlusSubsequentSteps\" templateName=\"complexWorkflow\">" +
        "<description>This is a six step job, four executed in parallel followed by two subsequent steps</description>" +
        "<!-- Every workflow begins with a top level sequence ========================================== -->" +
        "<sequence>" +
        " <flow>" +
        "  <!-- These four steps will be dispatched in this order but they will execute in parallel ===== -->" +
        "  <step name=\"StepOne\" stepNumber=\"1\" sequenceNumber=\"1\">" +
        "  </step>" +
        "  <step name=\"StepTwo\" stepNumber=\"2\" sequenceNumber=\"1\">" +
        "  </step>" +
        "  <step name=\"StepThree\" stepNumber=\"3\" sequenceNumber=\"1\">" +
        "  </step>" +
        "  <step name=\"StepFour\" stepNumber=\"4\" sequenceNumber=\"1\">" +
        "  </step>" +
        " </flow>" +
        "  <!-- These subsequent steps will be dispatched only when steps one to fouor have finished correctly ======= -->" +
        "  <step name=\"StepFive\" joinCondition=\"true\" stepNumber=\"5\" sequenceNumber=\"2\">" +
        "  </step>" +
        "  <step name=\"StepSix\" joinCondition=\"true\" stepNumber=\"6\" sequenceNumber=\"3\">" +
        "  </step>" +        
        "</sequence>" +
        "</workflow>" ;                    
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Workflow.class ) ; 
        
    /** An experiment in code alignment */
    private static final double cvsVersion = 1.67;
        
    private static final String
        ASTROGRIDERROR_SOMEMESSAGE = "AGWKFE00050" ; // none so far 
        
    private static final String
        MYSPACE_PROTOCOL = "myspace://" ;
        
    /**
      * <p> 
      * Static helper method for creating a bare workflow.
      * <p>
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param name              name of the intended workflow. 
      *                          If the Workflow is finally saved, name will form the file name
      *                          within MySpace, so the name should preferably be unique. 
      *                          However, the name can be set later.
      * @param description       some meaningful description of the workflow. 
      *                          Description can be set later.
      * @return A bare workflow.
      * 
      **/         
    public static Workflow createWorkflow(  String communitySnippet
                                          , String name
                                          , String description  ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.createWorkflow()") ;   
           
        Workflow workflow = null;
          
        debug( "community: " + communitySnippet ) ;
        debug( "name: " + name ) ;
		debug( "description: " + description ) ;          
            
        try {
            workflow = new Workflow() ;
            workflow.setUserid( CommunityMessage.getAccount( communitySnippet ) ) ;
            workflow.setName( name ) ;
            workflow.setDescription( description ) ; 
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.createWorkflow()") ; 
        }
 
        return workflow ;
        
    } // end createWorkflow()
    
    
    /**
      * <p> 
      * Static helper method for creating a workflow containing a given structure
      * of sequences, flows and steps.
      * <p>
      * The structure is given by passing a template name. There are a limited number
      * of templates, with the corresponding names:
      * <p><blockquote><pre>
      * (1) OneStepJob, 
      * (2) TwoParallelJobsteps, 
      * (3) TwoSequentialJobsteps, 
      * (4) TwoStepFlowAndMerge,
      * (5) ComplexWorkflow
      * </pre></blockquote><p>
      * Templates are intended as a short-term measure whilst gui development is
      * getting started.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param name              name of the intended workflow. 
      *                          If the Workflow is finally saved, name will form the file 
      *                          name within MySpace, so the name should preferably be
      *                          unique. However, the name can be set later.
      * @param description       some meaningful description of the workflow. 
      *                          Description can be set later.
      * @param templateName      the name of the template.
      * @return A workflow with the same structure as the template.
      * 
      **/        
    public static Workflow createWorkflowFromTemplate(  String communitySnippet
                                                      , String name
                                                      , String description
                                                      , String templateName  ) {
         if( TRACE_ENABLED ) trace( "entry: Workflow.createWorkflowFromTemplate()") ;   
           
         Workflow workflow = null ;
         String templateString = null ;
          
         debug( "community: " + communitySnippet ) ;
         debug( "name: " + name ) ;
         debug( "description: " + description ) ;          
            
         try {
                       
             InputSource source = new InputSource( new StringReader( retrieveTemplate(templateName) ) );
             Document doc = XMLUtils.newDocument(source) ;
             workflow = new Workflow( communitySnippet, doc ) ;              
              
             workflow.setUserid( CommunityMessage.getAccount( communitySnippet ) ) ;
             workflow.setName( name ) ;
             workflow.setDescription( description ) ; 
             workflow.setTemplateName( templateName ) ;
         }
         catch( Exception ex ) {
             debug( "Exception: " + ex.getLocalizedMessage() );
         }
         finally {
             if( TRACE_ENABLED ) trace( "exit: Workflow.createWorkflowFromTemplate()") ; 
         }
 
         return workflow ;
        
     } // end createWorkflowFromTemplate()
    
    
    
    
    /**
      * <p> 
      * Static helper method for reading a workflow from MySpace.
      * <p>
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param name              name of the workflow. 
      * @return A workflow reinstantiated from MySpace. If the requested workflow 
      *         is not found, will return null
      * 
      **/            
    public static Workflow readWorkflow( String communitySnippet
                                       , String name ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.readWorkflow()") ; 
        
        Workflow workflow = null;
        StringBuffer pathBuffer = new StringBuffer( 64 ) ;
        String xmlString = null ;
        String mySpaceLocation = null ;
         
        try {
            
            mySpaceLocation =  WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
           
            if( mySpaceLocation == null || mySpaceLocation.trim().equals("") ) {
                
                // This is here purely for test situations...
                workflow = WorkflowHelper.readWorkflow( communitySnippet, name ) ;

            }
            else {
                
                pathBuffer
                    .append( "/")
                    .append( CommunityMessage.getAccount( communitySnippet ) )
                    .append( "/")
                    .append( "serv1")
                    .append( "/")
                    .append( "workflow")
                    .append( "/")
                    .append( name ) ;
                    
                xmlString = Workflow.readMySpaceFile( communitySnippet
                                                    , mySpaceLocation
                                                    , pathBuffer.toString() ) ;                     

                if( xmlString != null ) {
                    InputSource source = new InputSource( new StringReader( xmlString ) ) ;
                    Document doc = XMLUtils.newDocument(source) ;
                    debug( "---*** start Workflow document ***---" ) ;
                    XMLUtils.PrettyDocumentToStream( doc, System.out ) ; 
                    debug( "---*** end Workflow document ***---" ) ;          
                    workflow = new Workflow( communitySnippet, doc ) ;
                }
                else {
                    debug( "File not found" ) ;
                }
                                      
            }

        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.readWorkflow()") ; 
        }
       
        return workflow ;
        
    } // end of readWorkflow() 
    

    /**
      * <p> 
      * Static helper method for deleting a workflow from MySpace.
      * <p>
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param name              name of the workflow. 
      * @return A boolean indicating success or failure.
      * 
      **/      
    public static boolean deleteWorkflow( String communitySnippet
                                        , String name  ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.deleteWorkflow()") ; 
        
        boolean retValue = true ; // Assumption is it works!
        StringBuffer pathBuffer = new StringBuffer( 64 );
        String
            mySpaceLocation = null,
            account = null ;       
         
        try {
            
            account = CommunityMessage.getAccount( communitySnippet ) ;
            mySpaceLocation =  WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
           
            if( mySpaceLocation == null || mySpaceLocation.trim().equals("") ) {
                
                // This is here purely for test situations...
                retValue = WorkflowHelper.deleteWorkflow( communitySnippet, name ) ;
                
            }
            else {
                
                MySpaceClient
                    mySpace =  MySpaceDelegateFactory.createDelegate( mySpaceLocation ) ; 
                
                pathBuffer
                   .append( "/")
                   .append( CommunityMessage.getAccount( communitySnippet ) )
                   .append( "/")
                   .append( "serv1")
                   .append( "/")               
                   .append( "workflow")
                   .append( "/")
                   .append( name ) ;
            
                 mySpace.deleteDataHolding( Workflow.extractUserid( account )
                                          , Workflow.extractCommunity( account )
                                          , CommunityMessage.getGroup( communitySnippet )
                                          , pathBuffer.toString() ) ;   
            }
                   
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.deleteWorkflow()") ; 
        }
        
        return retValue ;
        
    } // end of deleteWorkflow()
    
  
    /**
      * <p> 
      * Static helper method for saving a workflow in MySpace.
      * <p>
      * 
      * @param communitySnippet   userid, community, group and security token details.
      * @param workflow           the workflow to be saved. 
      * @return A boolean indicating success or failure.
      * 
      **/       
    public static boolean saveWorkflow( String communitySnippet
                                      , Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.saveWorkflow()") ; 
        
     boolean retValue = false ;
     String
         xmlWorkflow = null,
         filePath = null,
         mySpaceLocation = null,
         account = null ;
     StringBuffer pathBuffer = new StringBuffer( 64 ) ;        
         
     try {;
         
         account = CommunityMessage.getAccount( communitySnippet ) ;
         xmlWorkflow = workflow.constructWorkflowXML( communitySnippet ) ;       
         mySpaceLocation =  WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
           
         if( mySpaceLocation == null || mySpaceLocation.trim().equals("") ) {
             
             // This is here purely for test situations...
             retValue = WorkflowHelper.saveWorkflow( workflow ) ;
             
         }
         else {
             
             pathBuffer
                 .append( "/")
                 .append( CommunityMessage.getAccount( communitySnippet ) )
                 .append( "/")
                 .append( "serv1")
                 .append( "/")
                 .append( "workflow")
                 .append( "/")
                 .append( workflow.getName() ) ;
 
             debug( "mySpaceLocation: " + mySpaceLocation ) ;
             debug( "path set to: " + pathBuffer.toString() ) ;
             debug( "account: " + account ) ;
             debug( "Workflow.extractUserid( account ) returns: " + Workflow.extractUserid( account ) ) ;
             debug( "Workflow.extractCommunity( account ) returns: " + Workflow.extractCommunity( account ) ) ;
         
             MySpaceClient
                 mySpace =  MySpaceDelegateFactory.createDelegate( mySpaceLocation ) ; 
                              
             workflow.setDirty( false ) ;
            
             retValue = mySpace.saveDataHolding( Workflow.extractUserid( account ) 
                                               , Workflow.extractCommunity( account ) 
                                               , CommunityMessage.getGroup( communitySnippet)
                                               , pathBuffer.toString()     // path
                                               , xmlWorkflow               // file contents
                                               , "workflow"                // it's a workflow
                                               , "Overwrite" ) ;           // overwrite it if it already exists
                                                            
         }
                         
     }
     catch( Exception ex ) {
         workflow.setDirty( true ) ;
         ex.printStackTrace() ;
     }
     finally {
         if( TRACE_ENABLED ) trace( "exit: Workflow.saveWorkflow()") ; 
     }
        
     return retValue ;

    } // end of saveWorkflow()
    
 
    /**
      * <p> 
      * Static helper method for submitting a workflow to the Job Entry System.
      * <p>
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param workflow          the workflow to be submitted. 
      * @return A boolean indicating success or failure.
      * 
      **/         
    public static boolean submitWorkflow( String communitySnippet
                                        , Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.submitWorkflow()") ; 

        boolean retValue = false ;
        String
            request = null,
            jesLocation = null,
            response = null ;
        JobControllerDelegate jobController = null ;
                           
        try {
            jesLocation = WKF.getProperty( WKF.JES_URL, WKF.JES_CATEGORY ) ;
            request = workflow.constructJESXML( communitySnippet ) ;
            trace( "jesLocation: " + jesLocation ) ;
            jobController = JobControllerDelegate.buildDelegate( jesLocation ) ;
            jobController.submitJob( request ) ;
            retValue = true ;            
        }
        catch( JesDelegateException jdex ) {
            jdex.printStackTrace() ;
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "entry: Workflow.submitWorkflow()") ; 
        }
        
        return retValue ;

    } // end of submitWorkflow()
    
    
    /**
      * <p> 
      * Static helper method that reads a list of workflows contained in MySpace.
      * <p>
      * At present this returns just an Iterator of string Objects representing the names
      * of the workflow files held by the given user within MySpace.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param filter            a filter argument against file name sumitted to MySpace. 
      *                          NB: this is not currently used. 
      * @return An iterator of file names. 
      **/  
    public static Iterator readWorkflowList( String communitySnippet
                                           , String filter ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.readWorkflowList()") ; 
        
        // JBL: For the moment we are ignoring filter.
        
        StringBuffer argumentBuffer = new StringBuffer( 64 ) ;
        String mySpaceLocation = null ;
        Iterator iterator = null ;
        
        try {
            
            mySpaceLocation =  WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
           
 
            if( mySpaceLocation == null || mySpaceLocation.trim().equals("") ) {
                
                // This is here purely for test situations...
                iterator = WorkflowHelper.readWorkflowList( communitySnippet, filter).iterator() ;
        
            }
            else {
                
                argumentBuffer
                   .append( "/")
                   .append( CommunityMessage.getAccount( communitySnippet ) )
                   .append( "/")
                   .append( "serv1")
                   .append( "/" )
                   .append( "workflow")
                   .append( "/")
                   .append( "*" ) ;
                   
                iterator = Workflow.readMySpaceList( communitySnippet
                                                   , mySpaceLocation
                                                   , argumentBuffer.toString() ) ;
                                        
            }
                          
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.readWorkflowList()") ; 
        }
       
        return iterator ;
        
    } // end of readWorkflowList()
    
  
    /**
      * <p> 
      * Static helper method that reads a list of available application tools.
      * <p>
      * At present this returns just an Iterator of string Objects representing the names
      * of the tools.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @return An iterator of tool names. 
      **/ 
    public static Iterator readToolList( String communitySnippet ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.readToolList()") ;

        ArrayList
            list = null ;
        String
            toolName = null  ;      
        int
            numberTools = 0 ; 
        
        try {
            // debug( "TOOLS_LIST_TOTAL: " + WKF.getProperty( WKF.TOOLS_LIST_CATEGORY, WKF.TOOLS_LIST_TOTAL ) ) ; 
            numberTools = new Integer( WKF.getProperty( WKF.TOOLS_LIST_TOTAL, WKF.TOOLS_LIST_CATEGORY ) ).intValue();
            debug( "numberTools: " + numberTools ) ;
            list = new ArrayList( numberTools ) ;
            for( int i=0; i < numberTools; i++ ) {
                toolName =
                    WKF.getProperty( WKF.TOOLS_LIST_NAME + i, WKF.TOOLS_LIST_CATEGORY  ) ;
                debug( "toolName: " + toolName ) ;
                list.add( toolName ) ;
            }
 
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.readToolList()") ;
        }
        
        return list.listIterator() ;

    } // end of readToolList()
    
    
    /**
      * <p> 
      * Static helper method that instantiates a tool loaded with a minimum
      * set of parameters.
      * <p>
      * 
      * This method requires rewriting when we gain this information from the Registry.
      * At present all the information is held in rather ugly format in a config file.
      * There is little error checking, so make sure the tool name is correct
      * and that the tool details exist.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param name              name of required tool.
      * @return An instance of the tool. 
      **/ 
    public static Tool createTool( String communitySnippet
                                 , String name ) {  
        if( TRACE_ENABLED ) trace( "Workflow.createTool() entry") ;
          
        Tool
            tool = null ;
        int
            numberParams = 0 ;
        Parameter
            param = null ;
        Cardinality 
            cardinality = null ;
                                           
        try {
            tool = new Tool( name ) ;
            tool.setDocumentation( WKF.getProperty( WKF.TOOL_DOCUMENTATION, name ) ) ;
            debug( "Tool: " + tool.getName() );
            debug( "Tool documentation: " + tool.getDocumentation() ) ;
            
            numberParams = new Integer( WKF.getProperty( WKF.TOOL_INPUT_PARAMS_TOTAL, name ) ).intValue() ;
            debug( "Number of input params: " + numberParams ) ;
            for( int i=0; i<numberParams; i++ ) {
                param = tool.newInputParameter( WKF.getProperty( WKF.TOOL_INPUT_PARAM_NAME + i, name ) ) ;
                param.setDocumentation( WKF.getProperty( WKF.TOOL_INPUT_PARAM_DOCUMENTATION + param.getName(), name ) ) ;
                param.setType( WKF.getProperty( WKF.TOOL_INPUT_PARAM_TYPE + param.getName(), name ) ) ;
                cardinality = new Cardinality( WKF.getProperty( WKF.TOOL_INPUT_PARAM_CARDINALITY_MIN + param.getName(), name )
                                             , WKF.getProperty( WKF.TOOL_INPUT_PARAM_CARDINALITY_MAX + param.getName(), name ) ) ;
                param.setCardinality( cardinality ) ;
                if( cardinality.getMinimum() > 1 ) {
                    for( int j = 0; j<cardinality.getMinimum()-1; j++) {
                        tool.newInputParameter( param ) ;
                    }
                }
                
            }
            
            numberParams = new Integer( WKF.getProperty( WKF.TOOL_OUTPUT_PARAMS_TOTAL, name ) ).intValue() ;
            debug( "Number of output params: " + numberParams ) ;
            for( int i=0; i<numberParams; i++ ) {
                param = tool.newOutputParameter( WKF.getProperty( WKF.TOOL_OUTPUT_PARAM_NAME + i, name ) ) ;
                param.setDocumentation( WKF.getProperty( WKF.TOOL_OUTPUT_PARAM_DOCUMENTATION + param.getName(), name ) ) ;
                param.setType( WKF.getProperty( WKF.TOOL_OUTPUT_PARAM_TYPE + param.getName(), name ) ) ;
                cardinality = new Cardinality( WKF.getProperty( WKF.TOOL_OUTPUT_PARAM_CARDINALITY_MIN + param.getName(), name )
                                             , WKF.getProperty( WKF.TOOL_OUTPUT_PARAM_CARDINALITY_MAX + param.getName(), name ) ) ;
                param.setCardinality( cardinality ) ;
                if( cardinality.getMinimum() > 1 ) {
                    for( int j = 0; j<cardinality.getMinimum()-1; j++) {
                        tool.newOutputParameter( param ) ;
                    }
                }
            }
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "Workflow.createTool() exit") ;
        }
          
        return tool ;
                                           
    } // end of createTool()
    

    /**
      * <p> 
      * Static helper method that reads a list of queries contained in MySpace.
      * <p>
      * At present this returns just an Iterator of string Objects representing the names
      * of the query files held by the given user within MySpace.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param filter            a filter argument against file name sumitted to MySpace. 
      *                          NB: this is not currently used. 
      * @return An iterator of file names. 
      **/      
    public static Iterator readQueryList( String communitySnippet
                                        , String filter ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.readQueryList()") ;
        
        // JBL: For the moment we are ignoring filter.
        
        Iterator
           iterator = null ;
        StringBuffer
           argumentBuffer = new StringBuffer( 64 ) ;
        String
           mySpaceLocation ;
        
        try {
                       
            mySpaceLocation =  WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
           

            if( mySpaceLocation == null || mySpaceLocation.trim().equals("") ) {
                 
                 // This is here purely for test situations...
                iterator = WorkflowHelper.readQueryList( communitySnippet, filter).iterator() ;
 
            } 
            else {   
                
                argumentBuffer
                   .append( "/")
                   .append( CommunityMessage.getAccount( communitySnippet ) )
                   .append( "/")
                   .append( "serv1")
                   .append( "/" )
                   .append( "query")
                   .append( "/")
                   .append( "*" ) ;
                   
                iterator = Workflow.readMySpaceList( communitySnippet
                                                   , mySpaceLocation
                                                   , argumentBuffer.toString() ) ;
                                        
            }
                          
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.readQueryList()") ; 
        }
       
        return iterator ;
        
    } // end of readQueryList()
    
 
    /**
      * <p> 
      * Static helper method for reading a query from MySpace.
      * <p>
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param name              name of the query. 
      * @return A query held as an xml String. If the requested query is not found, 
      * will return null.
      * 
      **/           
    public static String readQuery( String communitySnippet
                                  , String name ) {     
        if( TRACE_ENABLED ) trace( "Workflow.readQuery() entry") ;                                                                        

        StringBuffer
           pathBuffer = new StringBuffer( 64 ) ;
        String
           xmlString = null ;
        String
           mySpaceLocation = null ;
         
        try {
            
            mySpaceLocation =  WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
           
            if( mySpaceLocation == null || mySpaceLocation.trim().equals("") ) {
                
               // This is here purely for test situations...
               xmlString = WorkflowHelper.readQuery( communitySnippet, name ) ;
               
            }
            else {
                
                pathBuffer
                    .append( "/")
                    .append( CommunityMessage.getAccount( communitySnippet ) )
                    .append( "/")
                    .append( "serv1")
                    .append( "/")
                    .append( "query")
                    .append( "/")
                    .append( name ) ;
                    
                xmlString = Workflow.readMySpaceFile( communitySnippet
                                                    , mySpaceLocation
                                                    , pathBuffer.toString() ) ;  
            }

         }
         catch ( Exception ex ) {
             ex.printStackTrace() ;
         }
         finally {
             if( TRACE_ENABLED ) trace( "Workflow.readQuery() exit") ; 
         }
       
         return xmlString ;   
                                         
    } // end of Workflow.readQuery()
    
    /**
      * <p> 
      * Name of the Workflow. 
      * If the workflow is saved in MySpace, this will constitute its file name.
      * <p>
      **/         
    private String name ;
    
    /**
      * <p> 
      * Some meaningful description of the workflow.
      * <p>
      **/   
    private String description ;
    
    /**
      * <p> 
      * If the workflow was created with the use of a template, this
      * is the name of that template, otherwise null.
      * <p>
      **/   
    private String templateName ;
        
    /**
      * <p> 
      * Userid is a synonym for Account, eg: jl99@star.le.ac.uk.
      * <p>
      **/     
    private String
        userid ;
    
    /**
      * <p> 
      * Child is the top level structure held within Workflow, which
      * must be some sort of container of activities.
      * It could be a Flow, but at present (January 2004) it is a Sequence.
      * <p>
      **/           
    private ActivityContainer 
        child ;
    
    /**
      * <p> 
      * Activities is the complete collection of activities that forms this
      * Workflow. It's use is primarily for navigating to an Activity given
      * a key.
      * <p>
      **/     
    private Map 
        activities ;
    
    /**
      * <p> 
      * The dirty flag. At present is not used in anger, but will be used to
      * guarantee that updates are flushed to MySpace when requested.
      * <p>
      **/         
    private boolean
        dirty ;
        
    /**
      * Default Workflow constructor.
      * <p> 
      * The parent is set to null by calling super(null).
      * This is the indication within a tree of activities that the top
      * Activity (ie: the Workflow instance itself) has been reached.
      * <p>
      * We set the dirty flag as this Workflow instance has obviously
      * not been saved yet.
      * 
      **/           
    protected Workflow() {
        super(null) ; // null because no parent 
        if( TRACE_ENABLED ) trace( " entry/exit: Workflow(): cvsVersion " + cvsVersion ) ;
        this.setDirty( true ) ;
    }
    
    
    /**
      * Workflow constructor using Document.
      * <p>
      * The parent is set to null by calling super(null).
      * This is the indication within a tree of activities that the top
      * Activity (ie: the Workflow instance itself) has been reached.
      * <p>
      * We set the dirty flag as this Workflow instance has obviously
      * not been saved yet.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param document          workflow as xml Document.
      *                          Its origination could be from MySpace or from a template.
      **/        
    public Workflow( String communitySnippet, Document document ) {
        super(null) ;   // null because no parent 
        if( TRACE_ENABLED ) trace( "entry: Workflow(communitySnippet,document): cvsVersion " + cvsVersion) ;
              
        try{
		    this.setDirty( true ) ;
            
            Element
               element = document.getDocumentElement() ;         
            
            this.name = element.getAttribute( WorkflowDD.WORKFLOW_NAME_ATTR ) ;           
            
            this.templateName = element.getAttribute( WorkflowDD.WORKFLOW_TEMPLATENAME_ATTR ) ;                       
            
            NodeList
               nodeList = element.getChildNodes() ;   
                                       
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {    
                       
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if( element.getTagName().equals( WorkflowDD.DESCRIPTION_ELEMENT ) ) {
                        //bug#106
                        this.description = XMLUtils.getChildCharacterData( element ) ;
                    }  
                    else if ( element.getTagName().equals( WorkflowDD.SEQUENCE_ELEMENT ) ) {
                        setChild( new Sequence( communitySnippet, element, this ) ) ;   
                    } 
                    else if ( element.getTagName().equals( WorkflowDD.FLOW_ELEMENT ) ) {
                        setChild( new Flow( communitySnippet, element, this ) ) ;   
                    } 
                                             
                } // end if
                                
            } // end for        
         
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow(communitySnippet,document)") ;
        }
        
    } // end of Workflow(Document)
    
    
    /**
      * <p> 
      * A navigational aid. Navigates straight to the Activity 
      * given a key without having to know how to traverse the Worlflow.
      * In effect, this gets you straight into context.
      * <p>
      * 
      * @param key - the key of the activity
      **/     
    public Activity getActivity( String key ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.getActivity()") ; 
        try { 
            if( TRACE_ENABLED )debug( "key: [" + key +"]" ) ;
            if( TRACE_ENABLED )debug( "activities: " + activities.toString() ) ;
            return (Activity)activities.get( new ActivityKey( key ) ) ;
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.getActivity()") ;  
        }
    }
    
   
    /**
      * <p> 
      * Maintains the map of activity keys for this Workflow instance.
      * <p>
      * If the activity already exists in the collection, the call is ignored.
      * However, this is indicated by a failure return.
      * <p>
      * NB: The map is simply a navigational aid. The activity must
      * be separately inserted into the Workflow structure itself.
      * <p>
      * JBL Note. Should not have a public access scope.
      * 
      * @param activity  an activity that has just been newly created.
      * @return boolean indicating success or failure.
      **/         
    public boolean putActivity( Activity activity ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.putActivity()") ;  
               
        boolean
            retValue = false ;
        
        try { 
            
            // JBL note: this itself probably needs to be synchronized...
            // But is any case poorly placed here. Much better in a
            // constructor.
            if( this.activities == null ) {
                this.activities = Collections.synchronizedMap( new HashMap() ) ;   
            }
        
            if( !activities.containsKey( activity.getKey() ) ) {
                this.activities.put( activity.getKey(), activity ) ;
                retValue  = true ;
            }
       
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.putActivity()") ;  
        }
       
        return retValue ;
        
    } // end putActivity() 
   

    /**
      * <p> 
      * Removes an activity from the map for this Workflow instance.
      * <p>
      * NB: The map is simply a navigational aid. The activity must
      * be separately removed from the Workflow structure itself.
      * 
      * @param activity  an activity to be removed.
      * @return boolean indicating success or failure.
      **/      
    public boolean removeActivity( Activity activity ) {
        return ( activities.remove( activity.getKey() ) == null ? false : true ) ;
    }
    
    
    /**
      * <p> 
      * Produces the xml description of this workflow instance.
      * <p>
      * @see  org.astrogrid.portal.workflow.design.Workflow#constructJESXML(java.lang.String) 
      *       to which it is strongly related. The duplication is historical. 
      *       JES was produced in an earlier iteration than Workflow and we have yet 
      *       to systematize the two object models.
      * <p>
      * @see  org.astrogrid.portal.workflow.design.Workflow#toXMLString(). 
      *       This was its original appellation, but change was forced
      *       by the necessity of using the communitySnippet.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @return the workflow as an xml String.
      **/      
    protected String constructWorkflowXML( String communitySnippet ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.constructWorkflowXML()") ;  
          
        String response = null ;
                                     
        try {
            
            Object []
               inserts = new Object[5] ;
            inserts[0] = this.name ;
            inserts[1] = (this.templateName == null)  ?  "" :  ("templateName=\"" + this.templateName + "\"") ;
            inserts[2] = communitySnippet ;
            inserts[3] = this.description ;
            inserts[4] = this.toXMLString() ;
            
            response = MessageFormat.format( WorkflowDD.WORKFLOW_TEMPLATE, inserts ) ;
            debug( "Workflow xml: \n" + response ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.constructWorkflowXML()") ;    
        }       
        
        return response ;                              
        
    } // end of constructWorkflowXML()


    /**
     * Basically here to satisfy the requirements of inheriting from 
     * the Activity abstract class.
     * 
     * @see org.astrogrid.portal.workflow.design.Workflow#constructWorkflowXML(java.lang.String)
     */
    public String toXMLString() {
        return getChild().toXMLString() ;
    }
    

    /**
      * <p> 
      * Produces the xml job description of this workflow instance.
      * <p>
      * org.astrogrid.portal.workflow.design.Workflow#constructWorkflowXML(java.lang.String)
      * to which it is strongly related. The duplication is historical. 
      * JES was produced in an earlier iteration than Workflow and we have yet 
      * to systematize the two object models.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @return the workflow as a JES xml String.
      **/   
    protected String constructJESXML( String communitySnippet ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.constructJESXML()") ;  
          
        String 
           response = null ;
                                     
        try {
            
            Object []
               inserts = new Object[4] ;
            inserts[0] = this.name ;
            inserts[1] = this.description ;
            inserts[2] = communitySnippet;          
            inserts[3] = this.toJESXMLString() ; // JobSteps come here
            
            response = MessageFormat.format( WorkflowDD.JOB_TEMPLATE, inserts ) ;
            debug( "JES xml: \n" + response ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.constructJESXML()") ;    
        }       
        
        return response ;                              
        
    } // end of constructJESXML()
    
    
    /**
     * Basically here to satisfy the requirements of inheriting from 
     * the Activity abstract class.
     * 
     * @see org.astrogrid.portal.workflow.design.Workflow#constructJESXML(java.lang.String)
     */
    public String toJESXMLString() {
        return getChild().toJESXMLString() ;  
    }

    /**
     * Sets workflow name. Sets the dirty flag.
     */
	public void setName(String name) {
		this.name = name;
        this.dirty = true;
	}

    /**
     * Gets workflow name.
     */
	public String getName() {
		return name;
	}

    /**
     * Sets the userid (synonym for account). Sets the dirty flag.
     */
	public void setUserid(String userid) {
		this.userid = userid;
        this.dirty = true;
	}

    /**
     * Gets the userid.
     */
	public String getUserid() {
		return userid;
	}

    
    /**
      * <p> 
      * Retrieves a workflow template
      * <p>
      * This shows obvious signs of difficulties encountered in retrieving
      * files on the classpath via the config file setup. Unfortunately
      * we have hit this problem periodically and sometimes persistently
      * where Cocoon is concerned, so for the moment the templates have been
      * hard coded. Hopefully, templates in this form will soon be a thing
      * of the past.
      * 
      * @param templateName  name of the template.
      * @return the template as an xml String.
      **/ 
    protected static String retrieveTemplate( String templateName ) { 
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
            else if( templateName.equals( "TwoStepFlowAndMerge" )  ) {
//                retValue = WKF.getProperty( WKF.WORKFLOW_TEMPLATE_TWOSTEPSEQUENCE, WKF.WORKFLOW_CATEGORY ) ;
                  retValue = twoStepFlowAndMergeTemplate ;
            }
            else if( templateName.equals( "ComplexWorkflow" )  ) {
//                retValue = WKF.getProperty( WKF.WORKFLOW_TEMPLATE_??????, WKF.WORKFLOW_CATEGORY ) ;
                  retValue = complexWorkflowTemplate ;
            }            
                
        }
        catch(Exception ex){
        	ex.printStackTrace() ;
        }

        finally {
            if( TRACE_ENABLED ) trace( "Workflow.retrieveTemplate() exit") ;
        }
        
        return retValue ;
        
    } // end of retrieveTemplate()
    
       
    /**
      * <p> 
      * Extracts the userid portion of a Community account.
      * <p>
      * 
      * @param account  the Community version of account
      * @return userid.
      **/ 
    protected static String extractUserid( String account ) {
        String retVal = account.substring( 0, account.indexOf("@") ) ;
        debug( "Userid: " + retVal ) ;
        return retVal; 
    }
    
    
    /**
      * <p> 
      * Extracts the community portion of a Community account.
      * <p>
      * 
      * @param account   the Community version of account
      * @return community.
      **/ 
    protected static String extractCommunity( String account ) {
        String
            retVal = account.substring( account.indexOf("@") + 1 ) ;
        debug( "Community: " + retVal ) ;        
        return retVal; 
    }
    

    protected void setDirty(boolean dirty) {
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
    
    
    /**
      * <p> 
      * Static helper method that reads a list of things from MySpace.
      * <p>
      * At present this returns just an Iterator of string Objects representing the names
      * of the files held by the given user within MySpace.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param myspaceLocation   location of MySpace - a url in String format. 
      * @param myspaceArguments  full path argument to search MySpace
      * @return An iterator of file names. 
      **/     
    protected static Iterator readMySpaceList( String communitySnippet
                                             , String myspaceLocation 
                                             , String myspaceArguments ) {
                                                 
        if( TRACE_ENABLED ) trace( "entry: Workflow.readMySpaceList(): cvsVersion " + cvsVersion ) ; 
        
        Iterator iterator = null ;
        java.util.Vector vector = null ;
        String account ;
        Object obj = null ;
        
        try {
               
            account = CommunityMessage.getAccount( communitySnippet ) ;
                            
            MySpaceClient
                mySpace =  MySpaceDelegateFactory.createDelegate( myspaceLocation ) ; 
             
            // JBL Note (Jan 2004): listDataHoldings returns a vector of vectors...
            // one for each list in the systems that the account participates in!
            // For now, deal with an empty vector. Then if not empty, get the
            // first (and only) vector held within the overall vector and retrieve
            // an iterator for that. This will represent the list from the one and only 
            // (at present) system that this account belongs to. 
            vector = mySpace.listDataHoldings( Workflow.extractUserid( account )
                                             , Workflow.extractCommunity( account )
                                             , CommunityMessage.getGroup( communitySnippet )
                                             , myspaceArguments ) ;
                                             
//            debug( "vector.size(): " + vector.size() ) ;
//            debug( "vector.isEmpty(): " + vector.isEmpty() ) ;
                       
            if( !( (vector == null) || (vector.size() == 0) ) ) { 
                obj = vector.firstElement() ;  
                debug( "Class name: " + obj.getClass().getName() ) ; 
                iterator = ((Vector)obj).iterator() ;                  
            }
                           
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.readMySpaceList()") ; 
        }
       
        return iterator ;
        
    } // end of readMySpaceList()
    
    
    /**
      * <p> 
      * Static helper method that reads a file from MySpace.
      * <p>
      * NB: At present this returns the file contents as a String.
      * 
      * @param communitySnippet  userid, community, group and security token details.
      * @param myspaceLocation   location of MySpace - a url in String format. 
      * @param myspaceArguments  full path argument to search MySpace
      * @return  the file contents as a String.  
      **/     
    protected static String readMySpaceFile( String communitySnippet
                                           , String myspaceLocation
                                           , String arguments ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.readMySpaceFile()") ; 
        
        String
            account = null,
            fileString = null ;
         
        try {
            
            debug( "myspaceLocation: " + myspaceLocation ) ;
            debug( "path set to: " + arguments ) ;
            account = CommunityMessage.getAccount( communitySnippet ) ;
            debug( "account: " + account ) ;
            debug( "Workflow.extractUserid( account ) returns: " + Workflow.extractUserid( account ) ) ;
            debug( "Workflow.extractCommunity( account ) returns: " + Workflow.extractCommunity( account ) ) ;
           
            MySpaceClient
                mySpace =  MySpaceDelegateFactory.createDelegate( myspaceLocation ) ; 
            
            fileString = mySpace.getDataHolding( Workflow.extractUserid( account )
                                               , Workflow.extractCommunity( account )
                                               , CommunityMessage.getGroup( communitySnippet )
                                               , arguments ) ;                      
      
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.readMySpaceFile()") ; 
        }
       
        return fileString ;
        
    } // end of readMySpaceFile() 
    
    
    /**
      * Formats some logical URL for a file in MySpace.
      * <p>
      * This is a guess at present. 
      * 
      * @param communitySnippet  Account and Group details.
      * @param logicalDirectoryPath  a logical path to a file within MySpace.
      *                              The path may be a full or partial path.
      *                              Directories should be separated by forward slashes.
      * @param fileName  just the file name
      * @return the url within MySpace for the given path and file, as a String
      **/   
    public static String formatMySpaceURL( String communitySnippet
                                         , String logicalDirectoryPath
                                         , String fileName ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.formatMySpaceURL()") ;  
               
        StringBuffer
            buffer = new StringBuffer(64) ;
            
        try {
            if( !Workflow.pathContainsAccountDetails( logicalDirectoryPath ) ) {
                buffer
                    .append( Workflow.MYSPACE_PROTOCOL )
                    .append( CommunityMessage.getAccount( communitySnippet ) )
                    .append( "/")
                    .append( "serv1")
                    .append( "/")
                    .append( logicalDirectoryPath )
                    .append( "/")
                    .append( fileName ) ;
            }
            else {
                buffer
                    .append( Workflow.MYSPACE_PROTOCOL )
                    .append( logicalDirectoryPath )
                    .append( "/")
                    .append( fileName ) ;            
            }
                       
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.formatMySpaceURL()" ) ;
        }
                    
        return buffer.toString() ; 
            
    } // formatMySpaceURL()
    

    /**
      * Inserts a tool into a Step.
      * <p>
      * The step is only indentified by its activity key as a String.
      * This means that we must retrieve the activity from the workflow
      * activity collection. The collection holds all sorts of things 
      * which are not necessarilly Step(s), so in retrieving the activity 
      * we do some type checking before insertion. The process can 
      * obviously fail if the activity is not found or is not an instance 
      * of a Step.
      * <p>
      * JBL Note. There is no need for this to be static.
      * 
      * @param stepActivityKey  the key of the proposed step
      * @param tool             the tool to be inserted
      * @param workflow         the relevant workflow.
      * @return boolean indication success or failure
      **/   
    public static boolean insertToolIntoStep( String stepActivityKey
                                            , Tool tool
                                            , Workflow workflow ) {
            if( TRACE_ENABLED ) trace( "entry: Workflow.insertToolIntoStep(stepActivityKey,tool,workflow)") ; 

            boolean retValue = false ;
            Step step = null ;
            Activity activity = null ;
            
            try {
            
                activity = workflow.getActivity( stepActivityKey ) ;
            
                if( activity == null ) {
                    debug( "activity not found" ) ;
                }
                else if( (activity instanceof Step) == false ) {
                    debug( "activity not a Step") ;
                }
                else {
                    step = (Step)activity ;
                            step.setTool( tool ) ;
                            retValue = true ;
                    }
                        
            }
            catch( Exception ex ) {
                    ex.printStackTrace() ;
            }
            finally {
                    if( TRACE_ENABLED ) trace( "exit: Workflow.insertToolIntoStep(stepActivityKey,tool,workflow)") ; 
            }
        
            return retValue ;

    } // end of insertToolIntoStep(stepActivityKey,tool,workflow)    
    
    
    /** 
      * A helper method that deletes a parameter from a given tool. 
      * The array position of the parameter is critical:
      * this is its position (zero based) within the actual cardinality
      * of this named parameter. That is, if the parameter named
      * "votable" has five occurances and the array position is set to
      * three, then the fourth votable parameter will be deleted.
      * <p>
      * For the sake of the sanity of the gui developers, you cannot delete
      * an optional parameter so that there are no instances of the parameter,
      * which does seem to defeat the idea of it being optional. However, you
      * can leave an optional parameter empty of value and it will be ignored
      * at submission time.
      * <p>
      * JBL Note:<p> 
      * (1) I believe this is too fernickety.
      * @see org.astrogrid.portal.workflow.design.Workflow#deleteParameter(org.astrogrid.portal.workflow.design.Tool,java.lang.String,java.lang.String,java.lang.String)      
      * which is based upon value rather than position.<p> 
      * (2) I do not like the direction argument, which I believe shows up a weakness of having 
      * input and output parameters rather than having "direction" as an attribute of a parameter.
      * But in any case, a better type-safe approach is desirable.
      * @deprecated
      * @param tool           the tool that owns the relevant parameter
      * @param paramName      the name of the parameter
      * @param direction      either "input" or "output".
      * @param arrayPosition  zero based position of parameter
      * @return boolean indicating success or failure.
      * 
      **/   
    public static boolean deleteParameter( Tool tool
                                         , String paramName
                                         , String direction
                                         , int arrayPosition ) {
    
        if( TRACE_ENABLED ) trace( "entry: Workflow.deleteParameter(tool,paramName,direction,arrayPosition)") ; 
        
            // It is possible we will not delete a parameter, so the default is false...
            boolean retValue = false ;
            
            // We need two iterators. The first will be used to count the occurances
            // of the requested parameter name. The second will be used for deletion.
            ListIterator it1, it2 ;
            
            // index is used to establish the actual parameter to be deleted
            // count is used to count the actual cardinality
            // minimumAllowedCardinality is used to hold the minimum allowed cardinality
            // for deletion purposes.
            int index = 0, count = 0, minimumAllowedCardinality ;
            
            Parameter p ;
     
        try {
            
            
            if( direction.equals( "input") ){
                it1 = tool.getInputParameters() ;
                it2 = tool.getInputParameters() ;
            }
            else  {
                it1 = tool.getOutputParameters() ;
                it2 = tool.getOutputParameters() ;
            }
            
            while( it1.hasNext() ) {
                p = (Parameter)it1.next() ;
                if( p.getName().equals( paramName ) ) {
                    count++ ;
                }
            }
            
            it1 = null ;
            
            while( it2.hasNext() ) {
                p = (Parameter)it2.next() ;
                if( p.getName().equals( paramName ) ) {
                    if( index == arrayPosition ) {
                        // OK. We have established that this
                        // is the parameter we wish to delete...
                        
                        // We do not allow deletion of an optional
                        // parameter that forces it to have no occurances,
                        // so we set minimum allowed to 1 in these circumstances.
                        if( p.getCardinality().getMinimum() == 0 ) {
                            minimumAllowedCardinality = 1 ;
                        }
                        else {
                            minimumAllowedCardinality = p.getCardinality().getMinimum() ;
                        }
                        
                        if( count > minimumAllowedCardinality ) {
                            it2.remove() ;
                            retValue = true ;
                        }
                        
                        break ;
                    }
                    index++ ;
                }
            }
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.deleteParameter(tool,paramName,direction,arrayPosition)") ; 
        }
        
        return retValue ;
                                                          
    } // end of Workflow.deleteParameter(tool,paramName,direction,arrayPosition)
    

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
      * @param direction  either "input" or "output".
      * @return boolean indicating success or failure.
      * 
      **/      
    public static boolean deleteParameter( Tool tool
                                         , String paramName
                                         , String value
                                         , String direction ) {
    
        if( TRACE_ENABLED ) trace( "entry: Workflow.deleteParameter(tool,paramName,value,direction)") ; 
        
            boolean retValue = false ;
            ListIterator iterator = null ;
            Parameter p ;
     
        try {
            
            if( direction.equals( "input") ){
                iterator = tool.getInputParameters() ;
            }
            else if( direction.equals( "output") ) {
                iterator = tool.getOutputParameters() ;
            }
            else {
                return retValue ;
            }
            
            while( iterator.hasNext() ) {
                p = (Parameter)iterator.next() ;
                if( p.getName().equals( paramName ) ) {
                    if( (value == null || value.equals("") )
                        &&
                        (p.getValue() == null || p.getValue().equals("") ) ) {
                            
                        iterator.remove() ;
                        retValue = true ;
                        break ;
                    }
                    else if( (value != null)
                             &&
                             (value.equals( p.getValue() )) ) {
                        iterator.remove() ;
                        retValue = true ;
                        break ;
                    }
                } // end if
            } // end while 
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.deleteParameter(tool,paramName,value,direction)") ; 
        }
        
        return retValue ;
                                                          
    } // end of Workflow.deleteParameter(tool,paramName,value,direction)


    
    
    /** 
      * A helper method that inserts a value into a parameter for
      * a given tool. The array position of the parameter is critical:
      * this is its position (zero based) within the actual cardinality
      * of this named parameter. That is, if the parameter named
      * "votable" has five occurances and the array position is set to
      * three, then the fourth votable parameter will have its value set.
      * <p>
      * JBL Note: I believe this is too fernickety.
      * @see org.astrogrid.portal.workflow.design.Workflow#insertParameterValue(org.astrogrid.portal.workflow.design.Tool,java.lang.String,java.lang.String,java.lang.String,java.lang.String)      
      * which is based upon old value rather than position.<p> 
      *  
      * @deprecated
      * @param tool           the owning tool
      * @param paramName      the name of the parameter
      * @param paramValue     the value to be inserted. Can be an instream
      *                       value or contents. This is detected by introspection.
      * @param direction      either "input" or "output".
      * @param arrayPosition  zero based position of parameter
      * @return  boolean indicating success or failure.
      * 
      **/      
    public static boolean insertParameterValue( Tool tool
                                              , String paramName
                                              , String paramValue
                                              , String direction
                                              , int arrayPosition ) {
    
        if( TRACE_ENABLED ) trace( "entry: Workflow.insertParameterValue()") ; 
        
            boolean retValue = false ;
            ListIterator iterator = null ;
            int index = 0 ;
            Parameter p ;
     
        try {
            
            if( direction.equals( "input") ){
                iterator = tool.getInputParameters() ;
            }
            else if( direction.equals( "output") ) {
                iterator = tool.getOutputParameters() ;
            }
            else {
                return retValue ;
            }
            
            while( iterator.hasNext() ) {
                p = (Parameter)iterator.next() ;
                if( p.getName().equals( paramName ) ) {
                    if( index == arrayPosition ) {
                        p.setValue( paramValue ) ;
                        retValue = true ;
                        break ;
                    }
                    index++ ;
                }
            }
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.insertParameterValue()") ; 
        }
        
        return retValue ;
                                                          
    } // end of Workflow.insertParameterValue()
    
    
    /** 
      * A helper method that inserts a value into a parameter for
      * a given tool. The old parameter value is used to identify the 
      * suitable parameter as target. This can also be used to establish
      * new values in a virgin set of new, empty parameters by setting 
      * the old value to null or the empty string.
      * <p>
      * This is by far the easiest way of setting parameter values!
      * <p>
      * Note. If the value to be inserted is a parameter with no upper cardinality  
      * and it is not a replacement for an old value (ie: it is a new insert), then
      * as a convenience to gui development, we examine how many empty ones are still
      * left, and insert a new empty parameter if none are found.
      *  
      * @param tool       the owning tool
      * @param paramName  the name of the parameter
      * @param oldValue   the value used to identify the target parameter.
      *                   Can be null or the empty string, in which case the first empty 
      *                   candidate will be used.
      * @param newValue   the value to be inserted. Can be an instream
      *                   value or contents. This is detected by introspection of the parameter
      *                   type. 
      * @param direction  either "input" or "output".
      * @return  boolean indicating success or failure.
      * 
      **/      
    public static boolean insertParameterValue( Tool tool
                                              , String paramName
                                              , String oldValue
                                              , String newValue
                                              , String direction ) {
    
        if( TRACE_ENABLED ) trace( "entry: Workflow.insertParameterValue(tool,paramName,oldValue,newValue,direction)") ; 
        
            boolean retValue = false ;
            ListIterator iterator = null ;
            Parameter 
                p = null,
                savedNewInsertTarget = null ;
            int countOfEmptyParams = 0 ;
     
        try {
            
            if( direction.equals( "input") ){
                iterator = tool.getInputParameters() ;
            }
            else if( direction.equals( "output") ) {
                iterator = tool.getOutputParameters() ;
            }
            else {
                return retValue ; // Return with error!
            }
            
            while( iterator.hasNext() ) {
                p = (Parameter)iterator.next() ;
                if( p.getName().equals( paramName ) ) {
                    // OK, we've identified the parameter...
                    
                    if( (oldValue == null || oldValue.equals("") )
                        &&
                        (p.getValue() == null || p.getValue().equals("") ) ) {
                          
                        // OK, we've identified that we need to insert
                        // into an empty parameter...    
                        p.setValue( newValue ) ;
                        savedNewInsertTarget = p ;
                        retValue = true ;
                        break ;
                    }
                    else if( (oldValue != null)
                             &&
                             (oldValue.equals( p.getValue() )) ) {
                        
                        // OK, we've identified that we wish to replace
                        // an existing value with a new value...         
                        p.setValue( newValue ) ;
                        retValue = true ;
                        break ;
                    }
                } // end if
            } // end while
            
            // If we have inserted a brand new value and the cardinality is unbounded
            if( (savedNewInsertTarget != null) 
                &&
                (savedNewInsertTarget.getCardinality().getMaximum() <= -1) ) {
                  
                // First, count up the empty parameters of this type still left...
                while( iterator.hasNext() ) {
                    p = (Parameter)iterator.next() ;
                    if( (p.getName().equals( paramName )) 
                        &&
                        (p.getValue() == null || p.getValue().equals("") ) ) {
                            
                        countOfEmptyParams++ ;
                    }
                    
                } // endwhile
                 
                // If the count of empty parameters (of this type )is now zero
                // then for the convenience of the gui we create a further empty 
                // parameter... 
                if( countOfEmptyParams == 0 ) { 
                    tool.newInputParameter( paramName ) ;
                }
                                 
            } // endif
             
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.insertParameterValue(tool,paramName,oldValue,newValue,direction)") ; 
        }
        
        return retValue ;
                                                          
    } // end of Workflow.insertParameterValue(tool,paramName,oldValue,newValue,direction)
   
                                               
    /**
     * 
     * Tries to second-guess whether a MySpace logical path is a
     * full or partial path. This is somewhat rough-and-ready and so is a 
     * prime candidate for change as MySpace evolves.
     * <p>
     * Some examples. The path may be presented as a full path...
     * <p><blockquote><pre>
     *     /jl99@star.le.ac.uk/serv1/datafederation/votables
     * </blockquote></pre><p>
     * or a simple partial path...
     * <p><blockquote><pre>
     *     datafederation/votables
     * </blockquote></pre><p>
     * 
     * @param path  the MySpace logical path
     * @return boolean indicating true if the full path is detected.
     */
    private static boolean pathContainsAccountDetails( String path ) {
        if( TRACE_ENABLED ) trace( "entry: Workflow.pathContainsAccountDetails()") ;
        
        boolean retValue = false ;
        
        try {
            
            // This test assumes that the @, for example, in "jl99@star.le.ac.uk"
            // indicates the presence of full path details. This is corroborated
            // by the serv1 appellation, which is currently required in full path
            // myspace references. 
            if( path.indexOf( "@") > 0 
                && 
                path.indexOf( "serv1") != -1) {
                    
                retValue = true ; 
                
            }
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Workflow.pathContainsAccountDetails()") ;  
        }
        
        return retValue ;
        
    } // end of pathContainsAccountDetails()
        
        
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
 
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

    
} // end of class Workflow