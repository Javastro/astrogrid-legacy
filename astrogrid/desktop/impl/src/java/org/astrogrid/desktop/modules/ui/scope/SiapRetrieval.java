package org.astrogrid.desktop.modules.ui.scope;

import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.SiapInformation;
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

import java.net.URI;
import java.net.URL;
import java.util.Map;

/** task that retrives, parses and adds to the display results of one siap service 
 * */
public class SiapRetrieval extends Retriever {
    /**
     * Logger for this class
     */
    static final Log logger = LogFactory.getLog(SiapRetrieval.class);

    public SiapRetrieval(UIComponent comp,ResourceInformation information,TreeNode primaryNode,VizModel model, Siap siap,double ra, double dec, double raSize,double decSize)  {
        super(comp,information,primaryNode,model,ra,dec);
        this.raSize = raSize;
        this.decSize = decSize;
        this.siap = siap;
    }
    private final double raSize;
    private final double decSize;
    private final Siap siap;
    protected Object construct() throws Exception{
            URL siapURL = siap.constructQueryS(new URI(information.getAccessURL().toString()),ra, dec,raSize,decSize);
            StringBuffer sb = new StringBuffer();
            sb.append("<html>Title: ").append(information.getTitle())
                .append("<br>ID: ").append(information.getId())
                .append("<br>Description: <p>")
                .append(information.getDescription()!= null ?   WordUtils.wrap(information.getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "")                
                .append("</p><br>Service Type: ").append(((SiapInformation)information).getImageServiceType())
                .append("</html>");                        
            TreeNode serviceNode = createServiceNode(siapURL,sb.toString());
            // build subtree for this service

            InputSource source = new InputSource(siapURL.openStream());
            SummarizingTableHandler th = new SiapTableHandler(serviceNode);
            parseTable(source, th);
            return th;           
    }
    /** attribute containing type extension of the image */
    public static final String IMAGE_TYPE_ATTRIBUTE = "type";
    /** attribute containing reference url for this image */
    public static final String IMAGE_URL_ATTRIBUTE = "imgURL";   
    public class SiapTableHandler extends BasicTableHandler {


        public SiapTableHandler(TreeNode serviceNode) {
            super(serviceNode);
        }
        int imgCol = -1;
        int formatCol = -1;
        int sizeCol = -1;
        int titleCol = -1;   
        
        protected void startTableExtensionPoint(int col,ColumnInfo columnInfo) {
            String ucd = columnInfo.getUCD();
            if (ucd == null) {
                return;
            }
        if (ucd.equalsIgnoreCase("VOX:Image_AccessReference")) {
            imgCol = col;
        } else if (ucd.equalsIgnoreCase("VOX:Image_Format")) {
            formatCol = col;
        } else if (ucd.equalsIgnoreCase("VOX:Image_FileSize")) {
            sizeCol = col;
        } else if (ucd.equalsIgnoreCase("VOX:Image_Title")) {
            titleCol = col;
        }            
     }
protected void rowDataExtensionPoint(Object[] row, TreeNode valNode) {
        String imgURL = row[imgCol].toString();
        long size;
        try {
            size = Long.parseLong(row[sizeCol].toString()) / 1024; // kb.
        } catch (Throwable t) { // not found, or can't parse
            size = Long.MAX_VALUE; // assume the worse
        }
        String title; 
        if (titleCol > -1) {
            title = row[titleCol].toString();
        } else {
            title  = "untitled";
        }
        valNode.setAttribute(IMAGE_URL_ATTRIBUTE,imgURL);
        String type = row[formatCol].toString();
        valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE ,type);

        valNode.setAttribute(LABEL_ATTRIBUTE,title + ", " + StringUtils.substringAfterLast(type,"/") + ", " + size + "k");   
        }        
        
    protected boolean isWorthProceeding() {
        return super.isWorthProceeding() && imgCol >= 0;
    }  
    
    // extended - resets our new variables too.
    public void endTable() throws SAXException {
        super.endTable();
        imgCol = -1;
        formatCol = -1;
        sizeCol = -1;
        titleCol = -1;           
    }
        
    } // end table handler class.

    public String getServiceType() {
        return SIAP;
    }
    
    public static final String SIAP = "SIAP";
}