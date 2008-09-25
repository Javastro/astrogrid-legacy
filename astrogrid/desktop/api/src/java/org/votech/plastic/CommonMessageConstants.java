/*
 * 
 */
package org.votech.plastic;

import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * Common constants 
 * @author jdt@roe.ac.uk
 */
public interface CommonMessageConstants {

    String DEFAULT_AUTH_ID = "votech.org";
    String PREFIX = "ivo://"+DEFAULT_AUTH_ID;
    /**
     * Common operations that a Plastic-compatible tool should support.
     * Note that these are still under discussion and liable to change.
     */
    public final static URI ECHO = URI.create(CommonMessageConstants.PREFIX+"/test/echo");
    public final static URI GET_IVORN = URI.create(CommonMessageConstants.PREFIX+"/info/getIvorn");
    public final static URI GET_NAME = URI.create(CommonMessageConstants.PREFIX+"/info/getName");
    public final static URI GET_VERSION= URI.create(CommonMessageConstants.PREFIX+"/info/getVersion");
    public final static URI GET_ICON= URI.create(CommonMessageConstants.PREFIX+"/info/getIconURL");
    public final static URI GET_DESCRIPTION = URI.create(CommonMessageConstants.PREFIX+"/info/getDescription");
    public final static URI VOTABLE_LOAD = URI.create(CommonMessageConstants.PREFIX+"/votable/load");
    public final static URI VOTABLE_LOAD_FROM_URL = URI.create(CommonMessageConstants.PREFIX+"/votable/loadFromURL");
    public final static URI VOTABLE_LOADED_EVENT = URI.create(CommonMessageConstants.PREFIX+"/votable/event/tableLoaded");
    public final static URI VO_SHOW_OBJECTS = URI.create(CommonMessageConstants.PREFIX+"/votable/showObjects");

    public final static URI ENSURE_VISIBLE = URI.create("ivo://votech.org/sky/ensureVisible");
    public final static URI GET_IMAGES = URI.create("ivo://votech.org/sky/getImages");
    public final static URI FITS_LOAD_FROM_URL = URI.create("ivo://votech.org/fits/image/loadFromURL");

    List EMPTY = Collections.EMPTY_LIST;
    String RPCNULL = ""; //TODO think about this....
	
	
    
    public static final String PLASTIC_HOME_PAGE = "http://plastic.sourceforge.net";
}
