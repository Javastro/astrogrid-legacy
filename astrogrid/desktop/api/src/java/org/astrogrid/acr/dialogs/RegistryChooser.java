/*$Id: RegistryChooser.java,v 1.3 2006/02/02 14:19:47 nw Exp $
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

/**prompt the user to select a registry resource by displaying  a  registry chooser dialogue.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 * * @service  dialogs.registryChooser
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
Revision 1.3  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.2  2005/09/12 15:21:43  nw
added stuff for adql.

Revision 1.1  2005/09/05 11:09:19  nw
added interfaces for registry and query dialogs
 
*/