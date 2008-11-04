/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.FlipPanel;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator;
import org.astrogrid.desktop.modules.ui.fileexplorer.NavigableFilesList;
import org.astrogrid.desktop.modules.ui.fileexplorer.NavigableFilesTable;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator.NavigationEvent;
import org.astrogrid.desktop.modules.ui.scope.QueryResults.QueryResult;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceViewer;

import ca.odell.glazedlists.swing.EventSelectionModel;

/** Extended resource viewer that also displays query results for that service.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 11, 20072:05:19 AM
 */
public class ResultsResourceViewer extends FlipPanel implements ResourceViewer , ActionListener, FileNavigator.NavigationListener{

    private final AstroScopeLauncherImpl parent;
    private final FileNavigator navigator;
    private final JEditorPane pane;
    
    private static String CLEAR = "clear";
    private static String TABLE = "table";
    private static String ERROR = "error";
    
    public ResultsResourceViewer(final AstroScopeLauncherImpl parent
            , final TypesafeObjectBuilder uiBuilder
            , final ActivitiesManager acts) {
        this.parent = parent;
        final JPanel clear = new JPanel();
        clear.setBorder(null);
        add(clear,CLEAR);
        
        navigator = uiBuilder.createFileNavigator(parent,acts);
        navigator.addNavigationListener(this);
        resultsTable = new ResultsFilesTable(navigator);
        final JScrollPane sp = new JScrollPane(resultsTable,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getViewport().setBackground(Color.WHITE);        
        add(sp,TABLE);

        pane = new JEditorPane();
        pane.setContentType("text/html");
        pane.setBorder(null);
        pane.setEditable(false);
   // not so good - we can lose formatting if this is enabled.    pane.putClientProperty("JEditorPane.honorDisplayProperties", Boolean.TRUE);      // this key is only defined on 1.5 - no effect on 1.4
        pane.setFont(UIConstants.SANS_FONT);  
        final JScrollPane errorScroll = new JScrollPane(pane,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        errorFiles = new NavigableFilesList(navigator);
        errorFiles.setPreferredSize(new Dimension(50,50));
        final JPanel errorPanel = new JPanel(new BorderLayout());
        final JButton errButton = new JButton("Email Error Report"+ UIComponentMenuBar.ELLIPSIS,IconHelper.loadIcon("person16.png"));
        errButton.setToolTipText("Create an error-report email which you can email to the curators of this service");
        errButton.addActionListener(this);
        errorPanel.add(errorScroll,BorderLayout.NORTH);
        errorPanel.add(errorFiles,BorderLayout.CENTER);
        errorPanel.add(errButton,BorderLayout.SOUTH);
        add(errorPanel,ERROR);
          
		}

	public void clear() {
	    show(CLEAR);
	    errorFiles.clearSelection();
	    resultsTable.clearSelection();
	    
	}
private final HtmlBuilder sb = new HtmlBuilder();
private final NavigableFilesList errorFiles;
private RetrieverService service;
private final NavigableFilesTable resultsTable;

public void display(final Resource res) {
    
    if ( res instanceof RetrieverService) {
        service = (RetrieverService)res;
        final QueryResult result = parent.getServicesList().getQueryResults().getResult(service.getRetriever());
        try {
        if (result.error != null) { // flip and display this
            sb.clear();
            sb.append("<head><style>");
            sb.append("  body { font-family: Arial, Helvetica, sans-serif; font-size: 10px; }");	    
            sb.append("  cite.label {font-size: 8px; color: #666633; font-style: normal}");	            
            sb.append("</style></head><body>");
            sb.h2(service.getTitle() + ": Query Failed");
            sb.appendTitledObjectNoBR("ID",service.getId());
            sb.hr();
            sb.append(result.error);
            sb.append("</body></html>");	

            pane.setText(sb.toString());
            pane.setCaretPosition(0);
            final FileObject f = result.resultsDir;
            if (f.exists()) {                
                navigator.move(f); // show what response we got.
                errorFiles.setVisible(true);
            } else {
                // no response - so dont show the files.
                errorFiles.setVisible(false);
            }
            show(ERROR);
            return;
        } else { // display files list then
            final FileObject f = result.resultsDir;
                if (f.exists()) {
                    navigator.move(f);
                    show(TABLE);
                    return;            
                } 
        }
            } catch (final FileSystemException e) { // unlikely, as all virtual
            }
           
        // fall thru - either unexpected - not got a service,
        // or filesystem exception was thrown.
        clear();
    }
}
	

	public void addTo(final JTabbedPane t) {        
        // assembled everything, so now add ourselves as a tab        
        t.addTab( "Results",IconHelper.loadIcon("filetable16.png"),this,"Shows results from selected service");
	}

    public void actionPerformed(final ActionEvent e) {
        final BrowserControl browser = parent.getContext().getBrowser();
        final Contact[] contacts = service.getCuration().getContacts();
        final StrBuilder sb = new StrBuilder("mailto:");
        boolean emailFound = false;
        for (int i = 0; i < contacts.length; i++) {
            final Contact c = contacts[i];
            if (StringUtils.isNotEmpty(c.getEmail())) {
                emailFound = true;
                sb.appendSeparator(",");
                sb.append(c.getEmail());
            }
        }
        if (!emailFound) {
            parent.showError("No contact email is provided by this service");
            return;
        }
        sb.append("?subject=Error%20Report%20for%20");
        sb.append(URLEncoder.encode(service.getId().toString()));
        final QueryResult result = parent.getServicesList().getQueryResults().getResult(service.getRetriever());
        if (result != null && result.error != null) {
            sb.append("&body=");
            sb.append(StringUtils.replace(URLEncoder.encode(result.error),"+","%20"));
        }
        try {
            final URL url = new URL(sb.toString());
            browser.openURL(url);
        } catch (final Exception ex) {
            parent.showError("Unable to create email error report",ex);
        }
    }

    // navigation interface.
    public void moved(final NavigationEvent e) {
        final EventSelectionModel selection = navigator.getModel().getSelection();
        selection.clearSelection();
        selection.invertSelection(); // should select all.     
    }

    public void moving() {
    }

}
