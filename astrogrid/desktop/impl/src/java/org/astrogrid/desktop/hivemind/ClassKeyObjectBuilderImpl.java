/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.hiveutils.service.impl.ObjectBuilderImpl;
import net.sourceforge.hiveutils.service.impl.ObjectContribution;

import org.apache.commons.logging.Log;
import org.apache.hivemind.schema.Translator;
import org.apache.hivemind.service.EventLinker;

/** Implementation of a {@code ClassKeyObjectBuilder}
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 7, 20071:29:00 PM
 */
public class ClassKeyObjectBuilderImpl extends ObjectBuilderImpl implements ClassKeyObjectBuilder{

    public ClassKeyObjectBuilderImpl(final Log logger, final Map config,
            final Translator objectTranslator, final EventLinker linker) {
        super(logger, config, objectTranslator, linker);
        for(final Iterator i = config.entrySet().iterator(); i.hasNext(); ) {
            final Map.Entry e = (Map.Entry)i.next();
            classMap.put(((ObjectContribution)e.getValue()).getObjectClass(),e.getKey());
        }
    }
    private final Map classMap = new HashMap();

    public Object create(final Class clazz) {
        final String key = (String)classMap.get(clazz);
        return super.create(key);               
    }

}
