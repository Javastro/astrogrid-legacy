/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.hivemind.schema.Translator;
import org.apache.hivemind.service.EventLinker;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;

import net.sourceforge.hiveutils.service.impl.ObjectBuilderImpl;

/** subclass of object builder which ensures objects
 * are always constructed on EDT (as you're meant to do with swing);
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
	public EventDispatchThreadObjectBuilder(Log logger, Map config,
			Translator objectTranslator, EventLinker linker) {
		super(logger, config, objectTranslator, linker);
		this.logger = logger;
	}
	private final Log logger;

	
	public Object create(String name, Object[] userArgs) {
		if (SwingUtilities.isEventDispatchThread()) {
			return super.create(name, userArgs);
		} else {
			Builder b = new Builder(name,userArgs);
			try {
				SwingUtilities.invokeAndWait(b);
			} catch (InterruptedException x) {
				logger.error("InterruptedException",x);
			} catch (InvocationTargetException x) {
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
		public Builder(String name, Object[] args) {
			super();
			this.name = name;
			this.args = args;
		}
	}
}
