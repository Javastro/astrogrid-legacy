/**
 * 
 */
package org.astrogrid.desktop.modules.ui.lookout;

import javax.swing.JTextPane;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.MessageContainer;
/** display a message in a text pane */
public class MessageDisplayPane extends JTextPane {
    public MessageDisplayPane() {
        setContentType("text/html");
        setEditable(false);
    }
    
    public void clear() {       
        setText("");
    }
    
    public void setMessage(MessageContainer m) {
        setText(fmt(m));
        setCaretPosition(0);
    }
    
    private String fmt(MessageContainer m) {
        StringBuffer sb = new StringBuffer();
        ExecutionMessage message = m.getMessage();
        sb.append("<html><p bgcolor='#CCDDEE'>")
        .append("<b>Subject:</b> ").append(m.getSummary()).append("<br>")
        .append("<b>Date: </b> " ).append(message.getTimestamp()).append("<br>")
        .append("<b>From: </b> ").append(message.getSource()).append("<br></p><tt>")
        .append( //@todo - work out how to preserve space indentation here..
                
                StringUtils.replace(
                        StringEscapeUtils.escapeHtml(message.getContent())
                        ,"\n"
                        ,"<br>"
                )
        )
        .append("</tt></html>");
        return sb.toString();
    }
}