/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.Vosi;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.CapabilityTester;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;
import org.astrogrid.desktop.modules.votech.Annotation;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.AnnotationSource;
import org.astrogrid.desktop.modules.votech.UserAnnotation;
import org.astrogrid.desktop.modules.votech.AnnotationService.AnnotationProcessor;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.FunctionList.Function;
import ca.odell.glazedlists.swing.JEventListPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** A {@link EditableResourceViewer} that displays formatted HTML of a resource, and also it's annotations.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:32:38 PM
 */
public class AnnotatedResourceViewer extends ResourceDisplayPane implements EditableResourceViewer, AnnotationProcessor, ActionListener, ChangeListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(AnnotatedResourceViewer.class);

    public AnnotatedResourceViewer(final HyperlinkListener hyper, final AnnotationService annService, final CapabilityTester tester,final Vosi vosi) {
		super(hyper, tester,vosi);
		this.annService = annService;
		userSource = annService.getUserAnnotationSource();
	
		
		// timer that triggers loading of further informatio when user has 'lingered' long enough.
		lingerTimer = new Timer(1000,this); //@todo make the linger time a peference.			
		lingerTimer.setCoalesce(true);
		lingerTimer.setRepeats(false);
		
		hyperLinkHandler = getHyperlinkListeners()[0];	
		CSH.setHelpIDString(this, "reg.details");

		
		// sort the list, then map into JPanels.
		final FunctionList annotationsPanels = new FunctionList(new SortedList(annotations,new Comparator() {
			// sort annotations.
			public int compare(final Object arg0, final Object arg1) {
				if (arg0 instanceof UserAnnotation && ! (arg1 instanceof UserAnnotation)) {
					return -1;
				} else if (! (arg0 instanceof UserAnnotation) && arg1 instanceof UserAnnotation) {
					return 1;
				} else {
					// both of the same type  - sort by name
					return ((Annotation)arg0).getSource().compareTo(((Annotation)arg1).getSource());
				}
			}
		}),new Function() {
			// map to ui components.
			public Object evaluate(final Object arg0) {
				// map all to annotation - as we're handling userAnnotation separately.
				return new AnnotationPanel((Annotation)arg0,hyperLinkHandler);
			}
		});
		final JEventListPanel<JMenuItem> annPanel = new JEventListPanel<JMenuItem>(annotationsPanels,new AnnotationsFormat());
		annPanel.setElementColumns(1);
		annPanel.setBackground(Color.white);
		annPanel.setBorder(BorderFactory.createEmptyBorder());
		
		final CellConstraints cc = new CellConstraints();
		final FormLayout layout = new FormLayout(
				"fill:100dlu:grow,fill:65dlu"
				,"fill:pref,fill:pref:grow"
				);
		final PanelBuilder builder = new PanelBuilder(layout);
		builder.add(this,cc.xywh(1,1,1,2));
		
		// set up the annotation editor.
		// don't like this - will leave for now..
		/*
		userAnnotationPanel.setCollapsed(true);
		final Action toggleAction = userAnnotationPanel.getActionMap().get(JCollapsiblePane.TOGGLE_ACTION);
		ImageIcon annotateIcon = IconHelper.loadIcon("annotate16.png");
		JLabel tmp = new JLabel();
		tmp.setIcon(annotateIcon);
		Icon greyedAnnotateIcon = tmp.getDisabledIcon();
		toggleAction.putValue(JCollapsiblePane.EXPAND_ICON, annotateIcon);
		toggleAction.putValue(JCollapsiblePane.COLLAPSE_ICON, greyedAnnotateIcon);
		
		JButton toggleButton = new JButton(toggleAction);
		toggleButton.setText("");
		toggleButton.putClientProperty("is3DEnabled", Boolean.FALSE);
		toggleButton.setBorderPainted(false);
		toggleButton.setToolTipText("Annotate this resource");	
		builder.add(toggleButton,cc.xy(2,1,"right,center"));
		*/
		 userAnnotationPanel = new UserAnnotationPanel();
		 userAnnotationPanel.addChangeListener(this); // on change, will save.
		builder.add(userAnnotationPanel,cc.xy(2,1));
		builder.add(annPanel,cc.xy(2,2));	
		
		final JPanel both = builder.getPanel();
		both.setBorder(BorderFactory.createEmptyBorder());
		scrollPane = new JScrollPane(both,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(new Dimension(300,300));
	}

	private final EventList annotations = new BasicEventList();
	
	private final AnnotationService annService;
	private final HyperlinkListener hyperLinkHandler;
	private final Timer lingerTimer;
	private final JScrollPane scrollPane;
	private final UserAnnotationPanel userAnnotationPanel;
	private final AnnotationSource userSource;
	
	
	// callback called by linger timer..
	public void actionPerformed(final ActionEvent e) {
		if (currentResource != null) {
		        annService.processRemainingAnnotations(currentResource,this);
		}
	}

	public void addTo(final JTabbedPane t) {
		t.addTab("Information", IconHelper.loadIcon("info16.png")
				, scrollPane, "Information provided by the resource");			
	}

	@Override
    public void clear() {
	    userAnnotationPanel.clear();
		lingerTimer.stop();
		annotations.clear();
		super.clear();
	}

	@Override
    public void display(final Resource res) {
	    userAnnotationPanel.clear(); // clear this first, which causes any writes to be saved.
		annotations.clear();
		lingerTimer.restart();
		super.display(res);	
		// annotations.
		annService.processLocalAnnotations(res,this);
	}
	
	public void process(final Annotation a) {
		if (currentResource != null && currentResource.getId().equals(a.getResourceId())) {		
			annotations.add(a); // eventlist itself takes care of producing the new panel.
		}
	}
	
	// callbacks to process annotations as they appear.
	public void process(final UserAnnotation a) {
		if (a.getSource().equals(userSource)) {
			userAnnotationPanel.setAnnotation(a);
		} else { // some other user's data
			process((Annotation)a); // no special treatment required.
		}
	}
	
	private void saveAnnotation() {
		final UserAnnotation ann = userAnnotationPanel.createAnnotation();
		if (ann == null) {
		    annService.removeUserAnnotation(currentResource);
		} else {		
		// now write back to store.
		    annService.setUserAnnotation(currentResource,ann);
		}
	}
	public void stateChanged(final ChangeEvent e) {
	    // called on notification from userannotationpanel.
	    saveAnnotation();
	}

	// delegate listener management to the user annotation panel.
    public void addChangeListener(final ChangeListener e) {
        this.userAnnotationPanel.addChangeListener(e);
    }

    public void removeChangeListener(final ChangeListener e) {
        this.userAnnotationPanel.removeChangeListener(e);
    }

	  
	  /** {@link JEventListPanel} formatter class.
	   *  - doesn't do much, as contents of list are already JComponents 
	 * - gives more control as to what to display, and how to attach controllers 
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jun 19, 200712:08:44 PM
	 */
	private static class AnnotationsFormat extends JEventListPanel.AbstractFormat {
		public AnnotationsFormat() {
			super("fill:pref"
					,"fill:pref"
					,"4dlu"
					,"0dlu"
					,new String[]{"1,1"}
					);
		}
		public JComponent getComponent(final Object arg0, final int arg1) {
			return (JComponent)arg0;
		}
		@Override
        public int getComponentsPerElement() {
			return 1;
		}
	}
}
