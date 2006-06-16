/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.desktop.modules.ag.CommunityInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley
 * @since May 11, 20065:49:10 PM
 */
public class SecurityToolEditorPanelFactory implements ToolEditorPanelFactory {

	private final CommunityInternal comm;
	public AbstractToolEditorPanel create(ToolModel model, UIComponent parent) {
		return new SecurityToolEditorPanel(model,comm);
	}

	public String getName() {
		return "Security";
	}

	public SecurityToolEditorPanelFactory(final CommunityInternal comm) {
		super();
		this.comm = comm;
	}

}
