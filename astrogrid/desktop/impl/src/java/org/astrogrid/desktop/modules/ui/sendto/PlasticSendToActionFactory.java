/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.Component;
import java.awt.Image;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.system.ReportingListModel.ReportingListDataListener;
import org.astrogrid.desktop.modules.ui.scope.SpectrumLoadPlasticButton;
import org.votech.plastic.CommonMessageConstants;

/** Factory for actions based on plastic applications.
 * @author Noel Winstanley
 * 
 * @since Jun 22, 20061:37:23 AM
 */
public class PlasticSendToActionFactory implements SendToActionFactory, ReportingListDataListener {

	public PlasticSendToActionFactory(Myspace vos,TupperwareInternal tupp) {
		this.tupp = tupp;
		this.vos = vos;
		
	}
	
	protected final TupperwareInternal tupp;
	protected final Myspace vos;
	protected JPopupMenu m;
	protected JSeparator startingPoint; // position may vary.
	
	/** this method needs to be called to fully configure the factory. */
	public void setMenu(JPopupMenu m, JSeparator startingPoint) {
		this.m = m;
		this.startingPoint = startingPoint;
		// start listening now.
		tupp.getRegisteredApplicationsModel().addListDataListener(this);
		// fire an 'all change' message to get things started.
		this.contentsChanged(null);
	}
	
	/** returns index of {@link #startingPoint} + 1 */
	private int findStartPoint() {
		return  m.getComponentIndex(startingPoint) + 1;
	}

	// reporting list data listener interface.
	public void objectsRemoved(Object[] obj) {
		for (int i =0; i < obj.length; i++) {
			removePlasticApp((PlasticApplicationDescription)obj[i]);
		}
	}

	/**
	 * @param description
	 */
	private void removePlasticApp(PlasticApplicationDescription description) {
		Component[] comp = m.getComponents();
		for (int i = 0; i < comp.length; i++) {
			if (comp[i].equals(description)) { // works as we've overridden equals.
				m.remove(i);
			}
		}
	}

	public void contentsChanged(ListDataEvent ignored) {
		// clear and rebuild.
		clearMyObjects();
		ListModel lm = tupp.getRegisteredApplicationsModel();
		for (int i = 0; i < lm.getSize(); i++) {
			PlasticApplicationDescription plas = (PlasticApplicationDescription)lm.getElementAt(i);
			addPlasticApp(plas);
		}
	}

	private void clearMyObjects() {
		Component[] comp = m.getComponents();
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof PlasticAppSubMenu) {
				m.remove(i);
			}
		}
	}

	public void intervalAdded(ListDataEvent e) {
		ListModel lm = tupp.getRegisteredApplicationsModel();
		for (int i = e.getIndex0(); i<= e.getIndex1(); i++) {
			PlasticApplicationDescription plas = (PlasticApplicationDescription)lm.getElementAt(i);
			addPlasticApp(plas);
		}		
	}

	/**
	 * @param plas
	 */
	private void addPlasticApp(PlasticApplicationDescription plas) {
		JMenu sub = new PlasticAppSubMenu(plas);
		if (sub.getItemCount() > 0) { // if the menu is empty, don't bother adding it.
		int pos = findStartPoint();
		while (pos < m.getComponentCount() && m.getComponent(pos) instanceof PlasticAppSubMenu) {
			pos++;
		}
		m.insert(sub,pos);
		
		}
	}

	public void intervalRemoved(ListDataEvent arg0) {
		// do nothing.
	}
	
	/** custom menu that models the functionality of a menu */
	class PlasticAppSubMenu extends JMenu {
		public PlasticAppSubMenu(PlasticApplicationDescription desc) {
			this.desc = desc;
			this.setText("To " + StringUtils.capitalize(desc.getName()));
			if (desc.getIcon() != null) {
                ImageIcon scaled = new ImageIcon(((ImageIcon)desc.getIcon()).getImage().getScaledInstance(-1,20,Image.SCALE_SMOOTH));
				this.setIcon(scaled);
			} else {
				this.setIcon(IconHelper.loadIcon("plasticeye.gif"));
			}
			
			// slightly dodgy  - but we'll assume no app would implement one of these, but not both.
			if (desc.understandsMessage(CommonMessageConstants.VOTABLE_LOAD) 
					|| desc.understandsMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL)) {
				this.add(new SendPlasticVotableSTA(desc.getId(),vos,tupp));
			}
			if (desc.understandsMessage(CommonMessageConstants.FITS_LOAD_FROM_URL)) {
				this.add(new SendPlasticFitsSTA(desc.getId(),tupp));
			}
			if (desc.understandsMessage(SpectrumLoadPlasticButton.SPECTRA_LOAD_FROM_URL)){
				this.add(new SendPlasticSpectrumSTA(desc.getId(),tupp));
			}
			//@future add code to disable menu when all items on the menu are disabled.
		}
		private final PlasticApplicationDescription desc;
		
		//necessary for all sub-menus to override, so we can get instances of action back again.
		protected JMenuItem createActionComponent(Action a) {
			return new SendToMenuImpl.ActionJMenuItem(a);
		}
		
		public boolean equals(Object obj) {
			if (obj == null || ! ((obj instanceof PlasticAppSubMenu) || (obj instanceof PlasticApplicationDescription))) {
				return false;
			}
			if (obj instanceof PlasticAppSubMenu) {
				return this.desc.equals(((PlasticAppSubMenu)obj).desc);
			} else {
				return this.desc.equals((PlasticApplicationDescription)obj);
			}
		}
	}
	

}
