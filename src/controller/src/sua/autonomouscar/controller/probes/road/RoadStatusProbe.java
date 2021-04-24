package sua.autonomouscar.controller.probes.road;

import sua.autonomouscar.interfaces.ERoadStatus;

import org.osgi.framework.BundleContext;
import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.devices.interfaces.IRoadSensor;

/**
 * A probe that measures the {@link ERoadStatus}.
 */
public class RoadStatusProbe extends RoadContextProbe implements IProbe<ERoadStatus> {

	protected RoadStatusProbe(BundleContext context) {
		super(context);
	}

	@Override
	public ERoadStatus getMeasurement() {
		IRoadSensor roadSensor = getRoadSensor();

		if (roadSensor == null)
		{
			return null;
		}

		return roadSensor.getRoadStatus();
	}
}
