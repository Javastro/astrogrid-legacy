/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;

import com.l2fprod.common.swing.JLinkButton;
import com.l2fprod.common.swing.JTaskPaneGroup;


/** base class for tasks that can be perforemed on resources.
 * Extends the standard link button, with extra machinery to determine 
 * when it's applicable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20073:41:59 PM
 */
public abstract class AbstractSingleActivity extends AbstractAction implements Activity {
	public AbstractSingleActivity(String name, Icon icon) {
		super(name, icon);
		setEnabled(false);
	}

	public AbstractSingleActivity(String name) {
		super(name);
		setEnabled(false);
	}
	
	private String section;
	
	public void setSection(String section) {
		this.section = section;
	}
	
	public String getSection() {
		return this.section;
	}
	protected final UIComponentBodyguard uiParent = new UIComponentBodyguard();
	public final void setUIParent(UIComponent up) {
		uiParent.set(up);
	}	
	public AbstractSingleActivity() {
		setEnabled(false);
	}
	
	public void setText(String s) {
		putValue(Action.NAME,s);
	}
	
	public void setIcon(Icon i) {
		putValue(Action.SMALL_ICON,i);
	}
	
	public void setToolTipText(String s) {
		putValue(Action.SHORT_DESCRIPTION,s);
	}
	
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(AbstractSingleActivity.class);

	
	protected JLinkButton createLinkButton() {
		return new JLinkButton(this) {
			public void setEnabled(boolean b) {
				super.setEnabled(b);
				setVisible(b);
			}			
		};
	}
	
	protected JMenuItem createMenuItem() {
		return new JMenuItem(this) {
			public void setEnabled(boolean b) {
				super.setEnabled(b);
				setVisible(b);
			}
		};
		
	}
	

	/** called by owning group when there's no current selection */
	public   abstract void noneSelected();

	/** tests whether 
	protected abstract boolean invokable(Resource r);
	/** called by owning group when the selection changes
	 * it's up to the ResourceTask itself to inspect the list and determine
	 * whether it's applicable or not.
	 * 
	 * it's also responsible for holding onto the resources that it's 
	 * able to operate over (might be a subset of all the resources). 
	 * so that these are available when actionPerformed() is called.
	 * @param r
	 */

	
	public abstract void selected(Transferable t) ;

	/** process the button click.
	 * subclass this - default implementaiton just displays 'unimplmeented'
	 */
	public void actionPerformed(ActionEvent e) {
		Component comp = null;
		if (e.getSource() instanceof Component) {
			comp = (Component)e.getSource();
		}
		JOptionPane.showMessageDialog(comp,"Unimplemented");
	}

	public void addTo(JTaskPaneGroup grp) {
		grp.add(createLinkButton());
	}

	public void addTo(JMenu menu) {
		menu.add(createMenuItem());
	}

	public void addTo(JPopupMenu menu) {
		menu.add(createMenuItem());
	}	
}
