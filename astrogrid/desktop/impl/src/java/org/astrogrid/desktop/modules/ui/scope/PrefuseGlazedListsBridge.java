/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.util.Iterator;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.actions.Activity;

import ca.odell.glazedlists.EventList;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** A 'bridge' between the selection models used within glazed lists and prefuse.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 4, 20071:11:29 AM
 */
public final class PrefuseGlazedListsBridge implements FocusListener,  ListSelectionListener {
	
	private final FocusSet prefuse;
	private final ScopeServicesList glazed;
	private final EventList glazedSelected;
	private final VizualizationManager viz;
	public PrefuseGlazedListsBridge(VizualizationManager viz, ScopeServicesList glazed) {
		super();
		this.viz = viz;
		this.prefuse = viz.getVizModel().getSelectionFocusSet();
		this.glazed = glazed;
		prefuse.addFocusListener(this);
		glazedSelected = glazed.getCurrentResourceModel().getTogglingSelected();
		glazed.getCurrentResourceModel().addListSelectionListener(this);
	}
	boolean listeningToGlazed = true;
	boolean listeningToPrefuse = true;
	
// prefuse listener interface
	public void focusChanged(FocusEvent arg0) {
		if (! listeningToPrefuse) {
			return;
		}
		//temporarily stop listening, to avoid infinite loops.
		listeningToGlazed = false;
		try {
			switch(arg0.getEventType()) {
			case FocusEvent.FOCUS_ADDED:
				Entity[] added = arg0.getAddedFoci();
				for (int i = 0; i < added.length; i++) {
					if (added[i].getAttribute(Retriever.SERVICE_ID_ATTRIBUTE) != null) {
						// found a service node - add the equivalent resource, and add to glazed side.
						Service s = glazed.findService((TreeNode)added[i]);
						if (s != null && ! glazedSelected.contains(s)) {
							glazedSelected.add(s); 
						}
					}
				}
				break;
			case FocusEvent.FOCUS_REMOVED:
				Entity[] removed = arg0.getRemovedFoci();
				for (int i = 0; i < removed.length; i++) {
					if (removed[i].getAttribute(Retriever.SERVICE_ID_ATTRIBUTE) != null) {
						// found a service node - add the equivalent resource, and add to glazed side.
						Service s = glazed.findService((TreeNode)removed[i]);
						if (s != null && glazedSelected.contains(s)) {
							glazedSelected.remove(s); 
						}
					}
				}				
				break;
			case FocusEvent.FOCUS_SET:
				glazedSelected.clear();
				for (Iterator i = arg0.getFocusSet().iterator(); i.hasNext();) {
					TreeNode t = (TreeNode)i.next();
					if (t.getAttribute(Retriever.SERVICE_ID_ATTRIBUTE) != null) {
						// found a service node - add the equivalent resource, and add to glazed side.
						Service s = glazed.findService(t);
						if (s != null && ! glazedSelected.contains(s)) {
							glazedSelected.add(s); 
						}
					}
				}				
			}
		}finally {
			listeningToGlazed = true;
		}
	}

	// glazed lists listener interface
//@fixme still no quite right - the selection is instantly deselected again. by another event.
	// valueIsAdjusting doesn't help either.
	public void valueChanged(ListSelectionEvent e) {
		if (! listeningToGlazed ) {
			return;
		}
		listeningToPrefuse = false;
		try {
			for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
				Service s = (Service)glazed.getList().get(i);
				if (s != null) {
					TreeNode t = glazed.findTreeNode(s);	
					if (t != null) {
					if (glazed.getCurrentResourceModel().isSelectedIndex(i) && ! prefuse.contains(t)) {
							DoubleClickMultiSelectFocusControl.selectSubtree(t,prefuse);
					} else if (prefuse.contains(t)) {
							DoubleClickMultiSelectFocusControl.deselectSubtree(t,prefuse);
						}					
					}
				}
			}
		} finally {
			listeningToPrefuse = true;
            viz.reDrawGraphs();
		}		
	}


	
}
