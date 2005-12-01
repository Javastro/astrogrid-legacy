package org.astrogrid.common.j2ee.environment;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * A Java bean representing an environment entry in a web-application.
 * <p>
 * The bean has the following properties: name, type, description,
 * defaultValue, operationValue and replacementValue.
 * <p>
 * The first four properties map to the elements env-entry-name, env-entry-type,
 * description, and env-entry-value that define the environment entry in web.xml.
 * These properties must all be set buy the bean's client; the bean itself
 * does not parse web.xml.
 * <p>
 * The operationValue property of the bean represents the current value of the
 * environment entry in the context of the running web-application. There is
 * no setter for this property and the value is read from JNDI. If the bean
 * should be used outside a web application then the value of this property
 * depends on the JNDI environment. if JNDI cannot deliver a value, then the
 * property value is null.
 * <p>
 * The replacementValue property of the bean holds the value for the
 * environment entry set by some client of the bean that is editing the
 * environment. Until this property is set by the client, the bean makes
 * the property equal to the operationalValue property. Once the property is
 * explictly set it keeps its value until changed by the client, even if the
 * operationalValue property changes.
 *
 * @author Guy Rixon
 */
public class EnvEntry {

  /** Creates a new instance of EnvEntry */
  public EnvEntry() {
  }

  /**
   * The name of the entry. This maps to the env-entry-name element.
   */
  private String name;

  /**
   * Gets the name of the entry.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of the entry.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * The Java class-name of the entry. This maps to the env-entry-type element.
   */
  private String type;

  /**
   * Gets the Java class-name of the entry.
   */
  public String getType() {
    return this.type;
  }

  /**
   * Sets the Java class-name of the entry.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * The value of the entry set in web.xml. This maps to the env-entry-value element.
   * The types are constrained by the J2EE specification to be the classes
   * java.lang.Boolean, java.lang.Integer, java.lang.Float and java.lang.String.
   * That contraint is not enforced here, but the value is assumed to be of a
   * suitable type.
   */
  private Object defaultValue;

  /**
   * Gets the current value of the entry.
   */
  public Object getDefaultValue() {
    return this.defaultValue;
  }

  /**
   * Sets the value of the entry.
   */
  public void setDefaultValue(Object value) {
    this.defaultValue = value;
  }

  /**
   * The value of the entry currently used by the application.
   * The types are constrained by the J2EE specification to be the classes
   * java.lang.Boolean, java.lang.Integer, java.lang.Float and java.lang.String.
   * That contraint is not enforced here, but the value is assumed to be of a
   * suitable type.
   */
  private Object operationalValue;

  /**
   * Gets the current value of the entry.
   */
  public Object getOperationalValue() {
    // Read the value from JNDI.
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context)initCtx.lookup("java:comp/env");
      return envCtx.lookup(this.name);
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Sets the value of the entry.
   */
  public void setOperationalValue(Object value) {
    this.operationalValue = value;
  }

 /**
   * The value of the entry to be used in the next reconfiguration.
   * The types are constrained by the J2EE specification to be the classes
   * java.lang.Boolean, java.lang.Integer, java.lang.Float and java.lang.String.
   * That contraint is not enforced here, but the value is assumed to be of a
   * suitable type.
   */
  private Object replacementValue;

  /**
   * Gets the current value of the entry.
   */
  public Object getReplacementValue() {
    if (this.replacementValue != null) {
      return this.replacementValue;
    }
    else {
      return this.getOperationalValue();
    }
  }

  /**
   * Sets the value of the entry.
   */
  public void setReplacementValue(Object value) {
    this.replacementValue = value;
  }

  /**
   * A textual description of the use of the entry. This should contain
   * plain text rather than XML and should be fairly short:
   * a couple of sentences at most. This maps to the
   * description element.
   */
  private String description;

  /**
   * Gets the description of the entry's purpose.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the entry's purpose.
   * All white-space characters are replaced with
   * spaces. This eliminates line breaks which would
   * otherwise cause problems in the formatting of the
   * text.
   */
  public void setDescription(String description) {
    this.description = description.replaceAll("\\s", " ");
  }

}
