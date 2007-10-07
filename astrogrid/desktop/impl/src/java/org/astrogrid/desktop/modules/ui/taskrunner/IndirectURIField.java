package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.ui.comp.JPromptingTextField;

/** specialized text field that listens to itself and warns about incorrect references 
 * also updates value of pval on change.
 * 
 * future - could improve so that the internal value is a URI - but not urgent, and maybe not worth the bother*/
class IndirectURIField extends JPromptingTextField implements DocumentListener{
	private final ParameterValue pval;

    public IndirectURIField(ParameterValue pval) {
        super("Enter URL");
        this.pval = pval;
        original = getBorder();
        warn = BorderFactory.createLineBorder(Color.RED);
        getDocument().addDocumentListener(this);
    }

    private void update() {
        String s = (String)getValue();
        pval.setIndirect(true); // should be set anyhow.
        pval.setValue(s);
        if (s != null) {
            try {
                URI u = new URI(s);
                if (u.isAbsolute() && ! u.getScheme().equals("file")) {
                    setBorder(original);
                    return;
                }
            } catch (URISyntaxException x) {
                // ok.
            }
        }
        setBorder(warn);
    }
	protected final Border original;
	protected final Border warn;
	// document listener interface
    public void changedUpdate(DocumentEvent e) {
        update();
    }

    public void insertUpdate(DocumentEvent e) {
        update();
    }

    public void removeUpdate(DocumentEvent e) {
        update();
    }


}