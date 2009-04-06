/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.hiveutils.service.impl.ObjectBuilderImpl;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.hivemind.schema.Translator;
import org.apache.hivemind.service.EventLinker;
import org.astrogrid.desktop.modules.votech.AnnotationSource;

/** Implementation of {@code IterableObjectBuilder}
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 5, 200711:19:15 AM
 */
public class IterableObjectBuilderImpl extends ObjectBuilderImpl implements
		IterableObjectBuilder {

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public IterableObjectBuilderImpl(final Log arg0, final Map arg1, final Translator arg2, final EventLinker arg3) {
		super(arg0, arg1, arg2, arg3);
		this.objects = new TreeMap( arg1) ; // sorts the entries by key value
	}
	private final Map objects;

	public Iterator<AnnotationSource> creationIterator() {
		return IteratorUtils.transformedIterator(objects.keySet().iterator(),
				new Transformer() {

					public Object transform(final Object arg0) {
						return create((String)arg0);
					}
		});
	}

	public Iterator<AnnotationSource> creationIterator(final Object o) {
		return IteratorUtils.transformedIterator(objects.keySet().iterator(),
				new Transformer() {

					public Object transform(final Object arg0) {
						return create((String)arg0,o);
					}
		});
	}

	public Iterator<AnnotationSource> creationIterator(final Object[] o) {
		return IteratorUtils.transformedIterator(objects.keySet().iterator(),
				new Transformer() {

					public Object transform(final Object arg0) {
						return create((String)arg0,o);
					}
		});
	}

	public String[] listObjectNames() {
		final Set keySet = objects.keySet();
		return (String[]) keySet.toArray(new String[keySet.size()]);
	}

}
