/*$Id: SwingLoginDialogue.java,v 1.18 2008/07/16 15:27:54 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.auth;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIDialogueComponentImpl;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.votech.VoMonInternal;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.JLinkButton;

/**
 * Dialog for logging in to an Astrogrid community. */
public class SwingLoginDialogue extends UIDialogueComponentImpl implements LoginDialogue {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(SwingLoginDialogue.class);

    private final JComboBox commField_;
    private final JTextField userField_;
    private final JPasswordField passField_;
    private final String defaultCommunity;
    private final Preferences prefs;
    private final Community comm;

    private Timer vomonRecheckTimer;
    /**
     * Constructs a new dialog.
     * @throws MalformedURLException 
     * @throws ServiceException 
     */
    public SwingLoginDialogue( final UIContext coxt,final VoMonInternal monitor,final BrowserControl browser, final Registry reg, final Community comm,String defaultCommunity) throws MalformedURLException, ServiceException {
        super(coxt,"Virtual Observatory Login","dialog.login");
        this.comm = comm;
    	this.defaultCommunity = defaultCommunity;
    	final MutableComboBoxModel model = new DefaultComboBoxModel();
    	//retreive a list of communities in a background thread
    	(new BackgroundWorker(this,"Listing known communities",Thread.MAX_PRIORITY) {
    	    {
    	        setTransient(true);
    	    }
    	        
            protected Object construct() throws Exception {
                return reg.xquerySearch(
                    "for $r in //vor:Resource[capability/@standardID='" + 
                    StandardIds.AG_ACCOUNTS +  
                    "' and not (@status='deleted' or @status='inactive')] order by $r/identifier return $r"
                );
            }
            protected void doFinished(Object result) {
                Resource[] knownCommunities = (Resource[])result;

                // Add each community that we get from the registry.
                // But if it has the same name as an existing string value 
                // (probably from the user prefs) remove the string so that
                // the combo box doesn't contain apparent duplicates.
                for (int i = 0; i < knownCommunities.length; i++) {
                    Resource community = knownCommunities[i];
                    String cName = mkCommunityString(community);
                    for (int j = model.getSize() - 1; j >= 0; j--) {
                        Object el = model.getElementAt(j);
                        if (el instanceof String && el.equals(cName)) {
                            model.removeElementAt(j);
                        }
                    }
                    model.addElement(community);
                    if (cName.equals(model.getSelectedItem())) {
                        model.setSelectedItem(community);
                    }
                }
            }
    	}).start();
    	FormLayout fl = new FormLayout("20dlu:grow,right:p,2dlu,100dlu,20dlu:grow","5dlu,p,5dlu,p,2dlu,p,2dlu,p,5dlu,p,5dlu");
    	fl.setColumnGroups(new int[][]{{1,5}});
    	PanelBuilder pb = new PanelBuilder(fl);    	
    	CellConstraints cc = new CellConstraints();
    	commField_ = new JComboBox(model);
    	pb.addTitle("Enter your login details",cc.xyw(2,2,3));
    	pb.addLabel("Community",cc.xy(2,4));
    	pb.add(commField_,cc.xy(4,4));
    	
    	pb.addLabel("User",cc.xy(2,6));
    	userField_ = new JTextField();
    	pb.add(userField_,cc.xy(4,6));
    	
    	pb.addLabel("Password",cc.xy(2,8));
        passField_ = new JPasswordField();
        pb.add(passField_,cc.xy(4,8));


        JButton registerButton = new JLinkButton("Click here to register..");
        registerButton.setToolTipText("Click here to apply for an account on the virtual observatory");
        
        registerButton.addActionListener(new ActionListener() {           
        	public void actionPerformed(ActionEvent e) {
        	    new BackgroundWorker(SwingLoginDialogue.this,"Opening registration page") {

                    protected Object construct() throws Exception {
                        getContext().getHelpServer().showHelpForTarget("dialog.login.register");
                        return null;
                    }
        	    }.start();
        	}
        });
        pb.add(registerButton,cc.xyw(2,10,3));

        JPanel p = pb.getPanel(); // main panel of the form.
        prefs = Preferences.userNodeForPackage(SwingLoginDialogue.class);
        userField_.setText(prefs.get("username",""));
        primSetCommunity();
        commField_.setEditable(false); // don't allow manual edits.
        commField_.setRenderer(new BasicComboBoxRenderer() {
   
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
		
				if (value instanceof Resource) {
					Resource r = (Resource) value;
					String s = mkCommunityString(r); //@future when registry moves to v1.0, add in more data display here
					setText(s);
					setIcon(monitor.suggestIconFor(r));
					setToolTipText(monitor.getTooltipInformationFor(r));
				} else {
					setText(value.toString());
				}
				return this;
			}
        });
        if (! isVomonPopulated(monitor, model)) {
            vomonRecheckTimer = new Timer(5000,new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (isVomonPopulated(monitor,model)) { // now loaded, so wiggle the selection and then stop the timer.
                        Object o = model.getSelectedItem();
                        model.setSelectedItem(null);
                        model.setSelectedItem(o);
                        vomonRecheckTimer.stop();
                    } 
                }
            });
            vomonRecheckTimer.setRepeats(true);
            vomonRecheckTimer.start();
        }
        // configure the dialogue.
        JPanel mainPanel = getMainPanel();
        mainPanel.add(p,BorderLayout.CENTER);
        getContentPane().add(mainPanel);
        assist.getPlasticList().setVisible(false); // don't show the plastic list - just clutter in this ui.
        pack();
        centerOnScreen();
    }

    /** test to see whether vomon returns any suggestions for the list of resources.
     * @param monitor
     * @param model
     * @return
     */
    private boolean isVomonPopulated(final VoMonInternal monitor,
            final MutableComboBoxModel model) {
        int sz = model.getSize();
        boolean monitorKnows = false;
        for (int i = 0; i < sz; i++) {
            Object o = model.getElementAt(i);
            if (o instanceof Resource && monitor.suggestIconFor((Resource)o) != null) {
                monitorKnows = true;
                break;
            }
        }
        return monitorKnows;
    }

    //overridden to save inputs as preferences.
    public void ok() {
        
        prefs.put("community",getCommunity());
        prefs.put("username",getUser());
        
        // user pressed ok - so try to login
        new BackgroundWorker(this,"Logging in",BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {

            protected Object construct() throws Exception {
                comm.login(getUser(),getPassword(),getCommunity()); 
                return null;
            }
            protected void doFinished(Object result) {
                setVisible(false); // close the dialogue.
            }
            protected void doError(Throwable ex) {
                showError("Unable to login: " + ExceptionFormatter.formatException(ex,ExceptionFormatter.INNERMOST));
            }

        }.start();
        
    }
    

    private void primSetCommunity() {
    	String community = prefs.get("community",defaultCommunity);
    	// find mactching string in llist.
    	for (int i =0; i < commField_.getItemCount(); i++) {
    		Object r =  commField_.getItemAt(i);
    		if (matches(community,r)) {
    			commField_.setSelectedIndex(i);
    			return;
    		}
    	}
    	// value isn't on the list - need to insert it by hand.
    	commField_.insertItemAt(community,0);
    	commField_.setSelectedIndex(0);
    }
    
    private boolean matches(String communityName, Object r) {
    	if (r instanceof Resource) {
    		return mkCommunityString((Resource) r).equals(communityName);
    	} else {
    	return communityName.equals(r.toString());
    	}
    }

    /**
     * Returns the content of the community text entry field.
     *
     * @return  community identifier
     */
    private String getCommunity() {
    	Object o = commField_.getSelectedItem();
    	if (o instanceof Resource) {
    		return mkCommunityString((Resource)o);
    	} else {
    		return o.toString();
    	}
    }

    private static String mkCommunityString(Resource r) {
    	return r.getId().getAuthority();
    }
 

    /**
     * Returns the content of the user text entry field.
     * 
     * @return  user identifier
     */
    private String getUser() {
        return userField_.getText();
    }
    
    private String getPassword() {
        return passField_.getText();
    }

  
    public void show() {
        super.show();
        passField_.setText("");
        toFront();
        requestFocus();
        passField_.requestFocus();
    }
 

    /** show the login dialogue and prompt for input */
    public void login() {
      ask();
    }


}


/* 
$Log: SwingLoginDialogue.java,v $
Revision 1.18  2008/07/16 15:27:54  nw
merged guy's security refactoring.

Revision 1.17.12.1  2008/07/09 09:55:54  gtr
I changed the capability used in community searches from policy manager to accounts.

Revision 1.17  2008/03/28 13:21:12  nw
converted register to a help link.

Revision 1.16  2008/03/28 13:09:01  nw
help-tagging

Revision 1.15  2008/03/18 08:34:13  nw
Complete - task 308: improve appearance on windows.

Revision 1.14  2008/03/12 11:37:36  nw
Complete - task 270: improve error message when login fails.

Revision 1.13  2008/02/13 17:25:15  mbt
Remove apparent duplicate entries from Community selector - see bug 2561.  Also widen dialogue a bit (community names can be a bit longer than uk.ac.le.star)

Revision 1.12  2008/01/30 08:38:38  nw
Incomplete - task 313: Digest registry upgrade.

RESOLVED - bug 2526: voexdesktop help needs to point to beta.astrogrid.org
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2526
Incomplete - task 314: get login working again.

Revision 1.11  2008/01/21 09:53:58  nw
Incomplete - task 134: Upgrade to reg v1.0

Revision 1.10  2007/11/30 08:33:40  nw
changes to how uidialogues register in the window list.

Revision 1.9  2007/11/26 14:44:45  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.8  2007/11/21 07:55:39  nw
Complete - task 65: Replace modal dialogues

Revision 1.7  2007/11/20 06:01:37  nw
added help id

Revision 1.6  2007/11/12 11:53:17  nw
RESOLVED - bug 2389: When you login, the Leicester community always appears as down
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2389

Revision 1.5  2007/10/22 07:24:20  nw
altered login dialogue to be a full UIComponent.

Revision 1.4  2007/09/04 13:38:37  nw
added debugging for EDT, and adjusted UI to not violate EDT rules.

Revision 1.3  2007/08/02 00:15:29  nw
prettified use of vomon

Revision 1.2  2007/07/12 10:12:00  nw
minor change

Revision 1.1  2007/03/22 19:01:18  nw
added support for sessions and multi-user ar.

Revision 1.8  2007/01/29 16:45:08  nw
cleaned up imports.

Revision 1.7  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.6  2007/01/23 11:48:14  nw
minor.

Revision 1.5  2007/01/09 16:21:32  nw
uses vomon.

Revision 1.4  2006/08/31 21:13:13  nw
added drop-down list of communitiies.

Revision 1.3  2006/08/02 13:27:57  nw
added 'register' link.

Revision 1.2  2006/05/17 23:57:45  nw
documentation improvements.

Revision 1.1  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.30.1  2006/04/14 02:45:01  nw
finished code.
extruded plastic hub.

Revision 1.3  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.2.2.1  2005/11/23 04:54:10  nw
tried to improve dialogue behavour

Revision 1.2  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/01 19:03:10  nw
beta of job monitor

Revision 1.1.2.1  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/
