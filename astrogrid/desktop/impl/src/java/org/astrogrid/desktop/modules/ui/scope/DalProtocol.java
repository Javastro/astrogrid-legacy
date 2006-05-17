/*$Id: DalProtocol.java,v 1.5 2006/05/17 15:45:17 nw Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import java.util.Calendar;

import javax.swing.JCheckBox;

import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ui.UIComponent;

import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * encapsulation of an entire data access protocol - name, listing services in registry,
 * querying them, and adding results to a subtree of the display.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2006
 *
 */
public abstract class DalProtocol {

    public DalProtocol(String name) {
        super();
        this.name = name;
        this.primaryNode = new DefaultTreeNode();
        this.checkBox = new JCheckBox(name);
        this.checkBox.setSelected(true);
    }
    private final String name;
    private final TreeNode primaryNode;
    private final JCheckBox checkBox;
    
    public String getName() {
        return name;
    }
    
    public void setPrimaryNodeLabel(String name) {
        this.primaryNode.setAttribute(Retriever.LABEL_ATTRIBUTE,name);
    }
    
    /** access the primary node - from where all other results from this protocol
     * will be rooted
     * @return
     */
    public final TreeNode getPrimaryNode() {
        return primaryNode;
    }
    
    /** get a UI component used for selecting / deselecting this protocol */
    public final JCheckBox getCheckBox() {
        return checkBox;
    }
    
    // back-to-front setter injection - neccessary, as there's a
    // circular dependency between vizModel and dalProtocol
    
    private VizModel vizModel;
    /** to be only called by VizModel */
    public void setVizModel(VizModel vm) {
        this.vizModel = vm;
    }
    
    protected VizModel getVizModel() {
        return vizModel;
    }
    
    /** produce a list of all known services of this protocol
     *  -- typically by querying the registry
     * @return
     */
    public abstract ResourceInformation[] listServices() throws Exception;
    
   
}


/* 
$Log: DalProtocol.java,v $
Revision 1.5  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

Revision 1.4  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.3  2006/03/24 10:30:15  KevinBenson
new checkboxes on heliosope for the Format, and the ability to query by Format
for stap services on helioscope

Revision 1.2  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/