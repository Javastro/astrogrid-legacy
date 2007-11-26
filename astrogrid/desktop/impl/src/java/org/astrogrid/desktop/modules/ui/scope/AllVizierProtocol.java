/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.Validation;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.StarTable;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 7, 20073:35:59 PM
 */
public class AllVizierProtocol extends SpatialDalProtocol {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(AllVizierProtocol.class);


	public AllVizierProtocol(Cone cone) {
		super("VizieR Tables");
		this.cone = cone;
		getPrimaryNode().setAttribute(Retriever.SERVICE_LOGO_ATTRIBUTE,"http://vizier.u-strasbg.fr/vizier_tiny.gif");
		getPrimaryNode().setAttribute(Retriever.SERVICE_ID_ATTRIBUTE,"ivo://CDS/Vizier");
		//@todo factor this out into somewhere more logical - like config, or the vizier ar component.
		// maybe that should be http-get based too? yep. most probably.
		URI u = URI.create("http://vizier.u-strasbg.fr/viz-bin/votable/-dtd/-A");
		vizierEndpoint = u;
	}
	private final Cone cone;
	private final URI vizierEndpoint;
	public Service[] filterServices(List resourceList) {
		return new Service[0]; // never run in filtered mode.
	}

	public Service[] listServices() throws Exception {
		return vizierService;
	}
	

	public Retriever createRetriever(UIComponent parent, Service i, double ra, double dec, double raSize, double decSize) {
//		return new VizierRetriever(parent,i,getPrimaryNode(),getVizModel(),ra,dec,raSize,decSize);
		return new CatalogTerminalVizierRetriever(parent,i,getPrimaryNode(),getVizModel(),ra,dec,raSize,decSize);

	}	
	
	/** variant of vizier retriever that stops at the 'catalog' node - as for catalog terminal cone retrieever */
	public class CatalogTerminalVizierRetriever extends VizierRetriever {

		public CatalogTerminalVizierRetriever(UIComponent comp, Service information, TreeNode primaryNode, VizModel model, double ra, double dec, double raSize, double decSize) {
			super(comp, information, primaryNode, model, ra, dec, raSize, decSize);
		}
		protected SummarizingTableHandler createTableHandler() {
			return new CatalogTerminalVizierTableHandler();
		}
		public class CatalogTerminalVizierTableHandler extends VizierTableHandler {
			/** overridden - don't care about data - just want to count the rows */
			
			public void rowData(Object[] row) throws SAXException {
		        if (!isWorthProceeding()) { // no point, not enough metadata - sadly, get called for each row of the table - no way to bail out.
		            resultCount = QueryResultSummarizer.ERROR;
		            message = "Insufficient table metadata";
		            return;
		        }
		        resultCount++;
		        tableRowCount++;
			}
			
			public void endTable() throws SAXException {
				serviceNode.setAttribute(RESULT_COUNT,Integer.toString(tableRowCount));
				super.endTable();
			}
		}
	}

// full-data variant.	
	
	/** a different sort of retriever - as it fetches results for multiple 'tables'/ 'services' */
	public class VizierRetriever extends Retriever {

		/**
		 * @param comp
		 * @param information
		 * @param primaryNode
		 * @param model
		 * @param ra
		 * @param dec
		 */
		public VizierRetriever(UIComponent comp, Service information, TreeNode primaryNode, VizModel model, double ra, double dec,double raSize,double decSize) {
			super(comp, information, primaryNode, model, ra, dec);
			this.raSize = raSize;
		}
		//@todo work out how to use this too.
		private double raSize;
			
		public String getServiceType() {
			return VIZIER;
		}
		
		public static final String VIZIER = "vizier";
		
// override - don't want to return an augmented TreeNode from this one.		
protected TreeNode createServiceNode(URL serviceURL, String tooltip) {
	TreeNode augmented =  super.createServiceNode(serviceURL, tooltip);
	// lazy - just copy the bits we want inro an un-augmented treenode..
	TreeNode plain = new DefaultTreeNode();
	plain.setAttributes(augmented.getAttributes());
	return plain;
}
		protected Object construct() throws Exception {
	        reportProgress("Constructing query");		    
			URL q = cone.constructQuery(vizierEndpoint,ra,dec,raSize);
	        reportProgress("Querying service");			
			InputSource source = new InputSource(q.openStream());
			SummarizingTableHandler th = createTableHandler();
			parseTable(source,th);
			return th;
		}

		protected SummarizingTableHandler createTableHandler() {
			return new VizierTableHandler();
		}
		
		protected void doFinished(Object result) {
			// add summary of search results to table view.
	       SummarizingTableHandler th = (SummarizingTableHandler)result;
	        model.addQueryResult(service,getPrimaryNode(),th.getResultCount(),th.getMessage());                                   
	        parent.setStatusMessage(service.getTitle() + " - " + th.getResultCount() + " results");
	    
		}
		
		/** parser for the multi-table vizier response */
		public class VizierTableHandler extends BasicTableHandler {
			// count per table - different to per-service resultCount as vizier returns more than one table.
			protected int tableRowCount; 
			
			public VizierTableHandler() {
				super(null);// we don't pass in a service node. instead, we create one every time we find a new table.
			}		
			
			// create a new service node for each table encountered.
			protected void newTableExtensionPoint(StarTable st) {
				tableRowCount =0;
				// phew, lucky they're logical.
				URI serviceURI = vizierEndpoint.resolve("?-source=" + st.getName());
				String s = null;
				try {
					s = cone.constructQuery(serviceURI,ra,dec,raSize).toString();
				} catch (InvalidArgumentException x) { // unlikely.
					logger.error("InvalidArgumentException",x);
				} catch (NotFoundException x) {
					logger.error("NotFoundException",x);
				}
				// pesky finals.
				final String serviceURL = s;
		        serviceNode =  new FileProducingTreeNode() {
		        	// create a service node.
					protected FileObject createFileObject(ScopeTransferableFactory factory) throws FileSystemException {
						return factory.new AstroscopeFileObject(VoDataFlavour.MIME_VOTABLE
								,this,serviceURL);
					}
		        };
		        serviceNode.setAttribute(SERVICE_URL_ATTRIBUTE,serviceURL);
		        String title = st.getParameterByName("Description").getValue().toString();
		        String wrapped = WordUtils.wrap(title,AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false);
		        StringBuffer sb = new StringBuffer("<html>");
		        sb.append(wrapped);
		        sb.append("<br>ID: ").append(st.getName());
		        sb.append("</html>");
		        serviceNode.setAttribute(LABEL_ATTRIBUTE,title);
		        serviceNode.setAttribute(WEIGHT_ATTRIBUTE,"2");
		        serviceNode.setAttribute(SERVICE_ID_ATTRIBUTE,st.getName());
		        serviceNode.setAttribute(TOOLTIP_ATTRIBUTE,sb.toString());
		        
     
		    }
			
			public void rowData(Object[] row) throws SAXException {
				tableRowCount++;
				super.rowData(row);
			}
			
			// add new result into tree
			public void endTable() throws SAXException {
				super.endTable();
				if  (tableRowCount > 0) { //otherwise don't bother
					final TreeNode nodeToAdd = serviceNode; // take a copy of this, otherwise we get a race condition.
				SwingUtilities.invokeLater(new Runnable() {
					// splice new result in.
					public void run() {
						DefaultEdge edge = new DefaultEdge(primaryNode,nodeToAdd);
			            edge.setAttribute(WEIGHT_ATTRIBUTE,"2");              
			            model.getTree().addChild(edge); 
					}
				});
				}
			}
		}// end of table handler.
		
		
	} // end of retriever.
	
	/** rudimentarty service descirption for vizier - just enough to get by the 
	 * astroscope mechanisms. a mock really.
	 */
	private final static Service[] vizierService = new Service[] {
		new Service() {

			public Capability[] getCapabilities() {
				return new Capability[]{};
			}

			public String[] getRights() {
				return new String[]{};
			}

			public Content getContent() {
				return new Content();
			}

			public String getCreated() {
				return null;
			}

			public Curation getCuration() {
				return new Curation();
			}
			final URI id = URI.create("ivo://CDS/Vizier");
		
			public URI getId() {
				return id;
			}

			public String getShortName() {
				return "vizier";
			}

			public String getStatus() {
				return "active";
			}

			public String getTitle() {
				return "All-Vizier Search";
			}

			public String getType() {
				return "service";
			}

			public String getUpdated() {
				return null;
			}

			public Validation[] getValidationLevel() {
				return new Validation[]{};
			}
		}
	};




}
