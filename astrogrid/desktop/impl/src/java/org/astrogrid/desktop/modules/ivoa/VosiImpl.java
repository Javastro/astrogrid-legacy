/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Vosi;
import org.astrogrid.acr.ivoa.VosiAvailabilityBean;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.ParamHttpInterface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
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
public class VosiImpl implements Vosi {
    
    private final RegistryInternal reg;
    

    /**
     * @param reg
     */
    public VosiImpl(final RegistryInternal reg) {
        super();
        this.reg = reg;
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
                            final Period period = new Period(content.trim());
                            final DateTime now = new DateTime();
                            final DateTime upSince = now.minus(period);
                            result.setUpSince(upSince.toDate());
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
            result.setNotes(new String[] {"Failed to contact service: " + e.getMessage()});
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
    
}
