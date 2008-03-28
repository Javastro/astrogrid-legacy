/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.dialogs.ConfirmDialog;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;

import com.l2fprod.common.swing.JLinkButton;
import com.l2fprod.common.swing.JTaskPaneGroup;


/** base class for tasks that can be perforemed on resources.
 * Extends the standard link button, with extra machinery to determine 
 * when it's applicable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20073:41:59 PM
 */
public abstract class AbstractActivity extends AbstractAction implements Activity {
	public AbstractActivity(String name, Icon icon) {
		super(name, icon);
		setEnabled(false);
	}

	public AbstractActivity(String name) {
		super(name);
		setEnabled(false);
	}

	protected final UIComponentBodyguard uiParent = new UIComponentBodyguard();
    private String helpID;
	public final void setUIParent(UIComponent up) {
		uiParent.set(up);
	}	
	public AbstractActivity() {
		setEnabled(false);
	}
	
	public void setText(String s) {
		putValue(Action.NAME,s);
	}
	
	public String getText() {
	    return (String)getValue(Action.NAME);
	}
	
	public void setAccelerator(KeyStroke ke) {
	    putValue(Action.ACCELERATOR_KEY,ke);
	}
	
	public void setIcon(Icon i) {
		putValue(Action.SMALL_ICON,i);
	}
	
	public void setToolTipText(String s) {
		putValue(Action.SHORT_DESCRIPTION,s);
	}
	/** supply a help id for this activity */
	public void setHelpID(String id) {
	    CSH.setHelpIDString(this,id);
	    this.helpID = id;
	}
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(AbstractActivity.class);

	
	public JLinkButton createLinkButton() {
		JLinkButton l =  new JLinkButton(this) {
			public void setEnabled(boolean b) {
				super.setEnabled(b);
				setVisible(b);
			}			
		};
		CSH.setHelpIDString(l,helpID);
		return l;
	}
	
	public JMenuItem createMenuItem() {
		JMenuItem m =  new JMenuItem(this);
		return m;
		
	}
    public JMenuItem createHidingMenuItem() {
        JMenuItem m =  new JMenuItem(this) {
            public void setEnabled(boolean b) {
                super.setEnabled(b);
                setVisible(b);
            }
        };
        return m;
        
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

	public void addTo(JTaskPaneGroup grp, int pos) {
		grp.add(createLinkButton(),pos);
	}
	
	public void addTo(JMenu menu) {
		menu.add(createMenuItem());
	}

	public void addTo(JPopupMenu menu) {
		menu.add(createHidingMenuItem());
	}	
	
	// helper methods for subclasses.
	/** show a confirmation popup, to check before proceeding
	 */
	protected void confirm(String message,Runnable continuation) {
	    ConfirmDialog.newConfirmDialog(uiParent.get().getComponent(),"Confirm",message,continuation).setVisible(true);
	}

	protected void confirm(String message,final BackgroundWorker continuation) {
	    ConfirmDialog.newConfirmDialog(uiParent.get().getComponent(),"Confirm",message,new Runnable() {

	        public void run() {
	            continuation.start();
	        }
	    }).setVisible(true);
	}
	
	/** show a confirm dialogue if sz > LargeSelectionThreshold,
	 * else just run the action 
	 * @param sz
	 * @param message
	 * @param continuation
	 */
	protected void confirmWhenOverThreshold(int sz,String message,Runnable continuation) {
	    if (sz > UIConstants.LARGE_SELECTION_THRESHOLD) {
	        confirm(message,continuation);
	    } else {
	        continuation.run();
	    }
	}
}
