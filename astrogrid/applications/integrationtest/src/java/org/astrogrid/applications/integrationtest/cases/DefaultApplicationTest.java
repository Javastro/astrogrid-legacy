package org.astrogrid.applications.integrationtest.cases;

import junit.framework.TestCase;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * An integration test for the CEA delegate and the CEA server-core.
 *
 * In this test, the delegate calls a CEA service at a given address and
 * invokes the application ivo://org.astrogrid.unregistered/default,
 * interface default, that is built into every CEA service; see
 * {@link org.astrogrid.applications.BuiltInApplication}.  The application
 * concatenates its input arguments as a string, with one space after each
 * argument, and echos them back as its output argument. The test succeeds if
 * the correct output argument is read back. 
 *
 * @author Guy Rixon
 */
public final class DefaultApplicationTest extends TestCase {
  
  /**
   * Tests the application against a real service.
   */
  public void testValidEndpoint() throws Exception {
    
    // The endpoint must be set in a system property.
    String serviceEndpoint = System.getProperty("cea.test.endpoint");
    assertNotNull("cea.test.endpoint", serviceEndpoint);
    
    // Create the delegate for the test.
    CommonExecutionConnectorClient delegate =
        DelegateFactory.createDelegate(serviceEndpoint);
    
    // Make the document that specifies the job to be run.
    Tool tool = this.createValidToolDocument();    
    this.assertTrue("Tool is valid", tool.isValid());
    
    // Make a job identifier. (This is a silly type: it's just a string.)
    // We shan't use this for the test so the value is arbitrary.
    JobIdentifierType foo = new JobIdentifierType("foo");
    
    // Initialize the job on the service.
    String executionId = delegate.init(tool, foo);
    this.assertNotNull("Execution ID is not null", executionId);
    
    // Start the job running.
    delegate.execute(executionId);
    
    // Poll for completion.
    // Eventually the job will complete or the job will fail or the 
    // delegate will throw an exception.
    MessageType status = delegate.queryExecutionStatus(executionId);
    while (status.getPhase() == ExecutionPhase.PENDING ||
           status.getPhase() == ExecutionPhase.INITIALIZING ||
           status.getPhase() == ExecutionPhase.RUNNING) {
      status = delegate.queryExecutionStatus(executionId);
    }
    
    // Check the results.
    if (status.getPhase() == ExecutionPhase.ERROR) {
      fail(status.getContent());
    }
    else {
      ResultListType results = delegate.getResults(executionId);
      ParameterValue out1 = results.getResult(0);
      assertEquals("123.456 foo bar baz", out1.getValue());
    }
  }
  
  /**
   * Creates a "tool" document for a valid request.
   */
  private Tool createValidToolDocument() {
    Tool tool = new Tool();
    tool.setName("org.astrogrid.unregistered/default");
    tool.setInterface("default");
    
    ParameterValue in1 = new ParameterValue();
    in1.setName("in1");
    in1.setValue("123.456");
    
    ParameterValue in2 = new ParameterValue();
    in2.setName("in2");
    in2.setValue("foo bar baz");
    
    ParameterValue out1 = new ParameterValue();
    out1.setName("out");
    out1.setValue("");
    
    Input in = new Input();
    in.addParameter(in1);
    in.addParameter(in2);
    tool.setInput(in);
    
    Output out = new Output();
    out.addParameter(out1);
    tool.setOutput(out);
    
    return tool;
  }
}
