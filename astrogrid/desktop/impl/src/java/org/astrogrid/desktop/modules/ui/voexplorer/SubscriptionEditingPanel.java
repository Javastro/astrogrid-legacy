package org.astrogrid.desktop.modules.ui.voexplorer;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;

/**
 * Allows editing of ResourceFolder subscriptions.  Note that subscription
 * is not a particular type of ResourceFolder, any ResourceFolder may
 * have a subscription atttribute.
 *
 * @author   Mark Taylor
 * @since    19 Oct 2007
 */
public class SubscriptionEditingPanel extends EditingPanel {

    private final JTextField nameField;
    private final JTextField urlField;
    private final Action browseAction;

    /**
     * Constructor.
     *
     * @param  chooser  chooser for browsing subscription locations
     */
    public SubscriptionEditingPanel(final ResourceChooserInternal chooser) {
        CSH.setHelpIDString(this,"reg.edit.subscription");
        nameField = new JTextField();
        urlField = new JTextField();
        urlField.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent evt) {
                ok.setEnabled(shouldOkBeEnabled());
            }
        });
        browseAction = new AbstractAction("Browse") {
            public void actionPerformed(ActionEvent evt) {
                URI loc = chooser.chooseResourceWithParent(
                    "Locate Subscription", true, true, true,
                    SubscriptionEditingPanel.this);
                urlField.setText(loc == null ? "" : loc.toString());
            }
        };

        FormLayout layout = new FormLayout(
            "2dlu, right:pref, 4dlu, max(30dlu;default):grow, default, 1dlu, default, 3dlu",
            "default, 2dlu, default, 1dlu, default, 2dlu:grow, default"
        );

        PanelBuilder builder = new PanelBuilder(layout, this);
        CellConstraints cc = new CellConstraints();

        int row = 1;
        builder.addLabel("The list named:", cc.xy(2, row));
        builder.add(nameField, cc.xy(4, row));
        row += 2;
        builder.addLabel("Loads resource description from:", cc.xy(2, row));
        builder.add(urlField, cc.xyw(4, row, 4));
        row += 2;
        builder.add(new JButton(browseAction), cc.xy(7, row));
        row += 2;
        builder.add(ok, cc.xy(5, row));
        builder.add(cancel, cc.xy(7, row));
    }

    public void setCurrentlyEditing(ResourceFolder folder) {
        super.setCurrentlyEditing(folder);
        nameField.setText(folder.getName());
        urlField.setText(folder.getSubscription());
    }

    public void loadEdits() {
        super.loadEdits();
        ResourceFolder folder = getCurrentlyEditing();
        folder.setName(nameField.getText());
        folder.setSubscription(urlField.getText());
    }

    public boolean shouldOkBeEnabled() {
        if (!super.shouldOkBeEnabled()) {
            return false;
        }
        String url = urlField.getText();
        if (url == null || url.trim().length() == 0) {
            return false;
        }
        try {
            new URL(url);
            return true;
        }
        catch (MalformedURLException e) {
            return false;
        }
    }
}
