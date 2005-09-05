/*$Id: QueryEditorPanel.java,v 1.1 2005/09/05 11:08:39 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.query;

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.dialogs.ResourceChooser;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**Implementaiton of the query editor
 *
 */
public class QueryEditorPanel extends JPanel {



    /** Construct a new QueryEditorPanel
     * @param parent parent window, with status display.
     * @param regChooser registry google dialogue
     * @param reg registry component
     * @param apps applications component
     * @param resourceChooser myspace chooser dialogue
     * @param myspace myspace componet.
     * 
     */
    public QueryEditorPanel(UIComponent parent,
            RegistryChooser regChooser, Registry reg,
            ApplicationsInternal apps,
            ResourceChooserInternal resourceChooser, MyspaceInternal myspace) {
        super();
        this.parent = parent;
        this.regChooser = regChooser;
        this.reg = reg;
        this.apps = apps;
        this.resourceChooser = resourceChooser;
        this.myspace = myspace;
        initialize();
    }
    
    /** parent ui window - used to display progress of background threads, etc 
     * 
     * pass this reference to any {@link org.astrogrid.desktop.modules.ui.BackgroundWorker} objects created
     * */
    protected final UIComponent parent;
    
    /** use to get registry chooser dialogue */
    protected final RegistryChooser regChooser;
    
    /** registry component - use for queries */
    protected final Registry reg;
    
    /** applications component - use to create template tools */
    protected final ApplicationsInternal apps;
    
    /** use to get myspace browse dialogue */
    protected final ResourceChooser resourceChooser;
    
    /** use to read and write myspace resources */
    protected final MyspaceInternal myspace;
    
    /** assemble the ui 
     * @todo implement */
    private void initialize() {
    }
    
    /** 

    /** clear display, set getTool == null;
     * @todo implement
     */
    public void clear() {
    }

    /** @todo implement
     * @return the uptodate edited tool
     */
    public Tool getTool() {
        return null;
    }

    /** set the tool to display. if no tool is set, or null is passed,
     * most of panel is grayed out, and user has to select a datacenter application before proceeding.
     * @param t tool to display, or null to indicate 'no application selected'
     * @todo implement
     */
    public void setTool(Tool t) {
    }

}


/* 
$Log: QueryEditorPanel.java,v $
Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/