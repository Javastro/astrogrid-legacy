package org.astrogrid.desktop.modules.ui.scope;

import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.resource.Service;
import org.xml.sax.SAXException;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/** taks that retreives, parses and adds to the display the results of one cone service 
 *  Variation that doesn't draw bubbles below the catalog name - as suggested by andy.
 * */
public class CatalogTerminalConeRetrieval extends ConeRetrieval {
    

	public CatalogTerminalConeRetrieval(Service service, TreeNode primaryNode, VizModel model, Cone cone, double ra, double dec, double sz) {
		super(service, primaryNode, model, cone, ra, dec, sz);
	}

	protected BasicTableHandler createTableHandler(TreeNode serviceNode) {
		return new CatalogTerminalTableHandler(serviceNode);
	}
	
	/** table handler that just counts the number of rows */
    public class CatalogTerminalTableHandler extends ConeTableHandler {

		/**
		 * @param serviceNode
		 */
		public CatalogTerminalTableHandler(TreeNode serviceNode) {
			super(serviceNode);
		}
		/** overridden - don't care about data - just want to count the rows */
		public void rowData(Object[] row) throws SAXException {
	        if (!isWorthProceeding()) { // no point, not enough metadata - sadly, get called for each row of the table - no way to bail out.
	            message = "Insufficient table metadata";
	            throw new DalProtocolException(message);
	        }
	        resultCount++;
		}
		public void endTable() throws SAXException {
			serviceNode.setAttribute(RESULT_COUNT,Integer.toString(resultCount));
		super.endTable();
		}
    }
    
}