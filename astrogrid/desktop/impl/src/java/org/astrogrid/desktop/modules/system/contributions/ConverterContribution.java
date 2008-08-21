/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import org.apache.commons.beanutils.Converter;

/** Contribution bean for registering converters.
 * used to configure how to convert string inputs into objects
 * biuilds on apache beanutiles converters framework.
 * @author Noel Winstanley
 * @since Apr 14, 20062:22:16 AM
 */
public class ConverterContribution {
  private Class output;
  private Converter converter;
  private boolean array;
/**
 * @return the converter
 */
public Converter getConverter() {
	return this.converter;
}
/**
 * @param converter the converter to set
 */
public void setConverter(final Converter converter) {
	this.converter = converter;
}
/** set the class of output this converter can produce.
 * @return the output
 */
public Class getOutput() {
	return this.output;
}
/**
 * @param output the output to set
 */
public void setOutput(final Class output) {
	this.output = output;
}

}
