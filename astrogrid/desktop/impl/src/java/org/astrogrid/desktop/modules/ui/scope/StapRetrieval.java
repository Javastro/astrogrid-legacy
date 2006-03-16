package org.astrogrid.desktop.modules.ui.scope;

import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.astrogrid.StapInformation;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;

import edu.berkeley.guir.prefuse.graph.TreeNode;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;

import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Calendar;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;


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

    public StapRetrieval(UIComponent comp,ResourceInformation information,TreeNode primaryNode,VizModel model, 
                         Stap stap, Calendar start, Calendar end, double ra, double dec, double raSize,double decSize)  {
        super(comp,information,primaryNode,model,ra,dec);
        this.raSize = raSize;
        this.decSize = decSize;
        this.stap = stap;
        this.start = start;
        this.end = end;
    }
    
    private final double raSize;
    private final double decSize;
    private final Stap stap;
    protected Object construct() throws Exception{
            URL stapURL = null;
            //check if there is a ra,dec and construct a stap query accordingly.
            if(Double.isNaN(ra) || Double.isNaN(dec)) {
                stapURL = stap.constructQuery(new URI(information.getAccessURL().toString()),start, end);
            }
            else {
                stapURL = stap.constructQueryS(new URI(information.getAccessURL().toString()),start, end, ra, dec, raSize, decSize);
            }
            
            StringBuffer sb = new StringBuffer();
            sb.append("<html>Title: ").append(information.getTitle())
                .append("<br>ID: ").append(information.getId())
                .append("<br>Description: <p>")
                .append(information.getDescription()!= null ?   WordUtils.wrap(information.getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "")
                .append("</html>");
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
                .append(o.getClass().isArray() ? ArrayUtils.toString(o) : o.toString());
            }
            tooltip.append("</p></html>");
            valNode.setAttribute(TOOLTIP_ATTRIBUTE,tooltip.toString());
            String instrumentID = row[instCol].toString();
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
            imgURL = row[accessCol].toString();
        }

        String details; 
        if (timeStart > -1 && row[timeStart] != null) {
            details = row[timeStart].toString();
        } else {
            details  = "No Start Time";
        }

        if (timeEnd > -1 && row[timeEnd] != null) {
            details += "-" + row[timeEnd].toString();
        } else {
            details  += " - No End Time";
        }
        valNode.setAttribute(IMAGE_URL_ATTRIBUTE,imgURL);
        if(formatCol > -1 && row[formatCol] != null) {
            String type = row[formatCol].toString();
            if(type.indexOf('-') > -1) {
                valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE ,type.substring(type.indexOf('-')+1));
            }else {
                valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE ,type);
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