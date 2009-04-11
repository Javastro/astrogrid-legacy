/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.net.URI;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.comp.JPromptingTextField;
import org.astrogrid.desktop.modules.ui.comp.PinnableLabel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Abstract parameter editor in the task runner form.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 200711:16:30 AM
 */
public abstract class AbstractTaskFormElement  implements ItemListener, ActionListener {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(AbstractTaskFormElement.class);
	
	/** the parameter value this form element operates upon */
	protected final ParameterValue pval;
	/** the description of this parameter */
	protected final ParameterBean pdesc;
	
	protected final JToggleButton indirectToggle;
	private final JPromptingTextField indirectField;
	protected final PinnableLabel label;
	/** not final - so it can be lazily initialized if needed.
	 do not refer to _editor directly - use getEditor() instead */
	protected FlipPanel _editor;
	protected final JPanel indirectPanel;
	protected final ResourceChooserInternal fileChooser;
	protected JButton chooserButton;
	protected boolean localFileEnabled = true;
	// add and remove buttons - null by default. An external manager 
	// will provide these through setters if needed.
	protected AbstractButton addButton;
	protected AbstractButton removeButton;
	protected AbstractButton optionalButton;

	public AbstractTaskFormElement(final ParameterValue pval, final ParameterBean pdesc, final ResourceChooserInternal fileChooser) {
		this.pval = pval;
		this.pdesc = pdesc;
		this.fileChooser = fileChooser;
		this.indirectToggle = new JToggleButton(IconHelper.loadIcon("anystorage16.png"));
		indirectToggle.setToolTipText("Load this input from a file");
		indirectToggle.setSelected(pval.getIndirect());
		indirectToggle.addItemListener(this);
		CSH.setHelpIDString(indirectToggle,"task.indirect");
		
		label =  new PinnableLabel(StringUtils.abbreviate(pdesc.getUiName(),50));
		label.setToolTipText("Click to pin the documentation for this parameter");	
		associate(label);	
		

		final FormLayout indirectForm = new FormLayout(
		        "fill:d:grow,d" // cols
		        ,"d,p:grow"
		        );
		final PanelBuilder pb = new PanelBuilder(indirectForm);
		final CellConstraints cc = new CellConstraints();

		indirectField = new IndirectURIField(pval);
		pb.add(indirectField,cc.xy(1,1));
		
        chooserButton = new JButton("Choose File..");
        chooserButton.setToolTipText("Select a file from local disk or VO Workspace to read this input parameter from");
        chooserButton.addActionListener(this);
        pb.add(chooserButton,cc.xy(2,1));
        
        pb.add(Box.createVerticalGlue(),cc.xyw(1,2,2));
        indirectPanel = pb.getPanel();
        CSH.setHelpIDString(indirectPanel,"task.indirect");
	}
	
	/** customization method, for use by subclasses
	 * call tthis to disallow the 'indirect' function - this parameter will always be direct
	 */
	protected void disableIndirect() {
        
        //make sure we just show direct mode.
        getEditor().setShowing(DIRECT);
        indirectToggle.setEnabled(false);
        indirectToggle.setVisible(false);	    
	}
	
	// register a listener of various components.
	public void addMouseListener(final MouseListener listener) {
	    getLabel().addMouseListener(listener);
	    getEditor().addMouseListener(listener);
	}
	
	// callback from chooserButton.
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() != chooserButton) {
			return;
		}
		final URI u = fileChooser.chooseResourceWithParent("Choose a file",true,localFileEnabled,true,chooserButton);
		if (u == null) {
			return;
		} else {
		
		    indirectField.setValue(u.toString());
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
	public void itemStateChanged(final ItemEvent e) {
		if (e.getSource() == indirectToggle) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				pval.setIndirect(true);
				pval.setValue((String)indirectField.getValue());
				getEditor().setShowing(INDIRECT);
			} else {
				pval.setIndirect(false);
				pval.setValue(getStringValue());
				getEditor().setShowing(DIRECT);
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
	public final void setAddButton(final AbstractButton addButton) {
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
	public final void setRemoveButton(final AbstractButton removeButton) {
		this.removeButton = removeButton;
		associate(removeButton);
	}


	/** return the component editor
	 * @return the editor
	 */
	public final FlipPanel getEditor() {
	    //lazily initialized, so that everything else can be setup in constructor of a subclass.
	    if (this._editor == null) {
	        _editor = new FlipPanel();
	        // border around the editor is necessary to cause mouse-over effects to be triggered.
	        _editor.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
	        associate(_editor);
	        final JComponent comp = createEditor();
            _editor.add(comp,DIRECT);	   
	        
	        _editor.add(indirectPanel,INDIRECT);
	        // populate it, if there's stuff already in the tool.
	        if (pval.getValue() != null && pval.getIndirect()) {
	            _editor.setShowing(INDIRECT);
	            indirectField.setValue(pval.getValue());
	        }	        
	    }
		return this._editor;
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
	public final PinnableLabel getLabel() {
		return this.label;
	}
	/**
	 * @param b
	 */
	public void setEnabled(final boolean b) {
		label.setEnabled(b);
		if (indirectToggle.isEnabled()) {
		    indirectToggle.setVisible(b);
		}
		getEditor().setVisible(b);
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
	public final void setOptionalButton(final AbstractButton optionalButton) {
		this.optionalButton = optionalButton;
		associate(optionalButton);
	}
	
	@Override
    public String toString() {
		return pval.getName() +":=" + pval.getValue();
	}

	/** associate the component with this form element
	 * plces this object in thejComponent's clientPropertyMap under the key 'AbstractTaskFormElement.class' 
	 * @param comp
	 */
	protected final void associate(final JComponent comp) {
		comp.putClientProperty(AbstractTaskFormElement.class,this);
	}

    /**
     * @return the indirectField
     */
    public final JPromptingTextField getIndirectField() {
        return this.indirectField;
    }

	
}
