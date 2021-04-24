package sua.autonomouscar.properties;

import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * The adaption property that represents the road context. Includes the values of the status and type.
 */
public class RoadContext {
	private ERoadStatus status;
	private ERoadType type;

	public RoadContext(ERoadStatus status, ERoadType type)
	{
		this.status = status;
		this.type = type;
	}

	public ERoadType getType() {
		return type;
	}

	public ERoadStatus getStatus() {
		return status;
	}

	public void setType(ERoadType type) {
		this.type = type;
	}

	public void setStatus(ERoadStatus status) {
		this.status = status;
	}
}
