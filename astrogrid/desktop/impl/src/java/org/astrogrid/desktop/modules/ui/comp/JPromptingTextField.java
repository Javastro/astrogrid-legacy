/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Color;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;

/**
 * Text Field that provides a greyed-out prompt when no input is present.
 * 
 * 
 * clients of this class must use getValue() ad setValue() to work with this
 * class. getText() and setText() will return the placeholder text.
 * 
 * alternately, register a property change listener for 'value' - and check that 
 * newValue() != oldValue() - as this component seems to emit a little noise.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 4, 20072:46:21 PM
 */
public class JPromptingTextField extends JFormattedTextField {

    /**
     * formatter that gives a standard way of converting the string back to a value.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Oct 5, 200710:24:56 AM
     */
    protected abstract class MyFormatter extends
    JFormattedTextField.AbstractFormatter {

        @Override
        public Object stringToValue(final String text) throws ParseException {
            if (text == null) {
                return null;
            }
            if (text.trim().length() == 0) {
                return null;
            }
            return text;
            
        }

    }

    /** formatter for displaying the value
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Oct 4, 20075:20:51 PM
     */
    protected final class DisplayFormatter extends MyFormatter {

        @Override
        public String valueToString(final Object value) throws ParseException {
            if (value == null || ((String) value).trim().length() == 0) {
                setForeground(getDisabledTextColor());
                return placeholder;
            } else {
                setForeground(Color.BLACK);
                return (String) value;
            }

        }
    }

    /** formatter for editing the value */
    protected final class EditFormatter extends MyFormatter {

        @Override
        public String valueToString(final Object value) throws ParseException {
            setForeground(Color.BLACK);
            return (String) value;
        }
    }

    /** formatter for a null value (in display mode only)
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Oct 4, 20075:20:37 PM
     */
    protected final class NullFormatter extends MyFormatter {

        @Override
        public String valueToString(final Object value) throws ParseException {
            setForeground(getDisabledTextColor());
            return placeholder;
        }
    }

    /**
     * formatter factory, based on default formatter, but which only uses
     * nullFormatted when not editing.
     * 
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Oct 4, 20075:19:40 PM
     */
    private static final class MyFormatterFactory extends DefaultFormatterFactory {
        @Override
        public JFormattedTextField.AbstractFormatter getFormatter(
                final JFormattedTextField source) {
            JFormattedTextField.AbstractFormatter format = null;
            if (source == null) {
                return null;
            }
            final Object value = source.getValue();

            if (value == null && !source.hasFocus()) {
                format = getNullFormatter();
            }
            if (format == null) {
                if (source.hasFocus()) {
                    format = getEditFormatter();
                } else {
                    format = getDisplayFormatter();
                }
                if (format == null) {
                    format = getDefaultFormatter();
                }
            }
            return format;
        }
    }

    private final String placeholder;
    /**
     * name of the property to monitor to be notified when the value of this field changes.
     */
    public static final String VALUE_PROPERTY = "value";

    /**
     * 
     */
    public JPromptingTextField(final String placeholder) {
        this.placeholder = placeholder;
        setFocusLostBehavior(JFormattedTextField.COMMIT);
        final DefaultFormatterFactory formatter = new MyFormatterFactory();
        formatter.setNullFormatter(new NullFormatter());
        formatter.setDisplayFormatter(new DisplayFormatter());
        formatter.setEditFormatter(new EditFormatter());
        setFormatterFactory(formatter);

    }

    
    public static void main(final String[] args) {
        final JButton b = new JButton("hogs focus");

        final JPromptingTextField tf = new JPromptingTextField("placeholder");
        // custom formatter, with different rules.

        tf.setColumns(22);
        tf.addPropertyChangeListener(VALUE_PROPERTY, new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                if (evt.getNewValue() != evt.getOldValue()) {
                    System.err.println(tf.getValue());
                }
            }
        });
        final JPanel p = new JPanel(new FlowLayout());
        p.add(b);
        p.add(tf);

        final JFrame f = new JFrame();
        f.setContentPane(p);
        f.pack();
        f.show();
    }
}