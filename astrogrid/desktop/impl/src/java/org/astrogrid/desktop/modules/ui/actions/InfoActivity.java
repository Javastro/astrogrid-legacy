/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.TreeBag;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;

import com.l2fprod.common.swing.JTaskPaneGroup;

/** information about the selection - a special kind of activity
 * @todo provide more metadata here.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20071:29:37 PM
 */
public class InfoActivity extends AbstractFileOrResourceActivity{

public InfoActivity() {
	typeField = new JTextArea();
	typeField.setRows(1);
	typeField.setEditable(false);
	typeField.setLineWrap(true);
	typeField.setWrapStyleWord(true);
	typeField.setVisible(false);
}

private JTextArea typeField;

private final Bag types = new TreeBag();

	public void noneSelected() {
		typeField.setVisible(false);
	}

	public void manyFilesSelected(FileObject[] l) {
		typeField.setVisible(true);
		types.clear();
		for (int i = 0; i < l.length; i++) {
				try {
					types.add(l[i].getType().getName());
				} catch (FileSystemException x) {
					logger.error("FileSystemException",x);
				}
		}
		fmtResult();		
	}

	public void manyResourcesSelected(Resource[] l) {
		typeField.setVisible(true);
		types.clear();
		for (int i = 0; i < l.length; i++) {
				types.add(ResourceFormatter.formatType(l[i].getType()));
		}
		fmtResult();		
	}

	private void fmtResult() {
		typeField.setText("Selected:");
		for (Iterator i = types.uniqueSet().iterator(); i.hasNext();) {
			String t = (String) i.next();
			int count = types.getCount(t);
			typeField.append("\n  " + count + " " + t);
		}
	}

	public void somethingElseSelected() {
		noneSelected();
	}	
	
	public void oneSelected(Resource r) {
		typeField.setText("Selection: " + ResourceFormatter.formatType(r.getType()));
		typeField.setVisible(true);
	}
	public void oneSelected(FileObject fo) {
		try {
			typeField.setText("Selection: " + fo.getType().getName());
			typeField.setVisible(true);
		} catch (FileSystemException x) {
			logger.error("FileSystemException",x);
		}
	}


	public void addTo(JTaskPaneGroup grp) {
		grp.add(typeField);
	}

	public void addTo(JMenu menu) {
		// do nothing
	}

	public void addTo(JPopupMenu menu) {
		// do nothing
	}




}
