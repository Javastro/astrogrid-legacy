/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URISyntaxException;
import java.util.List;

import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ui.AstroScope;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** Factory that builds instances of astroscope.
 * used to enable more than one instance under hivemind.
 * also can be used later to pre-fetch shared resources (e.g. registry queries)
 * and as a point to hang 'SendTo' actions,  Tupperware processors, and AR methods
 * that imply creating a new instance of astroscope.
 * @author Noel Winstanley
 * @since Jun 21, 20066:49:30 PM
 */
public class AstroScopeFactory implements AstroScopeInternal{

	public AstroScopeFactory(ObjectBuilder builder) {
		this.builder = builder;
	}
	private final ObjectBuilder builder;
	public void show() {
		newWindow();
	}
// astrscope intetnal interface.
	public void runSubset(List resources) {
		AstroScopeInternal	i = newWindow();
		i.runSubset(resources) ;
		//i.show();
	}
// factory interface
	public Object create() {
		return newWindow();
	}
	
	private AstroScopeLauncherImpl newWindow() {
		AstroScopeLauncherImpl	i = (AstroScopeLauncherImpl)builder.create("astroscope");
		i.show();
		return i;
	}

}
