/*$Id: DalProtocol.java,v 1.9 2007/03/08 17:43:56 nw Exp $
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

import java.util.List;

import javax.swing.JCheckBox;

import org.astrogrid.acr.ivoa.resource.Service;

import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * encapsulation of an entire data access protocol - name, listing services in registry,
 * querying them, and adding results to a subtree of the display.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
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
    public abstract Service[] listServices() throws Exception;
    
    
	/** produce a list of all services suitable to this protocol
	 * @param resourceList input list to filter
	 * @return the subset that this protocol can query
	 */
	public abstract Service[] filterServices(List resourceList) ;
   
}


/* 
$Log: DalProtocol.java,v $
Revision 1.9  2007/03/08 17:43:56  nw
first draft of voexplorer

Revision 1.8  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.7  2006/08/15 10:01:12  nw
migrated from old to new registry models.

Revision 1.6  2006/05/26 15:11:58  nw
tidied imported.corrected number formatting.

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