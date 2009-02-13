/*$Id: RegistryGoogle.java,v 1.7 2009/02/13 17:42:19 nw Exp $
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

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;

/** AR Service: Dialogue that prompts the user to select a registry resource 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Sep-2005
 * @service dialogs.registryGoogle
 * @see #selectResourcesFromList(String, boolean, URI[]) Example 1
 * @see #selectResourcesXQueryFilter(String, boolean, String) Example 2
 * @see Registry
 */
public interface RegistryGoogle {
  
    /** Display the resource chooser dialogue, populated with
     * resources described by a SRQL query - the little language used
     * in VOExplorer SmartLists. 
     * @param prompt the prompt to show the user
     * @param multiple if true, allow multiple selections
     * @param srql The query used to populate the dialogue.
     * @return 0 or more selected resources, never null.
     * @throws InvalidArgumentException  if srql cannot be parsed
   * @see <a href='http://eurovotech.org/twiki/bin/view/VOTech/SimpleRegistryQueryLanguage'>SRQL Language Description</a> 
       
     */
    Resource[] selectResources(String prompt, boolean multiple,String srql) throws InvalidArgumentException;

    
    /** display the resource chooser dialogue, enabling only resources which match a filter.
     * 
     * @param prompt message to prompt user for input.
     * @param multiple if true, allow multiple selections.
     * @param adqlFilter adql-like 'where' clause.
     * @return 0 or more selected resources. never null.
     * @exclude
     *  @deprecated unimplemented
     */
    @Deprecated
    Resource[] selectResourcesAdqlFilter(String prompt, boolean multiple, String adqlFilter);
    
    /** Display the resource chooser dialogue, populated with resources from an xquery.
     * 
     * {@example "Python Example"
     *  # connect to the AR
     * from xmlrpc import Server
     * from os.path import expanduser
     * ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
     *  #call this function
     * xquery = """
     * for $r in //vor:Resource[not (@status='inactive' or @status='deleted')] 
     * where $r/@xsi:type  &=  '*DataCollection' 
     * return $r
     * """
     * rs = ar.dialogs.registryGoogle.selectResourcesXQueryFilter("Choose a DataCollection",True,xquery)
     * #list the selected identifiers
     * for r in rs:
     *  print r['id']
     *  }
     * 
     * 
     * @param prompt message to prompt user for input.
     * @param multiple if true, allow multiple selections.
     * @param xqueryFilter a xquery to populate the resource chooser with. Same format as in an xquery list in voexplorer UI - which is convenient for
     * constructing these queries.
     * @return 0 or more selected resources. never null.
     */    
    Resource[] selectResourcesXQueryFilter(String prompt, boolean multiple, String xqueryFilter);
    
    /** Display the resource chooser dialogue, populated with a list of resources.
     * 
     * {@example "Python Example"
     *  # connect to the AR
     * from xmlrpc import Server
     * from os.path import expanduser
     * ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
     *  #call this function
     * ids = ['ivo://irsa.ipac/2MASS-PSC'
     *         ,'ivo://mast.stsci/siap-cutout/goods.hst'
     *         ,'ivo://stecf.euro-vo/SSA/HST/FOS'
     *         ,'ivo://uk.ac.cam.ast/iphas-dsa-catalog/IDR'
     *         ,'ivo://uk.ac.cam.ast/IPHAS/images/SIAP'
     *         ]
     * rs = ar.dialogs.registryGoogle.selectResourcesFromList("Choose a DataCollection",True,ids)
     * #list the selected identifiers
     * for r in rs:
     *  print r['id']
     *  }
     * 
     * 
     * @param prompt message to prompt user for input
     * @param multiple if true, allow multiple selections.
     * @param identifiers an array of resource identifiers that will be retrieved from registry and displayed in the dialogue.
     * @return 0 or more selected resources. never null.
     */
    Resource[] selectResourcesFromList(String prompt, boolean multiple, URI[] identifiers);
}


/* 
$Log: RegistryGoogle.java,v $
Revision 1.7  2009/02/13 17:42:19  nw
Complete - taskUse SRQL in AR

Revision 1.6  2008/09/25 16:02:02  nw
documentation overhaul

Revision 1.5  2008/08/21 11:35:00  nw
Complete - task 4: RegistryGoogle dialogue

Revision 1.4  2007/03/08 17:47:12  nw
updated interfaces.

Revision 1.3  2007/01/24 14:04:44  nw
updated my email address

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