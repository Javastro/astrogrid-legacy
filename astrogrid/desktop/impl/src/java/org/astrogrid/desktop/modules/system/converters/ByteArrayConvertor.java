/**
 * 
 */
package org.astrogrid.desktop.modules.system.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** converts a string into a byte array.
 * there's a bytearrayconvertor in the beanutils library already, but it doesn't seem to do what I want.
 * seems to expect a stringidied array representation of a byte array.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 21, 200810:47:30 AM
 */
public class ByteArrayConvertor implements Converter {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ByteArrayConvertor.class);

    public Object convert(final Class arg0, final Object arg1) {
        if (!(arg0.isArray()
                && arg0.getComponentType().equals(byte.class))) {
            throw new ConversionException("Can only convert  to byte arrays: "
                    + arg0.getName() + " " + arg1.getClass().getName());
        }
        if (arg1 == null) {
            return new byte[0];
        } else {
            return arg1.toString().getBytes();
        }
    }
    

    
    
}
