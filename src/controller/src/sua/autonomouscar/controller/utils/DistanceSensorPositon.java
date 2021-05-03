package sua.autonomouscar.controller.utils;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;

/**
 * Represents the possible positions of a {@link IDistanceSensor} in the vehicle.
 */
public enum DistanceSensorPositon {
	FRONT("FrontDistanceSensor", "LIDAR-FrontDistanceSensor"),
	REAR("RearDistanceSensor", "LIDAR-RearDistanceSensor"),
	LEFT("LeftDistanceSensor", "LIDAR-LeftDistanceSensor"),
	RIGHT("RightDistanceSensor", "LIDAR-RightDistanceSensor");
	
	// The identifier of the normal distance sensor in that position. 
	private final String normalSensorId;
	
	// The identifier of the LIDAR distance sensor in that position.
	private final String lidarSensorId;
	
	private DistanceSensorPositon(String normalSensorId, String lidarSensorId) {
		this.normalSensorId = normalSensorId;
		this.lidarSensorId = lidarSensorId;
	}

	public String getNormalSensorId() {
		return normalSensorId;
	}

	public String getLidarSensorId() {
		return lidarSensorId;
	} 
}
