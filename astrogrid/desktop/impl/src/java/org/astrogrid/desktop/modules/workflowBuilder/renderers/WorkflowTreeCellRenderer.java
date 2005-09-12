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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.editors.AbstractToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.BasicToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.RawXMLToolEditorPanel;
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author pjn3
 *
 * Custom renderer for Workflow tree - adds icons and text
 * @modified nww - optimized a little using string buffers. embedded tool panels.
 * @todo add indication when a required field is missing (e.g. iwhen 'test' of If is null, put in a red 'missing')
 * @todo move rendering of tools into a separate leaf of step.
 */
public class WorkflowTreeCellRenderer extends DefaultTreeCellRenderer {

    public WorkflowTreeCellRenderer(ApplicationsInternal apps) {
        this.apps = apps;
        rendererPanel = new JPanel(new GridLayout(0,1));
        final Border myBorder = BorderFactory.createEtchedBorder();
        rendererPanel.setBorder(myBorder);
        rendererPanel.setBackground(Color.WHITE);
        toolPanel = new BasicToolEditorPanel(null,false);
        toolPanel.setBorder(myBorder);        
        toolPanel.setBackground(Color.WHITE);
        stepPanel = new JPanel();
        stepPanel.setLayout(new BoxLayout(stepPanel,BoxLayout.Y_AXIS));
        jta = new JTextArea();     
        jta.setColumns(60);
        jta.setBorder(myBorder);
        jta.setToolTipText("Double click to edit");
    }
    private final ApplicationsInternal apps;
    private final JPanel rendererPanel;
    private final AbstractToolEditorPanel toolPanel;
    private final JPanel stepPanel;
    private final JTextArea jta; 
    
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
                StringBuffer sb = new StringBuffer();
				// Added as tree is mutable in workflow builder
				if (value instanceof DefaultMutableTreeNode)
					value = ((DefaultMutableTreeNode)value).getUserObject();
				if (value instanceof Workflow) {
					Workflow w = (Workflow)value;					
                    String name = w.getName() == null? "--" : w.getName();
					String desc = w.getDescription() == null? "--" : w.getDescription();
                    rendererPanel.removeAll();
					rendererPanel.add(new JLabel(name));
					rendererPanel.add(new JLabel(desc));
					rendererPanel.setToolTipText("<html>Name: " + name + "<br>Description: " + desc + "</html>");				
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
                    label.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                    stepPanel.removeAll();
                    stepPanel.add(label);           
					label.setIcon(IconHelper.loadIcon("icon_Step.gif"));
					String name = s.getName() == null? "" : s.getName();
					String task = "--";
					String iface = "--";
					if (s.getTool() != null) {
						task = s.getTool().getName() == null? "" : s.getTool().getName();
						iface = s.getTool().getInterface() == null? "" : s.getTool().getInterface();
                         try {
                            ApplicationInformation info = apps.getInfoForTool(s.getTool());
                            toolPanel.getToolModel().populate(s.getTool(),info);
                            stepPanel.add(toolPanel,BorderLayout.CENTER);
                            } catch (ACRException e) {
                                //@todo improve error reporting here - and at least display tool content.
                                stepPanel.add(new JLabel("Problem reading tool"));
                            }                        
					}
					String desc = s.getDescription() == null? "" : s.getDescription();					 
					String var = s.getResultVar() == null? "" : s.getResultVar();					
					
					sb.append("<html><b>Step</b> ");
					if (name.length() > 0) 
						sb.append(name);
                    if (var.length() > 0) 
                        sb.append("<b>Result Variable </b>").append(var);   
                    if (var.length() > 0)
                        sb.append("<br><i>").append(desc).append("</i>");
                    if (task.length() > 0)
						sb.append("<br><b>Task</b> ").append(task);					
					if (iface.length() > 0)
						sb.append(" <b>Interface</b> ").append(iface);
				
					sb.append("</html>");
					
					label.setText(sb.toString());
					return stepPanel;
				}
				else if (value instanceof Set){ 
					Set set = (Set)value;
					String var = set.getVar() == null? "" : set.getVar();
					String val = set.getValue() == null? "" : set.getValue();
                    sb.append("<html><b>Set</b>");
					if (var.length() > 0)
                        sb.append(" <i>").append(var).append("</i>");
					if (val.length() > 0) {
                        sb.append(" <b>:=</b> ").append(val);
                    } else {
                        sb.append(" uninitialized ");
                    }					
					sb.append("</html>");
					label.setIcon(IconHelper.loadIcon("icon_Set.gif"));
					label.setText(sb.toString());
                    label.setToolTipText(null);                    
				}
				else if (value instanceof Unset){ 
					Unset s = (Unset)value;
					String var = s.getVar() == null? "" : s.getVar();
					label.setIcon(IconHelper.loadIcon("icon_Unset.gif"));
					sb.append("<html><b>Unset</b>");
					if (var.length() > 0)
						sb.append(" <i>" ).append(var).append("</i>");
					sb.append("</html>");
					label.setText(sb.toString());
				}    		
				else if (value instanceof Script){ 
					Script s = (Script)value;
					String desc = s.getDescription() == null? "" : s.getDescription();
					label.setIcon(IconHelper.loadIcon("icon_Script.gif"));
					sb.append("<html><b>Script</b> ");
					if (desc.length() >0)
                        sb.append(desc);
					sb.append("</html>");
					label.setText(sb.toString());
					label.setToolTipText(null);
				} 
				else if (value instanceof For){ 
					For f = (For)value;
					String var = f.getVar() == null? "" : f.getVar();
					String items = f.getItems() == null? "" : f.getItems();
					sb.append("<html><b>For</b>");
					if (var.length() > 0)
					    sb.append(" <i>").append(var).append("</i>");
					if (items.length() > 0)
                        sb.append(" <b>in</b> ").append(items);
					sb.append("</html>");
					label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
					label.setText(sb.toString());
					label.setToolTipText(null);
				}   		
				else if (value instanceof While){
					While w = (While)value;
					String test = w.getTest() == null ? "" : w.getTest();
					label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
					sb.append( "<html><b>While</b>");
					if (test.length() > 0)
						sb.append( test);
					sb.append("</html>");
					label.setText(sb.toString());
					label.setToolTipText(null);
				}
				else if (value instanceof Parfor){
					Parfor p = (Parfor)value;
					String var = p.getVar() == null? "" : p.getVar();
					String items = p.getItems() == null? "" : p.getItems();
                    sb.append("<html><b>Parallel For</b>");
                    if (var.length() > 0)
                        sb.append(" <i>").append(var).append("</i>");
                    if (items.length() > 0)
                        sb.append(" <b>in</b> ").append(items);
                    sb.append("</html>");
					label.setIcon(IconHelper.loadIcon("icon_Loop.gif"));
					label.setText(sb.toString());
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
					sb.append("<html><b>If</b>");
					if (test.length() > 0) 
						sb.append(test);
					sb.append("</html>");
					label.setText(sb.toString());
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
					//Origional idea was to use scrollpane holding editorpane, however the custom
					//renderer just renders and does not allow user interaction - the scrollbars 
					//do not register inputs i.e. cannot scroll, hence use of textarea sized to fit 
					//script length.
					//JEditorPane jEditorPane = new JEditorPane();
					//jEditorPane.setText(value.toString());
					//jEditorPane.setCaretPosition(0);
					//jEditorPane.setEditable(false);
					String text = value.toString();
                    int lineCt = StringUtils.split(text,'\n').length ;
                    jta.setRows(lineCt);
					jta.setText(text);
					return jta;
				}
				else {    			
					label.setText("to do");
				}   		
				return(label);																	 
	}
}
