package org.astrogrid.desktop.modules.ui.scope;

import java.util.Date;

import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.desktop.modules.ui.comp.DoubleDimension;

/** represents a search position.
 * @TEST comparisons. */
public class PositionHistoryItem implements Comparable {
	private DoubleDimension radius;
	private SesamePositionBean position;
	private Date startTime;
	private Date endTime;

	public int compareTo(final Object o) {
        final PositionHistoryItem other = (PositionHistoryItem) o;
        if (position != null) {
            if (other.position == null) {
                return 1;
            }
            if (position.getRa() != other.position.getRa()) {
                return position.getRa() < other.position.getRa() ? -1 : 1;
            }
            if (position.getDec() != other.position.getDec()) {
                return position.getDec() < other.position.getDec() ? -1 : 1;
            }                
        } else if (other.position != null) {
            return -1;
        }
        
        if (radius != null) {
            if (other.radius == null) {
                return 1;
            }
            if (radius.getHeight() != other.radius.getHeight()) {
                return radius.getHeight() < other.radius.getHeight() ? -1 : 1;
            }
            if (radius.getWidth() != other.radius.getWidth()) {
                return radius.getWidth() < other.radius.getWidth() ? -1 : 1;
            }                
            
        } else if (other.radius != null) {
            return -1;
        }
        
        if (startTime != null) {
            if (other.startTime == null) {
                return 1;
            }
            final int i = startTime.compareTo(other.startTime);
            if (i != 0) {
                return i;
            }
        } else if (other.startTime != null) {
            return -1;
        }
               
        if (endTime != null) {
            if (other.endTime == null) {
                return 1;
            }
            final int i = endTime.compareTo(other.endTime);
            if (i != 0) {
                return i;
            }
        } else if (other.endTime != null) {
            return -1;
        }            
        
        return 0; // identical            
    }
        
       

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PositionHistoryItem other = (PositionHistoryItem) obj;
        if (this.endTime == null) {
            if (other.endTime != null) {
                return false;
            }
        } else if (!this.endTime.equals(other.endTime)) {
            return false;
        }
        if (this.position == null) {
            if (other.position != null) {
                return false;
            }
        } else if (!this.position.equals(other.position)) {
            return false;
        }
        if (this.radius == null) {
            if (other.radius != null) {
                return false;
            }
        } else if (!this.radius.equals(other.radius)) {
            return false;
        }
        if (this.startTime == null) {
            if (other.startTime != null) {
                return false;
            }
        } else if (!this.startTime.equals(other.startTime)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.endTime == null) ? 0 : this.endTime.hashCode());
        result = prime * result
                + ((this.position == null) ? 0 : this.position.hashCode());
        result = prime * result
                + ((this.radius == null) ? 0 : this.radius.hashCode());
        result = prime
                * result
                + ((this.startTime == null) ? 0 : this.startTime.hashCode());
        return result;
    }

    public DoubleDimension getRadius() {
		return this.radius;
	}

	public void setRadius(final DoubleDimension radius) {
		this.radius = radius;
	}

	public SesamePositionBean getPosition() {
		return this.position;
	}

	public void setPosition(final SesamePositionBean position) {
		this.position = position;
	}
	
    public final Date getStartTime() {
        return this.startTime;
    }

    public final void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    public final Date getEndTime() {
        return this.endTime;
    }

    public final void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }
	
}