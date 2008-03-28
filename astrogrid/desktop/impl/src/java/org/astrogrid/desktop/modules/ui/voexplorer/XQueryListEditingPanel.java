/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jedit.KeywordMap;
import jedit.SQLTokenMarker;
import jedit.Token;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.XQueryList;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 30, 20072:35:07 PM
 */
public class XQueryListEditingPanel extends EditingPanel implements ActionListener {
	
    public final void showExample() {
        text.setText("(:example query - list all cone searches:)\n"
                + "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')] \n"
                + "where $r/@xsi:type  &=  '*DataCollection' \n"
                + "return $r");
    }
    
	public XQueryListEditingPanel(UIComponent parent,QuerySizer sizer) {
        CSH.setHelpIDString(this,"reg.edit.xquery");	    
		FormLayout layout = new FormLayout(
				"2dlu,right:d,1dlu,max(30dlu;d):grow,4dlu,d,1dlu,d,3dlu" // cols
				,"d,d,max(30dlu;d),d,1dlu:grow,d" // rows
				);
		PanelBuilder builder = new PanelBuilder(layout,this);
		CellConstraints cc = new CellConstraints();
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
		text.setRows(15);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		JScrollPane sp = new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		builder.add(sp,cc.xyw(2,row,7));		
		
		row++;
		sizing = new QuerySizeIndicator(parent,sizer);
		
		builder.add(sizing,cc.xyw(2,row,5));
		estimate = new JButton("Estimate");
		estimate.addActionListener(this);
		builder.add(estimate,cc.xy(8,row));
		
		row++;
		// grows to fill space.
		row++;
		builder.add(ok,cc.xy(6,row));
		builder.add(cancel,cc.xy(8,row));		
				
	}
	private final JButton estimate;
	private final QuerySizeIndicator sizing;
	private final JTextArea text;
	
	public void setCurrentlyEditing(ResourceFolder currentlyEditing) {
		if (! (currentlyEditing instanceof XQueryList)) {
			throw new IllegalArgumentException("Not an instanceof XQueryList");
		}
		super.setCurrentlyEditing(currentlyEditing);
		XQueryList sl = (XQueryList)getCurrentlyEditing();
		if (StringUtils.isEmpty(sl.getQuery())) {
		    showExample();
		} else {
		    text.setText(sl.getQuery());
		}
	}
	
	public void loadEdits() {
		super.loadEdits();
		XQueryList sl = (XQueryList)getCurrentlyEditing();
		sl.setQuery(text.getText());
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == estimate) {
			sizing.setValue(text.getText());
		}
	}	
	
	//@future - check that the query is complete, somehow.
	protected boolean shouldOkBeEnabled() {
		return super.shouldOkBeEnabled() ;
	}
	//@todo implement this by hand.
public static class XQueryTokenMarker extends SQLTokenMarker {


		public XQueryTokenMarker() {
			super(getKeywordMap(),true);
		}
		private static KeywordMap kw;
		public static synchronized KeywordMap getKeywordMap()
		{
			if (kw == null) {
				kw = new KeywordMap(true);
				kw.add("for",Token.KEYWORD1);
				kw.add("let",Token.KEYWORD1);
				kw.add("where",Token.KEYWORD1);
				kw.add("return",Token.KEYWORD1);
				kw.add("declare",Token.KEYWORD2);
			}
			return kw;
		}	
}
}
