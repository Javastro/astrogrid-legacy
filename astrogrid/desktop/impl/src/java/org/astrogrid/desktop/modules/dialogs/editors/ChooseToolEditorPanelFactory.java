/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since Apr 11, 200611:37:16 PM
 */
public class ChooseToolEditorPanelFactory implements ToolEditorPanelFactory {

	
	private final ApplicationsInternal apps;
	private final ObjectBuilder builder;
	public AbstractToolEditorPanel create(ToolModel model, UIComponent parent) {
		return new ChooseAToolEditorPanel(model,parent,createPanel(),apps);
	}
	public ChooseToolEditorPanelFactory( final ApplicationsInternal apps, ObjectBuilder builder) {
		super();
		this.builder = builder;
		this.apps = apps;
	}
	public String getName() {
		return "Chooser";
	}
	public boolean isAdvanced() {
		return false;
	}
	
	private RegistryGooglePanel createPanel() {
		return (RegistryGooglePanel) builder.create("registryGooglePanel");
	}

}
