/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** Interface to a factory for tool editor panels.
 * @author Noel Winstanley
 * @since Apr 11, 200611:27:51 PM
 */
public interface ToolEditorPanelFactory {
	
	/** create a new panel */
	AbstractToolEditorPanel create(ToolModel model, UIComponent parent);
	/** access the UI name of this kind of panel */
	String getName();
}
