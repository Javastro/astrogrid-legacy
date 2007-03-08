package org.astrogrid.desktop.modules.ui.lookout;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MessageRecorderImpl;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;

/** Reders Exeuction Informaiton objects into a folder tree
 * @author Noel Winstanley
 * @since Jun 16, 200611:47:18 PM
 */
public class FolderTreeCellRenderer extends DefaultTreeCellRenderer {
    private String calcColour(String status) {
        if ("ERROR".equalsIgnoreCase(status)) {
            return "red";
        } else if ("RUNNING".equalsIgnoreCase(status)) {
            return "green";
        } else if ("PENDING".equalsIgnoreCase(status) || "INITIALIZING".equalsIgnoreCase(status)) {
            return "blue";
        } else {
            return "black";
        }
    }

    private static final DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT);
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
	    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	    Folder f= (Folder)value;
	    ExecutionInformation info = f.getInformation();
	    StringBuffer sb = new StringBuffer();
	    sb.append("<html><p>").append(info.getDescription()).append("<br>").append(info.getId()).append("<br>");
	    if (info.getStartTime() != null) {
	        sb.append(df.format(info.getStartTime()));                        
	    }
	    if (info.getFinishTime() != null) {
	        sb.append(" - ").append(df.format(info.getFinishTime()));
	    }
	    sb.append("</p></html>");
	    setToolTipText(sb.toString());
	    sb = new StringBuffer();
	    sb.append("<html><font color='");
	    sb.append(calcColour(info.getStatus()));
	    sb.append("'>");
	    if (f.getUnreadCount() > 0) {
	        sb.append("<b>");
	    }
	    sb.append(info.getName());                    
	    if (f.getUnreadCount() > 0) {
	        sb.append(" ");
	        sb.append(f.getUnreadCount());
	        sb.append("</b>");
	    }                    
	    sb.append("</font></html>");
	    setText(sb.toString());
	    // finally set the icon.
	    if (info.getId().equals(MessageRecorderImpl.JOBS)) {
	        setIcon(IconHelper.loadIcon("workflow16.png"));
	    } else if (info.getId().equals(MessageRecorderImpl.QUERIES)) {
	        setIcon(IconHelper.loadIcon("search16.png"));
	    } else if (info.getId().equals(MessageRecorderImpl.TASKS)) {
	        setIcon(IconHelper.loadIcon("exec16.png"));    
	    } else if (info.getId().equals(MessageRecorderImpl.ROOT)) {
	        setIcon(IconHelper.loadIcon("run16.png"));
	    } else {
	        setIcon(IconHelper.loadIcon("idle16.png")); // todo - replace this with a representation of running status.
	    }
	    return this;
	}
}