/*$Id: RegistryChooser.java,v 1.1 2005/09/05 11:09:19 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.dialogs;

import org.astrogrid.acr.astrogrid.ResourceInformation;

/** interface to a registry chooser dialogue.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 *
 */
public interface RegistryChooser {
    /** display the registry chooser dialogue, 
     * @param prompt message to prompt user
     * @return selected resgistry resource */
    ResourceInformation chooseResource(String prompt);
   /** display the resource chooser dialogue, and enable resources that only match the filter 
 * @param prompt message to prompt user
 * @param filter adql-like 'where' clause.
 * @return selected registry resource*/
    ResourceInformation chooseResourceWithFilter(String prompt, String filter);


    /** multiple selection variant of {@link #chooseResource} */
    ResourceInformation[] chooseResources(String prompt);
    /** multiple selection variant of {@link #chooseResourceWith Filter} */
    ResourceInformation[] chooseResourcesWithFilter(String prompt,String filter);
}


/* 
$Log: RegistryChooser.java,v $
Revision 1.1  2005/09/05 11:09:19  nw
added interfaces for registry and query dialogs
 
*/