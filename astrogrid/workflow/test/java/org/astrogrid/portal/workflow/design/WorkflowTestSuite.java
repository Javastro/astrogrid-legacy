package org.astrogrid.portal.workflow.design;

import java.io.StringReader;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.astrogrid.AstroGridException;
import org.astrogrid.community.common.util.CommunityMessage;
import org.astrogrid.portal.workflow.WKF;
import org.astrogrid.portal.workflow.design.activity.Activity;
import org.astrogrid.portal.workflow.design.activity.ActivityIterator;

public class WorkflowTestSuite extends TestCase {

  private static Logger logger = Logger.getLogger(WorkflowTestSuite.class);

  private static final String log4jproperties = "/home/jl99/log4j.properties";

  /**
   * Sets up the test fixture.
   *
   * Called before every test case method.
   */
  protected void setUp() {
    try {
      logger.info("About to check properties loaded");
      WKF.getInstance().checkPropertiesLoaded();
      logger.info("Properties loaded OK");
    } catch (AstroGridException agex) {
      logger.info(agex.getAstroGridMessage().toString());
    }
  }

  /**
   * Tears down the test fixture.
   *
   * Called after every test case method.
   */
  protected void tearDown() {
  }

  public void tostWorkflowActivityNavigation() {
    logger.info("---------------------------------------------------------");
    logger.info("enter: WorkflowTestSuite.testWorkflowActivityNavigation()");

    final String name = "OneStepJob",
      description = "This is a one step job",
      templateName = "OneStepJob";
    Workflow workflow = null, workflowActivity = null;
    Sequence sequenceActivity = null;
    Step stepActivity = null;

    try {
      workflow =
        Workflow.createWorkflowFromTemplate(
          communitySnippet(),
          name,
          description,
          templateName);

      if ((workflowActivity = (Workflow) workflow.getActivity("0")) == null) {
        logger.info("workflowActivity is null");
      }
      if ((sequenceActivity = (Sequence) workflow.getActivity("1")) == null) {
        logger.info("sequenceActivity is null");
      }
      if ((stepActivity = (Step) workflow.getActivity("2")) == null) {
        logger.info("stepActivity is null");
      }

      logger.info(
        "Workflow: " + workflow.constructWorkflowXML(communitySnippet()));
      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testWorkflowActivityNavigation()");
    }

  } // end of testWorkflowActivityNavigation()

  public void tostCreateWorkflowFromTemplate_MissingTemplate() {
    logger.info(
      "-------------------------------------------------------------------------");
    logger.info(
      "enter: WorkflowTestSuite.testCreateWorkflowFromTemplate_MissingTemplate()");

    final String name = "OneStepJob",
      description = "This is a one step job",
      templateName = "IncorrectName";

    try {
      Workflow.createWorkflowFromTemplate(
        communitySnippet(),
        name,
        description,
        templateName);
      //          logger.info( "testQueryToString_CONE: " + resultString ) ;
      //            assertTrue( resultString.equals( sqlString ) ) ;    
    } catch (Exception ex) {
      assertTrue(false);
    } finally {
      logger.info(
        "exit: WorkflowTestSuite.testCreateWorkflowFromTemplate_MissingTemplate()");
    }

  } // end of testCreateWorkflowFromTemplate_MissingTemplate()

  public void tostCreateWorkflowFromTemplate_OneStepTemplate() {
    logger.info(
      "-------------------------------------------------------------------------");
    logger.info(
      "enter: WorkflowTestSuite.testCreateWorkflowFromTemplate_OneStepTemplate()");

    final String name = "OneStepJob",
      description = "This is a one step job",
      templateName = "OneStepJob";
    Workflow workflow = null;

    try {
      workflow =
        Workflow.createWorkflowFromTemplate(
          communitySnippet(),
          name,
          description,
          templateName);

      prettyPrint(
        "Workflow:",
        workflow.constructWorkflowXML(communitySnippet()));

      assertTrue(true);
    } catch (Exception ex) {
      assertTrue(false);
    } finally {
      logger.info(
        "exit: WorkflowTestSuite.testCreateWorkflowFromTemplate_OneStepTemplate()");
    }

  } // end of testCreateWorkflowFromTemplate_OneStepTemplate()

  public void testSaveWorkflow() {
    logger.info("-------------------------------------------");
    logger.info("enter: WorkflowTestSuite.testSaveWorkflow()");

    Date date = new Date();

    final String name =
      "WorkflowTestSuite_testSaveWorkflow()_" + date.getTime(),
      description = "This is a one step job",
      templateName = "OneStepJob";
    Workflow workflow = null;
    boolean rc = false;

    try {

      workflow =
        Workflow.createWorkflowFromTemplate(
          communitySnippet(),
          name,
          description,
          templateName);

      logger.info(
        "Workflow: " + workflow.constructWorkflowXML(communitySnippet()));

      logger.info("About to save");
      rc = Workflow.saveWorkflow(communitySnippet(), workflow);
      logger.info("MySpace says: " + rc);

      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testSaveWorkflow()");
    }

  } // end of testSaveWorkflow()

  public void testReadWorkflowList() {
    logger.info("-----------------------------------------------");
    logger.info("enter: WorkflowTestSuite.testReadWorkflowList()");

    Iterator iterator;

    try {
      iterator = Workflow.readWorkflowList(communitySnippet(), "*");

      while (iterator.hasNext()) {
        logger.info("Workflow Name: " + (String) iterator.next());
      }

      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testReadWorkflowList()");
    }

  } // end of testReadWorkflowList()

  public void testReadWorkflow() {
    logger.info("-------------------------------------------");
    logger.info("enter: WorkflowTestSuite.testReadWorkflow()");

    final String
    //             name = "OneStepJob" ;
    name = "WorkflowTestSuite_testSaveWorkflow()_1070024120435";
    Workflow workflow;

    try {
      workflow = Workflow.readWorkflow(communitySnippet(), name);
      prettyPrint(
        "Workflow:",
        workflow.constructWorkflowXML(communitySnippet()));
      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testReadWorkflow()");
    }

  } // end of testReadWorkflow()   

  public void tostDeleteWorkflow() {
    logger.info("---------------------------------------------");
    logger.info("enter: WorkflowTestSuite.testDeleteWorkflow()");

    final String name = "OneStepJob";
    boolean ret = false;

    try {
      ret = Workflow.deleteWorkflow(communitySnippet(), name);

      logger.info("deleted: " + ret);

      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testDeleteWorkflow()");
    }

  } // end of testDeleteWorkflow()

  public void tostReadQueryList() {
    logger.info("--------------------------------------------");
    logger.info("enter: WorkflowTestSuite.testReadQueryList()");

    Iterator iterator;

    try {
      iterator = Workflow.readQueryList(communitySnippet(), "*");

      //            logger.info( "iterator: " + iterator ) ;
      //            logger.info( "About to execute iterator: iterator.next()" ) ;
      //            logger.info( iterator.next().getClass().getName() ) ;

      while (iterator.hasNext()) {
        logger.info("Query Name: " + (String) iterator.next());
      }

      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testReadQueryList()");
    }

  } // end of testReadQueryList()

  public void tostReadQuery() {
    logger.info("----------------------------------------");
    logger.info("enter: WorkflowTestSuite.testReadQuery()");

    final String name = "query_20031008.xml";
    String query;

    try {
      query = Workflow.readQuery(communitySnippet(), name);
      prettyPrint("Query looks like:", query);
      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testReadQuery()");
    }

  } // end of testReadQuery()

  public void tostReadToolList() {
    logger.info("-------------------------------------------");
    logger.info("enter: WorkflowTestSuite.testReadToolList()");

    Iterator iterator;

    try {

      iterator = Workflow.readToolList(communitySnippet());

      //            logger.info( "iterator: " + iterator ) ;
      //            logger.info( "About to execute iterator: iterator.next()" ) ;
      //            logger.info( iterator.next().getClass().getName() ) ;

      while (iterator.hasNext()) {
        logger.info("Tool Name: " + (String) iterator.next());
      }

      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testReadToolList()");
    }

  } // end of testReadToolList()

  public void tostCreateTool() {
    logger.info("-----------------------------------------");
    logger.info("enter: WorkflowTestSuite.testCreateTool()");

    Iterator iterator, iterator2;
    Tool tool;
    Parameter param;

    try {

      iterator = Workflow.readToolList(communitySnippet());
      while (iterator.hasNext()) {
        tool =
          Workflow.createTool(communitySnippet(), (String) iterator.next());
        logger.info("===================>>");
        logger.info("Tool name: " + tool.getName());
        logger.info("Tool documenation: " + tool.getDocumentation());
        iterator2 = tool.getInputParameters();
        logger.info("InputParams... ");
        while (iterator2.hasNext()) {
          param = (Parameter) iterator2.next();
          logger.info(param.getName());
          logger.info(param.getDocumentation());
          logger.info(param.getType());
          logger.info(
            new Integer(param.getCardinality().getMinimum()).toString());
          logger.info(
            new Integer(param.getCardinality().getMaximum()).toString());
        }

        iterator2 = tool.getOutputParameters();
        logger.info("OutputParams... ");
        while (iterator2.hasNext()) {
          param = (Parameter) iterator2.next();
          logger.info(param.getName());
          logger.info(param.getDocumentation());
          logger.info(param.getType());
          logger.info(
            new Integer(param.getCardinality().getMinimum()).toString());
          logger.info(
            new Integer(param.getCardinality().getMaximum()).toString());
        }

      }
      logger.info("===================>>");
      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testCreateTool()");
    }

  } // end of testCreateTool()

  public void tostSubmitWorkflow() {
    logger.info("---------------------------------------------");
    logger.info("enter: WorkflowTestSuite.testSubmitWorkflow()");

    final String name = "WorkflowOct3",
      description = "This is a one step job",
      templateName = "OneStepJob";
    Workflow workflow = null;
    boolean rc = false;

    try {
      workflow =
        Workflow.createWorkflowFromTemplate(
          communitySnippet(),
          name,
          description,
          templateName);

      Activity activity = workflow.getChild();

      Tool tool = Workflow.createTool(communitySnippet(), "Sextractor");

      Iterator it = tool.getInputParameters();
      Parameter p;

      while (it.hasNext()) {
        p = (Parameter) it.next();
        if (p.getName().equals("image1")) {
          p.setLocation(
            Workflow.formatMySpaceURL(
              communitySnippet(),
              "imagefiles",
              "image1_12345"));
        } else if (p.getName().equals("config_file")) {
          p.setLocation(
            Workflow.formatMySpaceURL(
              communitySnippet(),
              "tools/sextractor",
              "extractor_config"));
        } else if (p.getName().equals("config_parameters")) {
          p.setLocation(
            Workflow.formatMySpaceURL(
              communitySnippet(),
              "tools/sextractor",
              "sextractor_parameters"));
        }
      }

      it = tool.getOutputParameters();

      while (it.hasNext()) {
        p = (Parameter) it.next();
        if (p.getName().equals("output")) {
          p.setLocation(
            Workflow.formatMySpaceURL(
              communitySnippet(),
              "votable/files",
              "catalog_12345"));
        }
      }

      if (activity instanceof Sequence) {
        Sequence sequence = (Sequence) activity;
        Step step = (Step) sequence.getChildren().next();
        step.setTool(tool);
      }

      prettyPrint(
        "Workflow:",
        workflow.constructWorkflowXML(communitySnippet()));
      logger.info("About to submit");
      prettyPrint("JES string:", workflow.constructJESXML(communitySnippet()));
      rc = Workflow.submitWorkflow(communitySnippet(), workflow);
      logger.info("JobController says: " + rc);

      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testSubmitWorkflow()");
    }

  } // end of testSubmitWorkflow()

  public void tostCreateQueryAndSubmitWorkflow() {
    logger.info("-----------------------------------------------------------");
    logger.info("enter: WorkflowTestSuite.testCreateQueryAndSubmitWorkflow()");

    Date date = new Date();

    final String workflowName = "WorkflowTest_" + date.getTime(),
      description = "This is a one step job submitted at " + date.toGMTString(),
      templateName = "OneStepJob",
      queryName = "query_20031008.xml";

    Workflow workflow = null;
    String query = null, queryLocation = null, votableLocation = null;
    Sequence sequence = null;
    boolean rc = false;
    ActivityIterator iterator = null;
    Activity activity = null;
    Step step = null;
    ListIterator listIt = null;

    try {
      workflow =
        Workflow.createWorkflowFromTemplate(
          communitySnippet(),
          workflowName,
          description,
          templateName);

      Tool tool = Workflow.createTool(communitySnippet(), "QueryTool");

      query = Workflow.readQuery(communitySnippet(), queryName);

      queryLocation =
        Workflow.formatMySpaceURL(communitySnippet(), "query", queryName);

      votableLocation =
        Workflow.formatMySpaceURL(
          communitySnippet(),
          "votables",
          "votable_0123.xml");

      listIt = tool.getInputParameters();
      Parameter p;

      while (listIt.hasNext()) {
        p = (Parameter) listIt.next();
        if (p.getName().equals("query"))
          p.setLocation(queryLocation);
        break;
      }

      listIt = tool.getOutputParameters();

      while (listIt.hasNext()) {
        p = (Parameter) listIt.next();
        if (p.getName().equals("result"))
          p.setLocation(votableLocation);
        break;
      }

      sequence = (Sequence) workflow.getChild();
      iterator = sequence.getChildren();

      while (iterator.hasNext()) {
        activity = iterator.next();
        if (activity instanceof Step) {
          step = (Step) activity;
          step.setTool(tool);
          break;
        }
      }

      prettyPrint("JES string:", workflow.constructJESXML(communitySnippet()));
      logger.info("About to submit");
      rc = Workflow.submitWorkflow(communitySnippet(), workflow);
      logger.info("JobController says: " + rc);

      assertTrue(true);
    } catch (Exception ex) {

      assertTrue(false);
      ex.printStackTrace();
    } finally {
      logger.info("exit: WorkflowTestSuite.testCreateQueryAndSubmitWorkflow()");
    }

  } // end of testCreateQueryAndSubmitWorkflow()

  private String communitySnippet() {
    return CommunityMessage.getMessage(
      "1234",
      "jl99@star.le.ac.uk",
      "xray@star.le.ac.uk");
  }

  /** 
   * Assembles and returns a test suite for
   * all the test methods of this test case.
   *
   * @return A non-null test suite.
   */
  public static Test suite() {
    // Reflection is used here to add all the testXXX() methods to the suite.
    return new TestSuite(WorkflowTestSuite.class);
  }

  /**
     * Runs the test case.
     */
  public static void main(String args[]) {

    //		PropertyConfigurator.configure( log4jproperties ) ;

    logger.info("Entering WorkflowTestSuite application.");
    junit.textui.TestRunner.run(suite());
    logger.info("Exit WorkflowTestSuite application.");

  }

  public static void prettyPrint(String comment, String xmlString) {

    try {
      System.out.println(comment + "\n");
      InputSource source = new InputSource(new StringReader(xmlString));
      Document doc = XMLUtils.newDocument(source);
      XMLUtils.PrettyDocumentToStream(doc, System.out);
    } catch (Exception sax) {
      sax.printStackTrace();
    }

  }

} // end of class WorkflowTestSuite