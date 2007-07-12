/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Font;
import java.awt.geom.AffineTransform;

/** Static class of UI constants.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 11, 20072:33:38 PM
 */
public class UIConstants {
	private UIConstants() {
	}

	/** a nice sans-serif font */
	public static final Font SANS_FONT = Font.decode("Helvetica")	;
	/** smaller version of the dialog font */
	public static final Font SMALL_DIALOG_FONT = Font.decode("Dialog").deriveFont(AffineTransform.getScaleInstance(0.9,0.9));
}
