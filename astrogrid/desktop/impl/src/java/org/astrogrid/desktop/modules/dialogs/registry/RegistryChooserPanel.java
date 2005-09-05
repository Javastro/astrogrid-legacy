/*$Id: RegistryChooserPanel.java,v 1.1 2005/09/05 11:08:39 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.registry;

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ui.UIComponent;

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Implementation of the registry-google chooser.
 *@todo later add bookmark component. - won't affect public inteface, just implementation
 */
public class RegistryChooserPanel extends JPanel {



    /** Construct a new RegistryChooserPanel
     * 
     */
    public RegistryChooserPanel(UIComponent parent,Registry reg) {
        super();    
        this.parent = parent;
        this.reg = reg;
        initialize();
   }
    
    /** parent ui window - used to display progress of background threads, etc 
     * 
     * pass this reference to any {@link org.astrogrid.desktop.modules.ui.BackgroundWorker} objects created
     * */
    protected final UIComponent parent;
    /** registry component - use for queries */
    protected final Registry reg;
    /** boolean flag to indicate multiple resources can be selected */
    protected boolean multiple;
    /** assemble the ui 
     * @todo implement */
    private void initialize() {
    }
    
    /** set an additional result filter
     * @todo implemnt
     * @param filter an adql-like where clause, null indicates 'no filter'
     */
   public void setFilter(String filter) {
   }
   
   /** set whether user is permitted to select multiple resources 
    * @param multiple if true, multiple selection is permitted.*/
   public void setMultipleResources(boolean multiple) {
       //@todo add event handlers, gui logic to alter display to enable / disable multiple selection.
       this.multiple = multiple;
   }
   
   public boolean isMultipleResources() {
       return multiple;
   }
   
   /** clear display, set selectedResources == null 
    * @todo implement
    *
    */
   public void clear() {
   }

   /** access the resources selected by the user
    * @todo implement
    * @return
    */
   public ResourceInformation[] getSelectedResources() {
       return null;
   }

   
}


/* 
$Log: RegistryChooserPanel.java,v $
Revision 1.1  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs
 
*/