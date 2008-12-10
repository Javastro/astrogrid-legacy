/**
 * 
 */
package org.astrogrid.desktop.modules.system.converters;

import java.util.Collection;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/** converts a vector into an object array.
 * @author Noel Winstanley
 * @since Jun 10, 200612:12:55 AM
 */
public class ArrayConvertor implements Converter {

	public Object convert(final Class arg0, final Object arg1) {
		if (!(arg1 instanceof Collection 
				&& arg0.isArray()
				&& arg0.getComponentType().equals(Object.class))) {
			throw new ConversionException("Can only convert collections to object arrays: "
					+ arg0.getName() + " " + arg1.getClass().getName());
		}
		return ((Collection)arg1).toArray();
	}

}
