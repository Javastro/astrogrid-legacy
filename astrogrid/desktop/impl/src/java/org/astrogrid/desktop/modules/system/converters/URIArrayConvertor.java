/**
 * 
 */
package org.astrogrid.desktop.modules.system.converters;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** converts a vector into an array of URI
 * error tolerant.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 21, 200810:47:30 AM
 */
public class URIArrayConvertor implements Converter {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(URIArrayConvertor.class);

    public Object convert(final Class arg0, final Object arg1) {
        if (!(arg0.isArray()
                && arg0.getComponentType().equals(URI.class))) {
            throw new IllegalArgumentException("Can only convert  to URI arrays: "
                    + arg0.getName() + " " + arg1.getClass().getName());
        }
        final Collection c;
        if (arg1 instanceof Collection) {
            c = (Collection)arg1;
        } else if (arg1 instanceof Object[]) {
            c = Arrays.asList((Object[])arg1);
        } else {
            throw new IllegalArgumentException("Can only convert from Collections and Object[] " + arg1.getClass().getName());
        }
        final List<URI> results = new ArrayList<URI>();
        for (final Object o : c) {
            if (o == null) {
                continue;
            }
            try {
                final URI u = new URI(o.toString().trim());
                results.add(u);
            } catch (final URISyntaxException e) {
                logger.warn("Failed to parse an item as URI: " + e.getMessage());
            }
            
        }
        return results.toArray(new URI[results.size()]);       
    }

    
    
}
