package org.astrogrid.portal.myspace.acting.framework;

import java.io.IOException;
import java.util.Stack;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.astrogrid.portal.utils.acting.ActionUtils;

/**
 * Create a new environment context based on a given protocol.
 * 
 * @author peter.shillan
 */
public class ContextWrapperFactory {
  private static Stack MOCK_STACK = new Stack();
  
  /**
   * Create a new environment context based on a given protocol.
   * 
   * @param protocol context wrapper protocol
   * @param utils action utilities
   * @param params sitemap parameters
   * @param request <b>Cocoon</b> request
   * @param session <b>Cocoon</b> session
   * @return environment context
   * @throws IOException
   */
  public static ContextWrapper getContextWrapper(
      String protocol,
      ActionUtils utils,
      Parameters params,
      Request request,
      Session session) throws IOException {
    if(MOCK_STACK.size() == 0) {
      return new ContextWrapperImpl(utils, params, request, session);
    }
    
    return (ContextWrapper) MOCK_STACK.pop(); 
  }
  
  /**
   * Facilitates unit testing.
   * <b>DO NOT USE</b> outside of a test case.
   */
  public static void addMock(ContextWrapper mockContext) {
    MOCK_STACK.add(mockContext);
  }

  /**
   * No instances allowed.
   */
  private ContextWrapperFactory() {
  }
}
