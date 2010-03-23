/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;


import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;
import org.astrogrid.desktop.modules.system.Tuple;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
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

private final ResourceSummarizer summarizer = new ResourceSummarizer();



	@Override
    public void noneSelected() {
		typeField.setVisible(false);
		super.noneSelected();
	}

	@Override
    public void manySelected(final FileObjectView[] l) {
		typeField.setVisible(true);
		summarizer.clear();
		for (final FileObjectView v: l) {
		    summarizer.add(v);
		}
		fmtResult();		
	}

	@Override
    public void someSelected(final Resource[] l) {
		typeField.setVisible(true);
		summarizer.clear();
		for (final Resource r: l) {
		    summarizer.add(r);
		}
		fmtResult();		
	}

	private void fmtResult() {
		typeField.setText("Selected:");
		for ( final Tuple<Integer, String> s : summarizer) {
	          typeField.append("\n  " + s.fst() + " " + s.snd());
		}
	}

	
	@Override
    public void oneSelected(final Resource r) {
		typeField.setText("Selection: " + PrettierResourceFormatter.formatResourceType(r));
		typeField.setVisible(true);
	}
	@Override
    public void oneSelected(final FileObjectView fo) {
		    typeField.setText(StorageTableFormat.findBestContentType(fo));
		    typeField.setVisible(true);
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
    protected boolean invokable(final FileObjectView f) {
        return false;
    }

    // not used in this subclass.
    @Override
    protected boolean invokable(final Resource r) {
        return false;
    }




}
