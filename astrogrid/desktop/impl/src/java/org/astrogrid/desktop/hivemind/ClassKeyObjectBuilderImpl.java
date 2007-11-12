/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.hivemind.schema.Translator;
import org.apache.hivemind.service.EventLinker;

import net.sourceforge.hiveutils.service.impl.ObjectBuilderImpl;
import net.sourceforge.hiveutils.service.impl.ObjectContribution;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 7, 20071:29:00 PM
 */
public class ClassKeyObjectBuilderImpl extends ObjectBuilderImpl implements ClassKeyObjectBuilder{

    public ClassKeyObjectBuilderImpl(Log logger, Map config,
            Translator objectTranslator, EventLinker linker) {
        super(logger, config, objectTranslator, linker);
        for(Iterator i = config.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry)i.next();
            classMap.put(((ObjectContribution)e.getValue()).getObjectClass(),e.getKey());
        }
    }
    private Map classMap = new HashMap();

    public Object create(Class clazz) {
        String key = (String)classMap.get(clazz);
        return super.create(key);               
    }

}
