/*
 * $Id Metadata.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathResult;
import org.w3c.dom.xpath.XPathSetSnapshot;

/**
 * Represents the databases metadata, providing methods to access it.
 * Actually just wraps the metadata document, and provides some standard
 * convenience methods for often-used activities.
 *
 * @author M Hill
 */

public class Metadata
{
   Document metadataDom = null;
   
   public Metadata(Document metadataFromServer)
   {
      this.metadataDom = metadataFromServer;
   }

   public Metadata(Element metadataFromServer)
   {
      this.metadataDom = metadataFromServer.getOwnerDocument();
   }

   /**
    * Returns metadata document for whatever the client wants to do with it
    * For safety, it (tries to) return a clone, so that the client can't
    * change it. If cloning is not supported, you get the real thing.
    */
   public Document getDocument()
   {
      try
      {
         return (Document) metadataDom.clone();
      }
      catch (CloneNotSupportedException cnse)
      {
         return metadataDom;
      }
   }
   
   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema.
    * @todo Return a VOResource object model once VOResource exists
    */
   public Element getVoRegistryMetadata()
   {
      //transform to VOResource... err...
      return metadataDom.getDocumentElement();

   }

    /**
     * returns the elements in the metadata file that match the given XPath.
    * <p>
    * The xpath stuff doesn't seem to have settled yet - which is why the results are
    * therefore an array of nodes, rather than a nodelist (can't get a NodeList
    * at Sep 2003)
     */
   public Node[] getMetadata(String xpath) throws DatacenterException
   {
      try {
         XPathEvaluator evaluator = (XPathEvaluator) metadataDom; //Document should implement XPathEvaluator
         XPathResult results = evaluator.evaluate(xpath, metadataDom,
               null, XPathResult.NODE_SET_TYPE, null);

         XPathSetSnapshot set = results.getSetSnapshot(false);
         
         Node[] nodes = new Node[set.getLength()];
         
         for (int i=0;i<nodes.length;i++) {
            nodes[i] = set.item(i);
         }
         return nodes;
      }
      catch (XPathException e) {
         throw new DatacenterException(xpath + " is not a correct XPath expression");
      }
      catch (DOMException e) {
         throw new DatacenterException("Bleurgh", e);
      }
   }
   
   
}

/*
$Log: Metadata.java,v $
Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
