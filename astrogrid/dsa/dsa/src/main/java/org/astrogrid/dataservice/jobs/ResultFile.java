package org.astrogrid.dataservice.jobs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.slinger.targets.TargetIdentifier;

/**
 * A file of results from a query, cached on the server.
 * <p>
 * The name of the file is derived from the job identifier set at construction,
 * and the file is located in the directory named by the configuration key
 * {@code datacenter.cache.directory}.
 * <p>
 * Instantiating this class does not create the file. Instead, use the
 * normal methods of the superclass to create or delete the actual file.
 * <p>
 * The class implements {@link org.astrogrid.slinger.targets.TargetIdentifier}
 * in order that the file may receive the results of queries; an instance of
 * {@code ResultFile} can be given as a target to an instance of {@code Query}.
 * To satisfy this interface, the MIME type of the content may be recorded.
 * MIME type defaults to text/plain on construction.
 *
 * @author Guy Rixon
 */
public class ResultFile extends File implements TargetIdentifier {

  /**
   * The MIME type of the content.
   */
  private String mimeType = "text/plain";

  /**
   * Constructs the name of a results file for a named job.
   * The job identifier and the configured cache-directory determine the
   * name of the actual file on disc. Construction does not create the file.
   *
   * @param jobId The job identifier.
   */
  public ResultFile(String jobId) {
    super(SimpleConfig.getProperty("datacenter.cache.directory"), jobId);
  }

  /**
   * Supplies a stream for writing to the file.
   * @return
   * @throws IOException If the file does not exist.
   * @throws IOException if the file is not writeable.
   */
  public Writer openWriter() throws IOException {
    return new FileWriter(this);
  }

  /**
   * Supplies a stream for writing to the file.
   * @return
   * @throws IOException If the file does not exist.
   * @throws IOException if the file is not writeable.
   */
  public OutputStream openOutputStream() throws IOException {
    return new FileOutputStream(this);
  }

  /**
   * Sets the MIME type of the content.
   *
   * @param mimeType The type.
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Reveals the MIME type of the content.
   *
   * @return The type (defaults to text/plain if not previously set).
   */
  public String getMimeType() {
    return mimeType;
  }

}
