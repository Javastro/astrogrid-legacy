/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import org.apache.commons.beanutils.Converter;

/** Contribution bean for registering converters.
 * @author Noel Winstanley
 * @since Apr 14, 20062:22:16 AM
 */
public class ConverterContribution {
  private Class output;
  private Converter converter;
/**
 * @return the converter
 */
public Converter getConverter() {
	return this.converter;
}
/**
 * @param converter the converter to set
 */
public void setConverter(Converter converter) {
	this.converter = converter;
}
/**
 * @return the output
 */
public Class getOutput() {
	return this.output;
}
/**
 * @param output the output to set
 */
public void setOutput(Class output) {
	this.output = output;
}
  
}
