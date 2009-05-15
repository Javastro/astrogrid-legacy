package org.astrogrid.applications.authorization;

import static org.junit.Assert.*;

import java.security.GeneralSecurityException;

import javax.security.auth.x500.X500Principal;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.SimpleApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.security.SecurityGuard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit tests for CeaAuthenticatedAccessPolicy.
 *
 * @author Guy Rixon
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 15 May 2009
 */
public class CeaAuthenticatedAccessPolicyTest  {
  
  private static InternalCeaComponentFactory internal;
private ExecutionHistory eh;
private CeaAuthenticatedAccessPolicy sut;
private Application app;
private SecurityGuard sg;
  @BeforeClass
  public static void beforeClass()
  {
      ProtocolLibrary protocolLib = new DefaultProtocolLibrary(new Protocol[]{new FileProtocol()});
      internal = new InternalCeaComponentFactory(protocolLib, new InMemoryIdGen(), new TestAuthorityResolver());

  }
  
  @Before
  public void setUp() throws Exception {
      eh = new InMemoryExecutionHistory();
      sut = new CeaAuthenticatedAccessPolicy(eh);
   }
@Test
public void testInitAuthenticated() throws Exception {
    SecurityGuard sg = new SecurityGuard();
    sg.setX500Principal(new X500Principal("CN=foo"));
    sut.decide(CEAOperation.INIT,null, null,sg);
  }

@Test
  public void testInitAnonymous() throws Exception {
    SecurityGuard sg = new SecurityGuard();
    try {
      sut.decide(CEAOperation.INIT,null, null,sg);
      fail("Should not accept anonymous requests.");
    }
    catch (GeneralSecurityException gse) {
      // Expected.
    }
  }
  
@Test
  public void testExecuteAuthenticated() throws Exception {
    sg = new SecurityGuard();
    sg.setX500Principal(new X500Principal("CN=foo"));
    initApp();
    sut.decide(CEAOperation.EXECUTE, app.getId(), null, sg);
  }

private void initApp() throws 
        Exception {
    SimpleApplicationDescriptionLibrary lib = new org.astrogrid.applications.description.SimpleApplicationDescriptionLibrary( new MockNonSpringConfiguredConfig());
    ApplicationDescription appDesc = lib.getDescription(lib.getApplicationNames()[0]);
    assertNotNull(appDesc);
    Tool tool = new Tool();
    ListOfParameterValues input = new ListOfParameterValues();
    tool.setInput(input );
    ListOfParameterValues output = new ListOfParameterValues();
    tool.setOutput(output );
    app = appDesc.initializeApplication("foo",sg,tool);
    eh.addApplicationToCurrentSet(app);
}
 
@Test
  public void testExecuteAnonymous() throws Exception {
    sg = new SecurityGuard();
    sg.setX500Principal(new X500Principal("CN=foo"));
    initApp();
    try {
        SecurityGuard sganon = new SecurityGuard();
        sut.decide(CEAOperation.EXECUTE, app.getId(), null, sganon);
      fail("Should not accept anonymous requests.");
    } catch (GeneralSecurityException ex) {
      // Expected.
    }
  }
  
    public void testExecuteWrongUser() throws Exception {
        sg = new SecurityGuard();
        sg.setX500Principal(new X500Principal("CN=foo"));
        initApp();
    SecurityGuard sganon = new SecurityGuard();
    sganon.setX500Principal(new X500Principal("CN=bar"));
    try {
        sut.decide(CEAOperation.EXECUTE, app.getId(), null, sganon);
      fail("Should not accept requests other than from the job owner.");
    } catch (GeneralSecurityException ex) {
      // Expected.
    }
  }
  
}
