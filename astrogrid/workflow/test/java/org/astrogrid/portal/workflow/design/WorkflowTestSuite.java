package org.astrogrid.portal.workflow.design ;

import org.astrogrid.i18n.*;
import org.astrogrid.AstroGridException ;
import java.util.Iterator;
import junit.framework.Test; 
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Logger;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.community.common.util.CommunityMessage;
import java.util.Date ;
import org.astrogrid.portal.workflow.design.activity.*;
import org.astrogrid.portal.workflow.design.Step ; 
import org.w3c.dom.*;
import org.xml.sax.InputSource ;
import java.io.StringReader ; 
import java.util.ListIterator ;

public class WorkflowTestSuite extends TestCase {
	
	private static Logger 
		logger = Logger.getLogger( WorkflowTestSuite.class ) ;
		
	private static final String
        log4jproperties = "/home/jl99/log4j.properties" ;	
        
    private String
        lastQueryNameReadFromList = "",
        lastWorkflowNameReadFromList = "",
        nameOfWorkflowSavedInMySpace = "";

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
         Sequence
            sequenceActivity = null ;
         Step
            stepActivity = null ;
            
         try{
             workflow = Workflow.createWorkflowFromTemplate( communitySnippet()
                                                           , name
                                                           , description
                                                           , templateName ) ;
             
             if( (workflowActivity = (Workflow)workflow.getActivity( "0" )) == null ) {
                 logger.info( "workflowActivity is null" ) ;
             }
             if( (sequenceActivity = (Sequence)workflow.getActivity( "1" )) == null ) {
                 logger.info( "sequenceActivity is null" ) ;
             } 
             if( (stepActivity = (Step)workflow.getActivity( "2" )) == null ) {
                 logger.info( "stepActivity is null" ) ;
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
    
    

    public void testCreateWorkflowFromTemplate_MissingTemplate() {
        logger.info( "-------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testCreateWorkflowFromTemplate_MissingTemplate()" ); 
        
        final String 
            name = "WorkflowFromTemplate_MissingTemplate_" + this.runDate.getTime() ,
            description = "Workflow should fail because of missing template",
            templateName = "IncorrectName" ;
            
        try{
            Workflow.createWorkflowFromTemplate( communitySnippet()
                                               , name
                                               , description
                                               , templateName ) ;
//          logger.info( "testQueryToString_CONE: " + resultString ) ;
//            assertTrue( resultString.equals( sqlString ) ) ;    
        }
        catch( Exception ex ) {
            assertTrue( false ) ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testCreateWorkflowFromTemplate_MissingTemplate()" );  
        }
        
    } // end of testCreateWorkflowFromTemplate_MissingTemplate()


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
        Step
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
            step.setJoinCondition( JoinCondition.ANY ) ;
            
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
        Step step = null ;
        int i = 0 ; // The workflow step number
        String wfXML = null ;            
                       
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
                step.setJoinCondition( JoinCondition.ANY ) ; 
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
            step.setJoinCondition( JoinCondition.TRUE ) ; 
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
            step.setJoinCondition( JoinCondition.TRUE ) ; 
            // Setting sequence and step numbers shows a weakness in the current approach.
            // These should be auto-generated in some fashion...
            step.setSequenceNumber( 3 ) ;
            step.setStepNumber( i+2 ) ; 
            this.setUpHyperZStep( step
                                , hyperzConfigFileLocation
                                , votOutputLocation
                                , hyperzOutputLocation ) ; 
            
            wfXML = workflow.constructWorkflowXML( communitySnippet() ) ;
            logger.info( "---------------------------------------------------------------------------------" ); 
            prettyPrint( "Workflow xml:", wfXML ) ;
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
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testSaveWorkflow()" );  
         }
        
     } // end of testSaveWorkflow()




    public void testReadWorkflowList() {
         logger.info( "-----------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testReadWorkflowList()" ); 
        
         Iterator
            iterator ;
            
         try{
             iterator = Workflow.readWorkflowList( communitySnippet(), "*" ) ;
             
             while ( iterator.hasNext() ) {
                 this.lastWorkflowNameReadFromList = (String)iterator.next() ;
                 logger.info( "Workflow Name: " + this.lastWorkflowNameReadFromList ) ;
             }
 
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadWorkflowList()" );  
         }
        
     } // end of testReadWorkflowList()
 
 
 
    public void testReadWorkflow() {
         logger.info( "-------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testReadWorkflow()" ); 
        
         Workflow
            workflow ;
            
         try{
             workflow = Workflow.readWorkflow( communitySnippet()
                                             , this.lastWorkflowNameReadFromList ) ;
             prettyPrint( "Workflow:", workflow.constructWorkflowXML( communitySnippet() ) ) ;
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadWorkflow()" );  
         }
        
     } // end of testReadWorkflow()   
    
    

    public void testDeleteWorkflow() {
         logger.info( "---------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testDeleteWorkflow()" ); 
        
        boolean
            ret = false ;
            
        try{
            ret = Workflow.deleteWorkflow( communitySnippet()
                                         , nameOfWorkflowSavedInMySpace ) ;
            
            logger.info( "deleted: " + ret ) ;
 
            assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testDeleteWorkflow()" );  
         }
        
     } // end of testDeleteWorkflow()
    
    



    public void testReadQueryList() {
         logger.info( "--------------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testReadQueryList()" ); 
        
         Iterator
            iterator ;
                    
         try{
             iterator = Workflow.readQueryList( communitySnippet(), "*" ) ;
             
 //            logger.info( "iterator: " + iterator ) ;
 //            logger.info( "About to execute iterator: iterator.next()" ) ;
 //            logger.info( iterator.next().getClass().getName() ) ;
             
             while ( iterator.hasNext() ) {                
                 this.lastQueryNameReadFromList = (String)iterator.next() ;
                 logger.info( "Query Name: " + this.lastQueryNameReadFromList ) ;
             }
 
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadQueryList()" );  
         }
        
     } // end of testReadQueryList()
    
    
    
    public void testReadQuery() {
         logger.info( "----------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testReadQuery()" ); 
        
         String
             query ;
            
         try{
             query = Workflow.readQuery( communitySnippet(), this.lastQueryNameReadFromList ) ;
             prettyPrint( "Query looks like:", query ) ;
             assertTrue( true ) ;    
         }
         catch( Exception ex ) {
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadQuery()" );  
         }
        
     } // end of testReadQuery()
   
   
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
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testReadToolList()" );  
         }
        
     } // end of testReadToolList()
     
     
     
    public void testCreateTool() {
         logger.info( "-----------------------------------------" ); 
         logger.info( "enter: WorkflowTestSuite.testCreateTool()" ); 
        
         Iterator  
            iterator,
            iterator2 ;
         Tool
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
                                                          
            Activity
                activity = workflow.getChild() ;
                
            Tool
                tool = Workflow.createTool( communitySnippet()
                                          , "SExtractor" ) ;
                                          
            Iterator
                it = tool.getInputParameters() ;
            Parameter
                p ;
                
            while( it.hasNext() ) {
                p = (Parameter)it.next() ;
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
                p = (Parameter)it.next() ;
                if( p.getName().equals("CATALOG_NAME") ) {
                    p.setLocation( Workflow.formatMySpaceURL( communitySnippet()
                                                            , "catalogfiles"
                                                            , "catalog_12345" ) ) ;
                }
            }
            
                
            if( activity instanceof Sequence ) {
                Sequence
                    sequence = (Sequence) activity ;
                Step
                    step = (Step)sequence.getChildren().next() ;
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
             
             assertTrue( false ) ;
             ex.printStackTrace() ;
         }
         finally {
             logger.info( "exit: WorkflowTestSuite.testSubmitWorkflow()" );  
         }
        
     } // end of testSubmitWorkflow()





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
          Activity
              activity = null ;
          Step
              step = null ;
          ListIterator
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
             Parameter
                p ;
             
             while( listIt.hasNext() ) {
                 p = (Parameter)listIt.next() ;
                 if( p.getName().equals("query") )
                    p.setLocation( queryLocation ) ;
                    break ;
             } 
             
             listIt = tool.getOutputParameters() ;
             
             while( listIt.hasNext() ) {
                 p = (Parameter)listIt.next() ;
                 if( p.getName().equals("result") )
                    p.setLocation( votableLocation ) ;
                    break ;
             }                                          
                                                      
             
             sequence = (Sequence)workflow.getChild() ;            
             iterator = sequence.getChildren() ;
             
             while( iterator.hasNext() ) {
                 activity = iterator.next() ;
                 if( activity instanceof Step ) {
                     step = (Step)activity ;
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
             
              assertTrue( false ) ;
              ex.printStackTrace() ;
          }
          finally {
              logger.info( "exit: WorkflowTestSuite.testCreateQueryAndSubmitWorkflow()" );  
          }
        
      } // end of testCreateQueryAndSubmitWorkflow()
   
    
    public void testFormatMySpaceReference_withoutAccountDetails() {
        logger.info( "---------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testFormatMySpaceReference_withoutAccountDetails()" ); 
        
        String logicalDirectoryPath = "ag/votables" ;
        String fileName = "mySpecialVOTable.xml" ;
        String formattedRef ;
        String correctResponse = "myspace://jl99@star.le.ac.uk/serv1/ag/votables/mySpecialVOTable.xml" ;
        
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
    

    public void testFormatMySpaceReference_withAccountDetails() {
        logger.info( "------------------------------------------------------------------------" ); 
        logger.info( "enter: WorkflowTestSuite.testFormatMySpaceReference_withAccountDetails()" ); 
        
        String logicalDirectoryPath = "jl99@star.le.ac.uk/serv1/ag/votables" ;
        String fileName = "mySpecialVOTable.xml" ;
        String formattedRef ;
        String correctResponse = "myspace://jl99@star.le.ac.uk/serv1/ag/votables/mySpecialVOTable.xml" ;
         
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
        Activity
            activity = null ;
        Step
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
                if( activity instanceof Step ) {
                    step = (Step)activity ;
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
            
            if( jesXML.indexOf( "name=\"require\"" ) == -1 ) {
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
            logger.info( "exit: WorkflowTestSuite.testToJesXMLOnEmptyOptionalParameter()" );   
        }
        
    } // end of testToJesXMLOnEmptyOptionalParameter()




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
        Activity
            activity = null ;
        Step
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
                if( activity instanceof Step ) {
                    step = (Step)activity ;
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
            assertTrue( false ) ;
            ex.printStackTrace() ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testDeleteParameter()" );   
        }
        
    } // end of testDeleteParameter()



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
        Activity
            activity = null ;
        Step
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
                if( activity instanceof Step ) {
                    step = (Step)activity ;
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
            assertTrue( false ) ;
            ex.printStackTrace() ;
        }
        finally {
            logger.info( "exit: WorkflowTestSuite.testInsetParameterValue()" );   
        }
        
    } // end of testInsetParameterValue()






    private String communitySnippet(){
        return CommunityMessage.getMessage( "1234", "jl99@star.le.ac.uk", "xray@star.le.ac.uk" ) ;
    }


    // 
    private Step setUpHyperZStep( Step targetStep
                                , String config_file
                                , String input_catalog
                                , String output_catalog ) {
        logger.info( "enter: WorkflowTestSuite.setUpHyperZStep()" );
       
        ListIterator iterator ;
        
        try {
            
            Tool tool = Workflow.createTool( communitySnippet(), "HyperZ" ) ;
            Parameter p ;
            String pName ;
            
            iterator = tool.getInputParameters() ;            
            while( iterator.hasNext() ) {
                
                p = (Parameter)iterator.next() ;
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
                p = (Parameter)iterator.next() ;
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
    
    
















    // 
    private Step setUpSExtractorStep( Step targetStep
                                    , String detectionImage
                                    , String config_file
                                    , String parameters_name
                                    , String catalog_name ) {
        logger.info( "enter: WorkflowTestSuite.setUpSExtractorStep()" );
       
        ListIterator iterator ;
        
        try {
            
            Tool tool = Workflow.createTool( communitySnippet(), "SExtractor" ) ;
            Parameter p ;
            String pName ;
            
            iterator = tool.getInputParameters() ;            
            while( iterator.hasNext() ) {
                
                p = (Parameter)iterator.next() ;
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
                p = (Parameter)iterator.next() ;
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
    
    







    // 
    private Step setUpDataFederationStep( Step targetStep
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
       
        ListIterator iterator ;
        
        try {
            
            Tool tool = Workflow.createTool( communitySnippet(), "DataFederation" ) ;
            Parameter p ;
            int votablesIndex = 0 ;
            String pName ;
            
            iterator = tool.getInputParameters() ;            
            while( iterator.hasNext() ) {
                
                p = (Parameter)iterator.next() ;
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
            
            // This sets up the VOTfiles beyond the minimum 2...
            for( votablesIndex=2; votablesIndex < votables.length; votablesIndex++ ) {
                p = tool.newInputParameter( "VOTfiles" ) ;
                p.setValue( votables[votablesIndex] ) ;
            }
            
            iterator = tool.getOutputParameters() ;            
            while( iterator.hasNext() ) {
                p = (Parameter)iterator.next() ;
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