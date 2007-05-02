package org.astrogrid.desktop.modules.ui.folders;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLParser;

/** represents a smart / dynamic folder of resouces 
 *  defined by a filter expression
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20075:29:27 PM
 */
public class SmartList extends ResourceFolder {

	public SmartList(String name,String srql) throws InvalidArgumentException {
		this();
		setName(name);
		SRQLParser p = new SRQLParser(srql);
		SRQL sq = p.parse();
		if (sq == null) {
			throw new InvalidArgumentException("Failed to parse");
		}
		setQuery(sq);
	}
	
	public SmartList(String name, SRQL query) {
		super(name, defaultIcon);
		setQuery(query);
	}	
	
	public SmartList() {
		super("New Smartlist",defaultIcon);
	}

	public void setQuery(SRQL query) {
		this.query = query;
	}
	public SRQL getQuery() {
		return query;
	}

	public static final String defaultIcon = "smartfolder16.png";
	private SRQL query;
		

	
	public void display(RegistryGooglePanel p) {
		p.displayQuery(getName(),query);
	}

	public void edit(VOExplorerImpl voe) {
		voe.editExistingSmartList(this);
	}

	public void editAsNew(VOExplorerImpl voe) {
		voe.editNewSmartList(this);
	}
	

}