package sua.autonomouscar.controller.probes.road;

import sua.autonomouscar.interfaces.ERoadType;

import org.osgi.framework.BundleContext;
import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.devices.interfaces.IRoadSensor;

/**
 * A probe that measures the {@link ERoadType}.
 */
public class RoadTypeProbe extends RoadContextProbe implements IProbe<ERoadType> {

	public RoadTypeProbe(BundleContext context) {
		super(context);
	}

	@Override
	public ERoadType getMeasurement() {
		IRoadSensor roadSensor = getRoadSensor();

		if (roadSensor == null)
		{
			return null;
		}

		return roadSensor.getRoadType();
	}
}
