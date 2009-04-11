/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.SmartList;
import org.astrogrid.desktop.modules.ui.voexplorer.QuerySizeIndicator.QuerySizeListener;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.KeywordSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLParser;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SrqlQueryBuilderPanel;

import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** {@link EditingPanel} for smart lists.
 * @author Noel.Winstanley@manchester.ac.uk

 * @since Apr 26, 20077:21:19 PM
 */
public class SmartListEditingPanel extends EditingPanel implements ActionListener, ListEventListener, DocumentListener, QuerySizeListener {

	public SmartListEditingPanel(final UIComponent parent,final QuerySizer sizer) {
	    CSH.setHelpIDString(this,"reg.edit.smart");
        sizing = new QuerySizeIndicator(parent,sizer);
        sizing.addQuerySizeListener(this);
        
	    final FormLayout layout= new FormLayout(
				"2dlu, right:d, 1dlu, max(30dlu;d):grow, 4dlu, d, 1dlu, d, 3dlu" // cols
				,"d,top:d:grow,d,max(30dlu;d),d" // rows
		);
		final PanelBuilder builder = new PanelBuilder(layout,this);
		final CellConstraints cc = new CellConstraints();
		int row = 1;
		
		builder.addLabel("The search named:",cc.xy(2,row));
		folderName.setText("New Search");
		folderName.setColumns(20);
		builder.add(folderName,cc.xy(4,row));

		row++;
		qb = new SrqlQueryBuilderPanel();
		qb.getClauses().addListEventListener(this);
		flip.add(qb,QB);
		JLabel l = new JLabel("<html>The query is too complex to be edited in the form"
				+ "<br>Use the text-entry box below instead");
		flip.add(l,COMPLEX);
		l = new JLabel("<html>The query is either empty or cannot be parsed.<br> Provide a correct query using the text-entry box below");
		flip.add(l,ERROR);
		builder.add(flip,cc.xyw(2,row,7));

		row++;
		builder.addSeparator("Query Text",cc.xyw(2,row,7));

		row++;
		
		text= new JTextArea();
		text.setRows(5);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.getDocument().addDocumentListener(this);
		builder.add(new JScrollPane(text),cc.xyw(2,row,7));
		row++;

		builder.add(sizing,cc.xyw(2,row,4));
		builder.add(ok,cc.xy(6,row));
		builder.add(cancel,cc.xy(8,row));	
		ok.setEnabled(false); // we know the editor starts out with an invalid query.
		this.invalidate();
		
		this.addComponentListener(new ComponentAdapter() { // put focus in correct place on display
		    @Override
		    public void componentShown(final ComponentEvent e) {
		        folderName.selectAll();
		        folderName.requestFocusInWindow();   		        
		    
		    }
		});
	}

	private final JTextArea text;
	private final SrqlQueryBuilderPanel qb;
	private final FlipPanel flip = new FlipPanel();
	private final QuerySizeIndicator sizing;

	@Override
    public void setCurrentlyEditing(final ResourceFolder currentlyEditing) {
		if (! (currentlyEditing instanceof SmartList)) {
			throw new IllegalArgumentException("Not an instanceof SmartList");
		}
		super.setCurrentlyEditing(currentlyEditing);
		final SmartList sl = (SmartList)getCurrentlyEditing();
		if (sl.getQuery() != null) {
			final SRQL srql = sl.getQuery();
			sizing.setValue(srql);
			if (qb.canDisplayQuery(srql)) { // simple query
			        qb.setQuery(srql);
			        showForm();
			} else { // complex query
			    showTooComplex();
				final String qText = (String) srql.accept(vis);
				text.getDocument().removeDocumentListener(this); // temporarily remove listener, to prevent event-cycles. hope this isn't too expensive
				text.setText(qText);
				text.getDocument().addDocumentListener(this);
			}
		} else { // no query 
			qb.reset();
			showForm();
		}

	}
	/** load the edits made into the currentlyEdited resource */
	@Override
    public void loadEdits() {
		super.loadEdits();
		final SmartList sl = (SmartList)getCurrentlyEditing();		
		if (isFormShowing()) {
			sl.setQuery(qb.getQuery());
		} else if (isTooComplexShowing()){ // is a complex query, git it from the text box
			sl.setQuery(parseSrqlTextBox());
		}	else {
		    // error is showing - do nothing
		}
	}
// integration with QuerySizeIndicator..
	
// called when query building form alters.
	public void listChanged(final ListEvent arg0) {
		formTimer.restart();
	}
//	document listener interface	
	public void changedUpdate(final DocumentEvent e) {
		textTimer.restart();
	}

	public void insertUpdate(final DocumentEvent e) {
		textTimer.restart();
	}

	public void removeUpdate(final DocumentEvent e) {
		textTimer.restart();
	}
	
	//a timer that calls action-performed every half a second.
	// this timer is restarted every time the form or text box is altered.
	// so once 500ms have elapsed from an edit, things are in synch again
	// this approach ensures that app doesn't thrash with many intermediate changes.
	private final Timer formTimer = new Timer(500,this) {{
		setRepeats(false);
	}};
	private final Timer textTimer = new Timer(2000,this) {{
		setRepeats(false);
	}};

	// hooks things together - called by the timer - so once the use has finished editing.
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource().equals(formTimer)) {
			final SRQL query = qb.getQuery();
			// set this running straight away.
			sizing.setValue(query);
			final String qText = (String) query.accept(vis);
			text.getDocument().removeDocumentListener(this); // temporarily remove listener, to prevent event-cycles. hope this isn't too expensive
			text.setText(qText);
			text.getDocument().addDocumentListener(this);
		} else if (e.getSource().equals(textTimer)) {
			final SRQL srql = parseSrqlTextBox();
			if (srql == null) {
			    showError();
			} else {
				sizing.setValue(srql);
				if (qb.canDisplayQuery(srql)) {
					showForm();
					qb.getClauses().removeListEventListener(this); // temporarily stop listening.			
					qb.setQuery(srql);
					qb.getClauses().addListEventListener(this);				
				} else {
					showTooComplex();
				}
			}
		} 
	}	
	
	private boolean isFormShowing() {
	    return QB.equals(flip.getShowing())	 ;   
	}
	private boolean isErrorShowing() {
        return ERROR.equals(flip.getShowing())    ;   	    
	}
	private boolean isTooComplexShowing() {
        return COMPLEX.equals(flip.getShowing())   ;   	    
	}
	private void showForm() {
        flip.setShowing(QB);	    
        ok.setEnabled(shouldOkBeEnabled());
     
	}
	private void showError() {
	    flip.setShowing(ERROR);
	    ok.setEnabled(false);
	}
	private void showTooComplex() {
	    flip.setShowing(COMPLEX);
	    ok.setEnabled(shouldOkBeEnabled());
	}

	private final KeywordSRQLVisitor vis = new KeywordSRQLVisitor();
	private static final String QB = "qb";
	private static final String COMPLEX = "complex";
	private static final String ERROR = "error";
	
	private SRQL parseSrqlTextBox() {
		final String q = text.getText();
		final SRQLParser p = new SRQLParser(q);
		try {
			return  p.parse();
		} catch (final Throwable t) {
			return null;
		}
	}

	// validation 
	@Override
    protected boolean shouldOkBeEnabled() {
		return super.shouldOkBeEnabled() && ! isErrorShowing() && sizing.isValidQuery();
	}
	
	// called when we see an invalid query
    public void invalidQuery() {
        ok.setEnabled(false);
    }
    // called when we see a valid query
    public void validQuery() {
        ok.setEnabled(shouldOkBeEnabled());
    }
}
