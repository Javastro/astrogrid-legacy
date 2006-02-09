package org.astrogrid.desktop.modules.ui.scope;

import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;

import org.apache.commons.lang.WordUtils;
import org.xml.sax.InputSource;

import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;

import java.net.URI;
import java.net.URL;
import java.util.Map;

/** taks that retreives, parses and adds to the display the results of one cone service 
 * 
 * */
public class ConeRetrieval extends Retriever {
    
    public ConeRetrieval(UIComponent comp,ResourceInformation information,TreeNode primaryNode,VizModel model, Cone cone, double ra, double dec, double sz)  {
        super(comp,information,primaryNode,model,ra,dec);
        this.sz = sz;
        this.cone = cone;
    } 
    private final Cone cone;
    private final double sz;
    protected Object construct() throws Exception {
        URL coneURL = cone.constructQuery(new URI(information.getAccessURL().toString()),ra,dec,sz);          
        StringBuffer sb = new StringBuffer();
        sb.append("<html>Title: ").append(information.getTitle())
            .append("<br>ID: ").append(information.getId())
            .append("<br>Description: <p>")
            .append(information.getDescription()!= null ?   WordUtils.wrap(information.getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "")
            .append("</p></html>");                        
        TreeNode serviceNode = createServiceNode(coneURL, sb.toString());
        
        InputSource source = new InputSource(coneURL.openStream());
        SummarizingTableHandler th = new BasicTableHandler(serviceNode);
        parseTable(source, th);
        return th;
    }
    public String getServiceType() {
        return CONE;
    }
    
    public static final String CONE = "cone";
    
}