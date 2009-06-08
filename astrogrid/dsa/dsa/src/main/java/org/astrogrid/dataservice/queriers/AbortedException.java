package astrogrid.dataservice.queriers;

import org.astrogrid.dataservice.DatacenterException;

/**
 * An exception indicating that a job was aborted. These exceptions record no
 * cause and have a fixded message.
 *
 * @author Guy Rixon
 */
public class AbortedException extends DatacenterException {

  public AbortedException() {
    super("Job was aborted.");
  }
}
