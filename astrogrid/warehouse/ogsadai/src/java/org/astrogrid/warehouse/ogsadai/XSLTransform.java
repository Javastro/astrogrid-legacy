// (c) Astrogrid 2004

package org.astrogrid.warehouse.ogsadai;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.apache.log4j.Logger;

import uk.org.ogsadai.common.UserException;
import uk.org.ogsadai.common.XMLUtilities;
import uk.org.ogsadai.porttype.gds.activity.transform.Constants;

import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.ActivityOutputException;
import uk.org.ogsadai.client.toolkit.DataFormatException;

/**
 * This is an OGSA-DAI activity to convert OGSA-DAI XML Rowset output from
 * other activities using an XSLT stylesheet.
 * <P>
 * I'm not sure why this activity wasn't provided as part of the OGSA-DAI
 * client toolkit?  Maybe it will be in the next release?
 * <P>
 * It has two inputs (the XML to be transformed, and the XSL stylesheet) 
 * and one output (the transformed XML). 
 *
 * @author K.E.Andrews
 */
public class XSLTransform extends Activity {

    // Logger object for logging in this class
    private static Logger mLog =
        Logger.getLogger(XSLTransform.class.getName());

    // Index of inputs 
    private static final int XML_INPUT_INDEX = 0;
    private static final int XSLT_INPUT_INDEX = 1;
    // Index of archived output.
    private static final int XSL_OUTPUT_INDEX = 0;
    // Input name
    private static final String XML_INPUT_NAME = "xmlInput";
    private static final String XSLT_INPUT_NAME = "xsltInput";
    private static final String XSL_OUTPUT_NAME = "xslOutput";

    /** 
     * Constructs a request to transform data produced by another activity
     * using an XSLT stylesheet delivered by another activity.
     *
     * @param xmlInput input XML to be transformed, from another activity.
     * @param xsltInput input XSL stylesheet to be used, delivered by another 
     * activity.
     */
    public XSLTransform(ActivityOutput xmlInput, ActivityOutput xsltInput) {
        // Tell base class of inputs and outputs
      if ((xmlInput == null) || (xsltInput == null)) {
        throw new IllegalArgumentException("ActivityOutput must not be null");
      }
      addInput(XML_INPUT_NAME);
      super.setInput(XML_INPUT_INDEX, xmlInput.getName());
      addInput(XSLT_INPUT_NAME);
      super.setInput(XSLT_INPUT_INDEX, xsltInput.getName());
      addOutput(XSL_OUTPUT_NAME);
    }

    /** 
     * Gets the activity's XSLT data output.
     * 
     * @return the XSLT data output.
     */    
    public ActivityOutput getOutput() {
        return getOutputs()[XSL_OUTPUT_INDEX];
    }

    /**
     * Internal method to produce OGSA-DAI XML Perform document.
     * @see uk.org.ogsadai.client.toolkit.Activity#generateXML()
     *
     * @return String containing XML perform document for this activity.
     */
    protected String generateXML() {

        StringBuffer sb = new StringBuffer();
        sb.append("<xslTransform name=\"");
        sb.append(getName());
        sb.append("\">\n");
        sb.append("<inputXSLT from=\"");
        sb.append(getInputParameters()[XSLT_INPUT_INDEX].getOutputName());
        sb.append("\"/>\n");
        sb.append("<inputXML from=\"");
        sb.append(getInputParameters()[XML_INPUT_INDEX].getOutputName());
        sb.append("\"/>\n");

        sb.append("<output name=\"");
        sb.append(getOutputs()[XSL_OUTPUT_INDEX].getName());
        sb.append("\"/>\n");
        sb.append("</xslTransform>");
        return sb.toString();
    }
}
