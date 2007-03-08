/*$Id: SsapRetrieval.java,v 1.8 2007/03/08 17:43:56 nw Exp $
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

import java.net.URI;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
import edu.berkeley.guir.prefuse.graph.TreeNode;

public class SsapRetrieval extends Retriever {
    public SsapRetrieval(UIComponent comp,Service service,TreeNode primaryNode,VizModel model,Ssap ssap,double ra,double dec,double raSize, double decSize) {
        super(comp,service,primaryNode,model,ra,dec);
        this.ssap = ssap;
        this.raSize= raSize;
        this.decSize = decSize;
    }
    protected final Ssap ssap;
    protected final double raSize;
    protected final double decSize;
    protected Object construct() throws Exception {
        URL ssapURL =  ssap.constructQueryS(service.getId(),ra,dec,raSize,decSize);
        StringBuffer sb = new StringBuffer();
        sb.append("<html>Title: ").append(service.getTitle())
            .append("<br>ID: ").append(service.getId());
                if (service.getContent() != null) {
               sb.append("<br>Description: <p>")
                .append(service.getContent().getDescription()!= null 
                			?   WordUtils.wrap(service.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
            }
                sb.append("</html>");        TreeNode serviceNode = createServiceNode(ssapURL,sb.toString());
        // build subtree for this service

        InputSource source = new InputSource(ssapURL.openStream());
        SummarizingTableHandler th = new SsapTableHandler(serviceNode);
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
            }
            
        }
        
        protected void rowDataExtensionPoint(Object[] row, TreeNode valNode) {
            String url = safeTrim(row[urlCol]);
            valNode.setAttribute(SPECTRA_URL_ATTRIBUTE,url);
          
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
        }
        
        /*
        protected boolean isWorthProceeding() {
            return super.isWorthProceeding() && urlCol >= 0; // minimal subset of stuff.
        }*/
        //make it hyper-strict - to make life easy for vospec
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
        }        
        
        public void endTable() throws SAXException {
            super.endTable();
            urlCol= -1;
            titleCol = -1;
            formatCol = -1;
        }
    }// end table handler class.
    public String getServiceType() {
        return SSAP;
    }
    public static final String SSAP = "SSAP";
}

/* 
$Log: SsapRetrieval.java,v $
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