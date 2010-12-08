/*
 * (c) Copyright AstroGrid 2010.
 */
package org.astrogrid.dataservice.service.vosi;

/**
 * A string builder that knows about some XML constructs.
 *
 * @author Guy Rixon
 */
public class XmlBuilder {
  
  private final StringBuilder builder;

  public XmlBuilder() {
    builder = new StringBuilder();
  }

  public void append(CharSequence c) {
    builder.append(c);
  }

  public void openTag(String tagName) {
    builder.append('<');
    builder.append(tagName);
    builder.append('>');
  }

  public void closeTag(String tagName) {
    builder.append("</");
    builder.append(tagName);
    builder.append('>');
  }

  public void appendElement(String name, String value) {
    openTag(name);
    builder.append(value);
    closeTag(name);
  }

  public void newLine() {
    builder.append('\n');
  }

  @Override
  public String toString() {
    return builder.toString();
  }

}
