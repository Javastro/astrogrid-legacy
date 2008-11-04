package org.astrogrid.desktop.modules.ui.folders;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLParser;

/** A dynamic folder of resouces 
 *  defined by a SRQL expression.
 *  @TEST mock out display, edit, equals.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20075:29:27 PM
 */
public class SmartList extends ResourceFolder {

	public SmartList(final String name,final String srql) throws InvalidArgumentException {
		this();
		setName(name);
		final SRQLParser p = new SRQLParser(srql);
		final SRQL sq = p.parse();
		if (sq == null) {
			throw new InvalidArgumentException("Failed to parse");
		}
		setQuery(sq);
	}
	
	public SmartList(final String name, final SRQL query) {
		super(name, defaultIcon);
		setQuery(query);
	}	
	
	public SmartList() {
		super("New Smartlist",defaultIcon);
	}

	public void setQuery(final SRQL query) {
		this.query = query;
	}
	public SRQL getQuery() {
		return query;
	}

	public static final String defaultIcon = "smartfolder16.png";
	private SRQL query;
		

	
	public void display(final RegistryGooglePanel p) {
		logger.info("Displaying " + getName());
		p.displayQuery("Contents of " + getName(),query);
	}

	public void edit(final VOExplorerImpl voe) {
		voe.editExistingSmartList(this);
	}

	public void editAsNew(final VOExplorerImpl voe) {
		voe.editNewSmartList(this);
	}

    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.query == null) ? 0 : this.query.hashCode());
        return result;
    }

    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SmartList other = (SmartList) obj;
        if (this.query == null) {
            if (other.query != null) {
                return false;
            }
        } else if (!this.query.equals(other.query)) {
            return false;
        }
        return true;
    }
	

}