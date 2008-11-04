/*$Id: ConfigurationInternal.java,v 1.8 2008/11/04 14:35:49 nw Exp $
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
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.pref.PreferenceManagerImpl;

/** Internal interface to configuration, that exposes properties as Preference objects.
 * The methods in this class are Preferred, for internal use, to the Configuration interface.
 * The other interfaces extended are hivemind internal things - their methods should
 * not be called directly.
 * @see PreferenceManagerImpl for full documentation
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 23-Aug-2005
 *
 */
public interface ConfigurationInternal extends Configuration, ObjectProvider, SymbolSource {
	/** finds a named preference.
	 * @param name
	 * @return a preference object. will never return null.
	 * @throws IllegalArgumentException if named preference is not found.
	 */
	public Preference find(String name) throws IllegalArgumentException;

}


/* 
$Log: ConfigurationInternal.java,v $
Revision 1.8  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.7  2007/09/21 16:35:13  nw
improved error reporting,
various code-review tweaks.

Revision 1.6  2007/06/18 16:56:19  nw
made reset() method public

Revision 1.5  2007/04/18 15:47:07  nw
tidied up voexplorer, removed front pane.

Revision 1.4  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.3  2007/01/09 16:25:33  nw
new preferences system

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/