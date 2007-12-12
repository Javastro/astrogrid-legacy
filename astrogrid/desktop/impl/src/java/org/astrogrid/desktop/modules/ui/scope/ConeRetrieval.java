package org.astrogrid.desktop.modules.ui.scope;

import java.net.URL;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.MonitoringInputStream;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.scope.Retriever.BasicTableHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/** taks that retreives, parses and adds to the display the results of one cone service 
 *  
 * */
public class ConeRetrieval extends Retriever {
    
    public ConeRetrieval(Service service,TreeNode primaryNode,VizModel model, Cone cone, double ra, double dec, double sz)  {
        super(service,primaryNode,model,ra,dec);
        this.sz = sz;
        this.cone = cone;
    } 
    private final Cone cone;
    private final double sz;
    
    protected Object construct() throws Exception {
        reportProgress("Constructing query");
        URL coneURL = cone.constructQuery(service.getId(),ra,dec,sz);        
        // construct 2 urls - one that returns minimal results to query on to get data for astroscope.
        // other - with fullest data -  to use as the url passed to plastic apps / saved to disk
        final URL prelimURL = cone.addOption(coneURL,"VERB","1"); // least verbose.
        final URL fullURL = cone.addOption(coneURL,"VERB","3"); // most verbose
        StringBuffer sb = new StringBuffer();
        sb.append("<html>").append(service.getTitle())
        .append("<br>ID: ").append(service.getId());
//        if (service.getContent() != null) {
//            sb.append("<br>Description: <p>")
//            .append(service.getContent().getDescription()!= null 
//                    ?   WordUtils.wrap(service.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
//        }
        sb.append("</html>");                 

        reportProgress("Querying service");
        final MonitoringInputStream monitorStream = MonitoringInputStream.create(this,prelimURL,MonitoringInputStream.ONE_KB * 10);
        TreeNode serviceNode = createServiceNode(fullURL,monitorStream.getSize(), sb.toString());
        InputSource source = new InputSource( monitorStream);
        AstroscopeTableHandler th = createTableHandler(serviceNode);
        parseTable(source, th);
        return th;
    }
    /** can be overridden to provide an alternat table handler */
	protected BasicTableHandler createTableHandler(TreeNode serviceNode) {
		return new ConeTableHandler(serviceNode);
	}
	
	/** extension that detected various odd ways that a cone service can report error */
	public class ConeTableHandler extends BasicTableHandler {
	    public void info(String name, String value, String content)
	            throws SAXException {
	           checkForError(name,value,content);
	    }
	    public void param(String name, String value, String description)
	            throws SAXException {
	       checkForError(name,value,description);
	    }
	    
	    private void checkForError(String name,String value,String description) throws DalProtocolException {
	        if ("Error".equals(name)) {
	            message = description != null ? description : value;	            
	            throw new DalProtocolException(message);
	        }
	    }
	    
        /**
         * @param serviceNode
         */
        public ConeTableHandler(TreeNode serviceNode) {
            super(serviceNode);
        }
	}
	
    public String getServiceType() {
        return CONE;
    }
    
    public static final String CONE = "cone";
    
}