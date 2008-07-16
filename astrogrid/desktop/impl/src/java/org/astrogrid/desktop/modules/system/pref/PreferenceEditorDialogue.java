
package org.astrogrid.desktop.modules.system.pref;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;
import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.JDirectoryChooser;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;

/**
 * preference editor dialogue.
 * uses l2fprod's button bar for the main structure, with form panels being built using 'jForms'
 * 
 * later - icons for tab buttons.
 * later - enable sliders for numerical input - need to specify upper and lower bounds.

 *  
 * @author Noel Winstanley
 * @since Jan 12, 20074:19:31 PM
 */
class PreferenceEditorDialogue  extends JPanel implements Runnable, PropertyChangeListener {
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory
			.getLog(PreferenceEditorDialogue.class);


		public PreferenceEditorDialogue(PreferencesArranger arranger, Preference advancedPreference, UIContext cxt) {
		    CSH.setHelpIDString(this,"dialog.preference");
			this.showAdvancedPreference = advancedPreference;
			this.cxt = cxt;
			setBorder(null);
			// build the user interface.
			initUI(arranger);
			// start listening to changes 
			this.showAdvancedPreference.addPropertyChangeListener(this);

		}
	protected final UIContext cxt;
	

	// manages a stack of config panes.
	protected final CardLayout cardLayout = new CardLayout();
	protected final JPanel cardContainer = new JPanel(cardLayout);

	// a list of all input components.
	protected final List inputComponents = new ArrayList();
	// a list of all components whose visibility varies according to {@link #showAdvancedPreference} being true
	protected final List componentsOnlyVisibleWhenAdvanced = new ArrayList();
	// used to determine whether advanced options should be shown.
	protected final Preference showAdvancedPreference;

	/** makes all limited visibility components visible or invisible */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == showAdvancedPreference) {
			boolean vis = showAdvancedPreference.asBoolean();
			showOptionalComponents(vis);		
			
		}
		
	}

	/** flip visibility of all optional components.
	 * @param vis if true, show all optional components,
	 */
	void showOptionalComponents(boolean vis) {
		for (Iterator i = componentsOnlyVisibleWhenAdvanced.iterator(); i.hasNext();) {
			JComponent c = (JComponent) i.next();
			c.setVisible(vis);
		}
	}

	/** displays the dialogue and takes action on 'ok' or 'cancel' */
	public void run() {
		// reset / update all fields
		for (Iterator i1 = inputComponents.iterator(); i1.hasNext();) {
			JComponent c1 = (JComponent) i1.next();
			Preference p1 = (Preference)c1.getClientProperty(Preference.class);
			if (! (c1 instanceof ValueAccess)) {
				throw new ProgrammerError(" Encountered a component that was not a value access: " + c1);
			}
			((ValueAccess)c1).setValue(p1.getValue()); // set field to latest value from preference
			
			Border b = (Border) c1.getClientProperty(Border.class);
			if (b != null) {
				c1.setBorder(b); // reset borders - removing any previous warnings.
			}
		} 
		BaseDialog dialog = new BaseDialog() {
		    {
		        setDialogMode(BaseDialog.OK_CANCEL_DIALOG);
		        setModal(false);
		        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		        getBanner().setVisible(false);
		        getContentPane().add(PreferenceEditorDialogue.this);  
		        setTitle("VODesktop Preferences");
		    }
		    public void ok() {
		        super.ok();
	            // go through each of the input components, saving back to preferences those that
	            // have been modified.
	            for (Iterator i = inputComponents.iterator(); i.hasNext();) {
	                JComponent c = (JComponent) i.next();
	                Preference p = (Preference) c.getClientProperty(Preference.class);
	                if (! (c instanceof ValueAccess)) {
	                    throw new ProgrammerError(" Encountered a component that was not a value access: " + c);
	                }
	                String editedValue = ((ValueAccess)c).getValue();
	                p.setValue(editedValue); // only firest events if a value change has happened.
	            }		        
		    }
		};		

		// temporarily enable advanced view, so that we pack to the correct size.
		boolean showOptional =  showAdvancedPreference.asBoolean();
		if (! showOptional) {
		showOptionalComponents(true);
		}
	    dialog.pack(); // packs for maximum size.
        dialog.centerOnScreen();
	    // now flip back to the selected view setting.
	    if (! showOptional) {
	    	showOptionalComponents(false);
	    }
	    dialog.setVisible(true);
	    dialog.toFront();	  
	}


	/** 
	 * Initialize the UI.
	 * not private - so can be overridden when testing.
	 * 
	 */
	void initUI(PreferencesArranger arranger) {
		JButtonBar toolbar = new JButtonBar(JButtonBar.VERTICAL );
		toolbar.setPreferredSize(new Dimension(90,300)); // only x-coord is relevant here.
		toolbar.setUI(new BlueishButtonBarUI());

		setLayout(new BorderLayout());

		add(BorderLayout.WEST, new JScrollPane(toolbar
				,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
				,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		add(BorderLayout.CENTER,cardContainer);

		ButtonGroup group = new ButtonGroup();

		for (Iterator cats = arranger.listPreferenceCategories().iterator(); cats.hasNext();) {
			String catName = (String) cats.next();
			List basicPrefs = arranger.listBasicPreferencesForCategory(catName);
			List advancedPrefs = arranger.listAdvancedPreferencesForCategory(catName);
			JPanel p =  makePanel(catName,basicPrefs,advancedPrefs);
			cardContainer.add(p,catName);
			JComponent butt = addButton(catName, null /*@todo  no icon available here at present */
					, toolbar
					, group);
			if (basicPrefs.size() == 0) {
				componentsOnlyVisibleWhenAdvanced.add(butt);
			}
			//@todo set selected button / pane to one visible in this view.
		}
	}
		
	
	/**
	 * add a button to the bar.
	 * @param title name of the button - and name of the tab to show when clicked.
	 * @param iconName icon (maybe null)
	 * @param bar buton bar to add to
	 * @param group enforces the fact that only one button can be clicked at a time.
	 */
	private JComponent addButton(final String title, String iconName,
			JButtonBar bar, ButtonGroup group) {

		JToggleButton button = new JToggleButton(title,IconHelper.loadIcon(iconName)) {{
			setHorizontalTextPosition(JButton.CENTER);
			setVerticalTextPosition(JButton.CENTER);
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cardLayout.show(cardContainer, title);
				}
			});
		}};
	
		bar.add(button);
		group.add(button);

		if (group.getSelection() == null) {
			button.setSelected(true);
			cardLayout.show(cardContainer, title);
		}
		return button;
	}
	/** construct one panel of the prefernece dialogue
	 * 
	 * @param title module name / panel title.
	 * @param basicPrefs list of basic preferences to display
	 * @param advancedPrefs list of advanced preferences to display (if advanced are enabled)
	 * @return a panel.
	 * @see  #makeForm(Collection, Collection)
	 */
	private JPanel makePanel(String title, List basicPrefs, List advancedPrefs) {
		FormLayout layout = new FormLayout(
				"12dlu,right:max(90dlu;min), 3dlu, left:max(200dlu;min),3dlu,max(7dlu;min),1dlu,max(3dlu;min)"
				,"");
	//	DefaultFormBuilder builder = new DefaultFormBuilder(layout,new FormDebugPanel());
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.setDefaultDialogBorder();
		JComponent top = builder.appendSeparator(title);
		top.setFont(top.getFont().deriveFont(Font.BOLD));
		top.setOpaque(true);
		top.setBackground(builder.getPanel().getBackground().brighter());		
		builder.setLeadingColumnOffset(1);
		builder.nextLine();
		
		List thisPanelInputComponents = new ArrayList();
		
		if (basicPrefs != null && basicPrefs.size() > 0) {
			// order the preferences  first.
			RowFactory fac = new RowFactory();
			for (Iterator i = basicPrefs.iterator(); i.hasNext();) {
				Preference p = (Preference) i.next();
				fac.buildFormRow(builder, p);
			}
			thisPanelInputComponents.addAll(fac.inputComponentList);
		}
		if (advancedPrefs != null && advancedPrefs.size() > 0) {
			RowFactory fac = new RowFactory();		
			JComponent label = builder.appendSeparator("Advanced");
			componentsOnlyVisibleWhenAdvanced.add(label); // want this to vanish.
			builder.nextLine();
			for (Iterator i = advancedPrefs.iterator(); i.hasNext();) {
				Preference p = (Preference) i.next();
				fac.buildFormRow(builder, p);
			}
			thisPanelInputComponents.addAll(fac.inputComponentList);
			this.componentsOnlyVisibleWhenAdvanced.addAll(fac.allComponentList);
		}
		this.inputComponents.addAll(thisPanelInputComponents);
		// create a 'restore defaults' and 'apply' button bar.
		JPanel buttonPanel = makePanelButtons(thisPanelInputComponents) ;
		JPanel formPanel = builder.getPanel();
		JPanel all = new JPanel(new BorderLayout());
		all.add(formPanel,BorderLayout.CENTER);
		all.add(buttonPanel,BorderLayout.SOUTH);
		all.setBorder(BorderFactory.createEtchedBorder());
		return all;
	}
	

	/** build a panel containing 'restore defaults and 'apply' buttons. */
	private JPanel makePanelButtons(final List panelComponents) {
		JButton restore = new JButton("Restore Defaults") {{
			setToolTipText("Restore settings for this pane back to defaults");
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (Iterator i = panelComponents.iterator(); i
							.hasNext();) {
						JComponent c = (JComponent) i.next();
						Preference p = (Preference)c.getClientProperty(Preference.class);
						((ValueAccess)c).setValue(p.getDefaultValue()); // nb - just setting text - not altering the preference value itself.
						// clear any warnings on components.
						Border b = (Border) c.getClientProperty(Border.class);
						if (b != null) {
							c.setBorder(b);
						}
					}
				}
			});
		}};
		JButton apply = new JButton("Apply") {{
			setToolTipText("Apply any changes made to this pane");
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (Iterator i = panelComponents.iterator(); i
							.hasNext();) {
						JComponent c = (JComponent) i.next();
						Preference p = (Preference)c.getClientProperty(Preference.class);
						p.setValue(((ValueAccess)c).getValue()); // only fires those that differn.
					}
				}
			});			
		}};
		return ButtonBarFactory.buildAddRemoveRightBar(restore, apply);
	}
	

	/** factory class that takes care of producing rows and adding then to the form */
	class RowFactory {
	/**
		 * length at which proprties are considered to be oversized. 
		 */
		private static final int OVERSIZE = 35;

	/**
		 * number of columns that large fields may span.
		 */
		private static final int SPAN_ALL_COLUMNS = 4;

		/** accumulating list of all components creatrd so far by this row factory */
	final List allComponentList = new ArrayList();
	/** accumulating list of all input components creaed so far by this row factory */
	final List inputComponentList = new ArrayList();
	
	/** builds a label for a preference , based on UiName and description*/
	private JLabel mkLabel(Preference p) {
		JLabel l = new JLabel(p.getUiName());
		l.setToolTipText(p.getDescription());
		allComponentList.add(l);
		return l;
	}
	
	/** creates a single row of the form - layout and components used vary according
	 * to the preference metadata.
	 * 
	 * as a side-effect, lodges the preference as a client property of the input JComponent
	 * created for it, a
	 * nd stores the input componet in the {@link #inputComponentList} list
	 * and stores all components created for this row in the {@link #allComponentList}
	 * 
	 * @param builder the form builder to add row to.
	 * @param p the preference to build for.
	 * @param isLimitedVisibility if true, add to the list of limitedVisiblity components.
	 */
	void buildFormRow(DefaultFormBuilder builder,final  Preference p) {
		String[] options = p.getOptions();
		String[] suggestions = p.getAlternatives();
		JComponent comp;
		if (Preference.BOOLEAN.equals(p.getUnits())) {
			// checkbox
			 comp = new BooleanInput(p);
			builder.append("",comp);			
		} else if (options != null && options.length > 0) {
				// fixed set of options
				comp = new OptionInput(options, p.getValue());
				builder.append(mkLabel(p),addAnnotations(p,comp));
		} else  if (suggestions != null && suggestions.length > 0) {
				// recommended / suggested values - editable combobox
				String[] arr = p.getAllAlternatives();
				comp = new SuggestionInput(arr) ;
				// find maximum size..
				int maxSize = 0;
				for (int i = 0; i < arr.length; i++) {
					maxSize = Math.max(maxSize, arr[i].length());
				}
				if (maxSize > OVERSIZE) { 
					int fontSize = comp.getFont().getSize();
					comp.setFont(comp.getFont().deriveFont(fontSize * 0.8f));
				} 
				builder.append(mkLabel(p),addAnnotations(p,comp));
			
		} else {
			// plain text input
			// single line - either a short or long input box.
			int sz = p.getDefaultValue().length() < 7 ? 7 : 50;
			comp = new TextInput(p.getValue(),sz);
			if (p.getDefaultValue().length() > OVERSIZE) {
				int fontSize = comp.getFont().getSize();
				comp.setFont(comp.getFont().deriveFont(fontSize * 0.8f));				

			} 

			builder.append(mkLabel(p),addAnnotations(p, comp));
		
		}
		// store componebnt and prefernece together.
		comp.putClientProperty(Preference.class, p);
		inputComponentList.add(comp);
		this.allComponentList.add(comp);
		addEndMatter(builder,p);
		builder.nextLine();
	}
	
	/** builds any buttons added to the end of the row, that are unaffected by changes
	 * to the preference value.
	 * @param p
	 */
	private void addEndMatter(DefaultFormBuilder builder,final Preference p) {

		if (p.getHelpId() != null) {
			JButton butt = cxt.getHelpServer().createHelpButton(p.getHelpId());
			butt.setBorder(BorderFactory.createEmptyBorder());
			allComponentList.add(butt);
			builder.append(butt);
		} else {
			builder.append("");
		}
				
		if (p.isRequiresRestart()) {
			//JLabel l = new JLabel("<html><FONT color='red'>*");
			JLabel l = new JLabel("Requires restart");
			l.setFont(UIConstants.SMALL_DIALOG_FONT);
			l.setToolTipText("Requires restart to take effect");
			allComponentList.add(l);
			builder.append(l);
		}

		
	}
	
	/** builds any buttons / annotations which inspect / use the value of the preference */
	private JComponent addAnnotations(Preference p,final JComponent c) {
		JPanel panel = new JPanel();
		LayoutManager layout = new BoxLayout(panel,BoxLayout.X_AXIS);
		panel.setLayout(layout);
		panel.add(c);
		allComponentList.add(panel);
		if (p.getUnits() != null) {
			if (Preference.FILE.equals(p.getUnits())) {
				c.setInputVerifier(fileVerifier);
				// append a file chooser dialogue
				JButton b = new JButton("Change...") {{
					addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							final ValueAccess valueAccess = ((ValueAccess)c);
							File f = new File(valueAccess.getValue());
							fileChooser.setCurrentDirectory(f); // will be set to parent dir of this file.
							fileChooser.ensureFileIsVisible(f);
							
							if (JFileChooser.APPROVE_OPTION == fileChooser.showDialog(c, "Choose")) {
								f = fileChooser.getSelectedFile();
								if (f != null) {
									valueAccess.setValue(f.getAbsolutePath());
								}
							}
						}
					});
				}};
				panel.add(b);
			} else if (Preference.DIRECTORY.equals(p.getUnits())) {
				c.setInputVerifier(directoryVerifier);
				// append a directory choser dialogue.
				JButton b = new JButton("Change...") {{
					setToolTipText("Browse and select from the filesystem");
					addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							final ValueAccess valueAccess = ((ValueAccess)c);							
							File f = new File(valueAccess.getValue());
							//@todo these 2 methods don't do as much as you'd hope. seems to be a bug in the implementation.
							directoryChooser.setCurrentDirectory(f);
							directoryChooser.ensureFileIsVisible(f);
							if (JFileChooser.APPROVE_OPTION == directoryChooser.showDialog(c, "Choose")) {
								f = directoryChooser.getSelectedFile();
								if (f != null) {
									valueAccess.setValue(f.getAbsolutePath());
								}
							}
						}
					});
				}};
				panel.add(b);
			} else if (Preference.NUMBER.equals(p.getUnits())) {
				c.setInputVerifier(numberVerifier);
			} else if (Preference.SECONDS.equals(p.getUnits())) {
				c.setInputVerifier(secondsVerifier);
				// same as number, but append a label 'seconds'
				final JLabel label = new JLabel(p.getUnits());
				panel.add(label);				
			} else if (Preference.URL.equals(p.getUnits())) {
				// set the verifier.
				c.setInputVerifier(urlVerifier);
				// append a browser open dialogue.
				//Removed - BZ 2301
//				JButton b = new JButton("Test") {{
//					setToolTipText("Shpw this URL in webbrowser");
//					addActionListener(new ActionListener() {
//
//						public void actionPerformed(ActionEvent e) {
//							try {
//								URL u = new URL( ((ValueAccess)c).getValue());
//								cxt.getBrowser().openURL(u);
//							} catch (MalformedURLException x) {
//								// unlikelyt to happen, as input verifier should have caught this..
//								JOptionPane.showMessageDialog(c, "Failed to open URL - it's not valid");
//							} catch (ACRException x) {
//								logger.warn("Failed to open browser",x);
//							}
//						}
//					});
//				}};
//		`		
//				panel.add(b);
			} else if (Preference.BOOLEAN.equals(p.getUnits())) {
				// ignore
			} else { // unrecognized - fallback.
				final JLabel label = new JLabel(p.getUnits());
				panel.add(label);
			}
			return panel;
		} else {
			return c;
		}
	}
}
	
	
	// supporting components
	protected final JFileChooser fileChooser = new JFileChooser();
	protected final JDirectoryChooser directoryChooser = new JDirectoryChooser() {{
		setFileHidingEnabled(false);
	}};
	
	/** input verifiers */
	
	/** subclass of verifier that just highlights when input is incorrect - 
	 * doesn't prevent focus from leaving 
	 */
	abstract class HighlightingInputVerifier extends InputVerifier {

		public boolean shouldYieldFocus(JComponent input) {
			boolean ok = verify(input);
			if (! ok) {
				Toolkit.getDefaultToolkit().beep();
				Border b = input.getBorder();
				input.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				input.putClientProperty(Border.class, b); // cache original border.
			} else { // reset.
				Border b = (Border) input.getClientProperty(Border.class);
				if (b != null) {
					input.setBorder(b);
				}
			}
			return true;
		}
		
	}
	
	protected final InputVerifier urlVerifier = new HighlightingInputVerifier() {

		public boolean verify(JComponent input) {
			String v = ((ValueAccess)input).getValue();
			try {
				new URL(v);
				return true;
			} catch (MalformedURLException e) {
				return false;
			}
		}
	};
	
	protected final InputVerifier directoryVerifier = new HighlightingInputVerifier() {
		
		public boolean verify(JComponent input) {
			String v = ((ValueAccess)input).getValue();
			try {
				File f = new File(v);
				if (! f.exists()) {
					f.mkdirs();
				} 
				return f.exists() &&  f.isDirectory() && f.canRead() && f.canWrite();
			} catch (Exception e) {
				return false;
			}
				
		}
	};
	
	protected final InputVerifier fileVerifier = new HighlightingInputVerifier() {
		
		public boolean verify(JComponent input) {
			String v = ((ValueAccess)input).getValue();
			try {
				File f = new File(v);
				f.createNewFile(); // only creates if doesn't already exist.
				return f.exists() && f.isFile() && f.canRead() && f.canWrite();
			} catch (Exception e) {
				return false;
			}
				
		}
	};	
	
	protected final InputVerifier numberVerifier = new HighlightingInputVerifier() {

		public boolean verify(JComponent input) {
			String v = ((ValueAccess)input).getValue();
			try {
				Integer.parseInt(v);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
	};
	

	protected final InputVerifier secondsVerifier = new HighlightingInputVerifier() {

		public boolean verify(JComponent input) {
			String v = ((ValueAccess)input).getValue();
			try {
				int i = Integer.parseInt(v);
				return i >= 0; // can't be negative.
			} catch (NumberFormatException e) {
				return false;
			}
		}
	};	
	
	/** checkbox */
	private static final class BooleanInput extends JCheckBox implements ValueAccess {

		public BooleanInput(Preference p) {
			super(p.getUiName(), p.asBoolean());
			setToolTipText(p.getDescription());
		}

		public String getValue() {
			return Boolean.toString(isSelected());
		}

		public void setValue(String s) {
			setSelected(Boolean.valueOf(s).booleanValue());
		}
	}
	
	/** 				// fixed set of options - uneditable combobox
	 * 
	 * @author Noel Winstanley
	 * @since Jan 14, 20072:09:24 AM
	 */
		private static final class OptionInput extends JComboBox implements ValueAccess {
	
	
			OptionInput(String[] items, String val) {
				super(items);
				setEditable(false);
				setSelectedItem(val);
			}
	
			public String getValue() {
				return (String)getSelectedItem();
			}
	
			public void setValue(String s) {
				setSelectedItem(s);
			}
		}
	
	/** variable list of suggesionts - editable combobox */
	private static final class SuggestionInput extends JComboBox implements ValueAccess {
		public SuggestionInput(String[] arg0) {
			super(arg0);
			setEditable(true);
			setSelectedItem(arg0[0]);
			suggestions = new ArrayList(Arrays.asList(arg0));
		}
		private final List suggestions;

		public String getValue() {
			return (String)getSelectedItem();
		}

		public void setValue(String s) {
			if (suggestions.contains(s)) {
				setSelectedItem(s);
			} else {
				addItem(s);
				suggestions.add(s);
				setSelectedItem(s);
			}
		}
		
	}
	/** text field */
	private static final class TextInput extends JTextField implements ValueAccess {

		public TextInput(String text, int columns) {
			super(text, columns);
		}

		public String getValue() {
			return getText();
		}

		public void setValue(String s) {
			setText(s);
		}
	}
	/** interface that allows the value of a component to be get / set */
	static interface ValueAccess {
		String getValue();
		void setValue(String s);
	}
	
}
