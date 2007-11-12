/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/** implementation of the self tester.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 22, 20073:23:23 PM
 */
public class SelfTesterImpl implements SelfTester, Runnable {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SelfTesterImpl.class);

    // the suite of self-tests.
    private final TestSuite suite;
    private SelfTestDisplay theDisplay;
    
    // contents of the table model.
    private final EventList testResults = new BasicEventList();

    private final UIContext context;
    
    /**
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     * 
     */
    public SelfTesterImpl(final UIContext context,SchedulerInternal scheduler,List contribution,int delay)  {
        this.context = context;
        // assemble the test suite.
        logger.info("Assembling self test suite");
        this.suite = new TestSuite("Runtime self tests");
        for (Iterator i = contribution.iterator(); i.hasNext();) {
           Test t = (Test) i.next();
           if (t != null) { // possible to get a null suite if a contributing service is disabled.
            suite.addTest(t);
           }
        }
        logger.info(suite.countTestCases() + " self tests found");
        // set the tests to run, once, in a few seconds.
        scheduler.executeAfterDelay(1000 * delay,this);
        
    }
    
    /** runnable interface - runs the tests (before the ui is shown).
     *needs to be called on EDT, so use this to create the display first too.
     * on first error, ui will be displayed.
     */
    public void run() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                theDisplay = new SelfTestDisplay(context);
                theDisplay.actionPerformed(null);
            }
        });
    }
    // EDT-call enforced by hivemind.
    public void show() {
        theDisplay.show();
    }
    
    // ui component to display the results.
    private class SelfTestDisplay extends UIComponentImpl implements TestListener, ActionListener{

        // handle on the currently running test
        private SingleTestResult currentTest = null;
        // button to trigger a retest
        private final JButton retest;

        
        /**
         * @param context
         * @throws HeadlessException
         */
        public SelfTestDisplay(UIContext context) throws HeadlessException {
            super(context,"Self Testing","ui.selftest");
            
            // a table component, based on the testResults, where all updates occur on the EDT
            JTable table = new JTable(new EventTableModel(
                    GlazedListsSwing.swingThreadProxyList(testResults)
                    ,new SelfTestTableFormat()
                    ));
            table.getColumnModel().getColumn(0).setMaxWidth(10);
            
            setSize(600,400);
            JPanel pane = getMainPanel();
            pane.add(new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),BorderLayout.CENTER);
            retest = new JButton("Re-run self tests");
            pane.add(retest,BorderLayout.NORTH);
            retest.addActionListener(this);
            this.setTitle("Self Testing");
            // as this window can be re-shown, override the windowCLose op defined by parent class
            setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            // remove the window listener defined by the parent class.
            removeWindowListener(getWindowListeners()[0]);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    getContext().unregisterWindow(SelfTestDisplay.this);
                }
            });
            

        }

        public void setVisible(boolean b) {
            super.setVisible(b);
            if (b) {
                getContext().registerWindow(this);
            }
        }



        // callback when retest button pressed.
        public void actionPerformed(ActionEvent e) {
            logger.info("Running self tests");
            this.retest.setEnabled(false);
            try {
                testResults.getReadWriteLock().writeLock().lock();
                testResults.clear();
            } finally {
                testResults.getReadWriteLock().writeLock().unlock();
            }
            new BackgroundWorker(this,"Running self tests") {

                protected Object construct() throws Exception {
                    TestResult result = new TestResult();
                    result.addListener(SelfTestDisplay.this); // listener methods will be called on the BG thread, but changes appear in ui on EDT thread
                    suite.run(result);
                    return result;
                }
                protected void doFinished(Object result) {
                    TestResult tr= (TestResult)result;
                   setTitle("Self Testing - " + tr.runCount() + " tests run, " + tr.failureCount() + " failures, " + tr.errorCount() + " errors");
                }
                protected void doAlways() {
                    retest.setEnabled(true);              
                }
            }.start();
        }
        
        // test listener interface. build up the list of results.
        // assuming that all the tests in the suite are run sequentially in a single thread
        // which is the usual behaviour.
        public void startTest(Test arg0) {
            currentTest = new SingleTestResult();
            if (arg0 instanceof TestCase) {
                currentTest.name = ((TestCase)arg0).getName(); 
            } else if (arg0 instanceof TestSuite) {
                currentTest.name = ((TestSuite)arg0).getName();
            }
            logger.info( currentTest.name);
            try {
                testResults.getReadWriteLock().writeLock().lock();
                testResults.add(currentTest);
            } finally {
                testResults.getReadWriteLock().writeLock().unlock();
            }
        }

        public void endTest(Test arg0) {
            currentTest.completed = true;
            // fire another notification that this item has changes.
            try {
               testResults.getReadWriteLock().writeLock().lock();
               int pos = testResults.indexOf(currentTest);
               testResults.set(pos,currentTest);
            } finally {
                testResults.getReadWriteLock().writeLock().unlock();
            }
            // do some logging.
            if (currentTest.error != null) {
                logger.fatal(currentTest.name + " - error",currentTest.error);
            } else if (currentTest.failure != null) {
                logger.fatal(currentTest.name + " - failed - " + currentTest.failure.getMessage());
            } else {
                logger.info(currentTest.name + " - passed");
            }
            // don't need to hang onto this any longer.
            currentTest = null;
            
        }

        public void addError(Test arg0, Throwable arg1) {
            currentTest.error = arg1;
            showIfNotShowing();
        }
        
        public void addFailure(Test arg0, AssertionFailedError arg1) {
            currentTest.failure = arg1;
            showIfNotShowing();
        }

        
        private void showIfNotShowing() {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (! isVisible()) {
                        // at this point, should show the ui
                        setVisible(true);
                    }        
                }
            });
        }

    }
        /** represents result of a single test */
        private static class SingleTestResult {
            public String name;
            public boolean completed = false;
            public Throwable error;
            public AssertionFailedError failure;
            
        }
        /** format for the table. - works on a list of SingleTestResult*/
        private static class SelfTestTableFormat implements AdvancedTableFormat {

            private static final Icon OK_ICON = IconHelper.loadIcon("tick16.png");
            private static final Icon FAIL_ICON = IconHelper.loadIcon("no16.png");
            private static final Icon ERROR_ICON = IconHelper.loadIcon("no16.png");
            private static final Icon RUNNING_ICON = IconHelper.loadIcon("loader.gif");
            
            public int getColumnCount() {
                return 3;
            }

            public String getColumnName(int column) {
                switch(column) {
                    case 0: 
                        return "";
                    case 1:
                        return "Test Name";
                    case 2:
                        return "Details";
                    default:
                        return "unknown column";
                }
            }

            public Object getColumnValue(Object baseObject, int column) {
                SingleTestResult r = (SingleTestResult)baseObject;
                switch(column) {
                    case 0:
                        if (! r.completed) {
                            return RUNNING_ICON;
                        } else if (r.error != null) {
                            return ERROR_ICON;
                        } else if (r.failure != null) {
                            return FAIL_ICON;
                        } else {
                            return OK_ICON;
                        }
                    case 1:
                        return r.name;
                    case 2:
                        if (r.error != null) {
                            return r.error.getMessage();
                        } else if (r.failure != null) {
                            return r.failure.getMessage();
                        } else {
                            return "";
                        }
                     default:
                         return "unknown column";                                            
                }
            }

            public Class getColumnClass(int column) {
                if (column == 0) {
                    return Icon.class;
                } else {
                    return String.class;
                }
            }

            public Comparator getColumnComparator(int column) {
                if (column == 0) {
                    return null;
                } else {
                    return GlazedLists.caseInsensitiveComparator();
                }
            }
        }


        
}
