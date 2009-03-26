/**
 * 
 */
package org.jdesktop.swinghelper.debug;

import javax.swing.JComponent;

import junit.framework.Assert;

/** repaint manager for use in JUnit testing - on invalid component, fails.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 31, 200712:06:58 PM
 */
public class JunitCheckThreadViolationRepaintManager extends
        CheckThreadViolationRepaintManager {

    public JunitCheckThreadViolationRepaintManager() {
        super();
    }

    public JunitCheckThreadViolationRepaintManager(boolean completeCheck) {
        super(completeCheck);
    }
    
    @Override
    protected void violationDetected(JComponent c,
            StackTraceElement[] stackTrace) {
        super.violationDetected(c, stackTrace);
        component = c; // store for later.
        Assert.fail("EDT violation detected:" + c);
    }
    
    private static JComponent component;
    
    // checks for a violation, throwing an Assert.fail if sometiubg was amiss.
    // necessary to check manually as the violation might have been detected on a differtent thread.
    public static void checkAndClearFailure() {
        if (component != null) {
            JComponent c = component;
            component = null;
            Assert.fail("EDT violation detected:" + c);            
        }
    }

}
