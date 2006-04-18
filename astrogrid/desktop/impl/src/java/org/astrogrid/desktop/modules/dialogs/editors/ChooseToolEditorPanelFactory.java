/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since Apr 11, 200611:37:16 PM
 */
public class ChooseToolEditorPanelFactory implements ToolEditorPanelFactory {

	
	private final Registry registry;
	private final ApplicationsInternal apps;
	public AbstractToolEditorPanel create(ToolModel model, UIComponent parent) {
		return new ChooseAToolEditorPanel(model,parent,registry,apps,false); //@todo necessary to find way to set this to true or false?
	}
	public ChooseToolEditorPanelFactory(final Registry registry, final ApplicationsInternal apps) {
		super();
		this.registry = registry;
		this.apps = apps;
	}
	public String getName() {
		return "Chooser";
	}

}
