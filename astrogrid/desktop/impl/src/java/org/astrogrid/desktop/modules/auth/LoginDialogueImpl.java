/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import java.net.URISyntaxException;

import org.astrogrid.acr.astrogrid.UserInformation;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;

/** Miniscule implementation of the Login Dialogue.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 4, 200712:59:18 PM
 */
public class LoginDialogueImpl implements LoginDialogue {
    /**
     * 
     */
    public LoginDialogueImpl(TypesafeObjectBuilder builder) {
        dialogue = builder.createLoginDialogue();
    }
    private final SwingLoginDialogue dialogue;
    public UserInformation show() {
        dialogue.setPassword("");
        if (dialogue.showDialog()) {
            try {
                return LoginFactory.mkUserInfo(dialogue.getCommunity(),dialogue.getUser(),dialogue.getPassword());
            } catch (URISyntaxException x) {
                return null;
            }
        } else {
            return null;
        }
    }

}
