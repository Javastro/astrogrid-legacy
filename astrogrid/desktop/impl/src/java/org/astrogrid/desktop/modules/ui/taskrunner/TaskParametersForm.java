/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.net.URI;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsImpl;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.JEventListPanel;

/**Form which allows parameters of a tool document to be edited.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 3, 200710:23:22 AM
 */
public class TaskParametersForm extends JPanel implements ItemListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(TaskParametersForm.class);

	private final ApplicationsInternal apps;
	private final UIComponent parent;
	private final EventList inputElements;
	private final EventList outputElements;
	private final JComboBox interfaceChooser;
	private final JLabel resourceLabel;
	
	private final NumberFormat integerFormat;
	private final NumberFormat floatFormat;
	private final MouseListener hoverListener;
	private final ResourceChooserInternal fileChooser;
	/* the internal model */
	private Tool tool;
	/** current metadata */
	private CeaApplication currentResource;
	private Image currentResourceLogo;
	
	
	/** returns the information for the current resource being edited */
	public CeaApplication getCurrentResource() {
		return currentResource;
	}
	
	/** returns a label which displays the name and icon of the current resouce being edited */
	public JLabel getResourceLabel() {
		return resourceLabel;
	}
	public Image getCurrentResourceLogo() {
		return currentResourceLogo;
	}
	
	/** access the interface combo box - host needs to display this somewhere */
	public JComboBox getInterfaceCombo() {
		return interfaceChooser;
	}
	
	/** access the model - all changes are live on this */
	public Tool getTool() {
		return tool;
	}

	public TaskParametersForm(final UIComponent parent,final ApplicationsInternal apps, MouseListener hoverListener,ResourceChooserInternal fileChooser) {
		super();
		this.parent = parent;
		this.apps = apps;
		this.hoverListener = hoverListener;
		this.fileChooser = fileChooser;
		//@todo these formatters aren't strict enough - they seem
		// to accept anything as long as it starts with a digit.
		integerFormat = NumberFormat.getIntegerInstance();
		integerFormat.setMaximumFractionDigits(0);
		floatFormat = NumberFormat.getNumberInstance();
		
		this.inputElements = new BasicEventList();
		this.outputElements = new BasicEventList();
		
		JEventListPanel inputsPanel = new JEventListPanel(inputElements, new ElementFormat());
		JEventListPanel outputsPanel = new JEventListPanel(outputElements, new ElementFormat());

		interfaceChooser = new JComboBox();
		interfaceChooser.setEditable(false);
		interfaceChooser.addItemListener(this);
		interfaceChooser.addMouseListener(hoverListener);
		
		resourceLabel = new JLabel();
		resourceLabel.setText("No task selected");
		resourceLabel.addMouseListener(hoverListener);
		
		JScrollPane inScroll = new JScrollPane(inputsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		inScroll.setBorder(BorderFactory.createTitledBorder("Inputs")); //@todo work out border colouring later.
		JScrollPane outScroll = new JScrollPane(outputsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		outScroll.setBorder(BorderFactory.createTitledBorder("Outputs"));
		this.setLayout(new GridLayout(1,2));
		this.setBorder(BorderFactory.createEmptyBorder());
		this.add(inScroll);
		this.add(outScroll);
	}


	/** build a form with controls to edit parameters for the default interface of this application */
	public void buildForm(CeaApplication applicationResource) {
		String iName = applicationResource.getInterfaces()[0].getName();
		buildForm(iName,applicationResource);
	}
	

	/** build a form with controls to edit parameters for the specified interface of this application */
	public void buildForm(String interfaceName,CeaApplication applicationResource) {
		Tool t = apps.createTemplateTool(interfaceName,applicationResource);
		buildForm(t,interfaceName,applicationResource);
		
	}
	/** populates the contents of the current model 
	 * 	 * */
	public void buildForm(Tool values,String interfaceName,CeaApplication applicationResource) {
		// if we've got this far, the interfacename is valid. nogthing stopping us now.
		currentResource = applicationResource;
		currentResourceLogo = null;
		tool = values;
		// populate the label.
		resourceLabel.setText(currentResource.getTitle());
		resourceLabel.setIcon(null);
		doFetchIcon();
		
		// populate the top combo box.
		// stop listening to the interface chooser for a moment.
		try {
			interfaceChooser.removeItemListener(this);
			interfaceChooser.removeAllItems();
			InterfaceBean[] ibs = applicationResource.getInterfaces();
			for (int i = 0; i < ibs.length; i++) {
				interfaceChooser.addItem(ibs[i].getName());			
			}
			interfaceChooser.setEnabled(ibs.length > 1);
			interfaceChooser.setSelectedItem(interfaceName);
		} finally {
			interfaceChooser.addItemListener(this);
		}
		
		// now build the form.
		doBuildForm(interfaceName);
	}	
	

	/** not part of the public api 
	 * 
	 * callback from the combo box - trigger a populate action
	 */
	public void itemStateChanged(ItemEvent e) {
	
		if (e.getSource() != interfaceChooser || e.getStateChange() != ItemEvent.SELECTED) {
			return;
		}
		String interfaceName = (String)interfaceChooser.getSelectedItem();
		// create a template tool.
		tool = apps.createTemplateTool(interfaceName,currentResource);	
		// build the form.
		doBuildForm(interfaceName);
	}
	
	private void doBuildForm(String interfaceName) {
		logger.info("Populating from " + interfaceName + "@" + currentResource.getId());	
		// clear the element list list.
		inputElements.clear();
		outputElements.clear();
		
		// analyze the currentResource
		Map descriptions = createDescriptionMap(currentResource);
		Map refs = createInterfaceMap(ApplicationsImpl.findInterface(interfaceName,currentResource.getInterfaces()));
		
		ParameterValue[] pvals = tool.getInput().getParameter();
		
		// check for RA,DEC like parameters. a bit tricky.
		ParameterValue ra = null;
		ParameterValue dec = null;
		for (int i = 0; i < pvals.length; i++) {
			final ParameterValue pv = pvals[i];
			final ParameterBean pb = (ParameterBean)descriptions.get(pv.getName());
			if (pb.getType().equalsIgnoreCase("ra") 
					|| (pb.getType().equals("double") && "POS_EQ_RA_MAIN".equals(pb.getUcd()))) {
				// check it's not a repeating or fiddly thing thing like that. - too complex.
				ParameterReferenceBean ref = (ParameterReferenceBean)refs.get(pv.getName());
				if (ref.getMax() == 1 && ref.getMin() == 1) {
					ra = pv;
				}
			}
			if (pb.getType().equals("dec") 
					|| (pb.getType().equals("double") && "POS_EQ_DEC_MAIN".equals(pb.getUcd()))) {
				ParameterReferenceBean ref = (ParameterReferenceBean)refs.get(pv.getName());
				if (ref.getMax() == 1 && ref.getMin() == 1) {
					dec = pv;
				}
			}			
		}
		if (ra != null && dec != null) { // good - found both.
			ParameterBean raDesc = (ParameterBean)descriptions.get(ra.getName());
			ParameterBean decDesc = (ParameterBean)descriptions.get(dec.getName());
			inputElements.add(new PositionFormElement(ra,raDesc,dec,decDesc));
		} else {
			// might have found one - reset it, so it's not lost at the next step.
			ra = null;
			dec = null;
		}
		
		// process the rest of the parameters.
		for (int i = 0; i < pvals.length; i++) {
			final ParameterValue pv = pvals[i];
			if (pv == ra || pv == dec) {
				continue; // already handled these ones.
			}
			AbstractTaskFormElement el = createInputFormElement(pv,(ParameterBean)descriptions.get(pv.getName()));
			ParameterReferenceBean ref = (ParameterReferenceBean)refs.get(pv.getName());
			inputElements.add(el);
			if (ref.getMax() != 1 || ref.getMin() != 1) {
				new MultipleInputParameterManager(el,ref); // takes care of adding / removing parameters.
			}
		}
		
		// now the outputs
		pvals = tool.getOutput().getParameter();
		for (int i = 0; i < pvals.length; i++) {
			final ParameterValue pv = pvals[i];
			AbstractTaskFormElement el = createOutputFormElement(pv,(ParameterBean)descriptions.get(pv.getName()));
			ParameterReferenceBean ref = (ParameterReferenceBean)refs.get(pv.getName());
			outputElements.add(el);
			if (ref.getMax() != 1 || ref.getMin() != 1) {
				new MultipleOutputParameterManager(el,ref); // takes care of adding / removing parameters.
			}
		}
	}

	/** fetch the icon for this application in a background thread.
	 * store it in the currentResourceLogo field,
	 * and update the resourceLabel.
	 */
	private void doFetchIcon() {
		// check we've got enough metadata to work from.
		if (currentResource == null) { 
			return;
		}
		final Creator[] creators = currentResource.getCuration().getCreators();
		if (creators.length ==0) {
			return;
		}
		// find the first creator with a logo.
		for (int i = 0; i < creators.length; i++) {
			final URI logoURI = creators[i].getLogoURI();
			if (logoURI != null) {
				(new BackgroundWorker(parent,"Fetching creator logo") {
					
					protected Object construct() throws Exception {
						currentResourceLogo = IconHelper.loadIcon(logoURI.toURL()).getImage();
						return new ImageIcon(currentResourceLogo.getScaledInstance(-1,24,Image.SCALE_SMOOTH));

					}
					protected void doFinished(Object result) {
						if (result != null) {
							Icon i = (Icon)result;
							resourceLabel.setIcon(i);
						}
					}
					protected void doError(Throwable ex) {
						// ignore.
					}
				}).start();
				break; // no need to scan the restof the creators.
			}
		}
	}
		
	
	/** work out what kind of editing widget to return for this value.
	 * @param value
	 * @param desc
	 * @return
	 */
	private AbstractTaskFormElement createInputFormElement(ParameterValue value, ParameterBean desc) {
		String type = desc.getType();	
		AbstractTaskFormElement el = null;
		if (type.equalsIgnoreCase("boolean")) {
			el =  new BooleanFormElement(value,desc,fileChooser);
		} else if (type.equalsIgnoreCase("fits") || type.equalsIgnoreCase("binary")) {
			el =  new BinaryFormElement(value,desc,fileChooser);
		} else if (desc.getOptions() != null && desc.getOptions().length > 0) {
			el =  new EnumerationFormElement(value,desc,fileChooser);
		} else if (type.equalsIgnoreCase("anyxml") || type.equalsIgnoreCase("adql") || type.equalsIgnoreCase("votable")){
			el =  new LargeTextFormElement(value,desc,fileChooser);
		} else if (type.equalsIgnoreCase("anyuri")) {
			el =  new LooselyFormattedFormElement(value,desc,new URIFormat(),fileChooser);
		} else if (type.equalsIgnoreCase("integer")) {
			el=  new LooselyFormattedFormElement(value,desc,integerFormat,fileChooser);
		} else if (type.equalsIgnoreCase("real") || type.equalsIgnoreCase("double") 
					|| type.equalsIgnoreCase("ra") || type.equalsIgnoreCase("dec")) {
			el =  new LooselyFormattedFormElement(value,desc,floatFormat,fileChooser);
		} else {
			el =  new TextFormElement(value,desc,fileChooser);
		}
		el.getLabel().addMouseListener(hoverListener);
		//el.getEditor().addMouseListener(hoverListener); // doesn't work with it's children.
		return el;
	}

	/** work out what kind of editing widget to return for this value.
	 * @param value
	 * @param desc
	 * @return
	 */
	private AbstractTaskFormElement createOutputFormElement(ParameterValue value, ParameterBean desc) {
		AbstractTaskFormElement el = new OutputFormElement(value,desc,fileChooser);
		el.getLabel().addMouseListener(hoverListener);
	//	el.getEditor().addMouseListener(hoverListener);
		return el;		
	}
	/** helper method - temporarily create a map of the parameter descritpions - makes it 
	 * easier to work with them
	 * @param applicationResource
	 */
	private Map createDescriptionMap(CeaApplication applicationResource) {
		ParameterBean[] descriptions = applicationResource.getParameters();
		Map descriptionMap = new HashMap();
		for (int i = 0; i < descriptions.length; i++) {
			descriptionMap.put(descriptions[i].getName(),descriptions[i]);
		}
		return descriptionMap;
	}
	/** helper method - ceeate a map of paramete references of rthe inputs and outputs */
	private Map createInterfaceMap(InterfaceBean iface) {
		Map m = new HashMap();
		ParameterReferenceBean[] refs = iface.getInputs();
		for (int i = 0; i < refs.length; i++) {
			m.put(refs[i].getRef(),refs[i]);
		}
		refs = iface.getOutputs();
		for (int i = 0; i < refs.length; i++) {
			m.put(refs[i].getRef(),refs[i]);
		}		
		return m;
	}

	/** manages a repeatable/optional input parameter */
	private class MultipleInputParameterManager extends MultipleParameterManager {

		public MultipleInputParameterManager(AbstractTaskFormElement el, ParameterReferenceBean ref) {
			super(inputElements,el, ref);
		}

		protected void addParameter(ParameterValue val) {
			tool.getInput().addParameter(val);
		}

		protected void removeParameter(ParameterValue val) {
			tool.getInput().removeParameter(val);
		}
		protected AbstractTaskFormElement cloneFormElement(AbstractTaskFormElement el) {
			return createInputFormElement(clone(el.getValue()),el.getDescription());
		}
	}
	
	/** manages a repeatable/optional output parameter */
	private class MultipleOutputParameterManager extends MultipleParameterManager {

		public MultipleOutputParameterManager(AbstractTaskFormElement el, ParameterReferenceBean ref) {
			super(outputElements,el, ref);
		}

		protected void addParameter(ParameterValue val) {
			tool.getOutput().addParameter(val);
		}

		protected void removeParameter(ParameterValue val) {
			tool.getOutput().removeParameter(val);
		}

		protected AbstractTaskFormElement cloneFormElement(AbstractTaskFormElement el) {
			return createOutputFormElement(clone(el.getValue()),el.getDescription());
		}
	}
	
	/** additional logic that manages an optional / repeated / multiple parameter 
	 * it's this class's responsibility to provide the correct add/remove buttons.
	 * Each instancee of this class manages one optional/repeated parameter
	 */
	private static abstract class MultipleParameterManager implements ActionListener {
		private final ParameterReferenceBean ref;
		/** the list (input or output) containing all elements */
		private final List elementList;
		/** the list contaning all the elelemtns this manager is looking after */
		private final ArrayStack myElementStack = new ArrayStack();
		/**
		 * @param elementList the list the element belongs to (either inputs or outputs)
		 * @param el the optional / repeated elemenet
		 * @param ref information on how this parameter may be repeated.
		 */
		public MultipleParameterManager(List elementList, AbstractTaskFormElement el, ParameterReferenceBean ref) {
			this.ref = ref;
			this.elementList = elementList;
			myElementStack.push(el);
			// work out from the reference what we're allowed to do.
			switch (ref.getMin()) {
				case 0: // optional.
					supplyOptionalButton(el);
					break;
				case 1: // do nothing
					break;
				default : // needs to be more than one.
					supplyAdditionalRows(el);
					return; // don't want to consider max() in this case.
			}
			if (ref.getMax() != 1) {
				supplyAddButton(el);
			}
			// finally, if it's optional, start it off as disabled.
			if (el.getOptionalButton()!= null) {
				el.getOptionalButton().doClick();
			}
			// harrumph. now need to tell the list that we've changed.
			// easiest way is just to re-insert.
			int ix = elementList.indexOf(el);
			elementList.set(ix,el);
		}
		/** adds a button to the compoonent to make it 'optional' / remove it. */
		private void supplyOptionalButton(AbstractTaskFormElement el) {
			JToggleButton optionalButton = new JToggleButton(IconHelper.loadIcon("editdelete16.png"));
			optionalButton.setToolTipText("Remove this parameter");
			optionalButton.setActionCommand("optional");
			optionalButton.addActionListener(this);
			el.setOptionalButton(optionalButton);
		}
		
		/** supply a 'remove' button and splice it into the form element */
		private void supplyRemoveButton(AbstractTaskFormElement el) {
			JButton removeButton = new JButton(IconHelper.loadIcon("editremove16.png"));
			removeButton.setToolTipText("Remove this parameter");
			removeButton.setActionCommand("remove");
			removeButton.addActionListener(this);
			el.setRemoveButton(removeButton);
		}		
		
		/** supply a bunch of additional required rows */
		private void supplyAdditionalRows(AbstractTaskFormElement el) {
			// looping from 1, as we've already got one.
			AbstractTaskFormElement last = el;
			int ix = elementList.indexOf(el);
			for (int i = 1; i < ref.getMin(); i++) {
				last = cloneFormElement(el);
				last.getLabel().setVisible(false); // don't want this for a repeated elelemnt				
				addParameter(last.getValue());
				myElementStack.push(last);
				elementList.add(ix+i,last);
			}
			if (ref.getMax() == 0) { // we can have still more - blimey!
				supplyAddButton(last);
			}
		}
		
		/** create an 'add' button and splice it into the formElement */
		private void supplyAddButton(AbstractTaskFormElement el) {
			JButton addButton = new JButton(IconHelper.loadIcon("editadd16.png"));
			addButton.setToolTipText("Repeat this parameter");
			addButton.setActionCommand("add");
			addButton.addActionListener(this);
			el.setAddButton(addButton);
		}
		/** handles button clicks. */
		public void actionPerformed(ActionEvent e) {
			// unpack the event.
			AbstractButton butt = (AbstractButton)e.getSource();
			AbstractTaskFormElement src = (AbstractTaskFormElement)butt.getClientProperty(AbstractTaskFormElement.class);
						
			// work out what the command was
			String action = butt.getActionCommand();
			if (action.equals("add")) {
				// adjust buttons on the originating element.
				src.getAddButton().setEnabled(false); // already added.
				if (src.getRemoveButton() != null) {
					src.getRemoveButton().setEnabled(false); // remove the new one first.
				}
				if (src.getOptionalButton() != null) {
					src.getOptionalButton().setEnabled(false);
				}
				// create the new element
				AbstractTaskFormElement nu = cloneFormElement(src);
				nu.getLabel().setVisible(false); // don't want this for a repeated elelemnt
				addParameter(nu.getValue());
				supplyRemoveButton(nu);
				myElementStack.push(nu);
				if (ref.getMax() == 0 || myElementStack.size() < ref.getMax()) {
					// we can have more yet.
					supplyAddButton(nu);
				}
				// splice into the list.
				int ix = elementList.indexOf(src) + 1;
				elementList.add(ix,nu);
			} else if (action.equals("remove")) {
					elementList.remove(src);
					removeParameter(src.getValue());
					// must be the head of the stack - discard it.
					myElementStack.pop();
					// get the new head out.
					AbstractTaskFormElement top = (AbstractTaskFormElement)myElementStack.peek();
					// enable it's buttons.
					top.getAddButton().setEnabled(true); // obviously allowed, as must have been one added before.
					if (ref.getMin() != myElementStack.size()) {// can remove, or is optional.
						if (top.getRemoveButton() != null) {
							top.getRemoveButton().setEnabled(true);
						}
						if (top.getOptionalButton() != null) {
							top.getOptionalButton().setEnabled(true);
						}
					}
			} else if (action.equals("optional")) {
				JToggleButton tb = (JToggleButton)butt;
				if (tb.isSelected()) { // removed.
					src.setEnabled(false);
					removeParameter(src.getValue());
				} else { //added
					src.setEnabled(true);
					addParameter(src.getValue());				
				}
			}
		}
		
		/** create a new copy of this form eleemt */
		protected abstract AbstractTaskFormElement cloneFormElement(AbstractTaskFormElement el);
		/** abstract method - override to remove a parameter from the appropriate 
		 * place in the tool document
		 * @param val
		 */
		/** duplicate a parameter value */
		protected ParameterValue clone(ParameterValue val) {
			ParameterValue c = new ParameterValue();
			c.setEncoding(val.getEncoding());
			c.setIndirect(val.getIndirect());
			c.setName(val.getName());
			c.setValue(val.getValue());
			return c;
		}
		protected abstract void removeParameter(ParameterValue val);
		/** abstract method - override to add a parameter to the appropriate place
		 * in the tool document.
		 * @param val
		 */
		protected abstract void addParameter(ParameterValue val);
	}

	private static class ElementFormat extends JEventListPanel.AbstractFormat {
		public ElementFormat() {
			super("d,1dlu,d" ,"fill:max(60dlu;pref):grow,6dlu,d,d,d","3dlu","0dlu"
					,new String[]{"1,1,4,1","1,3","3,3","4,3","5,3","5,1"});
		}
		public JComponent getComponent(Object o, int ix) {
			if (o instanceof JComponent) {
				JComponent e = (JComponent)o;
				return ix == 0 ? e : null;
			} else if (o instanceof AbstractTaskFormElement) {
				AbstractTaskFormElement e = (AbstractTaskFormElement)o;
				switch(ix) {
				case 0:
					return e.getLabel();
				case 1:
					return e.getEditor();
				case 2:
					return e.getIndirectToggle();
				case 3:
					return e.getAddButton();
				case 4:
					return e.getRemoveButton();
				case 5:
					return e.getOptionalButton();
				default:
					return new JLabel("invalid index");
				}
			} else {
				// should never happen.
				return new JLabel("unexpected component type");
			}
		}
		public int getComponentsPerElement() {
			return 6;
		}
	}



	
}
