/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.xml.sax.SAXParseException;

/** Utility class that converts exceptions into astronomer-friendly messages.
 * 
 * Implementation is not thread safe, for efficiency.
 * Call static methods from EDT - for other threads, create your own instance and use that.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 21, 20071:21:24 PM
 */
public class ExceptionFormatter {
    
    /** format all exceptions in the cause chain */
    public static final int ALL = 0;
    /** format only the outermost exception - the default */
    public static final int OUTERMOST = 2;
    /** format only the innermost exception */
    public static final int INNERMOST = 1;
    
    private static final ExceptionFormatter instance = new ExceptionFormatter();
    /** safe to call from EDT. for use from other threads, create your own instance of this class */
    public static String formatException(Throwable ex) {
        return instance.format(ex);
    }

    /** safe to call from EDT. From other threads, create your own instance and use the member method */
    public static String formatException(Throwable ex,int strategy) {
        return instance.format(ex,strategy);
    }
    
    private final StrBuilder sb = new StrBuilder();
    
    /** format an exception, according to the provided strategy */
    public String format(Throwable ex,int strategy) {
        switch(strategy) {
            case OUTERMOST:
                return formatSingle(ex);
            case INNERMOST:
                while(ex.getCause() != null) {
                    ex = ex.getCause();
                }
                return formatSingle(ex);
            default:
                StrBuilder hb = new StrBuilder();
            do {
                hb.append(formatSingle(ex));
                hb.append("<br>");
                ex = ex.getCause();           
            } while(ex != null);
            return hb.toString();
        }
    }
    
    /** format an exception, using the default strategy (Outermost)*/
    public String format(Throwable ex) {
        return format(ex,OUTERMOST);
    }
    
    private String formatSingle(Throwable ex) {
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
        } else if (ex instanceof IOException) {
            return ex.getMessage();
        } else if (ex instanceof ServiceException) {
            return ex.getMessage();
        } else if (ex instanceof ACRException) { // should have clauses for subclasses here too.
            return ex.getMessage();        
        } else {
            sb.clear();
            sb.append(StringUtils.substringAfterLast(ex.getClass().getName(),"."));
            sb.append(" - ");
            sb.append(ex.getMessage());
            return sb.toString();     
        }
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
