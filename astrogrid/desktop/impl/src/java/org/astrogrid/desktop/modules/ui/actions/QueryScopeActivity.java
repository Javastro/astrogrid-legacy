/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.AstroScopeInternal;

/** Invoke astroscope on the selected resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20073:47:48 PM
 */
public final class QueryScopeActivity extends AbstractResourceActivity {
/**
 * 
 */
public QueryScopeActivity(final AstroScopeInternal ai) {
    setHelpID("activity.query");	
	this.ai = ai;
	setText("Position Query");
	setIcon(IconHelper.loadIcon("scope16.png"));
	setToolTipText("Query the selected services(s) by position");
	//setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,UIComponentMenuBar.MENU_KEYMASK));
}

private final AstroScopeInternal ai;


	/** Test whether it's something we can invoke.
	 * @param r
	 */
	@Override
    protected boolean invokable(final Resource r) {
		final boolean b =  r instanceof SiapService 
				|| r instanceof ConeService
				|| r instanceof SsapService
				|| r instanceof StapService
			
					;
		return b;
	}
@Override
public void actionPerformed(final ActionEvent e) {
	final List l = computeInvokable();
	final int sz = l.size();
	confirmWhenOverThreshold(sz,"Query all " + sz + " services?",new Runnable() {

        public void run() {
            ai.runSubset(l);
        }
	});
}


}
