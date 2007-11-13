package org.astrogrid.desktop.modules.ui.folders;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import junit.framework.TestCase;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.system.XStreamXmlPersist;
import org.astrogrid.desktop.modules.system.XmlPersist;

public class BranchBeanUnitTest extends TestCase {

    private static final XmlPersist persister = new XStreamXmlPersist();

    public void testSerialization() throws IOException, ServiceException {
        DefaultMutableTreeNode sampleNode =
            (DefaultMutableTreeNode) new JTree().getModel().getRoot();
        assertTrue(sampleNode.getChildCount() > 1);
        assertTrue(sampleNode.getDepth() > 1);
        assertXmlRoundTrip(sampleNode);
    }

    public void assertXmlRoundTrip(DefaultMutableTreeNode node)
            throws IOException, ServiceException {
        DefaultMutableTreeNode reNode = fromString(toString(node));
        assertEquals(node.getDepth(), reNode.getDepth());
        assertEquals(node.getChildCount(), reNode.getChildCount());
        assertEquals(toString(node), toString(reNode));
    }

    /**
     * Serializes a tree node to a string via a BranchBean.
     *
     * @param  node  tree node
     * @return   string representation
     */
    public static String toString(TreeNode node)
            throws IOException, ServiceException {
        StringWriter sw = new StringWriter();
        Object bean = BranchBean.fromTreeNode(node);
        persister.toXml(bean, sw);
        return sw.toString();
    }

    /**
     * Deserializes to a tree node via a BranchBean.
     *
     * @param   string representation
     * @return  tree node
     */
    public static DefaultMutableTreeNode fromString(String s)
            throws IOException, ServiceException {
        return BranchBean.toTreeNode(persister.fromXml(new StringReader(s)));
    }
}
