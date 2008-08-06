package org.astrogrid.desktop.modules.ui.scope;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl.ListServicesRegistryQuerier;
import org.astrogrid.desktop.modules.ui.comp.PositionUtils;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.VotableContentHandler.VotableHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.StarTable;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** base class for something that fetches a resource
 *   extensible for siap, cone, ssap, etc by implementing the abstract {@link #construct} method. This method should
 *   return an instance of {@link AstroscopeTableHandler} that containis the parsed results of 
 *   querying this service.
 *  @TEST
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 28-Oct-2005
 *
 */
public abstract class AbstractRetriever extends BackgroundWorker implements Retriever {
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory.getLog(AbstractRetriever.class);

    /** attribute pointing to the logo image for this service */
    public static final String SERVICE_LOGO_ATTRIBUTE = "img";
    /** attribute containing the url used to query the service */
    public static final String SERVICE_URL_ATTRIBUTE = "url";
    /** attribute containing the id (ivorn) of the service */
    public static final String SERVICE_ID_ATTRIBUTE = "id";
    /** attribute describing the weight / width of edge links */
    public static final String WEIGHT_ATTRIBUTE = "weight";    
    /** attribute describing the font of a node */
    public static final String FONT_ATTRIBUTE = "font";
    /** attribute describing what kind of service - provided by {@link #getServiceType()} in subclasses */
    public static final String SERVICE_TYPE_ATTRIBUTE = "service-type";
    /** attribute providing the text label to be displayed for this node */
    public static final String LABEL_ATTRIBUTE = "label";  
    /** attribute providing the number of results */
    public static final String RESULT_COUNT = "result-count";
    /** attribute giving the ra of the result */ 
    public static final String RA_ATTRIBUTE = "ra";
    /** attribute giving the dec of the result */
    public static final String DEC_ATTRIBUTE = "dec";
    /** attribute giving the offset value for the result */
    public static final String OFFSET_ATTRIBUTE = "offset";
    /** attribute giving the tooltip for this node */
    public static final String TOOLTIP_ATTRIBUTE = "tooltip";
    /** attribute giving a formatted ra,dec for this node - used in ResultsFileTable */
    public static final String POS_ATTRIBUTE = "position";
    /** attribute giving a formatted offset dfor this node - usd in ResultsFileTable */
    public static final String OFFSET_DISPLAY_ATTRIBUTE = "displayOffset";        
  //  private static final int MAX_INLINE_IMAGE_SIZE = 100000;
    protected final double ra;
    protected final double dec;
    protected final Service service;
    protected final Capability capability;
    protected final VizModel model;
    protected final NodeSocket nodeSocket;
    protected String subName = "";

    
    public AbstractRetriever(final Service information,final Capability cap,final NodeSocket socket,final VizModel model,final double ra, final double dec) {
        super(model.getParent(),information.getTitle(),SHORT_TIMEOUT,Thread.MIN_PRIORITY+3);
        this.ra = ra;
        this.dec = dec;
        this.service = information;
        this.capability = cap;
        this.model = model;
        this.nodeSocket = socket;
    }

    /** return a string describing what kind of service this is */
    public abstract String getServiceType();

    /** set a disambiguation string - distinguishes different retrievers 
     *  within the same service.  May be the empty string if only one retriever
     *  per parent service.
     */
    public void setSubName(final String subName) {
        this.subName = subName;
    }

    /** return a disambiguation string - distinguishes different retrievers 
     *  within the same service.  May be the empty string if only one retriever
     *  per parent service.
     */
    public String getSubName() {
        return this.subName;
    }

    public Service getService() {
        return this.service;
    }

    public Capability getCapability() {
        return this.capability;
    }

    /** returns terse string describing this retriever
     */
    public String getLabel() {
        final StringBuffer sbuf = new StringBuffer()
           .append(getService().getId())
           .append('_')
           .append(getServiceType());
        if (subName != null && subName.length()>0) {
            sbuf.append('_')
                .append(subName);
        }
        return sbuf.toString();
    }

    @Override
    public String toString() {
        return getLabel();
    }

    /** helper method - called by subclasses to SAX-parse a votable, attaching results as nodes to the service node 
     * @param tableHandler handler to use on the votable.
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException*/
    protected final void parseTable(final InputSource source, final VotableHandler tableHandler) throws ParserConfigurationException, FactoryConfigurationError, IOException, SAXException {
        final SAXParserFactory newInstance = SAXParserFactory.newInstance();
        newInstance.setValidating(false);
        final XMLReader parser = newInstance.newSAXParser().getXMLReader();
        final VotableContentHandler votHandler = new VotableContentHandler(false);
        votHandler.setReadHrefTables(true);
        votHandler.setVotableHandler(tableHandler);
        parser.setContentHandler(votHandler);
        parser.parse(source);        
    }
    
    /** sax-style parser for votables - these methods get called in callbacks, sax-style, as the document whizzes by, instead of having an in-memory model
    * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Dec-2005
     */
   public class BasicTableHandler implements AstroscopeTableHandler {
		/**
		 * Logger for this class
		 */
		private final Log logger = LogFactory.getLog(BasicTableHandler.class);


	    /** utility method - converts an object using toString(), and then trims it if not null */
	    protected final String safeTrim(final Object o) {
	    	if (o == null) {
	    		return "";
	    	} 
	    	if (o.getClass().isArray()) {
	    		return ArrayUtils.toString(o) ;
	    	}
	    	final String s = o.toString();
	    	if (s == null) {
	    		return "";
	    	}
	    	return s.trim();
	    }

	    /** helper method - find node by label
	     * 
	     * @param label label to search for
	     * @param startNode starting point to search downwards from. if null, use {@link #getRootNode()}
	     * @return treenode with matching label, or null;
	     */
	    protected  TreeNode findNode(final String label, TreeNode startNode) {
	        if(startNode == null)  {
	            startNode = model.getTree().getRoot();
	        }
	        final Iterator iter = startNode.getChildren();
	        while(iter.hasNext()) {
	            final TreeNode n = (TreeNode)iter.next();
	            if(n.getAttribute(AbstractRetriever.LABEL_ATTRIBUTE).equals(label)) {
	                return n;
	            }
	        }
	        return null;
	    }

    public BasicTableHandler(final TreeNode serviceNode) {
           this.serviceNode = serviceNode;
       }
       protected TreeNode serviceNode;
       protected int resultCount = 0;
       protected String message = null;
       protected int raCol = -1;
       protected int decCol = -1;
       protected String[] titles;
       
       public final int getResultCount() {
           return resultCount;
       }
       public final String getMessage() {
           return message;
       }
       public final TreeNode getServiceNode() {
           return serviceNode;
       } 
      
       
    /** here we get passed in a start table that is meta-data only
     * @see uk.ac.starlink.votable.TableHandler#startTable(uk.ac.starlink.table.StarTable)
     */

    public void startTable(final StarTable starTable) throws SAXException {
            reportProgress("Parsing response");
    	newTableExtensionPoint(starTable);
    	// get the info.
    	DescribedValue qStatus = starTable.getParameterByName("Error");
    	if (qStatus != null) {
    	    
    	    message = qStatus.getInfo().getDescription();
    	    if (message == null) {
    	        message = qStatus.getValueAsString(1000);
    	    }
    	    throw new DalProtocolException(message);
    	}
    	qStatus = starTable.getParameterByName("QUERY_STATUS");
    	if (qStatus != null && qStatus.getValue() != null &&  ! "OK".equalsIgnoreCase(qStatus.getValueAsString(1000))) {
            message = qStatus.getInfo().getDescription();
            if (message == null) {
                message = qStatus.getValueAsString(1000);
            }
    	    throw new DalProtocolException(message);
    	}
        titles = new String[starTable.getColumnCount()];
        for (int col = 0; col < starTable.getColumnCount(); col++) {
            final ColumnInfo columnInfo = starTable.getColumnInfo(col);
            final String ucd = columnInfo.getUCD();
            if (ucd != null) {
                if (ucd.equalsIgnoreCase("POS_EQ_RA_MAIN")) {
                    raCol = col;
                } else if (ucd.equalsIgnoreCase("POS_EQ_DEC_MAIN")) {
                    decCol = col;
                }
            }
            startTableExtensionPoint(col,columnInfo);            
            titles[col] = columnInfo.getName() + " (" + columnInfo.getUCD() + ")";
        }            
    }
   // extension point for subclasses - override thie to be passed each 
    // column info object - use for extracting other positions.
   protected void startTableExtensionPoint(final int col,final ColumnInfo columnInfo) {
   }

   // exention point for suibclasses. override this to be passed in each new table object
   protected void newTableExtensionPoint(final StarTable st) {
   }

       /**
        * Method getOffset
        * Description: method to calculate a distance offset between two points 
        * @param queryra known query ra.
        * @param querydec known query dec
        * @param objectra objects ra from results of a service/votable
        * @param objectdec objects dec from results of a service/votable
        * @return distance between two points.
        */
       protected final double getOffset(final double queryra, final double querydec, final double objectra, final double objectdec) {           
           return uk.ac.starlink.ttools.func.Coords.skyDistanceDegrees(queryra,querydec,objectra,objectdec);
       }  
       
       protected final String chopValue(final String doubleValue, final int scale) {
     	   // @todo would it be more efficient to use a NumberFormatter here? - this has Round_half_up behaviour too.
    	   try {
    	   return new BigDecimal(doubleValue).setScale(scale,BigDecimal.ROUND_HALF_UP).toString();
    	   } catch (final NumberFormatException e) {
    		   return "unknown";
    	   }
       }
       
    /** called once for each row in the table
     * @see uk.ac.starlink.votable.TableHandler#rowData(java.lang.Object[])
     */
    public void rowData(final Object[] row) throws SAXException {
        if (!isWorthProceeding()) { // no point, not enough metadata 
            message = "Insufficient table metadata";
            throw new DalProtocolException(message);
        }
        resultCount++; 
        final String rowRa = safeTrim(row[raCol]);
        final String rowDec = safeTrim(row[decCol]);                                 
        final DefaultTreeNode valNode = createValueNode();
        final String positionString = chopValue(String.valueOf(rowRa),6) + "," + chopValue(String.valueOf(rowDec),6);
        valNode.setAttribute(LABEL_ATTRIBUTE,"*");
        valNode.setAttribute(SERVICE_TYPE_ATTRIBUTE,getServiceType());
        
        valNode.setAttribute(RA_ATTRIBUTE,rowRa); // these might come in handy for searching later.
        valNode.setAttribute(DEC_ATTRIBUTE,rowDec);
        valNode.setAttribute(POS_ATTRIBUTE,positionString);

        // handle further parsing in subclasses.
        rowDataExtensionPoint(row,valNode);
        
        final StringBuffer tooltip = new StringBuffer();
        tooltip.append("<html><p>Position (decimal degrees): ").append(rowRa).append(", ").append(rowDec);
        try {
        tooltip.append("<br>Position (sexagesimal): ")
        .append(PositionUtils.getRASexagesimal((String.valueOf(rowRa) + "," + String.valueOf(rowDec))))
        .append(",").append(PositionUtils.getDECSexagesimal((String.valueOf(rowRa) + "," + String.valueOf(rowDec))));

        for (int v = 0; v < row.length; v++) {
            final Object o = row[v];
            if (o == null || omitRowFromTooltip(v)) {
                continue;
            }
            tooltip.append("<br>")
            .append(titles[v])
            .append( ": ")
            .append(safeTrim(o));
        }        
        tooltip.append("</p>");
        valNode.setAttribute(TOOLTIP_ATTRIBUTE,tooltip.toString());  
        final double offset = getOffset(ra, dec, Double.valueOf(rowRa).doubleValue(), Double.valueOf(rowDec).doubleValue());
        final String offsetVal = chopValue(String.valueOf(offset),6);
        valNode.setAttribute(OFFSET_DISPLAY_ATTRIBUTE,offsetVal); // formatted value, just for display.
        // now find correct offset node to add to.
        TreeNode offsetNode = findNode(offsetVal, serviceNode);
        final String tempAttr;
        if(offsetNode == null) { // not found offset node.
            offsetNode = new DefaultTreeNode();
            offsetNode.setAttribute(LABEL_ATTRIBUTE,offsetVal);
            offsetNode.setAttribute(OFFSET_ATTRIBUTE,String.valueOf(offset));
            offsetNode.setAttribute(TOOLTIP_ATTRIBUTE,"Offset from search position: " + String.valueOf(offset));
            serviceNode.addChild(new DefaultEdge(serviceNode,offsetNode));
            model.getNodeSizingMap().addOffset(offsetVal);
        }

        // now have found or created the offsetNode, find the pointNode within it.
        TreeNode pointNode = findNode(positionString,offsetNode);
        if (pointNode == null) {
            pointNode = new DefaultTreeNode();
            offsetNode.addChild(new DefaultEdge(offsetNode,pointNode));
            pointNode.setAttribute(LABEL_ATTRIBUTE,positionString);
            pointNode.setAttribute(TOOLTIP_ATTRIBUTE,"Actual position of result: " + positionString);
        }
        // now have found or created point node. add new result to this.
        
       pointNode.addChild(new DefaultEdge(pointNode,valNode));
        } catch (final NumberFormatException e) {
       	 logger.warn("Failed to parse",e);
        } catch (final ArrayIndexOutOfBoundsException e) {
          	 logger.warn("Failed to parse",e);
          }          
    }

    /** maybe overridden by subclasses to skip row data from tooltip */
    protected boolean omitRowFromTooltip(final int rowIndex) {
        return false;
    }
    
    /** can be extended by subclasses to provide extra functionalitiy */
	public DefaultTreeNode createValueNode() {
		return new DefaultTreeNode();
	}
    /** extension point for subclasses to add more row parsing here. */
    protected void rowDataExtensionPoint(final Object[] row, final TreeNode valNode) {
    }

    /** called at the end - tots up node sizing, an resets variables.
     * @see uk.ac.starlink.votable.TableHandler#endTable()
     */
    public void endTable() throws SAXException {
        model.getNodeSizingMap().setNodeSizing();
        raCol = -1;
        decCol = -1; 
        titles = null;
    }
 
    //test whether this table has sufficient metadata to make it worth parsing.
    // otherwise, we just skip it.
    protected boolean isWorthProceeding() {
        return raCol >= 0 && decCol >= 0; 
    }

// methods for inspecting votable content outside tables.
    public void info(final String name, final String value, final String content)
            throws SAXException {
        // unused in this impl
    }


    public void param(final String name, final String value, final String description)
            throws SAXException {
        // unused in this impl        
    }


    public void resource(final String name, final String id, final String type)
            throws SAXException {
        // unused in this impl        
    }

     
    
    } //end of table parser.


    @Override
    protected final void doError(final Throwable ex) {
           model.getSummarizer().addQueryFailure(this,ex);
    }
    

    //bz 2724 - not always true. In all-voscope, if the queries for all siaps complete before reg query to list all cones completes, this gets triggered.
    // can't determine that we're the last ever retriever by checking progressvalue alone.
    // check the tasklist too, and verify that we're the last task - there's no other retrievers or queriers still in the queue.
    @Override
    protected final void doAlways() {
       parent.setProgressValue(parent.getProgressValue() + 1); 
       if (parent.getProgressMax() <= parent.getProgressValue() && isLastQueryWorker()) {
           parent.setProgressMax(0);
           model.getParent()// same as this.parent, but saves casting..
               .getSubmitButton().enableA(); // flip the button back again,
       }
    }
    
    /** returns true if there's no other query tasks but us for this window on the task queue */
    private final boolean isLastQueryWorker() {
        for (final Iterator i =parent.getContext().getTasksList().iterator(); i.hasNext(); ) {
            final BackgroundWorker w = (BackgroundWorker)i.next();
            if (w.getParent() == parent // belongs to this scope 
                    && w != this // not this retriever
                    && 
                    (w instanceof AbstractRetriever  || w instanceof ListServicesRegistryQuerier) ) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void doFinished(final Object result) {        
        // splice our subtree into the main tree.. do on the event dispatch thread, as this will otherwise cause 
        // concurrent modification exceptions
        final AstroscopeTableHandler th = (AstroscopeTableHandler)result;
        model.getSummarizer().addQueryResult(this,th);
        if (th.getResultCount() > 0) {
            nodeSocket.addNode(th.getServiceNode());
        }                                       
        parent.setStatusMessage(service.getTitle() + " - " + th.getResultCount() + " results");
    }



    
    /** create a node to represent the service about to be called.
     * @param serviceURL
     * @return a new tree node.
     */
    protected TreeNode createServiceNode(final URL serviceURL, final long sz,final String tooltip) {
        TreeNode serviceNode;
        try {
            final AstroscopeFileObject afo = model.createFileObject(
                    serviceURL
                    ,sz == -1 ? AstroscopeFileObject.UNKNOWN_SIZE : sz // translate from one representation of unknown size to the other.
                    ,new Date().getTime()
                    ,VoDataFlavour.MIME_VOTABLE);
            String filename;
            if (service.getShortName() != null) {
                filename = StringUtils.replace(service.getShortName(),"/","-") + " Search Results.vot";
            } else {
                filename = StringUtils.replace(service.getTitle(),"/","-") + " Search Results.vot";
            }
            serviceNode = new FileProducingTreeNode();
            model.addResultFor(this,filename,afo,(FileProducingTreeNode)serviceNode);
        } catch (final Exception e) {
            logger.warn(service.getId() + " : Unable to create file object for serviceNode - falling back",e);
            serviceNode = new DefaultTreeNode(); // fall back to a default tree node.
        }

        final String label;
        if (nodeSocket.isService() && getSubName() != null && getSubName().trim().length() > 0) {
            label = getSubName();
        } else {
            label = service.getTitle();
        }
        serviceNode.setAttribute(LABEL_ATTRIBUTE,label);
       
        serviceNode.setAttribute(WEIGHT_ATTRIBUTE,"2");
        serviceNode.setAttribute(SERVICE_ID_ATTRIBUTE, service.getId().toString());
        serviceNode.setAttribute(SERVICE_URL_ATTRIBUTE,serviceURL.toString());
        serviceNode.setAttribute(TOOLTIP_ATTRIBUTE,tooltip);        
        if (service.getCuration() != null && service.getCuration().getCreators().length != 0 && service.getCuration().getCreators()[0].getLogoURI() != null) {
           // @todo getLogo liable to throw a wobbly - need to trap exceptions / malformatted data
        	serviceNode.setAttribute(SERVICE_LOGO_ATTRIBUTE,service.getCuration().getCreators()[0].getLogo().toString());                    
        }
        return serviceNode;
    }
    
}
