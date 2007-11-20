/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.votech.VoMonBean;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 19, 200712:42:40 PM
 */
public class VomonAnnotationSource extends DynamicAnnotationSource {

	
	
	public VomonAnnotationSource(VoMonInternal vomon) throws URISyntaxException {
		super(new URI(vomon.getEndpoint().toString()), "Monitoring service");
		this.vomon = vomon;
		setSortOrder(3);
	}
	private final VoMonInternal vomon;
	public Annotation getAnnotationFor(Resource r) {
		Annotation ann = new Annotation();
		ann.setResourceId(r.getId());
		ann.setSource(this);
		// slightly more compact formatting than getTooltipInformationFor
		HtmlBuilder sb = new HtmlBuilder();
		if (r instanceof CeaApplication) {
			VoMonBean[] beans = vomon.checkCeaAvailability(r.getId());
			if (beans == null || beans.length ==0) {
				sb.append("No known providing services");
			} else {
				sb.append("<i>Provided by these services:</i><br>");
				for (int i =0; i < beans.length; i++) {
					VoMonBean b = beans[i];
					sb.append("<a href='")
					.append(b.getId())
					.append("'>")
					.append(b.getId().getAuthority())
					.append(StringUtils.replace(b.getId().getPath(),"/","/<wbr>"))// mark potential word-wrap points.
					.append("</a> - ")
					.append(b.getStatus())
					.append(" at ")
					.append(b.getTimestamp())
					.append("<br>");
				}			
			}
		} else if (r instanceof Service){
		    VoMonBean b = vomon.checkAvailability(r.getId());
		    if (b == null) {
		        return null;
		    } else {
		        sb.append("Judged to be ")
		            .append(b.getStatus())
		            .append(" at ")
		            .append(b.getTimestamp());
		    }
		} else {
			return null;
		}

		ann.setNote(sb.toString());
		return ann;
	}
	public boolean shouldCache() {
		return false; // no point, as this data is cached anyway.
	}

}
