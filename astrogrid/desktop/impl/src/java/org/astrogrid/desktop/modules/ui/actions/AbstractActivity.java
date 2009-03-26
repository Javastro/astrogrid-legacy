/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

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


/** Base class for activities.
 * Extends the standard link button, with extra machinery to determine 
 * when it's applicable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20073:41:59 PM
 */
public abstract class AbstractActivity extends AbstractAction implements Activity {
	public AbstractActivity(final String name, final Icon icon) {
		super(name, icon);
		setEnabled(false);
	}

	public AbstractActivity(final String name) {
		super(name);
		setEnabled(false);
	}

	protected final UIComponentBodyguard uiParent = new UIComponentBodyguard();
    private String helpID;
	public final void setUIParent(final UIComponent up) {
		uiParent.set(up);
	}	
	public AbstractActivity() {
		setEnabled(false);
	}
	
	public void setText(final String s) {
		putValue(Action.NAME,s);
	}
	
	public String getText() {
	    return (String)getValue(Action.NAME);
	}
	
	public void setAccelerator(final KeyStroke ke) {
	    putValue(Action.ACCELERATOR_KEY,ke);
	}
	
	public void setIcon(final Icon i) {
		putValue(Action.SMALL_ICON,i);
	}
	
	public void setToolTipText(final String s) {
		putValue(Action.SHORT_DESCRIPTION,s);
	}
	/** supply a help id for this activity */
	public void setHelpID(final String id) {
	    CSH.setHelpIDString(this,id);
	    this.helpID = id;
	}
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(AbstractActivity.class);

	
	public JLinkButton createLinkButton() {
		final JLinkButton l =  new JLinkButton(this) {
			@Override
            public void setEnabled(final boolean b) {
				super.setEnabled(b);
				setVisible(b);
			}			
		};
		CSH.setHelpIDString(l,helpID);
		return l;
	}
	
	public JMenuItem createMenuItem() {
		final JMenuItem m =  new JMenuItem(this);
		return m;
		
	}
    public JMenuItem createHidingMenuItem() {
        final JMenuItem m =  new JMenuItem(this) {
            @Override
            public void setEnabled(final boolean b) {
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
	public void actionPerformed(final ActionEvent e) {
		Component comp = null;
		if (e.getSource() instanceof Component) {
			comp = (Component)e.getSource();
		}
		JOptionPane.showMessageDialog(comp,"Unimplemented");
	}

	public void addTo(final JTaskPaneGroup grp) {
		grp.add(createLinkButton());
	}

	public void addTo(final JTaskPaneGroup grp, final int pos) {
		grp.add(createLinkButton(),pos);
	}
	
	public void addTo(final JMenu menu) {
		menu.add(createMenuItem());
	}

	public void addTo(final JPopupMenu menu) {
		menu.add(createHidingMenuItem());
	}	
	
	// helper methods for subclasses.
	/** show a confirmation popup, to check before proceeding
	 * NB: although {@link BackgroundWorker} implements {@link Runnable}, it should not be used as the value for {@code continuation}
	 *  - a background worker must be executed using {@code start()}, not {@code run()}.
	 * @param message mesage to display in confimation
	 * @param continuation - continuation to run <b>on EDT</b> if user confirms the dialogue.
	 */
	protected void confirm(final String message,final Runnable continuation) {
	   
	    ConfirmDialog.newConfirmDialog(uiParent.get().getComponent(),"Confirm",message,continuation).setVisible(true);
	}


	/** show a confirm dialogue if sz > LargeSelectionThreshold,
	 * else just run the action 
     * NB: although {@link BackgroundWorker} implements {@link Runnable}, it should not be used as the value for {@code continuation}
     *  - a background worker must be executed using {@code start()}, not {@code run()}.	 
	 * @param sz the thrwshold
	 * @param message the message to dusplay in confirmation dialogue
	 * @param continuation action to run <b>on EDT if user confirms the dialogue.
	 */
	protected void confirmWhenOverThreshold(final int sz,final String message,final Runnable continuation) {
	    if (sz > UIConstants.LARGE_SELECTION_THRESHOLD) {
	        confirm(message,continuation);
	    } else {
	        continuation.run();
	    }
	}
}
