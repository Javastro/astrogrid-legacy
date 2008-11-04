/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** Configuraiton and UI enhancements only available on OSX platform.
 * 
 * as we dunno whether these classes are goiong to be available, have to go through
 * same rigmarole as with javaws - i.e. call methods using reflection.
 * Even though we can tell hivemind not to include this class when not running on
 * mac, still needs to be written in a reflective kind of way - otherwise devs on
 * other OS would be unable to compile or build.
 * 
 * @future add the hooks to handle 'file open' events.
 * @author Noel Winstanley
 * @since Jun 21, 200611:31:12 PM
 */
public class MacSetup implements InvocationHandler {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(MacSetup.class);
	
	public MacSetup(final UIContext ui) throws ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		this.ui = ui;
		final Class applicationClass = Class.forName("com.apple.eawt.Application");
		final Class listenerClass = Class.forName("com.apple.eawt.ApplicationListener");
		final Class eventClass= Class.forName("com.apple.eawt.ApplicationEvent");
		handledMethod = ReflectionHelper.getMethodByName(eventClass,"setHandled");
		
		application = ReflectionHelper.callStatic(applicationClass,"getApplication");
		ReflectionHelper.call(application,"setEnabledAboutMenu",true);
		ReflectionHelper.call(application,"setEnabledPreferencesMenu",true);	
        
		final Object listener = Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{listenerClass}, this);
        ReflectionHelper.call(application,"addApplicationListener",listener);		
		
		// finally, set up the methods we're to be listening for..
		aboutMethod = ReflectionHelper.getMethodByName(listenerClass,"handleAbout");
		preferencesMethod = ReflectionHelper.getMethodByName(listenerClass,"handlePreferences");
		quitMethod = ReflectionHelper.getMethodByName(listenerClass,"handleQuit");
		reopenMethod = ReflectionHelper.getMethodByName(listenerClass,"handleReOpenApplication");
		logger.info("Configured for OSX");
	}
	
	protected final Object application;
	protected final Method aboutMethod;
	protected final Method preferencesMethod;
	protected final Method quitMethod;
	protected final Method reopenMethod;
	protected final UIContext ui;
	protected final Method handledMethod;

	/** handles calls to the menu */
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		if (method.equals(aboutMethod)) {
		    ui.actionPerformed(new ActionEvent(this,0,UIContext.ABOUT));
			handledMethod.invoke(args[0],true);			
		} else if (method.equals(preferencesMethod)) {
		    ui.actionPerformed(new ActionEvent(this,0,UIContext.PREF));
			handledMethod.invoke(args[0],true);			
		} else if (method.equals(quitMethod)) {
	        // Workaround for 2868805:  show modal dialogs in a separate thread.
	        // This encapsulation is not necessary in 10.2, 
	        // but will not break either.
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                ui.actionPerformed(new ActionEvent(this,0,UIContext.EXIT));	            
	            }
	        });

	        // Throw IllegalStateException so new thread can execute.  
	        // If showing dialog on this thread in 10.2, we would throw
	        // upon JOptionPane.NO_OPTION
	        throw new IllegalStateException("Quit Pending User Confirmation");
		    
		//	shutdown.reallyHalt(); // no way of stopping it, but at least we can notify folk.
		//	handledMethod.invoke(args[0],new Object[]{Boolean.TRUE});
		} else if (method.equals(reopenMethod)) {
			ui.show(); // opens all ui.
			handledMethod.invoke(args[0],true);
		}
		return null;		
	}

}
