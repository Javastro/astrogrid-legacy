/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.VosiAvailabilityBean;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.ParamHttpInterface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.joda.time.DateTime;
import org.joda.time.Period;

/** VOSI Availability client.
 * <p/>
 * very loose parsing - first of identifying the correct capability, and thn
 * of parsing the response - hopefully this will give us forward-compatability when the standard changes.
 * 
 * visually checked with Vosi 1.0 - pretty confident it should work
 *  - only possible gotcha is the loose parsing of vosi capabilities - anything # capability - but this should be ok.
 *  - there's still legacy AG availability services around that have an old availabvility type here. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 20085:15:03 PM
 */
public class VosiImpl implements VosiInternal {
    
    private final RegistryInternal reg;
    private final DateFormat df;

    /**
     * @param reg
     */
    public VosiImpl(final RegistryInternal reg) {
        super();
        this.reg = reg;
        df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);        
    }


    public VosiAvailabilityBean checkAvailability(final URI id) throws InvalidArgumentException {
        Resource r;
        try {
            r = reg.getResource(id);
        } catch (final NotFoundException e) {
            throw new InvalidArgumentException(id + " could not be found in the registry");
        } catch (final ServiceException x) {
            throw new InvalidArgumentException("unable to resolve " + id + " in the registry",x);
        }
        if (!(r instanceof Service)) {
            throw new InvalidArgumentException(id + " is not a service");
        }
        final URL u =findRestAvailabilityURL((Service)r);
        // ok, now construct a parser and fetch this url.
        final XMLInputFactory fac = XMLInputFactory.newInstance();
        XMLStreamReader in = null;
        final VosiAvailabilityBean result = new VosiAvailabilityBean();
        try {
            // sloppy parser, by design.
            in = fac.createXMLStreamReader(u.openStream());
            final List<String> notes = new ArrayList<String>();
            while (in.hasNext()) {
                in.next();
                if (in.isStartElement()) {
                    final String localName = in.getLocalName();
                    if (StringUtils.containsIgnoreCase(localName,"available")) { // 0.3, 0.4
                        result.setAvailable(Boolean.valueOf(in.getElementText()).booleanValue());
                        
                    } else if (StringUtils.containsIgnoreCase(localName,"upSince")) { // 0.4
                        final String content = in.getElementText();
                        if (StringUtils.isNotEmpty(content)) {
                            try {
                                final XmlDateTime dateTime = XmlDateTime.Factory.parse(content.trim());
                                result.setUpSince(dateTime.getDateValue());
                            } catch (final XmlException x) {
                                // ignore.
                            }  
                        }
                        
                    } else if (StringUtils.containsIgnoreCase(localName,"downAt")) { // 0.4
                        final String content = in.getElementText();
                        if (StringUtils.isNotEmpty(content)) {
                            try {
                                final XmlDateTime dateTime = XmlDateTime.Factory.parse(content.trim());
                                result.setDownAt(dateTime.getDateValue());
                            } catch (final XmlException x) {
                                // ignore.
                            }  
                        }
                        
                    } else if (StringUtils.containsIgnoreCase(localName,"backAt")) { // 0.4
                        final String content = in.getElementText();
                        if (StringUtils.isNotEmpty(content)) {
                            try {
                                final XmlDateTime dateTime = XmlDateTime.Factory.parse(content.trim());
                                result.setBackAt(dateTime.getDateValue());
                            } catch (final XmlException x) {
                                // ignore.
                            }  
                        }
                        
                    } else if (StringUtils.containsIgnoreCase(localName,"note")) { // 0.4
                        notes.add(in.getElementText());
                        
                    } else if (StringUtils.containsIgnoreCase(localName,"uptime")) { // 0.3
                        final String content = in.getElementText();
                        if (StringUtils.isNotEmpty(content)) {
                            try {
                            final Period period = new Period(content.trim());
                            final DateTime now = new DateTime();
                            final DateTime upSince = now.minus(period);
                            result.setUpSince(upSince.toDate());
                            } catch (final IllegalArgumentException e) {
                                // no matter.
                            }
                        }
                        
                    } else if (StringUtils.containsIgnoreCase(localName,"validTo")) { // 0.3
                        final String content = in.getElementText();
                        if (StringUtils.isNotEmpty(content)) {
                            try {
                                final XmlDateTime dateTime = XmlDateTime.Factory.parse(content.trim());
                                result.setDownAt(dateTime.getDateValue());
                            } catch (final XmlException x) {
                                // ignore.
                            }  
                        }
                    }
                }
            } // end parser loop.
            if (! notes.isEmpty()) {
                result.setNotes(notes.toArray(new String[notes.size()]));
            }
            return result;
        } catch (final IOException e) {
            result.setAvailable(false);
            result.setNotes(new String[] {"Failed to contact service"});
            result.setBackAt(null);
            result.setDownAt(null);
            result.setUpSince(null);
            return result;
        } catch (final XMLStreamException x) {
            result.setAvailable(false);
            result.setNotes(new String[] {"Failed to read availability of service: " + x.getMessage()});
            result.setBackAt(null);
            result.setDownAt(null);
            result.setUpSince(null);
            return result;            
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final XMLStreamException e) {
                // ignored
            }
        }

    }

    
    /** locate a REST interface in an availability capabiliity, and reutrn the endpoint
     * else will return null.
     * 
     * extremely loose parsing - works as long as standardID contains 'availability'.
     * @param s
     * @return
     * @throws InvalidArgumentException 
     * @throws MalformedURLException 
     */
    private URL  findRestAvailabilityURL(final Service s) throws InvalidArgumentException {
        final Capability[] caps = s.getCapabilities();
        for (int i = 0; i < caps.length; i++) {
            final Capability capability = caps[i];
            // very loose parsing - hope the standardID will always contain 'availablity'
            if (capability.getStandardID() != null 
                   && StringUtils.containsIgnoreCase(capability.getStandardID().toString(), "#availability")) {
                final Interface[] ifaces = capability.getInterfaces();
                Interface ifac = null;
                if (ifaces.length == 1) {
                    ifac = ifaces[0];
                } else {
                    for (int j = 0; j < ifaces.length; j++) {
                        if (ifaces[j] instanceof ParamHttpInterface) {
                            ifac= ifaces[j];
                        }
                    }
                } // end find interface
                if (ifac != null) {
                    final AccessURL[] urls = ifac.getAccessUrls();
                    if (urls.length != 0) {
                        return urls[0].getValue();
                    } 
                } // end inspect interface.
                
                // ok. if we get here, keep scanning for another candidate.
            }
        }// end for all capabilities
        throw new InvalidArgumentException(s.getId() + " does not provide an availability capability");
    }


    public String makeTooltipFor(final VosiAvailabilityBean b) {
        final HtmlBuilder sb = new HtmlBuilder();
        if (b == null) {
            sb.append("This service provides no availability information");
        } else {
            if (b.isAvailable()) {
                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/greenled16.png'>&nbsp;");
                if (b.getDownAt() != null) {
                    sb.append("OK until " + df.format(b.getDownAt()));
                } else {
                    sb.append("Service OK");
                }
                
            } else {                      
                sb.append("<img src='classpath:/org/astrogrid/desktop/icons/redled16.png'>&nbsp;");
                if (b.getBackAt() != null) {
                    sb.append("Down until " + df.format(b.getBackAt()));
                } else {
                    sb.append("Service Down");
                }
            }
            if (b.getUpSince()!= null) {
                sb.append("<br>Up since " +df.format(b.getUpSince()));
            }
            final String[] notes = b.getNotes();
            if (notes != null && notes.length > 0) {
                sb.append("<p>");
                for (int i = 0; i < notes.length; i++) {
                    sb.append("<br>")
                        .append(notes[i]);
                }                 
            }                        
        }
        return sb.toString();
    }


    public Icon suggestIconFor(final VosiAvailabilityBean bean) {
        if (bean == null) {
            return UIConstants.UNKNOWN_ICON;
        } else if (bean.isAvailable()) {
            return UIConstants.SERVICE_OK_ICON;
        } else {
            return UIConstants.SERVICE_DOWN_ICON;
        }
    }
    
}
