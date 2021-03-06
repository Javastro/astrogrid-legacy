package org.astrogrid.desktop.modules.ui.scope;

import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.MonitoringInputStream;
import org.xml.sax.InputSource;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/** Retreiver for simple cone search v1.03
 *  @TEST
 * */
public class ConeRetrieval extends AbstractRetriever {
    
    public ConeRetrieval(final Service service, final ConeCapability cap, final URI acurl, final NodeSocket socket, final VizModel model, final Cone cone, final double ra, final double dec, final double sz)  {
        super(service,cap,socket,model,ra,dec);
        this.accessUrl = acurl;
        this.sz = sz;
        this.cone = cone;
    } 
    private final URI accessUrl;
    private final Cone cone;
    private final double sz;
    
    @Override
    protected Object construct() throws Exception {
        reportProgress("Constructing query");
        final URL coneURL = cone.constructQuery(accessUrl,ra,dec,sz);        
        // construct 2 urls - one that returns minimal results to query on to get data for astroscope.
        // other - with fullest data -  to use as the url passed to plastic apps / saved to disk
        final URL prelimURL;
        final URL fullURL;
        if (((ConeCapability)capability).isVerbosity()) {
            // bug 2856 - only use different urls if VERB flag is supported.
            prelimURL = cone.addOption(coneURL,"VERB","1"); // least verbose.
            fullURL = cone.addOption(coneURL,"VERB","3"); // most verbose
        } else {
            // verbose flag not supported.
            prelimURL = fullURL = coneURL;
        }
        final StringBuffer sb = new StringBuffer();
        sb.append("<html>").append(service.getTitle())
        .append("<br>ID: ").append(service.getId());
        final String subName = getSubName();
        if (subName != null && subName.trim().length() > 0) {
            sb.append(" - ").append(subName);
        }
//        if (service.getContent() != null) {
//            sb.append("<br>Description: <p>")
//            .append(service.getContent().getDescription()!= null 
//                    ?   WordUtils.wrap(service.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
//        }
       // sb.append("</html>");                 

        reportProgress("Querying service");
        final MonitoringInputStream monitorStream = MonitoringInputStream.create(this,prelimURL,MonitoringInputStream.ONE_KB * 10);
        final TreeNode serviceNode = createServiceNode(fullURL,monitorStream.getSize(), sb.toString());
        final InputSource source = new InputSource( monitorStream);
        final AstroscopeTableHandler th = createTableHandler(serviceNode);
        parseTable(source, th);
        return th;
    }
    /** can be overridden to provide an alternat table handler */
	protected BasicTableHandler createTableHandler(final TreeNode serviceNode) {
		return new BasicTableHandler(serviceNode);
	}
	
    @Override
    public String getServiceType() {
        return CONE;
    }
    
    public static final String CONE = "cone";
    
}
