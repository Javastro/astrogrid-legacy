/**
 * 
 */
package org.astrogrid.desktop.modules.system.transformers;

import org.apache.commons.collections.Transformer;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** transforms a document to a string */
public class DocumentTransformer implements Transformer {
    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
    	if (arg0 == null) {
    		return "null";
    	}
    	if (! (arg0 instanceof Document)) {
    		throw new IllegalArgumentException("Can only transform Documents:" + arg0.getClass().getName());
    	}
    	return DomHelper.DocumentToString((Document)arg0);

    }

}