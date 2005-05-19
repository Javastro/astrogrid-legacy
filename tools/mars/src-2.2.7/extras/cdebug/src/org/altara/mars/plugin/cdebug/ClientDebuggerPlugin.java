/* MARS Network Monitoring Engine Debugger
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2004 Leapfrog Research & Development, LLC

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, it is available at 
	http:///www.gnu.org/copyleft/gpl.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place - Suite 330,
	Boston, MA  02111-1307, USA.
*/

package org.altara.mars.plugin.cdebug;

import org.jdom.*;
import org.altara.util.*;
import org.altara.mars.*;
import org.altara.mars.engine.*;
import org.altara.mars.plugin.*;
import org.altara.mars.swingui.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ClientDebuggerPlugin implements ClientDebuggerFactory, Plugin, 
                                             Enableable, Displayable {
    boolean         enabled;
    int             horizon;
    SessionList     sessions;
    PluginDisplay   display;

    private static int DEFAULT_HORIZON = 20;
    
    /* Constructor */
    public ClientDebuggerPlugin() {
        /* disabled by default */
        enabled = false;

        /* set default horizon */
        horizon = DEFAULT_HORIZON;
    }

    private void enableDebugger() {
        if (enabled) return;

        /* set enabled flag */
        enabled = true;

        /* create new session list */
        sessions = new SessionList(horizon);

        /* set display component */
        if (display != null) {
            display.setComponent(new ClientDebuggerDisplay(sessions));
            display.show();
        }

        /* register with the engine's Debug proxy */
        Debug.getCurrent().setClientDebuggerFactory(this);
    }

    private void disableDebugger() {
        if (!enabled) return;

        /* clear registration with engine's Debug proxy */
        Debug.getCurrent().clearClientDebuggerFactory();

        /* hide display and lose reference to component */
        display.hide();
        display.setComponent(null);

        /* lose session list */
        sessions = null;

        /* clear enabled flag */
        enabled = false;
    }


    private void setHorizon(int horizon) {
        this.horizon = horizon;
        if (enabled) sessions.setHorizon(horizon);
    }

    /* ClientDebuggerFactory implementation. Creates new Sessions. */

    public ClientDebugger newDebugger(String name) {
        return sessions.newSession(name);
    }

    /* Plugin implementation (and associated) */

	public Element getConfig() {
        Element out = new Element(getElementName(),MarsModel.NAMESPACE);
        out.setAttribute("enabled",String.valueOf(enabled));
        out.setAttribute("horizon",String.valueOf(horizon));
        return out;
    }

	public void setConfig(Element in) throws Exception {
        String horizonStr = in.getAttributeValue("horizon");
		if (horizonStr == null)
			throw new InvalidDocumentException("Missing debugger horizon");
        setHorizon(Integer.valueOf(horizonStr).intValue());

        String enabledStr = in.getAttributeValue("enabled");            
		if (enabledStr == null)
			throw new InvalidDocumentException("Missing debugger enabled");
		if (Boolean.valueOf(enabledStr).booleanValue()) 
            enableDebugger();
        else
            disableDebugger(); 
    }

	public String getElementName() {
        return "clientdebug";
    }

	public String getDisplayName() {
        return "Client Debugger";
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public Editor getEditor() {
        return new ClientDebuggerEditor();
    }

    public void setPluginDisplay(PluginDisplay display) {
        this.display = display;
        display.setTitle("Debug");
        if (enabled) {
            display.setComponent(new ClientDebuggerDisplay(sessions));
            display.show();
        }
    }

    /* Editor */

    private class ClientDebuggerEditor extends JPanel implements Editor {  

        private JCheckBox   enabledBox;
        private JSlider     horizonSlider;

        private ClientDebuggerEditor() {
            // create fields
            enabledBox = new JCheckBox("Enabled",enabled);

            horizonSlider = new JSlider(JSlider.HORIZONTAL,10,100,horizon);
            horizonSlider.setMajorTickSpacing(10);
            horizonSlider.setPaintTicks(true);
            Hashtable horizonLabels = new Hashtable();
            horizonLabels.put(new Integer(10),new JLabel("10"));
            horizonLabels.put(new Integer(20),new JLabel("20"));
            horizonLabels.put(new Integer(50),new JLabel("50"));
            horizonLabels.put(new Integer(100),new JLabel("100"));
            horizonSlider.setLabelTable(horizonLabels);
            horizonSlider.setPaintLabels(true);

            // set enabled state properly
            horizonSlider.setEnabled(enabled);
            
            enabledBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        horizonSlider.setEnabled(enabledBox.isSelected());
                    }
                });

            // set up the layout manager
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = c.NORTHWEST; c.fill = c.BOTH;
			c.weightx = 1.0; c.weighty = 0.0;
			c.insets = new Insets(4,4,6,4);
	
			// lay out the panel
			c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
			add(enabledBox,c);
            c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(new JLabel("Horizon"),c);
			c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
			add(horizonSlider,c);
			c.gridx = 1; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
			JLabel horizonHelp =
				new JLabel("Number of inactive sessions to display.");
			horizonHelp.setFont(
                horizonHelp.getFont().deriveFont(Font.PLAIN,9));
			add(horizonHelp,c);
        }

        public void commit() {
            setHorizon(horizonSlider.getValue());
            if (enabledBox.isSelected()) 
                enableDebugger(); 
            else 
                disableDebugger();
        }

        public String getEditorTitle() {
            return getDisplayName();
        }
    }

    /* Display */

    private class ClientDebuggerDisplay extends JPanel {
        private ClientDebuggerDisplay(SessionList sessions) {
            /* create the session list */
            final JList sl = new JList(sessions);
            /* make it pretty */
            sl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            sl.setFont(MarsAbstractRenderer.cellFont);
            JScrollPane slsp = new JScrollPane(sl);
            slsp.setBorder(new TitledBorder("Debug Sessions"));
            
            /* create the session display */
            final EmptyTableModel etm = new EmptyTableModel();
            final JTable st = new JTable(etm);
            /* make it pretty */
            st.getColumnModel().getColumn(0).setResizable(false);
            st.getColumnModel().getColumn(0).setMaxWidth(80);
            st.getColumnModel().getColumn(1).setResizable(false);
            st.getColumnModel().getColumn(1).setMaxWidth(20);
            st.setAutoCreateColumnsFromModel(false);
            st.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            st.setFont(MarsAbstractRenderer.cellFont);
            JScrollPane stsp = new JScrollPane(st);
            stsp.setBorder(new TitledBorder("Selected Session"));

            /* hook the two together */
            sl.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent lse) {
                        if (sl.getSelectedIndex() >= sl.getModel().getSize()) {
                            sl.clearSelection();
                        } else {
                            Session session = ((Session)sl.getSelectedValue());
                            if (session == null) {
                                st.setModel(etm);
                            } else {    
                                st.setModel(session);
                            }
                        }
                    }
                });

            /* display them in a split pane */
            setLayout(new BorderLayout());
            JSplitPane splitPane = 
                new JSplitPane(JSplitPane.VERTICAL_SPLIT,slsp,stsp);
            add(splitPane,BorderLayout.CENTER);
            splitPane.setDividerLocation(0.8);
            splitPane.setResizeWeight(0.3);
        }
    }

    /* Machinery */

    private static class EmptyTableModel extends AbstractTableModel {
        
        private EmptyTableModel() {
        }

        public int getColumnCount() {
            return 3;
        }

        public String getColumnName(int col) {
            if (col == 0) { return "Time"; }
            else if (col == 1) { return ""; }
            else { return "Message"; }
        }
        
        public Class getColumnClass(int col) {
            if (col == 0) { return String.class; }
            else if (col == 1) { return Icon.class; }
            else { return String.class; }
        }

        public int getRowCount() {
            return 0;
        }

        public Object getValueAt(int row, int col) {
                return "";
        }
    }    
}