/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.URLFileName;
import org.apache.commons.vfs.provider.URLFileNameParser;
import org.apache.commons.vfs.provider.UriParser;
import org.apache.commons.vfs.provider.VfsComponentContext;
import org.apache.commons.vfs.provider.local.GenericFileNameParser;
import org.apache.commons.vfs.provider.url.UrlFileNameParser;
import org.apache.commons.vfs.provider.url.UrlFileProvider;
import org.apache.commons.vfs.provider.url.UrlFileSystem;

/** extended  url file provider that respects the ?query part of a url. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 9, 200712:03:09 PM
 */
public class QueryRespectingUrlFileProvider extends UrlFileProvider {

    /**
     * 
     */
    public QueryRespectingUrlFileProvider() {
        super();
        setFileNameParser(new QueryRespectingFileNameParser());
    }
    
    public synchronized FileObject findFile(FileObject baseFile, String uri,
            FileSystemOptions fileSystemOptions) throws FileSystemException {
        {
            try
            {
                final URL url = new URL(uri);

                URL rootUrl = new URL(url, "/");
                final String key = this.getClass().getName() + rootUrl.toString();
                FileSystem fs = findFileSystem(key, fileSystemOptions);
                if (fs == null)
                {
                    String extForm = rootUrl.toExternalForm();
                    final FileName rootName =
                        getContext().parseURI(extForm);
                    // final FileName rootName =
                    //    new BasicFileName(rootUrl, FileName.ROOT_PATH);
                    fs = new QueryRespectingUrlFileSystem(rootName, fileSystemOptions);
                    addFileSystem(key, fs);
                }
                if (url.getQuery() == null) {
                    return fs.resolveFile(url.getPath());
                } else {
                    return fs.resolveFile(url.getPath() +"?" + url.getQuery());
                }
            }
            catch (final MalformedURLException e)
            {
                throw new FileSystemException("vfs.provider.url/badly-formed-uri.error", uri, e);
            }
    }
}
    
    /** subclassed file system to access protected constructor */
    public static class QueryRespectingUrlFileSystem extends UrlFileSystem {

        /**
         * @param rootName
         * @param fileSystemOptions
         */
        public QueryRespectingUrlFileSystem(FileName rootName,
                FileSystemOptions fileSystemOptions) {
            super(rootName, fileSystemOptions);
        }
    }
    
    /** subclassed to  get at the nested parser */
    public static class QueryRespectingFileNameParser extends UrlFileNameParser {
        private URLFileNameParser url = new QueryRespectingURLFileNameParser(80);
        private GenericFileNameParser generic = new GenericFileNameParser();

        public FileName parseUri(VfsComponentContext context, FileName base,
                String filename) throws FileSystemException {
            if (isUrlBased(base, filename))
            {
                return url.parseUri(context, base, filename);
            }

            return generic.parseUri(context, base, filename);
        }
    }
    /** subclassed to respect query */
    public static class QueryRespectingURLFileNameParser extends URLFileNameParser {

        /**
         * @param defaultPort
         */
        public QueryRespectingURLFileNameParser(int defaultPort) {
            super(defaultPort);
        }
        public FileName parseUri(final VfsComponentContext context, FileName base, final String filename) throws FileSystemException
        {
            // FTP URI are generic URI (as per RFC 2396)
            final StringBuffer name = new StringBuffer();

            // Extract the scheme and authority parts
            final Authority auth = extractToPath(filename, name);

            // Extract the queryString
            String queryString = UriParser.extractQueryString(name);

            // Decode and normalise the file name
            UriParser.canonicalizePath(name, 0, name.length(), this);
            UriParser.fixSeparators(name);
            FileType fileType = UriParser.normalisePath(name);
            final String path = name.toString();

            return new QueryRespectingURLFileName(
                auth.scheme,
                auth.hostName,
                auth.port,
                getDefaultPort(),
                auth.userName,
                auth.password,
                path,
                fileType,
                queryString);
        }
    }
    
    /** phew! finally got here. all this just to override equals */
    public static class QueryRespectingURLFileName extends URLFileName {

        /**
         * @param scheme
         * @param hostName
         * @param port
         * @param defaultPort
         * @param userName
         * @param password
         * @param path
         * @param type
         * @param queryString
         */
        public QueryRespectingURLFileName(String scheme, String hostName,
                int port, int defaultPort, String userName, String password,
                String path, FileType type, String queryString) {
            super(scheme, hostName, port, defaultPort, userName, password, path, type,
                    queryString);
        }
        public boolean equals(Object obj) {
            if (!(obj instanceof QueryRespectingURLFileName))
            {
                return false;
            }
            final QueryRespectingURLFileName name = (QueryRespectingURLFileName) obj;
            return (getRootURI().equals(name.getRootURI()) 
                    && getPath().equals(name.getPath())
                    && (getQueryString() == null ? true
                            : getQueryString().equals(name.getQueryString()))
                    );
            
        }
    }
    
    
}