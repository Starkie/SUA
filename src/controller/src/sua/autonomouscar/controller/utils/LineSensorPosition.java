package sua.autonomouscar.controller.utils;

import sua.autonomouscar.devices.interfaces.ILineSensor;

/**
 * Represents the possible positions of a {@link ILineSensor} in the vehicle.
 */
public enum LineSensorPosition {
	LEFT("LeftLineSensor"),
	RIGHT("RightLineSensor");
	
	// The identifier of the line sensor in that position. 
	private final String lineSensorId;
	
	private LineSensorPosition(String sensorId) {
		this.lineSensorId = sensorId;
	}

	public String getSensorId() {
		return this.lineSensorId;
	}
}
