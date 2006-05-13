/*$Id: ToolModel.java,v 1.3 2006/05/13 16:34:55 nw Exp $
 * Created on 08-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.editors.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.editors.AbstractToolEditorPanel;
import org.astrogrid.workflow.beans.v1.Tool;

/** Model for a tool editor. - maintains a tool and it's associated application information
 * <p>
 * used to attach multiiple editors to the same tool - (MVC) so that it's possible to present 
 * different views/ editors for the same data.
 * <p>
 * Works by reference - editos take references to the tool contained in this model, and then call the 
 * mutators on it to alter parameter values, etc. editors are responsible for firing events to 
 * notify other listners when an alteration to the tool is made
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2005
 *
 */
public class ToolModel {

    /** Construct a new ToolModel
     * 
     */
    public ToolModel() {
        super();
    }
        
    private Tool tool;
    private ApplicationInformation info;
    
    /**
     * Identifier for a security action. The action is to be 
     * performed by the service delegate when the Tool is serialized
     * in a call to a web service; typically, the action means
     * "sign the request message". The format of the identifier is
     * opaque to this class (hence it's stored as a plain String), 
     * but is likely to be a URI identifiying some security standard.
     */
    private String securityMethod;
    
    /** get a reference to the tool managed by this model */
    public  Tool getTool() {
        return tool;
    }
    
    /**
     * Retrieve the security method.
     */
    public String getSecurityMethod() {
      return this.securityMethod;
    }
    
    /**
     * Specify the security method.
     */
    public void setSecurityMethod(String method) {
      this.securityMethod = method;
    }
    
    /** populate the model, with a tool and it's associated description
     * <p>
     * @fires toolSetEvent
     * @param t
     * @param info
     */
    public void populate(Tool t, ApplicationInformation info) {
        this.tool = t;
        this.info = info;
        fireToolSetEvent();
    }
    /**
     * clear the model (sets tool and info to null)
     *@fires toolClearedEvent
     */
    public void clear() {
        tool= null;
        info = null;
        fireToolClearedEvent();
    }
    
    private final Set listenerSet = new HashSet();
    public final void addToolEditListener(ToolEditListener l) {
        if (l != null) {          
            listenerSet.add(l);
        }
    }
    public final  void removeToolEditListener(ToolEditListener l) {
        listenerSet.remove(l);
    }
    /**
     * fire a tool set event
     *
     */
    protected final void fireToolSetEvent() {
        ToolEditEvent e = null;
        for(Iterator i = listenerSet.iterator(); i.hasNext(); ) {
            ToolEditListener l = (ToolEditListener)i.next();
            if (e == null){ // lazily create evet object
                e = new ToolEditEvent(this);
            }
            l.toolSet(e);
        }
    }
    
    /**
     * fire a tool changed event
     *
     */
    public final void fireToolChangedEvent() {
        ToolEditEvent e = null;
        for(Iterator i = listenerSet.iterator(); i.hasNext(); ) {
            ToolEditListener l = (ToolEditListener)i.next();
            if (e == null){ // lazily create evet object
                e = new ToolEditEvent(this);
            }
            l.toolChanged(e);
        }        
    }
    /**
     * fire a tool cleared event
     *
     */
    protected final void fireToolClearedEvent() {
        ToolEditEvent e = null;
        for(Iterator i = listenerSet.iterator(); i.hasNext(); ) {
            ToolEditListener l = (ToolEditListener)i.next();
            if (e == null){ // lazily create evet object
                e = new ToolEditEvent(this);
            }
            l.toolCleared(e);
        }        
    }
    /** fire a parameter added event
     * 
     * @param edit editor that caused the add of the parameter 
     * @param pv new parameter
     */
    public final void fireParameterAdded(AbstractToolEditorPanel edit,ParameterValue pv) {
        ToolEditEvent e = null;
        for(Iterator i = listenerSet.iterator(); i.hasNext(); ) {
            ToolEditListener l = (ToolEditListener)i.next();
            if (e == null){ // lazily create evet object
                e = new ToolEditEvent(edit,pv);
            }
            l.parameterAdded(e);
        }        
    }
  /**
   * fire a parameter removed event
   * @param edit editor that caused the removal of the parameter
   * @param pv parameter that has been removed
   */
    public final void fireParameterRemoved(AbstractToolEditorPanel edit,ParameterValue pv) {
        ToolEditEvent e = null;
        for(Iterator i = listenerSet.iterator(); i.hasNext(); ) {
            ToolEditListener l = (ToolEditListener)i.next();
            if (e == null){ // lazily create evet object
                e = new ToolEditEvent(edit,pv);
            }
            l.parameterRemoved(e);
        }        
    }
    /**
     * fire a parameter changed event
     * @param edit editor that caused the change of parameter value
     * @param pv altered parameter.
     */
    public final void fireParameterChanged(AbstractToolEditorPanel edit,ParameterValue pv) {
        ToolEditEvent e = null;
        for(Iterator i = listenerSet.iterator(); i.hasNext(); ) {
            ToolEditListener l = (ToolEditListener)i.next();
            if (e == null){ // lazily create evet object
                e = new ToolEditEvent(edit,pv);
            }
            l.parameterChanged(e);
        }        
    }

    public ApplicationInformation getInfo() {
        return this.info;
    }

}


/* 
$Log: ToolModel.java,v $
Revision 1.3  2006/05/13 16:34:55  nw
merged in wb-gtr-1537

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.56.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/