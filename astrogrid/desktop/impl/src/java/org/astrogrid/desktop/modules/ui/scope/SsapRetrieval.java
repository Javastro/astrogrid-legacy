/*$Id: SsapRetrieval.java,v 1.1 2006/02/02 14:51:11 nw Exp $
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

import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.ivoa.SiapInformation;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.scope.Retriever.BasicTableHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;

import edu.berkeley.guir.prefuse.graph.TreeNode;

import java.net.URI;
import java.net.URL;
import java.util.Map;

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
    public class SsapTableHandler extends BasicTableHandler {
        public SsapTableHandler(TreeNode serviceNode) {
            super(serviceNode);
        }
        int urlCol = -1;
        int titleCol = -1;
        int formatCol = -1;
        
        protected void startTableExtensionPoint(int col, ColumnInfo columnInfo) {
            super.startTableExtensionPoint(col, columnInfo);
            String ucd  = columnInfo.getUCD();
            if (ucd == null) {
                return;
            }
            if (ucd.equals("DATA_LINK")) {
                urlCol = col;
            } else if (ucd.equals("VOX:Image_Title")) {
                titleCol = col;
            } else if (ucd.equals("VOX:Spectrum_Format")) {
                formatCol = col;
            }
            
        }
        
        protected void rowDataExtensionPoint(Object[] row, TreeNode valNode) {
            String imgURL = row[urlCol].toString();
          
            String title;
            String type;
            if (titleCol > -1) {
                title = row[titleCol].toString();
            } else {
                title  = "untitled";
            }
            valNode.setAttribute("imgURL",imgURL);
            if (formatCol > -1) {
                String format = row[formatCol].toString();
                type =  StringUtils.substringAfterLast(format,"/");
            } else {
                type="unknown";
            }
            valNode.setAttribute("type" ,type);
                
            /* unused
            if (size < MAX_INLINE_IMAGE_SIZE && (format.equals("image/gif") || format.equals("image/png") || format.equals("image/jpeg"))) {
                valNode.setAttribute("preview",imgURL); // its a small image, of a suitable format for viewing.
            }
            */
            valNode.setAttribute("label",title + ", " + type);               
        }
        
        protected boolean isWorthProceeding() {
            return super.isWorthProceeding() && urlCol >= 0;
        }
        
        public void endTable() throws SAXException {
            super.endTable();
            urlCol= -1;
            titleCol = -1;
            formatCol = -1;
        }
    }// end table handler class.
}

/* 
$Log: SsapRetrieval.java,v $
Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/