/*$Id: UIActionContribution.java,v 1.2 2006/04/18 23:25:43 nw Exp $
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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.UIImpl;
import org.astrogrid.desktop.modules.system.UIImpl.InvokerWorker;

/** contribution to ui that represents a button
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2006
 *
 */
public class UIActionContribution extends AbstractAction implements UIStructureContribution{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(UIActionContribution.class);

    private String after;
    
    private String before;
    private String confirmMessage;
    private String methodName;
    private String name;
    private Object object;
    private List parameters= new ArrayList();
    private UIImpl parentImpl;
    private String parentName;
    
    public UIActionContribution() {
        super();
    }
    
    
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.YES_OPTION;
        if (confirmMessage != null  && confirmMessage.trim().length() > 0) {
            result = JOptionPane.showConfirmDialog(parentImpl,confirmMessage,"Confirmation",JOptionPane.YES_NO_OPTION);
        }
        if (result == JOptionPane.YES_OPTION) {
            InvokerWorker op = parentImpl.new InvokerWorker(this);
            op.start();
        }
    }
    
    public void addParameter(Object v) {
        parameters.add(v);
    }
    
    public String getAfter() {
        return this.after;
    }
    
    public String getBefore() {
        return this.before;
    }
    
    public String getConfirmMessage() {
        return this.confirmMessage;
    }
        
    
  
    public Icon getIcon() {
        return (Icon)getValue(SMALL_ICON);
    }
    
    public String getMethodName() {
        return this.methodName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Object getObject() {
        return this.object;
    }
    public List getParameters() {
        return parameters;
    }
    public String getParentName() {
        return this.parentName;
    }
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

    public void setUIImpl(UIImpl p){
        this.parentImpl= p;
    }

}


/* 
$Log: UIActionContribution.java,v $
Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/