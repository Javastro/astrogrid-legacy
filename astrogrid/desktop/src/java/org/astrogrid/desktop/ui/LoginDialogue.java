/*$Id: LoginDialogue.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.tree.TreeClient;
import org.astrogrid.store.tree.TreeClientException;
import org.astrogrid.store.tree.TreeClientFactory;

/**
 * Dialog for logging in to an Astrogrid community.
 * The most useful method is {@link #openTreeClientDialog}, which 
 * can be used on its own to acquire a logged-in 
 * {@link org.astrogrid.store.tree.TreeClient}.
 *
 * @author   Mark Taylor (Starlink)
 * @since    25 Nov 2004
 * @modified Noel Winstanley - copied across.
 */
public class LoginDialogue extends JPanel {

    private final JTextField commField_;
    private final JTextField userField_;
    private final JPasswordField passField_;
    private final JOptionPane opane_;

    /**
     * Constructs a new dialog.
     */
    public LoginDialogue() {

        /* Create query components. */
        commField_ = new JTextField();
        userField_ = new JTextField();
        passField_ = new JPasswordField();

        /* Arrange query components. */
        Stack stack = new Stack();
        stack.addItem( "Community", commField_ );
        stack.addItem( "User", userField_ );
        stack.addItem( "Password", passField_ );
        Component strut = Box.createHorizontalStrut( 300 );


        /* The main panel will be a JOptionPane since it has icons and
         * layout set up for free.  We won't be using its various
         * static show methods though. */
        opane_ = new JOptionPane();
        opane_.setMessageType( JOptionPane.QUESTION_MESSAGE );
        opane_.setOptionType( JOptionPane.OK_CANCEL_OPTION );
        opane_.setMessage( new Component[] { stack, strut } );
        prefs = Preferences.userNodeForPackage(LoginDialogue.class);
        userField_.setText(prefs.get("username",""));
        commField_.setText(prefs.get("community",""));
    }

    protected void save() {
        prefs.put("community",getCommunity());
        prefs.put("username",getUser());
    }
    
    protected final Preferences prefs;
    
    /**
     * Sets the content of the community text entry field.
     *
     * @param  comm  community identifier
     */
    public void setCommunity( String comm ) {
        prefs.put("community",comm);
        commField_.setText( comm );
    }

    /**
     * Returns the content of the community text entry field.
     *
     * @return  community identifier
     */
    public String getCommunity() {
        return commField_.getText();
    }

    /**
     * Sets the content of the user text entry field.
     *
     * @param   user  user identifier
     */
    public void setUser( String user ) {
        prefs.put("username",user);
        userField_.setText( user );
    }

    /**
     * Returns the content of the user text entry field.
     * 
     * @return  user identifier
     */
    public String getUser() {
        return userField_.getText();
    }
    
    public String getPassword() {
        return passField_.getText();
    }
    
    public void setPassword(String password) {
        passField_.setText(password);
    }
    
   

    /**
     * Returns the <code>Ivorn</code> defined by the current entries
     * in this dialogue's fields.
     *
     * @return  ivorn 
     */
    public Ivorn getIvorn() throws CommunityException {
        String community = getCommunity();
        String user = getUser();
        if ( user != null && user.trim().length() > 0 &&
             community != null && community.trim().length() > 0 ) {
            return CommunityAccountIvornFactory
                  .createIvorn( community, user );
        }
        else {
            return null;
        }
    }

  
    /** show the dialog, to get user input. will return 'true' if user confirms, 'false' if user cancels*/
    public boolean showDialog( Component parent ) {
        JDialog dialog = opane_.createDialog( parent, "Astrogrid Login" );
        dialog.getContentPane().add( opane_ );       
            dialog.show();
            Object status = opane_.getValue();            
            dialog.dispose();
            boolean result =   status instanceof Integer &&
                 ((Integer) status).intValue() == JOptionPane.OK_OPTION ;
            if (result) {
                save();
            }
            return result;
    }

    /**
     * Informs the user of an error.
     *
     * @param  parent  parent component
     * @param  message  message text
     */
    public void showError( Component parent, String message ) {
        JOptionPane.showMessageDialog( parent, message, "Login Error", 
                                       JOptionPane.ERROR_MESSAGE );
    }

    /**
     * Helper class for positioning pairs of components in a vertical stack.
     * Insulates the rest of the class from having to deal with the
     * apallingly unfriendly GridBagLayout.
     */
    private static class Stack extends JPanel {
        private final GridBagLayout layer_ = new GridBagLayout();
        private final GridBagConstraints gbc_ = new GridBagConstraints();
        private final JComponent container_ = new JPanel( layer_ );
        Stack() {
            super( new BorderLayout() );
            add( container_ );
            gbc_.gridy = 0;
        }
        void addItem( String text, JComponent comp ) {
            gbc_.gridx = 0;
            gbc_.anchor = GridBagConstraints.EAST;
            gbc_.weightx = 0.0;
            gbc_.fill = GridBagConstraints.NONE;
            JLabel label = new JLabel( text + ": " );
            layer_.setConstraints( label, gbc_ );
            container_.add( label );

            gbc_.gridx = 1;
            gbc_.anchor = GridBagConstraints.WEST;
            gbc_.weightx = 1.0;
            gbc_.fill = GridBagConstraints.HORIZONTAL;
            layer_.setConstraints( comp, gbc_ );
            container_.add( comp );

            gbc_.gridy++;
        }
    }
}


/* 
$Log: LoginDialogue.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/