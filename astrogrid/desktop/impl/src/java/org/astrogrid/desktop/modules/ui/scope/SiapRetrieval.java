package org.astrogrid.desktop.modules.ui.scope;

import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.Retriever.FileProducingTreeNode;
import org.astrogrid.desktop.modules.ui.scope.ScopeTransferableFactory.AstroscopeFileObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** task that retrives, parses and adds to the display results of one siap service 
 * */
public class SiapRetrieval extends Retriever {
    /**
     * Logger for this class
     */
    static final Log logger = LogFactory.getLog(SiapRetrieval.class);

    public SiapRetrieval(UIComponent comp,Service service,TreeNode primaryNode,VizModel model, Siap siap,double ra, double dec, double raSize,double decSize)  {
        super(comp,service,primaryNode,model,ra,dec);
        this.raSize = raSize;
        this.decSize = decSize;
        this.siap = siap;
    }
    private final double raSize;
    private final double decSize;
    private final Siap siap;
    protected Object construct() throws Exception{
            URL siapURL = siap.constructQueryS(service.getId(),ra, dec,raSize,decSize);
            StringBuffer sb = new StringBuffer();
            sb.append("<html>Title: ").append(service.getTitle())
                .append("<br>ID: ").append(service.getId());
            if (service.getContent() != null) {
               sb.append("<br>Description: <p>")
                .append(service.getContent().getDescription()!= null 
                			?   WordUtils.wrap(service.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
            }
                sb.append("</html>");
                //@todo when we get more metadata parsed in new model.
                //.append("</p><br>Service Type: ").append(((SiapInformation)information).getImageServiceType())
                  
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
        int dataLinkCol = -1;
        
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
        } else if (ucd.equalsIgnoreCase("DATA_LINK")) { // non-srtandard, but seen occasionally
        	dataLinkCol = col;
        }
     }
protected void rowDataExtensionPoint(Object[] row, TreeNode valNode) {
        String imgURL = safeTrim(row[imgCol]);
        long size;
        try {
            size = Long.parseLong(safeTrim(row[sizeCol])) / 1024; // kb.
        } catch (Throwable t) { // not found, or can't parse
            size = Long.MAX_VALUE; // assume the worse
        }
        String title; 
        if (titleCol > -1) {
            title = safeTrim(row[titleCol]);
        } else {
            title  = "untitled";
        }
        valNode.setAttribute(IMAGE_URL_ATTRIBUTE,imgURL);
        String type = safeTrim(row[formatCol]);
        valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE ,type);

        valNode.setAttribute(LABEL_ATTRIBUTE,title + ", " + StringUtils.substringAfterLast(type,"/") + ", " + size + "k");   
        }        

	public DefaultTreeNode createValueNode() {
		 return new FileProducingTreeNode() {
	        	// create a service node.
				protected FileObject createFileObject(ScopeTransferableFactory factory) throws FileSystemException {
					String type =getAttribute(IMAGE_TYPE_ATTRIBUTE);
					String url = getAttribute(IMAGE_URL_ATTRIBUTE);
					return factory.new AstroscopeFileObject(
							StringUtils.containsIgnoreCase(type,"fits")
								? VoDataFlavour.MIME_FITS_IMAGE
								: type
							,this,url);
				}
	        };
	}
        
    protected boolean isWorthProceeding() {
    	if (imgCol == -1) {// maybe it's a non-standard service - give it a second chance.
    		imgCol = dataLinkCol;
    	}
        return super.isWorthProceeding() && imgCol >= 0;
    }  
    
    // extended - resets our new variables too.
    public void endTable() throws SAXException {
        super.endTable();
        imgCol = -1;
        formatCol = -1;
        sizeCol = -1;
        titleCol = -1;           
        dataLinkCol = -1;
    }
        
    } // end table handler class.

    public String getServiceType() {
        return SIAP;
    }
    
    public static final String SIAP = "SIAP";
}