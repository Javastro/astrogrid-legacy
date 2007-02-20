/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.system.Configuration ;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since Apr 11, 200611:47:19 PM
 */
public class ADQLToolEditorPanelFactory implements ToolEditorPanelFactory {
	private final ResourceChooserInternal resourceChooser;
	private final RegistryGoogle regChooser;
	private final MyspaceInternal myspace;
	private final Registry registry;
	private final Preference advancedPreference;
	public AbstractToolEditorPanel create( ToolModel model, UIComponent parent ) {
		return new ADQLToolEditorPanel( model
                                      , resourceChooser
                                      , regChooser
                                      , parent
                                      , myspace
                                      , registry
                                      , advancedPreference );
	}
    public ADQLToolEditorPanelFactory( final ResourceChooserInternal resourceChooser
                                     , final RegistryGoogle regChooser
                                     , final MyspaceInternal myspace
                                     , final Registry registry
                                     , final Preference advancedPreference) {	
    super();
		this.resourceChooser = resourceChooser;
		this.regChooser = regChooser;
		this.myspace = myspace;
		this.registry = registry;
		this.advancedPreference = advancedPreference;
	}
	public String getName() {
		return "Query";
	}
	public boolean isAdvanced() {
		return false;
	}

}
