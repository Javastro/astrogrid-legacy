package org.astrogrid.portal.workflow.design;

import org.astrogrid.AstroGridException;
import org.astrogrid.community.common.util.CommunityMessage;
import org.astrogrid.portal.workflow.WKF;
import org.astrogrid.portal.workflow.intf.IActivity;
import org.astrogrid.portal.workflow.intf.IParameter;
import org.astrogrid.portal.workflow.intf.ISequence;
import org.astrogrid.portal.workflow.intf.IStep;
import org.astrogrid.portal.workflow.intf.ITool;
import org.astrogrid.portal.workflow.intf.IWorkflow;
import org.astrogrid.portal.workflow.intf.JoinCondition;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WorkflowTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( WorkflowTestSuite.class ) ;
		
	private static final String
        log4jproperties = "/home/jl99/log4j.properties" ;	
        
    private static final String
         ACCOUNT = "pjn3@test.astrogrid.org" ,    // account
         GROUP = "pjn3@test.astrogrid.org" ;      // group
        
      
    /**
     * Holds the last query read from MySpace using a list
     * request. Used as a basis of creating a workflow with
     * a query. 
     */  
    private static String lastQueryNameReadFromList = "" ;
    
    private static String lastWorkflowNameReadFromList = "" ;
    private static String nameOfWorkflowSavedInMySpace = "";

    private final Date
        runDate = new Date() ;
        
	/**
	 * Sets up the test fixture.
	 *
	 * Called before every test case method.
	 */
	protected void setUp() {
        try {
             logger.info( "About to check properties loaded") ;
             WKF.getInstance().checkPropertiesLoaded() ;
             logger.info( "Properties loaded OK") ;
         }
         catch( AstroGridException agex ) {
             logger.info( agex.getAstroGridMessage().toString() ) ;
         }
	}


	/**
	 * Tears down the test fixture.
	 *
	 * Called after every test case method.
	 */
	protected void tearDown() {	
	}
	
    
    /**
     * Tests the ability to navigate around a workflow using the activity key.
     * This is an essential aspect for gui development, since it allows navigation
     * straight into context if you know the key of an activity, without having to
     * navigate to the activity from the root workflow. The latter is definitely
     * possible, but is tortuous on anything but the simplest of workflows.
     * 
     * At present (Jan 2004) the activity key is a non-persistent part of the state 
     * of an activity. That is, it is generated whenever an activity is instantiated 
     * but is not saved as part of its persistent state. This probably needs to 
     * change at some point.
     *
     * This test must always be the first in the suite, so that we can guarantee 
     * the existence of the correct keys for testing navigation.
     *  
     */
    public void testWorkflowActivityNavigation() {
         logger.info( "---------------------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testWorkflowActivityNavigation()" ); 
        
         final String 
             name = "WorkflowActivityNavigation_" + this.runDate.getTime(),
             description = "Workflow meant to test basic activity navigation",
             templateName = "OneStepJob" ;
         Workflow
            workflow = null ,
            workflowActivity = null ;
         ISequence
            sequenceActivity = null ;
         IStep
            stepActivity = null ;
            
         try{
             workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                           , name
                                                           , description
                                                           , templateName ) ;
             
             if( (workflowActivity = (Workflow)workflow.getActivity( "0" )) == null ) {
                 logger.info( "workflowActivity is null" ) ;
                 assertTrue( false ) ;
             }
             if( (sequenceActivity = (ISequence)workflow.getActivity( "1" )) == null ) {
                 logger.info( "sequenceActivity is null" ) ;
                 assertTrue( false ) ;
             } 
             if( (stepActivity = (IStep)workflow.getActivity( "2" )) == null ) {
                 logger.info( "stepActivity is null" ) ;
                 assertTrue( false ) ;
             }  
 
             logger.info( "Workflow: " + workflow.constructWorkflowXML( communitySnippet() ) ) ;
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testWorkflowActivityNavigation()" );  
         }
        
     } // end of testWorkflowActivityNavigation()
    
    
    /**
     * Tests the ability of the workflow to fail tastefully when asked to
     * generate a workflow from a missing template. For now (Jan 2004),
     * should return a null reference. 
     */
    public void testCreateWorkflowFromTemplate_MissingTemplate() {
        logger.info( "-------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testCreateWorkflowFromTemplate_MissingTemplate()" ); 
        
        final String 
            name = "WorkflowFromTemplate_MissingTemplate_" + this.runDate.getTime() ,
            description = "Workflow should fail because of missing template",
            templateName = "IncorrectName" ;
            
        IWorkflow workflow = null ;
            
        try{
            workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                          , name
                                                          , description
                                                          , templateName ) ;
            
            if( workflow == null ) {
                assertTrue( true ) ;
            } 
               
        }
        catch( Exception ex ) {
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testCreateWorkflowFromTemplate_MissingTemplate()" );  
        }
        
    } // end of testCreateWorkflowFromTemplate_MissingTemplate()


    /**
     * Tests the ability of the workflow to create a simple one step job from a template.
     *  
     */
    public void testCreateWorkflowFromTemplate_OneStepTemplate() {
        logger.info( "-------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testCreateWorkflowFromTemplate_OneStepTemplate()" ); 
        
        final String 
            name = "WorkflowFromTemplate_OneStepTemplate_" + this.runDate.getTime() ,
            description = "Workflow created from the OneStepJob template",
            templateName = "OneStepJob" ;
        Workflow
            workflow = null ;
            
        try{
            workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                          , name
                                                          , description
                                                          , templateName ) ;
                                                          
            prettyPrint( "Workflow:", workflow.constructWorkflowXML( communitySnippet() ) ) ;
            
            assertTrue( true ) ;    
        }
        catch( Exception ex ) {
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testCreateWorkflowFromTemplate_OneStepTemplate()" );  
        }
        
    } // end of testCreateWorkflowFromTemplate_OneStepTemplate()
    
    
    
    /**
     * Tests the ability of the workflow to create a one-off complex workflow from a template.
     *  
     */
    public void testCreateWorkflowFromTemplate_ComplexWorlflowTemplate() {
        logger.info( "-------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testCreateWorkflowFromTemplate_ComplexWorlflowTemplate()" ); 
        
        final String 
            name = "testCreateWorkflowFromTemplate_ComplexWorlflowTemplate_" + this.runDate.getTime() ,
            description = "Workflow created from the complex template",
            templateName = "ComplexWorkflow" ;
        Workflow
            workflow = null ;
            
        try{
            workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                          , name
                                                          , description
                                                          , templateName ) ;
                                                          
            prettyPrint( "Workflow:", workflow.constructWorkflowXML( communitySnippet() ) ) ;
            
            assertTrue( true ) ;    
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testCreateWorkflowFromTemplate_ComplexWorlflowTemplate()" );  
        }
        
    } // end of testCreateWorkflowFromTemplate_ComplexWorlflowTemplate()
    
   

    
    
    
    
    
   
    /**
     * Tests our ability to create a workflow from scratch; ie: without the aid
     * of a template. The job consists of a sequence with one step. The step
     * contains an invocation of the DataFederation tool.
     * 
     * JBL Note: we will need to keep the results in some sort of file to make 
     * meaningful comparison of the results of this test.
     *  
     */
    public void testCreateWorkflowFromScratch_OneSequence_OneStep_Tool() {
        logger.info( "---------------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testCreateWorkflowFromScratch_OneSequence_OneStep_Tool()" ); 
        
        final String 
            workflowName = "WorkflowFromScratch_OneSequence_OneStep_Tool_" + this.runDate.getTime(),
            description = "A one sequence, one step workflow designed without use of a template" ; 

        final String
            votableLocation[] = { Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable01.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable02.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable03.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable04.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable05.xml" ) } ;
        final String
            votOutputLocation = Workflow.formatMySpaceURL( communitySnippet(), "votables", "outputXS6wf01.xml") ;
        
        Workflow
            workflow = null ;
        Sequence
            sequence = null ;
        IStep
            step = null ;
        String
            wfXML = null ;            
                       
        try{
            workflow = Workflow.createWorkflow( communitySnippet()
                                              , workflowName
                                              , description ) ;
                                              
            sequence = new Sequence( workflow ) ;
            workflow.setChild( sequence ) ;
            step = sequence.createStep( 0 ) ;
            step.setName( "One step sequence" ) ;
            step.setDescription( "One step sequence containing DataFederation tool" ) ;
            step.setJoinCondition( JoinCondition.ANY() ) ;
            
            // Setting sequence and step numbers shows a weakness in the current approach.
            // These should be auto-generated in some fashion...
            step.setSequenceNumber( 1 ) ;
            step.setStepNumber( 1 ) ;
                                                          
            prettyPrint( "Workflow:", workflow.constructWorkflowXML( communitySnippet() ) ) ;
            
            this.setUpDataFederationStep( step
                                        , votableLocation
                                        , true
                                        , "some sort of show string here"
                                        , "some sort of target string here"
                                        , "some sort of what2show string here"
                                        , "some sort of require string here" 
                                        , "some sort of output string here"
                                        , 5
                                        , votOutputLocation ) ;
 
            wfXML = workflow.constructWorkflowXML( communitySnippet() ) ;
            prettyPrint( "Workflow xml:", wfXML ) ;
            
            //JBL Note: Need to hold correct results in a file for comparison purposes...
            
//            if( jesXML.indexOf( "name=\"require\"" ) == -1 ) {
//                assertTrue( true ) ;
//            }
//            else {
//                assertTrue( false ) ;
//            }
              
            assertTrue( true ) ;
              
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testCreateWorkflowFromScratch_OneSequence_OneStep_Tool()" );  
        }
        
    } // end of testCreateWorkflowFromScratch_OneSequence_OneStep_Tool()


    /**
     * Tests our ability to create a relatively complex workflow from scratch; ie: without the aid
     * of a template. The job consists of a sequence which contains a flow followed by two steps.
     * The flow itself contains 5 steps, each running the SExtractor tool in parallel. The subsequent
     * two remaining steps run the DataFederation tool and the HyperZ tool.
     * 
     * JBL Note: we will need to keep the results in some sort of file to make 
     * meaningful comparison of the results of this test.
     *  
     */
    public void testCreateComplexWorkflow_Sequence_Flow_SevenSteps() {
        logger.info( "---------------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testCreateComplexWorkflow_Sequence_Flow_SevenSteps()" ); 
        
        final String 
            workflowName = "testCreateComplexWorkflow_Sequence_Flow_SevenSteps" + this.runDate.getTime(),
            description = "An overall sequence composed of one flow containing 5 parallel SExtractor steps," +                " followed by one step for DataFederation and one step for HyperZ." +                " Does this without the use of a template." ; 
                
        final String
            imageLocation[] = { Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/images", "image01" )
                              , Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/images", "image02" )
                              , Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/images", "image03" )
                              , Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/images", "image04" )
                              , Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/images", "image05" ) } ;
                              
                              
        final String
            catalogLocation[] = { Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/catalogs", "catalog01" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/catalogs", "catalog02" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/catalogs", "catalog03" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/catalogs", "catalog04" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "SExtractor/catalogs", "catalog05" ) } ;                              
                              
                              
        final String
            configFileLocation = Workflow.formatMySpaceURL( communitySnippet(), "SExtractor", "config_file") ;
            
            
        final String
            parametersLocation = Workflow.formatMySpaceURL( communitySnippet(), "SExtractor", "parameter_names") ;            
      
        final String
            votOutputLocation = Workflow.formatMySpaceURL( communitySnippet(), "votables", "outputXS6wf01.xml") ;
            
        final String
            hyperzConfigFileLocation = Workflow.formatMySpaceURL( communitySnippet(), "hyperz", "config_file") ;            

        final String
             hyperzOutputLocation = Workflow.formatMySpaceURL( communitySnippet(), "hyperz", "hse8eklw") ;
          
        Workflow workflow = null ;
        Sequence sequence = null ;
        Flow flow = null ;
        IStep step = null ;
        int i = 0 ; // The workflow step number          
                       
        try{
            // First, create the bare workflow...
            workflow = Workflow.createWorkflow( communitySnippet()
                                              , workflowName
                                              , description ) ;
              
            // The top level contains a sequence...                                
            sequence = new Sequence( workflow ) ;
            workflow.setChild( sequence ) ;
            
            // The sequence will contain a flow with parallel SExtractor steps...
            flow = sequence.createFlow( 0 ) ; 
            
            // Set up each of the SExtractor steps...
            for( i=0; i<imageLocation.length; i++ ) {   
                step = flow.createStep( i ) ;
                step.setName( "SExtractor job step") ;
                step.setDescription( "++++++++++++++++ SExtractor against image number " + i + "+++++++++++++" ) ;  
                step.setJoinCondition( JoinCondition.ANY() ) ; 
                // Setting sequence and step numbers shows a weakness in the current approach.
                // These should be auto-generated in some fashion...
                step.setSequenceNumber( 1 ) ;
                step.setStepNumber( i+1 ) ; 
                // File details dealt with by this helper method... 
                this.setUpSExtractorStep( step
                                        , imageLocation[i] 
                                        , configFileLocation
                                        , parametersLocation
                                        , catalogLocation[i] ) ; 
            }
            
            
            // The flow will be followed by a DataFederation step...
            step = sequence.createStep( 1 ) ; 
            step.setName( "DataFederation job step") ;
            step.setDescription( "+++++++++++++++ DataFederation against the output from the SExtractor steps +++++++++++++++" ) ;  
            step.setJoinCondition( JoinCondition.TRUE() ) ; 
            // Setting sequence and step numbers shows a weakness in the current approach.
            // These should be auto-generated in some fashion...
            step.setSequenceNumber( 2 ) ;
            step.setStepNumber( i+1 ) ;  
            this.setUpDataFederationStep( step
                                        , catalogLocation
                                        , true
                                        , "some sort of show string here"
                                        , "some sort of target string here"
                                        , "some sort of what2show string here"
                                        , "some sort of require string here" 
                                        , "some sort of output string here"
                                        , 5
                                        , votOutputLocation ) ;

            // The workflow finishes up on a HyperZ step...
            step = sequence.createStep( 2 ) ;  
            step.setName( "HyperZ job step" ) ;
            step.setDescription( "++++++++++++++ HyperZ ++++++++++++++" ) ;  
            step.setJoinCondition( JoinCondition.TRUE() ) ; 
            // Setting sequence and step numbers shows a weakness in the current approach.
            // These should be auto-generated in some fashion...
            step.setSequenceNumber( 3 ) ;
            step.setStepNumber( i+2 ) ; 
            this.setUpHyperZStep( step
                                , hyperzConfigFileLocation
                                , votOutputLocation
                                , hyperzOutputLocation ) ; 
            
            logger.info( "---------------------------------------------------------------------------------" ); 
            prettyPrint( "Workflow xml:", workflow.constructWorkflowXML( communitySnippet() ) ) ;
            logger.info( "---------------------------------------------------------------------------------" );            
            prettyPrint( "JES xml:", workflow.constructJESXML( communitySnippet() ) ) ;
            logger.info( "---------------------------------------------------------------------------------" ); 
            
            //JBL Note: Need to hold correct results in a file for comparison purposes...
            
//            if( jesXML.indexOf( "name=\"??????????????\"" ) == -1 ) {
//                assertTrue( true ) ;
//            }
//            else {
//                assertTrue( false ) ;
//            }
              
            assertTrue( true ) ;
              
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testCreateComplexWorkflow_Sequence_Flow_SevenSteps()" );  
        }
        
    } // end of testCreateComplexWorkflow_Sequence_Flow_SevenSteps()


    /**
     * Tests our ability to save a workflow into MySpace.
     * 
     * The workflow is only partially created before saving. Note: the name of the workflow
     * is saved as an instance variable so that it can be subsequently used when deleting 
     * a workflow from MySpace. 
     */
    public void testSaveWorkflow() {
        logger.info( "-------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testSaveWorkflow()" ); 
         
        nameOfWorkflowSavedInMySpace = "SaveWorkflow_" + this.runDate.getTime() ;
        final String 
             description = "Workflow which should have been saved in MySpace",
             templateName = "OneStepJob" ;
         Workflow
             workflow = null ;
         boolean
             rc = false ;
            
         try{
             
            workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                          , nameOfWorkflowSavedInMySpace
                                                          , description
                                                          , templateName ) ;
      
            logger.info( "Workflow: " + workflow.constructWorkflowXML( communitySnippet() ) ) ;
            
            logger.info( "About to save" ) ; 
            rc = Workflow.saveWorkflow( communitySnippet(), workflow ) ;
            logger.info( "MySpace says: " + rc ) ;
 
            assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             ex.printStackTrace() ;
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testSaveWorkflow()" );  
         }
        
     } // end of testSaveWorkflow()



    /**
     * Tests our ability to read a list of workflows from MySpace.
     * 
     * As a byproduct, saves the name of the last-read workflow so
     * that it may be subsequently read and reconstituted as a workflow
     * in a later test.
     *  
     */
    public void testReadWorkflowList() {
         logger.info( "-----------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testReadWorkflowList()" ); 
        
         Iterator iterator ;
         Object object ;
            
         try{
             iterator = Workflow.readWorkflowList( communitySnippet(), "*" ) ;
             
             while ( iterator.hasNext() ) {
                 object = iterator.next() ;   
                 logger.info( "Class name of iterator object is " + object.getClass().getName() ) ;                 
                 WorkflowTestSuite.lastWorkflowNameReadFromList = (String)object ;
                 logger.info( "Workflow Name: " + WorkflowTestSuite.lastWorkflowNameReadFromList ) ;
             }
 
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             ex.printStackTrace() ;
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadWorkflowList()" );  
         }
        
     } // end of testReadWorkflowList()
 
 
 
    /**
     * Tests our ability to read a workflow from MySpace and reconstitute
     * it as a workflow object.
     */
    public void testReadWorkflow() {
         logger.info( "-------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testReadWorkflow()" ); 
        
         Workflow workflow = null;
            
         try {
             
             if( WorkflowTestSuite.nameOfWorkflowSavedInMySpace.length() == 0 ) {
                 assertTrue( false ) ;
             }
             else {
                 workflow = Workflow.readWorkflow( communitySnippet()
                                                 , WorkflowTestSuite.nameOfWorkflowSavedInMySpace ) ;
                 if( workflow != null ) {
                     prettyPrint( "Workflow:", workflow.constructWorkflowXML( communitySnippet() ) ) ;
                     assertTrue( true ) ;    
                 }
                 else {
                     assertTrue( false ) ;
                 }

             }

         }
         catch( Exception ex ) {           
             ex.printStackTrace() ;
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadWorkflow()" );  
         }
        
     } // end of testReadWorkflow()   
    
    
    /**
     * Tests our ability to delete a workflow from MySpace.
     * 
     * It utilizes the name of the workflow previously saved into MySpace
     * by an earlier test.
     */
    public void testDeleteWorkflow() {
         logger.info( "---------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testDeleteWorkflow()" ); 
        
        boolean ret = false ;
            
        try {
            
            if( nameOfWorkflowSavedInMySpace.length() == 0 ) {
                assertTrue( false ) ; 
            }
            else {
                ret = Workflow.deleteWorkflow( communitySnippet()
                                             , nameOfWorkflowSavedInMySpace ) ;
            
                logger.info( "deleted: " + ret ) ;
 
                assertTrue( ret ) ;  
            }
  
         }
         catch( Exception ex ) {
             ex.printStackTrace() ;
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testDeleteWorkflow()" );  
         }
        
     } // end of testDeleteWorkflow()
    
    


    /**
     * Tests our ability to read a list of queries from MySpace.
     * You just get a list of query names.
     * 
     * As a byproduct, saves the name of the last-read query so
     * that it may be subsequently fully read in a later test.
     */
    public void testReadQueryList() {
         logger.info( "--------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testReadQueryList()" ); 
        
         Iterator iterator ;
         Object object ;
                    
         try{
             iterator = Workflow.readQueryList( communitySnippet(), "*" ) ;
             
 //            logger.info( "iterator: " + iterator ) ;
 //            logger.info( "About to execute iterator: iterator.next()" ) ;
 //            logger.info( iterator.next().getClass().getName() ) ;
             
             while ( iterator.hasNext() ) {             
                 object = iterator.next() ;   
                 logger.info( "Class name of iterator object is " + object.getClass().getName() ) ;
                 WorkflowTestSuite.lastQueryNameReadFromList = (String)object ;
                 logger.info( "Query Name: " + WorkflowTestSuite.lastQueryNameReadFromList ) ;
             }
 
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             ex.printStackTrace() ;
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadQueryList()" );  
         }
        
     } // end of testReadQueryList()
    
    
    /**
     * Tests our ability to read a full query from MySpace.
     * 
     */    
    public void testReadQuery() {
         logger.info( "----------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testReadQuery()" ); 
        
         String query = null ;
            
         try {
             if( WorkflowTestSuite.lastQueryNameReadFromList.length() == 0 ) {
                 assertTrue( false ) ;
             }
             else {
                 query = Workflow.readQuery( communitySnippet(), WorkflowTestSuite.lastQueryNameReadFromList ) ;
                 if( query != null ) {
                     prettyPrint( "Query looks like:", query ) ;
                     assertTrue( true ) ;    
                 }
                 else {
                     assertTrue( false ) ;
                 }
 
             }

         }
         catch( Exception ex ) {
             ex.printStackTrace() ;
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadQuery()" );  
         }
        
     } // end of testReadQuery()
   
   
    /**
     * Tests our ability to gain a list of application tools.
     */
    public void testReadToolList() {
         logger.info( "-------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testReadToolList()" ); 
        
         Iterator  
            iterator ;
            
         try{
             
             iterator = Workflow.readToolList( communitySnippet() ) ;
             
             while ( iterator.hasNext() ) {
                 logger.info( "Tool Name: " + (String)iterator.next() ) ;
             }
 
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             ex.printStackTrace() ;
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadToolList()" );  
         }
        
     } // end of testReadToolList()
     
     
    /** 
     * Tests our ability to fully instantiate an application tool;
     * ie: with all its input and output parameters set up.
     * 
     * Note: It does this for every tool it has information on.
     */
    public void testCreateTool() {
         logger.info( "-----------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testCreateTool()" ); 
        
         Iterator  
            iterator,
            iterator2 ;
         ITool
            tool ;
         Parameter
            param ;
            
         try{
             
             iterator = Workflow.readToolList( communitySnippet() ) ;
             while( iterator.hasNext() ) {
                 tool = Workflow.createTool( communitySnippet(), (String)iterator.next() ) ;
                 logger.info( "===================>>") ;
                 logger.info( "Tool name: " + tool.getName() ) ;
                 logger.info( "Tool documenation: " + tool.getDocumentation() ) ;
                 iterator2 = tool.getInputParameters() ;
                 logger.info( "InputParams... " ) ;
                 while( iterator2.hasNext() ) {
                     param = (Parameter)iterator2.next() ;
                     logger.info( param.getName() );
                     logger.info( param.getDocumentation() );
                     logger.info( param.getType() );
                     logger.info( new Integer( param.getCardinality().getMinimum()).toString() );
                     logger.info( new Integer( param.getCardinality().getMaximum()).toString() ); 
                 }
                
                 iterator2 = tool.getOutputParameters() ;
                 logger.info( "OutputParams... " ) ;
                 while( iterator2.hasNext() ) {
                      param = (Parameter)iterator2.next() ;
                      logger.info( param.getName() );
                      logger.info( param.getDocumentation() );
                      logger.info( param.getType() );
                      logger.info( new Integer( param.getCardinality().getMinimum()).toString() );
                      logger.info( new Integer( param.getCardinality().getMaximum()).toString() ); 
                 }
                 
             }
             logger.info( "===================>>") ;
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             ex.printStackTrace() ;
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testCreateTool()" );  
         }
        
     } // end of testCreateTool()
   
   
    /**
     * Tests the actual job submission facility, but no more.
     * 
     * First it creates a workflow consisting of one step designed to run
     * SExtractor. However, all the values are imaginary, so although the
     * job gets submitted, it will inevitably fail in execution.
     * 
     * JBL Note: shows workflow diagnostics need enhancing, as this always
     * returns true. You must examine the jes log to see that it actually
     * gets submitted correctly. Not a big job.
     */
    public void testSubmitWorkflow() {
        logger.info( "---------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testSubmitWorkflow()" ); 
        
        final String 
            name = "SubmitWorkflow_" + this.runDate.getTime() ,
            description = "A one step workflow that actually gets submitted",
            templateName = "OneStepJob" ;
        Workflow
            workflow = null ;
        boolean
            rc = false ;
            
        try{
            workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                          , name
                                                          , description
                                                          , templateName ) ;
                                                          
            IActivity
                activity = workflow.getChild() ;
                
            Tool
                tool = Workflow.createTool( communitySnippet()
                                          , "SExtractor" ) ;
                                          
            Iterator
                it = tool.getInputParameters() ;
            IParameter
                p ;
                
            while( it.hasNext() ) {
                p = (IParameter)it.next() ;
                if( p.getName().equals("DetectionImage") ) {
                    p.setLocation( Workflow.formatMySpaceURL( communitySnippet()
                                                            , "imagefiles"
                                                            , "image1_12345" ) ) ;
                }
                else if( p.getName().equals( "config_file" ) ) {
                    p.setLocation( Workflow.formatMySpaceURL( communitySnippet()
                                                            , "tools/sextractor"
                                                            , "extractor_config") ) ;
                }
                else if( p.getName().equals("PARAMETERS_NAME") ) {
                    p.setLocation( Workflow.formatMySpaceURL( communitySnippet()
                                                            , "tools/sextractor"
                                                            , "sextractor_parameters" ) ) ;
                }
            }
            
            it = tool.getOutputParameters() ;
            
            while( it.hasNext() ) {
                p = (IParameter)it.next() ;
                if( p.getName().equals("CATALOG_NAME") ) {
                    p.setLocation( Workflow.formatMySpaceURL( communitySnippet()
                                                            , "catalogfiles"
                                                            , "catalog_12345" ) ) ;
                }
            }
            
                
            if( activity instanceof ISequence ) {
                Sequence
                    sequence = (Sequence) activity ;
                IStep
                    step = (IStep)sequence.getChildren().next() ;
                step.setTool( tool ) ;    
            }
             
            prettyPrint( "Workflow:", workflow.constructWorkflowXML( communitySnippet() ) ) ;            
            logger.info( "About to submit" ) ; 
            prettyPrint( "JES string:", workflow.constructJESXML( communitySnippet() ) ) ;
            rc = Workflow.submitWorkflow( communitySnippet(), workflow ) ;
            logger.info( "JobController says: " + rc ) ;
 
            assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             ex.printStackTrace() ;
             assertTrue( false ) ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testSubmitWorkflow()" );  
         }
        
     } // end of testSubmitWorkflow()




    /**
     * Tests our ability to run a query.
     * 
     * This is a start. At present we are dependent upon the query existing
     * and making sense and being submitted to a datacenter that can process
     * it. Nothing insurmountable here, but at present this tests job submission
     * in a similar way to the previous test. Difficult to see how we can test
     * the results as the scheduling of the job against the datacenter is supposed
     * to be asynchronous.
     */
    public void testCreateQueryAndSubmitWorkflow() {
         logger.info( "-----------------------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testCreateQueryAndSubmitWorkflow()" ); 
        
         Date
            date = new Date() ;
            
         final String 
              workflowName = "CreateQueryAndSubmitWorkflow_" + this.runDate.getTime(),
              description = "A one step workflow with a real query that gets submitted",
              templateName = "OneStepJob",
              queryName = "query_20031008.xml" ; // How can I gurantee this will continue to exist.
              // By saving it in MySpace on every occasion (and also deleting it)
              
          Workflow
              workflow = null ;
          String
              query = null,
              queryLocation = null,
              votableLocation = null ;
          Sequence
              sequence = null ;
          boolean
              rc = false ;
          ActivityIterator
              iterator = null ;
          IActivity
              activity = null ;
          IStep
              step = null ;
          Iterator
              listIt = null ;
            
          try{
             workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                           , workflowName
                                                           , description
                                                           , templateName ) ;
                                                           
             Tool
                tool = Workflow.createTool( communitySnippet()
                                          , "QueryTool" ) ;
             
             query = Workflow.readQuery( communitySnippet()
                                       , queryName ) ;
                                       
             queryLocation = Workflow.formatMySpaceURL( communitySnippet()
                                                      , "query"
                                                      , queryName ) ;
                                                      
             votableLocation = Workflow.formatMySpaceURL( communitySnippet()
                                                        , "votables"
                                                        , "votable_0123.xml" ) ;
             
             listIt = tool.getInputParameters() ;
             IParameter
                p ;
             
             while( listIt.hasNext() ) {
                 p = (IParameter)listIt.next() ;
                 if( p.getName().equals("query") )
                    p.setLocation( queryLocation ) ;
                    break ;
             } 
             
             listIt = tool.getOutputParameters() ;
             
             while( listIt.hasNext() ) {
                 p = (IParameter)listIt.next() ;
                 if( p.getName().equals("result") )
                    p.setLocation( votableLocation ) ;
                    break ;
             }                                          
                                                      
             
             sequence = (Sequence)workflow.getChild() ;            
             iterator = sequence.getChildren() ;
             
             while( iterator.hasNext() ) {
                 activity = iterator.next() ;
                 if( activity instanceof IStep ) {
                     step = (IStep)activity ;
                     step.setTool( tool ) ;
                     break ;
                 }
             }
             
             prettyPrint( "JES string:", workflow.constructJESXML( communitySnippet() ) ) ;            
             logger.info( "About to submit" ) ; 
             rc = Workflow.submitWorkflow( communitySnippet(), workflow ) ;
             logger.info( "JobController says: " + rc ) ;
 
             assertTrue( true ) ;    
          }
          catch( Exception ex ) {
              ex.printStackTrace() ;
              assertTrue( false ) ;
          }
          finally {
              logger.info( "exit: WorkflowTestSuite.testCreateQueryAndSubmitWorkflow()" );  
          }
        
      } // end of testCreateQueryAndSubmitWorkflow()
   
    
    /**
     * Tests the helper function within workflow that formats a remote MySpace reference.
     * This test flexes the formatting given a partial myspace path; ie: without the
     * account details. The fact is detected and the account details lifted from 
     * the community snippet.
     */
    public void testFormatMySpaceReference_withoutAccountDetails() {
        logger.info( "---------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testFormatMySpaceReference_withoutAccountDetails()" ); 
        
        final String logicalDirectoryPath = "ag/votables" ;
        final String fileName = "mySpecialVOTable.xml" ;
        final String correctResponse = "myspace://" +  
                                        WorkflowTestSuite.ACCOUNT  + 
                                       "/serv1/" +
                                        logicalDirectoryPath +
                                       "/" +
                                        fileName ;
        String formattedRef ;
        
        try {
            
            formattedRef = Workflow.formatMySpaceURL( communitySnippet()
                                                    , logicalDirectoryPath
                                                    , fileName ) ;
                                                    
            logger.info( "formattedRef: " + formattedRef ) ;
            
            if( formattedRef.equals( correctResponse ) ) {
                assertTrue( true ) ;
            }
            else {
                assertTrue( false ) ;
            }
 
        }
        catch( Exception ex ){
            assertTrue( false ) ;
            ex.printStackTrace() ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testFormatMySpaceReference_withoutAccountDetails()" );   
        }
        
    } // end of testFormatMySpaceReference_withoutAccountDetails()
    

    /**
     * Tests the helper function within workflow that formats a remote MySpace reference.
     * This test flexes the formatting given a full myspace path; ie: with the account 
     * details included 
     */
    public void testFormatMySpaceReference_withAccountDetails() {
        logger.info( "------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testFormatMySpaceReference_withAccountDetails()" ); 
        
        final String logicalDirectoryPath = WorkflowTestSuite.ACCOUNT + "/serv1/ag/votables" ;
        final String fileName = "mySpecialVOTable.xml" ;
        final String correctResponse = "myspace://" +  
                                        logicalDirectoryPath +
                                       "/" +
                                        fileName ;         
        String formattedRef ;     
         
        try {
            formattedRef = Workflow.formatMySpaceURL( communitySnippet()
                                                    , logicalDirectoryPath
                                                    , fileName ) ;
                                                    
            logger.info( "formattedRef: " + formattedRef ) ;
            
            if( formattedRef.equals( correctResponse ) ) {
                assertTrue( true ) ;
            }
            else {
                assertTrue( false ) ;
            }
            
        }
        catch( Exception ex ){
            assertTrue( false ) ;
            ex.printStackTrace() ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testFormatMySpaceReference_withAccountDetails()" );   
        }
        
    } // end of testFormatMySpaceReference_withAccountDetails()


    /**
     * Tests our ability to eliminate empty optional parameters when a workflow gets submitted.
     * 
     * As an aid to gui development, each tool is instantiated with a full but minimum set of
     * input and output parameters. This means if the cardinality of a parameter is set at min=0, 
     * meaning optional, you still get one instance instantiated. Otherwise you would never 
     * know it was there, or that it could exist. The optional parameter can be deleted, but if 
     * it by chance happens to be left around and empty, it gets ignored on job submission.
     * 
     */
    public void testToJesXMLOnEmptyOptionalParameter() {
        logger.info( "------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testToJesXMLOnEmptyOptionalParameter()" ); 

        final String 
            workflowName = "ToJesXMLOnEmptyOptionalParameter_" + this.runDate.getTime(),
            description = "One step DataFederation workflow with an empty optional parameter",
            templateName = "OneStepJob"  ; 
              
 
        final String
            votableLocation[] = { Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable01.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable02.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable03.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable04.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable05.xml" ) } ;
        final String
            votOutputLocation = Workflow.formatMySpaceURL( communitySnippet(), "votables", "outputXS6wf01.xml") ;
            
        Workflow
             workflow = null ;            
        Sequence
            sequence = null ;
        ActivityIterator
            iterator = null ;
        IActivity
            activity = null ;
        IStep
            step = null ;
        String
            jesXML = null ;
            
        try {
            
            workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                          , workflowName
                                                          , description
                                                          , templateName ) ;
        
            sequence = (Sequence)workflow.getChild() ;            
            iterator = sequence.getChildren() ;
             
            while( iterator.hasNext() ) {
                activity = iterator.next() ;
                if( activity instanceof IStep ) {
                    step = (IStep)activity ;
                    break ;
                }
            }
            
            this.setUpDataFederationStep( step
                                        , votableLocation
                                        , true
                                        , "some sort of show string here"
                                        , "some sort of target string here"
                                        , "some sort of what2show string here"
                                        , "" // empty optional parameter ("require")
                                        , "some sort of output string here"
                                        , 5
                                        , votOutputLocation ) ;
 
            jesXML = workflow.constructJESXML( communitySnippet() ) ;
            prettyPrint( "JES string:", jesXML ) ;
            
            // Here we test for the non-existance of the optional parameter...
            if( jesXML.indexOf( "name=\"require\"" ) == -1 ) {
                assertTrue( true ) ;
            }
            else {
                assertTrue( false ) ;
            }
            
        }
        catch( Exception ex ){
            ex.printStackTrace() ;
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testToJesXMLOnEmptyOptionalParameter()" );   
        }
        
    } // end of testToJesXMLOnEmptyOptionalParameter()



    /**
     * Tests our ability to delete parameters from a tool invocation.
     * 
     * Here the DataFederation tool is used and we delete one of its votable input references. 
     * Rather counter-intuitively, we delete using value as the search focus. However, this 
     * works well and is extremely easy to use.
     *  
     */
    public void testDeleteParameter() {
        logger.info( "----------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testDeleteParameter()" ); 

        final String 
            workflowName = "DeleteParameter_" + this.runDate.getTime(),
            description = "One step DataFederation workflow where one parameter gets deleted",
            templateName = "OneStepJob"  ; 
              
 
        final String
            votableLocation[] = { Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable01.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable02.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable03.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable04.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable05.xml" ) } ;
        final String
            votOutputLocation = Workflow.formatMySpaceURL( communitySnippet(), "votables", "outputXS6wf01.xml") ;
            
        Workflow
             workflow = null ;            
        Sequence
            sequence = null ;
        ActivityIterator
            iterator = null ;
        IActivity
            activity = null ;
        IStep
            step = null ;
        String
            jesXML = null ;
            
        try {
            
            workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                          , workflowName
                                                          , description
                                                          , templateName ) ;
        
            sequence = (Sequence)workflow.getChild() ;            
            iterator = sequence.getChildren() ;
             
            while( iterator.hasNext() ) {
                activity = iterator.next() ;
                if( activity instanceof IStep ) {
                    step = (IStep)activity ;
                    break ;
                }
            }
            
            this.setUpDataFederationStep( step
                                        , votableLocation
                                        , true
                                        , "some sort of show string here"
                                        , "some sort of target string here"
                                        , "some sort of what2show string here"
                                        , "" // empty optional parameter ("require")
                                        , "some sort of output string here"
                                        , 5
                                        , votOutputLocation ) ;
                                        
            // Here is the test: deletion of the 3rd votable input parameter...
            Workflow.deleteParameter( step.getTool()
                                    , "VOTfiles"            // name of parameter
                                    , votableLocation[2]    // its value is used to find it
                                    , "input") ;            // an input parameter
 
            jesXML = workflow.constructJESXML( communitySnippet() ) ;
            prettyPrint( "JES string:", jesXML ) ;
            
            if( jesXML.indexOf( votableLocation[2] ) == -1 ) {
                assertTrue( true ) ;
            }
            else {
                assertTrue( false ) ;
            }
            
        }
        catch( Exception ex ){
            ex.printStackTrace() ;
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testDeleteParameter()" );   
        }
        
    } // end of testDeleteParameter()


    /**
     * Tests our ability to insert a value into a parameter.
     * 
     * Here the DataFederation tool is used and we insert a value into its optional parameter. 
     * Note how old value is used as the search reference. In this case the old value is 
     * empty, meaning insertion (the parameter must indeed be empty for this to work). 
     * The same technique can be used for replacing values.
     */
    public void testInsertParameterValue() {
        logger.info( "----------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testInsertParameterValue()" ); 
  
        final String 
            workflowName = "InsertParameterValue_" + this.runDate.getTime(),
            description = "One step DataFederation workflow where a parameter value is inserted" ,
            templateName = "OneStepJob"  ; 
              
 
        final String
            votableLocation[] = { Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable01.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable02.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable03.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable04.xml" )
                                , Workflow.formatMySpaceURL( communitySnippet(), "votables", "votable05.xml" ) } ;
        final String
            votOutputLocation = Workflow.formatMySpaceURL( communitySnippet(), "votables", "outputXS6wf01.xml") ;
            
        Workflow
             workflow = null ;            
        Sequence
            sequence = null ;
        ActivityIterator
            iterator = null ;
        IActivity
            activity = null ;
        IStep
            step = null ;
        String
            jesXML = null ;
            
        try {
            
            workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                          , workflowName
                                                          , description
                                                          , templateName ) ;
        
            sequence = (Sequence)workflow.getChild() ;            
            iterator = sequence.getChildren() ;
             
            while( iterator.hasNext() ) {
                activity = iterator.next() ;
                if( activity instanceof IStep ) {
                    step = (IStep)activity ;
                    break ;
                }
            }
            
            this.setUpDataFederationStep( step
                                        , votableLocation
                                        , true
                                        , "some sort of show string here"
                                        , "some sort of target string here"
                                        , "some sort of what2show string here"
                                        , "" // empty optional parameter ("require")
                                        , "some sort of output string here"
                                        , 5
                                        , votOutputLocation ) ;
                                        
            // Here is the test: insertion of value in the empty optional parameter...
            Workflow.insertParameterValue( step.getTool()
                                         , "require" // parameter name
                                         , ""        // empty old value                                           
                                         , "a suitable require string" // some new value
                                         , "input" ) ; // input parameter
 
            jesXML = workflow.constructJESXML( communitySnippet() ) ;
            prettyPrint( "JES string:", jesXML ) ;
            
            if( jesXML.indexOf( "a suitable require string" ) > 0 ) {
                assertTrue( true ) ;
            }
            else {
                assertTrue( false ) ;
            }
            
        }
        catch( Exception ex ){
            ex.printStackTrace() ;
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testInsetParameterValue()" );   
        }
        
    } // end of testInsetParameterValue()





    /**
     * Alter this to provide suitable test values for the community snippet.
     * Some realistic looking value for account details is a mandatory minimum.
     */
    private String communitySnippet(){
        return CommunityMessage.getMessage( "1234"                         // token
                                          , WorkflowTestSuite.ACCOUNT    // account
                                          , WorkflowTestSuite.GROUP      // group
                                          ) ;
    }


    /**
     * Helper method which sets up HyperZ specific job step details.
     */ 
    private IStep setUpHyperZStep( IStep targetStep
                                , String config_file
                                , String input_catalog
                                , String output_catalog ) {
        logger.info( "enter: WorkflowTestSuite.setUpHyperZStep()" );
       
        Iterator iterator ;
        
        try {
            
            Tool tool = Workflow.createTool( communitySnippet(), "HyperZ" ) ;
            IParameter p ;
            String pName ;
            
            iterator = tool.getInputParameters() ;            
            while( iterator.hasNext() ) {
                
                p = (IParameter)iterator.next() ;
                pName = p.getName() ;
                
                if( pName.equals("config_file") ) {
                    p.setValue( config_file ) ;
                }
                else if( pName.equals( "input_catalog") ) {
                    p.setValue( input_catalog ) ;
                }

            }
            
            iterator = tool.getOutputParameters() ;            
            while( iterator.hasNext() ) {
                p = (IParameter)iterator.next() ;
                if( p.getName().equals("output_catalog") )
                   p.setValue( output_catalog ) ;
                   break ;
            }  
            
            targetStep.setTool( tool ) ;                                        
                                                                
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.setUpHyperZStep()" );  
        }
        
        return targetStep ;
        
    } // end of setUpHyperZStep()
    
    
    /**
     * Helper method which sets up SExtractor specific job step details.
     */ 
    private IStep setUpSExtractorStep( IStep targetStep
                                    , String detectionImage
                                    , String config_file
                                    , String parameters_name
                                    , String catalog_name ) {
        logger.info( "enter: WorkflowTestSuite.setUpSExtractorStep()" );
       
        Iterator iterator ;
        
        try {
            
            Tool tool = Workflow.createTool( communitySnippet(), "SExtractor" ) ;
            IParameter p ;
            String pName ;
            
            iterator = tool.getInputParameters() ;            
            while( iterator.hasNext() ) {
                
                p = (IParameter)iterator.next() ;
                pName = p.getName() ;
                
                if( pName.equals("DetectionImage") ) {
                    p.setValue( detectionImage ) ;
                }
                else if( pName.equals( "config_file") ) {
                    p.setValue( config_file ) ;
                }
                else if( pName.equals( "PARAMETERS_NAME" ) ) {
                    p.setValue( parameters_name ) ;
                }

            }
            
            iterator = tool.getOutputParameters() ;            
            while( iterator.hasNext() ) {
                p = (IParameter)iterator.next() ;
                if( p.getName().equals("CATALOG_NAME") )
                   p.setValue( catalog_name ) ;
                   break ;
            }  
            
            targetStep.setTool( tool ) ;                                        
                                                                
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.setUpSExtractorStep()" );  
        }
        
        return targetStep ;
        
    } // end of setUpSExtractorStep()
    
    
    /**
     * Helper method which sets up DataFederation specific job step details.
     * Note: this shows how to setup added optional input parameters.
     * 
     */ 
    private IStep setUpDataFederationStep( IStep targetStep
                                        , String [] votables
                                        , boolean mergeCols
                                        , String show
                                        , String target
                                        , String what2show
                                        , String require
                                        , String output
                                        , int maxent
                                        , String merged_output ) {
        logger.info( "enter: WorkflowTestSuite.setUpDataFederationStep()" );
       
        Iterator iterator ;
        
        try {
            
            Tool tool = Workflow.createTool( communitySnippet(), "DataFederation" ) ;
            IParameter p ;
            int votablesIndex = 0 ;
            String pName ;
            
            iterator = tool.getInputParameters() ;            
            while( iterator.hasNext() ) {
                
                p = (IParameter)iterator.next() ;
                pName = p.getName() ;
                
                // There can be a variable number of VOTfiles.
                // The initial setup assumes there are only 2
                // ie: the minimum number...
                if( pName.equals("VOTfiles") ) {
                    p.setValue( votables[votablesIndex] ) ;
                    votablesIndex++ ;
                }
                else if( pName.equals( "mergeCols") ) {
                    p.setValue( new Boolean( mergeCols ).toString() ) ;
                }
                else if( pName.equals( "show" ) ) {
                    p.setValue( show ) ;
                }
                else if( pName.equals( "target" ) ) {
                    p.setValue( "target" ) ;
                }
                else if( pName.equals( "what2show") ) {
                    p.setValue( what2show ) ;
                }
                else if( pName.equals( "require" ) ) {
                    p.setValue( require ) ;
                }
                else if( pName.equals( "output" ) ) {
                    p.setValue( "output" ) ;
                }
                else if( pName.equals( "maxent" ) ) {
                    p.setValue( new Integer( maxent ).toString() ) ;
                }

            }
            
            iterator = null ; 
            
            // Here are the additional optional input parameters.
            // This sets up the VOTfiles beyond the minimum 2...
            for( votablesIndex=2; votablesIndex < votables.length; votablesIndex++ ) {
                p = tool.newInputParameter( "VOTfiles" ) ;
                p.setValue( votables[votablesIndex] ) ;
            }
            
            iterator = tool.getOutputParameters() ;            
            while( iterator.hasNext() ) {
                p = (IParameter)iterator.next() ;
                if( p.getName().equals("merged_output") )
                   p.setValue( merged_output ) ;
                   break ;
            }  
            
            targetStep.setTool( tool ) ;                                        
                                                                
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.setUpDataFederationStep()" );  
        }
        
        return targetStep ;
        
    } // end of setUpDataFederationStep()
    
    
	/** 
	 * Assembles and returns a test suite for
	 * all the test methods of this test case.
	 *
	 * @return A non-null test suite.
	 */
	public static Test suite() {
		// Reflection is used here to add all the testXXX() methods to the suite.
		return new TestSuite( WorkflowTestSuite.class ) ;
	}


	/**
     * Runs the test case.
     */
    public static void main( String args[] )  {

//		PropertyConfigurator.configure( log4jproperties ) ;
			
	   logger.info("Entering WorkflowTestSuite application.");
       junit.textui.TestRunner.run( suite() ) ;
	   logger.info("Exit WorkflowTestSuite application.");
		
    }
    
    /**
     * 
     * Pretty print to the console of an xml document.
     *
     * @param comment - some suitable comment of what the print relates to.
     * @param xmlString - the xml document in the form of a String
     */
    public static void prettyPrint( String comment, String xmlString ) {
        
        try {
            System.out.println( comment + "\n" ) ;
            InputSource
                source = new InputSource( new StringReader( xmlString ) );
            Document
                doc = XMLUtils.newDocument( source ) ;
            XMLUtils.PrettyDocumentToStream( doc, System.out ) ;
        }
        catch( Exception sax ) {
            sax.printStackTrace() ;
        }
          
    }
    
} // end of class WorkflowTestSuite