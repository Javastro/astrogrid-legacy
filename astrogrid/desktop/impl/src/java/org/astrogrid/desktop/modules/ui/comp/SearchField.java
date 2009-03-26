/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import org.astrogrid.desktop.icons.IconHelper;

/** based on code cribbed from http://software.jessies.org/salma-hayek/
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 1, 200712:15:29 PM
 */

/**
 * A text field for search/filter interfaces. The extra functionality includes
 * a placeholder string (when the user hasn't yet typed anything), and a button
 * to clear the currently-entered text.
 * 
 * 
 *if you want to listen for dociument changes, use {@link #getWrappedDocument()}
 *unless you want to receive notifactions for the placeholder text as well.
 * 
 * Use only for incremental search UIs. For other cases where you want to show some greyed-out prompting
 * text, use {@link JPromptingTextField}
 * 
 * 
 * @author Elliott Hughes
 */

public class SearchField extends JTextField {
    private boolean sendsNotificationForEachKeystroke = false;
    boolean showingPlaceholderText = false;
    private WrappedDocument wrappedDocument;
    
    public SearchField(String placeholderText) {
        super(new ZappableDocument(placeholderText),null,10);
        this.wrappedDocument = new WrappedDocument(getDocument(),placeholderText);
        addFocusListener(new PlaceholderText());
        initBorder();
        initKeyListener();
    }
    
    public SearchField() {
        this("Filter");
    }
    
    /** call this to access a document object which fires events, etc
     * _only_ when placeholderText is not shown in box.
     * 
     * calling getDocument() will cause spurious notifications to be received.
     * @return
     */
    public Document getWrappedDocument() {
        return wrappedDocument;
    }
    
    
    private void initBorder() {
        new CancelBorder(){

            @Override
            public void buttonActivated(MouseEvent e) {
                    cancel();
                }                
        }.attachTo(this);
        new SearchIconBorder().attachTo(this);
    }
    
    private void initKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancel();
                } else if (sendsNotificationForEachKeystroke) {
                    maybeNotify();
                }
            }
        });
    }
    
    
    
    private void cancel() {
        setText("");
        postActionEvent();
    }
    
    private void maybeNotify() {
        if (showingPlaceholderText) {
            return;
        }
        postActionEvent();
    }
    
    public void setSendsNotificationForEachKeystroke(boolean eachKeystroke) {
        this.sendsNotificationForEachKeystroke = eachKeystroke;
    }
    
    static final Color GRAY = new Color(0.7f, 0.7f, 0.7f);
    
    /**
     * inactive border which just draws a search icon.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Aug 1, 20071:15:24 PM
     */
    static class SearchIconBorder extends InteractiveBorder {

        private final Image image;
        public SearchIconBorder() {
            super(16,true); // 16 px, on the left?
            this.image = IconHelper.loadIcon("search16.png").getImage();
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y,
                int width, int height) {
            g.drawImage(image,x,y,c);
        }
        @Override
        public void buttonActivated(MouseEvent e) {
            // do nothing
        }
        
    }
    
    /**
     * Replaces the entered text with a gray placeholder string when the
     * search field doesn't have the focus. The entered text returns when
     * we get the focus back.
     */
    class PlaceholderText implements FocusListener {
        private String previousText = "";
        private Color previousColor;

        PlaceholderText() {
            focusLost(null);
        }

        public void focusGained(FocusEvent e) {
            setForeground(previousColor);
            if (showingPlaceholderText) {
                setText(previousText);
                showingPlaceholderText = false;
            }
        }

        public void focusLost(FocusEvent e) {
            previousText = getText();
            previousColor = getForeground();
            if (previousText.length() == 0) {
                showingPlaceholderText = true;
                setForeground(Color.GRAY);
                ((ZappableDocument)getDocument()).zapText();
            }
        }
    }
    
    /** document that doesn't fire notifications when text is set to the placeholder text 
     * 
     * this replaces the original document.
     * */
    private static class ZappableDocument extends PlainDocument {
        private final String placeholderText;
        
        /** zap the contents of the document, replacing it with the placeholder text, without firing any event notifications */
        public void zapText() {
            Content content = getContent();
            int sz = content.length();
            try {
                content.remove(0,sz-1);
                content.insertString(0,placeholderText);
            } catch (BadLocationException x) {
                x.printStackTrace();// very unlikely.
            }
        }
      
        public ZappableDocument(String placeholderText) {
            super();
            this.placeholderText = placeholderText;
        }
        
    }
    /** wrapper arund a document which is returned by any external client calling 'getDocument'
     * this wrapper hides the fact that placeholder text is present in the box.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Aug 1, 20072:21:46 PM
     */
    private static class WrappedDocument implements Document {
        private final Document original;
        private final String placeholderText;

        /**
         * @param document
         * @param placeholderText
         */
        public WrappedDocument(Document document, String placeholderText) {
            this.placeholderText = placeholderText;
            this.original = document;
        }

        public void addDocumentListener(DocumentListener listener) {
            this.original.addDocumentListener(listener);
        }

        public void addUndoableEditListener(UndoableEditListener listener) {
            this.original.addUndoableEditListener(listener);
        }

        public Position createPosition(int offs) throws BadLocationException {
            return this.original.createPosition(offs);
        }

        public Element getDefaultRootElement() {
            return this.original.getDefaultRootElement();
        }

        public Position getEndPosition() {
            return this.original.getEndPosition();
        }

        public int getLength() {
            // hide the fact that there's placeholder text here
            int len = original.getLength();
            try {
                if (placeholderText.equals(getText(0,len))) {
                    return 0;
                }
            } catch (BadLocationException x) {
                return len;
            } 
            return len;
        }

        public Object getProperty(Object key) {
            return this.original.getProperty(key);
        }

        public Element[] getRootElements() {
            return this.original.getRootElements();
        }

        public Position getStartPosition() {
            return this.original.getStartPosition();
        }

        public void getText(int offset, int length, Segment txt)
                throws BadLocationException {
            this.original.getText(offset, length, txt);
        }

        public String getText(int offset, int length)
                throws BadLocationException {
            return this.original.getText(offset, length);
        }

        public void insertString(int offset, String str, AttributeSet a)
                throws BadLocationException {
            this.original.insertString(offset, str, a);
        }

        public void putProperty(Object key, Object value) {
            this.original.putProperty(key, value);
        }

        public void remove(int offs, int len) throws BadLocationException {
            this.original.remove(offs, len);
        }

        public void removeDocumentListener(DocumentListener listener) {
            this.original.removeDocumentListener(listener);
        }

        public void removeUndoableEditListener(UndoableEditListener listener) {
            this.original.removeUndoableEditListener(listener);
        }

        public void render(Runnable r) {
            this.original.render(r);
        }
    }
}

