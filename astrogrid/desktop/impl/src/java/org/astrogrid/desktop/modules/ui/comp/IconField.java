package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JTextField;

import org.astrogrid.desktop.icons.IconHelper;

/** a text field that displays an additional icon
 * 
 * @todo make the margin work on OSX.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 28, 20071:53:50 PM
 */
public class IconField extends JTextField{

	/** create a field with the default icon - a 'search' icon */
	public IconField(int columns) {
		this(columns,IconHelper.loadIcon("search16.png"));
	}
	
	public IconField(int columns,ImageIcon icon) {
		super(columns);
		image= icon;
		x0 = getBorder().getBorderInsets(this).left;
		setMargin(new Insets(0,x0 + image.getIconWidth(),0,0));	
	}
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
	      int y = (getHeight() - image.getIconHeight())/2;
          g.drawImage(image.getImage(), x0, y, this);			
	}
	final ImageIcon image;
	final int x0;
}