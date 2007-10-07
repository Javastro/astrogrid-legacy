/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.event.MouseListener;
import java.text.Format;
import java.util.Map;

import javax.swing.JMenu;

import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.apache.commons.logging.Log;
import org.apache.hivemind.schema.Translator;
import org.apache.hivemind.service.EventLinker;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.hivemind.EventDispatchThreadObjectBuilder;
import org.astrogrid.desktop.modules.adqlEditor.ADQLEditorPanel;
import org.astrogrid.desktop.modules.auth.SwingLoginDialogue;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileExplorerImpl;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator;
import org.astrogrid.desktop.modules.ui.fileexplorer.StorageView;
import org.astrogrid.desktop.modules.ui.taskrunner.AdqlTextFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.BinaryFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.BooleanFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.EnumerationFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.LargeTextFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.LooselyFormattedFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.OutputFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.ParametersInfoPane;
import org.astrogrid.desktop.modules.ui.taskrunner.PositionFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.RadiusFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskParametersForm;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskRunnerImpl;
import org.astrogrid.desktop.modules.ui.taskrunner.TextFormElement;
import org.astrogrid.desktop.modules.ui.taskrunner.UIComponentWithMenu;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskParametersForm.Model;
import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.impl.matchers.FixedMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.Matchers;

/** extends the event dispatch object builder with methods to build particular kinds 
 * of ui components.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 23, 200712:36:50 AM
 */
public class TypesafeObjectBuilderImpl extends EventDispatchThreadObjectBuilder implements TypesafeObjectBuilder {

    /**
     * @param logger
     * @param config
     * @param objectTranslator
     * @param linker
     */
    public TypesafeObjectBuilderImpl(Log logger, Map config,
            Translator objectTranslator, EventLinker linker) {
        super(logger, config, objectTranslator, linker);
    }
    
    public AstroScopeLauncherImpl createAstroscope() {
        return (AstroScopeLauncherImpl)create("astroscope");
    }

    public ExecutionTracker createExecutionTracker(UIComponent parent) {
        return (ExecutionTracker)create("executionTracker",parent);
    }

    public FileExplorerImpl createFileExplorer() {
        return (FileExplorerImpl)create("fileExplorer");
    }
    
    public StorageView createStorageView(UIComponent parent, ActivitiesManager acts) {
        return (StorageView)create("storageView",new Object[]{parent,acts});
    }

    public RegistryGooglePanel createGooglePanel() {
        return (RegistryGooglePanel)create("registryGooglePanel");
    }

    public QueryBuilderImpl createQueryBuilderImpl() {
        return (QueryBuilderImpl)create("querybuilder");
    }


    public TaskRunnerImpl createTaskRunner() {
        return  (TaskRunnerImpl)create("taskrunner");
    }

    public VOExplorerImpl createVoExplorer() {
        return (VOExplorerImpl)create("voexplorer");
    }

    public ObjectBuilder getObjectBuilder() {
        return this;
    }

    public ADQLEditorPanel createAdqlEditorPanel(org.astrogrid.applications.beans.v1.parameters.ParameterValue pb,
            CeaApplication app, UIComponentWithMenu parent) {
        return (ADQLEditorPanel)create("adqlPanel",new Object[]{pb,app,parent});
    }

    public AdqlTextFormElement createAdqlTextFormElement(ParameterValue pv,
            ParameterBean pb,CeaApplication app, UIComponentWithMenu parent) {
        return (AdqlTextFormElement)create("adqlFormElement",new Object[]{pv,pb,app,parent});
    }

    public BinaryFormElement createBinaryFormElement(ParameterValue pv,
            ParameterBean pb) {
        return (BinaryFormElement)create("binaryFormElement",new Object[]{pv,pb});
    }

    public BooleanFormElement createBooleanFormElement(ParameterValue pv,
            ParameterBean pb) {
        return (BooleanFormElement)create("booleanFormElement",new Object[]{pv,pb});
    }

    public EnumerationFormElement createEnumerationFormElement(
            ParameterValue pv, ParameterBean pb) {
        return (EnumerationFormElement)create("enumerationFormElement",new Object[]{pv,pb});
    }

    public LargeTextFormElement createLargeTextFormElement(ParameterValue pv,
            ParameterBean pb) {
        return (LargeTextFormElement)create("largeFormElement",new Object[]{pv,pb});
    }

    public LooselyFormattedFormElement createLooselyFormattedFormElement(
            ParameterValue pv, ParameterBean pb, Format format) {
        return (LooselyFormattedFormElement)create("looseFormElement",new Object[]{pv,pb,format});
    }

    public OutputFormElement createOutputFormElement(ParameterValue pv,
            ParameterBean pb) {
        return (OutputFormElement)create("outputFormElement",new Object[]{pv,pb});
    }

    public PositionFormElement createPositionFormElement(ParameterValue ra,
            ParameterBean raDesc, ParameterValue dec, ParameterBean decDesc, UIComponent parent) {
        return (PositionFormElement)create("positionFormElement",new Object[]{ra,raDesc,dec,decDesc,parent});
    }
    

    public RadiusFormElement createRadiusFormElement(ParameterValue radius,
            ParameterBean radiusDesc) {
        return (RadiusFormElement)create("radiusFormElement",new Object[]{radius,radiusDesc});
    }

    public RadiusFormElement createRadiusFormElement(ParameterValue radius,
            ParameterBean radiusDesc, DecSexToggle toggle) {
        return (RadiusFormElement)create("radiusFormElementExternal",new Object[]{radius,radiusDesc,toggle});
        
    }

    public TextFormElement createTextFormElement(ParameterValue pv,
            ParameterBean pb) {
        return (TextFormElement)create("textFormElement",new Object[]{pv,pb});
    }

    public TaskParametersForm createTaskParametersForm(UIComponentWithMenu parent) {
        return (TaskParametersForm)create("taskParametersForm",parent);
    }

    public FileNavigator createFileNavigator(UIComponent parent,
            MatcherEditor ed, ActivitiesManager acts) {
        return (FileNavigator) create("fileNavigator",new Object[]{parent, ed, acts});
    }

    public FileNavigator createFileNavigator(UIComponent parent,
            ActivitiesManager acts) {
        return createFileNavigator(parent,new FixedMatcherEditor(Matchers.trueMatcher()),acts);
    }

    public SwingLoginDialogue createLoginDialogue() {
        return (SwingLoginDialogue)create("swingLoginDialogue");
    }

    public ParametersInfoPane createParametersInfoPane(Model model,
            EventList elements) {
        return (ParametersInfoPane)create("parametersInfoPane",new Object[]{model,elements});
     }




    

}
