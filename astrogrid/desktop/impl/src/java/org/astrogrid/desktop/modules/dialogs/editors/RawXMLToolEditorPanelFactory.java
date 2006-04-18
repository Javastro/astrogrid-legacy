/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since Apr 11, 200611:34:36 PM
 */
public class RawXMLToolEditorPanelFactory implements ToolEditorPanelFactory {

	private final Applications apps;
	public AbstractToolEditorPanel create(ToolModel model, UIComponent parent) {
		return new RawXMLToolEditorPanel(model,apps,parent);
	}
	public RawXMLToolEditorPanelFactory(final Applications apps) {
		super();
		this.apps = apps;
	}
	public String getName() {
		return "XML";
	}

}
