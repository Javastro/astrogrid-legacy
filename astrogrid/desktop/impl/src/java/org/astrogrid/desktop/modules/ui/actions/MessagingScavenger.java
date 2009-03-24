/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.SystemTrayInternal;
import org.astrogrid.desktop.modules.system.messaging.BibcodeMessageType;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.FitsImageMessageType;
import org.astrogrid.desktop.modules.system.messaging.Messaging;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.SpectrumMessageType;
import org.astrogrid.desktop.modules.system.messaging.VotableMessageType;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.CollectionList.Model;
import ca.odell.glazedlists.swing.GlazedListsSwing;

/**
 * A scavenger that monitors the plastic-registered applications, and provides activities suitable for 
 * communicating with each application.
 *  @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20071:29:37 PM
 */
public final class MessagingScavenger extends AbstractActivityScavenger 
    implements Model<ExternalMessageTarget,Activity>{

private final SystemTrayInternal systray;
private final Messaging messaging;
private final EventList<ExternalMessageTarget> targetList;

public MessagingScavenger(final Messaging messaging, final SystemTrayInternal systray) {
	super("Send to");
    this.messaging = messaging;
    this.systray = systray;
    this.targetList = messaging.getTargetList();
}

/** we use a collection list - which maps one plastic app into one or more activities 
 * the plastic app list shrinks and grows automatically as new apps connect / disconnect
 * 
 * furthermore, we use a list that only fires update messages on the EDT.
 * */
protected EventList<Activity> createEventList() {
	final TransformedList<ExternalMessageTarget, ExternalMessageTarget> proxy = GlazedListsSwing.swingThreadProxyList(targetList);
    return new CollectionList(proxy,this);
}



protected void loadChildren() {
	 // no need to do anything. - eventlist is already populated.
}
// Model interface implementation- maps a single plastic app to 0 or more activities.
public List<Activity> getChildren(final ExternalMessageTarget target) {
     final List<Activity> butts = new ArrayList<Activity>();
     if (target.accepts(VotableMessageType.instance)) {
        AbstractActivity activity = new PlasticVotableActivity(target);
        activity.setUIParent(uiParent.get());
        butts.add(activity);	
        activity = new PlasticVotableActivity.Fallback(target);
        activity.setUIParent(uiParent.get());
        butts.add(activity);            
    } 
    if (target.accepts(FitsImageMessageType.instance)) {
        AbstractActivity activity = new MessageFitsActivity(target);
        activity.setUIParent(uiParent.get());
        butts.add(activity);
        activity = new MessageFitsActivity.Fallback(target);
        activity.setUIParent(uiParent.get());
        butts.add(activity);         
    }
    if (target.accepts(SpectrumMessageType.instance)) {
        AbstractActivity activity = new MessageSpectrumActivity(target);
        activity.setUIParent(uiParent.get());
        butts.add(activity);		
        activity = new MessageSpectrumActivity.Fallback(target);
        activity.setUIParent(uiParent.get());
        butts.add(activity);        
    }		
    if (target.accepts(ResourceSetMessageType.instance)) {
        final Activity activity = new MessageRegistryActivity(target);
        activity.setUIParent(uiParent.get());
        butts.add(activity);
    }
    if (target.accepts(BibcodeMessageType.instance)) {
        final Activity activity = new MessageBibcodeActivity(target);
        activity.setUIParent(uiParent.get());
        butts.add(activity);
    }
    return butts;
}

/** configure the name and icon of an activity from a plastic description */
	public static void configureActivity(final String type,final AbstractActivity act,final ExternalMessageTarget plas) {
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




}
