/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import org.astrogrid.desktop.modules.ui.voexplorer.srql.KeywordSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLParser;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SrqlQueryBuilderPanel;

import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** panel that handles the 'new query' function. - allows editing of smart lists.
 * @author Noel.Winstanley@manchester.ac.uk

 * @since Apr 26, 20077:21:19 PM
 */
public class SmartListEditingPanel extends EditingPanel implements ActionListener, ListEventListener, DocumentListener {

	public SmartListEditingPanel(UIComponent parent,QuerySizer sizer) {
	    CSH.setHelpIDString(this,"reg.edit.smart");
		FormLayout layout= new FormLayout(
				"2dlu, right:d, 1dlu, max(30dlu;d):grow, 4dlu, d, 1dlu, d, 3dlu" // cols
				,"d,top:d:grow,d,max(30dlu;d),d" // rows
		);
		PanelBuilder builder = new PanelBuilder(layout,this);
		CellConstraints cc = new CellConstraints();
		int row = 1;
		
		builder.addLabel("The search named:",cc.xy(2,row));
		folderName.setText("New Search");
		folderName.setColumns(20);
		builder.add(folderName,cc.xy(4,row));

		row++;
		qb = new SrqlQueryBuilderPanel();
		qb.getClauses().addListEventListener(this);
		flip.add(qb,QB);
		JLabel l = new JLabel("<html>The query entered is either too complex to be edited in the form"
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
		sizing = new QuerySizeIndicator(parent,sizer);
		builder.add(sizing,cc.xyw(2,row,4));
		builder.add(ok,cc.xy(6,row));
		builder.add(cancel,cc.xy(8,row));		
		this.invalidate();
	}

	private final JTextArea text;
	private final SrqlQueryBuilderPanel qb;
	private final FlipPanel flip = new FlipPanel();
	private final QuerySizeIndicator sizing;

	public void setCurrentlyEditing(ResourceFolder currentlyEditing) {
		if (! (currentlyEditing instanceof SmartList)) {
			throw new IllegalArgumentException("Not an instanceof SmartList");
		}
		super.setCurrentlyEditing(currentlyEditing);
		SmartList sl = (SmartList)getCurrentlyEditing();
		if (sl.getQuery() != null) {
			SRQL srql = sl.getQuery();
			sizing.setValue(srql);
			if (qb.canDisplayQuery(srql)) { // simple query
			        qb.setQuery(srql);
			        showForm();
			} else { // complex query
			    showTooComplex();
				String qText = (String) srql.accept(vis);
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
	public void loadEdits() {
		super.loadEdits();
		SmartList sl = (SmartList)getCurrentlyEditing();		
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
	public void listChanged(ListEvent arg0) {
		formTimer.restart();
	}
//	document listener interface	
	public void changedUpdate(DocumentEvent e) {
		textTimer.restart();
	}

	public void insertUpdate(DocumentEvent e) {
		textTimer.restart();
	}

	public void removeUpdate(DocumentEvent e) {
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
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(formTimer)) {
			final SRQL query = qb.getQuery();
			// set this running straight away.
			sizing.setValue(query);
			String qText = (String) query.accept(vis);
			text.getDocument().removeDocumentListener(this); // temporarily remove listener, to prevent event-cycles. hope this isn't too expensive
			text.setText(qText);
			text.getDocument().addDocumentListener(this);
		} else if (e.getSource().equals(textTimer)) {
			SRQL srql = parseSrqlTextBox();
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
	    return QB.equals(flip.currentlyShowing())	 ;   
	}
	private boolean isErrorShowing() {
        return ERROR.equals(flip.currentlyShowing())    ;   	    
	}
	private boolean isTooComplexShowing() {
        return COMPLEX.equals(flip.currentlyShowing())   ;   	    
	}
	private void showForm() {
        flip.show(QB);	    
        ok.setEnabled(true);
	}
	private void showError() {
	    flip.show(ERROR);
	    ok.setEnabled(false);
	}
	private void showTooComplex() {
	    flip.show(COMPLEX);
	    ok.setEnabled(true);
	}

	private final KeywordSRQLVisitor vis = new KeywordSRQLVisitor();
	private static final String QB = "qb";
	private static final String COMPLEX = "complex";
	private static final String ERROR = "error";
	
	private SRQL parseSrqlTextBox() {
		String q = text.getText();
		SRQLParser p = new SRQLParser(q);
		try {
			return  p.parse();
		} catch (Throwable t) {
			return null;
		}
	}

	// validation 
	protected boolean shouldOkBeEnabled() {
		return super.shouldOkBeEnabled() && ! isErrorShowing(); 
	}
}
