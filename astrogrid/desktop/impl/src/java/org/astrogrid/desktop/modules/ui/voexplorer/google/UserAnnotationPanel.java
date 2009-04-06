package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrMatcher;
import org.apache.commons.lang.text.StrTokenizer;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.comp.ColorCellRenderer;
import org.astrogrid.desktop.modules.ui.comp.JPromptingTextField;
import org.astrogrid.desktop.modules.ui.comp.MyTitledBorder;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.votech.UserAnnotation;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**{@link JPanel} that displays and allows edits of a user's annotations. */
public class UserAnnotationPanel extends JPanel /*JCollapsiblePane*/ implements ItemListener, DocumentListener, ActionListener, PropertyChangeListener {
	private final Timer notifyTimer;

    /**
	 * 
	 */
	public UserAnnotationPanel() {

        notifyTimer = new Timer(750,this);
        notifyTimer.setCoalesce(true);
        notifyTimer.setRepeats(false);	    
	    
		final FormLayout layout = new FormLayout(
				"0dlu,30dlu,1dlu,29dlu,0dlu"
				,"min,1dlu,min,1dlu,min,0dlu,min,1dlu,min,0dlu,fill:40dlu,1dlu,min,0dlu,min"
		);
		final CellConstraints cc = new CellConstraints();
		final PanelBuilder builder = new PanelBuilder(layout,this);
		int row = 1;

		check = new JCheckBox("Flag");
		check.setToolTipText("Flag this resource as important");
		check.setFont(UIConstants.SMALL_DIALOG_FONT);
		check.setBackground(Color.white);
		check.addItemListener(this);
		builder.add(check,cc.xy(2,row));
		builder.add(new JLabel(IconHelper.loadIcon("flag16.png"))
		    ,cc.xyw(3,row++,2,"left, center"));
		row++;

		colours = new JComboBox(new Object[]{
				Color.BLACK,Color.GREEN,Color.BLUE,Color.RED, Color.LIGHT_GRAY,Color.GRAY
		});		
		colours.setToolTipText("Change the appearance of this resource in the list");
		colours.setRenderer(new ColorCellRenderer());
		colours.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				 dirty();
			}
		});
		
		builder.addLabel("Highlight",cc.xy(2,row,"right, center")).setFont(UIConstants.SMALL_DIALOG_FONT);
		builder.add(colours,cc.xy(4,row++));			

		row++;

		title = new JPromptingTextField("Alternative title");
		title.setToolTipText("Enter the title you'd like to use for this resource");
		title.setFont(UIConstants.SMALL_DIALOG_FONT);
		title.addPropertyChangeListener(JPromptingTextField.VALUE_PROPERTY,this);

		//builder.addLabel("Alternative title",cc.xyw(2,row++,3)).setFont(UIConstants.SMALL_DIALOG_FONT);
		row++;
		//
		row++;
		builder.add(title,cc.xyw(2,row++,3));
		row++;
		
		note = new JTextArea();
		note.setToolTipText("Use this to record your own notes about this resource");
		note.getDocument().addDocumentListener(this);
		note.setLineWrap(true);
		note.setWrapStyleWord(true);
		note.setEditable(true);
		//note.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);		// this key is only defined on 1.5 - no effect on 1.4
        note.setFont(UIConstants.SMALL_DIALOG_FONT);	
        final JScrollPane sp = new JScrollPane(note,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		builder.addLabel("Notes",cc.xyw(2,row++,3)).setFont(UIConstants.SMALL_DIALOG_FONT);

		row++;			
        builder.add(sp,cc.xyw(2,row++,3));
        
        row++;
		tags = new JPromptingTextField("Tags");
		tags.setToolTipText("label this resource with your own choice of tags");
		tags.setFont(UIConstants.SMALL_DIALOG_FONT);
		tags.addPropertyChangeListener(JPromptingTextField.VALUE_PROPERTY,this);

		//builder.addLabel("Tags",cc.xyw(2,row++,3)).setFont(UIConstants.SMALL_DIALOG_FONT);
		row++;
		//
		row++;
		builder.add(tags,cc.xyw(2,row++,3));

		this.setBackground(Color.WHITE);
		this.setBorder(MyTitledBorder.createLined("Annotate"));

	}
	
private final JToggleButton check;
private final JComboBox colours;
private final JTextArea note;
private final JPromptingTextField  tags;
private final JPromptingTextField title;

// change notification interface.
private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();

public void addChangeListener(final ChangeListener e) {
    listeners.add(e);
}   

public void removeChangeListener(final ChangeListener e) {
    listeners.remove(e);
}

/** something has changed - so that this editor is dirty. 
 * however, don't want to save & update on every edit - so start a timer instead.
 */
private void dirty() {
    notifyTimer.restart();
}

// callback from the notification timer.
public void actionPerformed(final ActionEvent ignored) {
    final ChangeEvent ce = new ChangeEvent(this);
    for (int i = 0; i < listeners.size(); i++) {
        listeners.get(i).stateChanged(ce);
    }    
}   

public void clear() {
    if (notifyTimer.isRunning()) {       
        notifyTimer.stop();
        // we've got outstanding things to save.
        actionPerformed(null);
    }

    check.setSelected(false);
    note.setText(null);
    title.setValue(null);
    colours.setSelectedIndex(0);
    tags.setValue(null);
    notifyTimer.stop(); // as all those 'sets' will have triggered it.
}   


// editor listeners..
// listen for changes - and just note that this item is now dirty.
public void changedUpdate(final DocumentEvent e) {
    dirty();
}

public void insertUpdate(final DocumentEvent e) {
    dirty();
}

public void itemStateChanged(final ItemEvent e) {
    dirty();
}
public void removeUpdate(final DocumentEvent e) {
    dirty();
}
// propertychange listener - again, just note that the item is now dirty.
public void propertyChange(final PropertyChangeEvent evt) {
    if (evt.getNewValue() != evt.getOldValue()) {
        dirty();
    }
}
/** create an annotation containing the form values.
 * if there's no values, return null.
 */
public UserAnnotation createAnnotation() {
    final LazyAnnotationBuilder b = new LazyAnnotationBuilder();
	if (check.isSelected()) {
	    b.getAnnotation().setFlagged(true);
    }
	if (StringUtils.isNotBlank(note.getText())) {
	    b.getAnnotation().setNote(StringUtils.trimToEmpty(note.getText()));
	}
	if (StringUtils.isNotBlank((String)title.getValue())) {
	    b.getAnnotation().setAlternativeTitle(StringUtils.trimToEmpty((String)title.getValue()));
	}
	if (colours.getSelectedIndex() != 0) {
		b.getAnnotation().setHighlight((Color)colours.getSelectedItem());
	}
	final String ta =  (String)tags.getValue();
	if (StringUtils.isNotBlank(ta)) {
		// try to parse it.
		final StrTokenizer tok = new StrTokenizer(ta,StrMatcher.charSetMatcher(", ")); // matches spaces and commas as delimiters
		b.getAnnotation().setTags(new LinkedHashSet(tok.getTokenList()));
	}
	return b.annotation; // which may be null at this point.
}

/** little helper class that lazily builds an annotation */
private static class LazyAnnotationBuilder {
    public UserAnnotation annotation;
    public UserAnnotation getAnnotation() {
        if (annotation == null) {
            annotation = new UserAnnotation();
        }
        return annotation;
    }
}

public void setAnnotation(final UserAnnotation ann) {
    notifyTimer.stop();
	check.setSelected(ann.isFlagged());
			
	if (ann.getHighlight() == null) {
		colours.setSelectedIndex(0);
	} else {
		colours.setSelectedItem(ann.getHighlight());
	}

	title.setValue(ann.getAlternativeTitle());
	note.setText(ann.getNote());	

	final Set tr = ann.getTags();
	if (tr != null && ! tr.isEmpty()) {
		final StrBuilder sb = new StrBuilder();
		sb.appendWithSeparators(tr," ");
		tags.setValue(sb.toString());
	}
	notifyTimer.stop(); // as all the previous sets have no-doubt triggered it.
			
}





}