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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;
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

/** displays resource as formatted html.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:32:38 PM
 */
public class AnnotatedResourceViewer extends ResourceDisplayPane implements EditableResourceViewer, AnnotationProcessor, ActionListener, ChangeListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(AnnotatedResourceViewer.class);
    public AnnotatedResourceViewer(final BrowserControl browser, final RegistryBrowser regBrowser, AnnotationService annService) {
		super(browser, regBrowser);
		this.annService = annService;
		userSource = annService.getUserAnnotationSource();
	
		
		// timer that triggers loading of further informatio when user has 'lingered' long enough.
		lingerTimer = new Timer(1000,this); //@todo make the linger time a peference.			
		lingerTimer.setCoalesce(true);
		lingerTimer.setRepeats(false);
		
		hyperLinkHandler = getHyperlinkListeners()[0];	
		CSH.setHelpIDString(this, "reg.details");
		
		// sort the list, then map into JPanels.
		FunctionList annotationsPanels = new FunctionList(new SortedList(annotations,new Comparator() {
			// sort annotations.
			public int compare(Object arg0, Object arg1) {
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
			public Object evaluate(Object arg0) {
				// map all to annotation - as we're handling userAnnotation separately.
				return new AnnotationPanel((Annotation)arg0,hyperLinkHandler);
			}
		});
		JEventListPanel annPanel = new JEventListPanel(annotationsPanels,new AnnotationsFormat());
		annPanel.setElementColumns(1);
		annPanel.setBackground(Color.white);
		annPanel.setBorder(BorderFactory.createEmptyBorder());
		
		CellConstraints cc = new CellConstraints();
		FormLayout layout = new FormLayout(
				"fill:100dlu:grow,fill:65dlu"
				,"fill:pref,fill:pref:grow"
				);
		PanelBuilder builder = new PanelBuilder(layout);
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
		
		JPanel both = builder.getPanel();
		both.setBorder(BorderFactory.createEmptyBorder());
		scrollPane = new JScrollPane(both,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(new Dimension(300,300));
	}

	private final EventList annotations = new BasicEventList();
	
	private final AnnotationService annService;
	private Resource current;
	private final HyperlinkListener hyperLinkHandler;
	private final Timer lingerTimer;
	private final JScrollPane scrollPane;
	private final UserAnnotationPanel userAnnotationPanel;
	private final AnnotationSource userSource;
	
	
	// callback called by linger timer..
	public void actionPerformed(ActionEvent e) {
		if (current != null) {
		        annService.processRemainingAnnotations(current,this);
		}
	}

	public void addTo(UIComponentBodyguard ignored,JTabbedPane t) {
		t.addTab("Details", IconHelper.loadIcon("info16.png")
				, scrollPane, "Details of chosen resource");			
	}

	public void clear() {
	    userAnnotationPanel.clear();
		lingerTimer.stop();
		annotations.clear();
		current = null;
		setText("<html><body></body></html>");
	}

	public void display(Resource res) {
	    userAnnotationPanel.clear(); // clear this first, which causes any writes to be saved.
		annotations.clear();
		current = res;
		lingerTimer.restart();
		final String html = ResourceFormatter.renderResourceAsHTML(res);
		setText(html);
		setCaretPosition(0);		
		// annotations.
		annService.processLocalAnnotations(res,this);
	}
	
	public void process(Annotation a) {
		if (current != null && current.getId().equals(a.getResourceId())) {		
			annotations.add(a); // eventlist itself takes care of producing the new panel.
		}
	}
	
	// callbacks to process annotations as they appear.
	public void process(UserAnnotation a) {
		if (a.getSource().equals(userSource)) {
			userAnnotationPanel.setAnnotation(a);
		} else { // some other user's data
			process((Annotation)a); // no special treatment required.
		}
	}
	
	private void saveAnnotation() {
		UserAnnotation ann = userAnnotationPanel.createAnnotation();
		if (ann == null) {
		    annService.removeUserAnnotation(current);
		} else {		
		// now write back to store.
		    annService.setUserAnnotation(current,ann);
		}
	}
	public void stateChanged(ChangeEvent e) {
	    // called on notification from userannotationpanel.
	    saveAnnotation();
	}

	// delegate listener management to the user annotation panel.
    public void addChangeListener(ChangeListener e) {
        this.userAnnotationPanel.addChangeListener(e);
    }

    public void removeChangeListener(ChangeListener e) {
        this.userAnnotationPanel.removeChangeListener(e);
    }

	  
	  /** formatter class - doesn't do much, as contents of list are already JComponents 
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
		public JComponent getComponent(Object arg0, int arg1) {
			return (JComponent)arg0;
		}
		public int getComponentsPerElement() {
			return 1;
		}
	}
}
