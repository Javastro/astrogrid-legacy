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
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.SystemTrayInternal;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.VOExplorerFactoryImpl;
import org.votech.plastic.CommonMessageConstants;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.CollectionList.Model;
import ca.odell.glazedlists.swing.GlazedListsSwing;

/**
 * A scavenger that monitors the plastic-registered applications, and provides activities suitable for 
 * communicating with each application.
 *  @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20071:29:37 PM
 * @TEST this.
 */
public final class PlasticScavenger extends AbstractActivityScavenger implements Model{

private final SystemTrayInternal systray;

public PlasticScavenger(final EventList apps,final TupperwareInternal tupp, final SystemTrayInternal systray) {
	super("Send to");
	this.tupp = tupp;   
	this.apps = apps;
    this.systray = systray;
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
public List getChildren(final Object sourceValue) {
    final PlasticApplicationDescription plas= (PlasticApplicationDescription)sourceValue;
    final List butts = new ArrayList();
    if (plas.understandsMessage(CommonMessageConstants.VOTABLE_LOAD_FROM_URL)
            || plas.understandsMessage(CommonMessageConstants.VOTABLE_LOAD)) {
        AbstractActivity activity = new PlasticVotableActivity(plas,this);
        activity.setUIParent(uiParent.get());
        butts.add(activity);	
        activity = new PlasticVotableActivity.Fallback(plas,this);
        activity.setUIParent(uiParent.get());
        butts.add(activity);            
    } 
    if (plas.understandsMessage(CommonMessageConstants.FITS_LOAD_FROM_URL)) {
        AbstractActivity activity = new PlasticFitsActivity(plas,this);
        activity.setUIParent(uiParent.get());
        butts.add(activity);
        activity = new PlasticFitsActivity.Fallback(plas,this);
        activity.setUIParent(uiParent.get());
        butts.add(activity);         
    }
    if  (plas.understandsMessage(SPECTRA_LOAD_FROM_URL)) {
        AbstractActivity activity = new PlasticSpectrumActivity(plas,this);
        activity.setUIParent(uiParent.get());
        butts.add(activity);		
        activity = new PlasticSpectrumActivity.Fallback(plas,this);
        activity.setUIParent(uiParent.get());
        butts.add(activity);        
    }		
    if (plas.understandsMessage(VOExplorerFactoryImpl.VORESOURCE_LOAD)
            || plas.understandsMessage(VOExplorerFactoryImpl.VORESOURCE_LOADLIST)) {
        final Activity activity = new PlasticRegistryActivity(plas,this);
        activity.setUIParent(uiParent.get());
        butts.add(activity);
    }
    if (plas.understandsMessage(VOExplorerFactoryImpl.BIBCODE_MESSAGE)) {
        final Activity activity = new PlasticBibcodeActivity(plas,this);
        activity.setUIParent(uiParent.get());
        butts.add(activity);
    }
    return butts;
}

/** configure the name and icon of an activity from a plastic description */
	public static void configureActivity(final String type,final AbstractActivity act,final PlasticApplicationDescription plas) {
		if (plas.getIcon() != null) {
	        final ImageIcon scaled = new ImageIcon((plas.getIcon()).getImage().getScaledInstance(-1,16,Image.SCALE_SMOOTH));
			act.setIcon(scaled);
		} else {
			act.setIcon(IconHelper.loadIcon("plasticeye.gif"));
		}
		final String appName = StringUtils.capitalize(plas.getName());
		act.setText("Send "+  (type == null? "" : type + " ") + "to " + appName);
	    act.setToolTipText("Send selection to " + appName); 
	    /* no point adding descirption - it can be horrendously long, 
	     * and user should know what the app is anyhow - as they've started it, not us.
	            "\n" 
	    		+WordUtils.wrap( plas.getDescription(),40,"\n",true));		
	     */
	}
/**
 * @return the systray
 */
public final SystemTrayInternal getSystray() {
    return this.systray;
}
/**
 * @return the tupp
 */
public final TupperwareInternal getTupp() {
    return this.tupp;
}



}
