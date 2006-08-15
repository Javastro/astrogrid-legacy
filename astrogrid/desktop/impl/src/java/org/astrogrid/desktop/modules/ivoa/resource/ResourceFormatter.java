/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaServerCapability;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.Source;

/** class containing static methods to render resources to html.
 * @author Noel Winstanley
 * @since Aug 5, 20062:53:23 AM
 */
public final class ResourceFormatter {

	/** straight copy from phil's stylesheet - will edit up later */
	public static String renderResourceAsHTML(Resource r) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<body><basefont face='Arial,Helvetica,sans-serif'>");//@todo get fonts to work.
		sb.append("<h2>").append(r.getTitle()).append("</h2>");
		sb.append("<i>");
		if (r.getShortName() != null ) {
			sb.append(r.getShortName()).append(", ");
		}
		sb.append(r.getId()).append("</i><br><hr>");
		//@future add some indication of validation level.
		// content.
		final Content content = r.getContent();
		formatContent(sb, content);
		// database.
		if (r instanceof DataCollection) {
			sb.append("<hr>");		
			DataCollection d = (DataCollection)r;			
			formatCatalog(d.getCatalog(), sb);
		}
		
		// cea app
		if (r instanceof CeaApplication) {
			sb.append("<hr>");
			CeaApplication cea = (CeaApplication) r;			
			formatCeaApplication(cea, sb);
		}
		// service
		if (r instanceof Service) {
			Service s = (Service)r;
			sb.append("<hr>");
			
			formatService(sb, s);
			// (no special treatment for other service subtypes yet - as nothing additonal is being parsed.
			if (r instanceof CeaService) {
			}
		}
		// curation
		final Curation curation = r.getCuration();
		sb.append("<hr><br>");
		formatCuration(sb, curation);
		
		//@todo some description of coverage, column metadata, etc.
		// requires that we parse this stuff up first.
		
		// footer
		sb.append("</body></html>");
		return sb.toString();
	}

	/**
	 * @param sb
	 * @param curation
	 */
	public static void formatCuration(StringBuffer sb, final Curation curation) {
		if (curation.getCreators().length > 0) {
			final Creator creator = curation.getCreators()[0];
			if (creator.getName() != null) {
				sb.append("Created by ");
				fmtResourceName(sb,creator.getName());
				sb.append("<br>");
			}
			//FIXME find a way to scale the logo
			if (creator.getLogo() != null) {
				sb.append("<img align='right'  border='1' alt='").append(creator.getLogo()).append("' src='").append(creator.getLogo()).append("'>").append("<br>");
			}
			
			
		}
		if (curation.getPublisher() != null) {
			sb.append("Published by ");
			fmtResourceName(sb,curation.getPublisher());
			sb.append("<br>");
		}
		if (curation.getContacts().length != 0) {
			sb.append("Contact:<br><address>");
			Contact contact = curation.getContacts()[0];
			if (contact.getName() != null) {
				fmtResourceName(sb,contact.getName());
				sb.append("<br>");
			}
			if (contact.getAddress() != null) {
				sb.append(contact.getAddress()).append("<br>");
			}
			if (contact.getEmail() != null) {
				sb.append("<a href='mailto:").append(contact.getEmail()).append("'>");
				sb.append(contact.getEmail()).append("</a><br>");
			}
			if (contact.getTelephone() != null) {
				sb.append(contact.getTelephone()).append("<br>");				
			}
			sb.append("</address>");
		}
		if (curation.getVersion() != null) {
			sb.append("Version ").append(curation.getVersion()).append("<br>");
		}
		if (curation.getDates().length > 0) {
			sb.append("Date ");
			ResourceFormatter.fmtStringArray(sb,curation.getDates());
			sb.append("<br>");
		}
	}

	/**
	 * @param sb
	 * @param s
	 */
	public static void formatService(StringBuffer sb, Service s) {
		for (int i = 0; i < s.getCapabilities().length; i++) {
			Capability c = s.getCapabilities()[i];
			String title;
			if (c.getStandardID() != null) {
				title = c.getStandardID().toString();
			} else if (c.getType() != null) {
				title = c.getType();
			} else {
				title = StringUtils.substringAfterLast(c.getClass().getName(),".");
			}
			sb.append("<h3>Capability ").append(title).append("</h3>");
			//@ todo validation level
			if (c.getDescription() != null) {
				sb.append("<p>").append(c.getDescription()).append("</p>");
			}
			if (c instanceof CeaServerCapability) {
				sb.append("Provides tasks - ");
				fmtStringArray(sb,((CeaServerCapability)c).getManagedApplications());
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
					sb.append("<a href='").append(u.getValue()).append("'>");
					sb.append(u.getValue()).append("</a>");
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
			if (s.getRights().length > 0) {
				sb.append("<br>Service Rights - ");
				fmtStringArray(sb,s.getRights());
			}
		}
	}

	/**
	 * @param r
	 * @param sb
	 */
	public static void formatCeaApplication(CeaApplication cea, StringBuffer sb) {

		ParameterBean[] params = cea.getParameters();
		InterfaceBean[] ifaces = cea.getInterfaces();
		sb.append("<h3>Interfaces</h3>");
		sb.append("<ul>");
		for (int i = 0; i < ifaces.length; i++ ) {
			InterfaceBean ib = ifaces[i];
			sb.append("<li><b>").append(ib.getName()).append("</b>");
			sb.append("<br>Inputs - ");
			fmtStringArray(sb,ib.getInputs());
			sb.append("<br>Outputs - ");
			fmtStringArray(sb,ib.getOutputs());	
		}
		sb.append("</ul>");
		sb.append("<h3>Parameter Details</h3>");
		sb.append("<table><tr><th>Name</th><th>Type</th><th>Description</th></tr>");
		for (int i = 0; i < params.length; i++) {
			ParameterBean p = params[i];
			sb.append("<tr>");
			sb.append("<td>").append(p.getUiName()).append("</td>");
			sb.append("<td>").append(p.getType()).append("</td>");				
			sb.append("<td>").append(p.getDescription()).append("</td>");				
		}
		sb.append("</table>");
	}

	/**
	 * @param r
	 * @param sb
	 */
	public static void formatCatalog(Catalog c, StringBuffer sb) {
		sb.append("<h3>Catalog ").append(c.getName()).append("</h3>");
		if (c.getDescription() != null && c.getDescription().trim().length() > 0) {
			sb.append("<p>").append(c.getDescription()).append("</p>");
		}
		sb.append("<table><tr><th>Table name</th><th>Column name</th><th>Description</th></tr>");
		for (int i = 0; i < c.getTables().length; i++) {
			TableBean tb = c.getTables()[i];
			sb.append("<tr bgcolor='#eeeeee'>");
			sb.append("<td>").append(tb.getName()).append("</td>");
			sb.append("<td></td>");
			sb.append("<td>").append(tb.getDescription()).append("</td>");				
			sb.append("</tr>");
			for (int j = 0; j < tb.getColumns().length; j++) {
				ColumnBean cb = tb.getColumns()[j];
				sb.append("<tr>");
				sb.append("<td></td>");
				sb.append("<td>").append(cb.getName()).append("</td>");
				sb.append("<td>").append(cb.getDescription()).append("</td>");						
				sb.append("</tr>");
			}

		}
		sb.append("</table>");
	}

	/**
	 * @param sb
	 * @param content
	 */
	public static void formatContent(StringBuffer sb, final Content content) {
		if (content != null) {
		if (! (content.getDescription() == null || content.getDescription().equals("not set") || content.getDescription().equals("(no description available)")) ) {
			sb.append("<p>").append(content.getDescription()).append("</p>");
		}
		if (content.getReferenceURL() != null) {
			sb.append("<br>Further information - ");
			sb.append("<a href='").append(content.getReferenceURL()).append("'>");
			sb.append(content.getReferenceURL()).append("</a><br>");
		}
		final Source source = content.getSource();
		if (source != null) {
			sb.append("Source reference - ").append(source.getValue());
			if (source.getFormat() != null && source.getFormat().trim().length() > 0) {
				sb.append(" (").append(source.getFormat()).append(")");
			}
			sb.append("<br>");
		}
		
		if (content.getRelationships().length != 0) {
			sb.append("<table><tr><th>Relationship</th><th>Resource</th></tr>");
			for (int i = 0; i < content.getRelationships().length; i++) {
				Relationship rel = content.getRelationships()[i];
				sb.append("<tr>");
				sb.append("<td>").append(rel.getRelationshipType()).append("</td>");
				sb.append("<td>");
				for (int j = 0; j < rel.getRelatedResources().length; j++) {
					fmtResourceName(sb,rel.getRelatedResources()[j]);
					sb.append("<br>");
				}
			}
			sb.append("</td></tr></table>");
		}
		
		if (content.getType().length > 0) {
			sb.append("Type - ");
			ResourceFormatter.fmtStringArray(sb,content.getType());
			sb.append("<br>");
		}
		if (content.getSubject().length > 0) {
			sb.append("Subject - ");
			ResourceFormatter.fmtStringArray(sb,content.getSubject());
			sb.append("<br>");
		}
		if (content.getContentLevel().length > 0) {
			sb.append("Level - ");
			ResourceFormatter.fmtStringArray(sb,content.getContentLevel());
			sb.append("<br>");
		}
		}
	}

	/**
	 * @param sb
	 * @param name
	 */
	private static void fmtResourceName(StringBuffer sb, ResourceName name) {
		if (name.getId() != null) {
			sb.append("<a href='").append(name.getId()).append("'>");
			String v = name.getValue();
			sb.append(v == null || v.trim().length() == 0 ? name.getId().toString() : v);
			sb.append("</a>");
		} else {
			sb.append(name.getValue());
		}
	}

	public static void fmtStringArray(StringBuffer sb, Object[] dates) {
		for (int i = 0; i < dates.length; i++) {
			sb.append(dates[i]);
			if (i < dates.length -1) {
				sb.append(", ");
			}
		}
	}

	public static void fmtStringArray(StringBuffer sb, Date[] dates) {
		for (int i = 0; i < dates.length; i++) {
			sb.append(dates[i].getValue());
			if (i < dates.length -1) {
				sb.append(", ");
			}
		}
	}
	public static void fmtStringArray(StringBuffer sb, ParameterReferenceBean[] dates) {
		for (int i = 0; i < dates.length; i++) {
			sb.append(dates[i].getRef());
			if (i < dates.length -1) {
				sb.append(", ");
			}
		}
	}	

}
