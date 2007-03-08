/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaServerCapability;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Authority;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.DataService;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Format;
import org.astrogrid.acr.ivoa.resource.HarvestCapability;
import org.astrogrid.acr.ivoa.resource.HasCoverage;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Organisation;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SearchCapability;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.Source;

/** class containing static methods to render resources to html.
 * @author Noel Winstanley
 * @since Aug 5, 20062:53:23 AM
 */
public final class ResourceFormatter {
	
	public static String renderResourceAsHTML(Resource r) {
		HtmlBuilder sb = new HtmlBuilder();
		
		sb.append("<html>");
		sb.append("<body><basefont face='Arial,Helvetica,sans-serif'>");//@todo get fonts to work.
		sb.h2(r.getTitle());
		sb.append("<i>");
		if (r.getShortName() != null ) {
			sb.append(r.getShortName()).append(", ");
		}
		sb.append(r.getId()).append("</i>");
		sb.br();
		//@future add some indication of validation level.
		sb.append(formatTypeHTML(r.getType()));
		sb.hr();
		// content.
		final Content content = r.getContent();
		formatContent(sb, content);
		if (r instanceof HasCoverage) {
			final Coverage coverage = ((HasCoverage)r).getCoverage();
			if (coverage != null) {
				formatCoverage(coverage,sb);
			}
		}
		// database.
		if (r instanceof DataCollection) {
			DataCollection d = (DataCollection)r;		
			sb.appendTitledResourceNames("Facilities",d.getFacilities())
			.appendTitledResourceNames("Instruments",d.getInstruments());

			final Format[] format = d.getFormats();
			if (format != null) {
				formatFormat(format,sb);
			}
			
			sb.appendTitledSequence("Rights",d.getRights());
			
			Catalog[] cats = d.getCatalogues();
			for (int i = 0; i < cats.length; i++) {
				formatCatalog(cats[i],sb);
			}
			sb.hr();
		}
		
		
		if (r instanceof Authority) {
			sb.appendTitledObject("Manages Organisation",((Authority)r).getManagingOrg());
			sb.hr();
		}
		
		if (r instanceof Organisation) {
			Organisation o = (Organisation)r;
			sb.appendTitledResourceNames("Facilities",o.getFacilities())
			.appendTitledResourceNames("Instruments",o.getInstruments());
			sb.hr();			
		}
		
		// cea app
		if (r instanceof CeaApplication) {
			CeaApplication cea = (CeaApplication) r;			
			formatCeaApplication(cea, sb);
			sb.hr();			
		}
		// service
		if (r instanceof Service) {
			Service s = (Service)r;
			if (r instanceof DataService) {
				DataService ds = (DataService)r;
				sb.appendTitledResourceNames("Facilities",ds.getFacilities())
				.appendTitledResourceNames("Instruments",ds.getInstruments());
				sb.hr();						
				if (r instanceof CatalogService) {
					CatalogService cs = (CatalogService)r;
					TableBean[] tables = cs.getTables();
					if (tables != null && tables.length > 0) {
						formatTables(tables,sb);
					}

				}
			}
			if (r instanceof RegistryService) {
				RegistryService rs = (RegistryService)r;
				sb.br().append("Full Registry? ");
				sb.append(rs.isFull());
				sb.br();
				sb.appendTitledSequence("Managed Authorities",rs.getManagedAuthorities());
			}
			// cone service, siap service - nothing additional - covered in the formatService call
			formatService(sb, s);
			sb.hr();			
		}
		// curation
		final Curation curation = r.getCuration();
	
		if (curation != null) {
			formatCuration(sb, curation);
		}
		
		//@todo some description of coverage, column metadata, etc.
		// requires that we parse this stuff up first.
		
		// footer
		sb.br();
		sb.h3("Record Information");
		sb.appendTitledObject("Created",r.getCreated());

		sb.appendTitledObject("Last updated",r.getUpdated());

		sb.appendTitledObject("Status",r.getStatus());
		sb.append("</body></html>");
		return sb.toString();
	}

	//@future increase number of mappings, and add icons for each type.
	private final static String formatTypeHTML(String type) {
		if (type == null) {
			return "Type: <i>unspecified</i>";
		}
		String unprefixed = type.indexOf(":") != -1 
			? StringUtils.substringAfterLast( type,":")
					: type;
		String converted = (String)typeMapper.get(unprefixed);
		return converted == null ? "Type: " + unprefixed : "Type: " + converted;
		
	}
	
	public final static String formatType(String type) {
		if (type == null) {
			return "unspecified";
		}
		String unprefixed = type.indexOf(":") != -1 
			? StringUtils.substringAfterLast( type,":")
					: type;
		String converted = (String)typeMapper.get(unprefixed);
		return converted == null ?  unprefixed : converted;
		
	}	
	
	//@todo surely I've already got this information somewhere.
	public static final Map typeMapper = new HashMap();
	static {
		typeMapper.put("ConeSearch","Catalog service");
		typeMapper.put("SimpleImageAccess", "Image service");
		typeMapper.put("SimpleSpectrumAccess","Spectrum Service");
		typeMapper.put("CeaApplicationType","Long-running Task");//@todo find a better characterization of cea.
		typeMapper.put("CeaHttpApplicationType", "Long-running Task");
	//	typeMapper.put("
	}
	
	
	/**
	 * @param sb
	 * @param curation
	 */
	public static void formatCuration(HtmlBuilder sb, final Curation curation) {
		sb.h3("Curation");
		if (curation.getCreators().length > 0) {
			final Creator creator = curation.getCreators()[0];
			sb.appendTitledResourceName("Created by",creator.getName());
			//@FIXME find a way to scale the logo
			if (creator.getLogoURI() != null) {
				sb.append("<img align='right'  border='1' alt='").append(creator.getLogoURI()).append("' src='").append(creator.getLogoURI()).append("'>").append("<br>");
			}
			
			
		}
		sb.appendTitledResourceName("Published by",curation.getPublisher());

		if (curation.getContacts().length != 0) {
			sb.append("Contact:<br><address>");
			Contact contact = curation.getContacts()[0];
			if (contact.getName() != null) {
				sb.appendResourceName(contact.getName());
				sb.br();
			}
			sb.conditionalAppend(contact.getAddress());
			if (contact.getEmail() != null) {
				sb.append("<a href='mailto:").append(contact.getEmail()).append("'>");
				sb.append(contact.getEmail()).append("</a>");
				sb.br();
			}
			sb.conditionalAppend(contact.getTelephone());
			sb.append("</address>");
		}
		sb.appendTitledObject("Version",curation.getVersion());

		if (curation.getDates().length > 0) {
			sb.append("Date ");
			ResourceFormatter.fmtDateArray(sb,curation.getDates());
			sb.br();
		}
	}

	/**
	 * @param sb
	 * @param s
	 */
	public static void formatService(HtmlBuilder sb, Service s) {
		final Capability[] capabilities = s.getCapabilities();
		for (int i = 0; i < capabilities.length; i++) {
			Capability c = capabilities[i];
			String title;
			if (c.getStandardID() != null) {
				title = c.getStandardID().toString();
			} else if (c.getType() != null) {
				title = c.getType();
			} else {
				title = StringUtils.substringAfterLast(c.getClass().getName(),".");
			}
			sb.h3("Service Capability",title);
			//@todo validation level
			if (c.getDescription() != null) {
				sb.append("<p>").append(c.getDescription()).append("</p>");
			}
			if (c instanceof CeaServerCapability) {
				sb.appendTitledSequence("Provdes Tasks",(((CeaServerCapability)c).getManagedApplications()));
			} else
			if (c instanceof HarvestCapability) {
				sb.append("Maximum records harvested ");
				sb.append(((HarvestCapability)c).getMaxRecords());
			} else 
			if (c instanceof SearchCapability) {
				SearchCapability sc = (SearchCapability)c;
				sb.append("Extension Search Supported: ");
				sb.append(sc.getExtensionSearchSupport());
				sb.append("<br>Maximum records a query will return: ");
				sb.append(sc.getMaxRecords());
				sb.appendTitledSequence("Supports additional protocols",sc.getOptionalProtocol());
			} else 
				//temporarily out, as vizier stuff messes it up.
	/*		if (c instanceof ConeCapability) {
				ConeCapability cc = (ConeCapability)c;
				sb.append("Supports VERB parameter? ");
				sb.append(cc.isVerbosity());
				sb.append("<br>Maxumum search radius: ");
				sb.append(cc.getMaxSR());
				sb.append("<br>Maximum results a query will return: ");
				sb.append(cc.getMaxRecords());
			} else*/
			if (c instanceof SiapCapability) {
				SiapCapability cc = (SiapCapability)c;
				sb.appendTitledObject("Service Type: ",cc.getImageServiceType());
				/* FIXME temporarily removed
				sb.append("<br>Maximum query region: ");
				sb.append(cc.getMaxQueryRegionSizeRa()).append(", ").append(cc.getMaxQueryRegionSizeDec());
				sb.append("<br> Maximum results a query will return: ");
				sb.append(cc.getMaxRecords());
				sb.append("<br>Maximum file size");
				sb.append(cc.getMaxFileSize());
				sb.append("<br>Maximum image extent");
				sb.append(cc.getMaxImageExtentRa()).append(", ").append(cc.getMaxImageExtentDec());
				sb.append("<br>Maximum image size");
				sb.append(cc.getMaxImageSizeRa()).append(", ").append(cc.getMaxImageSizeDec());
			*/
			}
			sb.append("<ul>");
			for (int j = 0 ; j < c.getInterfaces().length; j++) {
				Interface iface = c.getInterfaces()[j];
				sb.append("<li>Interface");
				if (iface.getType() != null) {
					sb.append(" type - ").append(iface.getType());
				}
				if (iface.getRole() != null) {
					sb.append(" role - ").append(iface.getRole());
				}
				if (iface.getVersion() != null) {
					sb.append(" version - ").append(iface.getVersion());
				}
				sb.append("<ul>");
				for (int k = 0; k < iface.getAccessUrls().length; k++) {
					sb.append("<li>");
					AccessURL u = iface.getAccessUrls()[k];
					if (u.getUse() != null) {
						sb.append(u.getUse()).append(" - ");
					}
					final URI url = u.getValueURI();
					formatURI(sb, url);
				}
				if (iface.getSecurityMethods().length > 0) {
					sb.append("<li>Security - ");
					for (int k = 0; k < iface.getSecurityMethods().length; k++) {
						SecurityMethod sm = iface.getSecurityMethods()[k];
						sb.append(sm.getStandardID());
						sb.append("<br>");
					}
				}
				sb.append("</ul>");	
			}
			sb.append("</ul>");
			sb.appendTitledSequence("Service Rights",s.getRights());
		}
	}

	/** Write out a url/uri -if it's a well known scheme - http / ftp / ivo, add a hyperlink around it.
	 * @param sb
	 * @param url
	 */
	private static void formatURI(StrBuilder sb, final URI url) {
		if (url != null) {
		String scheme = url.getScheme();
		// fix for BZ 1970 - odd scheme types.
		if (scheme != null && (scheme.equals("http") || scheme.equals("ftp") || scheme.equals("ivo"))) {
			sb.append("<a href='").append(url).append("'>");
			sb.append(url).append("</a>");
		} else {
				sb.append(url);
		}
		}
	}

	/**
	 * @param r
	 * @param sb
	 */
	public static void formatCeaApplication(CeaApplication cea, HtmlBuilder sb) {

		ParameterBean[] params = cea.getParameters();
		InterfaceBean[] ifaces = cea.getInterfaces();
		sb.append("<h3>Interfaces</h3>");
		sb.append("<ul>");
		for (int i = 0; i < ifaces.length; i++ ) {
			InterfaceBean ib = ifaces[i];
			sb.append("<li><b>").append(ib.getName()).append("</b>");
			sb.append("<br>Inputs - ");
			fmtParameterList(sb,ib.getInputs());
			sb.append("<br>Outputs - ");
			fmtParameterList(sb,ib.getOutputs());	
		}
		sb.append("</ul>");
		sb.h3("Parameter Details");
		sb.append("<table><tr><th>Name</th><th>Type</th><th>Description</th></tr>");
		for (int i = 0; i < params.length; i++) {
			ParameterBean p = params[i];
			sb.append("<tr>");
			sb.td(p.getUiName())
				.td(p.getType())			
				.td(p.getDescription())
				.append("</tr>");
		}
		sb.append("</table>");
	}

	/**
	 * @param r
	 * @param sb
	 */
	public static void formatCatalog(Catalog c, HtmlBuilder sb) {
		sb.h3("Catalog",c.getName());
		if (c.getDescription() != null && c.getDescription().trim().length() > 0) {
			sb.append("<p>").append(c.getDescription()).append("</p>");
		}
			formatTables(c.getTables(),sb);
	
	}
	public static void formatTables(TableBean[] ts, HtmlBuilder sb) {
		sb.append("<table><tr bgcolor='#eeeeee'><th>Table name</th><th>Description</th></tr>");
		for (int i = 0; i < ts.length; i++) {
			TableBean tb = ts[i];
			sb.td(tb.getName())
				.td(tb.getDescription())				
				.append("</tr>");
			/* too costly for ukidss, takes generation time from 20ms to almost a minute.
			 * surpisingly, generation time is much more expensive than the time it taks to display it.
			for (int j = 0; j < tb.getColumns().length; j++) {
				ColumnBean cb = tb.getColumns()[j];
				sb.append("<tr>");
				sb.td("")
					.td(cb.getName())
					.td(cb.getDescription())						
					.append("</tr>");
			}*/

		}
		sb.append("</table>");	
}
	
	public static void formatCoverage(Coverage c,HtmlBuilder sb) {
		sb.h3("Coverage");
		sb.appendTitledResourceName("Footprint",c.getFootprint());
		sb.appendTitledSequence("Wavebands",c.getWavebands());

		//@todo handle Stc in some way.
		sb.conditionalAppend(c.getStcResourceProfile() != null,
				"STC Present - See XML  viewfor details");
		sb.hr();
	}
	
	/**
	 * @param format
	 * @param sb
	 */
	private static void formatFormat(Format[] format, HtmlBuilder sb) {
		if (format != null && format.length > 0) {
			sb.append("Format :");
			for (int i = 0; i < format.length; i++) {
				sb.append(format[i].getValue());
				sb.append(" ");
			}
			sb.br();
		}
	}
	
	/**
	 * @param sb
	 * @param content
	 */
	public static void formatContent(HtmlBuilder sb, final Content content) {
		if (content != null) {
		if (! (content.getDescription() == null || content.getDescription().equals("not set") || content.getDescription().equals("(no description available)")) ) {
			sb.append("<p>").append(content.getDescription()).append("</p>");
		}
		final URI referenceURI = content.getReferenceURI();
		if (referenceURI != null) {
			sb.append("<br>Further information - ");
			formatURI(sb, referenceURI);
			sb.br();
		}
		final Source source = content.getSource();
		if (source != null) {
			sb.append("Source reference - ").append(source.getValue());
			if (source.getFormat() != null && source.getFormat().trim().length() > 0) {
				sb.append(" (").append(source.getFormat()).append(")");
			}
			sb.br();
		}
		
		if (content.getRelationships().length != 0) {
			sb.append("<table><tr><th>Relationship</th><th>Resource</th></tr>");
			for (int i = 0; i < content.getRelationships().length; i++) {
				Relationship rel = content.getRelationships()[i];
				sb.append("<tr>");
				sb.td(rel.getRelationshipType());
				sb.append("<td>");
				for (int j = 0; j < rel.getRelatedResources().length; j++) {
					sb.appendResourceName(rel.getRelatedResources()[j]);
					sb.br();
				}
			}
			sb.append("</td></tr></table>");
		}
		
		sb.appendTitledSequence("Type",content.getType());
		sb.appendTitledSequence("Subject",content.getSubject());		
		sb.appendTitledSequence("Level",content.getContentLevel());
		}
	}


	public static void fmtDateArray(StrBuilder sb, Date[] arr) {
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i].getValue());
			if (i < arr.length -1) {
				sb.append(", ");
			}
		}
	}
	public static void fmtParameterList(StrBuilder sb, ParameterReferenceBean[] arr) {
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i].getRef());
			if (i < arr.length -1) {
				sb.append(", ");
			}
		}
	}	

}
