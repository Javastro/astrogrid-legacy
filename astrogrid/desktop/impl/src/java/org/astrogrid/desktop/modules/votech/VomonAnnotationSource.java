/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URI;

import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.votech.VoMon;
import org.votech.VoMonBean;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 19, 200712:42:40 PM
 */
public class VomonAnnotationSource extends DynamicAnnotationSource {

	
	
	public VomonAnnotationSource(VoMonInternal vomon) {
		super(URI.create("votech://vomon"), "Monitoring");
		this.vomon = vomon;
		setSortOrder(3);
	}
	private final VoMonInternal vomon;
	public Annotation getAnnotationFor(Resource r) {
		Annotation ann = new Annotation();
		ann.setResourceId(r.getId());
		ann.setSource(this);
		// slightly different formatting than getTooltipInformationFor
		if (r instanceof CeaApplication) {
			VoMonBean[] beans = vomon.checkCeaAvailability(r.getId());
			if (beans == null || beans.length ==0) {
				ann.setNote("Not known");
			} else {
				// non-synchronized version of StringBuffer.
				HtmlBuilder sb = new HtmlBuilder();
				for (int i =0; i < beans.length; i++) {
					VoMonBean b = beans[i];
					sb.append("<a href='")
					.append(b.getId())
					.append("'>")
					.append(b.getId().getAuthority())
					.append(b.getId().getPath())
					.append("</a><br>")
					.append(" - <b>")
					.append(b.getStatus())
					.append("</b> at ")
					.append(b.getTimestamp())
					.append("<br>");
				}			
				ann.setNote(sb.toString());
			}
		} else if (r instanceof Service){
		    ann.setNote(vomon.getTooltipInformationFor(r));
		} else {
			return null;
		}
		
		return ann;
	}
	public boolean shouldCache() {
		return false; // no point, as this data is cached anyway.
	}

}
