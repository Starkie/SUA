package sua.autonomouscar.properties;

import java.util.Dictionary;
import java.util.Hashtable;

import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * The adaption property that represents the road context. Includes the values of the status and type.
 */
public class RoadContext {
	private static final String STATUS = "status";

	private static final String TYPE = "type";

	private Dictionary<String, Object> properties;

	public RoadContext(ERoadStatus status, ERoadType type)
	{
		this.properties = new Hashtable<>();

		this.setStatus(status);
		this.setType(type);
	}

	public ERoadType getType() {
		return (ERoadType)properties.get(TYPE);
	}

	public ERoadStatus getStatus() {
		return (ERoadStatus)properties.get(STATUS);
	}

	public void setType(ERoadType type) {
		if (type!= null)
		{
			this.properties.put(TYPE, type);
		}
	}

	public void setStatus(ERoadStatus status) {
		if (status != null)
		{
			this.properties.put(STATUS, status);
		}
	}

	public Dictionary<String, Object> getProperties() {
		return properties;
	}
}
