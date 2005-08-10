/*$Id: CEAComponentManager.java,v 1.5 2005/08/10 14:45:37 clq2 Exp $
 * Created on 04-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.component;

import org.astrogrid.applications.description.registry.RegistryUploader;
import org.astrogrid.applications.manager.ControlService;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.component.ComponentManager;

/** Definition of the services a cea server must provide to the outside world. 
 * < p/>
 * Extension of the component manager inteface - which provides introspection and reporting methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 04-May-2004
 *
 */
public interface CEAComponentManager extends ComponentManager {
    /** @return the controller component that does work */
    public ExecutionController getExecutionController();
    /** @return the meta data component that describes what this controller provides */
    public MetadataService getMetadataService();
    /** @return the query component that allows inspection of progress */
    public QueryService getQueryService();
    /** @return the component that will upload to the registry */
    public RegistryUploader getRegistryUploaderService();
    
    public ControlService getControlService();
    
}


/* 
$Log: CEAComponentManager.java,v $
Revision 1.5  2005/08/10 14:45:37  clq2
cea_pah_1317

Revision 1.4.86.1  2005/07/21 15:10:22  pah
changes to acommodate contol component, and starting to change some of the static methods to dynamic

Revision 1.4  2004/11/27 13:20:03  pah
result of merge of pah_cea_bz561 branch

Revision 1.3.76.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.3  2004/07/23 13:21:21  nw
Javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.2  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)

Revision 1.1.2.1  2004/05/21 12:00:22  nw
merged in latest changes from HEAD. start of refactoring effort
 
*/