/*$Id: SsapRetrieval.java,v 1.3 2006/04/18 23:25:44 nw Exp $
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
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
import edu.berkeley.guir.prefuse.graph.TreeNode;

public class SsapRetrieval extends Retriever {
    public SsapRetrieval(UIComponent comp,ResourceInformation information,TreeNode primaryNode,VizModel model,Ssap ssap,double ra,double dec,double raSize, double decSize) {
        super(comp,information,primaryNode,model,ra,dec);
        this.ssap = ssap;
        this.raSize= raSize;
        this.decSize = decSize;
    }
    protected final Ssap ssap;
    protected final double raSize;
    protected final double decSize;
    protected Object construct() throws Exception {
        URL ssapURL =  ssap.constructQueryS(new URI(information.getAccessURL().toString()),ra,dec,raSize,decSize);
        StringBuffer sb = new StringBuffer();
        sb.append("<html>Title: ").append(information.getTitle())
            .append("<br>ID: ").append(information.getId())
            .append("<br>Description: <p>")
            .append(information.getDescription()!= null ?   WordUtils.wrap(information.getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "")                
            .append("</html>");                        
        TreeNode serviceNode = createServiceNode(ssapURL,sb.toString());
        // build subtree for this service

        InputSource source = new InputSource(ssapURL.openStream());
        SummarizingTableHandler th = new SsapTableHandler(serviceNode);
        parseTable(source, th); 
        return th;                 
    }
    //constants used with tablehandler.
    public static final String SPECTRA_URL_ATTRIBUTE = "spectraURL";
    public static final String SPECTRA_TYPE_ATTRIBUTE = "type";
    public static final String SPECTRA_TITLE_ATTRIBUTE = "title";
    public static final String SPECTRA_AXES_ATTRIBUTE = "axes";
    public static final String SPECTRA_DIMEQ_ATTRIBUTE = "dimeq";
    public static final String SPECTRA_SCALEQ_ATTRIBUTE = "scaleq";
    
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
        
        protected void startTableExtensionPoint(int col, ColumnInfo columnInfo) {
            super.startTableExtensionPoint(col, columnInfo);
            String ucd  = columnInfo.getUCD();
            if (ucd == null) {
                return;
            }
            if (ucd.equalsIgnoreCase("DATA_LINK")) {
                urlCol = col;
            } else if (ucd.equalsIgnoreCase("VOX:Image_Title")) {
                titleCol = col;
            } else if (ucd.equalsIgnoreCase("VOX:Spectrum_Format")) {
                formatCol = col;
            } else if (ucd.equalsIgnoreCase("VOX:Spectrum_axes")) {
                spectrumAxesCol = col;
            } else if (ucd.equalsIgnoreCase("VOX:Spectrum_dimeq")) {
                spectrumDimeqCol = col;
            } else if (ucd.equalsIgnoreCase("VOX:Spectrum_scaleq")) {
                spectrumScaleqCol = col;
            }
            
        }
        
        protected void rowDataExtensionPoint(Object[] row, TreeNode valNode) {
            String url = row[urlCol].toString();
            valNode.setAttribute(SPECTRA_URL_ATTRIBUTE,url);
          
            String title;
            if (titleCol > -1) {
                title = row[titleCol].toString();
            } else {
                title  = "untitled";
            }
            valNode.setAttribute(SPECTRA_TITLE_ATTRIBUTE,title);
            String type;
            if (formatCol > -1) {
                type = row[formatCol].toString();
            } else {
                type="unknown";
            }
            valNode.setAttribute(SPECTRA_TYPE_ATTRIBUTE ,type);
                
            valNode.setAttribute(LABEL_ATTRIBUTE,title + ", " + StringUtils.substringAfterLast(type,"/"));
            
            if (spectrumAxesCol > -1) {                   
                valNode.setAttribute(SPECTRA_AXES_ATTRIBUTE,row[spectrumAxesCol].toString());
            }
            if (spectrumDimeqCol > -1) {                
                valNode.setAttribute(SPECTRA_DIMEQ_ATTRIBUTE,row[spectrumDimeqCol].toString());                
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
                } else if (o.getClass().equals(String.class)) {
                    sb.append(o.toString());
                }
                valNode.setAttribute(SPECTRA_SCALEQ_ATTRIBUTE,sb.toString());                
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