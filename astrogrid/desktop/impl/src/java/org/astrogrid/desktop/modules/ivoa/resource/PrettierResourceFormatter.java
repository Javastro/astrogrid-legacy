/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.SearchCapability;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SimpleDataType;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.acr.ivoa.resource.StcResourceProfile;
import org.astrogrid.acr.ivoa.resource.TapCapability;
import org.astrogrid.acr.ivoa.resource.Validation;
import org.astrogrid.acr.ivoa.resource.VospaceCapability;
import org.astrogrid.acr.ivoa.resource.WebServiceInterface;
import org.astrogrid.acr.ivoa.resource.SiapCapability.ImageSize;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkySize;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactoryImpl;
import org.astrogrid.desktop.modules.ui.voexplorer.google.SystemFilter;

/** Renders registry resource information as HTML.
 * 
 * Improvments and tweaks based on resourceFormatter - in a separate class so it's easy to switch back
 * it users demand.
 * 
 * relies on trimming to null being done at the parse stage.
 * 
 * @author Noel Winstanley
 * @since Aug 5, 20062:53:23 AM
 */
public final class PrettierResourceFormatter {
	
        
	public static String renderResourceAsHTML(final Resource r) {
		final HtmlBuilder sb = new HtmlBuilder();
// html header
		sb.append("<head><style>");
		sb.append("  body { font-family: Arial, Helvetica, sans-serif }");
		sb.append("  cite.label {font-size: 8px; color: #666633; font-style: normal}");
		sb.append("  a.res { color: #2584a7 }");
        sb.append("  a.vos { color: #2584a7 }"); // use same color for all 'internal' links		
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
		            final DataService ds = (DataService)r;
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
            final Organisation o = (Organisation)r;
            sb.append("<img src='classpath:/org/astrogrid/desktop/icons/organization16.png'>&nbsp;This resource describes an <b>Organization</b>");
            sb.br();
            sb.appendTitledResourceNames("Managed&nbsp;Facilities",o.getFacilities());
            sb.appendTitledResourceNames("Managed&nbsp;Instruments",o.getInstruments());            
        }		

		if (r instanceof DataCollection) {
			final DataCollection d = (DataCollection)r;			
			sb.append("<img src='classpath:/org/astrogrid/desktop/icons/datacollection16.png'>&nbsp;This resource describes a <b>Data&nbsp;Collection</b>");
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
			final AccessURL accessURL = d.getAccessURL();
			if (accessURL != null) {
			    sb.appendLabel("Access&nbsp;URL");
			    formatAccessURL(sb, accessURL);
			    sb.br();
			}
		}

        if (r instanceof Service) {
            final Service s = (Service)r;
            if (r instanceof RegistryService) {
                sb.append("This resource describes a <b>Registry&nbsp;Service</b>");
                sb.br();
                final RegistryService rs = (RegistryService)r;
                sb.appendLabel("Registry&nbsp;Type");
                sb.append(rs.isFull() ? "Full" : "Partial");
                sb.br();
                //@todo hyperlink these managed auths.
                sb.appendTitledSequence("Managed&nbsp;Authorities",rs.getManagedAuthorities());
            }
            // cone service, siap service - nothing additional - covered in the formatService call
            sb.br();
            formatServiceCapabilities(sb, s);
        }
        

// append cea at the end.
        if (r instanceof CeaApplication) {
            final CeaApplication cea = (CeaApplication) r;
            if (BuildQueryActivity.hasAdqlParameter(cea)) {
                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/db16.png'>&nbsp;This resource describes a <b>Catalog&nbsp;Query&nbsp;Service&nbsp;(ADQL)</b>");
            } else {
                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/exec16.png'>&nbsp;This resource describes a <b>Remote&nbsp;Application&nbsp;(CEA)</b>");
            }
            sb.br();
                
            final InterfaceBean[] ifaces =cea.getInterfaces();
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
    private static void formatCoverage(final HtmlBuilder sb, final Coverage coverage) {
        if (coverage != null) {
            appendServiceReference(sb,"Footprint&nbsp;Service",coverage.getFootprint());
            sb.appendTitledSequence("Waveband&nbsp;Coverage",coverage.getWavebands());

            final StcResourceProfile stc = coverage.getStcResourceProfile();
            if (stc != null) {
                sb.appendLabel("Spatial&nbsp;Coverage");
                if (stc.isAllSky()) {
                    sb.append("All-Sky");
                } else {
                    sb.append("Partial Sky");
                }
                    sb.br();
                sb.append("<object classid='")
                .append(ShowCoverageButton.class.getName())
                .append("'>")
                .append("</object>")
                ;
                sb.p();
            }
         }
    }
    
    public static void appendServiceReference(final HtmlBuilder sb,final String title,final ResourceName name) {
        if (name != null) {
            sb.appendLabel(title);
            // see if the value is a valid URL.
            URL url;
            try {
                url = new URL(name.getValue());
            } catch (final MalformedURLException e){
                url = null;
            }
            // now different cases depending on what we've got.
            if (url != null) {
                try {
                    sb.appendURI(url.toURI());
                } catch (final URISyntaxException x) {
                    // unlikely.
                    sb.append(url.toString());
                }
                if(name.getId() != null) { // tack it on the end.
                    sb.append(" (");
                    sb.appendURI("resource",name.getId());
                    sb.append(")");
                } 
            } else { // not a valid url. treat it as a name.
                if (name.getId() != null) {
                    sb.append("<a class='res' href='").append(name.getId()).append("'>");
                    final String v = name.getValue();
                    sb.append(v == null  ? name.getId().toString() : v);
                    sb.append("</a>");
                } else {
                    sb.append(name.getValue());
                }
            }
            sb.br();
        }

    }




    /**
     * @param sb
     * @param accessURL
     */
    private static void formatAccessURL(final HtmlBuilder sb, final AccessURL accessURL) {
          //  sb.appendURI(accessURL.getUse() != null ? accessURL.getUse() : "link" ,accessURL.getValueURI());
        //bz 2520 - make accessURls accessible.
        sb.appendURI(accessURL.getValueURI().toString(),accessURL.getValueURI());
    }




    /**
     * @param sb
     * @param validationLevel
     */
    private static void formatValidation(final HtmlBuilder sb,
            final Validation[] validationLevel) {
        if (validationLevel != null && validationLevel.length > 0) {
            sb.appendLabel("Validated");
            for(int i = 0 ; i < validationLevel.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }

                final Validation validation = validationLevel[i];
                //Character.toChars()
                if (validation.getValidationLevel() > 0) { // skip 0-sized ones.
                    sb.append(createValidationRoundel(validation.getValidationLevel()));// convert it to unicode.
                    if (validation.getValidatedBy() != null) {
                        sb.append("&nbsp;by&nbsp;");
                        sb.appendURI(validation.getValidatedBy());
                    }
                }
            }
            sb.br();
        }
    }
    
    /** converts an integer validation level into a formatted string roundel */
    public static char[] createValidationRoundel(final int validationLevel) {
        return Character.toChars(9311 +validationLevel );
    }
    



	
	public final static String formatType(final String type) {
		if (type == null) {
			return "unspecified";
		}
		final String unprefixed = type.indexOf(":") != -1 
			? StringUtils.substringAfterLast( type,":")
					: type;
		final String converted = (String)typeMapper.get(unprefixed);
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
	private static void formatCuration(final HtmlBuilder sb, final Curation curation) {
	    sb.hr();
        
        sb.appendTitledObjectNoBR("Version",curation.getVersion());

        if (curation.getDates().length > 0) {
            sb.appendLabel("Dates");
            final Date[] arr = curation.getDates();
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
        
		final Creator[] cres = curation.getCreators();
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
		        final Contact contact = contacts[i];
		        if (contact.getName() != null) {
		            sb.appendResourceName(contact.getName());
		            sb.append(", ");
		        }
		        if (contact.getEmail() != null) {
		            sb.append(Character.toChars(9993));
		            sb.append("&nbsp;<a href='mailto:").append(contact.getEmail()).append("'>");
		            sb.append(contact.getEmail()).append("</a>");
		            sb.append(", ");
		        }
		        if (contact.getTelephone() != null) {
                    sb.append(Character.toChars(9742));
                    sb.append("&nbsp;");
		            sb.append(contact.getTelephone()).append(", ");
		        }
		        if (contact.getAddress() != null) {
		            sb.br();
                    sb.append(Character.toChars(9998));
                    sb.append("&nbsp;");		            
		            sb.append(contact.getAddress());
		        }
		    }		   
		}

	}

	private static void formatServiceCapabilities(final HtmlBuilder sb, final Service s) {
	    sb.appendTitledSequence("Service&nbsp;Rights",s.getRights());
	    final Capability[] capabilities = s.getCapabilities();
	    for (int capabilitiesIndex = 0; capabilitiesIndex < capabilities.length; capabilitiesIndex++) {
	        if (capabilitiesIndex > 0) {
	            sb.p();
	        }
	        final Capability c = capabilities[capabilitiesIndex];
	        // display capability-specific info.
	        if (c instanceof CeaServerCapability) {
	            sb.append("This resource describes a <b>Remote&nbsp;Application&nbsp;(CEA)&nbsp;Service</b>");
	            sb.br();
	            formatCapabilityDescription(sb, c);
	            sb.appendTitledURIs("Provided&nbsp;Tasks",(((CeaServerCapability)c).getManagedApplications()));
	        } else   if (c instanceof HarvestCapability) {
	            sb.append("<b>Harvest&nbsp;Capability</b>");
	            sb.br();
	            formatCapabilityDescription(sb, c);
	            sb.appendLabel("Maximum&nbsp;Records&nbsp;Harvested");
	            sb.append(((HarvestCapability)c).getMaxRecords());
	            sb.br();
	        } else    if (c instanceof SearchCapability) {
	            final SearchCapability sc = (SearchCapability)c;
	            sb.append("<b>Search&nbsp;Capability</b>");
	            sb.br();
	            formatCapabilityDescription(sb, c);
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
	        } else     if (c instanceof ConeCapability) {
	            final ConeCapability cc = (ConeCapability)c;
	            sb.append("<img src='classpath:/org/astrogrid/desktop/icons/cone16.png'>&nbsp;This resource describes a <b>Catalog&nbsp;Cone&nbsp;Search&nbsp;Service</b>");
	            sb.br();
	            formatCapabilityDescription(sb, c);
	            sb.appendLabel("Verbose&nbsp;Parameter");
	            sb.append(cc.isVerbosity() ? "supported" : "unsupported");
	            sb.append("&nbsp; ");
	            if (cc.getMaxSR() > 0.0) {
	                sb.appendLabel("Maxumum&nbsp;Search&nbsp;Radius");
	                sb.append(cc.getMaxSR());
	                sb.append("&nbsp; ");
	            }
	            if (cc.getMaxRecords() > 0) {
	                sb.appendLabel("Maximum&nbsp;Results&nbsp;Returned");
	                sb.append(cc.getMaxRecords());
	            }
	            sb.br();
	            if (cc.getTestQuery() != null) {
	                sb.append("<object classid='")
	                .append(TestQueryButton.class.getName())
	                .append("'>")
	                .append("<param name='capabilityIndex' value='")
	                .append(capabilitiesIndex)
	                .append("' >")
	                .append("</object>")
	                ;	                            
	                sb.appendTitledObjectNoBR("RA",cc.getTestQuery().getRa());
	                sb.appendTitledObjectNoBR("Dec",cc.getTestQuery().getDec());
	                sb.appendTitledObjectNoBR("SR",cc.getTestQuery().getSr());
	                if (cc.getTestQuery().getVerb() != 0) {
	                    sb.appendTitledObjectNoBR("Verbose",cc.getTestQuery().getVerb());
	                }
	                sb.appendTitledObjectNoBR("Catalog",cc.getTestQuery().getCatalog());
	                sb.appendTitledObjectNoBR("Extra&nbsp;Params",cc.getTestQuery().getExtras());
	                sb.br();
	            }
	        } else if (c instanceof SiapCapability) {
	            final SiapCapability cc = (SiapCapability)c;
	            sb.append("<img src='classpath:/org/astrogrid/desktop/icons/siap16.png'>&nbsp;This resource describes a <b>Image&nbsp;Access&nbsp;Service&nbsp;(SIAP)</b>");
	            sb.br();
	            formatCapabilityDescription(sb, c);
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
	            final ImageSize isz = cc.getMaxImageSize();
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
	            if (cc.getTestQuery() != null) {
	                sb.br();
	                sb.append("<object classid='")
	                .append(TestQueryButton.class.getName())
	                .append("'>")
	                .append("<param name='capabilityIndex' value='")
	                .append(capabilitiesIndex)
	                .append("' >")
	                .append("</object>")
	                ;                               
	                sb.appendTitledObjectNoBR("Long",cc.getTestQuery().getPos().getLong() );                                
	                sb.appendTitledObjectNoBR("Lat",cc.getTestQuery().getPos().getLat());
	                sb.appendTitledObjectNoBR("Size",cc.getTestQuery().getSize().getLong()
	                        +",&nbsp;" + cc.getTestQuery().getSize().getLat());  
	                if (cc.getTestQuery().getVerb() != 0) {
	                    sb.appendTitledObjectNoBR("Verbose",cc.getTestQuery().getVerb());
	                }
	                sb.appendTitledObjectNoBR("Extra&nbsp;Params",cc.getTestQuery().getExtras());
	                sb.br();
	            }                        
	            sb.br();
	        } else if (c instanceof StapCapability) {
	            final StapCapability sc = (StapCapability)c;
	            sb.append("<img src='classpath:/org/astrogrid/desktop/icons/latest16.png'>&nbsp;This resource describes a <b>Time&nbsp;Series&nbsp;Access&nbsp;Service&nbsp(STAP)</b>");
	            sb.br();
	            formatCapabilityDescription(sb, c);
	            if (sc.getMaxRecords() > 0) {
	                sb.appendLabel("Max&nbsp;Records");
	                sb.append(sc.getMaxRecords());
	                sb.append("&nbsp; ");
	            }
	            sb.appendLabel("Positioning&nbsp;Supported");
	            sb.append(sc.isSupportPositioning());
	            sb.append("&nbsp; ");
	            sb.appendTitledSequence("Supported&nbsp;Formats",sc.getSupportedFormats());
	            if (sc.getTestQuery() != null) {
	                sb.br();
	                sb.append("<object classid='")
	                .append(TestQueryButton.class.getName())
	                .append("'>")
	                .append("<param name='capabilityIndex' value='")
	                .append(capabilitiesIndex)
	                .append("' >")
	                .append("</object>")
	                ;                               
	                sb.appendTitledObjectNoBR("Start",sc.getTestQuery().getStart());
	                sb.appendTitledObjectNoBR("End",sc.getTestQuery().getEnd());
	                if (sc.getTestQuery().getPos() != null) {
	                    sb.appendTitledObjectNoBR("Long",sc.getTestQuery().getPos().getLong());                                    
	                    sb.appendTitledObjectNoBR("Lat",sc.getTestQuery().getPos().getLat());
	                }
	                if (sc.getTestQuery().getSize() != null) {
	                    sb.appendTitledObjectNoBR("Size",sc.getTestQuery().getSize().getLong()
	                            + ",&nbsp;" + sc.getTestQuery().getSize().getLat());                                                                        
	                }
	                sb.br();
	            }	                       
	        } else if (c instanceof SsapCapability) {
	            final SsapCapability sc = (SsapCapability)c;
	            sb.append("<img src='classpath:/org/astrogrid/desktop/icons/ssap16.png'>&nbsp;This resource describes a <b>Spectrum&nbsp;Access&nbsp;Service&nbsp;(SSAP)</b>");
	            sb.br();
	            formatCapabilityDescription(sb, c);
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
	            sb.appendTitledSequence("Supported&nbsp;Frames",sc.getSupportedFrames());
	            sb.append("&nbsp; ");      
	            if (sc.getTestQuery() != null) {
	                sb.br();
	                sb.append("<object classid='")
	                .append(TestQueryButton.class.getName())
	                .append("'>")
	                .append("<param name='capabilityIndex' value='")
	                .append(capabilitiesIndex)
	                .append("' >")
	                .append("</object>")
	                ;                               
	                sb.appendTitledObjectNoBR("Long",sc.getTestQuery().getPos().getLong());
	                sb.appendTitledObjectNoBR("Lat",sc.getTestQuery().getPos().getLat());
	                sb.appendTitledObjectNoBR("Ref Frame",sc.getTestQuery().getPos().getRefframe());                              
	                sb.appendTitledObjectNoBR("Size",sc.getTestQuery().getSize());
	                sb.appendTitledObjectNoBR("Query",sc.getTestQuery().getQueryDataCmd());
	                sb.br();
	            }      
	        } else if (c instanceof TapCapability) {
                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/db16.png'>&nbsp;This resource describes a <b>Catalog&nbsp;Query&nbsp;Service&nbsp;(ADQL)</b>");
                sb.br();
                formatCapabilityDescription(sb, c);
	        } else if (c instanceof VospaceCapability) {	
                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/anystorage16.png'>&nbsp;This resource describes a <b>VOSpace&nbsp;Storage&nbsp;Service</b>");
                sb.br();
                formatCapabilityDescription(sb, c);	            
                sb.appendLabel("VOSpace&nbsp;Root");
                sb.appendURI(((VospaceCapability)c).getVospaceRoot());
                sb.br();
	        } else if (c.getStandardID() != null && StringUtils.containsIgnoreCase(c.getStandardID().toString(),"availability")) { // loose rule for availability.
	            sb.append("This resource provides a <b>Service&nbsp;Availability&nbsp;Check</b>");
	            //sb.br();
	            //formatCapabilityDescription(sb, c);
// removed - as availability is now a annotation source.	            
//	            sb.append("<object classid='")
//	            .append(TestAvailabilityButton.class.getName())
//	            .append("'>")
//	            .append("</object>"); 
	            sb.br();
	        }	else { // take a guess.
	            final String capType = c.getType();
	            final String capTypeUnprefixed =capType != null &&  capType.indexOf(":") != -1 
	            ? StringUtils.substringAfterLast( capType,":")
	                    : capType;
	            if ("WebBrowser".equals(capTypeUnprefixed)) {
	                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/browser16.png'>&nbsp;This resource describes a <b>Web&nbsp;Interface</b>");
	                sb.br();
	                formatCapabilityDescription(sb, c);
	            } else if (SystemFilter.isBoringCapability(c)) {
	                sb.append("This resource descibes an <b>Technical System Service</b>");
	                sb.br();
	                sb.appendTitledObject("Type",capTypeUnprefixed);
	                sb.appendTitledObject("StandardID",c.getStandardID());	                            
	                formatCapabilityDescription(sb, c);
	            } else {
	                sb.append("This resource descibes an <b>Service</b>");
	                sb.br();   	                            
	                sb.appendTitledObject("Type",capTypeUnprefixed);
	                sb.appendTitledObject("StandardID",c.getStandardID());
	                formatCapabilityDescription(sb, c);	                            
	            }

	        }

	        // examine the interfaces..
	        for (int j = 0 ; j < c.getInterfaces().length; j++) {	            
	            final Interface iface = c.getInterfaces()[j];
	            sb.appendTitledObjectNoBR("Interface Type",formatInterfaceType(iface));
	            sb.appendTitledObjectNoBR("Role",iface.getRole());
	            sb.appendTitledObjectNoBR("Version",iface.getVersion());
	            final SecurityMethod[] securityMethods = iface.getSecurityMethods();
	            if (securityMethods.length >0) { // just notify the user that security is required
	                sb.br().append("<img src='classpath:/org/astrogrid/desktop/icons/lock16.png'>&nbsp;This service requires Community Login");
	                sb.br();

	            }
	            /* don't think the user will ever want to see the details.
	            if (securityMethods.length > 0) {
	                sb.appendLabel("Security");
	                for (int k = 0; k < securityMethods.length; k++) {
	                    if (k > 0) {
	                        sb.append(", ");	                        
	                    }
	                    SecurityMethod sm = securityMethods[k];
	                    sb.appendURI(sm.getStandardID());
	                }
	            }
	             */
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
	                final WebServiceInterface wInterface = (WebServiceInterface)iface;
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
	                final ParamHttpInterface phi = (ParamHttpInterface)iface;
	                sb.br();
	                sb.appendTitledObjectNoBR("Query&nbsp;Type",phi.getQueryType());
	                sb.appendTitledObject("Result&nbsp;Type",phi.getResultType());
	                final InputParam[] params = phi.getParams();
	                if (params != null && params.length > 0) {
	                    sb.append("<table><tr><th>Parameter</th><th>Description</th><th>Type</th><th>Use</th></tr>");
	                    for (int i = 0; i < params.length; i++) {
	                        final InputParam inputParam = params[i];
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
	                            final SimpleDataType dataType = inputParam.getDataType();
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




    /**
     * @param sb
     * @param c
     */
    private static void formatCapabilityDescription(final HtmlBuilder sb, final Capability c) {
        // validation, and description.
        formatValidation(sb,c.getValidationLevel());
        if (StringUtils.isNotEmpty(c.getDescription())) {
            sb.append(c.getDescription());
            sb.br();
        }
    }

	
	/** create a readable description for a type of 
     * @param iface
     * @return
     */
    private static String formatInterfaceType(final Interface iface) {
        if (iface instanceof WebServiceInterface) {
            return "Web Service";
        } else if (iface instanceof ParamHttpInterface) {
            return "Http Query";
        }
        
        final String type = iface.getType();
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
	private static void formatContent(final HtmlBuilder sb, final Content content) {
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
                } catch (final URISyntaxException x) {
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
		        final Relationship rel = rels[i];
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

	/** url that bibcodes can be retreived from
	 * - should this be a preference? */
	public static final String BIBCODE_URL = "http://adsabs.harvard.edu/cgi-bin/nph-bib_query?bibcode=";


}
