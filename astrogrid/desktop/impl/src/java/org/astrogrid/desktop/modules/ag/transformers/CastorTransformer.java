/**
 * 
 */
package org.astrogrid.desktop.modules.ag.transformers;

import java.io.StringWriter;

import org.apache.commons.collections.Transformer;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.exolab.castor.xml.Marshaller;

/** Transform castor object to String.
 * @author Noel Winstanley
 * @since Apr 18, 20066:58:56 PM
 */
public class CastorTransformer implements Transformer {

	public Object transform(final Object arg0) {
		   try {
               final StringWriter sw = new StringWriter();
               Marshaller.marshal(arg0,sw);
               return sw.toString();
           } catch (final Exception e) {
               throw new RuntimeException(new ExceptionFormatter().formatException(e));
           }      
	}

}
