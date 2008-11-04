/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
/** Resource folder populated by an XQuery.
 * 
 * @TEST mock out calls to voexp
 * */
public class XQueryList extends ResourceFolder {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(XQueryList.class);

	public XQueryList() {
		super("New XQuerylist",SmartList.defaultIcon);
	}
	public XQueryList(final String name, final String query) {
		super(name, SmartList.defaultIcon);
		this.query = query;
	}
	private String query;
	public String getQuery() {
		return this.query;
	}
	public void setQuery(final String query) {
		this.query = query;
	}
	public void display(final RegistryGooglePanel p) {
		logger.info("Displaying " + getName());
		p.displayQuery("Contents of " + getName(),query);
	}

	public void edit(final VOExplorerImpl voe) {
		voe.editExistingQueryList(this);
	}
	public void editAsNew(final VOExplorerImpl voe) {
		voe.editNewQueryList(this);
	}
}