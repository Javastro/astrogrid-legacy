/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.service.ObjectProvider;

/**implementation of hte 'missing' object provider.
 * @author Noel Winstanley
 * @since Apr 13, 200612:43:25 PM
 */
public class PrimitiveObjectProvider implements ObjectProvider {

	public Object provideObject(Module module, Class requiredClass, String input,
			Location location) {
		String val = module.expandSymbols(input,location);
		if (requiredClass == Integer.class) {
			return new Integer(Integer.parseInt(val));
		}
		if (requiredClass == Long.class) {
			return new Long(Long.parseLong(val));
		}
		throw new ApplicationRuntimeException("Don't know what to do with " + requiredClass + " " + input);
		
	}

}
