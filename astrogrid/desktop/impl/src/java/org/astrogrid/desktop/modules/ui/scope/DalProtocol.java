/*$Id: DalProtocol.java,v 1.1 2006/02/02 14:51:11 nw Exp $
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

import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ui.UIComponent;

import javax.swing.JCheckBox;

import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * encapsulation of an entire data access protocol - name, listing services in registry,
 * querying them, and adding results to a subtree of the display.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2006
 *
 */
public abstract class DalProtocol {

    public DalProtocol(String name,UIComponent parent) {
        super();
        this.parent = parent;
        this.name = name;
        this.primaryNode = new DefaultTreeNode();
        this.checkBox = new JCheckBox(name);
        this.checkBox.setSelected(true);
    }
    protected final UIComponent parent;
    private final String name;
    private final TreeNode primaryNode;
    private final JCheckBox checkBox;
    
    public String getName() {
        return name;
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
    
    
    /** create a retriever to query one service of this protocol
     * 
     * @param i the resource information describing this protocol
     * @param ra right ascension search position
     * @param dec declination of search position
     * @param raSize ra size of search
     * @param decSize dec size of search
     * @return an initialized, unstarted retriever.
     */
    public abstract Retriever createRetriever(ResourceInformation i, double ra, double dec, double raSize, double decSize);



}


/* 
$Log: DalProtocol.java,v $
Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/