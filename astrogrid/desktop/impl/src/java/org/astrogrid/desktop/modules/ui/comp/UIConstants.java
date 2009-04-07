/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Font;
import java.awt.Insets;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

import org.astrogrid.desktop.icons.IconHelper;

/** Static class of UI constants.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 11, 20072:33:38 PM
 */
public class UIConstants {
	private UIConstants() {
	}

	/** a nice sans-serif font */
	public static final Font SANS_FONT = Font.decode("Helvetica")	;
	public static final Font SANS_TITLE_FONT = SANS_FONT.deriveFont(Font.BOLD).deriveFont(AffineTransform.getScaleInstance(1.2,1.2));
	/** smaller version of the dialog font */
	public static final Font SMALL_DIALOG_FONT = Font.decode("Dialog").deriveFont(AffineTransform.getScaleInstance(0.9,0.9));
    
	public static final Icon COMPLETED_ICON = IconHelper.loadIcon("tick16.png");
    public static final Icon ERROR_ICON = IconHelper.loadIcon("no16.png");
    public static final Icon RUNNING_ICON = IconHelper.loadIcon("greenled16.png");
    public static final Icon UNKNOWN_ICON = IconHelper.loadIcon("idle16.png");
    public static final Icon PENDING_ICON = IconHelper.loadIcon("yellowled16.png");
    public static final Icon SERVICE_DOWN_ICON= IconHelper.loadIcon("redled16.png");
    public static final Icon SERVICE_OK_ICON = RUNNING_ICON;
    public static final Icon PIN_ICON = IconHelper.loadIcon("pin16.gif");    
    
    /** length of time to display transient popups for */
    public static final int POPUP_DISMISS_TIME = 5000;
    
    /** threshold above which to prompt user that they really want to do an activity on so many resources */
    public static final int LARGE_SELECTION_THRESHOLD = 30;
    
    
    /** margin sizes of buttons aren't consistent between different platform laf - which pushes out my layout.
     * so will define my own margins.
     */
    public static final Insets SMALL_BUTTON_MARGIN = new Insets(1,1,1,1);
}
