package org.astrogrid.desktop.modules.system.converters;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.beanutils.Converter;

/** converts strings to URL objects
 * @author Noel Winstanley
 * @since Apr 14, 20062:33:59 AM
 */
public  class URLConvertor implements Converter {
	// convert to URL
	public Object convert(Class arg0, Object arg1) {
	    if (arg0 != URL.class) {
	        throw new RuntimeException("Can only convert to URLs " + arg0.getName());
	    }
	    try {
	        return new URL(arg1.toString());
	    } catch (MalformedURLException e) {
	        throw new IllegalArgumentException("Cannot convert " + arg1 + " to URL");
	    }
	}
}