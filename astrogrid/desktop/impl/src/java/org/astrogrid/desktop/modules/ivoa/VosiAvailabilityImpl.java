/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.ParamHttpInterface;
import org.astrogrid.acr.ivoa.resource.Service;
import org.joda.time.Period;

/** Implementation against availability
 * very loose parsing - first of identifying the correct capability, and thn
 * of parsing the response - hopefully this will give us forward-compatability when the standard changes.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 20085:15:03 PM
 */
public class VosiAvailabilityImpl implements VosiAvailability {

    public VosiAvailabilityBean getAvailability(Service s) throws ServiceException {
        URL u =findRestAvailabilityURL(s);
        if (u == null) {
            return null;
        }
        // ok, now construct a parser and fetch this url.
        XMLInputFactory fac = XMLInputFactory.newInstance();
        XMLStreamReader in = null;
        try {
            in = fac.createXMLStreamReader(u.openStream());
            final VosiAvailabilityBean result = new VosiAvailabilityBean();
            while (in.hasNext()) {
                in.next();
                if (in.isStartElement()) {
                    final String localName = in.getLocalName();
                    if (StringUtils.containsIgnoreCase(localName,"available")) {
                        result.setAvailable(Boolean.valueOf(in.getElementText()).booleanValue());
                    } else if (StringUtils.containsIgnoreCase(localName,"uptime")) {
                        String content = in.getElementText();
                        if (StringUtils.isNotEmpty(content)) {
                            Period period = new Period(content.trim());
                            result.setUptime(period);
//                            Pattern pattern = Pattern.compile("PT(\\d+)S");
//                            Matcher matcher = pattern.matcher(content.trim());
//                            if (matcher.matches()) {
//                                String secs = matcher.group(1);
//                                try {
//                                    result.setUptime(Long.parseLong(secs));
//                                } catch (NumberFormatException e) {
//                                    // never mind..
//                                }
//                            }
                        }
                    } else if (StringUtils.containsIgnoreCase(localName,"validTo")) {
                        String content = in.getElementText();
                        if (StringUtils.isNotEmpty(content))
                        {
                            try {
                                    XmlDateTime dateTime = XmlDateTime.Factory.parse(content.trim());
                                    result.setValidTill(dateTime.getDateValue());
             
                            } catch (XmlException x) {
                                // ignore.
                            }  
                            }
                    }
                }
            }
            return result;
        } catch (IOException e) {
            throw new ServiceException("Failed to contact service",e);
        } catch (XMLStreamException x) {
            throw new ServiceException("Failed to read availability information from service",x);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (XMLStreamException e) {
                // ignored
            }
        }

    }

    
    /** locate a REST interface in an availability capabiliity, and reutrn the endpoint
     * else will return null.
     * @param s
     * @return
     * @throws MalformedURLException 
     */
    private URL  findRestAvailabilityURL(Service s) {
        Capability[] caps = s.getCapabilities();
        for (int i = 0; i < caps.length; i++) {
            Capability capability = caps[i];
            // very loose parsing - hope the standardID will always contain 'availablity'
            if (StringUtils.containsIgnoreCase(capability.getStandardID().toString(), "availability")) {
                Interface[] ifaces = capability.getInterfaces();
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
                    AccessURL[] urls = ifac.getAccessUrls();
                    if (urls.length != 0) {
                        return urls[0].getValue();
                    } 
                } // end inspect interface.
                
                // ok. if we get here, keep scanning for another candidate.
            }
        }// end for all capabilities
        return null;
    }
    
}
