/**
 * 
 */
package org.astrogrid.desktop.modules.ag.converters;

import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.joda.time.DateTime;

/** convertor that will parse a string into a Date.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 10, 20084:45:07 PM
 */
public class DateConvertor implements Converter {

    public Object convert(final Class arg0, final Object arg1) {
        if (arg0 != Date.class) {
            throw new ConversionException("Can only convert to util.Date");
        }
        final DateTime now = new DateTime(arg1.toString());
        return now.toDate();
    }

}
