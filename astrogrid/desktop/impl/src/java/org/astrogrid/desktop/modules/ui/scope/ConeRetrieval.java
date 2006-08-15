package org.astrogrid.desktop.modules.ui.scope;

import java.net.URI;
import java.net.URL;

import org.apache.commons.lang.WordUtils;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.xml.sax.InputSource;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/** taks that retreives, parses and adds to the display the results of one cone service 
 * 
 * */
public class ConeRetrieval extends Retriever {
    
    public ConeRetrieval(UIComponent comp,Service information,TreeNode primaryNode,VizModel model, Cone cone, double ra, double dec, double sz)  {
        super(comp,information,primaryNode,model,ra,dec);
        this.sz = sz;
        this.cone = cone;
    } 
    private final Cone cone;
    private final double sz;
    protected Object construct() throws Exception {
        URL coneURL = cone.constructQuery(new URI(getFirstEndpoint(information).toString()),ra,dec,sz);        
        // construct 2 urls - one that returns minimal results to query on to get data for astroscope.
        // other - with fullest data -  to use as the url passed to plastic apps / saved to disk
        final URL prelimURL = cone.addOption(coneURL,"VERB","1"); // least verbose.
        final URL fullURL = cone.addOption(coneURL,"VERB","3"); // most verbose
        StringBuffer sb = new StringBuffer();
        sb.append("<html>Title: ").append(information.getTitle())
            .append("<br>ID: ").append(information.getId());
            if (information.getContent() != null) {
               sb.append("<br>Description: <p>")
                .append(information.getContent().getDescription()!= null 
                			?   WordUtils.wrap(information.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
            }
                sb.append("</html>");                 
        
        TreeNode serviceNode = createServiceNode(fullURL, sb.toString());
        
        InputSource source = new InputSource(prelimURL.openStream());
        SummarizingTableHandler th = new BasicTableHandler(serviceNode);
        parseTable(source, th);
        return th;
    }
    public String getServiceType() {
        return CONE;
    }
    
    public static final String CONE = "cone";
    
}