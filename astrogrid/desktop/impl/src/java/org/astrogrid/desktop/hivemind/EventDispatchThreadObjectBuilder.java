/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.swing.SwingUtilities;

import net.sourceforge.hiveutils.service.impl.ObjectBuilderImpl;

import org.apache.commons.logging.Log;
import org.apache.hivemind.schema.Translator;
import org.apache.hivemind.service.EventLinker;

/** Hivemind object builder that always constructs objects on the Swing Event Dispatch Thread.
 * <p/>
 *  This is one of the requirements for correct swing programming - that all GUI code is contructed and configured on the EDT
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 18, 20074:06:57 PM
 */
public class EventDispatchThreadObjectBuilder extends ObjectBuilderImpl {

	/**
	 * @param logger
	 * @param config
	 * @param objectTranslator
	 * @param linker
	 */
	public EventDispatchThreadObjectBuilder(final Log logger, final Map config,
			final Translator objectTranslator, final EventLinker linker) {
		super(logger, config, objectTranslator, linker);
		this.logger = logger;
	}
	private final Log logger;

	
	public Object create(final String name, final Object[] userArgs) {
		if (SwingUtilities.isEventDispatchThread()) {
			return super.create(name, userArgs);
		} else {
			final Builder b = new Builder(name,userArgs);
			try {
				SwingUtilities.invokeAndWait(b);
			} catch (final InterruptedException x) {
				logger.error("InterruptedException",x);
			} catch (final InvocationTargetException x) {
				logger.error("InvocationTargetException",x);
			}
			return b.result;
		}
	}
	
	private class Builder implements Runnable {
		final String name;
		final Object[] args;
		
		Object result;
		public void run() {
			result = EventDispatchThreadObjectBuilder.super.create(name, args);
		}
		public Builder(final String name, final Object[] args) {
			super();
			this.name = name;
			this.args = args;
		}
	}
}
