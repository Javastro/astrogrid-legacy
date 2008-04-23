package org.astrogrid.desktop.modules.ui.scope;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.modules.ui.MonitoringInputStream;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** task that retrives, parses and adds to the display results of one siap service
 * @TEST
 *  Implements query and parsing of result in line with v1.00 of the draft spec. (24/5/2004)
 * */
public class SiapRetrieval extends Retriever {


    public SiapRetrieval(Service service, SiapCapability cap, URI acurl, NodeSocket socket,VizModel model, Siap siap,double ra, double dec, double raSize,double decSize)  {
        super(service,cap,socket,model,ra,dec);
        this.accessUrl = acurl;
        this.raSize = raSize;
        this.decSize = decSize;
        this.siap = siap;
    }
    private final URI accessUrl;
    private final double raSize;
    private final double decSize;
    private final Siap siap;
    protected Object construct() throws Exception{
        reportProgress("Constructing query");        
            URL siapURL = siap.constructQueryS(accessUrl, ra, dec,raSize,decSize);
            StringBuffer sb = new StringBuffer();
            sb.append("<html>Title: ").append(service.getTitle())
                .append("<br>ID: ").append(service.getId());
            String subName = getSubName();
            if (subName != null && subName.trim().length() > 0) {
                sb.append(" - ").append(subName);
            }
//            if (service.getContent() != null) {
//               sb.append("<br>Description: <p>")
//                .append(service.getContent().getDescription()!= null 
//                			?   WordUtils.wrap(service.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
//            }
            String serviceType = ((SiapService)service).findSiapCapability().getImageServiceType();
            if (StringUtils.isNotEmpty(serviceType) ){
                sb.append("</p><br>Service Type: ").append(serviceType);
            }
            sb.append("</html>");
                  
            // build subtree for this service
            reportProgress("Querying service");
            final MonitoringInputStream monitorStream = MonitoringInputStream.create(this,siapURL,MonitoringInputStream.ONE_KB );
            TreeNode serviceNode = createServiceNode(siapURL,monitorStream.getSize(),sb.toString());
            InputSource source = new InputSource(monitorStream);
            AstroscopeTableHandler th = new SiapTableHandler(serviceNode);
            parseTable(source, th);
            return th;           
    }
    /** attribute containing type extension of the image */
    public static final String IMAGE_TYPE_ATTRIBUTE = "type";
    /** attribute containing reference url for this image */
    public static final String IMAGE_URL_ATTRIBUTE = "imgURL";   
    //parse additional siap data, and ignore non results tables.
    public class SiapTableHandler extends BasicTableHandler {


        public SiapTableHandler(TreeNode serviceNode) {
            super(serviceNode);
        }
        int imgCol = -1;
        int formatCol = -1;
        int sizeCol = -1;
        int titleCol = -1;   
        int dataLinkCol = -1;
        private boolean skipNextTable = false;
        private boolean resultsTableParsed = false;
        
        public void resource(String name, String id, String type)
                throws SAXException {
            skipNextTable = ! "results".equals(type);
        }
        
        public void startTable(StarTable starTable) throws SAXException {
            if (skipNextTable || resultsTableParsed) {
                return;
            }
                super.startTable(starTable);
        }
        public void rowData(Object[] row) throws SAXException {
            if (skipNextTable || resultsTableParsed) {
                return;
            }
           super.rowData(row);
            
        }
       
        
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
        
        protected boolean omitRowFromTooltip(int rowIndex) {
            return rowIndex ==dataLinkCol || rowIndex == imgCol;
        }
        
        protected void rowDataExtensionPoint(Object[] row, TreeNode valNode) {
            try {
                URL imgURL = new URL(safeTrim(row[imgCol]));
                valNode.setAttribute(IMAGE_URL_ATTRIBUTE,imgURL.toString());

                long size;
                try {
                    size = Long.parseLong(safeTrim(row[sizeCol])) ;
                } catch (Throwable t) { // not found, or can't parse
                    size = AstroscopeFileObject.UNKNOWN_SIZE;
                }

                String title; 
                if (titleCol > -1) {
                    title = safeTrim(row[titleCol]);
                } else {
                    title  = "untitled";
                }
                
                String type = safeTrim(row[formatCol]);
                valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE ,type);

                valNode.setAttribute(LABEL_ATTRIBUTE
                        ,title + ", " + StringUtils.substringAfterLast(type,"/") 
                        + ", " + 
                          (size == AstroscopeFileObject.UNKNOWN_SIZE
                              ? "unknown size"
                              : FileUtils.byteCountToDisplaySize(size)));
                try {
                    long date = new Date().getTime(); //@fixme work out this 
                    AstroscopeFileObject fileObject = model.createFileObject(imgURL
                            ,size
                            ,date
                            ,StringUtils.containsIgnoreCase(type,"fits") ? VoDataFlavour.MIME_FITS_IMAGE : type
                            );
                    filenameBuilder.clear();
                    filenameBuilder.append(StringUtils.replace(title,"/","_"));
                    filenameBuilder.append(".");
                    filenameBuilder.append(StringUtils.substringAfterLast(type,"/"));
                    model.addResultFor(SiapRetrieval.this,filenameBuilder.toString(),fileObject,(FileProducingTreeNode)valNode);
                } catch (FileSystemException e) {
                    logger.warn(service.getId() + " : Unable to create result file object - skipping row ",e);
                }
            } catch(MalformedURLException e) {
                logger.warn(service.getId() + " : Unable to parse url in service response - skipping row",e);
            }
        }        

        private final StrBuilder filenameBuilder = new StrBuilder(64);
        
	public DefaultTreeNode createValueNode() {
	    return new FileProducingTreeNode();
	}
        
    protected boolean isWorthProceeding() {
    	if (imgCol == -1) {// maybe it's a non-standard service - give it a second chance.
    		imgCol = dataLinkCol;
    	}
        return super.isWorthProceeding() && imgCol >= 0;
    }  
    
    // extended - resets our new variables too.
    public void endTable() throws SAXException {
        if (skipNextTable || resultsTableParsed) {
            return;
            
        }
        resultsTableParsed = true;
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
