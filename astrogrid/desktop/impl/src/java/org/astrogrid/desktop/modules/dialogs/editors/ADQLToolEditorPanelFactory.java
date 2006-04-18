/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since Apr 11, 200611:47:19 PM
 */
public class ADQLToolEditorPanelFactory implements ToolEditorPanelFactory {
	private final ResourceChooserInternal resourceChooser;
	private final RegistryChooser regChooser;
	private final Adql074 adql;
	private final MyspaceInternal myspace;
	private final Registry registry;
	public AbstractToolEditorPanel create(ToolModel model, UIComponent parent) {
		return new ADQLToolEditorPanel(model,resourceChooser,regChooser,adql,parent,myspace,registry);
	}
	public ADQLToolEditorPanelFactory(final ResourceChooserInternal resourceChooser, final RegistryChooser regChooser, final Adql074 adql, final MyspaceInternal myspace, final Registry registry) {
		super();
		this.resourceChooser = resourceChooser;
		this.regChooser = regChooser;
		this.adql = adql;
		this.myspace = myspace;
		this.registry = registry;
	}
	public String getName() {
		return "Query";
	}

}
