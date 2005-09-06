/* WorkflowTreeCellRenderer.java
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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Then;
import org.astrogrid.workflow.beans.v1.Unset;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author pjn3
 *
 * Custom renderer for Workflow tree - adds icons and text
 */
public class WorkflowTreeCellRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(JTree tree, 
            Object value,
			  boolean isSelected,
			  boolean expanded, 
			  boolean leaf, 
			  int row, 
			  boolean hasFocus) {
				// The tree component's row height property controls the height of every displayed node. 
				// If the row height is greater than 0, all rows will be given the specified height. 
				// However, if the rows can be of differing heights, it is necessary to set the row height to 0. 
				// With a 0 row height, all heights of all rows are computed individually.
				tree.setRowHeight(0);
				JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, 
                            											  value, 
																		  isSelected, 
																		  expanded, 
																		  leaf, 
																		  row, 
																		  hasFocus);
				ToolTipManager.sharedInstance().registerComponent(tree);
				// Added as tree is mutable in workflow builder
				if (value instanceof DefaultMutableTreeNode)
					value = ((DefaultMutableTreeNode)value).getUserObject();
				if (value instanceof Workflow) {
					Workflow w = (Workflow)value;
					JPanel rendererPanel = new JPanel(new GridLayout(0,1));
					String name = w.getName() == null? "--" : w.getName();
					String desc = w.getDescription() == null? "--" : w.getDescription();
					rendererPanel.add(new JLabel(name));
					rendererPanel.add(new JLabel(desc));
					rendererPanel.setToolTipText("<html>Name: " + name + "<br>Description: " + desc + "</html>");
					rendererPanel.setBorder(BorderFactory.createLineBorder(Color.black));
					return rendererPanel;
				}
				else if (value instanceof Sequence){  
					label.setIcon(IconHelper.loadIcon("icon_Sequence.gif"));
					label.setText("Sequence");
					label.setToolTipText(null);    			
				}
				else if (value instanceof Flow){ 
					label.setIcon(IconHelper.loadIcon("icon_Flow.gif"));
					label.setText("Flow");   		
					label.setToolTipText(null);
				}    		
				else if (value instanceof Step){ 
					Step s = (Step)value;
					label.setIcon(IconHelper.loadIcon("icon_Step.gif"));
					String name = s.getName() == null? "" : s.getName();
					String task = "--";
					String iface = "--";
					if (s.getTool() != null) {
						task = s.getTool().getName() == null? "" : s.getTool().getName();
						iface = s.getTool().getInterface() == null? "" : s.getTool().getInterface();
					}
					String desc = s.getDescription() == null? "" : s.getDescription();					 
					String var = s.getResultVar() == null? "" : s.getResultVar();					
					
					String temp = "<html><b>Step</b>";					
					if (name.length() > 0) 
						temp += ", Name: " + name;
					if (task.length() > 0)
						temp += ", Task: " + task;					
					if (iface.length() > 0)
						temp += ", Interface: " + iface;
					if (var.length() > 0) 
						temp += ", Variable: " + var;					
					temp += "</html>";
					
					label.setText(temp);
					label.setToolTipText("<html>Name: " + s.getName() 
							+ "<br>Task: " + task
							+ "<br>Interface: " + iface 
							+ "<br>Desc: " + desc
							+ "<br>Var: " + var
							+ "</html>");
				}
				else if (value instanceof Set){ 
					Set set = (Set)value;
					String var = set.getVar() == null? "" : set.getVar();
					String val = set.getValue() == null? "" : set.getValue();
					String temp = "<html><b>Set</b>";
					if (var.length() > 0)
						temp += ", Variable: " + var;
					if (val.length() > 0)
						temp += ", Value: " + val;
					temp += "</html>";
					label.setIcon(IconHelper.loadIcon("icon_Set.gif"));
					label.setText(temp);
					label.setToolTipText("<html>Variable: " + var + "<br>Value: " + val +"</html>");
				}
				else if (value instanceof Unset){ 
					Unset s = (Unset)value;
					String var = s.getVar() == null? "" : s.getVar();
					label.setIcon(IconHelper.loadIcon("icon_Unset.gif"));
					String temp = "<html><b>Unset</b>";
					if (var.length() > 0)
						temp += ", Variable: " + var;
					temp += "</html>";
					label.setText(temp);
					label.setToolTipText("Variable: " + var);
				}    		
				else if (value instanceof Script){ 
					Script s = (Script)value;
					String desc = s.getDescription() == null? "" : s.getDescription();
					label.setIcon(IconHelper.loadIcon("icon_Script.gif"));
					String temp = "<html><b>Script</b>";
					if (desc.length() >0)
						 temp += ", Description: " + desc;
					temp += "</html>";
					label.setText(temp);
					label.setToolTipText("Description: " + desc);
				} 
				else if (value instanceof For){ 
					For f = (For)value;
					String var = f.getVar() == null? "" : f.getVar();
					String items = f.getItems() == null? "" : f.getItems();
					String temp = "<html><b>For</b>";
					if (var.length() > 0)
						temp += ", Variable: " + var;
					if (items.length() > 0)
						temp += ", Items: " + items;
					temp += "</html>";
					label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
					label.setText(temp);
					label.setToolTipText("<html>Variable: " + var + " <br>Items: " + items + "</html>");
				}   		
				else if (value instanceof While){
					While w = (While)value;
					String test = w.getTest() == null ? "" : w.getTest();
					label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
					String temp = "<html><b>While</b>";
					if (test.length() > 0)
						temp += ", Test: " + test;
					temp += "</html>";
					label.setText(temp);
					label.setToolTipText("Test: " + test);
				}
				else if (value instanceof Parfor){
					Parfor p = (Parfor)value;
					String var = p.getVar() == null? "" : p.getVar();
					String items = p.getItems() == null? "" : p.getItems();
					String temp = "<html><b>Parallel for loop</b>";
					if (var.length() > 0)
						temp += ", Variable: " + var;
					if (items.length() > 0)
						temp += ", Items: " + items;
					temp += "</html>";
					label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
					label.setText(temp);
					label.setToolTipText("<html>Variable: " + var + "<br>Items: " + items + "</html>");
				}    		
				else if (value instanceof Scope){ 
					label.setIcon(IconHelper.loadIcon("icon_Scope.gif"));
					label.setText("Scope");
					label.setToolTipText(null);
				}  
				else if (value instanceof If){
					If i = (If)value;
					label.setIcon(IconHelper.loadIcon("icon_If.gif"));
					String test = i.getTest() == null? "" : i.getTest();
					String temp = "<html><b>If</b>";
					if (test.length() > 0) 
						temp +=  ", Test: " + test;
					temp += "</html>";
					label.setText(temp);
					label.setToolTipText("Test: " + test);
				}
				else if (value instanceof Then){ 
					label.setIcon(IconHelper.loadIcon("icon_Then.gif"));
					label.setText("Then");
					label.setToolTipText(null);
				}
				else if (value instanceof Else){ 
					label.setIcon(IconHelper.loadIcon("icon_Else.gif"));
					label.setText("Else");
					label.setToolTipText(null);
				} 
				else if (value instanceof String) {
					JPanel rendererPanel = new JPanel();
					//Origional idea was to use scrollpane holding editorpane, however the custom
					//renderer just renders and does not allow user interaction - the scrollbars 
					//do not register inputs i.e. cannot scroll, hence use of textarea sized to fit 
					//script length.
					//JEditorPane jEditorPane = new JEditorPane();
					//jEditorPane.setText(value.toString());
					//jEditorPane.setCaretPosition(0);
					//jEditorPane.setEditable(false);
					int lineCt = 1;
					String text = value.toString();
					for (int i = 0; i < text.length(); i++) {
						if (text.charAt(i) == '\n') {
							lineCt++;
							if (lineCt > 5 ) {
								text = text.substring(0, i);
								lineCt = 8;
								text += "\n\n                                                  ----- Double click to view full script ------";
								break;
							}
						}
					}
					JTextArea jta = new JTextArea(lineCt, 60);
					jta.setText(text);
					JScrollPane pane = new JScrollPane(jta,
							JScrollPane.VERTICAL_SCROLLBAR_NEVER,
							JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);    			
					rendererPanel.add(pane);    
					return rendererPanel;
				}
				else {    			
					label.setText("to do");
				}   		
				return(label);																	 
	}
}
