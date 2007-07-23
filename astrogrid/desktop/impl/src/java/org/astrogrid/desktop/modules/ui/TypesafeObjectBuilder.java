/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.event.MouseListener;
import java.text.Format;

import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.adqlEditor.ADQLEditorPanel;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileExplorerImpl;
import org.astrogrid.desktop.modules.ui.taskrunner.AdqlTextFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.BinaryFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.BooleanFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.EnumerationFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.LargeTextFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.LooselyFormattedFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.OutputFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.PositionFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskParametersForm;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskRunnerImpl;
import org.astrogrid.desktop.modules.ui.taskrunner.TextFormElement;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;

/** wraps the untyped hivemind ObjectBuilder with 
 * methods that make explicit the parameters required and the return type
 * for each kind of object.
 * 
 * means that we only need to unit test for type-mistakes this class.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 23, 200712:40:27 AM
 */
public interface TypesafeObjectBuilder {

    // main windows
    /** returns a new object with the name <tt>astroscope</tt> */
    public AstroScopeLauncherImpl createAstroscope();
    
    /** reutns a new object with the name <tt>taskrunner</tt> */
    public TaskRunnerImpl createTaskRunner();
    
    /** retuerns a new object as named <tt>querybuilder</tt> */
    public QueryBuilderImpl createQueryBuilderImpl();
    
    /** reutnrs a new object a snamed <tt>voexplorer</tt> */
    public VOExplorerImpl createVoExplorer();
    
    /** reutns a new object as named <tt>fileExplorer</tt> */
    public FileExplorerImpl createFileExplorer();
    
    
    // subcomponents.
    /** returns a new object as named <tt>executionTracker</tt> */
    public ExecutionTracker createExecutionTracker();
    
    /** returns a new object as named <tt>registryGooglePanel</tt> */
    public RegistryGooglePanel createGooglePanel();
    
    
    /** returns a new object as named <tt>taskParametersForm</tt> */
    public TaskParametersForm createTaskParametersForm(UIComponent parent, MouseListener listener);

    
    ADQLEditorPanel createAdqlEditorPanel(ParameterValue pb, CeaApplication app, UIComponent parent);
    
//task parameter editors
    TextFormElement createTextFormElement(ParameterValue pv,ParameterBean pb);
    LooselyFormattedFormElement createLooselyFormattedFormElement(ParameterValue pv,ParameterBean pb, Format format);       
    LargeTextFormElement createLargeTextFormElement(ParameterValue pv,ParameterBean pb);
    BinaryFormElement createBinaryFormElement(ParameterValue pv,ParameterBean pb);
    EnumerationFormElement createEnumerationFormElement(ParameterValue pv,ParameterBean pb);
    BooleanFormElement createBooleanFormElement(ParameterValue pv,ParameterBean pb);
    OutputFormElement createOutputFormElement(ParameterValue pv,ParameterBean pb);
    PositionFormElement createPositionFormElement(ParameterValue ra, ParameterBean raDesc,ParameterValue dec, ParameterBean decDesc, UIComponent parent);
    AdqlTextFormElement createAdqlTextFormElement(ParameterValue pv,ParameterBean pb, CeaApplication app,UIComponent parent);
    
    //
    // access the core object builder
    public ObjectBuilder getObjectBuilder();
    
    
    
    

}