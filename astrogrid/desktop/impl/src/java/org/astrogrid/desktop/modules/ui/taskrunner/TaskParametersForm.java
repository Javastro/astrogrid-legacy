/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URI;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.adqlEditor.ADQLEditorPanel;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.comp.ExpandCollapseButton;
import org.astrogrid.desktop.modules.ui.comp.MyTitledBorder;
import org.astrogrid.desktop.modules.ui.comp.PinnableLabel;
import org.astrogrid.desktop.modules.ui.taskrunner.ParamBuilder.Param;
import org.astrogrid.workflow.beans.v1.Tool;

import ca.odell.glazedlists.CompositeList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.JEventListPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** {@link JPanel} which allows parameters of a tool document to be edited.
 * Displays as two columns - left hand column is input parameters.
 * Right hand column is a split pane, with outputs above, and a parameter documentation area below.
 * 
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
	private final UIComponentWithMenu parent;
	private final EventList<AbstractTaskFormElement> inputElements;
	private final EventList<AbstractTaskFormElement> outputElements;
	private final JComboBox interfaceChooser;
	private final JToggleButton resourceLabel;
	private final JPanel bottomPanel;
	
	private final NumberFormat integerFormat;
	private final NumberFormat floatFormat;
	/* the internal model */
	private final Model model = new Model();
	/* constructs ui components */
    private final TypesafeObjectBuilder builder;
/** parameter help display */
    private final ParametersInfoPane infoPane;

private final LocalFileUploadAssistant uploadAssist;

private final CellConstraints cc;

private final PanelBuilder pb;

private final FormLayout fl;

private Dimension previousSize = null;
private Dimension preservedPreferredSize = null ;
	
	
	/**
     * @return the model
     */
    public final Model getModel() {
        return this.model;
    }

    public JPanel getBottomPane() {
	    return bottomPanel;
	} 
	
    /** if theres an adql editor in the tool currently being editor
     * method will show or hide the full adql editor.
     * @param b
     */
    public void setExpanded(final boolean b) {
        logger.debug("Setting expanded to " + b);
        final AdqlTextFormElement el = getAdqlFormElement();
        if (el == null) {
            return;
        }

        final AbstractButton butt = el.getOptionalButton();
        if (butt != null && butt instanceof ExpandCollapseButton) {
            logger.debug("found button");
            final ExpandCollapseButton ec = (ExpandCollapseButton) butt;
            if (ec.isSelected() != b) {
                logger.debug("state differs");
                ec.doClick();
            }

        }
        // belt-and-braces. there's an error in my logic somewhere
        // that means that the expansion state of the form and the button
        // get out-of-synch sometimes. this corrects it.
        getBottomPane().setVisible( b);
    }
    
    /** return a reference to the adql text form, if one exitsts, else null */
    public AdqlTextFormElement getAdqlFormElement() {
        for (final Iterator<AbstractTaskFormElement> i = inputElements.iterator(); i.hasNext();) {
            final AbstractTaskFormElement el = i.next();
            if (el instanceof AdqlTextFormElement) {
                return (AdqlTextFormElement)el;            
            }
        }
        return null;
                   
    }

	
	/** returns a label which displays the name and icon of the current resouce being edited */
	public JComponent getResourceLabel() {
		return resourceLabel;
	}

	/** access the interface combo box - host needs to display this somewhere */
	public JComboBox getInterfaceCombo() {
		return interfaceChooser;
	}
	
	/** access the model - all changes are live on this */
	public Tool getTool() {
		return model.tool();
	}
	
	
	/** splice an externally created toolbar into the panel.*/
	public void setToolbar(final JPanel toolbar) {
	    pb.add(toolbar,cc.xyw(1,1,5));
	}
	/** splice an externally created right-most pane into the panel 
	 * 
	 * @param title title for the right-most pane
	 * @param rightPane compoent to display in right-most column. If set to null,
	 * the right column is removed from the layout instead. (HACK)
	 */
	public void setRightPane(final String title,final JComponent rightPane) {
	    if (rightPane == null) {
	        fl.removeColumn(5);
	        fl.removeColumn(4);
	    } else {
	        pb.addTitle("Execution",cc.xy(5,2));
	        pb.add(rightPane,cc.xy(5,3));
	    }
	}
	
	public TaskParametersForm(final UIComponentWithMenu parent, final ApplicationsInternal apps,final TypesafeObjectBuilder builder, final FileSystemManager vfs) {
		super();
		this.parent = parent;
		this.apps = apps;
		this.builder = builder;		
		//@todo these formatters aren't strict enough - they seem
		// to accept anything as long as it starts with a digit.
		integerFormat = NumberFormat.getIntegerInstance();
		integerFormat.setMaximumFractionDigits(0);
		floatFormat = NumberFormat.getNumberInstance();
		
		// datastructure of all inputs and outputs.
		final CompositeList<AbstractTaskFormElement> allElements = new CompositeList<AbstractTaskFormElement>(); // a view of all parameterrs.
		this.inputElements = allElements.createMemberList();
		allElements.addMemberList(inputElements);
		this.outputElements = allElements.createMemberList();
		allElements.addMemberList(outputElements);

		// help pane - create early, as it listens to various other components.
		infoPane = builder.createParametersInfoPane(model,allElements);
		infoPane.setBorder(BorderFactory.createEmptyBorder());
		CSH.setHelpIDString(infoPane,"task.info");
		final JScrollPane infoScroll = new JScrollPane(infoPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		infoScroll.setBorder(MyTitledBorder.createLined("Information")); 
		infoScroll.setPreferredSize(new Dimension(100,100));
		
		// file upload assistant. - create it, and then leave it alone to do it's thing.
		this.uploadAssist = new LocalFileUploadAssistant(parent,vfs,allElements);
		
		final JEventListPanel<AbstractTaskFormElement> inputsPanel = new JEventListPanel<AbstractTaskFormElement>(inputElements, new ElementFormat());
		inputsPanel.setBorder(BorderFactory.createEmptyBorder());
		final JEventListPanel<AbstractTaskFormElement> outputsPanel = new JEventListPanel<AbstractTaskFormElement>(outputElements, new ElementFormat());
		outputsPanel.setBorder(BorderFactory.createEmptyBorder());

		interfaceChooser = new JComboBox();
		interfaceChooser.setToolTipText("Change the set of parameters required to call this task");
		interfaceChooser.setEditable(false);
		interfaceChooser.addItemListener(this);
		CSH.setHelpIDString(interfaceChooser,"task.interfaceChooser");
		
		resourceLabel = new PinnableLabel("No task selected");
		resourceLabel.setToolTipText("Click to pin the overview documentation");
		infoPane.registerAdditional(resourceLabel);
		
		
		final JScrollPane inScroll = new JScrollPane(inputsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);		
		inScroll.setBorder(BorderFactory.createEtchedBorder());
		
		final JScrollPane outScroll = new JScrollPane(outputsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		outScroll.setBorder(BorderFactory.createEtchedBorder());
		this.setBorder(null);
		
		bottomPanel = new JPanel(new FlowLayout());
		bottomPanel.setBorder(null);		
		bottomPanel.setVisible(false);
		// listen to the frame changing size, and change preferred size of the adql pane accordingly
		//-means that all free page gets assignrf to the adql page.
        
		this.addComponentListener( new ComponentAdapter() {

		    @Override
            public void componentResized(final ComponentEvent e) {

		        if( bottomPanel.getComponentCount() == 0 
		                ||
		                bottomPanel.getComponent(0) instanceof ADQLEditorPanel == false ) {
		            // no adql panel present - no point continuing.
		            return;
		        }

		        if (previousSize == null) {
		            // First time called - on window paint.
		            // Just preserve the size...
		            previousSize = getSize();
		        }
		        else {
                    final ADQLEditorPanel ae = (ADQLEditorPanel)bottomPanel.getComponent(0) ;
		            reclaimScreenRealEstate( ae ) ; 
                    ae.revalidate() ; 
		        }

		    }
		});
		
		fl = new FormLayout(
				"fill:d:grow,3dlu,fill:d:grow,3dlu,d:grow" // cols
				,"d,d,fill:50dlu:grow,fill:d" // rows   - can't say 'grow' for the last row - as it takes up space even when not visible
				);
        fl.setColumnGroups(new int[][]{ {1, 3} });
		pb = new PanelBuilder(fl,this);
        cc = new CellConstraints();
        pb.addTitle("Inputs",cc.xy(1,2)).setToolTipText("This column lists the input parameters for the task");
		pb.addTitle("Outputs",cc.xy(3,2)).setToolTipText("This column lists the outputs of the task");
		pb.add(inScroll,cc.xy(1,3));
		final JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		rightSplit.setLeftComponent(outScroll);
		rightSplit.setRightComponent(infoScroll);
		rightSplit.setDividerLocation(250);
		rightSplit.setDividerSize(7);
		rightSplit.setBorder(null);
		rightSplit.setResizeWeight(0.7);
		pb.add(rightSplit,cc.xy(3,3));
		pb.add(bottomPanel,cc.xyw(1,4,5));
		
        // hide info pane when bottom pane appears.
		bottomPanel.addComponentListener(new ComponentAdapter() {
		        @Override
                public void componentShown(final ComponentEvent e) {
		        infoScroll.setVisible(false);
		        rightSplit.setDividerLocation(1.0);	
		        rightSplit.setDividerSize(0);		        
		    }
		        @Override
                public void componentHidden(final ComponentEvent e) {
		        infoScroll.setVisible(true);
		        rightSplit.setDividerLocation(0.5);		 
		        rightSplit.setDividerSize(7);		        
		    }
		});
	}
	

	/** build a form with controls to edit parameters for the default interface of this application */
	public void buildForm(final CeaApplication applicationResource) {
		final String iName = applicationResource.getInterfaces()[0].getName();
		buildForm(iName,applicationResource);
	}
	
	/** build a form with controls to edit parameters for the specified interface of this application */
	public void buildForm(final String interfaceName,final CeaApplication applicationResource) {
		final Tool t = apps.createTemplateTool(interfaceName,applicationResource);
		buildForm(t,interfaceName,applicationResource);	
	}

	public void clear() {
	    model.clear();
	    resourceLabel.setText("");
	    resourceLabel.setIcon(PinnableLabel.EMPTY_ICON);
	    interfaceChooser.removeItemListener(this);
	    interfaceChooser.removeAllItems();
	    interfaceChooser.setEnabled(false);
	    interfaceChooser.addItemListener(this);
	    inputElements.clear();
	    outputElements.clear();
	    infoPane.clear();
	}
	/** populates the contents of the current model 
	 * 	 * */
	public void buildForm(final Tool values,final String interfaceName,final CeaApplication applicationResource) {	    
	    if (model.currentResource() != null && model.currentResource().getId().equals(applicationResource.getId())) { //application is unchanged.
	        model.newApplication(applicationResource);
	        interfaceChooser.setSelectedItem(interfaceName);
	    } else { // it's a different application
            model.newApplication(applicationResource);	        
	        // populate the label.
	        resourceLabel.setText(model.currentResource().getTitle());
	        doFetchIcon();

	        // populate the top combo box.
	        // stop listening to the interface chooser for a moment.
	        try {
	            interfaceChooser.removeItemListener(this);
	            interfaceChooser.removeAllItems();
	            final InterfaceBean[] ibs = applicationResource.getInterfaces();
	            for (int i = 0; i < ibs.length; i++) {
	                interfaceChooser.addItem(ibs[i].getName());			
	            }
	            interfaceChooser.setEnabled(ibs.length > 1);
	            interfaceChooser.setSelectedItem(interfaceName);
	        } finally {
	            interfaceChooser.addItemListener(this);
	        }
	    }
		model.changeTool(values,interfaceName);
	}	
	
	/** not part of the public api 
	 * 
	 * callback from the combo box - trigger a populate action
	 */
	public void itemStateChanged(final ItemEvent e) {
		if (e.getSource() != interfaceChooser || e.getStateChange() != ItemEvent.SELECTED) {
			return;
		}
		final String interfaceName = (String)interfaceChooser.getSelectedItem();
		model.changeInterface(interfaceName); 
	}
    
    private void reclaimScreenRealEstate( final ADQLEditorPanel adqlEditor ) { 
                       
            //
            // OK. This is the ADQL Editor...
            // We've been resized. Calculate the difference between new and old sizes.
            // We do this just for the sake of resizing the ADQL editor,
            // where we need to set the preferred size to recapture screen real estate.
            Dimension curr = getSize();
            if( previousSize != null ) {
                final double diffH = curr.getHeight() - previousSize.getHeight() ;
                final double diffW = curr.getWidth() - previousSize.getWidth() ;
                previousSize = curr ;
                // now alter the size of the component.
                curr = adqlEditor.getPreferredSize() ;                   
                final Dimension nu = new Dimension((int)(curr.getWidth()+diffW),(int)(curr.getHeight() + diffH));
                adqlEditor.setPreferredSize(nu) ; 
                preservedPreferredSize = nu ;
            }
            else {
                previousSize = curr ;
                preservedPreferredSize = getPreferredSize() ;
            }
                          
    }
  
	
	
	/** internal model for the task runner
	 * contains the current resource, current tool, history of tools for other interfaces, etc.
	 * these are a bunch of related fields, which are safer to encapsulate
	 * togehter with some state-manipulating methods.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Aug 3, 20071:05:22 AM
	 * @TEST
	 */
	public class Model {
	    
	    private final Tool nullTool;
	    {
	        nullTool = new Tool();
	        // do I need to populate any more here?
	    }
	    private CeaApplication application;
	    private Tool tool=nullTool;
	    private String interfaceName;
	    /** map to cache some info about ui state of different form elements 
	     *  - a cache of interfaceName -> InterfaceState objects*/	    
	    private final Map<String, InterfaceState> m= new HashMap<String, InterfaceState>();
	    
	// state changing operations.
	    /** empty the model */
	    public void clear() {
	        logger.debug("clearing model");
	        application = null;
	        tool = nullTool;
	        interfaceName = null;
	        setExpanded(false);
	        m.clear();
	    }
	    /** setup model with new application */
	    public void newApplication(final CeaApplication app) {
	        clear();
	        logger.debug("Setting new application");
	        this.application = app;	      
	    }
	    
	    /** change the interface to another in the same application */
	    public void changeInterface(final String interfacename) {
	        logger.debug("Changing interface");
	        // first of all, record the final configuratio of this interface..
	        rememberCurrent();
	        // now onto the new interface
	        this.interfaceName = interfacename;
	     // see if we've got a previously worked-on tool for this interface.
	        final InterfaceState previously = m.get(interfaceName);
	        if (previously != null) {
	            if (logger.isDebugEnabled()) {
	                logger.debug("Found previous state for inteface" + interfaceName + " " + previously);
	            }
	            //grand. work from this on.e
	            tool = previously.tool;
	            // build the form.
	            doBuildForm(interfaceName);
	            // update the expansion state
	            setExpanded(! previously.collapsed);
	        } else { // create a new tool.
	            logger.debug("Creating new state for interface" + interfaceName);
	            // create a template tool.         
	            tool = apps.createTemplateTool(interfaceName,currentResource());
	            // build the form.
	            doBuildForm(interfaceName);	          
	            // update the expansion state.
	            setExpanded(false);
	            // record this new state.
	            m.put(interfaceName, new InterfaceState(tool));                       
	        }	        
	    }
        /**
         * remembers current edit state - do this before changing to another tool / interface
         */
        private void rememberCurrent() {
            if (interfaceName != null) { // else we weren'te editing anything previously.
	            final InterfaceState leaving = m.get(interfaceName);
	            if (leaving != null) {
	                leaving.collapsed = ! getBottomPane().isVisible();
	                logger.debug("Recording window state of interface we're leaving as " + leaving.collapsed);
	            }
	        }
        }
	    
	    /** change the tool to a different one in the same application */
	    public void changeTool(final Tool t, final String iName) {
	        rememberCurrent();
	        this.interfaceName = iName;
	        this.tool = t;
            // now build the form.
            doBuildForm(iName);
            // finally sort out the expansion state.
            setExpanded(false);	        
            m.put(iName, new InterfaceState(tool));  
	    }
	    
	    // // data accessors.
	    public CeaApplication currentResource() {
	        return application;
	    }	   
	    public URI currentResourceId() {
	        return application.getId();
	    }	    
	    public Tool tool() {
	        return tool;
	    }	    
	    /**used to keep state of of one interface */
	    private  class InterfaceState {
	        
	        public boolean collapsed = true;
	        public Tool tool = null;
	        public InterfaceState(final Tool tool) {
	            super();
	            this.collapsed = ! getBottomPane().isVisible();
	            this.tool = tool;
	        }
	        @Override
            public String toString() {
	            return "InterfaceState[" + tool.getName() + ", " + collapsed +"]";
	        }
	    }
        public final String getIName() {
            return this.interfaceName;
        }
        /**
         * 
         */
        public boolean isAdqlInterface() {
            for (final Iterator<AbstractTaskFormElement> i = inputElements.iterator(); i.hasNext();) {
                final AbstractTaskFormElement el = i.next();
                if (el instanceof AdqlTextFormElement) {
                    return true;
                }            
            }          
            return false;
        }	    
	}// end inner class 'Model'

    /** fetch the icon for this application in a background thread.
     * store it in the currentResourceLogo field,
     * and update the resourceLabel.
     * @todo move this into the model?
     */
    private void doFetchIcon() {
        // check we've got enough metadata to work from.
        if (model.currentResource() == null) { 
            return;
        }
        final Creator[] creators = model.currentResource().getCuration().getCreators();
        if (creators.length ==0) {
            return;
        }
        // find the first creator with a logo.
        for (int i = 0; i < creators.length; i++) {
            final URI logoURI = creators[i].getLogoURI();
            if (logoURI != null) {
                (new BackgroundWorker(parent,"Fetching creator logo",BackgroundWorker.SHORT_TIMEOUT,Thread.MIN_PRIORITY) {
                    
                    @Override
                    protected Object construct() throws Exception {
                        final Image currentResourceLogo = IconHelper.loadIcon(logoURI.toURL()).getImage();
                        return new ImageIcon(currentResourceLogo.getScaledInstance(-1,24,Image.SCALE_SMOOTH));

                    }
                    @Override
                    protected void doFinished(final Object result) {
                        if (result != null) {
                            final Icon i = (Icon)result;
                            resourceLabel.setIcon(i);
                        }
                    }
                    @Override
                    protected void doError(final Throwable ex) {
                        // ignore.
                    }
                }).start();
                break; // no need to scan the restof the creators.
            }
        }
    }
        
	/** called from within model methods to actually construct
	 * the  ui form 
	 * @param interfaceName
	 */
	private void doBuildForm(final String interfaceName) {
		logger.info("Populating from " + interfaceName + "@" + model.currentResourceId());	
		// clear the element list list.
		inputElements.clear();
		outputElements.clear();

		// build some metadata
		final ParamBuilder info = new ParamBuilder(interfaceName,model.currentResource(),model.tool());
		
		// handle special cases first
		// check for RA,DEC like parameters. a bit tricky.
		// and 'Radius' like parameters.
		// at the same time, check for a single ADQL parameter too.
		Param ra = null;
		Param dec = null;
		Param adql = null;
		Param radius = null;
		for (int i = 0; i < info.inputs.length; i++) { 
		    final Param p = info.inputs[i];
			if (p.isFiddly()) {
			    continue; // don't handle repeated or optional params. too fiddly.
			}
			final ParameterBean pb = p.description;
			if (pb.getType().equalsIgnoreCase("ra") 
					|| "POS_EQ_RA_MAIN".equals(pb.getUcd())
					|| "POS_RA_MAIN".equals(pb.getUcd())
					|| pb.getName().equalsIgnoreCase("RA")        
					) {
					ra = p;
			} else if (pb.getType().equalsIgnoreCase("dec") 
                    || "POS_EQ_DEC_MAIN".equals(pb.getUcd())
                    || "POS_DEC_MAIN".equals(pb.getUcd())
                    || pb.getName().equalsIgnoreCase("DEC")    			        
                    ){
					dec = p;
			} else 	if ("PHYS_SIZE_RADIUS".equals(pb.getUcd())
			        || pb.getName().equalsIgnoreCase("radius")) {
			    radius = p;
			} else 	if (pb.getType().equalsIgnoreCase("adql")) {
				adql = p;
			}
		}// end of scanning loop.
		
		// in all these following special cases, have already checked that max() and min() == 1 therefore values.length must == 1
		// check if we found enough for a Position element
		PositionFormElement posForm = null; // keep a reference to this, as needed by the radius editor.
		if (ra != null && dec != null) { // good - found a position
			posForm = builder.createPositionFormElement(
			        ra.values[0]
			       ,ra.description
			       ,dec.values[0]
			       ,dec.description
			       ,parent);
            inputElements.add(posForm);
		} else {
			// still might have found one of these - reset it, so it's not lost at the next step.
			ra = null;
			dec = null;
		}
		
		if (radius != null) {// found a radius. 
		    RadiusFormElement radForm;
		    if (posForm == null) { // ok, no associated position
		        radForm = builder.createRadiusFormElement(radius.values[0],radius.description);
		    } else {
                radForm = builder.createRadiusFormElement(radius.values[0],radius.description,posForm.getToggle());		        
		    }
		    inputElements.add(radForm);
		}
		
		if (adql != null) {
			// full adql editor..
			final AdqlTextFormElement adqlElement = builder.createAdqlTextFormElement(adql.values[0],adql.description,model.currentResource(),parent);
			final ADQLEditorPanel adqlEditor = adqlElement.getEditorPanel();
			adqlEditor.setBorder( null );
            if( previousSize == null && preservedPreferredSize == null ) {
                //
                // This is for the initial display of the ADQL editor...
                preservedPreferredSize = new Dimension(895,450) ;
                adqlEditor.setPreferredSize(new Dimension(895,450));                
            }
            else {
                //
                // This is required if we flip between panes and resize...
                adqlEditor.setPreferredSize( preservedPreferredSize ) ;
                reclaimScreenRealEstate( adqlEditor ) ;
            }
			bottomPanel.removeAll(); // might have an adql editor from a previous app.
			bottomPanel.add(adqlEditor);			
			// standard sized editor...
			// button to enable one editor or the other.
			final ExpandCollapseButton ep = new ExpandCollapseButton(bottomPanel);
	        CSH.setHelpIDString(ep,"task.form.adql");  
			ep.setToolTipText("Open full editor for this parameter");
			adqlElement.setOptionalButton(ep);
			// ep already shows / hides the fiull editor. now just need to make it do the same with the standard editor
			ep.addActionListener(new ActionListener() {			
				public void actionPerformed(final ActionEvent e) {
					adqlElement.setEnabled(! ep.isSelected());
				}
			});
			inputElements.add(adqlElement);
		}
	// end of the special cases
		// process the rest of the parameters.
		for (int i = 0; i < info.inputs.length; i++) {
		    final Param p = info.inputs[i];
			if (p == ra || p == dec || p == adql || p == radius) {
				continue; // already handled these ones.
			}
			final AbstractTaskFormElement el = createInputFormElement(p);
			inputElements.add(el);
			if (p.isFiddly()) {
				new FiddlyInputManager(el,p); // takes care of adding / removing parameters.
			}
		}
		
		// now the outputs
		for (int i = 0; i < info.outputs.length; i++) {
		    final Param p = info.outputs[i];
            final AbstractTaskFormElement el = createOutputFormElement(p);
			outputElements.add(el);
			if (p.isFiddly()) {
				new FiddlyOutputManager(el,p); // takes care of adding / removing parameters.
			}
		}
                
		infoPane.displayCurrentResource();

	}

	/** work out what kind of editing widget to return for this value.
	 * @param value
	 * @param desc
	 * @return
	 */
	private AbstractTaskFormElement createInputFormElement(final Param p) {
	    final ParameterBean desc = p.description;
	    final ParameterValue value;
	    if (p.values.length >0) { // at least one value provided - so use the first.
	        value = p.values[0];
	    } else { // no value provided - need to construct one of our own.
	        value = new ParameterValue();
	        value.setName(p.ref.getRef());
	        // leave value uninitialized - it's initialized in the individual formElement classes.
	        // depending on the parameter type.
	    }	        
	    return createInputFormElement(desc, value);
	}

    private AbstractTaskFormElement createInputFormElement(
            final ParameterBean desc, final ParameterValue value) {
        final String type = desc.getType();	
        if (type.equalsIgnoreCase("boolean")) {
			return  builder.createBooleanFormElement(value,desc);
		} else if (type.equalsIgnoreCase("fits") || type.equalsIgnoreCase("binary")) {
			return builder.createBinaryFormElement(value,desc);
		} else if (desc.getOptions() != null && desc.getOptions().length > 0) {
		 return  builder.createEnumerationFormElement(value,desc);
		} else if (type.equalsIgnoreCase("anyxml") || type.equalsIgnoreCase("adql") || type.equalsIgnoreCase("votable")){
			return  builder. createLargeTextFormElement(value,desc);
		} else if (type.equalsIgnoreCase("anyuri")) {
			return  builder.createLooselyFormattedFormElement(value,desc,new URIFormat());
		} else if (type.equalsIgnoreCase("integer")) {
		 return  builder.createLooselyFormattedFormElement(value,desc,integerFormat);
		} else if (type.equalsIgnoreCase("real") || type.equalsIgnoreCase("double") 
					|| type.equalsIgnoreCase("ra") || type.equalsIgnoreCase("dec")) {
			return  builder.createLooselyFormattedFormElement(value,desc,floatFormat);
		} else {
			return  builder.createTextFormElement(value,desc);
		}
    }

	/** work out what kind of editing widget to return for this value.
	 * @param value
	 * @param desc
	 * @return
	 */
	private AbstractTaskFormElement createOutputFormElement(final Param p) {
	    final ParameterBean desc = p.description;	    
        final ParameterValue value;
        if (p.values.length >0) { // at least one value provided - so use the first.
            value = p.values[0];
        } else { // no value provided - need to construct one of our own.
            value = new ParameterValue();
            value.setName(p.ref.getRef());
            // leave value uninitialized - it's initialized in the individual formElement classes.
            // depending on the parameter type.
        }	    
        return createOutputFormElement(desc, value);		
	}

    private AbstractTaskFormElement createOutputFormElement(
            final ParameterBean desc, final ParameterValue value) {
        return builder.createOutputFormElement(value,desc);
    }
	
	/** Additional logic that manages a fiddly parameter - one that is optional or repeatable. 
	 * it's this class's responsibility to provide the correct add/remove buttons.
	 * Each instancee of this class manages one optional/repeated parameter
	 * @TEST
	 */
	private static abstract class FiddlyParameterManager implements ActionListener {
		private final Param p;
		/** the list (input or output) containing all elements */
		private final List<AbstractTaskFormElement> elementList;
		/** the list contaning all the elelemtns this manager is looking after */
		private final ArrayStack myElementStack = new ArrayStack();
		/**
		 * @param elementList the list the element belongs to (either inputs or outputs)
		 * @param el the optional / repeated elemenet
		 * @param ref information on how this parameter may be repeated.
		 */
		public FiddlyParameterManager(final List<AbstractTaskFormElement> elementList, final AbstractTaskFormElement el, final Param p) {
		    this.p = p;
		    logger.debug(p.ref.getRef() + " is a fiddly parameter");
		    this.elementList = elementList;
		    final int pos = elementList.indexOf(el);
		    AbstractTaskFormElement current = null;
		    final int required = Math.max(1,Math.max(p.ref.getMax(),p.values.length)); // works 'cos p.ref.getMax()==0 when it means 'infinite'
		    logger.debug("" + required + " fields required"); 
		    for (int ix = 0; ix < required; ix++) {
		        // create/ find current
		        if (ix == 0) { // found
		            current = el;
		        } else { // create a copy
		            if (ix < p.values.length) { // got a value for this position
		                logger.debug("Cloning with value " + p.values[ix]);
		                current = cloneFormElement(current,p.values[ix]);
		            } else { // create with uninitialized value
		                logger.debug("Cloning with uninitialized value");
		                current = cloneFormElement(current,clone(current.pval));
		                addParameter(current.getValue()); // add into model - not already there.
		            }
		            current.getLabel().setVisible(false); // don't want this for repeated elements.
		            elementList.add(pos+ix,current); // add the new one into the list.
		        }
		        myElementStack.push(current); // store this with the rest of the elements managed by this class.

		        // work out  what we're allowed to do.
		        if (ix == 0 && p.ref.getMin() == 0) { // first field, and it's an optional param.
		            logger.debug("supplying optional button");
		            supplyOptionalButton(current);
		                // if we've got more than one value already, disable.
		                current.getOptionalButton().setEnabled(p.values.length < 2);		            
		        }

		        if (ix+1 >= p.ref.getMin() && (p.ref.getMax() == 0 || ix + 1 < p.ref.getMax())) {
		            logger.debug("supplying add button");
		            supplyAddButton(current);
		            current.getAddButton().setEnabled(ix+1 == required); // disable for all but last
		        }
		        if (ix != 0 && ix+1 > p.ref.getMin()) { 
		            logger.debug("supplying remove button");
		            supplyRemoveButton(current);
		            current.getRemoveButton().setEnabled(ix+1 == required); // disable for all but last.
		        }	
		        // notify things have changed by re-inserting into the list.
		        elementList.set(pos+ix,current);
		        
		    } // end for loop

		    // finally. handle configuration of optional button - at the end, so that all other components are in place
		    if (el.getOptionalButton() != null 
		            && (p.values.length == 0
		                    || StringUtils.isEmpty(p.values[0].getValue()))) {
		        logger.debug("disabling this param - no value");
		        current.getOptionalButton().doClick();
		    }
		}
		
		/** adds a button to the compoonent to make it 'optional' / remove it. */
		private void supplyOptionalButton(final AbstractTaskFormElement el) {
		//	JToggleButton optionalButton = new JToggleButton(IconHelper.loadIcon("remove16.png"));
			final JCheckBox optionalButton = new JCheckBox();
			optionalButton.setSelected(true);
			optionalButton.setToolTipText("This is an optional parameter: check to enable it");
			optionalButton.setActionCommand("optional");
			CSH.setHelpIDString(optionalButton,"task.form.optional");
			optionalButton.addActionListener(this);
			el.setOptionalButton(optionalButton);
		}
		
		/** supply a 'remove' button and splice it into the form element */
		private void supplyRemoveButton(final AbstractTaskFormElement el) {
			final JButton removeButton = new JButton(IconHelper.loadIcon("editremove16.png"));
			removeButton.setToolTipText("Remove this parameter");
			removeButton.setActionCommand("remove");
            CSH.setHelpIDString(removeButton,"task.form.remove");			
			removeButton.addActionListener(this);
			el.setRemoveButton(removeButton);
		}		

		
		/** create an 'add' button and splice it into the formElement */
		private void supplyAddButton(final AbstractTaskFormElement el) {
			final JButton addButton = new JButton(IconHelper.loadIcon("editadd16.png"));
			addButton.setToolTipText("Repeat this parameter");
			addButton.setActionCommand("add");
            CSH.setHelpIDString(addButton,"task.form.add");			
			addButton.addActionListener(this);
			el.setAddButton(addButton);
		}
		/** handles button clicks. */
		public void actionPerformed(final ActionEvent e) {
			// unpack the event.
			final AbstractButton butt = (AbstractButton)e.getSource();
			final AbstractTaskFormElement src = (AbstractTaskFormElement)butt.getClientProperty(AbstractTaskFormElement.class);
						
			// work out what the command was
			final String cmd = butt.getActionCommand();
			if (cmd.equals("add")) {
				// adjust buttons on the originating element.
				src.getAddButton().setEnabled(false); // already added.
				if (src.getRemoveButton() != null) {
					src.getRemoveButton().setEnabled(false); // remove the new one first.
				}
				if (src.getOptionalButton() != null) {
					src.getOptionalButton().setEnabled(false);
				}
				// create the new element
				final AbstractTaskFormElement nu = cloneFormElement(src,clone(src.pval));
				nu.getLabel().setVisible(false); // don't want this for a repeated elelemnt
				addParameter(nu.getValue());
				supplyRemoveButton(nu);
				myElementStack.push(nu);
				if (p.ref.getMax() == 0 || myElementStack.size() < p.ref.getMax()) {
					// we can have more yet.
					supplyAddButton(nu);
				}
				// splice into the list.
				final int ix = elementList.indexOf(src) + 1;
				elementList.add(ix,nu);
			} else if (cmd.equals("remove")) {
					elementList.remove(src);
					removeParameter(src.getValue());
					// must be the head of the stack - discard it.
					myElementStack.pop();
					// get the new head out.
					final AbstractTaskFormElement top = (AbstractTaskFormElement)myElementStack.peek();
					// enable it's buttons.
					top.getAddButton().setEnabled(true); // obviously allowed, as must have been one added before.
					if (p.ref.getMin() != myElementStack.size()) {// can remove, or is optional.
						if (top.getRemoveButton() != null) {
							top.getRemoveButton().setEnabled(true);
						}
						if (top.getOptionalButton() != null) {
							top.getOptionalButton().setEnabled(true);
						}
					}
			} else if (cmd.equals("optional")) {
				final JToggleButton tb = (JToggleButton)butt;
				if (! tb.isSelected()) { // removed.
					src.setEnabled(false);
					removeParameter(src.getValue());
				} else { //added
					src.setEnabled(true);
					addParameter(src.getValue());				
				}
			}
		}
				
		  /** create a new copy of this form eleemt */
        protected abstract AbstractTaskFormElement cloneFormElement(AbstractTaskFormElement el,ParameterValue newValue);
    
		/** duplicate a parameter value */
		private ParameterValue clone(final ParameterValue val) {
			final ParameterValue c = new ParameterValue();
			c.setEncoding(val.getEncoding());
			c.setIndirect(val.getIndirect());
			c.setName(val.getName());
			c.setValue(val.getValue());
			return c;
		}
		/** abstract method - override to remove a parameter from the appropriate 
		 * place in the tool document
		 * @param val
		 */
		protected abstract void removeParameter(ParameterValue val);
		/** abstract method - override to add a parameter to the appropriate place
		 * in the tool document.
		 * @param val
		 */
		protected abstract void addParameter(ParameterValue val);
	}

    /** manages a repeatable/optional input parameter */
    private class FiddlyInputManager extends FiddlyParameterManager {

        public FiddlyInputManager(final AbstractTaskFormElement el, final Param ref) {
            super(inputElements,el, ref);
        }

        @Override
        protected void addParameter(final ParameterValue val) {
            model.tool().getInput().addParameter(val);
        }

        @Override
        protected void removeParameter(final ParameterValue val) {
            model.tool().getInput().removeParameter(val);
        }

        @Override
        protected AbstractTaskFormElement cloneFormElement(
                final AbstractTaskFormElement el, final ParameterValue newValue) {
            return createInputFormElement(el.getDescription(),newValue);
        }
    }
    
    /** manages a repeatable/optional output parameter */
    private class FiddlyOutputManager extends FiddlyParameterManager {

        public FiddlyOutputManager(final AbstractTaskFormElement el, final Param ref) {
            super(outputElements,el, ref);
        }

        @Override
        protected void addParameter(final ParameterValue val) {
            model.tool().getOutput().addParameter(val);
        }

        @Override
        protected void removeParameter(final ParameterValue val) {
            model.tool().getOutput().removeParameter(val);
        }

        @Override
        protected AbstractTaskFormElement cloneFormElement(
                final AbstractTaskFormElement el, final ParameterValue newValue) {
            return createOutputFormElement(el.getDescription(),newValue);            
        }
    }
    
    // formatting of the ui forms.
    /** Formatter used to lay out a list of UI elements in a {@link JEventListPanel} */
	private static class ElementFormat extends JEventListPanel.AbstractFormat {
		public ElementFormat() {
			super("top:d,d" ,"22px,fill:60dlu:grow,0dlu,22px,22px,22px","1dlu","0dlu"
					,new String[]{"2,1,5,1","2,2","4,2","5,2","6,2","1,1"});
		}
		public JComponent getComponent(final Object o, final int ix) {
			if (o instanceof JComponent) {
				return ix == 0 ? ((JComponent)o) : null;
			} else if (o instanceof AbstractTaskFormElement) {
				final AbstractTaskFormElement e = (AbstractTaskFormElement)o;
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
		@Override
        public int getComponentsPerElement() {
			return 6;
		}
	}


	
}
