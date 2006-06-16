package org.astrogrid.desktop.modules.dialogs.editors;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.desktop.modules.ag.CommunityInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * A panel to control whether calls to services are authenticated.
 * This panel is designed to be used with the CompositeToolEditorPanel.
 *
 * The user is asked to whether they want to identify themselves to
 * application servers and shown whether they are logged in. If they
 * choose to identify themselves and are not logged in, then a
 * callback on the control forces them to log in. If the user
 * logs in or out while this panel is showing, then callbacks update
 * the diplay to reflect their status.
 * 
 * @author Guy Rixon
 */
public class SecurityToolEditorPanel 
    extends AbstractToolEditorPanel 
    implements UserLoginListener, ItemListener {
  
  private CommunityInternal community;
  
  /**
   * A GUI label that shows whether the user is logged in, and,
   * if so, as whom they are logged in. The text is set in
   * the constructor and by callbacks as the user logs in
   * and out.
   */
  private JLabel loggedInDisplay;

  /**
   * A GUI label that shows whether the user has the credentials
   * needed for digital signature. The text is set in
   * the constructor and by callbacks as the user logs in
   * and out.
   */
  private JLabel accreditedDisplay; 
  
  /**
   * The control enabling authentication.
   * When this control is set to return true, credentials
   * get added to the tool document and invocations of the
   * application service are authenticated.
   */
  private JCheckBox securityEnablerControl;
  
  /** 
   * Constructs a new instance of SecurityToolEditorPanel.
   */
  public SecurityToolEditorPanel(ToolModel toolModel, 
                                 CommunityInternal community) {
    super(toolModel);
    this.community = community;
    this.loggedInDisplay = new JLabel();
    this.accreditedDisplay = new JLabel();
    this.securityEnablerControl = new JCheckBox("Identify yourself to the application server?");
    
    // We want to be called back when the user logs in or out,
    // so as to update the display.
    this.community.addUserLoginListener(this);
    
    // We want to be called back when the user decides whether or not to
    // authenticate, as we may need to log the user in at that point.
    this.securityEnablerControl.addItemListener(this);
    
    // Set the initial state of the display.
    this.securityEnablerControl.setSelected(false);
    if (community.isLoggedIn()) {
      this.userLogin(null);
    }
    else {
      this.userLogout(null);
    }
    
    // Lay out the visible controls.
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(this.securityEnablerControl);
    this.add(this.loggedInDisplay);
    this.add(this.accreditedDisplay);
  }

  /**
   * abstract method - retuns true if this tool editor is happy to edit this kind of tool 
   * @param t the tool to edit (may be null to indicate no tool selected )
   * @param info description of this tool.
   * 
   * @return true if the editor is happy to handle this kind of tool
   * 
   */
  public boolean isApplicable(Tool t, org.astrogrid.acr.astrogrid.ApplicationInformation info) {
    return (t != null); // Security works for all CEA applications.
  }

  /**
   * Notification that the user has logged into the ACR.
   * This is called by the constructor (with a null event) and
   * by the framework if the user later logs in.
   */
  public void userLogin(org.astrogrid.acr.astrogrid.UserLoginEvent e) {
    if(community.isLoggedIn()) {
      String message1 = "You are logged in as " +
                        community.getUserInformation().getName() +
                        " of community " +
                        community.getUserInformation().getCommunity() +
                        ".";
      this.loggedInDisplay.setText(message1);
      String s = this.hasDigitalSignatureCredentials()? "has " : "has not ";
      String message2 = "Your community " +
                        s +
                        "provided credentials to authenticate " +
                        "to the server.";
      this.accreditedDisplay.setText(message2);
    }
  }

  /**
   * Notification that the user has logged out of the ACR.
   * This is called by the constructor (with a null event) and
   * by the framework if the user later logs out.
   */
  public void userLogout(org.astrogrid.acr.astrogrid.UserLoginEvent e) {
    if (!community.isLoggedIn()) {
      this.loggedInDisplay.setText("You are not logged in.");
      this.accreditedDisplay.setText("");
      this.securityEnablerControl.setSelected(false);
      this.toolModel.setSecurityMethod(null);
    }
  }
  
  /**
   * Notifies that the state of the security-enabling control has changed.
   * If it changes to "selected", logs the user in.
   */
  public void itemStateChanged(ItemEvent e) {
    if (e.getSource() == this.securityEnablerControl && 
        e.getStateChange() == ItemEvent.SELECTED) {
      community.guiLogin();
      if (community.isLoggedIn() && this.hasDigitalSignatureCredentials()) {
        this.toolModel.setSecurityMethod("IVOA digital signature");
      }
      else {
        this.securityEnablerControl.setSelected(false);
        this.toolModel.setSecurityMethod(null);
      }
    }
    else {
      this.toolModel.setSecurityMethod(null);
    }
  }
  
  /**
   * Checks that the user has credentials for digital signature
   * of requests to services.
   *
   * @return - true iff correct credentials are held.
   */
  private boolean hasDigitalSignatureCredentials() {
    try {
      SecurityGuard g = this.community.getSecurityGuard();
      g.getCertificateChain(); // throws if credentials are absent
      g.getPrivateKey(); // throws if credentials are absent
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }
  
}
