/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.ui.folders.Folder;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.SmartList;
import org.astrogrid.desktop.modules.ui.folders.StaticList;
import org.astrogrid.desktop.modules.ui.folders.StorageFolder;
import org.astrogrid.desktop.modules.ui.folders.XQueryList;
import org.astrogrid.desktop.modules.ui.scope.ScopeHistoryProvider.SearchHistoryItem;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.KeywordSRQLVisitor;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQL;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLParser;
import org.astrogrid.desktop.modules.ui.voexplorer.srql.SRQLVisitor;
import org.astrogrid.desktop.modules.votech.Annotation;
import org.astrogrid.desktop.modules.votech.UserAnnotation;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.core.BaseException;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**  Implementation of persistence using xstream.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 31, 20073:54:35 PM
 */
public class XStreamXmlPersist implements XmlPersist {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(XStreamXmlPersist.class);

    private final XStream xstream;
    public XStreamXmlPersist() {
        xstream = new XStream(
                new PureJavaReflectionProvider()
                //new Sun14ReflectionProvider()
                //,new StaxDriver() gives ugly output, and xpp jar is only 25sk anyhow.                
        );
        
        // custom convertors, etc
        
        // if we developo a lot of these, could  register them all via hivemind configurations...
        xstream.alias("annotation",UserAnnotation.class);
        xstream.registerConverter(new URIConverter());
        xstream.registerConverter(new ColorConverter());
        xstream.omitField(Annotation.class,"source");
        xstream.addImmutableType(Color.class);
        
        xstream.alias("astroscope-history",SearchHistoryItem.class);
        
        
        xstream.alias("folder",Folder.class);
        xstream.alias("storage-folder",StorageFolder.class);
        xstream.omitField(StorageFolder.class,"file");
        xstream.omitField(SRQL.class,"class");
        
        xstream.useAttributeFor(ResourceFolder.class,"fixed");
        xstream.useAttributeFor(Folder.class,"name");
        xstream.useAttributeFor(Folder.class,"iconName");
        
        xstream.alias("smart-list",SmartList.class);
        xstream.registerConverter(new SmartListConverter());
        xstream.registerConverter(new SRQLConverter());
        
        xstream.alias("resource-list",StaticList.class); // can't call it 'list' as it conflicts.
        xstream.addImplicitCollection(StaticList.class,"resouceUris","resource",URI.class);// stuck with this mis-spelling of resourceUris now - but map it to something sensible.

        xstream.alias("xquery-list",XQueryList.class);
        
        
        
    }
    
    public Object fromXml(InputStream is) throws ServiceException {
        try {
            return xstream.fromXML(is);
        } catch (BaseException e) {
            throw new ServiceException(e);
        }
    }

    public Object fromXml(Reader r) throws ServiceException {
        try {
            return xstream.fromXML(r);
        } catch (BaseException e) {
            throw new ServiceException(e);
        }
    }

    public Object fromXml(String s) throws ServiceException {
        try {
            return xstream.fromXML(s);
        } catch (BaseException e) {
            throw new ServiceException(e);
        }
    }

    public void toXml(Object o, OutputStream os) throws ServiceException {
        try {
            xstream.toXML(o,os);
        } catch (BaseException e) {
            throw new ServiceException(e);
        }        
    }

    public void toXml(Object o, Writer w) throws ServiceException {
        try {
            xstream.toXML(o,w);
        } catch (BaseException e) {
            throw new ServiceException(e);
        }           
    }

    public String toXml(Object o) throws ServiceException {
        try {
            return xstream.toXML(o);
        } catch (BaseException e) {
            throw new ServiceException(e);
        }   
    }
    
    /** convertor for smart lists - necessary to remove
     * class="" attribute on query field.
     * a bit brittle - as needs to 'match' the configuration (e.g. attribute names) used for other kinds of resource folder 
     */
    public static class SmartListConverter implements Converter {

        public void marshal(Object arg0, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            SmartList sl = (SmartList)arg0;
            
            writer.addAttribute("name",sl.getName());
            writer.addAttribute("iconName",sl.getIconName());
            writer.addAttribute("fixed",Boolean.toString(sl.isFixed()));
            writer.startNode("query");
            context.convertAnother(sl.getQuery());
            writer.endNode();
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                UnmarshallingContext context) {
            SmartList sl = new SmartList();
            final String fixedVal = reader.getAttribute("fixed");
            if (fixedVal != null) {
                sl.setFixed(Boolean.valueOf(fixedVal).booleanValue());
            }
            final String nameVal = reader.getAttribute("name");
            if (nameVal != null) {
                sl.setName(nameVal);
            }
            final String iconVal = reader.getAttribute("iconName");
            if (iconVal != null) {
                sl.setIconName(iconVal);
            }
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                if ("query".equals(reader.getNodeName())) {
                    SRQL q = (SRQL)context.convertAnother(sl,SRQL.class);
                    sl.setQuery(q);
                }
                reader.moveUp();
            }
            return sl;
        }

        public boolean canConvert(Class arg0) {
            return arg0 == SmartList.class;
        }
    }

    /** converter for SRQL */
    public static class SRQLConverter implements Converter {

        public boolean canConvert(Class arg0) {
            return SRQL.class.isAssignableFrom(arg0);
        }


        private final SRQLVisitor visitor = new KeywordSRQLVisitor();
        public void marshal(Object arg0, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            SRQL srql = (SRQL)arg0;
            try {
                writer.setValue((String)srql.accept(visitor));
            } catch (Throwable t) {
                throw new ConversionException(t);
            }
            
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                UnmarshallingContext context) {
            String s = reader.getValue();
            try {
                return (new SRQLParser(s)).parse();
            } catch (Throwable t) {
                throw new ConversionException(t);
            }
        }
    }
    
    /** converter for java.net.URI */
    public static class URIConverter extends AbstractSingleValueConverter{

        public boolean canConvert(Class arg0) {
            return arg0.equals(URI.class);
        }

        public Object fromString(String arg0) {
            try {
                return new URI(arg0);
            } catch (URISyntaxException x) {
                throw new ConversionException(x);
            }
        } 
    }
    
    /** convertor for java.awt.Color */
    public static class ColorConverter implements Converter {

        public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
            Color c = (Color)arg0;
            int i = c.getRGB();
            arg1.setValue("#" + Integer.toHexString(i).substring(2,8)); // omit the alpha chanel, otherwise can't parse back in. odd.
        }

        public Object unmarshal(HierarchicalStreamReader arg0, UnmarshallingContext arg1) {
            try {
                return Color.decode(arg0.getValue());
            } catch (NumberFormatException e) {
                throw new ConversionException(e);
            }
        }

        public boolean canConvert(Class arg0) {
            return arg0.equals(Color.class);
        }
    }
    
}