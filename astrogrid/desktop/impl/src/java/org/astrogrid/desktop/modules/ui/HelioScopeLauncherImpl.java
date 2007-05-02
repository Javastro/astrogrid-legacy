/*$Id: HelioScopeLauncherImpl.java,v 1.23 2007/05/02 15:38:28 nw Exp $
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.scope.AbstractScope;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.StapProtocol;
import org.astrogrid.desktop.modules.ui.scope.TemporalDalProtocol;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;
import org.freixas.jcalendar.JCalendarCombo;


/** Implementation of HelioScope
 */
public class HelioScopeLauncherImpl extends AbstractScope  
    implements HelioScopeInternal{

    //Various gui components.
    private JCalendarCombo startCal;
    private JCalendarCombo endCal;

    
    /** override:  create a help menu with additional entries */
    protected JMenu createHelpMenu() {
 	JMenu menu = super.createHelpMenu();
 	menu.insertSeparator(0);
 	/*
 	JMenuItem ref = new JMenuItem("Reference");
 	getHelpServer().enableHelpOnButton(ref, "helioscope.menu.reference");
 	menu.insert(ref,0);
 	*/
 	JMenuItem sci = new JMenuItem("Helioscope Help");
 	getContext().getHelpServer().enableHelpOnButton(sci, "helioscope.menu.science");
 	menu.insert(sci,0);
 	return menu;
 }

    
    /**
     * 
     * @param ui
     * @param conf
     * @param hs
     * @param myspace
     * @param chooser
     * @param reg
     * @param browser todo
     * @param rci
     * @param siap
     * @param cone
     * @throws URISyntaxException 
     */
    public HelioScopeLauncherImpl(UIContext context,
                                  MyspaceInternal myspace, ResourceChooserInternal chooser, Registry reg, 
                                  Stap stap,TupperwareInternal tupp, SendToMenu sendTo, SnitchInternal snitch, RegistryBrowser browser) {
        super(context,myspace,chooser,tupp, sendTo, snitch,"HelioScope",new DalProtocol[]{
        		new StapProtocol(reg,stap)
        });

        
        getContext().getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.helioscopeLauncher");
        // declared in parent class.
        //        dynamicButtons.add(new SaveNodesButton(vizModel.getSelectionFocusSet(),this,chooser,myspace));
        
        setIconImage(IconHelper.loadIcon("helioscope.png").getImage());
    }
   
    
    /** create a menu item that can populate input fields from a string */
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

	/** convert input fields into a string */
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
    	
                final Calendar startStapCal = startCal.getCalendar();
                final Calendar endStapCal = endCal.getCalendar();
                
                if (startStapCal.after(endStapCal)) {
                    showError("Your Start date/time must be before the end date/time");
                    return;
                }
                super.storeHistoryItem();
                if(!formatTimeSeriesCheck.isSelected() && !formatGraphicCheck.isSelected()) {
                    showError("You much have Time Series and/or Graphics checked when doing a search");
                    return;
                }
                    
                //setStatusMessage(position);
                clearTree();
                topAction.setEnabled(true);
                
                //  @todo refactor this string-munging methods.

                final double ra = getRA();
                final double dec = getDEC();
          //c      final String region = ""; //regionText.getText().trim();
                final double raSize = Double.NaN; //needsParsedRegion() ?  getRA(region) : Double.parseDouble(region);
                final double decSize = Double.NaN;  //needsParsedRegion() ? getDEC(region) : raSize;
                
                for (Iterator i = protocols.iterator(); i.hasNext(); ) {
                    final TemporalDalProtocol p =(TemporalDalProtocol)i.next();
//                    if (p.getCheckBox().isSelected()) {
                        (new BackgroundOperation("Searching for " + p.getName() + " Services") {
                            protected Object construct() throws Exception {
	                        	if (resourceList == null) {
	                        		return p.listServices();
	                        	} else {
	                        		return p.filterServices(resourceList);
	                        	}
                            }
                            protected void doFinished(Object result) {
                                Service[] services = (Service[])result;
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
                        }).start();
  //                  }
                }

    }

private List resourceList;
	public void runSubset(List resources) {
		this.resourceList = resources;
		setTitle("Helioscope : on subset");		
	}


  
}

/* 
$Log: HelioScopeLauncherImpl.java,v $
Revision 1.23  2007/05/02 15:38:28  nw
changes for 2007.3.alpha1

Revision 1.22  2007/04/18 15:47:05  nw
tidied up voexplorer, removed front pane.

Revision 1.21  2007/03/08 17:43:58  nw
first draft of voexplorer

Revision 1.20  2007/01/11 18:15:49  nw
fixed help system to point to ag site.

Revision 1.19  2006/08/15 10:09:04  nw
migrated from old to new registry models.

Revision 1.18  2006/08/02 13:31:20  nw
added snitching for each scope search.

Revision 1.17  2006/07/18 10:41:02  KevinBenson
small fix to add savenode button to the dynamicbar after the super() called.

Revision 1.16  2006/06/27 10:34:40  nw
send-to menu, tupperware actions.

Revision 1.15  2006/05/26 15:14:56  nw
corrected icon paths.

Revision 1.14  2006/05/17 23:59:36  nw
documentaiton, and tweaks, from feedback by jonathan and kevin.

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