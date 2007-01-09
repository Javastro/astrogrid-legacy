/*$Id: ConfigurationInternal.java,v 1.3 2007/01/09 16:25:33 nw Exp $
 * Created on 23-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.apache.hivemind.SymbolSource;
import org.apache.hivemind.service.ObjectProvider;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.system.Configuration;

/** An OO self-describing interface to the configuration system.
 * The methods in this class are Preferred, for internal use, to the Configuration interface.
 * The other interfaces extended are hivemind internal things - their methods should
 * not be called directly.
 * @see PreferenceManagerImpl for full documentation
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Aug-2005
 *
 */
public interface ConfigurationInternal extends Configuration, ObjectProvider, SymbolSource {
	/** finds a named preference.
	 * @param name
	 * @return a preference object. will never return null.
	 * @throws IllegalArgumentException if named preference is not found.
	 */
	public Preference find(String name) throws IllegalArgumentException;

	
    /** reset the configuration back to factory settings. any user history or configuraiton will be lost 
     * @throws ServiceException*/
    public void reset() throws ServiceException;
}


/* 
$Log: ConfigurationInternal.java,v $
Revision 1.3  2007/01/09 16:25:33  nw
new preferences system

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/