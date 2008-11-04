/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

/** utilities for converting CEA Tool objects between XML Document and Java Structure representations. */
class DocumentToStructureConversion {
public static Map convertDocumentToStruct(final Document doc) throws InvalidArgumentException {

    Tool tool;
    try {
        tool = (Tool)Unmarshaller.unmarshal(Tool.class,doc);    
        if (! tool.isValid()) {
            // unmarshall will probalbyl catch this.
            throw new InvalidArgumentException("The document is not a valid tool");
        }
        final Map<String,Object> result = new LinkedHashMap<String,Object>();
        result.put("interface",tool.getInterface());
        result.put("name",tool.getName());
        final Hashtable<String,Object> inputs= new Hashtable<String,Object>();
        final Hashtable<String,Object> outputs = new Hashtable<String,Object>();
        if (tool.getInput() != null) {
            result.put("input",inputs);
            for (int i = 0; i < tool.getInput().getParameterCount(); i++) {
                convertParameterToHash(inputs, tool.getInput().getParameter(i));
            }
        }
        if (tool.getOutput() != null) {
            result.put("output",outputs);
            for (int i = 0; i < tool.getOutput().getParameterCount(); i++) {
                convertParameterToHash(outputs, tool.getOutput().getParameter(i));
            }
        }
        return result;
    } catch (final MarshalException e) {
        throw new InvalidArgumentException(e);
    } catch (final ValidationException e) {
        throw new InvalidArgumentException(e);
    }        
}

private static void convertParameterToHash(final Hashtable<String,Object> parameterSet,final ParameterValue v) {        
    final Hashtable<String,Object> newParam = new Hashtable<String,Object>();
    newParam.put("value",v.getValue());
    newParam.put("indirect",Boolean.valueOf( v.getIndirect()));
    //bz2253
    if (parameterSet.containsKey(v.getName())) { // already has a param of the same name
        final Object existingParam = parameterSet.get(v.getName());
        if (existingParam instanceof List) { // already more than one value
            ((List)existingParam).add(newParam);
        } else if (existingParam instanceof Map) {// a single value already
            // would like to have a list of just values, but indirect may vary between parameters too - so need a list of parameter Hashes.
            final List vect = new Vector();
            vect.add(existingParam);
            vect.add(newParam);
            parameterSet.put(v.getName(),vect);
        } else {
            throw new  ProgrammerError("Unexpected " + existingParam);
        }
        
    } else {
        parameterSet.put(v.getName(),newParam);
    }
}

private static  ParameterValue convertHashToParameter(final String name,final Map<String,Object> h) {
    final ParameterValue v= new ParameterValue();
    v.setName(name);
    v.setIndirect(((Boolean)h.get("indirect")).booleanValue());
    v.setValue(h.get("value").toString());
    return v;
}

private static  ParameterValue[] convertParams(final Map<String,Object> inputHash) {
   // ParameterValue[] arr = new ParameterValue[inputHash.size()];
    final List<ParameterValue> pvs= new ArrayList<ParameterValue>(inputHash.size()); // using a list, as there's at least hash.size() params, but there might be more.
    for (final Map.Entry<String,Object> e : inputHash.entrySet()) {
        final Object valObj = e.getValue();
        //bz2253
        if (valObj instanceof List) {
            for(final Map<String,Object> param : (List<Map<String,Object>>)valObj) {
                pvs.add(convertHashToParameter(e.getKey(),param));                           
            }
        } else if (valObj instanceof Map) {
            pvs.add(convertHashToParameter(e.getKey(),(Map<String,Object>)e.getValue()));                        
        } else {
            throw new IllegalStateException("Unexpected type - " + valObj);
        }                      
    }
    return pvs.toArray(new ParameterValue[pvs.size()]);
}

public static Document convertStructToDocument(final Map struct) throws InvalidArgumentException {
    final Tool t = new Tool();
    t.setName(struct.get("name").toString());
    t.setInterface(struct.get("interface").toString());
    final org.astrogrid.workflow.beans.v1.Input input = new org.astrogrid.workflow.beans.v1.Input();
    
    Map paramHash= (Map)struct.get("input");
    if (paramHash != null) {    
        input.setParameter(convertParams(paramHash));
        t.setInput(input);
    }
    
    final Output output = new Output();
    paramHash = (Map)struct.get("output");
    if (paramHash != null) {
        output.setParameter(convertParams(paramHash));
        t.setOutput(output);
    }

    try {
        final Document doc = XMLUtils.newDocument();
        Marshaller.marshal(t,doc);
        return doc;
    } catch (final ParserConfigurationException e) {
        throw new InvalidArgumentException(e);
    } catch (final MarshalException e) {
        throw new InvalidArgumentException(e);
    } catch (final ValidationException e) { 
        throw new InvalidArgumentException(e);
    }
}
}