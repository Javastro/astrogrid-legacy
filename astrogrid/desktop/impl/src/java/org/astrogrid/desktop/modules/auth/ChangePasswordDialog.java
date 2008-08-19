/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIDialogueComponentImpl;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 19, 20086:20:12 PM
 */
public class ChangePasswordDialog extends UIDialogueComponentImpl {



    private final CommunityInternal community;
    private final  JPasswordField p1;
    private final JPasswordField p2;

    /**
     * @param community
     * @param contextImpl
     */
    public ChangePasswordDialog(final CommunityInternal community,
            final UIContext context) {
        super(context,"Change Password","dialog.change.password");
        this.community = community;
        final FormLayout fl = new FormLayout("30dlu:grow,right:d,4dlu,60dlu,30dlu:grow",
                "5dlu,p,5dlu,p,5dlu,p,5dlu");
        final PanelBuilder pb = new PanelBuilder(fl);
        final CellConstraints cc = new CellConstraints();
        
        p1 = new JPasswordField();
        p2 = new JPasswordField();
        // populate the panel builder.

        pb.addTitle("Change password for: " + community.getUserInformation().getName(),cc.xyw(2,2,3));
        pb.addLabel("New password",cc.xy(2,4));
        pb.add(p1,cc.xy(4,4));
        pb.addLabel("Confirm new password",cc.xy(2,6));
        pb.add(p2,cc.xy(4,6));
        
        // assemble all together.
        final JPanel mainPanel = getMainPanel();
        mainPanel.add(pb.getPanel(),BorderLayout.CENTER);
        getContentPane().add(mainPanel);
        assist.getPlasticList().setVisible(false);
        pack();
        centerOnScreen();
    }

    
    @Override
    public void ok() {
        final String newPassword = p1.getText();
        if (newPassword.length() == 0) {
            showTransientError("Empty password","The password cannot be empty");
            return;
        }
        if (! newPassword.equals(p2.getText())) {
            showTransientError("Passwords do not match","The password and password confirmation must be identical");
          return;  
        }
        if (newPassword.equals(community.getUserInformation().getPassword())) {
            showTransientError("No Change","New password is identical to existing password");
            return;
        }
        (new BackgroundWorker(this,"Changing password") {

            @Override
            protected Object construct() throws Exception {
                community.changePassword(newPassword);
                return null;
            }
            @Override
            protected void doFinished(final Object result) {
                setVisible(false);
            }
            @Override
            protected void doError(final Throwable ex) {
                showError(ExceptionFormatter.formatException(ex,ExceptionFormatter.INNERMOST),ex);
            }
        }).start();
    }
}
