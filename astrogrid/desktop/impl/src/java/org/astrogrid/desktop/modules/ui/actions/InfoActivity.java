/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.TreeBag;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.fileexplorer.StorageTableFormat;

import com.l2fprod.common.swing.JTaskPaneGroup;

/** Disply information about the current selection.
 * A special kind of activity, that just fits into the activity framework.
 * @todo provide more metadata here.
 * @todo display a minimal icon, and make this a dragsource for the selection.
 * @todo consider replacin the text box with a list - maybe backed by a counting
 * event list or something.
 *  - then selections update more smoothly.
 *  @todo invesigate wther it's possible to get types of other kiinds of file objects - I beleiv that most getType is based on the filename, and so shoiuldn't cost
 *      - however, I think just the act of getContent() forces an attach() - would need to work around this.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20071:29:37 PM
 */
public class InfoActivity extends AbstractFileOrResourceActivity implements Activity.Info, Activity.NoContext{

public InfoActivity() {
    setHelpID("activity.selection");
	typeField = new JTextArea();
	typeField.setRows(1);
	typeField.setEditable(false);
	typeField.setLineWrap(true);
	typeField.setWrapStyleWord(true);
	typeField.setVisible(false);
	typeField.setFont(UIConstants.SMALL_DIALOG_FONT);
}

private final JTextArea typeField;

private final Bag types = new TreeBag();

	@Override
    public void noneSelected() {
		typeField.setVisible(false);
		super.noneSelected();
	}

	@Override
    public void manySelected(final FileObject[] l) {
		typeField.setVisible(true);
		types.clear();
		for (int i = 0; i < l.length; i++) {
				try {
				    types.add(StorageTableFormat.findBestContentType(l[i]));
				} catch (final FileSystemException x) {
					logger.error("FileSystemException",x);
				}
		}
		fmtResult();		
	}

	@Override
    public void someSelected(final Resource[] l) {
		typeField.setVisible(true);
		types.clear();
		for (int i = 0; i < l.length; i++) {
				types.add(PrettierResourceFormatter.formatType(l[i].getType()));
		}
		fmtResult();		
	}

	private void fmtResult() {
		typeField.setText("Selected:");
		for (final Iterator i = types.uniqueSet().iterator(); i.hasNext();) {
			final String t = (String) i.next();
			final int count = types.getCount(t);
			typeField.append("\n  " + count + " " + t);
		}
	}

	
	@Override
    public void oneSelected(final Resource r) {
		typeField.setText("Selection: " + PrettierResourceFormatter.formatType(r.getType()));
		typeField.setVisible(true);
	}
	@Override
    public void oneSelected(final FileObject fo) {
		try {
		    typeField.setText(StorageTableFormat.findBestContentType(fo));
		    typeField.setVisible(true);
		} catch (final FileSystemException x) {
			logger.error("FileSystemException",x);
		}
	}


	@Override
    public void addTo(final JTaskPaneGroup grp) {
	    typeField.setBackground(grp.getBackground());
		grp.add(typeField);
	}

	@Override
    public void addTo(final JMenu menu) {
		// do nothing
	}

	@Override
    public void addTo(final JPopupMenu menu) {
		// do nothing
	}

	// not used in this subclass.
    @Override
    protected boolean invokable(final FileObject f) {
        return false;
    }

    // not used in this subclass.
    @Override
    protected boolean invokable(final Resource r) {
        return false;
    }




}
