/*$Id: AstroScopeLauncherImpl.java,v 1.57 2007/03/09 15:34:24 nw Exp $
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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle;
import org.astrogrid.desktop.modules.ui.comp.DimensionTextField;
import org.astrogrid.desktop.modules.ui.comp.NameResolvingPositionTextField;
import org.astrogrid.desktop.modules.ui.comp.PositionUtils;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle.DecSexListener;
import org.astrogrid.desktop.modules.ui.scope.AbstractScope;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;
import org.astrogrid.desktop.modules.ui.scope.DalProtocol;
import org.astrogrid.desktop.modules.ui.scope.Retriever;
import org.astrogrid.desktop.modules.ui.scope.SaveNodesButton;
import org.astrogrid.desktop.modules.ui.scope.SiapProtocol;
import org.astrogrid.desktop.modules.ui.scope.SpatialDalProtocol;
import org.astrogrid.desktop.modules.ui.scope.SpectrumLoadPlasticButton;
import org.astrogrid.desktop.modules.ui.scope.SsapProtocol;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;

import edu.berkeley.guir.prefuse.graph.TreeNode;



/**AstroScope Implementation.
 * 
 */
public class AstroScopeLauncherImpl extends AbstractScope 
    implements AstroScopeInternal, DecSexListener {
   
	protected DecSexToggle dsToggle;
	protected DimensionTextField regionText;
           
	protected NameResolvingPositionTextField posText;
	protected final Sesame ses;
    /**
     * 
     * @param ui
     * @param conf
     * @param hs
     * @param myspace
     * @param chooser
     * @param reg
     * @param siap
     * @param cone
     * @param browser todo
     * @param rci
     * @throws URISyntaxException 
     */
    public AstroScopeLauncherImpl(UIInternal ui, Configuration conf, HelpServerInternal hs,  
                                  MyspaceInternal myspace, ResourceChooserInternal chooser, Registry reg, 
                                  Siap siap, Cone cone, Ssap ssap,Sesame ses, TupperwareInternal tupp, SendToMenu sendTo,
                                  SnitchInternal snitch, RegistryBrowser browser)  {
        super(conf,hs,ui,myspace,chooser,tupp,sendTo,snitch,"AstroScope",
        		new DalProtocol[]{
        			new SiapProtocol(reg,siap)
        			, new SsapProtocol(reg,ssap)
        			,new ConeProtocol(reg,cone)   
        			,new AllVizierProtocol(cone)
        }, browser);
        // work-around for architectural glitch - posText is created by super constructore
        // before we can populate member variables - so need to pass in 'ses' later.
        posText.setSesame(ses);   
        this.ses = ses;
    
        getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.astroscopeLauncher");
        setIconImage(IconHelper.loadIcon("astroscope.png").getImage());
      
    }
    // overridden to add a spectrum button - don't think this is general enough to add to the baseclass, so it's available in helipscope too.
   public JButton[] buildPlasticButtons(PlasticApplicationDescription plas) {
	   if (! plas.understandsMessage(SpectrumLoadPlasticButton.SPECTRA_LOAD_FROM_URL)) {
		   return super.buildPlasticButtons(plas);
	   } else {
		   JButton[] parent =  super.buildPlasticButtons(plas);
		   JButton[] result = new JButton[parent.length + 1];
		   System.arraycopy(parent,0,result,0,parent.length);
		   result[parent.length] = new SpectrumLoadPlasticButton(plas,vizModel.getSelectionFocusSet(), this, tupperware);
		   return result;
	   }
}
   
   protected JMenuItem createHistoryMenuItem(String historyItem) {
		 String[] hist = historyItem.split("_");
		 if (hist.length == 3) {
			 return new HistoryMenuItem(hist[1],hist[2],hist[0]);			 
		 }else if(hist.length == 2) {
			 return new HistoryMenuItem(hist[0],hist[1]);
		 }else {
			 return null;
		 }
		
	}
   
   /** override:  create a help menu with additional entries */
   protected JMenu createHelpMenu() {
	JMenu menu = super.createHelpMenu();
	menu.insertSeparator(0);
/*
	JMenuItem ref = new JMenuItem("Reference");
	getHelpServer().enableHelpOnButton(ref, "astroscope.menu.reference");
	menu.insert(ref,0);
	*/
	JMenuItem sci = new JMenuItem("Astroscope Help");
	getHelpServer().enableHelpOnButton(sci, "astroscope.menu.science");
	menu.insert(sci,0);
	return menu;
}
	
	/** implementation of an item in the history menu.
	 * 
	 * each listens to decSexToggle - to change representation from one system to other.
	 * @author Noel Winstanley
	 * @since May 17, 20067:34:18 PM
	 */
	private class HistoryMenuItem extends JMenuItem
		implements ActionListener, DecSexListener{
		public HistoryMenuItem(String pos,String size) {
			this(pos,size,null);
		}
		
		public HistoryMenuItem(String pos,String size, String objName) {
			super();
			this.pos = pos;
			this.size = size;
			this.objName = objName;
			if (dsToggle.isDegrees()) {
				this.degreesSelected(null);
			} else {
				this.sexaSelected(null);
			}
			this.addActionListener(this);
			dsToggle.addListener(this);
		}
		
		private final String pos;
		private final String size;
		private final String objName;
		
		public void actionPerformed(ActionEvent e) {
			try {
				if(!dsToggle.isDegrees()) {
					//positions were done in degrees, but 
					//current radio buttons are in sexa, meaning input text
					//boxes are expecting sexagesimal or numberformatexc are thrown.
					
					String sexPos = pos;
					try {
						sexPos=  PositionUtils.decimalToSexagesimal(pos);
					} catch (NumberFormatException nfe) {
					}
					
					String sexSize = size;
					try {
						if (size.indexOf(",") != -1) {
							sexSize = PositionUtils.decimalToSexagesimal(size);
						} else {
							sexSize = PositionUtils.decimalRaToSexagesimal(Double.parseDouble(size));
						}
					} catch(NumberFormatException nfe) {
					}
					posText.setPosition(sexPos);
					regionText.setDimension(sexSize);					
				}else {
					//most commonly used when toggle was in degrees originally
					//and position was given in degrees.
					posText.setPosition(pos);
					regionText.setDimension(size);
				}
			} catch (ParseException ex) {
					showError("Failed to parse history",ex);
			} catch(java.lang.NumberFormatException nfe) {
					showError("Failed to parse history",nfe);
			}
		}

		public void degreesSelected(EventObject ignored) {
			try {
				String[] posArr = pos.split(",");
				String[] sizeArr = size.split(",");
				String objString = "";
				if(objName != null && objName.trim().length() > 0)  {
					objString = " Object: " + objName;
				}
			
				StringBuffer sb = new StringBuffer("position: ") 
				.append(nf.format(Double.parseDouble(posArr[0])))
				.append(',')
				.append(nf.format(Double.parseDouble(posArr[1])))
				.append(" radius: ") 
				.append(nf.format(Double.parseDouble(sizeArr[0])))
				.append(',')
				.append(nf.format(Double.parseDouble(sizeArr[1])))
				.append(objString);
				setText(sb.toString());
			} catch (NumberFormatException e) {
				// don't care too much.
			}
		}

		public void sexaSelected(EventObject ignored) {
			// try to convert for display - if can't convert, leave as it.
			String sexPos = pos;
			String sexSize = size;	
				try {
					sexPos=  PositionUtils.decimalToSexagesimal(pos);
				} catch (NumberFormatException e) {
				}

				try {
					if (size.indexOf(",") != -1) {
						sexSize = PositionUtils.decimalToSexagesimal(size);
					} else {
						sexSize = PositionUtils.decimalRaToSexagesimal(Double.parseDouble(size));
					}
				} catch(NumberFormatException e) {
				}
			String objString = "";
			if(objName != null && objName.trim().length() > 0)  {
				objString = " Object: " + objName;
			}
			
			setText("position: " + sexPos
					+ " radius: " + sexSize + objString);

		}
					
		}
	
	/** coverts contents of text fields to a history string */
	protected String grabHistoryItem() {
		Point2D pos = posText.getPosition();
		Dimension2D dim = regionText.getDimension();
		if (Double.isNaN(pos.getX())) {
			return null;
		}
		
		String objName = "";
		if(posText.getObjectName() != null) 
			objName = posText.getObjectName();
		return objName + "_" + pos.getX()+ "," + pos.getY() +  "_" + dim.getWidth() + "," + dim.getHeight();
	}    
    
	/** safe to share this between astroscope instances - as is only ever used on single event dispatch thread */
  private final static NumberFormat nf =NumberFormat.getNumberInstance();
	static {
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
	}
	
    private void toggleAndConvertNodes(TreeNode nd,boolean fromDegrees) {
        String ndVal = null;  
        String posVal = null;
        boolean foundOffset = false;
        if((ndVal = nd.getAttribute(Retriever.OFFSET_ATTRIBUTE)) != null) {
            foundOffset = true;
            double val;
            if(fromDegrees) {
                val = Double.parseDouble(ndVal) * 3600;                
            } else {
                val = Double.parseDouble(ndVal);
            }
            nd.setAttribute(Retriever.LABEL_ATTRIBUTE,nf.format(val));
            nd.setAttribute(Retriever.TOOLTIP_ATTRIBUTE,String.valueOf(val));
            for(int i = 0;i < nd.getChildCount();i++) {
                TreeNode childNode = nd.getChild(i);
                //should be a position string.
                ndVal = childNode.getAttribute(Retriever.LABEL_ATTRIBUTE);
                if(fromDegrees) {
                    posVal = PositionUtils.getRASexagesimal(ndVal) + "," + PositionUtils.getDECSexagesimal(ndVal);
                }else {
                    posVal = nf.format(PositionUtils.getRADegrees(ndVal)) + "," + nf.format(PositionUtils.getDECDegrees(ndVal));
                }
                childNode.setAttribute(Retriever.LABEL_ATTRIBUTE,posVal);
                childNode.setAttribute(Retriever.TOOLTIP_ATTRIBUTE,posVal);
            }
        }//if
        for(int i = 0;i < nd.getChildCount();i++) {
            if(!foundOffset)
                toggleAndConvertNodes(nd.getChild(i), fromDegrees);
        }
        
    }

	/**
	 * Builds the custom part of the interface - the search form.
	 */
	protected JPanel createSearchPanel() {
		JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.Y_AXIS));

        posText = new NameResolvingPositionTextField(this);
        posText.setToolTipText("Object name (3c273) or Position (187.27,+2.05 or 12:29:06.00,+02:03:08.60)");
        posText.setAlignmentX(LEFT_ALIGNMENT);
        posText.setColumns(10);
        
        regionText = new DimensionTextField();
        regionText.setToolTipText("Search radius (0.008333 degs or 30.00\")");
        regionText.setAlignmentX(LEFT_ALIGNMENT);
        regionText.setColumns(10);
        
        searchPanel.add(new JLabel("Position or Object Name"));
        searchPanel.add(posText);
        searchPanel.add(new JLabel("Search Radius (degs/\")"));
        searchPanel.add(regionText);
     
        dsToggle = new DecSexToggle();
        dsToggle.addListener(posText);
        dsToggle.addListener(regionText);
        dsToggle.addListener(this);
       // JPanel coords = new JPanel();
       // coords.setLayout(new GridLayout(1,2));
        Box coords = Box.createHorizontalBox();
        coords.setAlignmentX(LEFT_ALIGNMENT);
        coords.add(dsToggle.getDegreesRadio());
        coords.add(dsToggle.getSexaRadio());
        dsToggle.getDegreesRadio().setBorder(  BorderFactory.createLineBorder(Color.red));
        dsToggle.getSexaRadio().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                dsToggle.getSexaRadio().getBorder()));        
        searchPanel.add(coords);
        protocolsPanel = new JPanel();
        protocolsPanel.setLayout(new BoxLayout(protocolsPanel,BoxLayout.Y_AXIS));
        
        for (Iterator i = protocols.iterator(); i.hasNext(); ) {
            DalProtocol p = (DalProtocol)i.next();
            protocolsPanel.add(p.getCheckBox());
        }
        searchPanel.add(protocolsPanel);
      
		return searchPanel;
	}
	// defined here, so we can control it's visibiliity.
	private  JPanel protocolsPanel;

	/** perform a query */
	protected void query() {
		// slightly tricky - what we do depends on whether posText is currently in the middle of resolving a name or not.
		final String positionString = posText.getObjectName(); // grab this first, in case we need it in a mo.
		Point2D position = posText.getPosition();	
		if (Double.isNaN(position.getX())) { // position is not a number - indicates that it's currently being resolved.
			// so we'll resolve it ourselves - simplest thing to do - if we've got the position string..
			// hopefully we've got something we can work with..
			if (positionString != null) {
				(new BackgroundOperation("Resolving object " + positionString) {
					protected Object construct() throws Exception {
						return ses.sesame(positionString.trim(),"x");
					}
					protected void doError(Throwable ex) {
						showError("Simbad failed to resolve " + positionString);
					}
					protected void doFinished(Object result) {
						String temp = (String) result;
						Point2D pos;
						try {
							double ra = Double.parseDouble( temp.substring(temp.indexOf("<jradeg>")+ 8, temp.indexOf("</jradeg>")));
							double dec = Double.parseDouble( temp.substring(temp.indexOf("<jdedeg>")+ 8, temp.indexOf("</jdedeg>")));
							pos = new Point2D.Double(ra,dec);
						} catch (Throwable t) {
							doError(t);
							return;
						}
						// now on with the query
						queryBody(pos);
					}
				}).start();
			} else { // no position, and no objectName 
				//blergh - caught it at just thw wrong time - lets try again.
				query();
			}
		} else {
			// everything is fine - so we'll query directly.
			queryBody(position);
		}
	}
	
	/** actually do the query */
	private void queryBody(Point2D position) {  

	            setStatusMessage("" + position.getX() + ',' + position.getY());
	            clearTree();
	            topAction.setEnabled(true);
	            // ok. everything looks valid. add a task to later on storee the history item
	            // make this a later task, so that resolver thread has chance to return.
	            SwingUtilities.invokeLater(new Runnable(){
	            	public void run() {
	            		storeHistoryItem();
	            	}
	            });
	            
	            final double ra = position.getX();
	            final double dec = position.getY();
	                                      
	            Dimension2D dim = regionText.getDimension();
	            final double raSize = dim.getWidth();
	            final double decSize = dim.getHeight();

	            for (Iterator i = protocols.iterator(); i.hasNext(); ) {
	                final SpatialDalProtocol p =(SpatialDalProtocol)i.next();
	                if (p.getCheckBox().isSelected()) {
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
	                            for (int i = 0; i < services.length; i++) {
	                                    setProgressMax(getProgressMax()+1); // should give a nice visual effect.
	                                    p.createRetriever(AstroScopeLauncherImpl.this,services[i],ra,dec,raSize,decSize).start();
	                            }                            
	                        }                            
	                    }).start();
	                }
	            }
	}
	private List resourceList;
	// another listener to the decSex toggle - convert node display
	public void degreesSelected(EventObject e) {
        toggleAndConvertNodes(vizModel.getRootNode(),false);
        vizualizations.reDrawGraphs();		
	}
	public void sexaSelected(EventObject e) {
        toggleAndConvertNodes(vizModel.getRootNode(),true);
        vizualizations.reDrawGraphs();		
	}
	// configure to run against this list of services.
	public void runSubset(List resources) {
		this.resourceList = resources;
		setTitle("Astroscope : on subset");
		protocolsPanel.setVisible(false);
	}

  
}

/* 
$Log: AstroScopeLauncherImpl.java,v $
Revision 1.57  2007/03/09 15:34:24  nw
vizier and voexplorer

Revision 1.56  2007/03/08 17:43:59  nw
first draft of voexplorer

Revision 1.55  2007/01/11 18:15:50  nw
fixed help system to point to ag site.

Revision 1.54  2006/10/31 12:56:09  nw
removed vospec button

Revision 1.53  2006/10/17 07:21:30  KevinBenson
small changes to history part of astroscope to have an object Name.  One small thing on Retriever to try and put successful status messages.

Revision 1.52  2006/09/14 13:52:59  nw
implemented plastic spectrum messaging.

Revision 1.51  2006/08/15 10:10:20  nw
migrated from old to new registry models.

Revision 1.50  2006/08/02 13:31:20  nw
added snitching for each scope search.

Revision 1.49  2006/06/27 19:14:24  nw
added vospec back in.

Revision 1.48  2006/06/27 10:34:07  nw
send-to menu, tupperware actions.

Revision 1.47  2006/05/26 15:14:56  nw
corrected icon paths.

Revision 1.46  2006/05/18 00:16:34  nw
improved name resolving code.

Revision 1.45  2006/05/17 23:59:36  nw
documentaiton, and tweaks, from feedback by jonathan and kevin.

Revision 1.44  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

Revision 1.43  2006/05/12 07:00:11  KevinBenson
forgot to zap out things on clear history

Revision 1.42  2006/05/11 12:36:57  KevinBenson
change tooltip and small label change on astroscope

Revision 1.41  2006/05/11 12:14:54  KevinBenson
minor tweaks with border colors and labels

Revision 1.40  2006/05/11 11:40:59  KevinBenson
small change on a label

Revision 1.39  2006/05/11 10:02:45  KevinBenson
added history to astro and helioscope.  Along with tweaks to alignment and borders
And changing decimal places to 6 degrees.

Revision 1.38  2006/04/26 15:56:54  nw
added 'halt query' and 'halt all tasks' functinaltiy.

Revision 1.36  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.35  2006/03/31 15:20:56  nw
removed work-around, due to new version of plastic library

Revision 1.34  2006/03/16 18:12:56  jdt
Some bug fixes, and the next version of the plastic library.

Revision 1.32  2006/02/27 12:20:50  nw
improved plastic integration

Revision 1.31  2006/02/24 15:25:57  nw
plasticization of astroscope

Revision 1.29  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer

Revision 1.28  2006/02/02 14:50:49  nw
refactored into separate components.

Revision 1.27  2005/12/02 17:05:08  nw
replaced dom-style parsing of votables with sax-style. faster, better for memory

Revision 1.26  2005/12/02 13:42:48  nw
changed to use system pooled executor, re-worked background processes

Revision 1.25  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.24  2005/11/15 19:39:07  nw
merged in improvements from release branch.

Revision 1.23.2.2  2005/11/15 19:34:19  nw
improvements to saving, threading, clustering.

Revision 1.23.2.1  2005/11/15 13:26:23  nw
fixed astroscope.
worked on javahelp

Revision 1.23  2005/11/11 10:27:41  nw
minor changes.

Revision 1.22  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.21  2005/11/10 14:55:15  KevinBenson
minor tweaks plus javahelp is there now

Revision 1.20  2005/11/10 14:18:52  KevinBenson
minor fixes to highlight other displays on selects. and fous on nodes

Revision 1.19  2005/11/10 12:18:27  KevinBenson
small tweaks

Revision 1.18  2005/11/10 10:43:13  KevinBenson
minor change on the haversine formula

Revision 1.17  2005/11/09 16:05:55  KevinBenson
minor change to add a "Go to Top" button.

Revision 1.16  2005/11/09 14:10:44  KevinBenson
removed some statemetns that were not needed

Revision 1.15  2005/11/09 14:06:52  KevinBenson
minor changes for clearTree to refocus in the center.  And fix an expression on the position

Revision 1.14  2005/11/08 15:03:56  KevinBenson
minor changes on sizing

Revision 1.13  2005/11/07 16:25:05  KevinBenson
added some clustering to it. so there is an offset and some clustered child nodes as well.

Revision 1.12  2005/11/04 17:49:52  nw
reworked selection and save datastructures.

Revision 1.11  2005/11/04 14:09:12  nw
improved error handling in getPosition,
started looking at image preview.

Revision 1.10  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.9  2005/11/03 11:56:49  KevinBenson
added a new astroscope cluster

Revision 1.8  2005/11/02 17:29:56  KevinBenson
fixed scrollpane

Revision 1.7  2005/11/02 09:50:11  KevinBenson
should have Noel's 2 minor fixes.  Plus a couple of additions for buttons and node selections

Revision 1.5  2005/11/01 14:40:20  KevinBenson
Some small changes to have hyperbolic working with selections and saving to myspace

Revision 1.3  2005/10/31 16:13:51  KevinBenson
added hyperbolic in there, plus the saving to myspace area.

Revision 1.2  2005/10/31 12:49:38  nw
rehashed downloading mechanism,
put in a bunch of sample vizualizations.

Revision 1.1  2005/10/26 15:53:15  KevinBenson
new astroscope being added into the workbench.

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.2.2  2005/10/10 18:12:36  nw
merged kev's datascope lite.

Revision 1.2  2005/10/10 12:09:45  KevinBenson
small change to the astroscope to show browser and vospace when the user hits okay

Revision 1.1  2005/10/04 20:46:48  KevinBenson
new datascope launcher and change to module.xml for it.  Vospacebrowserimpl changes to handle file copies to directories on import and export

Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.3  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:33  nw
first release of application launcher
 
*/