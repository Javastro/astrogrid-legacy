/**
 * 
 */
package org.astrogrid.desktop.modules.system.transformers;

import java.io.StringWriter;

import org.apache.commons.collections.Transformer;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

public class DocumentTransformer implements Transformer {
    public DocumentTransformer() {}
    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        StringWriter sw= new StringWriter();
        DomHelper.DocumentToWriter((Document)arg0,sw);
        return sw.toString();
    }

}