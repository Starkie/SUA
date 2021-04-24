package sua.autonomouscar.controller.probes.road;

import org.osgi.framework.BundleContext;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.IIdentifiable;

public abstract class RoadContextProbe {
	private BundleContext context;

	private String roadSensorId;

	/**
	 * Creates a new instance of the {@link RoadStatusProbe} class.
	 * @param context The context the probe is running in.
	 */
	protected RoadContextProbe(BundleContext context) {
		this.context = context;
	}

	public void setRoadSensor(String sensorId) {
		this.roadSensorId = sensorId;
	}

	protected IRoadSensor getRoadSensor() {
		return OSGiUtils.getService(context, IRoadSensor.class, String.format("(%s=%s)", IIdentifiable.ID, this.roadSensorId));
	}
}
