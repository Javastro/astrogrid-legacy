/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrMatcher;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.util.Comparator;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.JTextComponent;

import jedit.JEditTextArea;
import jedit.SyntaxDocument;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceFormatter;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.comp.ExternalViewerHyperlinkListener;
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
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.FunctionList.Function;
import ca.odell.glazedlists.swing.JEventListPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.JCollapsiblePane;

/** displays resource as formatted html.
 * @fixme catch shutdown /window close and save last results if editor is dirty.
 * (or have a timed save - depends on storage mechanism being efficient).
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:32:38 PM
 */
public class AnnotatedResourceViewer extends JEditorPane implements ResourceViewer, AnnotationProcessor, ActionListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(AnnotatedResourceViewer.class);

	public AnnotatedResourceViewer(final BrowserControl browser, final RegistryBrowser regBrowser, AnnotationService annService) {
		super();
		this.annService = annService;
		userSource = annService.getUserAnnotationSource();
		
		// timer that triggers loading of further informatio when user has 'lingered' long enough.
		lingerTimer = new Timer(1000,this); //@todo make the linger time a peference.
		lingerTimer.setCoalesce(true);
		lingerTimer.setRepeats(false);
		hyperLinkHandler = new ExternalViewerHyperlinkListener(browser, regBrowser);
		
		CSH.setHelpIDString(this, "reg.details");
		setContentType("text/html");
		setBorder(BorderFactory.createEmptyBorder());
		setEditable(false);
		putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);		// this key is only defined on 1.5 - no effect on 1.4
        setFont(font);
		addHyperlinkListener(hyperLinkHandler);
		clear();
		
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
				return new AnnotationPanel((Annotation)arg0);
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
		builder.add(userAnnotationPanel,cc.xy(2,1));
		builder.add(annPanel,cc.xy(2,2));	
		
		JPanel both = builder.getPanel();
		both.setBorder(BorderFactory.createEmptyBorder());
		scrollPane = new JScrollPane(both,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(new Dimension(300,300));
	}
	
	private final AnnotationSource userSource;
	private final UserAnnotationPanel userAnnotationPanel = new UserAnnotationPanel();
	private final EventList annotations = new BasicEventList();
	private final JScrollPane scrollPane;
	private final AnnotationService annService;
	private final Timer lingerTimer;
	private final ExternalViewerHyperlinkListener hyperLinkHandler;
	
	public void clear() {
		lingerTimer.stop();
		annotations.clear();
		if (userAnnotationPanel.isDirty()) {
			saveAnnotations();
		}
		current = null;
		userAnnotationPanel.clear();
		setText("<html><body></body></html>");
	}
	
	private Resource current;
	
	public void display(Resource res) {
		annotations.clear();
		if (userAnnotationPanel.isDirty()) {
			saveAnnotations();
		}		
		current = res;
		userAnnotationPanel.clear();
		lingerTimer.restart();
		final String html = ResourceFormatter.renderResourceAsHTML(res);
		setText(html);
		setCaretPosition(0);		
		// annotations.
		annService.processLocalAnnotations(res,this);
	}

	// callback called by linger timer..
	public void actionPerformed(ActionEvent e) {
		if (current != null) { 
			annService.processRemainingAnnotations(current,this);
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

	public void process(Annotation a) {
		if (current != null && current.getId().equals(a.getResourceId())) {		
			annotations.add(a); // eventlist itself takes care of producing the new panel.
		}
	}

	public void addTo(UIComponentBodyguard ignored,JTabbedPane t) {
		t.addTab("Details", IconHelper.loadIcon("info16.png")
				, scrollPane, "Details of chosen resource");			
	}
	
	// save the annotations
	private void saveAnnotations() {
		if (current == null) {
			return; // not a resource present.
		}
		UserAnnotation ann = new UserAnnotation();
		ann.setResourceId(current.getId());
		ann.setSource(userSource);
		userAnnotationPanel.loadIntoAnnotation(ann);
		// now write back to store.
		annService.setUserAnnotation(current,ann);
	}
	
	/** formatter class - doesn't do much, as contents of list are already JComponents 
	 * - gives more control as to what to display, and how to attach controllers 
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jun 19, 200712:08:44 PM
	 */
	public static class AnnotationsFormat extends JEventListPanel.AbstractFormat {
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
	
	// shared resources for annotationPanel.
	//@todo work out how to get ui font here instead.
	private static final Font font = Font.decode("Helvetica")	;	
	private static final Font smallfont = Font.decode("Dialog").deriveFont(AffineTransform.getScaleInstance(0.9,0.9));		
	/** class that displays an 'external' annotation.
	 * Only displays those fields which are set.
	 * Uneditable, obviously.
	 * Tags are handled separately - merged into a global list of tags.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jun 19, 200712:14:22 PM
	 */
	private class AnnotationPanel extends JPanel {

		public AnnotationPanel(Annotation ann) {
			//@todo share these layouts - mk static?
			FormLayout layout = new FormLayout(
					"0dlu,60dlu,0dlu"
					,"d,0dlu,d,1dlu,d,1dlu,d,0dlu,d"
					);
			CellConstraints cc = new CellConstraints();
			PanelBuilder builder = new PanelBuilder(layout,this);
			String t = StringUtils.trimToNull(ann.getAlternativeTitle());
			if (t != null) {
				title = new JTextField();
				title.setFont(smallfont);
				title.setEditable(false);
				title.setText(t);
				title.setBorder(BorderFactory.createEmptyBorder());
				builder.addLabel("Alternative title",cc.xy(2,1)).setFont(smallfont);
				builder.add(title,cc.xy(2,3));
			} else {
				title = null;
			}
			
			String n = StringUtils.trimToNull(ann.getNote());
			if (n != null) {
				note = new JEditorPane();
				note.setContentType("text/html");
				note.setBorder(BorderFactory.createEmptyBorder());
				note.setEditable(false);
				note.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);		// this key is only defined on 1.5 - no effect on 1.4
		        note.setFont(font);
				note.addHyperlinkListener(hyperLinkHandler);				
				note.setText(n);
			//	builder.addLabel("Notes",cc.xy(2,5)).setFont(smallfont);				
				builder.add(note,cc.xy(2,5));
			} else {
				note = null;
			}
			
			String[] ta = ann.getTags();
			if (ta != null && ta.length > 0) {
				tags = new JTextField();
				tags.setFont(smallfont);
				tags.setEditable(false);
				StrBuilder sb = new StrBuilder();
				sb.appendWithSeparators(ta,", ");
				tags.setText(sb.toString());
				tags.setBorder(BorderFactory.createEmptyBorder());
				builder.addLabel("Tags",cc.xy(2,7)).setFont(smallfont);
				builder.add(tags,cc.xy(2,9));
			} else {
				tags = null;
			}
			this.setBackground(Color.WHITE);
			this.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.LIGHT_GRAY)
					,ann.getSource().getName()
					,TitledBorder.LEFT
					,TitledBorder.TOP
					,smallfont
					,Color.GRAY
					));
		}

		private final JTextField title;
		private final JEditorPane note;
		private final JTextField tags;
	}
	
	/** class that displays the user's annotations, and allows them to be edited. */
	private class UserAnnotationPanel extends JPanel /*JCollapsiblePane*/ implements ItemListener, DocumentListener {
		private boolean dirty = false;
		public boolean isDirty() {
			return dirty;
		}
		
		/**
		 * 
		 */
		public UserAnnotationPanel() {

			FormLayout layout = new FormLayout(
					"0dlu,30dlu,1dlu,29dlu,0dlu"
					,"min,1dlu,min,1dlu,min,0dlu,min,1dlu,min,0dlu,fill:40dlu,1dlu,min,0dlu,min"
			);
			CellConstraints cc = new CellConstraints();
			PanelBuilder builder = new PanelBuilder(layout,this);
			int row = 1;

			check = new JCheckBox("Flag");
			check.setFont(smallfont);
			check.setBackground(Color.white);
			check.addItemListener(this);
			builder.add(check,cc.xyw(2,row++,3));
			row++;

			// not as easy to get a combo box to show colours as you'd think. sheesh.
			colours = new JComboBox(new Object[]{
					Color.WHITE,Color.YELLOW,Color.GREEN,Color.BLUE,Color.RED
			});
			// magic #1 - make it editable, so we can zap the editor, but remove the ability to focus on the editor.
			colours.setEditable(true);
			colours.getEditor().getEditorComponent().setFocusable(false);
			colours.getEditor().getEditorComponent().setForeground((Color)colours.getSelectedItem());
			// renderer is quite straightforward.
			colours.setRenderer(new ColorCellRenderer());
			colours.addActionListener(new ActionListener() {
				// magic #2 listen to changes, and zap the editor.
				// need to zap forgound too, as it's trying to toString() the colour.
				public void actionPerformed(ActionEvent e) {
					Color col = (Color)colours.getSelectedItem();
					 colours.getEditor().getEditorComponent().setBackground(col);
					 colours.getEditor().getEditorComponent().setForeground(col);
					 dirty= true;
				}
			});
			builder.addLabel("Highlight",cc.xy(2,row,"right, center")).setFont(smallfont);
			builder.add(colours,cc.xy(4,row++));			

			row++;

			title = new JTextField();
			title.setEditable(true);
			title.setFont(smallfont);
			title.getDocument().addDocumentListener(this);

			builder.addLabel("Alternative title",cc.xyw(2,row++,3)).setFont(smallfont);
			row++;
			builder.add(title,cc.xyw(2,row++,3));
			row++;
			
			note = new JTextArea();
			note.getDocument().addDocumentListener(this);
			note.setLineWrap(true);
			note.setWrapStyleWord(true);
			note.setEditable(true);
			//note.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);		// this key is only defined on 1.5 - no effect on 1.4
	        note.setFont(smallfont);	
	        JScrollPane sp = new JScrollPane(note,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			builder.addLabel("Notes",cc.xyw(2,row++,3)).setFont(smallfont);
			row++;			
	        builder.add(sp,cc.xyw(2,row++,3));
	        
	        row++;
			tags = new JTextField();
			tags.setFont(smallfont);
			tags.setEditable(true);
			tags.getDocument().addDocumentListener(this);

			builder.addLabel("Tags",cc.xyw(2,row++,3)).setFont(smallfont);
			row++;
			builder.add(tags,cc.xyw(2,row++,3));

			this.setBackground(Color.WHITE);
			this.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.LIGHT_GRAY)
					,"Annotate"
					,TitledBorder.LEFT
					,TitledBorder.TOP
					,smallfont
					,Color.GRAY
			));		
		}
		
	public void setAnnotation(UserAnnotation ann) {
		check.setSelected(ann.isFlagged());
		note.setText(ann.getNote());	
		title.setText(ann.getAlternativeTitle());
				
		if (ann.getHighlight() == null) {
			colours.setSelectedIndex(0);
		} else {
			colours.setSelectedItem(ann.getHighlight());
		}
		String[] tr = ann.getTags();
		if (tr != null && tr.length > 0) {
			StrBuilder sb = new StrBuilder();
			sb.appendWithSeparators(tr," ");
			tags.setText(sb.toString());
		}
				
	}
	
	public void loadIntoAnnotation(UserAnnotation ann) {
		ann.setFlagged(check.isSelected());

		ann.setNote(StringUtils.trimToNull(note.getText()));
		ann.setAlternativeTitle(StringUtils.trimToNull(title.getText()));
		if (colours.getSelectedIndex() == 0) {
			ann.setHighlight(null);
		} else {
			ann.setHighlight((Color)colours.getSelectedItem());
		}
		String ta =  tags.getText();
		if (ta == null || ta.trim().length() ==0) {
			ann.setTags(null);
		} else {
			// try to parse it.
			StrTokenizer tok = new StrTokenizer(ta,StrMatcher.charSetMatcher(", ")); // matches spaces and commas as delimiters
			ann.setTags(tok.getTokenArray());
		}
	}
	
	public void clear() {
		check.setSelected(false);
		note.setText(null);
		title.setText(null);
		colours.setSelectedIndex(0);
		tags.setText(null);
		dirty = false;
	}

	private final JTextComponent title;
	private final JTextComponent tags;
	private final JToggleButton check;
	private final JComboBox colours;	
	private final JTextArea note;
	
	// listen for changes - and just note that this item is now dirty.
	public void itemStateChanged(ItemEvent e) {
		dirty = true;
	}

	public void changedUpdate(DocumentEvent e) {
		dirty = true;
	}

	public void insertUpdate(DocumentEvent e) {
		dirty = true;
	}

	public void removeUpdate(DocumentEvent e) {
		dirty = true;
	}	

	}

	  public static class ColorCellRenderer extends JLabel  implements ListCellRenderer {

		    // width doesn't matter as combobox will size
		    private final static Dimension preferredSize = new Dimension(0, 20);
		    public ColorCellRenderer() {
		        setOpaque(true);
		        setHorizontalAlignment(CENTER);
		        setVerticalAlignment(CENTER);
		        setPreferredSize(preferredSize);
		        setText(" ");
		    }

		    public Component getListCellRendererComponent(JList list, Object value,
		        int index, boolean isSelected, boolean cellHasFocus) {
		        if (isSelected) {
		            setBackground(list.getSelectionBackground());
		            setForeground(list.getSelectionForeground());
		        } else {
		            setBackground(list.getBackground());
		            setForeground(list.getForeground());
		        }
		      if (value instanceof Color && ! isSelected) {
		    		  setBackground((Color) value);
		      }
		      return this;
		    }
		  }

}
