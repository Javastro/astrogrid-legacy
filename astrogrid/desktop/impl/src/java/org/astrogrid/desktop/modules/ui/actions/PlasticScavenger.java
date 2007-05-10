/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Image;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.VOExplorerFactoryImpl;
import org.votech.plastic.CommonMessageConstants;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.CollectionList.Model;
import ca.odell.glazedlists.swing.GlazedListsSwing;

/** * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20071:29:37 PM
 */
public final class PlasticScavenger extends AbstractActivityScavenger implements Model{

private static final Log logger = LogFactory.getLog(PlasticScavenger.class);

public PlasticScavenger(EventList apps,TupperwareInternal tupp) {
	super("Plastic Applications");
	this.tupp = tupp;   
	this.apps = apps;
}
private final EventList apps;
private final TupperwareInternal tupp;

/** we use a collection list - which maps one plastic app into one or more activities 
 * the plastic app list shrinks and grows automatically as new apps connect / disconnect
 * 
 * furthermore, we use a list that only fires update messages on the EDT.
 * */
protected EventList createEventList() {
	return new CollectionList(GlazedListsSwing.swingThreadProxyList(apps),this);
}

public static final URI SPECTRA_LOAD_FROM_URL =  URI.create("ivo://votech.org/spectrum/loadFromURL");


protected void loadChildren() {
	 // no need to do anything. - eventlist is already populated.
}
// Model interface implementation- maps a single plastic app to 0 or more activities.
public List getChildren(Object sourceValue) {
	PlasticApplicationDescription plas= (PlasticApplicationDescription)sourceValue;
	List butts = new ArrayList();
		if (plas.understandsMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL)
				|| plas.understandsMessage(CommonMessageConstants.VOTABLE_LOAD)) {
			AbstractActivity activity = new PlasticVotableActivity(plas,tupp);
			activity.setUIParent(uiParent.get());
			butts.add(activity);	
		} 
		if (plas.understandsMessage(CommonMessageConstants.FITS_LOAD_FROM_URL)) {
			AbstractActivity activity = new PlasticFitsActivity(plas,tupp);
			activity.setUIParent(uiParent.get());
			butts.add(activity);
		}
		if  (plas.understandsMessage(SPECTRA_LOAD_FROM_URL)) {
			AbstractActivity activity = new PlasticSpectrumActivity(plas,tupp);
			activity.setUIParent(uiParent.get());
			butts.add(activity);		
			}		
	if (plas.understandsMessage(VOExplorerFactoryImpl.VORESOURCE_LOAD)
			|| plas.understandsMessage(VOExplorerFactoryImpl.VORESOURCE_LOADLIST)) {
		Activity activity = new PlasticRegistryActivity(plas,tupp);
		activity.setUIParent(uiParent.get());
		butts.add(activity);
	}
	return butts;
}

/** configure the name and icon of an activity from a plastic description */
	public static void configureActivity(String type,AbstractActivity act,PlasticApplicationDescription plas) {
		if (plas.getIcon() != null) {
	        ImageIcon scaled = new ImageIcon((plas.getIcon()).getImage().getScaledInstance(-1,16,Image.SCALE_SMOOTH));
			act.setIcon(scaled);
		} else {
			act.setIcon(IconHelper.loadIcon("plasticeye.gif"));
		}
		final String appName = StringUtils.capitalize(plas.getName());
		act.setText("Send "+  (type == null? "" : type + " ") + "to " + appName);
	    act.setToolTipText("<html>Send selection to " + appName + "<br>" 
	    		+WordUtils.wrap( plas.getDescription(),40,"<br>",true));		
	}



}
