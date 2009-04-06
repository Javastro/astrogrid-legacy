/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import net.sourceforge.hivelock.SecurityException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.scope.DalProtocolException;
import org.xml.sax.SAXParseException;

import com.l2fprod.common.swing.BaseDialog;
import com.l2fprod.common.swing.JLinkButton;

/** Converts exceptions into astronomer-friendly messages.
 * 
 * Implementation is not thread safe, for efficiency.
 * Call static methods from EDT - for other threads, create your own instance and use that.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 21, 20071:21:24 PM
 */
public class ExceptionFormatter {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ExceptionFormatter.class);
    
    /** format all exceptions in the cause chain */
    public static final int ALL = 0;
    /** format only the outermost exception - the default */
    public static final int OUTERMOST = 2;
    /** format only the innermost exception */
    public static final int INNERMOST = 1;
    
    private static final ExceptionFormatter instance = new ExceptionFormatter();
    /** safe to call from EDT. for use from other threads, create your own instance of this class */
    public static String formatException(final Throwable ex) {
        return instance.format(ex);
    }

    /** safe to call from EDT. From other threads, create your own instance and use the member method */
    public static String formatException(final Throwable ex,final int strategy) {
        return instance.format(ex,strategy);
    }
    
    private final StrBuilder sb = new StrBuilder(128);
    
    /** format an exception, according to the provided strategy */
    public String format(Throwable ex,final int strategy) {
        // handy to see what's getting thrown around
        logger.debug("Intercepted",ex); // just for debugging.
        switch(strategy) {
            case OUTERMOST:
                return formatSingle(ex);
            case INNERMOST:
                while(ex.getCause() != null) {
                    ex = ex.getCause();
                }
                return formatSingle(ex);
            default:
                final StrBuilder hb = new StrBuilder(128);
            do {
                hb.append(formatSingle(ex));
                hb.append("<br>");
                ex = ex.getCause();           
            } while(ex != null);
            return hb.toString();
        }
    }
    
    /** format an exception, using the default strategy (Outermost)*/
    public String format(final Throwable ex) {
        return format(ex,OUTERMOST);
    }
    
    private String formatSingle(final Throwable ex) {
        if (ex instanceof InvalidArgumentException) {
            sb.clear();
            sb.append("Insufficient data - ");
            sb.append(ex.getMessage());
            return sb.toString();            
        } else if (ex instanceof SAXParseException ) {
            sb.clear();
            sb.append("Malformed XML - ");
            sb.append(ex.getMessage());
            return sb.toString();
        } else if (ex instanceof DalProtocolException) {
            return ex.getMessage();
        } else if (ex instanceof ConnectException) { // subclass of socket exception
              return "Could not connect to service";
        } else if (ex instanceof NoRouteToHostException) {// subclass of socket exception
            return ex.getMessage();
        } else if (ex instanceof SocketException) {
            sb.clear();
            sb.append("Communication error - ");
            sb.append(ex.getMessage());
            return sb.toString();            
        } else if (ex instanceof FileNotFoundException) {
            sb.clear();
            sb.append("Could not read ");
            sb.append(ex.getMessage());
            return sb.toString();            
        } else if (ex instanceof UnknownHostException) {
            sb.clear();
            sb.append("Unknown service - ");
            sb.append(ex.getMessage());
            return sb.toString();              
        } else if (ex instanceof SecurityException) { // hivelock - only caused by login.
            return "You must be logged in to perform this operation";
        } else if (ex instanceof IOException) {
            return ex.getMessage();
        } else if (ex instanceof ServiceException) {
            return ex.getMessage();
        } else if (ex instanceof ACRException) { // should have clauses for subclasses here too.
            return ex.getMessage();
        } else if (ex instanceof OutOfMemoryError) {
            return "Out of Memory - consider increasing memory using the -Xmx flag. e.g. -Xmx256M";
        } else if (ex instanceof RuntimeException && ex.getCause() != null) { //always traverse a runtimeException
            return formatSingle(ex.getCause());
        } else {
            sb.clear();
            sb.append(StringUtils.substringAfterLast(ex.getClass().getName(),"."));
            sb.append(" - ");
            sb.append(ex.getMessage());
            return sb.toString();     
        }
    }

    /** static helper method - show a well-formatted error in a popup dialogue
     * <p/>
     * 
     * @deprecated classes that extend this class should call {@link #showError(String, Throwable)} instead
     * @todo hide visibility altogether, or add suitable replacement code elsehwere.
     */
    @Deprecated
    public static final void showError(final Component parent,final String msg, final Throwable e) {
        if (GraphicsEnvironment.isHeadless()) {
            logger.error(msg,e);
        }
        
        final BaseDialog bd = BaseDialog.newBaseDialog(parent);

        bd.setModal(false);
        bd.setTitle("An Error Occurred");
        bd.getBanner().setTitle(msg);
        bd.getBanner().setSubtitle("<html>" + formatException(e));        
        bd.getBanner().setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        bd.setDialogMode(BaseDialog.CLOSE_DIALOG);
        bd.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final JLinkButton lb = new JLinkButton("Show technical error report" + UIComponentMenuBar.ELLIPSIS);
        lb.setFont(UIConstants.SMALL_DIALOG_FONT);
        
        final JPanel content = (JPanel)bd.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(lb,BorderLayout.NORTH);
        final JEditorPane resultDisplay = new JEditorPane();
        resultDisplay.setContentType("text/html");
        resultDisplay.setEditable(false);
        resultDisplay.setBorder(null);
        resultDisplay.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);      // this key is only defined on 1.5 - no effect on 1.4
        resultDisplay.setFont(UIConstants.SANS_FONT);                       
        final JScrollPane sp = new JScrollPane(resultDisplay,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVisible(false);
        sp.setSize(500,400);
        sp.setPreferredSize(new Dimension(500,400));
        content.add(sp,BorderLayout.CENTER);
        lb.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ev) {
                final StringWriter sw = new StringWriter();        
                final PrintWriter pw = new PrintWriter(sw);
                pw.println("<html><body><pre>");
                pw.println("Date of Error: " + (new Date()).toString());
                if (parent != null) {
                    pw.println("Within component: " + parent.getClass().getName());
                }
                // maybe add more header info here - user, etc. - hard to get to.
                
                pw.println();
                e.printStackTrace(pw);
                
                if (parent != null && parent instanceof UIComponentImpl) {            
                    pw.println();
                    final UIComponent u = (UIComponent)parent;
                    try {
                        final Map<String, String> m = u.getContext().getConfiguration().list();
                        final Properties props = new Properties();
                        props.putAll(m);
                        // nggg. clunky.
                        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        props.save(bos,"Application Configuration");
                        pw.println(bos.toString());
                    } catch (final ACRException ex) {
                        pw.println("Failed to list configuration");
                        ex.printStackTrace(pw);
                    }
                }
                
                pw.println();   
                final Properties sysProps = System.getProperties();
                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                sysProps.save(bos,"System Properties");
                pw.println(bos.toString());
                
                pw.println();
                pw.println("If you think this is a bug in the Workbench, please email this transcript to");
                pw.println("astrogrid_help@star.le.ac.uk, along with details of your username and a description");
                pw.println("of what was happening at the time of the error");
                
                // finish off the report
                pw.println("</pre></body></html>");
                
                // display report in a dialogue
                resultDisplay.setText(sw.toString());
                resultDisplay.setCaretPosition(0);
                sp.setVisible(true);
                bd.pack();
            }
        });
       bd.pack();
       if (parent == null) {
           bd.centerOnScreen();
       } else {
           bd.setLocationRelativeTo(parent);
       }       
       bd.setVisible(true);
    }
    
    /* might need to get to the innermost one first - see if this seems to be needed.
     *         Throwable innermost = e;
        while (innermost.getCause() != null) {
            innermost = innermost.getCause();            
        }
        
        String eMsg = null;
        if (innermost.getMessage() != null) { 
            int endOfPrefix = innermost.getMessage().lastIndexOf("Exception:") ; // try to get all nested exception messages
            if (endOfPrefix > -1) {
                eMsg = innermost.getMessage().substring(endOfPrefix+ 10);
            } else {
                eMsg = innermost.getMessage();
            }
        } else { // an exception with no message.
            eMsg = innermost.getClass().getName(); 
        }
     */
    
}
