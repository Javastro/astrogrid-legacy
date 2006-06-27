/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.JOptionPane;

import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.framework.ReflectionHelper;

/** Configuraiton and UI enhancements only available on mac platform.
 * 
 * as we dunno whether these classes are goiong to be available, have to go through
 * same rigmarole as with javaws - i.e. call methods using reflection.
 * Even though we can tell hivemind not to include this class when not running on
 * mac, still needs to be written in a reflective kind of way - otherwise devs on
 * other OS would be unable to compile or build.
 * 
 * @todo add the hooks to handle 'file open' events.
 * @author Noel Winstanley
 * @since Jun 21, 200611:31:12 PM
 */
public class MacSetup implements InvocationHandler {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(MacSetup.class);
	
	public MacSetup(UIInternal ui, Shutdown shutdown) throws ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		this.ui = ui;
		this.shutdown = shutdown;
		Class applicationClass = Class.forName("com.apple.eawt.Application");
		Class listenerClass = Class.forName("com.apple.eawt.ApplicationListener");
		Class eventClass= Class.forName("com.apple.eawt.ApplicationEvent");
		handledMethod = ReflectionHelper.getMethodByName(eventClass,"setHandled");
		
		Method m = ReflectionHelper.getMethodByName(applicationClass,"getApplication");
		application = m.invoke(null,null);
		
		m = ReflectionHelper.getMethodByName(applicationClass,"setEnabledAboutMenu");
		m.invoke(application,new Object[]{Boolean.TRUE});
		
		m = ReflectionHelper.getMethodByName(applicationClass,"setEnabledPreferencesMenu");
		m.invoke(application,new Object[]{Boolean.TRUE});
		
		Object listener = Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{listenerClass}, this);
		
		m = ReflectionHelper.getMethodByName(applicationClass,"addApplicationListener");
		m.invoke(application,new Object[]{listener});
		
		// finally, set up the methods we're to be listening for..
		aboutMethod = ReflectionHelper.getMethodByName(listenerClass,"handleAbout");
		preferencesMethod = ReflectionHelper.getMethodByName(listenerClass,"handlePreferences");
		quitMethod = ReflectionHelper.getMethodByName(listenerClass,"handleQuit");
		logger.info("Configured for OSX");
	}
	
	protected final Object application;
	protected final Method aboutMethod;
	protected final Method preferencesMethod;
	protected final Method quitMethod;
	protected final UIInternal ui;
	protected final Shutdown shutdown;
	protected final Method handledMethod;

	/** handles calls to the menu */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.equals(aboutMethod)) {
			ui.showAboutDialog();
			handledMethod.invoke(args[0],new Object[]{Boolean.TRUE});			
		} else if (method.equals(preferencesMethod)) {
			ui.showPreferencesDialog();
			handledMethod.invoke(args[0],new Object[]{Boolean.TRUE});			
		} else if (method.equals(quitMethod)) {
			shutdown.reallyHalt(); // no way of stopping it, but at least we can notify folk.
			handledMethod.invoke(args[0],new Object[]{Boolean.TRUE});
		}
		return null;		
	}

}
