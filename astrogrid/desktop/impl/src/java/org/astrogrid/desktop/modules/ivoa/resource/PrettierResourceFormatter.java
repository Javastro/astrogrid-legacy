/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaServerCapability;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Authority;
import org.astrogrid.acr.ivoa.resource.Capability;
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
import org.astrogrid.acr.ivoa.resource.InputParam;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Organisation;
import org.astrogrid.acr.ivoa.resource.ParamHttpInterface;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SearchCapability;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SimpleDataType;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.acr.ivoa.resource.StcResourceProfile;
import org.astrogrid.acr.ivoa.resource.Validation;
import org.astrogrid.acr.ivoa.resource.WebServiceInterface;
import org.astrogrid.acr.ivoa.resource.ConeCapability.Query;
import org.astrogrid.acr.ivoa.resource.SiapCapability.ImageSize;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkySize;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactoryImpl;
import org.astrogrid.desktop.modules.ui.voexplorer.google.SystemFilter;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** class containing static methods to render resources to html.
 * Improvments and tweaks based on resourceFormatter - in a separate class so it's easy to switch back
 * it users demand.
 * 
 * relies on trimming to null being done at the parse stage.
 * 
 * @author Noel Winstanley
 * @since Aug 5, 20062:53:23 AM
 */
public final class PrettierResourceFormatter {
	
        
	public static String renderResourceAsHTML(Resource r) {
		HtmlBuilder sb = new HtmlBuilder();
// html header
		sb.append("<head><style>");
		sb.append("  body { font-family: Arial, Helvetica, sans-serif }");
		sb.append("  cite.label {font-size: 8px; color: #666633; font-style: normal}");
		sb.append("  a.res { color: #2584a7 }");
		sb.append(" img.logo {border-style: inset }");
		sb.append("</style></head><body>");
		
// core resource data
		sb.h2(r.getTitle());
		
		sb.appendTitledObjectNoBR("Short Name",r.getShortName());
		sb.appendTitledObjectNoBR("ID",r.getId());		
		sb.br();
		
		sb.appendTitledObjectNoBR("Type",formatType(r.getType()));
	    sb.appendTitledObjectNoBR("Created",r.getCreated());
	    sb.appendTitledObjectNoBR("Updated",r.getUpdated());	   
	    //    sb.appendTitledObject("Status",r.getStatus()); always active.
		
		final Validation[] validationLevel = r.getValidationLevel();
        formatValidation(sb, validationLevel);
		sb.hr();
		
        
		// content
		        final Content content = r.getContent();
		        formatContent(sb, content);     

		     // type-specific that apply to more than one type.
		        // coverage
		        if (r instanceof HasCoverage) {
		            formatCoverage(sb, ((HasCoverage)r).getCoverage());
		        }                           
		        if (r instanceof DataService) {   
		            DataService ds = (DataService)r;
		            sb.appendTitledResourceNames("Facilities",ds.getFacilities())
		            .appendTitledResourceNames("Instruments",ds.getInstruments());                   
		        }        
		        // table metadata
		        if (CapabilityIconFactoryImpl.hasTabularMetadata(r)) {
		            sb.append("<object classid='")
		                .append(ShowMetadataButton.class.getName())
		                .append("'>");
		            sb.br();
		        }
		        
// type-specific headlines
		if (r instanceof Authority) {
		    sb.append("<img src='classpath:/org/astrogrid/desktop/icons/authority16.png'>&nbsp;This resource describes an <b>Authority</b>");
		    sb.br();
		    sb.appendTitledResourceName("Managed&nbsp;Organization",((Authority)r).getManagingOrg());
		}
        
        if (r instanceof Organisation) {
            Organisation o = (Organisation)r;
            sb.append("<img src='classpath:/org/astrogrid/desktop/icons/organization16.png'>&nbsp;This resource describes an <b>Organization</b>");
            sb.br();
            sb.appendTitledResourceNames("Managed&nbsp;Facilities",o.getFacilities());
            sb.appendTitledResourceNames("Managed&nbsp;Instruments",o.getInstruments());            
        }		

		if (r instanceof DataCollection) {
			DataCollection d = (DataCollection)r;			
			//@todo should I have an icon for data collection?
			sb.append("This resource describes a <b>Data&nbsp;Collection</b>");
			sb.br();
			sb.appendTitledResourceNames("Facilities",d.getFacilities())	;	
			sb.appendTitledResourceNames("Instruments",d.getInstruments());

			final Format[] format = d.getFormats();
	        if (format != null && format.length > 0) {
	            sb.appendLabel("Format");
	            for (int i = 0; i < format.length; i++) {
	                if (i > 0) {
	                    sb.append(", ");
	                }
	                sb.append(format[i].getValue());
	            }
	            sb.br();
	        }	
						
			sb.appendTitledSequence("Rights",d.getRights());
			AccessURL accessURL = d.getAccessURL();
			if (accessURL != null) {
			    sb.appendLabel("Access&nbsp;URL");
			    formatAccessURL(sb, accessURL);
			    sb.br();
			}
		}

        if (r instanceof Service) {
            Service s = (Service)r;
            if (r instanceof RegistryService) {
                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/server16.png'>&nbsp;This resource describes a <b>Registry&nbsp;Service</b>");
                sb.br();
                RegistryService rs = (RegistryService)r;
                sb.appendLabel("Full&nbsp;Registry?");
                sb.append(rs.isFull() ? "true" : "false");
                sb.br();
                //@todo hyperlink these managed auths.
                sb.appendTitledSequence("Managed&nbsp;Authorities",rs.getManagedAuthorities());
            }
            // cone service, siap service - nothing additional - covered in the formatService call
            formatServiceCapabilities(sb, s);
        }
        

// append cea at the end.
        if (r instanceof CeaApplication) {
            CeaApplication cea = (CeaApplication) r;
            if (BuildQueryActivity.hasAdqlParameter(cea)) {
                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/db16.png'>&nbsp;This resource describes a <b>Catalog&nbsp;Query&nbsp;Service&nbsp;(ADQL)</b>");
            } else {
                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/exec16.png'>&nbsp;This resource describes a <b>Remote&nbsp;Application&nbsp;(CEA)</b>");
            }
            sb.br();
                
            InterfaceBean[] ifaces =cea.getInterfaces();
            sb.appendLabel("Interfaces");
            for(int i = 0; i < ifaces.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(ifaces[i].getName());
            }
            sb.br();
        }
   
        
// curation
		final Curation curation = r.getCuration();
	
		if (curation != null) {
			formatCuration(sb, curation);
		}
		
		// footer
		sb.append("</body></html>");
		return sb.toString();
	}




    /**
     * @param sb
     * @param coverage
     */
    private static void formatCoverage(HtmlBuilder sb, final Coverage coverage) {
        if (coverage != null) {
            sb.appendTitledResourceName("Footprint&nbsp;Service",coverage.getFootprint());
            sb.appendTitledSequence("Waveband&nbsp;Coverage",coverage.getWavebands());

            StcResourceProfile stc = coverage.getStcResourceProfile();
            if (stc != null) {
                sb.appendLabel("Spatial&nbsp;Coverage");
                if (stc.isAllSky()) {
                    sb.append("All-Sky");
                } else {
                    sb.append("Partial Sky");
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DomHelper.PrettyDocumentToStream(stc.getStcDocument(),bos);
                sb.br();
                sb.append("<object classid='")
                .append(ShowCoverageButton.class.getName())
                .append("'>")
                .append("<param name='doc' value='")
                .append(bos.toString())
                .append("' >")
                .append("</object>")
                ;
                sb.br();
            }
         }
    }




    /**
     * @param sb
     * @param accessURL
     */
    private static void formatAccessURL(HtmlBuilder sb, AccessURL accessURL) {
          //  sb.appendURI(accessURL.getUse() != null ? accessURL.getUse() : "link" ,accessURL.getValueURI());
        //bz 2520 - make accessURls accessible.
        sb.appendURI(accessURL.getValueURI().toString(),accessURL.getValueURI());
    }




    /**
     * @param sb
     * @param validationLevel
     */
    private static void formatValidation(HtmlBuilder sb,
            final Validation[] validationLevel) {
        if (validationLevel != null && validationLevel.length > 0) {
		    sb.appendLabel("Validated");
		    for(int i = 0 ; i < validationLevel.length; i++) {
		        if (i > 0) {
		            sb.append(", ");
		        }
		        sb.append(validationLevel[i].getValidationLevel());
		        if (validationLevel[i].getValidatedBy() != null) {
		            sb.append("&nbsp;by&nbsp;");
		            sb.appendURI(validationLevel[i].getValidatedBy());
		        }
		    }
		    sb.br();
		}
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
	
	
	public static final Map typeMapper = new HashMap();
	static {
		typeMapper.put("ConeSearch","Catalog cone search service");
		typeMapper.put("SimpleImageAccess", "Image access service (SIAP)");
		typeMapper.put("SimpleSpectrumAccess","Spectrum access service (SSAP)");
		typeMapper.put("CeaApplicationType","Remote application (CEA)");
		typeMapper.put("CeaHttpApplicationType", "Remote application (CEA)");
		typeMapper.put("SimpleTimeAccess","Time range access service (STAP)");
	//	typeMapper.put("
	}
	
	
	/**
	 * @param sb
	 * @param curation
	 */
	private static void formatCuration(HtmlBuilder sb, final Curation curation) {
	    sb.hr();
        
        sb.appendTitledObjectNoBR("Version",curation.getVersion());

        if (curation.getDates().length > 0) {
            sb.appendLabel("Dates");
            Date[] arr = curation.getDates();
                for (int i = 0; i < arr.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    if (arr[i].getRole() != null) {
                        sb.append(arr[i].getRole());
                        sb.append("&nbsp;:&nbsp;");
                    }
                    sb.append(arr[i].getValue());
                }
                sb.br();
            }                   
        
		Creator[] cres = curation.getCreators();
		if (cres.length > 0) {
		    sb.appendLabel("Creator");
		    for (int i =0; i < cres.length; i++) {
		        if (i > 0) {
		            sb.append(", ");
		        }		        
		        sb.appendResourceName(cres[i].getName());
		        
		        final URI logoURI = cres[i].getLogoURI();
                if (logoURI != null) {
                    sb.br();
		            sb.append("<img class='logo' alt='creator logo' src='").append(logoURI).append("'>");
		            sb.br();
		        }
		       
		    }
		    sb.br();
		}
		
		sb.appendTitledResourceName("Publisher",curation.getPublisher());

		sb.appendTitledResourceNames("Contributors",curation.getContributors());
		
		
		final Contact[] contacts = curation.getContacts();
		if (contacts.length != 0) {
		    sb.appendLabel("<img src='classpath:/org/astrogrid/desktop/icons/person16.png'>&nbsp;Contact");
		    for (int i = 0; i < contacts.length; i++) {
		        if (i > 0) {
		            sb.br();
		        }			
		        Contact contact = contacts[i];
		        if (contact.getName() != null) {
		            sb.appendResourceName(contact.getName());
		            sb.append(", ");
		        }
		        if (contact.getEmail() != null) {
		            sb.append("<a href='mailto:").append(contact.getEmail()).append("'>");
		            sb.append(contact.getEmail()).append("</a>");
		            sb.append(", ");
		        }
		        if (contact.getTelephone() != null) {
		            sb.append(contact.getTelephone()).append(", ");
		        }
		        if (contact.getAddress() != null) {
		            sb.br();
		            sb.append(contact.getAddress());
		        }
		    }		   
		}

	}

	private static void formatServiceCapabilities(HtmlBuilder sb, Service s) {
	    sb.appendTitledSequence("Service&nbsp;Rights",s.getRights());
	    final Capability[] capabilities = s.getCapabilities();
	    for (int capabilitiesIndex = 0; capabilitiesIndex < capabilities.length; capabilitiesIndex++) {
	        if (capabilitiesIndex > 0) {
	            sb.p();
	        }
	        Capability c = capabilities[capabilitiesIndex];
	        // display capability-specific info.
	        if (c instanceof CeaServerCapability) {
	            sb.append("<img src='classpath:/org/astrogrid/desktop/icons/server16.png'>&nbsp;This resource describes a <b>Remote&nbsp;Application&nbsp;(CEA)&nbsp;Service</b>");
	            sb.br();
	            sb.appendTitledURIs("Provided&nbsp;Tasks",(((CeaServerCapability)c).getManagedApplications()));
	        } else
	            if (c instanceof HarvestCapability) {
	                sb.append("<b>Harvest&nbsp;Capability</b>");
	                sb.br();
	                sb.appendLabel("Maximum&nbsp;Records&nbsp;Harvested");
	                sb.append(((HarvestCapability)c).getMaxRecords());
	                sb.br();
	            } else 
	                if (c instanceof SearchCapability) {
	                    SearchCapability sc = (SearchCapability)c;
	                    sb.append("<b>Search&nbsp;Capability</b>");
	                    sb.br();
	                    if (sc.getMaxRecords() > 0) {
	                        sb.appendLabel("Maximum&nbsp;Records&nbsp;Returned");
	                        sb.append(sc.getMaxRecords());
	                        sb.append("&nbsp; ");
	                    }
	                    sb.appendLabel("Extension&nbsp;Search&nbsp;Support");
	                    sb.append(sc.getExtensionSearchSupport() );
	                    sb.append("&nbsp; ");
	                    sb.appendTitledSequenceNoBR("Additional&nbsp;Protocols",sc.getOptionalProtocol());
	                    sb.br();
	                } else  
	                    if (c instanceof ConeCapability) {
	                        ConeCapability cc = (ConeCapability)c;
	                        sb.append("<img src='classpath:/org/astrogrid/desktop/icons/cone16.png'>&nbsp;This resource describes a <b>Catalog&nbsp;Cone&nbsp;Search&nbsp;Service</b>");
	                        sb.br();
	                        sb.appendLabel("Verbose&nbsp;Parameter&nbsp;Supported?");
	                        sb.append(cc.isVerbosity() ? "true" : "false");
	                        sb.append("&nbsp; ");
	                        if (cc.getMaxSR() > 0.0) {
	                            sb.appendLabel("Maxumum&nbsp;Search&nbsp;Radius");
	                            sb.append(cc.getMaxSR());
	                            sb.append("&nbsp; ");
	                        }
	                        if (cc.getMaxRecords() > 0) {
	                            sb.appendLabel("Maximum&nbsp;Results&nbsp;Returned");
	                            sb.append(cc.getMaxRecords());
	                            sb.br();
	                        }
	                        //@todo output test queries
	                    } else if (c instanceof SiapCapability) {
	                        SiapCapability cc = (SiapCapability)c;
	                        sb.append("<img src='classpath:/org/astrogrid/desktop/icons/siap16.png'>&nbsp;This resource describes a <b>Image&nbsp;Access&nbsp;Service&nbsp;(SIAP)</b>");
	                        sb.br();
	                        sb.appendTitledObjectNoBR("Service&nbsp;Type",cc.getImageServiceType());
	                        if (cc.getMaxFileSize() > 0) {
	                            sb.appendLabel("Maximum&nbsp;File&nbsp;size");
	                            sb.append(cc.getMaxFileSize());
	                            sb.append("&nbsp; ");
	                        }
	                        if (cc.getMaxRecords() > 0) {
	                            sb.appendLabel("Maximum&nbsp;Results&nbsp;Returned");
	                            sb.append(cc.getMaxRecords());
                                sb.append("&nbsp; ");	                            
	                        }	   
	                        sb.br();
	                        SkySize sz = cc.getMaxImageExtent();
	                        if (sz != null) {
	                            sb.appendLabel("Maximum&nbsp;Image&nbsp;Extent");
	                            sb.append(sz.getLat()).append(",&nbsp;").append(sz.getLong());
	                            sb.append("&nbsp; ");
	                        }
	                        ImageSize isz = cc.getMaxImageSize();
	                        if (isz != null) {
	                            sb.appendLabel("Maximum&nbsp;Image&nbsp;Size");
	                            sb.append(isz.getLat()).append(",&nbsp;").append(isz.getLong());
	                            sb.append("&nbsp; ");
	                        }
	                        sz = cc.getMaxQueryRegionSize();
	                        if (sz != null) {
	                            sb.appendLabel("Maximum&nbsp;Query&nbsp;Size");
	                            sb.append(sz.getLat()).append(",").append(sz.getLong());
	                            sb.append("&nbsp; ");
	                        }
                            //@todo output test queries	                        
	                        sb.br();
	                    } else if (c instanceof StapCapability) {
	                        StapCapability sc = (StapCapability)c;
	                        sb.append("<img src='classpath:/org/astrogrid/desktop/icons/latest16.png'>&nbsp;This resource describes a <b>Time&nbsp;Series&nbsp;Access&nbsp;Service&nbsp(STAP)</b>");
	                        sb.br();
	                        if (sc.getMaxRecords() > 0) {
	                            sb.appendLabel("Max&nbsp;Records");
	                            sb.append(sc.getMaxRecords());
	                            sb.append("&nbsp; ");
	                        }
	                        sb.appendLabel("Positioning&nbsp;Supported");
	                        sb.append(sc.isSupportPositioning());
	                        sb.append("&nbsp; ");
	                        sb.appendTitledSequence("Supported&nbsp;Formats",sc.getSupportedFormats());
	                        //@todo test query.
	                    } else if (c instanceof SsapCapability) {
	                        SsapCapability sc = (SsapCapability)c;
	                        sb.append("<img src='classpath:/org/astrogrid/desktop/icons/ssap16.png'>&nbsp;This resource describes a <b>Spectrum&nbsp;Access&nbsp;Service&nbsp;(SSAP)</b>");
	                        sb.br();
	                        sb.appendTitledObjectNoBR("Compliance",sc.getComplianceLevel());
	                        sb.appendTitledSequenceNoBR("Creation&nbsp;Types",sc.getCreationTypes());
	                        sb.appendTitledSequenceNoBR("Data&nbsp;Sources",sc.getDataSources());
	                        sb.br();
	                        if (sc.getMaxRecords() > 0) {
	                            sb.appendLabel("Max&nbsp;Records");
	                            sb.append(sc.getMaxRecords());
	                            sb.append("&nbsp; ");
	                        }
	                        if (sc.getDefaultMaxRecords() > 0) {
	                            sb.appendLabel("Default&nbsp;Max&nbsp;Records");
	                            sb.append(sc.getDefaultMaxRecords());
	                            sb.append("&nbsp; ");
	                        }
	                        if (sc.getMaxFileSize() > 0) {
	                            sb.appendLabel("Max&nbsp;Filesize");
	                            sb.append(sc.getMaxFileSize());
                                sb.append("&nbsp; ");
	                        }
                            sb.br();
                            if (sc.getMaxAperture() > 0.0) {
                                sb.appendLabel("Max&nbsp;Aperture");
                                sb.append(sc.getMaxAperture());
                                sb.append("&nbsp; ");
                            }
                            if (sc.getMaxSearchRadius() > 0.0) {
                                sb.appendLabel("Max&nbsp;Search&nbsp;Radius");
                                sb.append(sc.getMaxSearchRadius());
                            }
                            sb.br();                            
                            sb.appendTitledURIs("Supported&nbsp);Frames",sc.getSupportedFrames());
                            sb.appendTitledSequence("Supports",sc.getSupports());
                            sb.append("&nbsp; ");                            
	                    }	else { // take a guess.
	                        String capType = c.getType();
	                        String capTypeUnprefixed =capType != null &&  capType.indexOf(":") != -1 
	                        ? StringUtils.substringAfterLast( capType,":")
	                                : capType;
	                        //@todo put this back in.
	                      //  if (capabilities.length == 1 && SystemFilter.isBoringServiceTitle(s) || SystemFilter.isBoringRelationship(s)) {
	                        //    sb.append("<img src='classpath:/org/astrogrid/desktop/icons/service16.png'>&nbsp;This resource describes a <b>Technical&nbsp;System&nbsp;Service</b>");
	                        //} else 
	                            if ("WebBrowser".equals(capTypeUnprefixed)) {
	                            sb.append("<img src='classpath:/org/astrogrid/desktop/icons/browser16.png'>&nbsp;This resource describes a <b>Web&nbsp;Interface</b>");
	                            sb.br();
	                        } else if (ConeProtocol.isConeSearchableCdsCatalog(s)){ // detects vizier interfaces that aren't web-browser
	                            sb.append("<img src='classpath:/org/astrogrid/desktop/icons/cone16.png'>&nbsp;This resource describes a <b>Catalog&nbsp;Cone&nbsp;Search&nbsp;Service</b>");
	                            sb.br();
	                        } else {
	                            sb.append("This resource descibes an <b>Service</b>");
	                            sb.br();   
	                            sb.appendTitledObject("Type",capTypeUnprefixed);
	                            sb.appendTitledObject("StandardID",c.getStandardID());
	                        }
	                       
	                    }
	        // validation, and description.
	        formatValidation(sb,c.getValidationLevel());
	        if (StringUtils.isNotEmpty(c.getDescription())) {
	            sb.append(c.getDescription());
	            sb.br();
	        }

	        // examine the interfaces..
	        for (int j = 0 ; j < c.getInterfaces().length; j++) {	            
	            Interface iface = c.getInterfaces()[j];
	            	sb.appendTitledObjectNoBR("Interface Type",formatInterfaceType(iface));
	            sb.appendTitledObjectNoBR("Role",iface.getRole());
	            sb.appendTitledObjectNoBR("Version",iface.getVersion());
	            final SecurityMethod[] securityMethods = iface.getSecurityMethods();
	            if (securityMethods.length > 0) {
	                sb.appendLabel("Security");
	                for (int k = 0; k < securityMethods.length; k++) {
	                    if (k > 0) {
	                        sb.append(", ");
	                    }
	                    SecurityMethod sm = securityMethods[k];
	                    sb.append(sm.getStandardID());
	                }
	            }
	            final AccessURL[] accessUrls = iface.getAccessUrls();
	            if (accessUrls != null) {
	                if (accessUrls.length == 1) {
	                    sb.appendLabel("Access&nbsp;URL");
	                } else if (accessUrls.length > 1) {
                        sb.appendLabel("Access&nbsp;URLs");	                    
	                }
	                for (int k = 0; k < accessUrls.length; k++) {
	                    if (k > 0) {
	                        sb.append(", ");
	                    }
	                    formatAccessURL(sb,accessUrls[k]);					
	                }
	            }
	            if (iface instanceof WebServiceInterface) {
	                WebServiceInterface wInterface = (WebServiceInterface)iface;
	                final URI[] wsdlURLs = wInterface.getWsdlURLs();
                    if (wsdlURLs.length > 0) {
                        sb.br();
                        sb.appendLabel("WSDL");
                        for (int w = 0; w < wsdlURLs.length; w++) {
                            if (w > 0) {
                                sb.append(", ");
                            }
                            sb.appendURI(wsdlURLs[w]);
                        }
                    } 
	            }  else if (iface instanceof ParamHttpInterface) {
	                ParamHttpInterface phi = (ParamHttpInterface)iface;
	                sb.br();
	                sb.appendTitledObjectNoBR("Query&nbsp;Type",phi.getQueryType());
	                sb.appendTitledObject("Result&nbsp;Type",phi.getResultType());
	                InputParam[] params = phi.getParams();
	                if (params != null && params.length > 0) {
	                    sb.append("<table><tr><th>Parameter</th><th>Description</th><th>Type</th><th>Use</th></tr>");
	                    for (int i = 0; i < params.length; i++) {
	                        InputParam inputParam = params[i];
	                        sb.append("<tr><td>");
	                        sb.append(inputParam.getName());
	                        sb.append("</td><td>");
	                        sb.append(inputParam.getDescription());
	                        sb.append("</td><td>");
	                        if (inputParam.getUcd() != null) {
	                            sb.append(inputParam.getUcd());
	                            sb.append("&nbsp; ");
	                        }
	                        if (inputParam.getUnit() != null) {
                                sb.append(inputParam.getUnit());
                                sb.append("&nbsp; ");	                            
	                        }
	                        if (inputParam.getDataType() != null) {
	                            SimpleDataType dataType = inputParam.getDataType();
	                            sb.append(dataType.getType());
	                            if (dataType.getArraysize() != null && !  dataType.getArraysize().equals("1")){
	                                sb.append("&nbsp; ");
	                                sb.append(dataType.getArraysize());
	                            }
	                        }
	                        sb.append("</td><td>");
	                        if (inputParam.isStandard()) {
	                            sb.append("standard");
	                            sb.append("&nbsp; ");
	                        }
	                        if (inputParam.getUse() != null) {
	                            sb.append(inputParam.getUse());
	                        }
	                        sb.append("</td></tr>");
	                    }
	                    sb.append("</table>");
	                }

	            }
	            sb.br();
	        }			

	    }
	}

	
	/** create a readable description for a type of 
     * @param iface
     * @return
     */
    private static String formatInterfaceType(Interface iface) {
        if (iface instanceof WebServiceInterface) {
            return "Web Service";
        } else if (iface instanceof ParamHttpInterface) {
            return "Http Query";
        }
        
        String type = iface.getType();
        if (StringUtils.contains(type,"WebBrowser"))  {
               return "Web Form";
        } else if (StringUtils.contains(type,"OAIHTTP")) {
            return "OAI HTTP";
        } else if (StringUtils.contains(type,"OAISOAP")) {
            return "OAI SOAP";
        } else if (StringUtils.contains(type,":")) {
            return StringUtils.substringAfter(type,":");
        } else {
            return type;
        }
            
    }




    /** complete.
	 * @param sb
	 * @param content
	 */
	private static void formatContent(HtmlBuilder sb, final Content content) {
		if (content != null) {
		    sb.br();
	        sb.appendTitledSequenceNoBR("Content&nbsp;Type",content.getType());
	        sb.appendTitledSequenceNoBR("Subject",content.getSubject());      
	        sb.appendTitledSequenceNoBR("Level",content.getContentLevel());
	        
		final String description = content.getDescription();
        if (! (description == null || description.equals("not set") || description.equals("(no description available)")) ) {
            sb.br();
			sb.append(description);
			if (! description.endsWith(".")) {
			    sb.append(".");
			}
		}
		final URI referenceURI = content.getReferenceURI();
		if (referenceURI != null) {
		    sb.append("  ");
			sb.appendURI("Further&nbsp;Information...",referenceURI);		
		}
		sb.p();
		final Source source = content.getSource();
		if (source != null && source.getValue() != null) {
		    sb.appendLabel("Source&nbsp;Reference");
		    if ("bibcode".equalsIgnoreCase(source.getFormat())) {
		        try {
                    sb.appendURI(source.getValue(),new URI(BIBCODE_URL + source.getValue()));
                } catch (URISyntaxException x) {
                    sb.append(source.getValue());
                }
		    } else {
		        sb.append(source.getValue());
		    }
			if (source.getFormat() != null) {
				sb.append("&nbsp;(").append(source.getFormat()).append(")");
			}
			sb.br();
		}
		
		final Relationship[] rels = content.getRelationships();
		if (rels.length != 0) {
		    sb.appendLabel("Relationships");
		    for (int i = 0; i < rels.length; i++) {
		        if (i > 0) {
		            sb.append("; ");
		        }
		        Relationship rel = rels[i];
		        sb.append(rel.getRelationshipType());
		        sb.append("&nbsp; ");
		        for (int j = 0; j < rel.getRelatedResources().length; j++) {
		            if (j > 0) {
		                sb.append(", ");
		            }
		            sb.appendResourceName(rel.getRelatedResources()[j]);					
		        }
		    }
		    sb.br();
		}				
		}
	}

	/** url that bibcodes can be retreived from */
	private static final String BIBCODE_URL = "http://adsabs.harvard.edu/cgi-bin/nph-bib_query?bibcode=";


}
