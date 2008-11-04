/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.XQueryList;
import org.astrogrid.desktop.modules.ui.voexplorer.QuerySizeIndicator.QuerySizeListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** {@link EditingPanel} for an XQuery list.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 30, 20072:35:07 PM
 */
public class XQueryListEditingPanel extends EditingPanel implements ActionListener, QuerySizeListener, DocumentListener {
	
    public final void showExample() {
        text.setText("(:example query - list all Data Collections :)\n"
                + "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')] \n"
                + "where $r/@xsi:type  &=  '*DataCollection' \n"
                + "return $r");
    }
    
	public XQueryListEditingPanel(final UIComponent parent,final QuerySizer sizer) {
        CSH.setHelpIDString(this,"reg.edit.xquery");	    
        sizing = new QuerySizeIndicator(parent,sizer);
        sizing.addQuerySizeListener(this);
		final FormLayout layout = new FormLayout(
				"2dlu,right:d,1dlu,max(30dlu;d):grow,4dlu,d,1dlu,d,3dlu" // cols
				,"d,d,max(30dlu;d),d,1dlu:grow,d" // rows
				);
		final PanelBuilder builder = new PanelBuilder(layout,this);
		final CellConstraints cc = new CellConstraints();
		int row = 1;
		builder.addLabel("The query named:",cc.xy(2,row));
		folderName.setText("new xquery");
		folderName.setColumns(20);
		builder.add(folderName,cc.xy(4,row));
		
		row++;
		builder.addLabel("Contains resources that match:",cc.xyw(2,row,3));
		
		row++;
		//@future use the JEdit widget here, to get some text colorisation.
		text= new JTextArea();
		text.getDocument().addDocumentListener(this);
		text.setRows(15);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		final JScrollPane sp = new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		builder.add(sp,cc.xyw(2,row,7));		
		
		row++;
		
		builder.add(sizing,cc.xyw(2,row,5));
		estimate = new JButton("Test");
		estimate.setToolTipText("<html>Estimate the number of results this query will produce<br> Necessary before 'OK' is enabled");
		estimate.addActionListener(this);
		builder.add(estimate,cc.xy(8,row));
		
		row++;
		// grows to fill space.
		row++;
		builder.add(ok,cc.xy(6,row));
		builder.add(cancel,cc.xy(8,row));		
		
	    ok.setEnabled(false); //to start with, query hasn't been sized.
				
	}
	private final JButton estimate;
	private final QuerySizeIndicator sizing;
	private final JTextArea text;
	
	@Override
    public void setCurrentlyEditing(final ResourceFolder currentlyEditing) {
		if (! (currentlyEditing instanceof XQueryList)) {
			throw new IllegalArgumentException("Not an instanceof XQueryList");
		}
		super.setCurrentlyEditing(currentlyEditing);
		final XQueryList sl = (XQueryList)getCurrentlyEditing();
		if (StringUtils.isEmpty(sl.getQuery())) {
		    showExample();
		} else {
		    text.setText(sl.getQuery());
		}
	}
	
	@Override
    public void loadEdits() {
		super.loadEdits();
		final XQueryList sl = (XQueryList)getCurrentlyEditing();
		sl.setQuery(text.getText());
	}
	
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == estimate) {
			sizing.setValue(text.getText());
		}
	}	

	@Override
    protected boolean shouldOkBeEnabled() {
		return super.shouldOkBeEnabled() 
		&& sizing.isValidQuery();
	}



// called when we see an invalid query
public void invalidQuery() {
    ok.setEnabled(false);
}
// called when we see a valid query
public void validQuery() {
    ok.setEnabled(shouldOkBeEnabled());
}

public void changedUpdate(final DocumentEvent e) {
    ok.setEnabled(false);   
}

public void insertUpdate(final DocumentEvent e) {
    ok.setEnabled(false);
}

public void removeUpdate(final DocumentEvent e) {
    ok.setEnabled(false);
}
}
