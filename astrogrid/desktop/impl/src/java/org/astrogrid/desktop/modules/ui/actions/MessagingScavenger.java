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
import org.astrogrid.desktop.modules.system.messaging.CeaAdqlSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.CeaSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.ConeSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.FitsImageMessageType;
import org.astrogrid.desktop.modules.system.messaging.Messaging;
import org.astrogrid.desktop.modules.system.messaging.ResourceSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.SiapSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.SpectrumMessageType;
import org.astrogrid.desktop.modules.system.messaging.SsapSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.StapSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.TapSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.VospaceSetMessageType;
import org.astrogrid.desktop.modules.system.messaging.VotableMessageType;
import org.astrogrid.desktop.modules.ui.UIComponent;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.CollectionList.Model;
import ca.odell.glazedlists.swing.GlazedListsSwing;

/**
 * A scavenger that monitors the registered applications (SAMP or PLASTIC), and provides activities suitable for 
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
@Override
protected EventList<Activity> createEventList() {
	final TransformedList<ExternalMessageTarget, ExternalMessageTarget> proxy = GlazedListsSwing.swingThreadProxyList(targetList);
    return new CollectionList(proxy,this);
}



@Override
protected void loadChildren() {
	 // no need to do anything. - eventlist is already populated.
}
// Model interface implementation- maps a single plastic app to 0 or more activities.
public List<Activity> getChildren(final ExternalMessageTarget target) {
    
     final List<Activity> butts = new ArrayList<Activity>();
     
     if (target.accepts(VotableMessageType.instance)) {
        butts.add( new PlasticVotableActivity(target));
        butts.add(new PlasticVotableActivity.Fallback(target));
    } 
    if (target.accepts(FitsImageMessageType.instance)) {
        butts.add(new MessageFitsActivity(target));
        butts.add( new MessageFitsActivity.Fallback(target));
    }
    if (target.accepts(SpectrumMessageType.instance)) {
        butts.add(new MessageSpectrumActivity(target));
        butts.add(new MessageSpectrumActivity.Fallback(target));
    }		
    if (target.accepts(ResourceSetMessageType.instance)) {
        butts.add(new MessageResourcesActivity(target));
    }
    if (target.accepts(BibcodeMessageType.instance)) {
        butts.add(new MessageBibcodeActivity(target));
    }
    
    // typed resource set messages 
    if(target.accepts(CeaSetMessageType.instance)) {
        butts.add(new MessageCeaActivity(target));
    }
    if(target.accepts(CeaAdqlSetMessageType.instance)) {
        butts.add(new MessageCeaAdqlActivity(target));
    }
    if(target.accepts(ConeSetMessageType.instance)) {
        butts.add(new MessageConeActivity(target));
    }
    if(target.accepts(SiapSetMessageType.instance)) {
        butts.add(new MessageSiapActivity(target));
    }
    if(target.accepts(SsapSetMessageType.instance)) {
        butts.add(new MessageSsapActivity(target));
    }
    if(target.accepts(StapSetMessageType.instance)) {
        butts.add(new MessageStapActivity(target));
    }
    if(target.accepts(TapSetMessageType.instance)) {
        butts.add(new MessageTapActivity(target));
    }
    if(target.accepts(VospaceSetMessageType.instance)) {
        butts.add(new MessageVospaceActivity(target));
    }
    // ok, now initialize each of the buttons.
    final UIComponent parent = uiParent.get();
    for (final Activity a: butts) {
        a.setUIParent(parent);
        
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
