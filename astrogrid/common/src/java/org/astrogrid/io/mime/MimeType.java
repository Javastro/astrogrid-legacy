package org.astrogrid.io.mime;

import java.io.File;
    /**
 * represents ngas mime types and provides certain methods for processing them.
 * @author Francis Woolfe (student with ESO sept 2003). I'd appreciate it if you let me
 * know when you test / use this class, or if anything that does becomes operational.
 * Email: francis_woolfe@yahoo.com
 * @TODO - this is really not very OO - should be made better so that it is easier to add another supported mime type.
 * @author pharriso@eso.org 05-Aug-2005 
 */
public class MimeType {
    /**
     * the mime type associated with this particular instance of the class. It is strongly recommended
     * that this should be one of the static strings provided for representing known mime types.
     */
    public String mimeType;
    /**
     * the file extension associated with a mime type. Note that initial implementation only allows one file extension per file type.
     */
    String fileExtension;

    /**
     * This string represents the obvious mime type.
     */
    final static public String application_xcfits="application/x-cfits", image_xfitshdr="image/x-fitshdr",
    application_xhfits="application/x-hfits", ngas_log="ngas/log", ngas_paf="ngas/paf",
    application_xgfits="application/x-gfits", ngas_nglog="ngas/nglog",
    image_xfits="image/x-fits", cal_xtfits="cal/x-tfits", unspecified_mime_type="application/octet-stream", votable="application/x-votable+xml";
    
    /**
     * Used if the mime type is not known.
     */
    public final static String UNKNOWN=unspecified_mime_type;
    

    /**
     * an array of all known valid mime types.
     */
    private static String[] validContentTypes={"application/x-cfits", "image/x-fitshdr", "application/x-hfits", "ngas/log",  "ngas/paf", "application/x-gfits","ngas/nglog", "image/x-fits", "cal/x-tfits"};
    
    /**
     * the number of all known valid mime types.
     */
    private static final int nmbrOfContentTypes = 9;

    /**
     * the list of all know valid file extensions. There is a one to one correspondance between
     * mime types and file extensions, given by fileExtensions[i] <--> validContentTypes[i]
     * for i an element of [0 .. nmbrOfContentTypes).
     */
    public static String[] fileExtensions={"fits.Z", "hdr", "hfits", "log", "paf", "fits.gz", "nglog", "fits", "tfits"};

    /**
     * get the file extension a file with this mime type should have once it has been decompressed. If
     * this mime type is not a compressed mime type, the file extension returned is the file extension
     * associated with this mime type.
     * @param mimeType the mime type
     * @return the file extension 
     */
    public static String getDecompressedFileExtension(String mimeType){
        String fileExtension=null;
        for (int i = 0; i<nmbrOfContentTypes; i++) 
            if (mimeType.equals(validContentTypes[i]))  fileExtension =  fileExtensions[i];
        if (fileExtension==null) return UNKNOWN;
        if (isCompressed(fileExtension)) {
            fileExtension = getDecompressedFileExtensionFromCompressedFileExtension(fileExtension);
        }
        return fileExtension;
    }

    /**
     * Whether this file extension corresponds to a compressed mime type. At present the only compression
     * methods give file extesions .Z and .gz
     * @param fileExtension the file extesion to check
     * @return true iff the file extension corresponds to a compressed mime type.
     */
    static boolean isCompressed(String fileExtension){
        return (fileExtension.endsWith(".Z") || fileExtension.endsWith(".gz") );
    }
    
    /**
     * The file extension that a file will have once it has been decompressed. If a file extension is
     * specified that doesn't correspond to a compressed file, the same file extension is returned.
     * @param fileExtension the file extension
     * @return the decompressed file extension
     */
    public static String getDecompressedFileExtensionFromCompressedFileExtension(String fileExtension){
        String decompressedFileExtension=fileExtension;
        if (fileExtension.endsWith(".Z")) {
            decompressedFileExtension = fileExtension.substring(0, fileExtension.length()-2);
        }
        if (fileExtension.endsWith(".gz"))
            decompressedFileExtension = fileExtension.substring(0, fileExtension.length()-3);       
        return decompressedFileExtension;
    }
    
    /**
     * Strongly recommended to use this constructor with argument that is a valid mime type, stored 
     * as a static string in this class.
     * @param mimeType the mime type
     */
    public MimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    /**
     * Turns an array of strings into a ; separated list.
     * @param ar the array to concatenate
     * @param len the length of the array to concatenate.
     * @return the concatenated array.
     */
    static private String concat(String[] ar, int len){
        String concat="";
        for(int i=0; i<len; i++) concat=concat+ar[i]+(i==len-1 ? "" : "; ");
        return concat;
    }

    /**
     * Returns a ; separated list of all valid content types.
     * @return a ; separated list of all valid content types.
     */
    static public String getContentTypes() {
        return concat(validContentTypes, nmbrOfContentTypes);   
    }
    
    /**
     * Returns a ; separated list of all valid file extensions.
     * @return a ; separated list of all valid file extension.
     */
    static public String getFileExtensions() {
        return concat(fileExtensions, nmbrOfContentTypes);
    }
    
    /**
     * checks whether a given string is a valid content type. 
     * @param content_type the string to check
     * @return whether it is a valid content type.
     */
    static public boolean contentTypeIsValid(String content_type){
        boolean valid=false;
        for (int i=0; i<nmbrOfContentTypes; i++)
            valid= valid || (content_type == validContentTypes[i]);
        return valid;
        
    }
    
    /**
     * Extracts the file extension from a certain file name.
     * @param fileName the file name
     * @return the file extension
     */
    static public String findFileExtension(String fileName){
        File file = new File(fileName);
        String baseName=file.getName();
        //sort validContentTypes and fileExtensions on the length of the extension, longest coming first.
        //this is important since some file extensions are the endings of other file extensions, eg fits & tfits.
        int m, mpos; String tempExt; String tempType;
        for(int i=0; i<nmbrOfContentTypes; i++){
            m=fileExtensions[i].length(); mpos=i;
            for(int j=i; j<nmbrOfContentTypes; j++){
                if (fileExtensions[j].length() > m) {
                    m=fileExtensions[j].length(); mpos=j; 
                }
            }
            tempType = validContentTypes[i]; tempExt=fileExtensions[i];
            validContentTypes[i] = validContentTypes[mpos]; fileExtensions[i]=fileExtensions[mpos];
            validContentTypes[mpos] = tempType; fileExtensions[mpos] = tempExt;
        };
        for(int i=0; i<nmbrOfContentTypes; i++)
            if (baseName.length()>=fileExtensions[i].length() && baseName.substring(baseName.length()-fileExtensions[i].length()).equals(fileExtensions[i])) return fileExtensions[i];
        return null;

    }
    
    /**
     * guesses the content type of a certain file name from its file extension
     * @param fileName the file name
     * @return the mime type
     */
    static public String guessContentType(String fileName){
        String extension = findFileExtension(fileName);
        for (int i=0; i<nmbrOfContentTypes; i++)
            if (extension == fileExtensions[i]) return validContentTypes[i];
        return null;    
    }

    
    /* This is the list of all NGAS mime types and their corresponding file extensions
    <MimeTypes>
      <MimeTypeMap Extension="fits.Z" MimeType="application/x-cfits"/>
      <MimeTypeMap Extension="hdr" MimeType="image/x-fitshdr"/>

      <MimeTypeMap Extension="hfits" MimeType="application/x-hfits"/>
      <MimeTypeMap Extension="log" MimeType="ngas/log"/>
      <MimeTypeMap Extension="paf" MimeType="ngas/paf"/>
      <MimeTypeMap Extension="fits.gz" MimeType="application/x-gfits"/>
      <MimeTypeMap Extension="nglog" MimeType="ngas/nglog"/>
      <MimeTypeMap Extension="fits" MimeType="image/x-fits"/>
      <MimeTypeMap Extension="tfits" MimeType="cal/x-tfits"/>
    </MimeTypes>     
     
     */ 
     
}
