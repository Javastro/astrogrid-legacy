/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.Test;
import junit.framework.TestCase;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.desktop.modules.system.pref.Preference;

/** HTTP-Request client for Sesame.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 15, 20082:04:05 PM
 */
public class SesameRestImpl implements SesameInternal, PropertyChangeListener {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SesameRestImpl.class);

    private final Ehcache cache;
    private final Preference endpoint;
    private String base;
    private final HttpClient http;

    protected final XMLInputFactory inputFactory;
    private static final URI DEFAULT_ENDPOINT = URI.create("http://vizier.u-strasbg.fr/viz-bin/nph-sesame/");
    public SesameRestImpl(final HttpClient http,final Ehcache cache, final Preference endpoint) {
        super();
        this.cache = cache;
        inputFactory = XMLInputFactory.newInstance(); 
        this.endpoint = endpoint;
        endpoint.addPropertyChangeListener(this);
        endpoint.initializeThroughListener(this);
        this.http = http;
        base = DEFAULT_ENDPOINT.toString();
    }

    public SesamePositionBean resolve(final String objectName) throws ServiceException,
            NotFoundException {
        final Element element = cache.get(objectName);
        if (element != null && element.getValue() != null) {
            return (SesamePositionBean)element.getValue();
        } else {
            final SesamePositionBean b = primResolve(objectName);
            if (b != null) { // unlikely to be null, but still.
                cache.put(new Element(objectName,b));
            }
            return b;
        }
    }

    public String sesame(final String objectName, final String resultType) throws ServiceException {
        return sesameChooseService(objectName,resultType,false,"SNV");
    }

    public String sesameChooseService(final String objectName, final String resultType
            , final boolean allIdentifiers, final String service) throws ServiceException {
            final HttpMethod meth = buildGetMethod(objectName, resultType,
                    allIdentifiers, service);
            try {            
            final int status = http.executeMethod(meth);
            if (status != HttpStatus.SC_OK) {
                throw new ServiceException("Failed to query sesame: returned HTTP Error " + status);
            }
            return meth.getResponseBodyAsString();
        } catch (final HttpException x) {
            throw new ServiceException("Failed to query sesame",x);
        } catch (final IOException x) {
            throw new ServiceException("Failed to query sesame",x);
        } finally {
            meth.releaseConnection();
        }
    }
    private SesamePositionBean primResolve(final String objectName) throws ServiceException, NotFoundException {
        final HttpMethod meth = buildGetMethod(objectName, "x",
                true, "SNV");
        try {            
        final int status = http.executeMethod(meth);
        if (status != HttpStatus.SC_OK) {
            throw new ServiceException("Failed to query sesame: returned HTTP Error " + status);
        }
        return parseResponse(meth.getResponseBodyAsStream());
    } catch (final HttpException x) {
        throw new ServiceException("Failed to query sesame",x);
    } catch (final IOException x) {
        throw new ServiceException("Failed to query sesame",x);
    } finally {
        meth.releaseConnection();
    }
    }
    /** Parse the xml response into a bean
     * @param responseBodyAsStream
     * @return
     * @throws ServiceException 
     * @throws NotFoundException 
     */
    private SesamePositionBean parseResponse(final InputStream str) throws ServiceException, NotFoundException {
        SesamePositionBean result = new SesamePositionBean();
        final List<String> infoList = new ArrayList<String>();
        final Set<String> aliases = new HashSet<String>();
        try {
            final XMLStreamReader is = inputFactory.createXMLStreamReader(str);
            for (is.next(); ! (is.isEndElement() && is.getLocalName().equals("Sesame")); is.next()) {
                if (is.isStartElement()) { // otherwise, we don't care.
                    final String elementName = is.getLocalName();
                    if (elementName.equals("name")) { //used to be called 'target'
                        result.setTarget(is.getElementText());
                    } else if (elementName.equals("Resolver")) {
                        // reset the position bean, only copying across the target field.
                        final String target = result.getTarget();
                        result = new SesamePositionBean();
                        result.setTarget(target);
                        result.setService(is.getAttributeValue(null,"name"));
                    } else if (elementName.equals("INFO") || elementName.equals("ERROR")) { 
                        // add to message list..
                        infoList.add(result.getService() + ": " + is.getElementText());
                    } else if (elementName.equals("otype")) {
                        result.setOType(is.getElementText());
                    } else if (elementName.equals("jpos")) {
                        result.setPosStr(is.getElementText());
                    } else if (elementName.equals("jradeg")) {
                        try{
                            result.setRa(Double.parseDouble(is.getElementText()));
                        } catch (final NumberFormatException e) {
                            infoList.add(result.getService() + ": Failed to parse jradeg");
                        }
                    } else if (elementName.equals("jdedeg")) {
                        try{
                            result.setDec(Double.parseDouble(is.getElementText()));
                        } catch (final NumberFormatException e) {
                            infoList.add(result.getService() + ": Failed to parse jdedeg");
                        }                       
                    } else if (elementName.equals("errRAmas")) {
                        try{
                            result.setRaErr(Double.parseDouble(is.getElementText()));
                        } catch (final NumberFormatException e) {
                            infoList.add(result.getService() + ": Failed to parse errRAmas");
                        }                       
                    } else if (elementName.equals("errDEmas")) {
                        try{
                            result.setDecErr(Double.parseDouble(is.getElementText()));
                        } catch (final NumberFormatException e) {
                            infoList.add(result.getService() + ": Failed to parse errDEmas");
                        }                       
                    } else if (elementName.equals("oname")) {
                        result.setOName(is.getElementText());
                    } else if (elementName.equals("alias")) {
                        aliases.add(is.getElementText());
                    }
                }
            }
        } catch (final XMLStreamException x) {
            throw new ServiceException("Failed to parse response from Sesame",x);
        } finally {
         IOUtils.closeQuietly(str);
        }
        // we've either got a resilt with a valid position, or we need to throw a not found exception..
        if (result.getPosStr() == null) {
            throw new NotFoundException(infoList.toString());
        }
        result.setAliases(aliases.toArray(new String[aliases.size()]));
        return result;
    }

    /**
     * @param objectName
     * @param resultType
     * @param allIdentifiers
     * @param service
     * @return
     */
    private HttpMethod buildGetMethod(final String objectName, final String resultType,
            final boolean allIdentifiers, final String service) {
        final StringBuilder sb = new StringBuilder(base);
        sb.append("-o")
            .append(resultType);
        if (allIdentifiers) {
            sb.append("i");
        }
        sb.append("/").append(service);
        
        final HttpMethod meth = new GetMethod(sb.toString());
        meth.setFollowRedirects(true);
        meth.setQueryString(URLEncoder.encode(objectName));
        return meth;
    }

    public Test getSelftest() {
        return new TestCase("Sesame object name resolver") {
            protected void runTest(){
                try {
                    final SesamePositionBean pos = primResolve("m32");
                    assertNotNull(pos);
                } catch (final ServiceException x) {
                    fail("Unable to access Sesame service");
                } catch (final NotFoundException x) {
                    fail("Sesame service was unable to resolve 'm32' to a position");
                }
            }
        };
    }

    /** called when preference changes */
    public void propertyChange(final PropertyChangeEvent evt) {
       
        try {
            final URL u = new URL(endpoint.getValue());
            base = u.toString();
            if (! base.endsWith("/")) {
                base += "/";
            }
        } catch (final MalformedURLException x) {
            logger.error("Sesame endpoint is malformed",x);
        }
    }

}
