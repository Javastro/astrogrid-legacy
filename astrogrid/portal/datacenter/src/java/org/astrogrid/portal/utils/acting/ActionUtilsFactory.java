package org.astrogrid.portal.utils.acting;

import java.util.Stack;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class ActionUtilsFactory {
  private static Stack MOCK_STACK = new Stack();
   
  public static ActionUtils getActionUtils() {
    if(MOCK_STACK.size() == 0) {
      return new ActionUtilsDefault();
    }
    
    return (ActionUtils) MOCK_STACK.pop();
  }
  
  /**
   * Facilitates unit testing.
   * <b>DO NOT USE</b> outside of a test case.
   */
  public static void addMock(ActionUtils mockUtils) {
    MOCK_STACK.add(mockUtils);
  }
}
