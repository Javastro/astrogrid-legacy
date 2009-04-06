/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;

/** Factory that builds instances of astroscope.
 * used to enable more than one instance under hivemind.
 * also can be used later to pre-fetch shared resources (e.g. registry queries)
 * and as a point to hang 'SendTo' actions,  Tupperware processors, and AR methods
 * that imply creating a new instance of astroscope.
 * @author Noel Winstanley
 * @since Jun 21, 20066:49:30 PM
 */
public class AstroScopeFactory implements AstroScopeInternal{

	public AstroScopeFactory(final TypesafeObjectBuilder builder) {
		this.builder = builder;
	}
	private final TypesafeObjectBuilder builder;
	public void show() {
		newWindow();
	}
// astrscope intetnal interface.
	public void runSubset(final List<? extends Resource> resources) {
		final AstroScopeInternal	i = newWindow();
		i.runSubset(resources) ;
	}
	   public void runSubsetAsHelioscope(final List<? extends Resource> resources) {
	        final AstroScopeInternal  i = newWindow();
	        i.runSubsetAsHelioscope(resources) ;
	    }
// factory interface
	public Object create() {
		return newWindow();
	}
	
	private AstroScopeLauncherImpl newWindow() {
		final AstroScopeLauncherImpl i = builder.createAstroscope();
		i.show();
		return i;
	}

}
