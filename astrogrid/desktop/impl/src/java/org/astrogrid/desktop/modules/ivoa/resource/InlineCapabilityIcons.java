/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.ResourceDisplayPane;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.voexplorer.google.CapabilityIconFactory;

/** component to be used within html to display dynamically-generated icons.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 7, 20095:25:49 PM
 */
public final class InlineCapabilityIcons extends JLabel {

    private final static Border border = new EmptyBorder(1,1,5,1); 

    public InlineCapabilityIcons() {
        setVerticalAlignment(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.LEFT);
        setFont(UIConstants.SANS_TITLE_FONT);
        setIconTextGap(8);
        
        setBorder(border);

        addHierarchyListener(new HierarchyListener() {
            // when we're shown, set the icon.
            public void hierarchyChanged(final HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && isShowing()) {
                    final ResourceDisplayPane resourceDisplayPane = ResourceDisplayPaneEmbeddedButton.getResourceDisplayPane(e);
                    final Resource r = resourceDisplayPane.getCurrentResource();
                    final CapabilityIconFactory iconFactory = resourceDisplayPane.getIconFactory();
                    final Icon icon = iconFactory.buildIcon(r);
                    setIcon(icon);
                    setToolTipText(iconFactory.getTooltip(icon));
                    final String fullTitle = r.getTitle();
                    if (StringUtils.isNotBlank(fullTitle)) {
                        final String title = WordUtils.abbreviate(fullTitle
                                ,Math.min(fullTitle.length(),45)
                                ,55
                                ,String.valueOf(UIComponentMenuBar.ELLIPSIS)
                        );
                        setText(title);
                    }
                    invalidate();
                }
            }
        });

    }


}
