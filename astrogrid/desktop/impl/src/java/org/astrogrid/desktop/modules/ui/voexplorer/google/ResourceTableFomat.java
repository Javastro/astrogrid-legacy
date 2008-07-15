package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.Color;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.HasCoverage;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.acr.ivoa.resource.Validation;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.resource.PrettierResourceFormatter;
import org.astrogrid.desktop.modules.ui.comp.ModularColumn;
import org.astrogrid.desktop.modules.ui.comp.ModularTableFormat;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.votech.Annotation;
import org.astrogrid.desktop.modules.votech.AnnotationService;
import org.astrogrid.desktop.modules.votech.UserAnnotation;
import org.astrogrid.desktop.modules.votech.VoMonInternal;

import ca.odell.glazedlists.GlazedLists;

/** Glazed Lists format for a table of registry entries
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 07-Sep-2005
 * @author Mark Taylor
 */

public class ResourceTableFomat extends ModularTableFormat {

    protected final static String STATUS_NAME = "Status";
    protected final static String LABEL_NAME = "Title";
    protected final static String CAPABILITY_NAME = "Capability";
    protected final static String DATE_NAME = "Date";
    protected final static String SHORT_NAME = "Short Name";
    protected final static String SUBJECT_NAME = "Subject";
    protected final static String PUBLISHER_NAME = "Publisher";
    protected final static String CONTACT_NAME = "Contact";
    protected final static String CREATOR_NAME = "Creator";
    protected final static String ID_NAME = "Id";
    protected final static String TYPE_NAME = "Type";
    protected final static String VALIDATION_NAME = "Validation";
    protected final static String VERSION_NAME = "Version";
    protected final static String SOURCE_NAME = "Source";
    
    protected final static String FLAG_NAME = "Flagged";
    protected final static String TAG_NAME = "Tags";
    protected final static String WAVEBAND_NAME = "Waveband";
    
    private final static String MORE = ", ...";  // continuation string

    public ResourceTableFomat(final AnnotationService annService,final VoMonInternal vomon,final CapabilityIconFactory capBuilder) {
        super();
        this.vomon = vomon;
        this.annService = annService;
        this.capBuilder = capBuilder;

        List columnList = new ArrayList();

        columnList.add(new IconColumn(STATUS_NAME) {
            public Icon getValue(Resource res) {
                return vomon.suggestIconFor(res);
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(40);
                tcol.setMaxWidth(40);
            }
            public String getToolTipText(Resource res) {
                return vomon.getTooltipInformationFor(res);
            }
        });

        columnList.add(new StringColumn(LABEL_NAME) {
            final StrBuilder result = new StrBuilder(64);
            public String getValue(Resource res) {
                if (res == null) {
                    return "";
                }
                String title = null;
                int titleLevel = Integer.MAX_VALUE;
                Color highlight = null;
                int highlightLevel = Integer.MAX_VALUE;
                // check for overrides.
                for (Iterator i = annService.getLocalAnnotations(res); i.hasNext(); ) {
                    Annotation a = (Annotation)i.next();
                    String t = StringUtils.trimToNull(a.getAlternativeTitle());
                    // am getting an odd NPE from the next line - think it might be a race condition.
                    // will check for a.getSource() != null too.
                    if (t != null && a.getSource() != null && a.getSource().getSortOrder() <= titleLevel) {
                        title = t;
                        titleLevel = a.getSource().getSortOrder();
                    }
                    if (a instanceof UserAnnotation) {
                        UserAnnotation u = (UserAnnotation)a;
                        if (u.getHighlight() != null && ! (u.getHighlight().equals(Color.BLACK) || u.getHighlight().equals(Color.WHITE)) 
                                && u.getSource().getSortOrder() <= highlightLevel) {
                            highlight = u.getHighlight();
                            highlightLevel = u.getSource().getSortOrder();
                        }
                    }
                }
                
                // work out what we've found, and assemble all bits of data into a single formatted string.
                result.clear();
                if (title != null|| highlight != null) {
                    result.append("<html>");
                }
                if (highlight != null) {
                    result.append("<body color='#");
                    int i = highlight.getRGB();
                    result.append(Integer.toHexString(i).substring(2,8));
                    result.append("'>");
                }
                if (title != null) {
                    result.append("<i>").append(title);
                } else {
                    result.append(StringUtils.replace(res.getTitle(), "\n", " ") );
                }
                return result.toString();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(300);
            }
            public String getToolTipText(Resource res) {
                return res.getTitle();
            }
        });

        columnList.add(new IconColumn(CAPABILITY_NAME) {
            public Icon getValue(Resource res) {
                return capBuilder.buildIcon(res);
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(80);
                tcol.setMaxWidth(100);
                tcol.setResizable(true);
                // using a custom renderer here as want the icon to be left aligned - 
                // otherwise it doesn't display correctly.
                DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
                    {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    protected void setValue(Object value) {
                        setIcon((Icon)value);
                    }
                };                
                tcol.setCellRenderer(r);
            }
            public String getToolTipText(Resource res) {
                return capBuilder.getTooltip(getValue(res));
            }
        });

        columnList.add(new StringColumn(DATE_NAME) {
            public String getValue(Resource res) {
                String date = res.getUpdated();
                if (date == null) {
                    date = res.getCreated();
                }
                return date == null ? "" : date.substring(0,10);
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(90);
                tcol.setMaxWidth(90);
            }
        });

        columnList.add(new StringColumn(SHORT_NAME) {
            public String getValue(Resource res) {
                return res.getShortName();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(150);
            }
        });

        columnList.add(new StringColumn(SUBJECT_NAME) {
            final StrBuilder sbuf = new StrBuilder();
            public String getValue(Resource res) {
                sbuf.clear();
                String[] subjects = res.getContent().getSubject();
                sbuf.appendWithSeparators(subjects,", ");
                return sbuf.toString();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(150);
            }
        });

        columnList.add(new StringColumn(PUBLISHER_NAME) {
            public String getValue(Resource res) {
                return resourceNameToString(res.getCuration().getPublisher());
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(150);
            }
        });

        columnList.add(new StringColumn(CONTACT_NAME) {
            final StrBuilder sbuf = new StrBuilder();
            public String getValue(Resource res) {
                sbuf.clear();
                Contact[] contacts = res.getCuration().getContacts();
                if (contacts.length > 0) {
                    Contact contact = contacts[0];
                    String name = resourceNameToString(contact.getName());
                    if (name != null) {
                        sbuf.append(name)
                            .append(' ');
                    }
                    String mail = contact.getEmail();
                    if (mail != null) {
                        sbuf.append('<')
                            .append(mail)
                            .append('>');
                    }
                    if (contacts.length > 1) {
                        sbuf.append(MORE);
                    }
                }
                return sbuf.toString();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(200);
            }
        });

        columnList.add(new StringColumn(CREATOR_NAME) {
            final StrBuilder sbuf = new StrBuilder();
            public String getValue(Resource res) {
                sbuf.clear();
                Creator[] creators = res.getCuration().getCreators();
                if (creators.length > 0) {
                    sbuf.append(resourceNameToString(creators[0].getName()));
                    if (creators.length > 1) {
                        sbuf.append(MORE);
                    }
                }
                return sbuf.toString();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(200);
            }
        });

        columnList.add(new StringColumn(ID_NAME) {
            public String getValue(Resource res) {
                URI id = res.getId();
                return id == null ? "" : id.toString();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(200);
            }
        });

        columnList.add(new StringColumn(TYPE_NAME) {
            public String getValue(Resource res) {
                return res.getType();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(150);
            }
        });
        
        columnList.add(new StringColumn(SOURCE_NAME) {

            protected String getValue(Resource res) {
                Source source = res.getContent().getSource();
                if (source != null && source.getValue() != null) {
                    return source.getValue();
                } 
                return "";
            }
        });
        columnList.add(new StringColumn(WAVEBAND_NAME) {
            final StrBuilder sbuf = new StrBuilder();
            public String getValue(Resource res) {
                sbuf.clear();
                if (res instanceof HasCoverage) {
                    String[] bands = ((HasCoverage)res).getCoverage().getWavebands();
                    sbuf.appendWithSeparators(bands,", ");
                }
                return sbuf.toString();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(150);
            }
        });
        columnList.add(new StringColumn(VALIDATION_NAME) {
            final StrBuilder sbuf = new StrBuilder();
            public String getValue(Resource res) {
                sbuf.clear();
                Validation[] validations = res.getValidationLevel();
                // find the highest validation level. (typically will be only one.)
                int bestValidation = -1;
                for (int i = 0; i < validations.length; i++) {
                    bestValidation = Math.max(bestValidation,validations[i].getValidationLevel());
                }
                if (bestValidation > 0) {
                    return new String(PrettierResourceFormatter.createValidationRoundel(bestValidation));
                } else {
                    return "";
                }
               
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(50);
                tcol.setMaxWidth(50);
            }
        });

        columnList.add(new StringColumn(VERSION_NAME) {
            public String getValue(Resource res) {
                return res.getCuration().getVersion();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(75);
            }
        });
        
        columnList.add(new IconColumn(FLAG_NAME) {
            final Icon FLAG = IconHelper.loadIcon("flag16.png"); 
            protected Icon getValue(Resource res) {
                UserAnnotation a = annService.getUserAnnotation(res);
                 return a != null && a.isFlagged() ?  FLAG : null;
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(40);
                tcol.setMaxWidth(40);
            }                        
        });
        
        columnList.add(new StringColumn(TAG_NAME) {
            final Set tags = new HashSet();
            final StrBuilder sb = new StrBuilder();
            protected String getValue(Resource res) {
                tags.clear();
                sb.clear();
                for (Iterator i = annService.getLocalAnnotations(res); i.hasNext(); ) {
                    Annotation a = (Annotation)i.next();
                    Set ts = a.getTags();
                    if (ts != null) {
                        tags.addAll(ts);
                    }
                }
                sb.appendWithSeparators(tags,", ");
                return sb.toString();
            }
            public void configureColumn(TableColumn tcol) {
                tcol.setPreferredWidth(150);
            }
        });

        setColumns((ModularColumn[])columnList.toArray(new ModularColumn[columnList.size()]));
    }

    /**
     * Configures a table column model appropriately for describing a TableModel
     * built using this TableFormat.
     *
     * @param  colModel  column model
     */
    public void configureColumnModel(TableColumnModel colModel) {
        int ncol = colModel.getColumnCount();
        for (int icol = 0; icol < ncol; icol++) {
            ((Column) getColumn(icol)).configureColumn(colModel.getColumn(icol));
        }
    }

    /**
     * Returns a tooltip for a given resource in a given column.
     *
     * @param   res  resource (table cell content)
     * @param   icol  index of column in which <code>res</code> appears
     * @return   tooltip string
     */
    public String getToolTipText(Resource res, int icol) {
        return ((Column) getColumn(icol)).getToolTipText(res);
    }

    /**
     * Returns the names of the columns which are visible by default in a resource table.
     *
     * @return   array of initially visible column names
     */
    public String[] getDefaultColumns() {
        return new String[] {
            STATUS_NAME, FLAG_NAME,LABEL_NAME, CAPABILITY_NAME, DATE_NAME,
        };
    }

    private final CapabilityIconFactory capBuilder;
    private final AnnotationService annService;
    private final VoMonInternal vomon;

    /**
     * Converts a ResourceName to a human-readable string.
     *
     * @param   rname  resource name
     * @return   stringified version
     */
    private static String resourceNameToString(ResourceName rname) {
        return rname == null ? ""
                             : rname.getValue();
    }

    /**
     * ModularColumn implementation for use internally to this TableFormat.
     */
    protected static abstract class Column extends ModularColumn {

        /**
         * Constructor.
         *
         * @param  name  column name
         * @param  clazz  most specific known superclass for values in this column
         * @param  comparator  default comparator to use for this column;
         *                     use null for a non-comparable column
         */
        public Column(String name, Class clazz, Comparator comparator) {
            super(name, clazz, comparator);
        }

        /**
         * Performs column-specific configuration on a TableColumn to hold this column,
         * for instance setting preferred width.
         * The default implementation does nothing; subclasses which know how
         * their contents will look are encouraged to override it.
         *
         * @param  tcol  column to configure
         */
        public void configureColumn(TableColumn tcol) {
            //by default does nothing
        }

        /**
         * Returns a tooltip for a resource object appearing in this column.
         * The default implementation returns null; subclasses which have
         * more to say about their contents are encouraged to override it.
         *
         * @param  res  column cell content
         * @return   tooltop describing <code>res</code>
         */
        public String getToolTipText(Resource res) {
            return null;
        }
    }

    /**
     * Column implementation for a column containing strings.
     */
    protected static abstract class StringColumn extends Column {

        /**
         * Constructs a column with a default comparator.
         *
         * @param name  column name
         */
        public StringColumn(String name) {
            this(name, false);
        }

        /**
         * Constructs a column with an optionally case sensitive comparator
         *
         * @param  name  column name
         * @param  caseSensitive    true iff comparisons should be case sensitive
         */
        public StringColumn(String name, boolean caseSensitive) {
            super(name, String.class, caseSensitive ? GlazedLists.comparableComparator() : GlazedLists.caseInsensitiveComparator());
        }

        /**
         * Returns the string value corresponding to a given resource.
         *
         * @param  res  resource
         * @return  string value
         */
        protected abstract String getValue(Resource res);

        public Object getColumnValue(Object obj) {
            return obj instanceof Resource ? getValue((Resource) obj)
                                           : null;
        }
    }

    /**
     * ModularColumn implementation for a column containing icons.
     */
    protected static abstract class IconColumn extends Column {

        /**
         * Constructor.
         *
         * @param   name  column name
         */
        IconColumn(String name) {
            super(name, Icon.class, ICON_COMPARATOR);
        }

        /**
         * Returns the icon value corresponding to a given resource.
         *
         * @param  res  resource
         * @return   icon value
         */
        protected abstract Icon getValue(Resource res);

        public Object getColumnValue(Object obj) {
            return obj instanceof Resource ? getValue((Resource) obj)
                                           : null;
        }

        /** compare 2 icons. */
        private static final Comparator ICON_COMPARATOR = new Comparator() {
            public int compare(Object arg0, Object arg1) {
                Icon a = (Icon)arg0;
                Icon b = (Icon)arg1;
                return map(a) - map(b); 
            }
            private int map(Icon a) {
                if (a == null) {
                    return 0;
                }
                if (a == UIConstants.SERVICE_OK_ICON) {
                    return 5;
                }
                if (a == UIConstants.UNKNOWN_ICON) {
                    return 4;
                }
                if (a == UIConstants.SERVICE_DOWN_ICON) {
                    return 1;
                }
                return a.hashCode(); // works for icons in capabilities list.
            }
        };
    }
}
