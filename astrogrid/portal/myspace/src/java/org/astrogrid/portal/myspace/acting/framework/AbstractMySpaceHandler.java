package org.astrogrid.portal.myspace.acting.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements a base class which all MySpace handlers <b>MUST</b> inherit.
 * 
 * <p>
 * The class uses the <b>Template Method</b> pattern to factor out all common
 * functionality from handlers and put it into its own <code>execute()</code>
 * method.  All subclasses must implement the constructor and the <code>
 * executeTemplateMethod()</code> to perform their work.
 * </p>
 * 
 * @author peter.shillan
 */
public abstract class AbstractMySpaceHandler implements MySpaceHandler {
  protected ContextWrapper context;
  
  /**
   * Stores the environmental context for later use.
   * 
   * @param context wrapper class that provides access to the environment.
   */
  protected AbstractMySpaceHandler(ContextWrapper context) {
    this.context = context;
  }
    
  /**
   * Execute the handler's <code>executeTemplateMethod()</code> providing a
   * generic error handling and reporting mechanism.
   * 
   * <p>
   * Subclasses can use the <code>results</code> map to report back to the
   * caller and any exceptions thrown will result in a <code>null</code>
   * return (meaning <b>ERROR</b> in the sitemap) and a local attribute
   * holding the error message.
   * </p>
   */
  public final Map execute() {
    HashMap results = new HashMap();
    
    final String className = getClass().getName();
    try{
      executeTemplateMethod(results);
      
      // Nothing thrown means success.
      addLocalResult(MySpaceHandler.PARAM_ACTION, "true", results);
      addLocalResult(MySpaceHandler.PARAM_ACTION_HANDLER, className, results);
    }
    catch(Throwable t) {
      context.setLocalAttribute(MySpaceHandler.PARAM_ACTION, "false");
      context.setLocalAttribute(MySpaceHandler.PARAM_ACTION_HANDLER, className);
      context.setLocalAttribute(MySpaceHandler.PARAM_ACTION_ERR_MSG, t.getLocalizedMessage());
      
      results = null;
    }
    
    return results;
  }
  
  /**
   * Convenience method for adding a local attribute and a result at the same
   * time.
   * 
   * @param name name of result attribute
   * @param value value of result attribute
   * @param results map of results to use
   */
  public void addLocalResult(String name, String value, Map results) {
    context.setLocalAttribute(name, value);
    results.put(name, value);
  }
  
  /**
   * Convenience method for adding a global attribute and a result at the same
   * time.
   * 
   * @param name name of result attribute
   * @param value value of result attribute
   * @param results map of results to use
   */
  public void addGlobalResult(String name, String value, Map results) {
    context.setGlobalAttribute(name, value);
    results.put(name, value);
  }
  
  /**
   * Template method which does the subclass-specific work.
   * 
   * @param results attributes which will be returned can be set here
   * @throws Throwable
   */
  abstract protected void executeTemplateMethod(Map results) throws Throwable;
}
