package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Color;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.desktop.modules.ui.comp.MyTitledBorder;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.votech.Annotation;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** {@link JPanel} that displays an 'external' annotation.
 * Only displays those fields which are set.
 * Uneditable - just a display.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 19, 200712:14:22 PM
 */
public class AnnotationPanel extends JPanel {

	public AnnotationPanel(final Annotation ann, final HyperlinkListener hyperLinkHandler) {
		//@todo share these layouts - mk static?
		final FormLayout layout = new FormLayout(
				"0dlu,60dlu,0dlu"
				,"d,0dlu,d,1dlu,p,1dlu,d,0dlu,d"
				);
		final CellConstraints cc = new CellConstraints();
		final PanelBuilder builder = new PanelBuilder(layout,this);
		final String t = StringUtils.trimToNull(ann.getAlternativeTitle());
		if (t != null) {
			title = new JTextField();				
			title.setFont(UIConstants.SMALL_DIALOG_FONT);
			title.setEditable(false);
			title.setText(t);
			title.setBorder(BorderFactory.createEmptyBorder());
			builder.addLabel("Alternative title",cc.xy(2,1)).setFont(UIConstants.SMALL_DIALOG_FONT);
			builder.add(title,cc.xy(2,3));
		} else {
			title = null;
		}
		
		final String n = StringUtils.trimToNull(ann.getNote());
		if (n != null) {
			note = new JEditorPane();
			note.setContentType("text/html");		
			note.setBorder(BorderFactory.createEmptyBorder());
			note.setEditable(false);
			note.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);		// this key is only defined on 1.5 - no effect on 1.4
	        note.setFont(UIConstants.SANS_FONT);
			note.addHyperlinkListener(hyperLinkHandler);				
			note.setText(n);
			// necessary to wrap it all in a scroll pane (even though we don't want any scroll bars)
			// to ensure all text content is displayed. odd.
			final JScrollPane sp = new JScrollPane(note,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			sp.setBorder(null);
		//	builder.addLabel("Notes",cc.xy(2,5)).setFont(SMALL_FONT);
			builder.add(sp,cc.xy(2,5));
		} else {
			note = null;
		}
		
		final Set ta = ann.getTags();
		if (ta != null && ta.size() > 0) {
			tags = new JTextField();
			tags.setFont(UIConstants.SMALL_DIALOG_FONT);
			tags.setEditable(false);
			final StrBuilder sb = new StrBuilder();
			sb.appendWithSeparators(ta,", ");
			tags.setText(sb.toString());
			tags.setBorder(BorderFactory.createEmptyBorder());
			builder.addLabel("Tags",cc.xy(2,7)).setFont(UIConstants.SMALL_DIALOG_FONT);
			builder.add(tags,cc.xy(2,9));
		} else {
			tags = null;
		}
		this.setBackground(Color.WHITE);
		this.setBorder(MyTitledBorder.createLined(ann.getSource().getName()));
	}

	private final JEditorPane note;
	private final JTextField tags;
	private final JTextField title;
}