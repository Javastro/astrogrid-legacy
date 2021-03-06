package org.apache.tools.ant1point6.taskdefs;

import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputRequest;
import org.apache.tools.ant.input.MultipleChoiceInputRequest;
import org.apache.tools.ant.util.StringUtils;

/**
 * Reads an input line from the console.
 *
 * AGINAB requires the Ant 1.6 version of this task,
 * which won't be around any time soon in Maven.
 * Hence, this override.
 *
 * @since Ant 1.5
 *
 * @ant.task category="control"
 */
public class Input extends Task {
    private String validargs = null;
    private String message = "";
    private String addproperty = null;
    private String defaultvalue = null;

    /**
     * Defines valid input parameters as comma separated strings. If set, input
     * task will reject any input not defined as accepted and requires the user
     * to reenter it. Validargs are case sensitive. If you want 'a' and 'A' to
     * be accepted you need to define both values as accepted arguments.
     *
     * @param validargs A comma separated String defining valid input args.
     */
    public void setValidargs (String validargs) {
        this.validargs = validargs;
    }

    /**
     * Defines the name of a property to be created from input. Behaviour is
     * according to property task which means that existing properties
     * cannot be overridden.
     *
     * @param addproperty Name for the property to be created from input
     */
    public void setAddproperty (String addproperty) {
        this.addproperty = addproperty;
    }

    /**
     * Sets the Message which gets displayed to the user during the build run.
     * @param message The message to be displayed.
     */
    public void setMessage (String message) {
        this.message = message;
    }

    /**
     * Defines the default value of the property to be created from input.
     * Property value will be set to default if not input is received.
     *
     * @param defaultvalue Default value for the property if no input
     * is received
     */
    public void setDefaultvalue (String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    /**
     * Set a multiline message.
     * @param msg The message to be displayed.
     */
    public void addText(String msg) {
        message += getProject().replaceProperties(msg);
    }

    /**
     * No arg constructor.
     */
    public Input () {
    }

    /**
     * Actual method executed by ant.
     * @throws BuildException on error
     */
    public void execute () throws BuildException {
        if (addproperty != null
            && getProject().getProperty(addproperty) != null) {
            log("skipping " + getTaskName() + " as property " + addproperty
                + " has already been set.");
            return;
        }

        InputRequest request = null;
        if (validargs != null) {
            Vector accept = StringUtils.split(validargs, ',');
            request = new MultipleChoiceInputRequest(message, accept);
        } else {
            request = new InputRequest(message);
        }

        getProject().getInputHandler().handleInput(request);

        String value = request.getInput();
        if ((value == null || value.trim().length() == 0)
            && defaultvalue != null) {
            value = defaultvalue;
        }
        if (addproperty != null && value != null) {
            getProject().setNewProperty(addproperty, value);
        }
    }

}
