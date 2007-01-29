/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** Button that loads a spectrum over plastic.
 * @author Noel Winstanley
 * @since Sep 14, 200611:42:44 AM
 */
public class SpectrumLoadPlasticButton extends PlasticButton {

	public static final URI SPECTRA_LOAD_FROM_URL;
	static {
		try {
			SPECTRA_LOAD_FROM_URL= new URI("ivo://votech.org/spectrum/loadFromURL");
		} catch (URISyntaxException x) {
			throw new RuntimeException("Failed to construct URI",x);
		}
	}

	/**
	 * @param descr
	 * @param name
	 * @param description
	 * @param selectedNodes
	 * @param ui
	 * @param tupp
	 */
	public SpectrumLoadPlasticButton(PlasticApplicationDescription descr,  FocusSet selectedNodes, UIComponent ui, TupperwareInternal tupp) {
		super(descr, "View Spectra in " + StringUtils.capitalize(descr.getName()), 
				descr.getDescription(), selectedNodes, ui, tupp);
	}


    public void focusChanged(FocusEvent arg0) {
        for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
            TreeNode t= (TreeNode)i.next();
            if (t.getAttribute(SsapRetrieval.SPECTRA_URL_ATTRIBUTE) != null) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }

	public void actionPerformed(ActionEvent arg0) {
		   (new BackgroundWorker(ui,super.getText()) {        
	            protected Object construct() throws Exception {        
	                for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
	                    final TreeNode tn = (TreeNode)i.next();
	                    // find each leaf node
	                    if (tn.getChildCount() > 0 ) {
	                        continue;
	                    }
	                        URL url = new URL(tn.getAttribute(SsapRetrieval.SPECTRA_URL_ATTRIBUTE));
	                        List args = new ArrayList();
	                        args.add(url.toString()); // plastic expects a string, but we first construct a url to ensure it's valid.
	                        args.add(url.toString()); // use this as the identifier too.
	                        args.add(tn.getAttributes());
	                        tupperware.singleTargetPlasticMessage(
	                                SPECTRA_LOAD_FROM_URL
	                                ,args,targetId);                 
	                }// end for each child node.
	                return null;
	            }
	        }).start();		
	}

}
