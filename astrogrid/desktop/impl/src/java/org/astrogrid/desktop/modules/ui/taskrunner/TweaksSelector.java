/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.TapService;
import org.astrogrid.desktop.modules.system.ProgrammerError;

/** Locates UI tweaks specific to the kind of service we're currently building an invocation for.

 * used to customize the appearance and behaviour of the taskrunner
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 22, 20083:43:11 PM
 */
public class TweaksSelector {
       public ProtocolSpecificTweaks findTweaks(final Resource r) {
           if (r instanceof CeaApplication) {
               return new CeaTweaks((CeaApplication)r);
           } 
           if (r instanceof TapService) {
               return new TapTweaks((TapService)r);
           }
           throw new ProgrammerError("Can't find tweaks for " + r.getId());
       }
}
