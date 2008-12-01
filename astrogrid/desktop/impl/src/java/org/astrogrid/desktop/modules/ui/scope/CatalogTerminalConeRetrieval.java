package org.astrogrid.desktop.modules.ui.scope;

import java.net.URI;

import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.Service;
import org.xml.sax.SAXException;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/**Variant retriever for a cone service that doesn't draw bubbles below the catalog name. 
 * */
public class CatalogTerminalConeRetrieval extends ConeRetrieval {
    

	public CatalogTerminalConeRetrieval(final Service service, final ConeCapability cap, final URI acurl, final NodeSocket socket, final VizModel model, final Cone cone, final double ra, final double dec, final double sz) {
		super(service, cap, acurl, socket, model, cone, ra, dec, sz);
	}

	@Override
    protected BasicTableHandler createTableHandler(final TreeNode serviceNode) {
		return new CatalogTerminalTableHandler(serviceNode);
	}
	
	/** table handler that just counts the number of rows */
    public class CatalogTerminalTableHandler extends BasicTableHandler {

		/**
		 * @param serviceNode
		 */
		public CatalogTerminalTableHandler(final TreeNode serviceNode) {
			super(serviceNode);
		}
		/** overridden - don't care about data - just want to count the rows */
		@Override
        public void rowData(final Object[] row) throws SAXException {
		    isWorthProceeding();
	        resultCount++;
		}
		@Override
        public void endTable() throws SAXException {
			serviceNode.setAttribute(RESULT_COUNT,Integer.toString(resultCount));
		super.endTable();
		}
    }
    
}
