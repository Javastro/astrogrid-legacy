/* ActivityListRenderer.java
 * Created on 18-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.workflowBuilder.renderers;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ToolTipManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.icons.IconHelper;

/**
 * @author pjn3
 *
 * * Custom renderer for activity JList - adds icons and text depending on object
 */
public class ActivityListRenderer extends DefaultListCellRenderer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ActivityListRenderer.class);
	
	private String helpLocation = "/org/astrogrid/desktop/modules/workflowBuilder/helpText/";
	private boolean showToolTip = true;
	
	public Component getListCellRendererComponent(JList list,
												  Object value,
												  int index,
												  boolean isSelected,
												  boolean hasFocus) {
		JLabel label = (JLabel)super.getListCellRendererComponent(list,
                            									  value,
																  index,
																  isSelected,
																  hasFocus);
		// Keep tooltip visible for longer
		ToolTipManager ttm = ToolTipManager.sharedInstance();
		ttm.setDismissDelay(99999);
		
		if (value.equals("Sequence")){ 
			label.setIcon(IconHelper.loadIcon("icon_Sequence.gif"));
			label.setText("Sequence");
			label.setToolTipText(getToolTipText("Sequence"));    			
		}
		else if (value.equals("Flow")){ 
			label.setIcon(IconHelper.loadIcon("icon_Flow.gif"));
			label.setText("Flow");   		
			label.setToolTipText(getToolTipText("Flow"));
		}    		
		else if (value.equals("Step") ){
			label.setIcon(IconHelper.loadIcon("icon_Step.gif"));
			label.setText("Step");
			label.setToolTipText(getToolTipText("Step"));
		}
		else if (value.equals("Set")){ 
			label.setIcon(IconHelper.loadIcon("icon_Set.gif"));
			label.setText("Set");
			label.setToolTipText(getToolTipText("Set"));
		}
		else if (value.equals("Unset")){ 
			label.setIcon(IconHelper.loadIcon("icon_Unset.gif"));
			label.setText("Unset");
			label.setToolTipText(getToolTipText("Unset"));
		}    		
		else if (value.equals("Script")){ 
			label.setIcon(IconHelper.loadIcon("icon_Script.gif"));
			label.setText("Script");
			label.setToolTipText(getToolTipText("Script"));
		} 
		else if (value.equals("For")){ 
			label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
			label.setText("For loop");
			label.setToolTipText(getToolTipText("For"));
		}   		
		else if (value.equals("While")){
			label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
			label.setText("While loop");
			label.setToolTipText(getToolTipText("While"));
		}
		else if (value.equals("ParFor")){
			label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
			label.setText("Parallel loop");
			label.setToolTipText(getToolTipText("ParFor"));
		}    		
		else if (value.equals("Scope")){ 
			label.setIcon(IconHelper.loadIcon("icon_Scope.gif"));
			label.setText("Scope");
			label.setToolTipText(getToolTipText("Scope"));
		}  
		else if (value.equals("If")){
			label.setIcon(IconHelper.loadIcon("icon_If.gif"));
			label.setText("If");
			label.setToolTipText(getToolTipText("If"));
		}
		else if (value.equals("Then")){ 
			label.setIcon(IconHelper.loadIcon("icon_Then.gif"));
			label.setText("Then");
			label.setToolTipText(getToolTipText("Then"));
		}
		else if (value.equals("Else")){ 
			label.setIcon(IconHelper.loadIcon("icon_Else.gif"));
			label.setText("Else");
			label.setToolTipText(getToolTipText("Else"));
		}
		else {
			label.setText("To do");
			label.setToolTipText(null);
			logger.error("Unrecogonised activity: " + value);
		}
		if (!showToolTip)
			label.setToolTipText(null);
		return(label);																	 
	}
	
    /**
     * Returns tool tip for activity, read from file
     * 
     * @param String activity, type of activity 
     * @return String tool tip text
     */
	private String getToolTipText(String activity) {
	    String text = "";
	    String str ;
		try {
		    InputStream is = this.getClass().getResourceAsStream(helpLocation + "help_"+activity+"_tip.html");
	        InputStreamReader inputStreamReader = new InputStreamReader(is); 
	        BufferedReader br = new BufferedReader(inputStreamReader);	    
	        while ((str = br.readLine()) != null) 
	    	    text = text + str;
	        br.close();
	    } 
	    catch(Exception ex) {
	    	text = activity;
	    }
	    return text;
	}
	
    /** 
     * Turn activity list tooltips on or off
     * @param b boolean tooltips on/off
     */
    public void setToolTipVisible(boolean b) {
    	showToolTip = b;
    }
}
