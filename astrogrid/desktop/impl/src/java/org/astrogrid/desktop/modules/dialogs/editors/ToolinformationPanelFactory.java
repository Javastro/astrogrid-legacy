/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since Apr 11, 200611:32:18 PM
 */
public class ToolinformationPanelFactory implements ToolEditorPanelFactory {

	private final Applications apps;
	
	
	public ToolinformationPanelFactory(final Applications apps) {
		super();
		this.apps = apps;
	}


	public AbstractToolEditorPanel create(ToolModel model, UIComponent parent) {
		return new ToolInformationPanel(model,apps,parent);
	}


	public String getName() {
		return "Info";
	}

}
