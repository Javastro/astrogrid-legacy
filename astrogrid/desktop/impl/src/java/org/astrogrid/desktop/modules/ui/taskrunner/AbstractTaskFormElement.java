/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParsePosition;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Abstract class for all task form elements.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 200711:16:30 AM
 */
public abstract class AbstractTaskFormElement  implements ItemListener, ActionListener {
	
	/** the parameter value this form element operates upon */
	protected final ParameterValue pval;
	/** the description of this parameter */
	protected final ParameterBean pdesc;
	
	protected final JToggleButton indirectToggle;
	private final JTextField indirectField;
	protected final JLabel label;
	protected final FlipPanel editor;
	protected final ResourceChooserInternal fileChooser;
	protected JButton chooserButton;
	protected boolean localFileEnabled = true;
	// add and remove buttons - null by default. An external manager 
	// will provide these if needed.
	protected AbstractButton addButton;
	protected AbstractButton removeButton;
	protected AbstractButton optionalButton;
	public AbstractTaskFormElement(ParameterValue pval, ParameterBean pdesc, ResourceChooserInternal fileChooser) {
		this.pval = pval;
		this.pdesc = pdesc;
		this.fileChooser = fileChooser;
		this.indirectToggle = new JToggleButton(IconHelper.loadIcon("anystorage16.png"));
		indirectToggle.setToolTipText("Load this input from storage");
		indirectToggle.setSelected(pval.getIndirect());
		indirectToggle.addItemListener(this);
		
		StrBuilder sb = new StrBuilder();
		sb.append(pdesc.getUiName());
		if (! pdesc.getType().equalsIgnoreCase("text")) {
			sb.append(" - ").append(pdesc.getType());
		}
		if (StringUtils.isNotEmpty(pdesc.getUcd())) {
			sb.append(" (").append(pdesc.getUcd()).append(")");
		}
		if (StringUtils.isNotEmpty(pdesc.getUnits())) {
			sb.append(" (").append(pdesc.getUnits()).append(")");
		}		
		label = new JLabel(sb.toString());
		associate(label);
		label.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.LIGHT_GRAY));		
		editor = new FlipPanel();
		associate(editor);
		editor.add(createEditor(),DIRECT);
		
		FormLayout layout = new FormLayout("d,2dlu,fill:max(50dlu;pref):grow,2dlu,d","d");
		CellConstraints cc = new CellConstraints();
		PanelBuilder indirect = new PanelBuilder(layout);
		indirect.addLabel("URL:",cc.xy(1,1));
		indirectField = new IndirectURIField();
		indirect.add(indirectField,cc.xy(3,1));
		chooserButton = new JButton("Browse..");
		chooserButton.setToolTipText("Select a file");
		chooserButton.addActionListener(this);
		indirect.add(chooserButton,cc.xy(5,1));
		
		editor.add(indirect.getPanel(),INDIRECT);
		// populate it, if there's stuff already in the tool.
		if (pval.getValue() != null && pval.getIndirect()) {
			editor.show(INDIRECT);
			indirectField.setText(pval.getValue());
		}
	}
		
	
	// callback from chooserButton.
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() != chooserButton) {
			return;
		}
		URI u = fileChooser.chooseResourceWithParent("Choose a file",true,localFileEnabled,true,chooserButton);
		if (u == null) {
			return;
		}
		//@FIXME inline file parameters - wait for vfs integration first.
		indirectField.setText(u.toString());
		
	}
	
	/** specialized text field that listens to itself and warns about incorrect references 
	 * also updates value of pval on change.*/
	private class IndirectURIField extends JTextField implements DocumentListener{
		/**
		 * 
		 */
		public IndirectURIField() {
			original = getBorder();
			warn = BorderFactory.createLineBorder(Color.RED);
			getDocument().addDocumentListener(this);
		}
		public void changedUpdate(DocumentEvent e) {
			update();
		}
		private void update() {
			String s = getText();
			pval.setIndirect(true); // should be set anyhow.
			pval.setValue(s);
			try {
				URI u = new URI(s);
				if (u.isAbsolute() && ! u.getScheme().equals("file")) {
					setBorder(original);
					return;
				}
			} catch (URISyntaxException x) {
				// ok.
			}
			setBorder(warn);
		}
		protected final Border original;
		protected final Border warn;


		public void insertUpdate(DocumentEvent e) {
			update();
		}

		public void removeUpdate(DocumentEvent e) {
			update();
		}
	}
	
	
	/** subclasses should implement this */
	protected abstract JComponent createEditor();
	/** subclesses should implement this to return the value contained by the editor */
	protected abstract String getStringValue();
	/**
	 * @return the parameter description.
	 */
	public final ParameterBean getDescription() {
		return this.pdesc;
	}

	public final ParameterValue getValue() {
		return this.pval;
	}
	
	// called when the indirect button is clicked.
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == indirectToggle) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				pval.setIndirect(true);
				pval.setValue(indirectField.getText());
				editor.show(INDIRECT);
			} else {
				pval.setIndirect(false);
				pval.setValue(getStringValue());
				editor.show(DIRECT);
			}
		}
	}
	
	protected final static String INDIRECT = "indirect";
	protected final static String DIRECT = "direct";

	/**
	 * @return the addButton
	 */
	public final AbstractButton getAddButton() {
		return this.addButton;
	}


	/**
	 * @param addButton the addButton to set
	 */
	public final void setAddButton(AbstractButton addButton) {
		this.addButton = addButton;
		associate(addButton);
	}


	/**
	 * @return the removeButton
	 */
	public final AbstractButton getRemoveButton() {
		return this.removeButton;
	}


	/**
	 * @param removeButton the removeButton to set
	 */
	public final void setRemoveButton(AbstractButton removeButton) {
		this.removeButton = removeButton;
		associate(removeButton);
	}


	/** return the component editor
	 * @return the editor
	 */
	public final FlipPanel getEditor() {
		return this.editor;
	}


	/** return the indirect toggle button.
	 * @return the indirectToggle
	 */
	public final JToggleButton getIndirectToggle() {
		return this.indirectToggle;
	}


	/** return the name label.
	 * @return the label
	 */
	public final JLabel getLabel() {
		return this.label;
	}
	/**
	 * @param b
	 */
	public void setEnabled(boolean b) {
		label.setEnabled(b);
		indirectToggle.setVisible(b);
		editor.setVisible(b);
		if (addButton != null) { // no point allowing more to be added if this is disabled..
			addButton.setVisible(b);
		}
	
	}
	/**
	 * @return the optionalButton
	 */
	public final AbstractButton getOptionalButton() {
		return this.optionalButton;
	}
	/**
	 * @param optionalButton the optionalButton to set
	 */
	public final void setOptionalButton(AbstractButton optionalButton) {
		this.optionalButton = optionalButton;
		associate(optionalButton);
	}
	
	public String toString() {
		return pval.getName() +":=" + pval.getValue();
	}

	/** associate the component with this form element
	 * plces this object in thejComponent's clientPropertyMap under the key 'AbstractTaskFormElement.class' 
	 * @param comp
	 */
	protected final void associate(JComponent comp) {
		comp.putClientProperty(AbstractTaskFormElement.class,this);
	}
	
}
