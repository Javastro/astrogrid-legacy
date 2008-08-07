package org.astrogrid.desktop.modules.ui.scope;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.desktop.modules.ui.MonitoringInputStream;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;


/** 
 * task that retrives, parses and adds to the display results of one siap service 
 * @TEST
 * */
public class StapRetrieval extends AbstractRetriever {
	/**
	 * Logger for this class
	 */
	static final Log logger = LogFactory.getLog(StapRetrieval.class);

	private final Date start;
	private final Date end;
	private final String format;
    private final URI accessUrl;

	public StapRetrieval(final Service information,final StapCapability cap,final URI acurl,final NodeSocket socket,final VizModel model, 
			final Stap stap, final Date start,final Date end, final double ra, final double dec, final double raSize,final double decSize, final String format)  {
		super(information,cap,socket,model,ra,dec);
        this.accessUrl = acurl;
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
	@Override
    protected Object construct() throws Exception{
        reportProgress("Constructing query");	    
		URL stapURL = null;
		//check if there is a ra,dec and construct a stap query accordingly.
		if(Double.isNaN(ra) || Double.isNaN(dec)) {
			if(format != null) {
                stapURL = stap.constructQueryF(accessUrl,start, end, format);
            } else {
                stapURL = stap.constructQuery(accessUrl,start, end);
            }
		}
		else {
			if(format != null) {
                stapURL = stap.constructQuerySF(accessUrl,start, end, ra, dec, raSize, decSize, format);
            } else {
                stapURL = stap.constructQueryS(accessUrl,start, end, ra, dec, raSize, decSize);
            }
		}

		final StringBuffer sb = new StringBuffer();
		sb.append("<html>Title: ").append(service.getTitle())
		.append("<br>ID: ").append(service.getId());
        final String subName = getSubName();
        if (subName != null && subName.trim().length() > 0) {
            sb.append(" - ").append(subName);
        }
//		if (service.getContent() != null) {
//			sb.append("<br>Description: <p>")
//			.append(service.getContent().getDescription()!= null 
//					?   WordUtils.wrap(service.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
//		}
		//sb.append("</html>");
		//.append("</p><br>Service Type: ").append(((StapInformation)information).getImageServiceType())

		reportProgress("Querying service");
		// build subtree for this service
		final MonitoringInputStream monitorStream = MonitoringInputStream.create(this,stapURL,MonitoringInputStream.ONE_KB );
		final TreeNode serviceNode = createServiceNode(stapURL,monitorStream.getSize(), sb.toString());
        final InputSource source = new InputSource( monitorStream);
		final AstroscopeTableHandler th = new StapTableHandler(serviceNode);
		parseTable(source, th);
		return th;           
	}
	/** attribute containing type extension of the image */
	public static final String IMAGE_TYPE_ATTRIBUTE = "type";
	/** attribute containing reference url for this image */
	public static final String IMAGE_URL_ATTRIBUTE = "imgURL";   
	public class StapTableHandler extends BasicTableHandler {


		public StapTableHandler(final TreeNode serviceNode) {
			super(serviceNode);
		}
		int accessCol = -1;
		int formatCol = -1;
		int sizeCol = -1;
		int titleCol = -1;   
		int instCol = -1;
		int timeStart = -1;
		int timeEnd = -1;
		//voevent extensions
		int referencesCol = -1;
		int parametersCol = -1;

		@Override
        protected void startTableExtensionPoint(final int col,final ColumnInfo columnInfo) {
			final String ucd = columnInfo.getUCD();
			final String colName = columnInfo.getName();

			if (ucd != null && ucd.equalsIgnoreCase("VOX:AccessReference")) {
				accessCol = col;
			} else if (ucd != null && ucd.equalsIgnoreCase("VOX:Format")) {
				formatCol = col;
			} else if (ucd != null && ucd.equalsIgnoreCase("VOX:Image_Title")) {
				titleCol = col;
			} else if (ucd != null && ucd.equalsIgnoreCase("INST_ID")) {
				instCol = col;
			} else if (ucd != null && ucd.equalsIgnoreCase("time.obs.start")) {
				timeStart = col;            
			} else if (ucd != null && ucd.equalsIgnoreCase("time.obs.end")) {
				timeEnd = col;
			} else if (colName != null && colName.equals("References")) { // voevent special case.
				referencesCol = col;
			} else if (colName != null && colName.equals("Parameters")) {
				parametersCol = col;
			}
		}


		/** called once for each row in the table
		 * @see uk.ac.starlink.votable.TableHandler#rowData(java.lang.Object[])
		 */
		@Override
        public void rowData(final Object[] row) throws SAXException {
		    isWorthProceeding();
			resultCount++;

			//String rowRa = row[raCol].toString();
			//String rowDec = row[decCol].toString();

			final DefaultTreeNode valNode = createValueNode();
			//String positionString = chopValue(String.valueOf(rowRa),2) + "," + chopValue(String.valueOf(rowDec),2) ;
			valNode.setAttribute(LABEL_ATTRIBUTE,"*");
			valNode.setAttribute(SERVICE_TYPE_ATTRIBUTE,getServiceType());
			// unused
			//valNode.setAttribute(RA_ATTRIBUTE,rowRa); // these might come in handy for searching later.
			//valNode.setAttribute(DEC_ATTRIBUTE,rowDec);

			// handle further parsing in subclasses.
			try {
			URL imgURL = null;
			if(row[accessCol] != null) {
				imgURL = new URL(safeTrim(row[accessCol]));
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

			valNode.setAttribute(IMAGE_URL_ATTRIBUTE,imgURL.toString());
			final StringBuffer tooltip = new StringBuffer();
			tooltip.append("<html>");//.append(rowRa).append(", ").append(rowDec);
			for (int v = 0; v < row.length; v++) {
				if (v == parametersCol || v == referencesCol || v == accessCol) {
					continue; // don't want to add these to the tooltip - treated separately below. 
				}
				final Object o = row[v];
				if (o == null) {
					continue;
				}
				tooltip
				.append(titles[v])
				.append( ": ")
				.append(safeTrim(o))
				.append("<br>")
				;
			}
			// tool tip finished later - after we've checked whether this is a voevent..
			String type = null;
			if(formatCol > -1 && row[formatCol] != null) {
				type = safeTrim(row[formatCol]);
				//NWW - don't see why the following is necessary..
				//@todo Should fix this more correctly on the retriever or check about stap doing a real mime type on the return.
				//    if(type.indexOf('-') > -1) {
				//        valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE ,"/" + type.substring(type.indexOf('-')+1).toLowerCase());
				//   }else {
				//      valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE ,"/" +  type.toLowerCase());
				//  }
				valNode.setAttribute(IMAGE_TYPE_ATTRIBUTE,type);
				// additional rules for elizabeth's voevent-returning services..
				if (StringUtils.containsIgnoreCase(type,"voevent")) {
					// parse further... add new nodes for each 'reference'
					if (referencesCol != -1) {
						final String[] refs = StringUtils.split(safeTrim(row[referencesCol]),',');
						for (int i = 0; i < refs.length; i++) {
							final String[] kv = StringUtils.split(refs[i],"=",2);
							if (kv.length != 2) {
								continue;
							}
							final FileProducingTreeNode referenceNode = new FileProducingTreeNode();
				            try {
				                final URL u = new URL(safeTrim(kv[1]));
				                final long date = new Date().getTime(); //@fixme worth this out.
				                final long size = AstroscopeFileObject.UNKNOWN_SIZE; //@todo possible to get this info from somewhere?
				                final String label = safeTrim(kv[0]);
				                final AstroscopeFileObject afo = model.createFileObject(u
				                        ,size
				                        ,date
				                        ,null // don't know the mime type.
				                );
				                final String name = afo.getName().getBaseName();
				                model.addResultFor(StapRetrieval.this,StringUtils.replace(details,"/","_") + " - " + StringUtils.replace(label,"/","_") + " - " + name,afo,referenceNode);
				                referenceNode.setAttribute(LABEL_ATTRIBUTE,label + " (" + name +")");
				                referenceNode.setAttribute(IMAGE_URL_ATTRIBUTE,u.toString());
				                referenceNode.setAttribute(TOOLTIP_ATTRIBUTE,u.toString());
				                valNode.addChild(new DefaultEdge(valNode,referenceNode));
				                resultCount++;
				            } catch(final FileSystemException e) {
				                logger.warn(service.getId() + " : Unable to create result file object - skipping row",e);
				            }catch(final MalformedURLException e) {
				                logger.warn(service.getId() + " : Unable to parse url in service response - skipping row",e);
				            }
						}
					}
					if (parametersCol != -1) {
						// and finally augment the tooltip.
						final String[] params = StringUtils.split(safeTrim(row[parametersCol]),',');
						for (int i = 0; i < params.length; i++) {
							final String[] kv = StringUtils.split(params[i],"=",2);
							if (kv.length != 2) {
								continue;
							}
							tooltip.append("<br>")
							.append(safeTrim(kv[0]))
							.append(": ")
							.append(safeTrim(kv[1]));
						}
					}
				}                
			} 
			// add the tool tip in.
			tooltip.append("</p></html>");
			valNode.setAttribute(TOOLTIP_ATTRIBUTE,tooltip.toString());            
			final String instrumentID = safeTrim(row[instCol]);
			TreeNode instrNode =findNode(instrumentID, getServiceNode());
			if(instrNode == null) {
				instrNode = new DefaultTreeNode();
				instrNode.setAttribute(LABEL_ATTRIBUTE,instrumentID);
				instrNode.setAttribute(TOOLTIP_ATTRIBUTE,"Instrument: " + instrumentID);
				getServiceNode().addChild(new DefaultEdge(getServiceNode(),instrNode));
			}
			instrNode.addChild(new DefaultEdge(instrNode,valNode));
			try {
			    final long date = new Date().getTime(); //@fixme worth this out.
			    final long size = AstroscopeFileObject.UNKNOWN_SIZE; //@todo possible to get this info from somewhere?
			    final AstroscopeFileObject afo = model.createFileObject(imgURL
			            ,size
			            ,date
                        ,StringUtils.containsIgnoreCase(type,"fits") ? VoDataFlavour.MIME_FITS_IMAGE : type
                );
			    filenameBuilder.clear();
			    filenameBuilder.append(StringUtils.replace(details,"/","_"));
			    if (StringUtils.contains(type,"/")) {
			        filenameBuilder.append(".");
			        filenameBuilder.append(StringUtils.substringAfterLast(type,"/"));
			    } else if (StringUtils.isNotEmpty(type)) {
		                 filenameBuilder.append(".");
			        filenameBuilder.append(type.trim().toLowerCase());
			    }
			    model.addResultFor(StapRetrieval.this,filenameBuilder.toString(),afo,(FileProducingTreeNode)valNode);
                valNode.setAttribute(LABEL_ATTRIBUTE,filenameBuilder.toString());
			} catch(final FileSystemException e) {
			    logger.warn(service.getId() + " : Unable to create result file object - skipping row",e);
			}
            } catch(final MalformedURLException e) {
                logger.warn(service.getId() + " : Unable to parse url in service response - skipping row",e);
            }			


		}
		private final StrBuilder filenameBuilder = new StrBuilder(64);

		@Override
        public DefaultTreeNode createValueNode() {
		    return new FileProducingTreeNode();
			
		}

		@Override
        protected void isWorthProceeding() throws InsufficientMetadataException {
		    if (accessCol == -1) {
		        throw new InsufficientMetadataException("Access Reference column not detected");
		    }
		}  

		// extended - resets our new variables too.
		@Override
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

	@Override
    public String getServiceType() {
		return STAP;
	}

	public static final String STAP = "STAP";
}
