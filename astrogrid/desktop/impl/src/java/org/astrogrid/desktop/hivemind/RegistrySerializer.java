/**
 * 
 */
package org.astrogrid.desktop.hivemind;

/**
 *
copied from hivemind source - unable to improve by extension
so had to cut-n-paste, and then edit the problem.
*/
//Copyright 2004, 2005 The Apache Software Foundation

//

// Licensed under the Apache License, Version 2.0 (the "License");

// you may not use this file except in compliance with the License.

// You may obtain a copy of the License at

//

//     http://www.apache.org/licenses/LICENSE-2.0

//

// Unless required by applicable law or agreed to in writing, software

// distributed under the License is distributed on an "AS IS" BASIS,

// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

// See the License for the specific language governing permissions and

// limitations under the License.






import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Attribute;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.ModuleDescriptorProvider;
import org.apache.hivemind.Occurances;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.DefaultErrorHandler;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.hivemind.internal.Visibility;
import org.apache.hivemind.parse.AttributeMappingDescriptor;
import org.apache.hivemind.parse.ConfigurationPointDescriptor;
import org.apache.hivemind.parse.ContributionDescriptor;
import org.apache.hivemind.parse.ConversionDescriptor;
import org.apache.hivemind.parse.CreateInstanceDescriptor;
import org.apache.hivemind.parse.DependencyDescriptor;
import org.apache.hivemind.parse.ImplementationDescriptor;
import org.apache.hivemind.parse.InstanceBuilder;
import org.apache.hivemind.parse.InterceptorDescriptor;
import org.apache.hivemind.parse.InvokeFactoryDescriptor;
import org.apache.hivemind.parse.ModuleDescriptor;
import org.apache.hivemind.parse.ServicePointDescriptor;
import org.apache.hivemind.parse.SubModuleDescriptor;
import org.apache.hivemind.schema.AttributeModel;
import org.apache.hivemind.schema.ElementModel;
import org.apache.hivemind.schema.Rule;
import org.apache.hivemind.schema.impl.SchemaImpl;
import org.apache.hivemind.schema.rules.CreateObjectRule;
import org.apache.hivemind.schema.rules.InvokeParentRule;
import org.apache.hivemind.schema.rules.PushAttributeRule;
import org.apache.hivemind.schema.rules.PushContentRule;
import org.apache.hivemind.schema.rules.ReadAttributeRule;
import org.apache.hivemind.schema.rules.ReadContentRule;
import org.apache.hivemind.schema.rules.SetModuleRule;
import org.apache.hivemind.schema.rules.SetParentRule;
import org.apache.hivemind.schema.rules.SetPropertyRule;
import org.apache.hivemind.util.IdUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;



/**
 * Used within {@code GenerateHivedoc}.
 * 
 * This class serializes a set of {@link ModuleDescriptor module descriptors} into a

 * {@link Document XML document}. The set of module descriptors to process is specified indirectly

 * by supplying one or several {@link ModuleDescriptorProvider} (see

 * {@link #addModuleDescriptorProvider(ModuleDescriptorProvider)}). In this respect this class is

 * used the same way as {@link org.apache.hivemind.impl.RegistryBuilder}. There is even a

 * corresponding {@link #createDefaultRegistryDocument() static method} to serialize the modules of

 * the default registry.

 * <p>

 * The resulting XML file does not conform to the hivemind module deployment descriptor schema. The

 * following changes occur:

 * <ul>

 * <li>The outermost element is &lt;registry&gt; (which contains a list of &lt;module&gt;)

 * <li>A unique id (unique within the file) is assigned to each &lt;module&gt;,

 * &lt;configuration-point&gt;, &lt;service-point&gt;, &lt;contribution&gt;, &tl;schema&gt; and

 * &lt;implementation&gt; (this is to make it easier to generate links and anchors)

 * <li>Unqualified ids are converted to qualified ids (whereever possible).

 * </ul>

 * 

 * @author Knut Wannheden

 * @since 1.1

 */

class RegistrySerializer

{

    private final Set _processedSchemas = new HashSet();



    private final List _providers = new ArrayList();



    private final ErrorHandler _handler;



    private Document _document;



    private ModuleDescriptor _md;



    public RegistrySerializer()

    {

        _handler = new DefaultErrorHandler();

    }



    public void addModuleDescriptorProvider(final ModuleDescriptorProvider provider)

    {

        _providers.add(provider);

    }



    public Document createRegistryDocument()

    {

        final DocumentBuilder builder = getBuilder();



        _document = builder.newDocument();



        final Element registry = _document.createElement("registry");



        _document.appendChild(registry);



        for (final Iterator i = _providers.iterator(); i.hasNext();)

        {

            final ModuleDescriptorProvider provider = (ModuleDescriptorProvider) i.next();



            processModuleDescriptorProvider(registry, provider);

        }



        return _document;

    }



    private void processModuleDescriptorProvider(final Element registry, final ModuleDescriptorProvider provider)

    {

        for (final Iterator j = provider.getModuleDescriptors(_handler).iterator(); j.hasNext();)

        {

            _md = (ModuleDescriptor) j.next();



            final Element module = getModuleElement(_md);



            registry.appendChild(module);

        }

    }



    private Element getModuleElement(final ModuleDescriptor md)

    {

        final Element module = _document.createElement("module");



        module.setAttribute("id", md.getModuleId());

        module.setAttribute("version", md.getVersion());

        module.setAttribute("package", md.getPackageName());



        module.appendChild(_document.createTextNode(md.getAnnotation()));



        addDependencies(module);



        addServicePoints(module);



        addConfigurationPoints(module);



        addContributions(module);



        addImplementations(module);



        addSchemas(module);



        addSubModules(module);



        return module;

    }



    private void addDependencies(final Element module)

    {

        final List dependencies = _md.getDependencies();



        if (dependencies != null)

        {

            for (final Iterator i = dependencies.iterator(); i.hasNext();)

            {

                final DependencyDescriptor dd = (DependencyDescriptor) i.next();



                final Element dependency = getDependencyElement(dd);



                module.appendChild(dependency);

            }

        }

    }



    private void addServicePoints(final Element module)

    {

        final List servicePoints = _md.getServicePoints();



        if (servicePoints != null)

        {

            for (final Iterator i = servicePoints.iterator(); i.hasNext();)

            {

                final ServicePointDescriptor spd = (ServicePointDescriptor) i.next();



                final Element servicePoint = getServicePointElement(spd);



                module.appendChild(servicePoint);



                final SchemaImpl s = spd.getParametersSchema();



                if (s != null && s.getId() != null) {
                    addSchema(module, s, "schema");
                }

            }

        }

    }



    private void addConfigurationPoints(final Element module)

    {

        final List configurationPoints = _md.getConfigurationPoints();



        if (configurationPoints != null)

        {

            for (final Iterator i = configurationPoints.iterator(); i.hasNext();)

            {

                final ConfigurationPointDescriptor cpd = (ConfigurationPointDescriptor) i.next();



                final Element configurationPoint = getConfigurationPointElement(cpd);



                module.appendChild(configurationPoint);



                final SchemaImpl s = cpd.getContributionsSchema();



                if (s != null && s.getId() != null) {
                    addSchema(module, s, "schema");
                }

            }

        }

    }



    private void addContributions(final Element module)

    {

        final List contributions = _md.getContributions();



        if (contributions != null)

        {

            for (final Iterator i = contributions.iterator(); i.hasNext();)

            {

                final ContributionDescriptor cd = (ContributionDescriptor) i.next();



                final Element contribution = getContributionElement(cd);



                module.appendChild(contribution);

            }

        }

    }



    private void addImplementations(final Element module)

    {

        final List implementations = _md.getImplementations();



        if (implementations != null)

        {

            for (final Iterator i = implementations.iterator(); i.hasNext();)

            {

                final ImplementationDescriptor id = (ImplementationDescriptor) i.next();



                final Element implementation = getImplementationElement(id);



                module.appendChild(implementation);

            }

        }

    }



    private void addSchemas(final Element module)

    {

        final Collection schemas = _md.getSchemas();



        for (final Iterator i = schemas.iterator(); i.hasNext();)

        {

            final SchemaImpl s = (SchemaImpl) i.next();



            addSchema(module, s, "schema");

        }

    }



    private void addSubModules(final Element module)

    {

        final List subModules = _md.getSubModules();



        if (subModules != null)

        {

            for (final Iterator i = subModules.iterator(); i.hasNext();)

            {

                final SubModuleDescriptor smd = (SubModuleDescriptor) i.next();



                final Element subModule = getSubModuleElement(smd);



                module.appendChild(subModule);

            }

        }

    }



    private Element getDependencyElement(final DependencyDescriptor dd)

    {

        final Element dependency = _document.createElement("dependency");



        dependency.setAttribute("module-id", dd.getModuleId());

        dependency.setAttribute("version", dd.getVersion());



        return dependency;

    }



    private Element getServicePointElement(final ServicePointDescriptor spd)

    {

        final Element servicePoint = _document.createElement("service-point");



        servicePoint.setAttribute("id", qualify(spd.getId()));

        servicePoint.setAttribute("interface", spd.getInterfaceClassName());

        if (spd.getVisibility() == Visibility.PRIVATE) {
            servicePoint.setAttribute("visibility", "private");
        }

        if (spd.getParametersCount() != Occurances.REQUIRED) {
            servicePoint.setAttribute("parameters-occurs", spd.getParametersCount().getName()

                    .toLowerCase());
        }



        servicePoint.appendChild(_document.createTextNode(spd.getAnnotation()));



        if (spd.getParametersSchema() != null) {
            addSchema(servicePoint, spd.getParametersSchema(), "parameters-schema");
        } else if (spd.getParametersSchemaId() != null) {
            servicePoint.setAttribute("parameters-schema-id", qualify(spd.getParametersSchemaId()));
        }



        final InstanceBuilder ib = spd.getInstanceBuilder();



        if (ib != null)

        {

            final Element instanceBuilder = getInstanceBuilderElement(ib);



            servicePoint.appendChild(instanceBuilder);

        }



        final List interceptors = spd.getInterceptors();



        if (interceptors != null)

        {

            for (final Iterator i = interceptors.iterator(); i.hasNext();)

            {

                final InterceptorDescriptor icd = (InterceptorDescriptor) i.next();



                final Element interceptor = getInterceptorElement(icd);



                servicePoint.appendChild(interceptor);

            }

        }



        return servicePoint;

    }



    private Element getConfigurationPointElement(final ConfigurationPointDescriptor cpd)

    {

        final Element configurationPoint = _document.createElement("configuration-point");



        configurationPoint.setAttribute("id", qualify(cpd.getId()));

        if (cpd.getVisibility() == Visibility.PRIVATE) {
            configurationPoint.setAttribute("visibility", "private");
        }



        configurationPoint.appendChild(_document.createTextNode(cpd.getAnnotation()));



        if (cpd.getContributionsSchema() != null) {
            addSchema(configurationPoint, cpd.getContributionsSchema(), "schema");
        } else if (cpd.getContributionsSchemaId() != null) {
            configurationPoint.setAttribute("schema-id", qualify(cpd.getContributionsSchemaId()));
        }



        return configurationPoint;

    }



    private Element getContributionElement(final ContributionDescriptor cd)

    {

        final Element contribution = _document.createElement("contribution");



        contribution.setAttribute("configuration-id", qualify(cd.getConfigurationId()));



        if (cd.getConditionalExpression() != null) {
            contribution.setAttribute("if", cd.getConditionalExpression());
        }



        final List parameters = cd.getElements();



        if (parameters != null)

        {

            for (final Iterator i = parameters.iterator(); i.hasNext();)

            {

                final org.apache.hivemind.Element parameter = (org.apache.hivemind.Element) i.next();



                final Element element = getParamterElement(parameter);



                contribution.appendChild(element);

            }

        }



        contribution.appendChild(_document.createTextNode(cd.getAnnotation()));



        return contribution;

    }



    private Element getImplementationElement(final ImplementationDescriptor id)

    {

        final Element implementation = _document.createElement("implementation");



        implementation.setAttribute("service-id", qualify(id.getServiceId()));



        if (id.getConditionalExpression() != null) {
            implementation.setAttribute("if", id.getConditionalExpression());
        }



        implementation.appendChild(_document.createTextNode(id.getAnnotation()));



        final InstanceBuilder ib = id.getInstanceBuilder();



        if (ib != null)

        {

            final Element instanceBuilder = getInstanceBuilderElement(ib);



            implementation.appendChild(instanceBuilder);

        }



        final List interceptors = id.getInterceptors();



        if (interceptors != null)

        {

            for (final Iterator i = interceptors.iterator(); i.hasNext();)

            {

                final InterceptorDescriptor icd = (InterceptorDescriptor) i.next();



                final Element interceptor = getInterceptorElement(icd);



                implementation.appendChild(interceptor);

            }

        }



        return implementation;

    }



    private Element getSubModuleElement(final SubModuleDescriptor smd)

    {

        final Element subModule = _document.createElement("sub-module");



        subModule.setAttribute("descriptor", smd.getDescriptor().getPath());



        return subModule;

    }



    private Element getInstanceBuilderElement(final InstanceBuilder ib)

    {

        Element instanceBuilder;



        if (ib instanceof CreateInstanceDescriptor)

        {

            final CreateInstanceDescriptor cid = (CreateInstanceDescriptor) ib;

            instanceBuilder = _document.createElement("create-instance");



            instanceBuilder.setAttribute("class", cid.getInstanceClassName());

            if (!cid.getServiceModel().equals("singleton")) {
                instanceBuilder.setAttribute("model", cid.getServiceModel());
            }

        }

        else

        {

            final InvokeFactoryDescriptor ifd = (InvokeFactoryDescriptor) ib;

            instanceBuilder = _document.createElement("invoke-factory");



            if (!ifd.getFactoryServiceId().equals("hivemind.BuilderFactory")) {
                instanceBuilder.setAttribute("service-id", qualify(ifd.getFactoryServiceId()));
            }

            if (ifd.getServiceModel() != null) {
                instanceBuilder.setAttribute("model", ifd.getServiceModel());
            }



            final List parameters = ifd.getParameters();



            if (parameters != null)

            {

                for (final Iterator i = parameters.iterator(); i.hasNext();)

                {

                    final org.apache.hivemind.Element parameter = (org.apache.hivemind.Element) i.next();



                    final Element element = getParamterElement(parameter);



                    instanceBuilder.appendChild(element);

                }

            }

        }



        return instanceBuilder;

    }



    private Element getInterceptorElement(final InterceptorDescriptor icd)

    {

        final Element interceptor = _document.createElement("interceptor");



        interceptor.setAttribute("service-id", qualify(icd.getFactoryServiceId()));

        if (icd.getBefore() != null) {
            interceptor.setAttribute("before", icd.getBefore());
        }

        if (icd.getAfter() != null) {
            interceptor.setAttribute("after", icd.getAfter());
        }

        return interceptor;

    }



    private Element getParamterElement(final org.apache.hivemind.Element parameter)
    {
        final Element element = _document.createElement(parameter.getElementName());
        final List attributes = parameter.getAttributes();
        for (final Iterator i = attributes.iterator(); i.hasNext();)
        {
            final Attribute attribute = (Attribute) i.next();
            element.setAttribute(attribute.getName(), attribute.getValue());
        }

        //NWW - added code to get text content of elements too
        final String content = parameter.getContent();
        if (content != null && content.trim().length() > 0) {
        	//1.5 only, element.setTextContent(content);
        	final Text t = _document.createTextNode(content);
        	element.appendChild(t);
        }
        //NWW end

        final List elements = parameter.getElements();
        

        for (final Iterator i = elements.iterator(); i.hasNext();)

        {

            final org.apache.hivemind.Element nestedParameter = (org.apache.hivemind.Element) i.next();



            element.appendChild(getParamterElement(nestedParameter));

        }



        return element;

    }



    private void addSchema(final Element container, final SchemaImpl s, final String elementName)

    {

        if (_processedSchemas.contains(s)) {
            return;
        }



        final Element schema = _document.createElement(elementName);



        if (s.getId() != null) {
            schema.setAttribute("id", qualify(s.getId()));
        }



        if (s.getVisibility() == Visibility.PRIVATE) {
            schema.setAttribute("visibility", "private");
        }



        schema.appendChild(_document.createTextNode(s.getAnnotation()));



        for (final Iterator j = s.getElementModel().iterator(); j.hasNext();)

        {

            final ElementModel em = (ElementModel) j.next();



            final Element element = getElementElement(em);



            schema.appendChild(element);

        }



        container.appendChild(schema);



        _processedSchemas.add(s);

    }



    private Element getRulesElement(final ElementModel em)

    {

        final Element rules = _document.createElement("rules");



        for (final Iterator i = em.getRules().iterator(); i.hasNext();)

        {

            final Rule r = (Rule) i.next();



            Element rule = null;



            if (r instanceof CreateObjectRule)

            {

                final CreateObjectRule cor = (CreateObjectRule) r;

                rule = _document.createElement("create-object");



                rule.setAttribute("class", cor.getClassName());

            }

            else if (r instanceof InvokeParentRule)

            {

                final InvokeParentRule ipr = (InvokeParentRule) r;

                rule = _document.createElement("invoke-parent");



                rule.setAttribute("method", ipr.getMethodName());

                if (ipr.getDepth() != 1) {
                    rule.setAttribute("depth", Integer.toString(ipr.getDepth()));
                }

            }

            else if (r instanceof PushAttributeRule)

            {

                final PushAttributeRule par = (PushAttributeRule) r;

                rule = _document.createElement("push-attribute");



                rule.setAttribute("attribute", par.getAttributeName());

            }

            else if (r instanceof PushContentRule)

            {              

                rule = _document.createElement("push-content");

            }

            else if (r instanceof ReadAttributeRule)

            {

                final ReadAttributeRule rar = (ReadAttributeRule) r;

                rule = _document.createElement("read-attribute");



                rule.setAttribute("property", rar.getPropertyName());

                rule.setAttribute("attribute", rar.getAttributeName());

                if (!rar.getSkipIfNull()) {
                    rule.setAttribute("skip-if-null", "false");
                }

                if (rar.getTranslator() != null) {
                    rule.setAttribute("translator", rar.getTranslator());
                }

            }

            else if (r instanceof ReadContentRule)

            {

                final ReadContentRule rcr = (ReadContentRule) r;

                rule = _document.createElement("read-content");



                rule.setAttribute("property", rcr.getPropertyName());

            }

            else if (r instanceof SetModuleRule)

            {

                final SetModuleRule smr = (SetModuleRule) r;

                rule = _document.createElement("set-module");



                rule.setAttribute("property", smr.getPropertyName());

            }

            else if (r instanceof SetParentRule)

            {

                final SetParentRule spr = (SetParentRule) r;

                rule = _document.createElement("set-parent");



                rule.setAttribute("property", spr.getPropertyName());

            }

            else if (r instanceof SetPropertyRule)

            {

                final SetPropertyRule spr = (SetPropertyRule) r;

                rule = _document.createElement("set-property");



                rule.setAttribute("property", spr.getPropertyName());

                rule.setAttribute("value", spr.getValue());

            }

            else if (r instanceof ConversionDescriptor)

            {

                final ConversionDescriptor cd = (ConversionDescriptor) r;

                rule = _document.createElement("conversion");



                rule.setAttribute("class", cd.getClassName());

                if (!cd.getParentMethodName().equals("addElement")) {
                    rule.setAttribute("parent-method", cd.getParentMethodName());
                }



                for (final Iterator j = cd.getAttributeMappings().iterator(); j.hasNext();)

                {

                    final AttributeMappingDescriptor amd = (AttributeMappingDescriptor) j.next();



                    final Element map = _document.createElement("map");



                    map.setAttribute("attribute", amd.getAttributeName());

                    map.setAttribute("property", amd.getPropertyName());



                    rule.appendChild(map);

                }

            }

            else

            {

                rule = _document.createElement("custom");



                rule.setAttribute("class", r.getClass().getName());

            }



            if (rule != null) {
                rules.appendChild(rule);
            }

        }

        return rules;

    }



    private Element getElementElement(final ElementModel em)

    {

        final Element element = _document.createElement("element");

        element.setAttribute("name", em.getElementName());



        element.appendChild(_document.createTextNode(em.getAnnotation()));



        for (final Iterator i = em.getAttributeModels().iterator(); i.hasNext();)

        {

            final AttributeModel am = (AttributeModel) i.next();



            final Element attribute = getAttributeElement(am);



            element.appendChild(attribute);

        }



        for (final Iterator i = em.getElementModel().iterator(); i.hasNext();)

        {

            final ElementModel nestedEm = (ElementModel) i.next();



            final Element nestedElement = getElementElement(nestedEm);



            element.appendChild(nestedElement);

        }



        if (!em.getRules().isEmpty())

        {

            final Element rules = getRulesElement(em);



            element.appendChild(rules);

        }



        return element;

    }



    private Element getAttributeElement(final AttributeModel am)

    {

        final Element attribute = _document.createElement("attribute");



        attribute.setAttribute("name", am.getName());

        if (am.isRequired()) {
            attribute.setAttribute("required", "true");
        }

        if (am.isUnique()) {
            attribute.setAttribute("unique", "true");
        }

        if (!am.getTranslator().equals("smart")) {
            attribute.setAttribute("translator", am.getTranslator());
        }



        attribute.appendChild(_document.createTextNode(am.getAnnotation()));



        return attribute;

    }



    private String qualify(final String id)

    {

        return IdUtils.qualify(_md.getModuleId(), id);

    }



    private DocumentBuilder getBuilder()

    {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // needs to be namespace aware to work with xslt.



        factory.setIgnoringComments(true);



        try

        {

            return factory.newDocumentBuilder();

        }

        catch (final ParserConfigurationException e)

        {

            throw new ApplicationRuntimeException(e);

        }

    }



    public static Document createDefaultRegistryDocument()

    {

        final ClassResolver resolver = new DefaultClassResolver();

        final ModuleDescriptorProvider provider = new XmlModuleDescriptorProvider(resolver);



        final RegistrySerializer serializer = new RegistrySerializer();



        serializer.addModuleDescriptorProvider(provider);



        return serializer.createRegistryDocument();

    }

}
