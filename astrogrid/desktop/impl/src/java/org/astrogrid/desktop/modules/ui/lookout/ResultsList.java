/**
 * 
 */
package org.astrogrid.desktop.modules.ui.lookout;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.recorder.ResultsExecutionMessage;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.dnd.PreferredTransferable;
import org.astrogrid.desktop.modules.ui.sendto.ParameterValueTransferable;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.exolab.castor.xml.CastorException;

/** List for displaying results.
 * @author Noel Winstanley
 * @since Jun 21, 20064:54:45 PM
 */
public class ResultsList extends JList {

	
	public ResultsList(SendToMenu m, Myspace ms, final UIComponent parent) {
		this.ms = ms;
		this.model = new DefaultListModel();
		super.setModel(model);
		this.menu = m;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//setVisibleRowCount(2);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				PreferredTransferable atom = (PreferredTransferable)getSelectedValue();
				menu.show(atom,parent,ResultsList.this,e.getX(),e.getY());
			}
		});
		
		setCellRenderer(new ListCellRenderer() {
			final StringBuffer sb = new StringBuffer();
			final JLabel theComponent;
			{
				theComponent = new JLabel();
				theComponent.setIcon(IconHelper.loadIcon("sendto16.png"));
				theComponent.setBorder(BorderFactory.createEtchedBorder());
			}
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				ParameterValueTransferable t = (ParameterValueTransferable)value;
				ParameterValue val = t.getValue();
				sb.setLength(0);
				sb.append("<html><b>")
					.append(val.getName())
					.append("</b><br>");
				if (val.getIndirect()) {
					sb.append("<i>at</i> ");
					sb.append(val.getValue());
				}
				sb.append("</html>");
				theComponent.setText(sb.toString());
				return theComponent;
			}
		});
	}
	private final DefaultListModel model;
	private final SendToMenu menu;
	private final Myspace ms;
	
	public void clear() {
		clearSelection();
		model.clear();
	}
	
	public void setResults(ResultsExecutionMessage m) {
		model.clear();
		ResultListType list =  m.getResults();
		for (int i = 0; i < list.getResultCount(); i++) {
			model.addElement(new ParameterValueTransferable(list.getResult(i),ms));
		}
		//@todo clean this up, to make detection more reliable.
		if (list.getResultCount() == 1 && "transcript".equals(list.getResult(0).getName())) { // we've got a workflow - so add in all the intermediate results too...
			Reader r = new StringReader(list.getResult(0).getValue());
			try {
				Workflow wf = Workflow.unmarshalWorkflow(r);
				org.astrogrid.workflow.beans.v1.Step s = null;
				
				Iterator i = wf.findXPathIterator("//stepExecutionRecord/message[source='CEA' and phase='COMPLETED']"); //@heuristic to find results message.
				while (i.hasNext()) {
					MessageType rec = (MessageType)i.next();
					ResultListType stepResults = ResultListType.unmarshalResultListType(new StringReader(rec.getContent()));
					for (int j =0; j < stepResults.getResultCount(); j++) {
						model.addElement(new ParameterValueTransferable(stepResults.getResult(j),ms));
					}
				}
			} catch (CastorException e) {
				// doesn't matter.
			}
		}
	}
	
	
	
}
