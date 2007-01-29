/*$Id: UIActionContribution.java,v 1.9 2007/01/29 10:46:28 nw Exp $
 * Created on 21-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.contributions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.system.UIImpl;
import org.astrogrid.desktop.modules.system.UIImpl.InvokerWorker;

/** contribution to ui that represents a button.
 * Extends AbstractAction with methods to control display,
 * and details of a method name to invoke on a supplied object
 * when the action is triggered.
 * Method is invoked in a background worker thread.
 * @todo move some functionality from UIImpl into this class
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Mar-2006
 *
 */
public class UIActionContribution extends AbstractAction implements UIStructureContribution, PropertyChangeListener{
   
    private String after;
    
    private String before;
    private String confirmMessage;
    private String methodName;
    private String name;
    private Object object;
    private List parameters= new ArrayList();
    private UIImpl parentImpl;
    private String parentName;
    private Component parentComponent;
    private String helpId;
    private boolean onEventDispatchThread;
    
    
    public static String VISIBLE_PROPERTY = "visible";
    private Preference visiblePreference;
    private boolean visible = true;
    
    /** register a 'parent component' with this action.
     * then, when a watche preference goes 'true', the visible 
     * property of the parent component is altered accordingly.
     * @param c
     */
    public void setParentComponent(Component c) {
    	parentComponent = c;
    	parentComponent.setVisible(isVisible());
    }
    
    /** provide an optional preference object which is 'watched'.
     * when this preference goes 'true', the 'visible' property of this object
     * goes 'true', and a propertyChangeEvent is fired from this class,
     * of {@link #VISIBLE_PROPERTY}
     * @param p
     */
    public void setVisibleCondition(Preference p) {
    	// remove any previous listeners.
    	if (visiblePreference != null) {
    		visiblePreference.removePropertyChangeListener(this);
    	}
    	visiblePreference = p;
    	visiblePreference.addPropertyChangeListener(this);
    	visiblePreference.initializeThroughListener(this);
    }
    
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == visiblePreference) {
	    		setVisible(visiblePreference.asBoolean());	
	    	if (parentComponent != null) {
	    		parentComponent.setVisible(visiblePreference.asBoolean());
	    	}
		}
	}

    // package-private method, for testing only.
    Preference getVisibleCondition() {
    	return visiblePreference;
    }    	
	
	/** sets the visiblity of the action */
	public void setVisible(boolean b) {
		if (visible != b) {
			boolean old = visible;
			visible = b;
			firePropertyChange(VISIBLE_PROPERTY, Boolean.valueOf(old),Boolean.valueOf(b));
		}
	}
	/** access the visibility of the action */
	public boolean isVisible() {
		return visible;
	}
	
	public UIActionContribution() {
        super();
    }
    
    /** invoke the specified method on the object */
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.YES_OPTION;
        if (confirmMessage != null  && confirmMessage.trim().length() > 0) {
            result = JOptionPane.showConfirmDialog(parentImpl,confirmMessage,"Confirmation",JOptionPane.YES_NO_OPTION);
        }
        if (result == JOptionPane.YES_OPTION) {
        	InvokerWorker op = parentImpl.new InvokerWorker(this);
        	if (onEventDispatchThread) { //run direct on swing thread.
        		SwingUtilities.invokeLater(op);
        	} else {
        		op.start(); // run in background.
        	}
        }
    }
    
    /** adds a parameter to the method call */
    public void addParameter(Object v) {
        parameters.add(v);
    }
    /** hivemind ordering constraint */
    public String getAfter() {
        return this.after;
    }
    /** hivemind ordering constraint */
    public String getBefore() {
        return this.before;
    }
    
    /** if non-null, will prompt user for confirmation before executing method */
    public String getConfirmMessage() {
        return this.confirmMessage;
    }
        
    
  /** display icon */
    public Icon getIcon() {
        return (Icon)getValue(SMALL_ICON);
    }
    /** name of the method to invoke */
    public String getMethodName() {
        return this.methodName;
    }
    
    /** name of this action object - used to refer to it by other objects in ordering system */
    public String getName() {
        return this.name;
    }
    /** object to invoke the method on */
    public Object getObject() {
        return this.object;
    }
    /** list of parameters to pass to method */
    public List getParameters() {
        return parameters;
    }
    /** parent of this object */
    public String getParentName() {
        return this.parentName;
    }
    /** text label for this action */
    public String getText() {
        return (String)getValue(NAME);
    }
    public void setAfter(String after) {
        this.after = after;
    }
    public void setBefore(String before) {
        this.before = before;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public void setIconName(String i) {
        putValue(SMALL_ICON,IconHelper.loadIcon(i));
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setText(String text) {
        putValue(NAME,text);
    }

    public void setToolTipText(String text) {
        putValue(SHORT_DESCRIPTION,text);
    }
    /** tooltip for this action */
    public String getToolTipText() {
    	return (String)getValue(SHORT_DESCRIPTION);
    }

    /** the 'parent' ui of this action - used for event notification when this action is triggered. */
    public void setUIImpl(UIImpl p){
        this.parentImpl= p;
    }

	/** an optional HelpID to link to for further help */
	public String getHelpId() {
		return this.helpId;
	}

	public void setHelpId(String helpId) {
		this.helpId = helpId;
	}

	/** if true, this action should be executed on the EDT.
	 * if false (the default) it should be executed on a background thread
	 * @return
	 */
	public boolean isOnEventDispatchThread() {
		return this.onEventDispatchThread;
	}

	public void setOnEventDispatchThread(boolean onEventDispatchThread) {
		this.onEventDispatchThread = onEventDispatchThread;
	}
}


/* 
$Log: UIActionContribution.java,v $
Revision 1.9  2007/01/29 10:46:28  nw
allow to execute actions on EDT in some cases.

Revision 1.8  2007/01/23 11:50:49  nw
preferences integration.

Revision 1.7  2007/01/11 18:15:50  nw
fixed help system to point to ag site.

Revision 1.6  2007/01/10 19:12:16  nw
integrated with preferences.

Revision 1.5  2007/01/10 14:55:30  nw
integrated with preference system.

Revision 1.4  2006/06/27 19:13:37  nw
minor tweaks

Revision 1.3  2006/06/15 09:57:05  nw
doc fix

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/