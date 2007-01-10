/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since Apr 11, 200611:42:46 PM
 */
public class BasicToolEditorPanelFactory implements ToolEditorPanelFactory {

	private final ResourceChooserInternal chooser;
	public AbstractToolEditorPanel create(ToolModel model, UIComponent parent) {
		return new BasicToolEditorPanel(model,chooser);
	}
	public BasicToolEditorPanelFactory(final ResourceChooserInternal chooser) {
		super();
		this.chooser = chooser;
	}
	public String getName() {
		return "Parameter";
	}
	public boolean isAdvanced() {
		return false;
	}

}
