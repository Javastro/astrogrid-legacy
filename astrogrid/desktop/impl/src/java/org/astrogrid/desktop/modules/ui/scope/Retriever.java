package org.astrogrid.desktop.modules.ui.scope;

import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.votable.TableContentHandler;
import uk.ac.starlink.votable.TableHandler;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.math.BigDecimal;

import org.astrogrid.desktop.modules.ui.comp.PositionUtils;

/** base class for something that fetches a resource
 *   extensible for siap, cone, ssap, etc by implementing the abstract {@link #construct} method. This method should
 *   return an instance of {@link Retriever.SummarizingTableHandler} that containis the parsed results of 
 *   querying this service.
 *  
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Oct-2005
 *
 */
public abstract class Retriever extends BackgroundWorker {
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
    /** attribute giving the ra of the result */ 
    public static final String RA_ATTRIBUTE = "ra";
    /** attribute giving the dec of the result */
    public static final String DEC_ATTRIBUTE = "dec";
    /** attribute giving the offset value for the result */
    public static final String OFFSET_ATTRIBUTE = "offset";
    /** attribute giving the tooltip for this node */
    public static final String TOOLTIP_ATTRIBUTE = "tooltip";        
  //  private static final int MAX_INLINE_IMAGE_SIZE = 100000;
    protected final double ra;
    protected final double dec;
    protected final ResourceInformation information;
    protected final VizModel model;
    protected final TreeNode primaryNode;
    
    public Retriever(UIComponent comp,ResourceInformation information,TreeNode primaryNode,VizModel model,double ra, double dec) {
        super(comp,information.getTitle(),1000*60L,Thread.MIN_PRIORITY+3); // make low priority, timeout after 1 min.
        this.ra = ra;
        this.dec = dec;
        this.information = information;
        this.model = model;
        this.primaryNode = primaryNode;
    }


    
    /** return a string describing what kind of service this is */
    public abstract String getServiceType();
        
    
    /** helper method - called by subclasses to SAX-parse a votable, attaching results as nodes to the service node 
     * @param tableHandler handler to use on the votable.
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException*/
    protected void parseTable(InputSource source, TableHandler tableHandler) throws ParserConfigurationException, FactoryConfigurationError, IOException, SAXException {
        XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        TableContentHandler votHandler = new TableContentHandler(false);
        votHandler.setReadHrefTables(true);
        votHandler.setTableHandler(tableHandler);
        parser.setContentHandler(votHandler);
        parser.parse(source);        
    }
    
    /** extension to the starlink tablehandler that produces a summary of what it's parsed too */
    public interface SummarizingTableHandler extends TableHandler {
        /** return a count of the number of rows parsed - or {@link QueryResultSummarizer#ERROR} if failed to parse */
        public int getResultCount();
        /** return an optional message about the results of the parse */
        public String getMessage();
        /** return the service tree node with all the results attached to it */
        public TreeNode getServiceNode();
    }

    /** sax-style parser for votables - these methods get called in callbacks, sax-style, as the document whizzes by, instead of having an in-memory model
     * maybe this will stop my laptop overheating whenever I do M54,1.0 - at the moment the overheating trip cuts in and shus the machine down. :)
     * @author Noel Winstanley nw@jb.man.ac.uk 02-Dec-2005
     */
   public class BasicTableHandler implements SummarizingTableHandler {





    public BasicTableHandler(TreeNode serviceNode) {
           this.serviceNode = serviceNode;
       }
       protected final TreeNode serviceNode;
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
    public void startTable(StarTable starTable) throws SAXException {

        titles = new String[starTable.getColumnCount()];
        for (int col = 0; col < starTable.getColumnCount(); col++) {
            ColumnInfo columnInfo = starTable.getColumnInfo(col);
            String ucd = columnInfo.getUCD();
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
   protected void startTableExtensionPoint(int col,ColumnInfo columnInfo) {
   }
   
   
   /*
   protected double hav(double val) {
        return Math.pow((Math.sin(0.5D * val)),2);    
       }
       
    protected double ahav(double val) {
           return 2.0D * Math.asin(Math.sqrt(val));
       }
       */
       

       /**
        * Method getOffset
        * Description: method to calculate a distance offset between two points in the sky using the 
        * haversine formula.  Uses the library from Dr Michael Thomas Flanagan at www.ee.ucl.ac.uk/~mflanaga
        * @param queryra known query ra.
        * @param querydec known query dec
        * @param objectra objects ra from results of a service/votable
        * @param objectdec objects dec from results of a service/votable
        * @return distance between two points.
        */
       protected double getOffset(double queryra, double querydec, double objectra, double objectdec) {
           
           return uk.ac.starlink.ttools.func.Coords.skyDistanceDegrees(queryra,querydec,objectra,objectdec);
           /*
           // gcdist = ahav( hav(dec1-dec2) + cos(dec1)*cos(dec2)*hav(ra1-ra2) )
           queryra = Math.toRadians(queryra);  
           querydec = Math.toRadians(querydec);
           //from the look of the formula I suspect this to be ra1 and dec1 since it should be the greater distance
           objectra = Math.toRadians(objectra); 
           objectdec = Math.toRadians(objectdec);
           //System.out.println("about to run haversine formula with " + queryra + ", " + querydec + ", " + objectra + ", " + objectdec);
           double result = ahav( hav(objectdec-querydec) + Math.cos(objectdec)*Math.cos(querydec)*hav(objectra-queryra) );
           //System.out.println("the haversine result = " + result + " throwing it toDegrees = " + Math.toDegrees(result));
           //return Math.toDegrees(result); NWW: fix accordng to kev.
           return result;
           */
       }  
       
       protected String chopValue(String doubleValue, int scale) {
           //int decIndex = doubleValue.indexOf('.');
           //int expIndex = doubleValue.indexOf('E');
           //System.out.println("the doublevalue33 = " + doubleValue + " with bigdeciaml = " + new BigDecimal(doubleValue).setScale(scale,BigDecimal.ROUND_HALF_UP).toString());
           return new BigDecimal(doubleValue).setScale(scale,BigDecimal.ROUND_HALF_UP).toString();
           //we use the scale during the substring process
           //and to go to scale we need to increment by one character
           //to include the "." decimal point.
           /*
           scale++;           
           if(decIndex != -1 && doubleValue.length() > (decIndex + scale)) {
               if((decIndex + scale) <= (expIndex+1))
                   return doubleValue;
               
               String temp = doubleValue.substring(0,(decIndex + scale));
               if((decIndex = doubleValue.indexOf('E')) != -1) {
                   temp += doubleValue.substring(decIndex);
               }
               return temp;
           }
           return doubleValue;
           */
       }
       
    /** called once for each row in the table
     * @see uk.ac.starlink.votable.TableHandler#rowData(java.lang.Object[])
     */
    public void rowData(Object[] row) throws SAXException {
        if (!isWorthProceeding()) { // no point, not enough metadata - sadly, get called for each row of the table - no way to bail out.
            resultCount = QueryResultSummarizer.ERROR;
            message = "Insufficient table metadata";
            return;
        }
        resultCount++;
        String rowRa = row[raCol].toString();
        String rowDec = row[decCol].toString();                                 
        DefaultTreeNode valNode = new DefaultTreeNode();
        String positionString = chopValue(String.valueOf(rowRa),6) + "," + chopValue(String.valueOf(rowDec),6);
        valNode.setAttribute(LABEL_ATTRIBUTE,"*");
        valNode.setAttribute(SERVICE_TYPE_ATTRIBUTE,getServiceType());
        // unused
        valNode.setAttribute(RA_ATTRIBUTE,rowRa); // these might come in handy for searching later.
        valNode.setAttribute(DEC_ATTRIBUTE,rowDec);

        // handle further parsing in subclasses.
        rowDataExtensionPoint(row,valNode);
        
        StringBuffer tooltip = new StringBuffer();
        tooltip.append("<html><p>").append(rowRa).append(", ").append(rowDec);
        tooltip.append("<br>")
        .append(PositionUtils.getRASexagesimal((String.valueOf(rowRa) + "," + String.valueOf(rowDec))))
        .append(",").append(PositionUtils.getDECSexagesimal((String.valueOf(rowRa) + "," + String.valueOf(rowDec))));
        for (int v = 0; v < row.length; v++) {
            Object o = row[v];
            if (o == null) {
                continue;
            }
            tooltip.append("<br>")
            .append(titles[v])
            .append( ": ")
            .append(o.getClass().isArray() ? ArrayUtils.toString(o) : o.toString());
        }        
        tooltip.append("</p></html>");
        valNode.setAttribute(TOOLTIP_ATTRIBUTE,tooltip.toString());  
        double offset = getOffset(ra, dec, Double.valueOf(rowRa).doubleValue(), Double.valueOf(rowDec).doubleValue());
        String offsetVal = chopValue(String.valueOf(offset),6);
        TreeNode offsetNode = model.findNode(offsetVal, serviceNode);
        String tempAttr;
        if(offsetNode == null) { // not found offset node.
            offsetNode = new DefaultTreeNode();
            offsetNode.setAttribute(LABEL_ATTRIBUTE,offsetVal);
            offsetNode.setAttribute(OFFSET_ATTRIBUTE,String.valueOf(offset));
            offsetNode.setAttribute(TOOLTIP_ATTRIBUTE,String.valueOf(offset));
            serviceNode.addChild(new DefaultEdge(serviceNode,offsetNode));
            model.getNodeSizingMap().addOffset(offsetVal);
        }
        // now have found or created the offsetNode, find the pointNode within it.
        TreeNode pointNode = model.findNode(positionString,offsetNode);
        if (pointNode == null) {
            pointNode = new DefaultTreeNode();
            offsetNode.addChild(new DefaultEdge(offsetNode,pointNode));
            pointNode.setAttribute(LABEL_ATTRIBUTE,positionString);
            pointNode.setAttribute(TOOLTIP_ATTRIBUTE,positionString);
        }
        // now have found or created point node. add new result to this.
        
       pointNode.addChild(new DefaultEdge(pointNode,valNode));
                   
    }
    /** extension point for subclasses to add more row parsing here. */
    protected void rowDataExtensionPoint(Object[] row, TreeNode valNode) {
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
    } //end of table parser.




    protected void doError(Throwable ex) {
        parent.setStatusMessage(information.getName() + " : Service Failed  : " + ex.getMessage());
        model.getProtocols().addQueryResult(information,QueryResultSummarizer.ERROR,fmt(ex));
    }
    
    private String fmt(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.substringAfterLast(ex.getClass().getName(),"."));
        String msg = ex.getMessage();
        if (msg != null) {
            sb.append(' ');
            sb.append(msg);
        }
        return sb.toString();
    }

    protected void doAlways() {
       parent.setProgressValue(parent.getProgressValue() + 1); // @minor sometimes we get a race here, leading to the display being off-by-one.
    }

    protected void doFinished(Object result) {        
        // splice our subtree into the main tree.. do on the event dispatch thread, as this will otherwise cause 
        // concurrent modification exceptions
        SummarizingTableHandler th = (SummarizingTableHandler)result;
        TreeNode serviceNode = th.getServiceNode();
        model.getProtocols().addQueryResult(information,th.getResultCount(),th.getMessage());
        if (th.getResultCount() > 0) {
            DefaultEdge edge = new DefaultEdge(primaryNode,serviceNode);
            edge.setAttribute(WEIGHT_ATTRIBUTE,"2");              
            model.getTree().addChild(edge);   
        }                                       
    }



    /** create a node to represent the service about to be called.
     * @param serviceURL
     * @return a new tree node.
     */
    protected TreeNode createServiceNode(URL serviceURL, String tooltip) {
        TreeNode serviceNode = new DefaultTreeNode();
        serviceNode.setAttribute(LABEL_ATTRIBUTE,information.getTitle());
        serviceNode.setAttribute(WEIGHT_ATTRIBUTE,"2");
        serviceNode.setAttribute(SERVICE_ID_ATTRIBUTE, information.getId().toString());
        serviceNode.setAttribute(SERVICE_URL_ATTRIBUTE,serviceURL.toString());
        serviceNode.setAttribute(TOOLTIP_ATTRIBUTE,tooltip);        
        if (information.getLogoURL() != null) {
            serviceNode.setAttribute(SERVICE_LOGO_ATTRIBUTE,information.getLogoURL().toString());                    
        }
        return serviceNode;
    }
}