/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.net.URI;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.TapCapability;
import org.astrogrid.acr.ivoa.resource.TapService;
import org.astrogrid.acr.ivoa.resource.Validation;

/** Adapter class that synthesizes a cea application
 * from a tap service.
 * 
 * most methods delegate to tap object.
 *
 * Only used internally in taskrunner.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 22, 200812:14:53 AM
 */
public class TapCeaApplication  implements CeaApplication, TapService{
    /**
     * 
     */
    private static final URI[] APP_CAPS = new URI[] {TapCapability.CAPABILITY_ID};
    
    private static final InterfaceBean[] IFACES = new InterfaceBean[] {
        new InterfaceBean("Basic","Perform an ADQL Query"
                ,new ParameterReferenceBean[] {// inputs
                      new ParameterReferenceBean("ADQL",1,1)
              }
                  , new ParameterReferenceBean[] {}
              )
        ,new InterfaceBean("Full","Perform an ADQL Query"
          ,new ParameterReferenceBean[] {// inputs
                new ParameterReferenceBean("ADQL",1,1)
                ,new ParameterReferenceBean("FORMAT",1,1)
        }
            , new ParameterReferenceBean[] { // outputs
                new ParameterReferenceBean("DEST",1,1)
        }
        )
    };
    private static final ParameterBean[] PARAMS = new ParameterBean[] {
      new ParameterBean() {{
          setId("ADQL");
          setType("ADQL");
          setName("Query");
          setDescription("A Query");
          
      }}
      ,new ParameterBean() {{
          setId("DEST");
          setType("amyURI");
          setName("Destination");
          setDescription("the URI for the location to " + 
          		"which the table of results is written");
      }}
      ,new ParameterBean() {{
          setId("FORMAT");
          setType("text");
          setName("Result Format");
          setDescription("The MIME type in which the table of " + 
          "results is to be encoded");
          setOptions(new String[] {
                  "application/x-votable+xml;tabledata"
                  ,"application/x-votable+xml;fits"
                  ,"application/x-votable+xml;binary"
          });
          
      }}      
    };
    private final TapService ts;
    private final Content newContent = new Content();

    /**
     * @param ts
     */
    public TapCeaApplication(final TapService ts) {
        super();
        this.ts = ts;

        // copy content.. - so that it's got a relationship back to the TapService, which contains the table metadata
        final Content content = ts.getContent();
        newContent.setContentLevel(content.getContentLevel());
        newContent.setDescription(content.getDescription());
        newContent.setReferenceURI(content.getReferenceURI());
        newContent.setSource(content.getSource());
        newContent.setSubject(content.getSubject());
        newContent.setType(content.getType());
        final Relationship[] relationships = content.getRelationships();
        final Relationship newRel = new Relationship();
        newRel.setRelationshipType("related-to");
        newRel.setRelatedResources(new ResourceName[] {
                new ResourceName() {{
                    setId(ts.getId());
                    setValue(ts.getId().toString()); // work around bug in how we're currently doing this.
                }}
        });
        if (relationships == null || relationships.length == 0) {
            newContent.setRelationships(new Relationship[] {newRel});
        } else {
            final Relationship[] newRels = new Relationship[relationships.length +1];
            newRels[0] = newRel;
            System.arraycopy(relationships,0,newRels,1,relationships.length);
            newContent.setRelationships(newRels);
        }
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.TapService#findTapCapability()
     */
    public TapCapability findTapCapability() {
        return this.ts.findTapCapability();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Service#getCapabilities()
     */
    public Capability[] getCapabilities() {
        return this.ts.getCapabilities();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getContent()
     */
    public Content getContent() {

        return newContent;
       
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getCreated()
     */
    public String getCreated() {
        return this.ts.getCreated();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getCuration()
     */
    public Curation getCuration() {
        return this.ts.getCuration();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getId()
     */
    public URI getId() {
        return this.ts.getId();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Service#getRights()
     */
    public String[] getRights() {
        return this.ts.getRights();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getShortName()
     */
    public String getShortName() {
        return this.ts.getShortName();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getStatus()
     */
    public String getStatus() {
        return this.ts.getStatus();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getTitle()
     */
    public String getTitle() {
        return this.ts.getTitle();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getType()
     */
    public String getType() {
        return this.ts.getType();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getUpdated()
     */
    public String getUpdated() {
        return this.ts.getUpdated();
    }

    /**
     * @return
     * @see org.astrogrid.acr.ivoa.resource.Resource#getValidationLevel()
     */
    public Validation[] getValidationLevel() {
        return this.ts.getValidationLevel();
    }

 // cea application-specific parameters.
    public String getApplicationKind() {
        return "Query";
    }

    public InterfaceBean[] getInterfaces() {
        return IFACES;
    }

    public ParameterBean[] getParameters() {
        return PARAMS;
    }

    public URI[] getApplicationCapabilities() {
        // dunno if it'll be used. but fill it in.
        return APP_CAPS;
    }
    
}
