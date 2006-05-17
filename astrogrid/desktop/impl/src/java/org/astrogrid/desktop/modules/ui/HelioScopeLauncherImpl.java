/*$Id: HelioScopeLauncherImpl.java,v 1.13 2006/05/17 15:45:17 nw Exp $
 * Created on 12-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.astrogrid.StapInformation;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.HelioScope;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.DalProtocolManager;
import org.astrogrid.desktop.modules.ui.scope.HyperbolicVizualization;
import org.astrogrid.desktop.modules.ui.scope.ImageLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.QueryResultSummarizer;
import org.astrogrid.desktop.modules.ui.scope.SaveNodesButton;
import org.astrogrid.desktop.modules.ui.scope.StapProtocol;
import org.astrogrid.desktop.modules.ui.scope.TemporalDalProtocol;
import org.astrogrid.desktop.modules.ui.scope.VizModel;
import org.astrogrid.desktop.modules.ui.scope.Vizualization;
import org.astrogrid.desktop.modules.ui.scope.VizualizationManager;
import org.astrogrid.desktop.modules.ui.scope.VotableLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.WindowedRadialVizualization;
import org.freixas.jcalendar.JCalendarCombo;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;

import com.l2fprod.common.swing.JButtonBar;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;


/** Implementation of the HelioScope launcher
 * 
 * @todo tidy up scrappy get position code - in particular, report errors from simbad correctly - at moment,
 * if simbad service is down, user is told 'you must enter a name known to simbad' - which is very misleading.
 * @todo hyperbolic doesn't always update to display nodes-to-download as yellow. need to add a redraw in somewhere. don't want to redraw too often though.
 */
public class HelioScopeLauncherImpl extends AbstractScope  
    implements HelioScope{

    //Various gui components.
    private JCalendarCombo startCal;
    private JCalendarCombo endCal;


    
    /**
     * 
     * @param ui
     * @param conf
     * @param hs
     * @param myspace
     * @param chooser
     * @param reg
     * @param rci
     * @param siap
     * @param cone
     * @throws URISyntaxException 
     */
    public HelioScopeLauncherImpl(UIInternal ui, Configuration conf, HelpServerInternal hs,  
                                  MyspaceInternal myspace, ResourceChooserInternal chooser, Registry reg, 
                                  Stap stap, PlasticHubListener hub) throws URISyntaxException {
        super(conf,hs,ui,myspace,chooser,hub, new DalProtocol[]{
        		new StapProtocol(reg,stap)
        });



        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.helioscopeLauncher");
        setIconImage(IconHelper.loadIcon("search.gif").getImage());
    }
    
    protected String getScopeDescription() {
    	return null;
    }
    protected String getScopeIconURL() {
    	return null;
    }
    protected String getScopeName() {
    	return "Helioscope";
    }
    protected String getScopeRegId() {
    	return "ivo://org.astrogrid/helioscope";
    }
    
	protected JMenuItem createHistoryMenuItem(String historyItem) {
		final String[] times = historyItem.split("_");
		JMenuItem m = new JMenuItem(times[0] + " to " + times[1]);
		m.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
                startCal.setSelectedItem(times[0]);
                endCal.setSelectedItem(times[1]);				
			}
		});
		return m;
	}

	protected String grabHistoryItem() {
		return startCal.getSelectedItem() + "_" + endCal.getSelectedItem();
	}
    
        
    /**
     * Extracts out the dec of a particular position in the form of a ra,dec
     * @param position
     * @return
     */    
    private double getDEC() {
        return Double.NaN;
    }

    /**
     * Extracts out the ra of a particular position in the form of a ra,dec
     * @param position
     * @return
     * @todo refactor -report error to user - or prevent invalid input in the first place.
     */
    private double getRA() {
        return Double.NaN;
    }

    
    private JCheckBox formatTimeSeriesCheck;
    private JCheckBox formatGraphicCheck;


	/**
	 * @param scopeMain
	 * @return
	 */
	protected JPanel createSearchPanel() {
		JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.Y_AXIS));

        
        startCal = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE|JCalendarCombo.DISPLAY_TIME,true);
        startCal.setNullAllowed(false);
        startCal.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        startCal.setEditable(true);
        startCal.setMaximumSize(new Dimension(175,50));
        startCal.setAlignmentX(LEFT_ALIGNMENT);
        Calendar setStartCal = startCal.getCalendar();
        setStartCal.set(2000,0,1,0,0,0);
        startCal.setDate(setStartCal.getTime());

        endCal = new JCalendarCombo(JCalendarCombo.DISPLAY_DATE|JCalendarCombo.DISPLAY_TIME,true);
        endCal.setNullAllowed(false);
        endCal.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        endCal.setEditable(true);
        endCal.setMaximumSize(new Dimension(175,50));
        endCal.setAlignmentX(LEFT_ALIGNMENT);
        Calendar setEndCal = endCal.getCalendar();
        setEndCal.set(2000,0,2,0,0,0);
        endCal.setDate(setEndCal.getTime());

        
        searchPanel.add(new JLabel("Start Date&Time: "));
        searchPanel.add(startCal);

        searchPanel.add(new JLabel("End Date&Time: "));
        searchPanel.add(endCal);

        
        formatTimeSeriesCheck = new JCheckBox("Time Series");
        formatGraphicCheck = new JCheckBox("Graphic");
        formatTimeSeriesCheck.setSelected(true);
        formatGraphicCheck.setSelected(true);
        searchPanel.add(formatTimeSeriesCheck);
        searchPanel.add(formatGraphicCheck);
		return searchPanel;
	}



    
    /**
     * method: query
     * description: Queries the registry for sia and conesearch types and begins working on them.
     *
     *
     */
    protected void query() {
        logger.debug("query() - inside query method)");
        (new BackgroundOperation("Checking Position") {
            protected Object construct() throws Exception {
                return null;//getPosition();
            }
            protected void doFinished(Object o) {
                //String position = (String)o;
                final Calendar startStapCal = startCal.getCalendar();
                final Calendar endStapCal = endCal.getCalendar();
                
                if (startStapCal.after(endStapCal)) {
                    showError("Your Start date/time must be before the end date/time");
                    return;
                }
                
                if(!formatTimeSeriesCheck.isSelected() && !formatGraphicCheck.isSelected()) {
                    showError("You much have Time Series and/or Graphics checked when doing a search");
                    return;
                }
                    
                //setStatusMessage(position);
                clearTree();
                reFocusTopButton.setEnabled(true);
                
                //  @todo refactor this string-munging methods.

                final double ra = getRA();
                final double dec = getDEC();
                final String region = ""; //regionText.getText().trim();
                final double raSize = Double.NaN; //needsParsedRegion() ?  getRA(region) : Double.parseDouble(region);
                final double decSize = Double.NaN;  //needsParsedRegion() ? getDEC(region) : raSize;
                
                for (Iterator i = protocols.iterator(); i.hasNext(); ) {
                    final TemporalDalProtocol p =(TemporalDalProtocol)i.next();
//                    if (p.getCheckBox().isSelected()) {
                        (new BackgroundOperation("Searching for " + p.getName() + " Services") {
                            protected Object construct() throws Exception {
                                return p.listServices();
                            }
                            protected void doFinished(Object result) {
                                ResourceInformation[] services = (ResourceInformation[])result;
                                logger.info(services.length + " " + p.getName() + " services found");
                                
                                if(formatTimeSeriesCheck.isSelected() && formatGraphicCheck.isSelected()) {
                                    p.setPrimaryNodeLabel("Time Series/Images");
                                }
                                else if(formatTimeSeriesCheck.isSelected()) {
                                    p.setPrimaryNodeLabel("Time Series");
                                }
                                else if(formatGraphicCheck.isSelected()) {
                                    p.setPrimaryNodeLabel("Images");
                                }                                
                                for (int i = 0; i < services.length; i++) {
                                    if (services[i].getAccessURL() != null) {
                                        setProgressMax(getProgressMax()+1); // should give a nice visual effect.
                                        if(formatTimeSeriesCheck.isSelected() && formatGraphicCheck.isSelected()) {
                                            p.createRetriever(HelioScopeLauncherImpl.this,services[i],startStapCal,endStapCal, ra,dec,raSize,decSize).start();
                                        }
                                        else if(formatTimeSeriesCheck.isSelected()) {
                                            p.createRetriever(HelioScopeLauncherImpl.this,services[i],startStapCal,endStapCal, ra,dec,raSize,decSize,"TIME_SERIES").start();
                                        }
                                        else if(formatGraphicCheck.isSelected()) {
                                            p.createRetriever(HelioScopeLauncherImpl.this,services[i],startStapCal,endStapCal, ra,dec,raSize,decSize,"GRAPHICS").start();
                                        }
                                         }
                                }                            
                            }                            
                        }).start();
  //                  }
                }

            }
        }).start();
    }


  
}

/* 
$Log: HelioScopeLauncherImpl.java,v $
Revision 1.13  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

Revision 1.12  2006/05/12 07:00:11  KevinBenson
forgot to zap out things on clear history

Revision 1.11  2006/05/11 10:02:45  KevinBenson
added history to astro and helioscope.  Along with tweaks to alignment and borders
And changing decimal places to 6 degrees.

Revision 1.10  2006/04/26 15:56:54  nw
added 'halt query' and 'halt all tasks' functinaltiy.

Revision 1.9  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.8  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.7  2006/03/31 15:20:56  nw
removed work-around, due to new version of plastic library

Revision 1.6  2006/03/24 10:30:15  KevinBenson
new checkboxes on heliosope for the Format, and the ability to query by Format
for stap services on helioscope

Revision 1.5  2006/03/17 09:15:42  KevinBenson
minor change on stapretrieval to show only the startdate for the valNode and for
helioscope to make the combobox editable

Revision 1.4  2006/03/16 18:12:56  jdt
Some bug fixes, and the next version of the plastic library.

Revision 1.3  2006/03/16 10:02:34  KevinBenson
small label changed

Revision 1.2  2006/03/16 09:16:00  KevinBenson
usually comment/clean up type changes such as siap to stap. Plus setting date&time values
to a previous date

Revision 1.1  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

 
*/