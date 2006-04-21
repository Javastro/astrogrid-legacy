/**
 * 
 */
package org.astrogrid.desktop.modules.ag.transformers;

import org.apache.commons.collections.Transformer;
import org.astrogrid.util.DomHelper;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

/** Converts castor object to a dom, which is then passed to another transformer 
 * for further processing.
 * @author Noel Winstanley
 * @since Apr 18, 20067:01:58 PM
 */
public class StyledCastorTransformer implements Transformer {

	private final Transformer trans;
	public StyledCastorTransformer(final Transformer trans) {
		super();
		this.trans = trans;
	}
	public Object transform(Object arg0) {
		Document doc;
		try {
			doc = DomHelper.newDocument();
			Marshaller.marshal(arg0,doc);
			return doc;
		} catch (Exception x) {
			throw new RuntimeException(x.getMessage());
		}

	}

}
