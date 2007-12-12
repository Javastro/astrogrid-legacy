/*$Id: DalProtocol.java,v 1.12 2007/12/12 13:54:12 nw Exp $
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

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;

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

    public DalProtocol(String name, Image img) {
        super();
        this.name = name;
        this.primaryNode = new ImageTreeNode();
        primaryNode.setAttribute(Retriever.LABEL_ATTRIBUTE,name);
        primaryNode.setImage(img);
        this.checkBox = new JCheckBox(name);
        this.checkBox.putClientProperty(OWNER,this);
        this.menuCheckBox = new JCheckBoxMenuItem(name);
        this.menuCheckBox.setModel(this.checkBox.getModel());
        this.menuCheckBox.putClientProperty(OWNER,this);
        // setting shared between two models.
        this.checkBox.setSelected(true);
    }
    public static final Class OWNER = DalProtocol.class;
    private final String name;
    private final ImageTreeNode primaryNode;
    private final JCheckBox checkBox;
    private final JCheckBoxMenuItem menuCheckBox;
    
    public String getName() {
        return name;
    }
    
    
    /** access the primary node - from where all other results from this protocol
     * will be rooted
     */
    public final ImageTreeNode getPrimaryNode() {
        return primaryNode;
    }
    
    /** get a UI component used for selecting / deselecting this protocol */
    public final JCheckBox getCheckBox() {
        return checkBox;
    }
    /** get a menu item for selecting / deselecting this protocol */
    public final JCheckBoxMenuItem getMenuItemCheckBox() {
        return menuCheckBox;
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
Revision 1.12  2007/12/12 13:54:12  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.11  2007/06/18 16:42:36  nw
javadoc fixes.

Revision 1.10  2007/05/03 19:20:42  nw
removed helioscope.merged into uberscope.

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