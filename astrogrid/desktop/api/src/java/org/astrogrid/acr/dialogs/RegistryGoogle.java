/*$Id: RegistryGoogle.java,v 1.2 2006/08/31 20:21:39 nw Exp $
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
import org.astrogrid.acr.ivoa.resource.Resource;

/**prompt the user to select a registry resource by displaying  a more advanced registry chooser dialogue.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Sep-2005
 * @service dialogs.registryGoogle
 */
public interface RegistryGoogle {
  
    /** display the resource chooser dialogue.
     * 
     * @param prompt message to prompt user for input.
     * @param multiple if true, allow multiple selections.
     * @return 0 or more selected resources. never null.
     */
    Resource[] selectResources(String prompt, boolean multiple);

    
    /** display the resource chooser dialogue, enabling only resources which match a filter.
     * 
     * @param prompt message to prompt user for input.
     * @param multiple if true, allow multiple selections.
     * @param adqlFilter adql-like 'where' clause.
     * @return 0 or more selected resources. never null.
     */
    Resource[] selectResourcesAdqlFilter(String prompt, boolean multiple, String adqlFilter);
    
    /** display the resource chooser dialogue, enabling only resources which match a filter
     * 
     * @param prompt message to prompt user for input.
     * @param multiple if true, allow multiple selections.
     * @param xqueryFilter xpath-like condition
     * @return 0 or more selected resources. never null.
     */    
    Resource[] selectResourcesXQueryFilter(String prompt, boolean multiple, String xqueryFilter);
}


/* 
$Log: RegistryGoogle.java,v $
Revision 1.2  2006/08/31 20:21:39  nw
improved documentation

Revision 1.1  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.3  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.2  2005/09/12 15:21:43  nw
added stuff for adql.

Revision 1.1  2005/09/05 11:09:19  nw
added interfaces for registry and query dialogs
 
*/