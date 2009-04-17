/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.text.Format;
import java.util.Map;

import net.sourceforge.hiveutils.service.ObjectBuilder;

import org.apache.commons.logging.Log;
import org.apache.hivemind.schema.Translator;
import org.apache.hivemind.service.EventLinker;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.hivemind.EventDispatchThreadObjectBuilder;
import org.astrogrid.desktop.modules.adqlEditor.ADQLEditorPanel;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.system.ui.MultiConeImpl;
import org.astrogrid.desktop.modules.ui.comp.DecSexToggle;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileExplorerImpl;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileNavigator;
import org.astrogrid.desktop.modules.ui.fileexplorer.StorageView;
import org.astrogrid.desktop.modules.ui.scope.ScopeServicesList;
import org.astrogrid.desktop.modules.ui.taskrunner.AbstractTaskFormElement;
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
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceViewer;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.impl.matchers.FixedMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.matchers.Matchers;

/** Implementation of {@code TypesafeObjectBuilder}, that ensures objects are created on the EDT.
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
    public TypesafeObjectBuilderImpl(final Log logger, final Map config,
            final Translator objectTranslator, final EventLinker linker) {
        super(logger, config, objectTranslator, linker);
    }
    
    public AstroScopeLauncherImpl createAstroscope() {
        return (AstroScopeLauncherImpl)create("astroscope");
    }

    public ExecutionTracker createExecutionTracker(final UIComponent parent) {
        return (ExecutionTracker)create("executionTracker",parent);
    }

    public FileExplorerImpl createFileExplorer() {
        return (FileExplorerImpl)create("fileExplorer");
    }
    
    public StorageView createStorageView(final UIComponent parent, final ActivitiesManager acts) {
        return (StorageView)create("storageView",new Object[]{parent,acts});
    }

    public RegistryGooglePanel createGooglePanel(final UIComponent parent) {
        return (RegistryGooglePanel)create("registryGooglePanel",parent);
    }
    public ScopeServicesList createScopeServicesList(final UIComponent parent) {
        return (ScopeServicesList)create("scopeServiceList",parent);
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

    public ADQLEditorPanel createAdqlEditorPanel(final org.astrogrid.applications.beans.v1.parameters.ParameterValue pb,
            final CeaApplication app, final UIComponentWithMenu parent) {
        return (ADQLEditorPanel)create("adqlPanel",new Object[]{pb,app,parent});
    }

    public AdqlTextFormElement createAdqlTextFormElement(final ParameterValue pv,
            final ParameterBean pb,final CeaApplication app, final UIComponentWithMenu parent) {
        return (AdqlTextFormElement)create("adqlFormElement",new Object[]{pv,pb,app,parent});
    }

    public BinaryFormElement createBinaryFormElement(final ParameterValue pv,
            final ParameterBean pb) {
        return (BinaryFormElement)create("binaryFormElement",new Object[]{pv,pb});
    }

    public BooleanFormElement createBooleanFormElement(final ParameterValue pv,
            final ParameterBean pb) {
        return (BooleanFormElement)create("booleanFormElement",new Object[]{pv,pb});
    }

    public EnumerationFormElement createEnumerationFormElement(
            final ParameterValue pv, final ParameterBean pb) {
        return (EnumerationFormElement)create("enumerationFormElement",new Object[]{pv,pb});
    }

    public LargeTextFormElement createLargeTextFormElement(final ParameterValue pv,
            final ParameterBean pb) {
        return (LargeTextFormElement)create("largeFormElement",new Object[]{pv,pb});
    }

    public LooselyFormattedFormElement createLooselyFormattedFormElement(
            final ParameterValue pv, final ParameterBean pb, final Format format) {
        return (LooselyFormattedFormElement)create("looseFormElement",new Object[]{pv,pb,format});
    }

    public OutputFormElement createOutputFormElement(final ParameterValue pv,
            final ParameterBean pb) {
        return (OutputFormElement)create("outputFormElement",new Object[]{pv,pb});
    }

    public PositionFormElement createPositionFormElement(final ParameterValue ra,
            final ParameterBean raDesc, final ParameterValue dec, final ParameterBean decDesc, final UIComponent parent) {
        return (PositionFormElement)create("positionFormElement",new Object[]{ra,raDesc,dec,decDesc,parent});
    }
    

    public RadiusFormElement createRadiusFormElement(final ParameterValue radius,
            final ParameterBean radiusDesc) {
        return (RadiusFormElement)create("radiusFormElement",new Object[]{radius,radiusDesc});
    }

    public RadiusFormElement createRadiusFormElement(final ParameterValue radius,
            final ParameterBean radiusDesc, final DecSexToggle toggle) {
        return (RadiusFormElement)create("radiusFormElementExternal",new Object[]{radius,radiusDesc,toggle});
        
    }

    public TextFormElement createTextFormElement(final ParameterValue pv,
            final ParameterBean pb) {
        return (TextFormElement)create("textFormElement",new Object[]{pv,pb});
    }

    public TaskParametersForm createTaskParametersForm(final UIComponentWithMenu parent) {
        return (TaskParametersForm)create("taskParametersForm",parent);
    }

    public FileNavigator createFileNavigator(final UIComponent parent,
            final MatcherEditor ed, final ActivitiesManager acts) {
        return (FileNavigator) create("fileNavigator",new Object[]{parent, ed, acts});
    }

    public FileNavigator createFileNavigator(final UIComponent parent,
            final ActivitiesManager acts) {
        return createFileNavigator(parent,new FixedMatcherEditor(Matchers.trueMatcher()),acts);
    }

    public ParametersInfoPane createParametersInfoPane(final Model model,
            final EventList<AbstractTaskFormElement> elements) {
        return (ParametersInfoPane)create("parametersInfoPane",new Object[]{model,elements});
     }

    public ResourceViewer createAnnotatedResourceView() {
        return (ResourceViewer)create("annotatedResourceView");
    }

    public ResourceViewer createFormattedResourceView() {
        return (ResourceViewer)create("formattedResourceView");
    }

    public ResourceViewer createResultsResourceView(final AstroScopeLauncherImpl parent,
            final ActivitiesManager acts) {
        return (ResourceViewer)create("resultsResourceView",new Object[]{parent,acts});        
    }

    public ScopeServicesList createScopeServicesList(final AstroScopeLauncherImpl parent,
            final ActivitiesManager acts) {
        return (ScopeServicesList)create("scopeServiceList",new Object[]{parent,acts});
    }

    public ResourceViewer createTableResourceView() {
        return (ResourceViewer)create("tableResourceView");
    }

    public ResourceViewer createXMLResourceView(final UIComponent parent) {
        return (ResourceViewer)create("xmlResourceView",parent);
    }

    public MultiConeImpl createMultiCone() {
        return (MultiConeImpl)create("multicone");
    }




    

}
