/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.swing.SwingUtilities;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.Validation;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.Retriever;
import org.astrogrid.desktop.modules.ui.scope.SpatialDalProtocol;
import org.astrogrid.desktop.modules.ui.scope.VizModel;
import org.astrogrid.desktop.modules.ui.scope.Retriever.SummarizingTableHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
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

	/**
	 * @param name
	 */
	public AllVizierProtocol(Cone cone) {
		super("VizieR");
		this.cone = cone;
		getPrimaryNode().setAttribute(Retriever.SERVICE_LOGO_ATTRIBUTE,"http://vizier.u-strasbg.fr/vizier_tiny.gif");
		//@todo factor this out into somewhere more logical - like config, or the vizier ar component.
		// maybe that should be http-get based too? yep. most probably.
		URI u = null;
		try {
			u = new URI("http://vizier.u-strasbg.fr/viz-bin/votable/-dtd/-A");
		} catch (URISyntaxException x) {
			logger.error("URISyntaxException",x);
		}
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
		return new VizierRetriever(parent,i,getPrimaryNode(),getVizModel(),ra,dec,raSize,decSize);
	}	
	
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

		protected Object construct() throws Exception {
			URL q = cone.constructQuery(vizierEndpoint,ra,dec,raSize);
			InputSource source = new InputSource(q.openStream());
			SummarizingTableHandler th = new VizierTableHandler();
			parseTable(source,th);
			return th;
		}
		
		protected void doFinished(Object result) {
			// add summary of search results to table view.
	       SummarizingTableHandler th = (SummarizingTableHandler)result;
	        model.getProtocols().addQueryResult(service,th.getResultCount(),th.getMessage());                                   
	        parent.setStatusMessage(service.getTitle() + " - " + th.getResultCount() + " results");
	    
		}
		
		/** parser for the multi-table vizier response */
		public class VizierTableHandler extends BasicTableHandler {

			public VizierTableHandler() {
				super(null);// we don't pass in a service node. instead, we create one every time we find a new table.
			}		
			
			// create a new service node for each table encountered.
			protected void newTableExtensionPoint(StarTable st) {
		        serviceNode = new DefaultTreeNode();
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
		        
		        // phew, lucky they're logical.
		        URI serviceURI = vizierEndpoint.resolve("?-source=" + st.getName());
				try {
					URL serviceURL = cone.constructQuery(serviceURI,ra,dec,raSize);
					serviceNode.setAttribute(SERVICE_URL_ATTRIBUTE,serviceURL.toString());
				} catch (InvalidArgumentException x) { // unlikely.
					logger.error("InvalidArgumentException",x);
				} catch (NotFoundException x) {
					logger.error("NotFoundException",x);
				}
     
		    }
			
			// add new result into tree
			public void endTable() throws SAXException {
				super.endTable();
				if  (serviceNode.getChildCount() > 0) { //otherwise don't bother
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
				return null;
			}

			public String[] getRights() {
				return null;
			}

			public Content getContent() {
				return null;
			}

			public String getCreated() {
				return null;
			}

			public Curation getCuration() {
				return null;
			}
			final URI id;
			{ 
				URI i = null;
				try {
					i = new URI("ivo://CDS/Vizier");
				} catch (Exception e) {
				}
				id = i; // sheesh. wot a palava.
			}
			
			public URI getId() {
				return id;
			}

			public String getShortName() {
				return "vizier";
			}

			public String getStatus() {
				return null;
			}

			public String getTitle() {
				return "All-Vizier Search";
			}

			public String getType() {
				return null;
			}

			public String getUpdated() {
				return null;
			}

			public Validation[] getValidationLevel() {
				return null;
			}
		}
	};




}
