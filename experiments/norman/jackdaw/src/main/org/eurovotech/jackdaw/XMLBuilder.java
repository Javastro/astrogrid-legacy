package org.eurovotech.jackdaw;
// Copied from Norman's .../code/misc/XMLBuilder.java

import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convenience class to create and serialise XML.
 *
 * <p>This provides a convenient wrapper around the DOM interface,
 * which makes it easier to put together XML output.  Specifically,
 * most of the methods return an object of an appropriate type, making
 * it easier to chain methods together.
 *
 * <p>This class defines two nested classes, <code>XMLBuilder.Node</code>
 * and <code>XMLBuilder.Doc</code>, a subclass of it.
 *
 * <p>Create a document with document element "foo", by
 * <pre>
 * XMLBuilder.Doc xb = new XMLBuilder().newDocument("foo");
 * </pre>
 *
 * <p>Add attributes and children to Nodes with the methods
 * {@link XMLBuilder.Node#addAttribute}, {@link XMLBuilder.Node#newChild},
 * and {@link XMLBuilder.Node#newSibling},
 * which have multiple signatures.  You can create a set of Nodes,
 * which can be manipulated and later inserted into a document as a
 * tree, with the method {@link XMLBuilder.Doc#newNodeSet}.
 *
 * <p>For example, a program which builds up an XSLT stylesheet might
 * include
 * <pre>
 * XMLBuilder.Node chooseElement = xb.newNodeSet();
 * XMLBuilder.Node myTemplate = xb.newChild("template")
 *                                .addAttribute("match", "/");
 * // ...
 * chooseElement
 *        .newChild("otherwise")
 *        .newChild("text", "Here is an error message: n=")
 *        .newSibling("value-of").addAttribute("select", "$n");
 * // ...
 * myTemplate.newChild("choose", chooseElement);
 * </pre>
 * <p>That would produce XML corresponding to
 * <pre>
 * &lt;template match='/'&gt;
 *   ...
 *   &lt;otherwise&gt;
 *     &lt;text&gt;Here is an error message: n=&lt;/text&gt;
 *     &lt;value-of select="$n"/&gt;
 *   &lt;/otherwise&gt;
 * &lt;/template&gt;
 * </pre>
 */
public class XMLBuilder {
    Document d;
    boolean indentOutput = false;

    public XMLBuilder() {
        try {
            d = javax.xml.parsers.DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .getDOMImplementation()
                    .createDocument(null, null, null);
        } catch (Exception e) {
            // There's a variety of distinct exceptions can appear here.
            // I don't care about the difference between any of them,
            // and all of them are `can't happen'.
            // So turn them all into assertion errors!
            throw new AssertionError
                    ("Failed to create XML DOM (this shouldn't happen!)");
        }
    }

    /**
     * Create a new document using the document builder associated
     * with this XMLBuilder.
     * @param documentElementName the name of the top-level element
     */
    public Doc newDocument(String documentElementName) {
        return new Doc(documentElementName);
    }

    /**
     * Indicate whether the generated XML should be 'indented' (which
     * actually means 'line-broken')
     * @param flag true if the output should be indented
     * @return this object
     */
    public XMLBuilder setIndent(boolean flag) {
        indentOutput = flag;
        return this;
    }

    /**
     * A representation of a node (ie, element) in an XML tree
     */
    public class Node {
        org.w3c.dom.Node e;
        private Node() {
            // nothing
        }
        private Node(String name) {
            assert d != null;
            e = d.createElement(name);
        }

        /**
         * Create a new child of this element
         * @param name the name of the new element
         * @return the newly created node
         */
        public Node newChild(String name) {
            assert name != null;
            Node n = new Node(name);
            this.e.appendChild(n.e);
            return n;
        }

        /**
         * Create a new child of this element, with text content
         * @param name the name of the new element
         * @param content the text content of the node
         * @return the newly created node
         */
        public Node newChild(String name, String content) {
            Node n = new Node(name);
            n.e.appendChild(d.createTextNode(content));
            this.e.appendChild(n.e);
            return n;
        }

        /**
         * Create a new child of this element, whose children are
         * obtained by copying one or more nodes from
         * the nodeset created using {@link XMLBuilder.Doc#newNodeSet}.
         * @param name the name of the new element
         * @param newNodes the node content of the new node
         * @return the newly created node
         */
        public Node newChild(String name, Node newNodes) {
            Node n = new Node(name);
            n.e.appendChild(newNodes.e.cloneNode(true));
            this.e.appendChild(n.e);
            return n;
        }

        /**
         * Create a new sibling of this element.
         * The new node is appended as the last sibling of this node
         * @param name the name of the new element
         * @return the newly created node
         */
        public Node newSibling(String name) {
            Node n = new Node(name);
            this.e.getParentNode().appendChild(n.e);
            return n;
        }

        /**
         * Create a new sibling of this element, with text content.
         * The new node is appended as the last sibling of this node
         * @param name the name of the new element
         * @param content the text content of the node
         * @return the newly created node
         */
        public Node newSibling(String name, String content) {
            Node n = new Node(name);
            n.e.appendChild(d.createTextNode(content));
            this.e.getParentNode().appendChild(n.e);
            return n;
        }

        /**
         * Create a new sibling of this element, copying one or more
         * nodes from the nodeset created using
         * {@link XMLBuilder.Doc#newNodeSet}.
         * The new node is appended as the last sibling of this node
         * @param name the name of the new element
         * @param newNodes the node content of the new node
         * @return the newly created node
         */
        public Node newSibling(String name, Node newNodes) {
            Node n = new Node(name);
            n.e.appendChild(newNodes.e.cloneNode(true));
            this.e.getParentNode().appendChild(n.e);
            return n;
        }

        /**
         * Add a comment after this node
         * @param comment the text of the comment
         * @return this node
         */
        public Node newComment(String comment) {
            this.e.appendChild(d.createComment(comment));
            return this;
        }

        /**
         * Add an attribute to this element
         * @param key the attribute name
         * @param value the attribute value
         * @return this node
         */
        public Node addAttribute(String key, String value) {
            if (e instanceof Element)
                ((Element)e).setAttribute(key, value);
            return this;
        }
    }

    /**
     * A document which can be built up then serialised
     */
    public class Doc extends Node {

        /**
         * Create a new XML builder document, which will produce an XML document
         * with the given document element
         */
        private Doc(String documentElementName) {
            e = d.createElement(documentElementName);
            d.appendChild(e);
        }

        /**
         * Create a new set of nodes.  These may later be inserted into a
         * document using
         * {@link XMLBuilder.Node#newChild(String,XMLBuilder.Node)}
         * @return a new Node
         */
        public Node newNodeSet() {
            Node n = new Node();
            n.e = d.createDocumentFragment();
            return n;
        }

        /**
         * Serialise the XMLBuilder object to the given Writer
         */
        public void serialise(java.io.PrintWriter pw) 
                throws IOException {
            serialise(new StreamResult(pw));
        }

        /**
         * Serialise the XMLBuilder object to the given Stream
         */
        public void serialise(java.io.PrintStream ps) 
                throws IOException {
            serialise(new StreamResult(ps));
        }

        private Transformer identity;
        private void serialise(StreamResult res) 
                throws IOException {
            try {
                if (identity == null) {
                    identity = javax.xml.transform.TransformerFactory
                            .newInstance()
                            .newTransformer();
                    // set the transformer to indent -- marginally prettier
                    if (indentOutput)
                        identity.setOutputProperty("indent", "yes");
                }
                identity.transform(new javax.xml.transform.dom.DOMSource(d),
                                   res);
            } catch (javax.xml.transform.TransformerException e) {
                throw (IOException)new IOException("Error serialising")
                        .initCause(e);
            }
        }
    }
}
