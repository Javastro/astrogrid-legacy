/**
 * 
 */
package org.astrogrid.desktop.modules.ag.transformers;

import java.io.StringWriter;

import org.apache.commons.collections.Transformer;
import org.exolab.castor.xml.Marshaller;

/** transform castor object to String.
 * @author Noel Winstanley
 * @since Apr 18, 20066:58:56 PM
 */
public class CastorTransformer implements Transformer {

	public Object transform(Object arg0) {
		   try {
               StringWriter sw = new StringWriter();
               Marshaller.marshal(arg0,sw);
               return sw.toString();
           } catch (Exception e) {
               throw new RuntimeException(e.getMessage());
           }      
	}

}
