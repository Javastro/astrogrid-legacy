package org.astrogrid.registry.client.admin;


import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;


public class RegistryAdminDocumentHelper {
  
  public RegistryAdminDocumentHelper() {
     
  } 
  
  
   public static Document createDocument(TreeMap tm) {
      Document registryDoc = null;
      try {
         
         DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         registryDoc = registryBuilder.newDocument();
         Element root = registryDoc.createElement("VODescription");
         registryDoc.appendChild(root);
         Element voresource = registryDoc.createElement("VOResource");
         root.appendChild(voresource);
         Set st = tm.keySet();
         Iterator iter = st.iterator();
         String key = null;
         String val = null;
         String []elems = null;
         StringBuffer sb = new StringBuffer(200);
         String temp = null;
         String tempElems = ((String)tm.firstKey());
         //SchemaBuilder sb = new SchemaBuilder("voresource");       
         boolean hasAttribute = false;
         while(iter.hasNext()) {
            key = (String)iter.next();
            val = (String)tm.get(key);
            elems = key.split(".");
//            tempElems = closeNodes(key,tempElems);
            if(elems.length > 0) {
               for(int i=1;i < elems.length;i++) {
                  if("attr".equals(elems[i])) {
                     hasAttribute = true; 
                     i++;
                     sb.insert((sb.length() - 2),elems[i]);
                     //sb.addAttribute(elems[i],val);                    
                  }else {
                     /*if(!sb.contains(elems[i])) {
                        //sb.addElement(elems[i]);
                       }
                     */
                      
                     if(sb.indexOf("</" + elems[i]) == -1) {
                        sb.append("<").append(elems[i]).append(">");
                     }//if
                  }//else
               }//for
               //place the val here
               if(!hasAttribute) {
                  sb.append(val);
                  sb.append(elems[(elems.length-1)]);
               }else {
                  sb.insert((sb.length() - 2),"='" + val + "'");
               }
            }//if
         }//while
      }catch(Exception e) {
         e.printStackTrace();
      }
      return registryDoc;
   }
   
   private static String closeNodes(String elems,String tempElems) {
      //remember elems is the next string
      //tempElems is the current string.
      if(elems != null && tempElems != null && elems.length() > 0 && tempElems.length() > 0) {
         String []elemSplit = elems.split(".");
         String []tempElemsSplit = tempElems.split(".");
         if(elems.equals(tempElems)) {
            //probably the very first time so return nothing.
            return tempElems;
         }
         for(int i = 0; i < tempElemsSplit.length;i++) {
            if(tempElemsSplit[i].equals(elemSplit[i]) ) {
               
            }else {
               //we don't match
               
               if("attr".indexOf(elems) != -1) {
                  //need to think about this.
                  return elems.substring(0,elems.indexOf(".attr"));
               }else {
                  String closeNode = "</" + tempElemsSplit[i] + ">";
                  return elems;   
               }
            }//else
         }
            
      }
      return "";
   }
   
}


/*
class SchemaBuilder {
   
   private String elementName = null;
   private String []elementAttributes = null;
   private SchemaBuilder childElement = null;
   
}
*/