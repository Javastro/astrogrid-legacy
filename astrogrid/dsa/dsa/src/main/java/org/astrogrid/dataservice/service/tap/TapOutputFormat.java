package org.astrogrid.dataservice.service.tap;

/**
 * The MIME type for the output format of a TAP query. The type describes
 * either a table of results or an error document in TAP's peculiar form.
 * <p>
 * The TAP standard defines a set of names for formats which may appear as
 * values of the FORMAT parameter in TAP queries. These names map to prescribed
 * MIME-types; in some cases, there is more than one name for each MIME type.
 * <p>
 * This class parses the names in its single-argument constructor stored the
 * equivalent MIME-type, and gives back the MIME-type in the {@link #toString}
 * method. The constructor throws exceptions for invalid format-names, so any
 * instance of this class is sure to contain a valid MIME-type.
 *
 * @author Guy Rixon
 */
public class TapOutputFormat {

  /**
   * The MIME type for the output format.
   */
  private final String mimeType;


  /**
   * Constructs an output format from a given format-name.
   *
   * @param name The format name (null => VOTable MIME-type).
   * @throws TapException If the name is not valid.
   */
  public TapOutputFormat(String name) throws TapException {
    if (name == null) {
      mimeType = "application/x-votable+xml";
    }
    else {
      String lcName = name.trim().toLowerCase();
      if ("application/x-votable+xml".equals(lcName)) {
        mimeType = "application/x-votable+xml";
      }
      else if ("text/xml".equals(lcName)) {
        mimeType = "text/xml";
      }
      else if ("votable".equals(lcName)) {
        mimeType = "application/x-votable+xml";
      }
      else if ("text/csv".equals(lcName)) {
        mimeType = "text/csv";
      }
      else if ("csv".equals(lcName)) {
        mimeType = "text/csv";
      }
      else if ("text/tab-separated-values".equals(lcName)) {
        mimeType = "text/tab-separated-values";
      }
      else if ("tsv".equals(lcName)) {
        mimeType = "text/tab-separated-values";
      }
      else if ("text/html".equals(lcName)) {
        mimeType = "text/html";
      }
      else if ("html".equals(lcName)) {
        mimeType = "text/html";
      }
      else {
        throw new TapException(name + " is not a supported format");
      }
    }
  }

  /**
   * Reveals the MIME type.
   *
   * @return The MIME type.
   */
  @Override
  public String toString() {
    return mimeType;
  }
}
