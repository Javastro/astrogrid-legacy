/*
 * $Id Metadata.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
    * <p>
    * For safety, it should return a clone so the client can't change it. However
    * there seems to be a problem with some Document implementations that make
    * the clone method private - highly naughty behaviour - so we have to return
    * the real thing.
    */
   public Document getDocument()
   {
      return metadataDom;
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
    * <p> Even worse the xpath implementations don't seem to be settled, so this
    * method just does a getElementByName...
     */
   public NodeList getMetadata(String elementName) throws DatacenterException
   {
      /* Xpath version
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
      }*/

      /** element by name version... */
      return metadataDom.getElementsByTagName(elementName);
   }
   
   
}

/*
$Log: Metadata.java,v $
Revision 1.3  2003/11/05 16:06:41  mch
Removed dependencies on Document.clone() and dodgy XPath implementations

Revision 1.2  2003/10/13 14:11:41  nw
commented out chunks that weren't compiling.
need to fix later.

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
