/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swinghelper.debug;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.ref.WeakReference;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

/**
 * <p>This class is used to detect Event Dispatch Thread rule violations<br>
 * See <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">How to Use Threads</a>
 * for more info</p>
 * <p/>
 * <p>This is a modification of original idea of Scott Delap<br>
 * Initial version of ThreadCheckingRepaintManager can be found here<br>
 * <a href="http://www.clientjava.com/blog/2004/08/20/1093059428000.html">Easily Find Swing Threading Mistakes</a>
 * </p>
 *
 * @author Scott Delap
 * @author Alexander Potochkin
 * @author Noel Winstanley took a copy, loosened up some member variables so it can be used in unit testing, and removed Java-5isms.
 * and replaced sys.out with logging
 * https://swinghelper.dev.java.net/
 */
public class CheckThreadViolationRepaintManager extends RepaintManager {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(CheckThreadViolationRepaintManager.class);

    // it is recommended to pass the complete check  
    private boolean completeCheck = true;
    private WeakReference<JComponent> lastComponent;

    public CheckThreadViolationRepaintManager(boolean completeCheck) {
        this.completeCheck = completeCheck;
    }

    public CheckThreadViolationRepaintManager() {
        this(true);
    }

    public boolean isCompleteCheck() {
        return completeCheck;
    }

    public void setCompleteCheck(boolean completeCheck) {
        this.completeCheck = completeCheck;
    }

    @Override
    public synchronized void addInvalidComponent(JComponent component) {
        checkThreadViolations(component);
        super.addInvalidComponent(component);
    }

    @Override
    public void addDirtyRegion(JComponent component, int x, int y, int w, int h) {
        checkThreadViolations(component);
        super.addDirtyRegion(component, x, y, w, h);
    }

    protected void checkThreadViolations(JComponent c) {
        if (!SwingUtilities.isEventDispatchThread() && (completeCheck || c.isShowing())) {
            boolean repaint = false;
            boolean fromSwing = false;
            boolean imageUpdate = false;
         //   StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
         //NW - can't get trace from Thread in java 1.4, instead, create an exception, and get it from that.
            StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
         //   for (StackTraceElement st : stackTrace) {
            for (int i = 0; i < stackTrace.length ; i++) {
                StackTraceElement st = stackTrace[i];
                if (repaint && st.getClassName().startsWith("javax.swing.")) {
                    fromSwing = true;
                }
                if (repaint && "imageUpdate".equals(st.getMethodName())) {
                    imageUpdate = true;
                }
                if ("repaint".equals(st.getMethodName())) {
                    repaint = true;
                    fromSwing = false;
                }
            }
            if (imageUpdate) {
                //assuming it is java.awt.image.ImageObserver.imageUpdate(...) 
                //image was asynchronously updated, that's ok 
                return;
            }
            if (repaint && !fromSwing) {
                //no problems here, since repaint() is thread safe
                return;
            }
            //ignore the last processed component
            if (lastComponent != null && c == lastComponent.get()) {
                return;
            }
            lastComponent = new WeakReference<JComponent>(c);
            violationDetected(c, stackTrace);
        }
    }

    /** a violation has been detected, do with it what you will..
     * @param c
     * @param stackTrace
     */
    protected void violationDetected(JComponent c,
            StackTraceElement[] stackTrace) {
        Throwable t = new Throwable("EDT violation");
        t.setStackTrace(stackTrace);
        logger.error(c,t);
    }

    public static void main(String[] args) throws Exception {
        //set CheckThreadViolationRepaintManager 
        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
        //Valid code  
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                test();
            }
        });
        System.out.println("Valid code passed...");
        repaintTest();
        System.out.println("Repaint test - correct code");
        //Invalide code (stack trace expected) 
        test();
    }

    static void test() {
        JFrame frame = new JFrame("Am I on EDT?");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JButton("JButton"));
        frame.pack();
        frame.setVisible(true);
        frame.dispose();
    }

    //this test must pass
    static void imageUpdateTest() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JEditorPane editor = new JEditorPane();
        frame.setContentPane(editor);
        editor.setContentType("text/html");
        //it works with no valid image as well 
        editor.setText("<html><img src=\"file:\\lala.png\"></html>");
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    private static JButton test;
    static void repaintTest() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    test = new JButton();
                    test.setSize(100, 100);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } 
        // repaint(Rectangle) should be ok
        test.repaint(test.getBounds());
        test.repaint(0, 0, 100, 100);
        test.repaint();
    }
}