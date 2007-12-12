/*$Id: SsapRetrieval.java,v 1.15 2007/12/12 13:54:12 nw Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.MonitoringInputStream;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

public class SsapRetrieval extends Retriever {
    public SsapRetrieval(Service service,TreeNode primaryNode,VizModel model,Ssap ssap,double ra,double dec,double raSize, double decSize) {
        super(service,primaryNode,model,ra,dec);
        this.ssap = ssap;
        this.raSize= raSize;
        this.decSize = decSize;
    }
    protected final Ssap ssap;
    protected final double raSize;
    protected final double decSize;
    protected Object construct() throws Exception {
        reportProgress("Constructing query");        
        URL ssapURL =  ssap.constructQueryS(service.getId(),ra,dec,raSize,decSize);
        StringBuffer sb = new StringBuffer();
        sb.append("<html>Title: ").append(service.getTitle())
            .append("<br>ID: ").append(service.getId());
//                if (service.getContent() != null) {
//               sb.append("<br>Description: <p>")
//                .append(service.getContent().getDescription()!= null 
//                			?   WordUtils.wrap(service.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
//            }
                sb.append("</html>"); 
        // build subtree for this service
                reportProgress("Querying service");
        final MonitoringInputStream monitorStream = MonitoringInputStream.create(this,ssapURL,MonitoringInputStream.ONE_KB );
        TreeNode serviceNode = createServiceNode(ssapURL
                ,monitorStream.getSize()
                ,sb.toString());
        InputSource source = new InputSource(monitorStream);
        AstroscopeTableHandler th = new SsapTableHandler(serviceNode);
        parseTable(source, th); 
        return th;                 
    }
    //constants used with tablehandler.
    // the keys are UCDs - this makes it simpler to pass all metadata onto another app via plastic.
    public static final String SPECTRA_URL_ATTRIBUTE = "DATA_LINK";
    public static final String SPECTRA_TITLE_ATTRIBUTE = "VOX:Image_Title";
    public static final String SPECTRA_AXES_ATTRIBUTE = "VOX:Spectrum_axes";
    public static final String SPECTRA_DIMEQ_ATTRIBUTE = "VOX:Spectrum_dimeq";
    public static final String SPECTRA_SCALEQ_ATTRIBUTE ="VOX:Spectrum_scaleq";
    public static final String SPECTRA_FORMAT_ATTRIBUTE = "VOX:Spectrum_Format";
    public static final String SPECTRA_UNITS_ATTRIBUTE = "VOX:Spectrum_units";
    public static final String SPECTRA_START_TIME_ATTRIBUTE = "VOX:OBS_START_TIME";
    public static final String SPECTRA_END_TIME_ATTRIBUTE = "VOX:OBS_END_TIME";
    public static final String SPECTRA_OBS_ID_ATTRIBUTE = "OBS_ID";
    
    public class SsapTableHandler extends BasicTableHandler {


        public SsapTableHandler(TreeNode serviceNode) {
            super(serviceNode);
        }
        int urlCol = -1;
        int titleCol = -1;
        int formatCol = -1;
        int spectrumAxesCol = -1;
        int spectrumDimeqCol = -1;
        int spectrumScaleqCol = -1;
        int spectrumUnitsCol = -1;
        int endTimeCol = -1;
        int startTimeCol = -1;
        int obsIdCol = -1;
        
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
       
        
        protected void startTableExtensionPoint(int col, ColumnInfo columnInfo) {
            super.startTableExtensionPoint(col, columnInfo);
            String ucd  = columnInfo.getUCD();
            if (ucd == null) {
                return;
            }
            if (ucd.equalsIgnoreCase(SPECTRA_URL_ATTRIBUTE)) {
                urlCol = col;
            } else if (ucd.equalsIgnoreCase(SPECTRA_TITLE_ATTRIBUTE)) {
                titleCol = col;
            } else if (ucd.equalsIgnoreCase(SPECTRA_FORMAT_ATTRIBUTE)) {
                formatCol = col;
            } else if (ucd.equalsIgnoreCase(SPECTRA_AXES_ATTRIBUTE)) {
                spectrumAxesCol = col;
            } else if (ucd.equalsIgnoreCase(SPECTRA_DIMEQ_ATTRIBUTE)) {
                spectrumDimeqCol = col;
            } else if (ucd.equalsIgnoreCase(SPECTRA_SCALEQ_ATTRIBUTE)) {
                spectrumScaleqCol = col;
            } else if (ucd.equalsIgnoreCase(SPECTRA_UNITS_ATTRIBUTE)){
            	spectrumUnitsCol = col;
            } else if (ucd.equalsIgnoreCase(SPECTRA_OBS_ID_ATTRIBUTE)) {
                obsIdCol = col;
            } else if (ucd.equalsIgnoreCase(SPECTRA_START_TIME_ATTRIBUTE)) {
                startTimeCol = col;
            } else if (ucd.equalsIgnoreCase(SPECTRA_END_TIME_ATTRIBUTE)) {
                endTimeCol= col;
            }
        }
        
        protected void rowDataExtensionPoint(Object[] row, TreeNode valNode) {
            try {
                URL url = new URL(safeTrim(row[urlCol]));
                valNode.setAttribute(SPECTRA_URL_ATTRIBUTE,url.toString());

                String title;
                if (titleCol > -1) {
                    title = safeTrim(row[titleCol]);
                } else {
                    title  = "untitled";
                }
                valNode.setAttribute(SPECTRA_TITLE_ATTRIBUTE,title);
                
                String type;
                if (formatCol > -1) {
                    type = safeTrim(row[formatCol]);
                } else {
                    type="unknown";
                }
                valNode.setAttribute(SPECTRA_FORMAT_ATTRIBUTE ,type);

                valNode.setAttribute(LABEL_ATTRIBUTE,title + ", " + StringUtils.substringAfterLast(type,"/"));

                if (spectrumAxesCol > -1) {                   
                    valNode.setAttribute(SPECTRA_AXES_ATTRIBUTE,safeTrim(row[spectrumAxesCol]));
                }
                
                if (spectrumDimeqCol > -1) {                
                    valNode.setAttribute(SPECTRA_DIMEQ_ATTRIBUTE,safeTrim(row[spectrumDimeqCol]));                
                }
                
                if (spectrumScaleqCol > -1) {                 
                    StringBuffer sb = new StringBuffer();
                    Object o = row[spectrumScaleqCol];
                    // sometimes seems to be a string, sometimes a double array
                    if (o.getClass().isArray() && o.getClass().getComponentType() == Double.TYPE) {
                        double[] is = (double[]) o;
                        for (int i = 0; i < is.length ; i++) {
                            sb.append(is[i]);
                            sb.append(" ");
                        }
                    } else  {
                        sb.append(safeTrim(o));
                    }
                    valNode.setAttribute(SPECTRA_SCALEQ_ATTRIBUTE,sb.toString());                
                }

                if (spectrumUnitsCol > -1) {
                    valNode.setAttribute(SPECTRA_UNITS_ATTRIBUTE,safeTrim(row[spectrumUnitsCol]));
                }

                // now build a file object of the results.
                try {
                    // construct a time, in order of preference.
                    Date parse = null;
                    if (endTimeCol != -1) {
                            parse = dfA.parse(row[endTimeCol].toString(),new ParsePosition(0));
                            if (parse == null) {
                                parse = dfB.parse(row[endTimeCol].toString(),new ParsePosition(0));
                            }
                    }
                    if (parse == null && startTimeCol != -1) {
                        parse = dfA.parse(row[startTimeCol].toString(),new ParsePosition(0));
                        if (parse == null) {
                            parse = dfB.parse(row[startTimeCol].toString(),new ParsePosition(0));
                        }                       
                    }
                    if (parse == null) {
                        parse = new Date();
                    }
                    
                    AstroscopeFileObject fileObject = model.createFileObject(url
                            ,AstroscopeFileObject.UNKNOWN_SIZE
                            ,parse.getTime()
                            ,StringUtils.containsIgnoreCase(type,"fits") ? VoDataFlavour.MIME_FITS_SPECTRUM : type
                    );
                    filenameBuilder.clear();
                    filenameBuilder.append(StringUtils.replace(title,"/","_"));
                    if (obsIdCol != -1) {
                        Object o = row[obsIdCol];
                        if (o != null) {
                            filenameBuilder.append(" (")
                            .append(StringUtils.replace(o.toString(),"/","_"))
                            .append(")");
                        }
                    }
                    filenameBuilder.append(".");
                    filenameBuilder.append(StringUtils.substringAfterLast(type,"/"));
                    model.addResultFor(service,filenameBuilder.toString(),fileObject,(FileProducingTreeNode)valNode);                    
                } catch (FileSystemException e) {
                    logger.warn(service.getId() + " : Unable to create result file object - skipping row",e);
                }
            }catch (MalformedURLException e) {
                logger.warn(service.getId() + " : Unable to parse url in service response - skipping row",e);
            }           
        }
        private final StrBuilder filenameBuilder = new StrBuilder();
        //3 different formats, for the various formats seen 'in the wild'.
        // "1998-01-31 11:26:00.792"
        // 1995-03-13 19:50:17
        //Aug 22 1995  8:25:23:150PM - non-standard - shan't parse.
        private final DateFormat dfA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        private final DateFormat dfB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
    	public DefaultTreeNode createValueNode() {
    	    return new FileProducingTreeNode();
   	}        
    	
    	protected boolean omitRowFromTooltip(int rowIndex) {
    	    return rowIndex == urlCol;
    	}
        
        protected boolean isWorthProceeding() {
            return super.isWorthProceeding() && urlCol >= 0; // minimal subset of stuff.
        }
        //make it hyper-strict - to make life easy for vospec
        /*
        protected boolean isWorthProceeding() {
            return super.isWorthProceeding() 
                && urlCol >= 0
                && titleCol >= 0
                && formatCol >= 0
                && spectrumAxesCol >= 0
                && spectrumDimeqCol >= 0
                && spectrumScaleqCol >= 0                
            ; 
                //@todo should I require units too?
        } */       
        
        public void endTable() throws SAXException {
            if (skipNextTable || resultsTableParsed) {
                return;
                
            }
            resultsTableParsed = true;
                super.endTable();
                urlCol= -1;
                titleCol = -1;
                formatCol = -1;
                endTimeCol = -1;
                startTimeCol = -1;
                obsIdCol = -1;                
            
        }
    }// end table handler class.
    public String getServiceType() {
        return SSAP;
    }
    public static final String SSAP = "SSAP";
}

/* 
$Log: SsapRetrieval.java,v $
Revision 1.15  2007/12/12 13:54:12  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.14  2007/11/28 09:03:51  nw
Complete - task 256: Complete retriever bugfxes

Revision 1.13  2007/11/27 08:19:01  nw
integrate commons.io
progress tracking or reading from streams.

Revision 1.12  2007/11/26 14:44:45  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.11  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.10  2007/05/10 19:35:22  nw
reqwork

Revision 1.9  2007/04/18 15:47:08  nw
tidied up voexplorer, removed front pane.

Revision 1.8  2007/03/08 17:43:56  nw
first draft of voexplorer

Revision 1.7  2007/02/06 18:56:10  nw
made response parsing resilient to whitespace in columns.

Revision 1.6  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.5  2006/09/14 13:52:59  nw
implemented plastic spectrum messaging.

Revision 1.4  2006/08/15 09:59:58  nw
migrated from old to new registry models.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.6.1  2006/04/14 02:45:00  nw
finished code.extruded plastic hub.

Revision 1.2  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/