/*$Id: ApplicationInformationBuilder.java,v 1.8 2006/04/21 13:48:12 nw Exp $
 * Created on 07-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.builders;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.xpath.CachedXPathAPI;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** Information Builder for Application Registry Entries.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Sep-2005
 *
 */
public class ApplicationInformationBuilder extends ResourceInformationBuilder {


    /**
     * @see org.astrogrid.desktop.modules.ag.builders.InformationBuilder#isApplicable(org.apache.xpath.CachedXPathAPI, org.w3c.dom.Element)
     */
    public boolean isApplicable(CachedXPathAPI xpath, Element el) {
        try {
            return xpath.eval(el, "contains(@xsi:type,'CeaApplicationType') or contains(@xsi:type,'CeaHttpApplicationType')",nsNode ).bool();
            //return StringUtils.contains(type, "CeaApplicationType") || StringUtils.contains(type,"CeaHttpApplicationType");
        } catch (TransformerException e) {  
            logger.debug("TransformerException",e);
            return false;
        }
    }


    public ResourceInformation build(CachedXPathAPI xpath, Element element) throws ServiceException {
        try {

        NodeList l = xpath.selectNodeList(element,".//ceab:Interface",nsNode);
        InterfaceBean[] interfaces = new InterfaceBean[l.getLength()];
        for (int i = 0; i < interfaces.length; i++) {
            interfaces[i] = buildBeanFromInterfaceElement(xpath,((Element)l.item(i)));
        }
        l = xpath.selectNodeList(element,".//cea:ParameterDefinition",nsNode);        
        Map parameters = new HashMap();
        for (int i =0; i < l.getLength(); i++) {
            ParameterBean pb = buildBeanFromParameterElement(xpath,(Element)l.item(i));
            parameters.put(pb.getName(),pb);
        }
        return new ApplicationInformation(
                findId(xpath,element)
                ,findName(xpath,element)
                ,findDescription(xpath,element)
                ,parameters
                ,interfaces
                ,findAccessURL(xpath,element)
                ,findLogo(xpath,element)
                );
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }
    }
    
    private  ParameterBean buildBeanFromParameterElement(CachedXPathAPI xpath,Element element)  throws TransformerException{
        String[] options = null;
        NodeList l = xpath.selectNodeList(element,"ceapd:OptionList/ceapd:OptionVal",nsNode);
        if (l.getLength() > 0) { //otherwise leave as null
            options = new String[l.getLength()];
            for (int i = 0; i < options.length; i++) {
                options[i] = ((Element)l.item(i)).getFirstChild().getNodeValue();
            }
        }
        return new ParameterBean(
                xpath.eval(element,"normalize-space(@name)",nsNode).str()
                ,xpath.eval(element,"normalize-space(ceapd:UI_Name)",nsNode).str()
                ,xpath.eval(element,"normalize-space(ceapd:UI_Description)",nsNode).str()
                ,xpath.eval(element,"normalize-space(ceapd:UCD)",nsNode).str()
                ,xpath.eval(element,"normalize-space(ceapd:DefaultValue)",nsNode).str()
                ,xpath.eval(element,"normalize-space(ceapd:Units)",nsNode).str()
                ,xpath.eval(element,"normalize-space(@type)",nsNode).str()
                ,xpath.eval(element,"normalize-space(@subtype)",nsNode).str()
                ,options
                );
    }
    
    private  InterfaceBean buildBeanFromInterfaceElement(CachedXPathAPI xpath, Element element)  throws TransformerException{
        NodeList l = xpath.selectNodeList(element,"ceab:input/ceab:pref",nsNode);
        ParameterReferenceBean[] inputs = new ParameterReferenceBean[l.getLength()];
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = builtBeanFromParameterReference(xpath,(Element)l.item(i));
        }
        l = xpath.selectNodeList(element,"ceab:output/ceab:pref",nsNode);        
        ParameterReferenceBean[] outputs = new ParameterReferenceBean[l.getLength()];
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = builtBeanFromParameterReference(xpath,(Element)l.item(i));
        }         
        return new InterfaceBean(
                xpath.eval(element,"normalize-space(@name)",nsNode).str()
                ,inputs
                ,outputs
                );
    }
    
    private  ParameterReferenceBean builtBeanFromParameterReference(CachedXPathAPI xpath,Element element) throws TransformerException{
        int max;
        int min;
        try {
            max = Integer.parseInt(xpath.eval(element,"@maxoccurs",nsNode).str()); //@todo use xpath to parse number here?
        } catch (NumberFormatException e) {
            max = 1; // default
        }
        try {
            min = Integer.parseInt(xpath.eval(element,"@minoccurs",nsNode).str())   ;
        } catch (NumberFormatException e) {
            min = 1; // default;
        }
            
        return new ParameterReferenceBean(
                xpath.eval(element,"normalize-space(@ref)",nsNode).str()
                ,max
                ,min             
                );
    }

}


/* 
$Log: ApplicationInformationBuilder.java,v $
Revision 1.8  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.7  2006/03/02 11:32:28  nw
fixed parsing of option lists -- sorry phil!

Revision 1.6  2005/12/02 13:40:32  nw
optimized, and made more error-tolerant

Revision 1.5  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.4  2005/10/18 16:53:34  nw
refactored common functionality.
added builders for siap and cone.

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.6.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.2  2005/10/05 08:47:59  pjn3
String contains (1.5) changed to StringUtils.contains (1.4)

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/