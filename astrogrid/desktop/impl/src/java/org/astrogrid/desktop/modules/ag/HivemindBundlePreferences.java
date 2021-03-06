/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.filemanager.common.BundlePreferences;

/** Subclass of Myspace bundlePreferences for compatability with Hivemind.
 * <p/>
 *  Necessary, as hivemind can't pass in an Integer - so this extension adds functions
 *  that accept int-valued preferences.
 * @author Noel Winstanley
 * @since Jan 10, 20072:44:09 PM
 */
public class HivemindBundlePreferences extends BundlePreferences {

	public void setMaxExtraNodesInt(final int arg0) {
		super.setMaxExtraNodes(Integer.valueOf(arg0));
	}
	
	public void setPrefetchDepthInt(final int arg0) {
		super.setPrefetchDepth(Integer.valueOf(arg0));
	}
	
}
