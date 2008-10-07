/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Implementation object that backs Resource instances. Only used internally.
 * @author Noel Winstanley
 * @exclude
 */
public class Handler implements InvocationHandler, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1241489182906733435L;
	private final HashMap m;
	public Handler(final HashMap m) {
		super();
		this.m = m;
	}
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final String name = method.getName();
		if (name.equals("toString")) {
			return m.toString();
		} else if (name.equals("equals")) {
			if (! (args[0] instanceof Resource)) {
				return Boolean.FALSE;
			}
			return Boolean.valueOf(
					m.get("getId").equals(((Resource)args[0]).getId())
					);
		} else if (name.equals("hashCode")) {
			//return new Integer(m.get("getId").hashCode());		   
		    return Integer.valueOf(m.get("getId").hashCode());
		} else {
			return m.get(name);
		}
	}
}