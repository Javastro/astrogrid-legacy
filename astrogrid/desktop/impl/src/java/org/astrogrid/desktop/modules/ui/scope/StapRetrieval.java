package org.astrogrid.desktop.modules.ui.scope;

import java.net.URI;
import java.net.URL;
import java.util.Calendar;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;


/** 
 * task that retrives, parses and adds to the display results of one siap service 
 * */
public class StapRetrieval extends Retriever {
    /**
     * Logger for this class
     */
    static final Log logger = LogFactory.getLog(StapRetrieval.class);
    
    private final Calendar start;
    private final Calendar end;
    private final String format;

    public StapRetrieval(UIComponent comp,Service information,TreeNode primaryNode,VizModel model, 
                         Stap stap, Calendar start, Calendar end, double ra, double dec, double raSize,double decSize)  {
        super(comp,information,primaryNode,model,ra,dec);
        this.raSize = raSize;
        this.decSize = decSize;
        this.stap = stap;
        this.start = start;
        this.end = end;
        this.format = null;
    }
    
    public StapRetrieval(UIComponent comp,Service information,TreeNode primaryNode,VizModel model, 
            Stap stap, Calendar start, Calendar end, double ra, double dec, double raSize,double decSize, String format)  {
        super(comp,information,primaryNode,model,ra,dec);
        this.raSize = raSize;
        this.decSize = decSize;
        this.stap = stap;
        this.start = start;
        this.end = end;        
        this.format = format;
    }
    
    
    private final double raSize;
    private final double decSize;
    private final Stap stap;
    protected Object construct() throws Exception{
            URL stapURL = null;
            final URI endpoint = new URI(getFirstEndpoint(information).toString());
			//check if there is a ra,dec and construct a stap query accordingly.
            if(Double.isNaN(ra) || Double.isNaN(dec)) {
                if(format != null)
                    stapURL = stap.constructQueryF(endpoint,start, end, format);
                else
                    stapURL = stap.constructQuery(endpoint,start, end);
            }
            else {
                if(format != null)
                    stapURL = stap.constructQuerySF(endpoint,start, end, ra, dec, raSize, decSize, format);
                else
                    stapURL = stap.constructQueryS(endpoint,start, end, ra, dec, raSize, decSize);
            }
            
            StringBuffer sb = new StringBuffer();
            sb.append("<html>Title: ").append(information.getTitle())
                .append("<br>ID: ").append(information.getId());
            if (information.getContent() != null) {
               sb.append("<br>Description: <p>")
                .append(information.getContent().getDescription()!= null 
                			?   WordUtils.wrap(information.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
            }
                sb.append("</html>");
                //.append("</p><br>Service Type: ").append(((StapInformation)information).getImageServiceType())
                                        
            TreeNode serviceNode = createServiceNode(stapURL, sb.toString());
            // build subtree for this service

            InputSource source = new InputSource(stapURL.openStream());
            SummarizingTableHandler th = new StapTableHandler(serviceNode);
            parseTable(source, th);
            return th;           
    }
    /** attribute containing type extension of the image */
    public static final String IMAGE_TYPE_ATTRIBUTE = "type";
    /** attribute containing reference url for this image */
    public static final String IMAGE_URL_ATTRIBUTE = "imgURL";   
    public class StapTableHandler extends BasicTableHandler {


        public StapTableHandler(TreeNode serviceNode) {
            super(serviceNode);
        }
        int accessCol = -1;
        int formatCol = -1;
        int sizeCol = -1;
        int titleCol = -1;   
        int instCol = -1;
        int timeStart = -1;
        int timeEnd = -1;
        
        protected void startTableExtensionPoint(int col,ColumnInfo columnInfo) {
            String ucd = columnInfo.getUCD();
            if (ucd == null) {
                return;
            }
        if (ucd.equalsIgnoreCase("VOX:AccessReference")) {
            accessCol = col;
        } else if (ucd.equalsIgnoreCase("VOX:Format")) {
            formatCol = col;
        } else if (ucd.equalsIgnoreCase("VOX:Image_Title")) {
            titleCol = col;
        } else if (ucd.equalsIgnoreCase("INST_ID")) {
            instCol = col;
        } else if (ucd.equalsIgnoreCase("time.obs.start")) {
            timeStart = col;            
        } else if (ucd.equalsIgnoreCase("time.obs.end")) {
            timeEnd = col;
        }
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
            
            //String rowRa = row[raCol].toString();
            //String rowDec = row[decCol].toString();
            
            DefaultTreeNode valNode = new DefaultTreeNode();
            //String positionString = chopValue(String.valueOf(rowRa),2) + "," + chopValue(String.valueOf(rowDec),2) ;
            valNode.setAttribute(LABEL_ATTRIBUTE,"*");
            valNode.setAttribute(SERVICE_TYPE_ATTRIBUTE,getServiceType());
            // unused
            //valNode.setAttribute(RA_ATTRIBUTE,rowRa); // these might come in handy for searching later.
            //valNode.setAttribute(DEC_ATTRIBUTE,rowDec);

            // handle further parsing in subclasses.
            rowDataExtensionPoint(row,valNode);
            StringBuffer tooltip = new StringBuffer();
            tooltip.append("<html><p>");//.append(rowRa).append(", ").append(rowDec);
            for (int v = 0; v < row.length; v++) {
                Object o = row[v];
                if (o == null) {
                    continue;
                }
                tooltip.append("<br>")
                .append(titles[v])
                .append( ": ")
                .append(safeTrim(o));
            }
            tooltip.append("</p></html>");
            valNode.setAttribute(TOOLTIP_ATTRIBUTE,tooltip.toString());
            String instrumentID = safeTrim(row[instCol]);
            TreeNode instrNode = model.findNode(instrumentID, getServiceNode());
            if(instrNode == null) {
                instrNode = new DefaultTreeNode();
                instrNode.setAttribute(LABEL_ATTRIBUTE,instrumentID);
                instrNode.setAttribute(TOOLTIP_ATTRIBUTE,instrumentID);
                getServiceNode().addChild(new DefaultEdge(getServiceNode(),instrNode));
            }
            instrNode.addChild(new DefaultEdge(instrNode,valNode));
        }
        
        
        
   protected void rowDataExtensionPoint(Object[] row, TreeNode valNode) {
        String imgURL = "";
        if(row[accessCol] != null) {
            imgURL = safeTrim(row[accessCol]);
        }

        String details; 
        if (timeStart > -1 && row[timeStart] != null) {
            details = safeTrim(row[timeStart]);
        } else {
            details  = "No Start Time";
        }
        /*
        if (timeEnd > -1 && row[timeEnd] != null) {
            details += "-" + row[timeEnd].toString();
        } else {
            details  += " - No End Time";
        }
        */
        
        valNode.setAttribute(IMAGE_URL_ATTRIBUTE,imgURL);
        if(formatCol > -1 && row[formatCol] != null) {
            String type = safeTrim(row[formatCol]);
            //@todo Should fix this more correctly on the retriever or check about stap doing a real mime type on the return.
            if(type.indexOf('-') > -1) {
                valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE ,"/" + type.substring(type.indexOf('-')+1).toLowerCase());
            }else {
                valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE ,"/" + type.toLowerCase());
            }
            
        }
        valNode.setAttribute(LABEL_ATTRIBUTE,details);
    }
        
    protected boolean isWorthProceeding() {
        return accessCol >= 0;
    }  
    
    // extended - resets our new variables too.
    public void endTable() throws SAXException {
        super.endTable();
        accessCol = -1;
        formatCol = -1;
        sizeCol = -1;
        titleCol = -1;
        instCol = -1;
        timeStart = -1;
        timeEnd = -1;
    }
        
    } // end table handler class.

    public String getServiceType() {
        return STAP;
    }
    
    public static final String STAP = "STAP";
}