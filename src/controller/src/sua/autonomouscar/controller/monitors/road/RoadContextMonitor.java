package sua.autonomouscar.controller.monitors.road;

import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.interfaces.IMonitor;
import sua.autonomouscar.controller.probes.road.RoadStatusProbe;
import sua.autonomouscar.controller.probes.road.RoadTypeProbe;
import sua.autonomouscar.properties.RoadContext;

public class RoadContextMonitor implements IMonitor {
	private BundleContext context;
	private RoadStatusProbe roadStatusProbe;
	private RoadTypeProbe roadTypeProbe;
	private RoadContext roadContext;

	// Registration of the road context adaptation property. Will be used to notify the listeners on a value change.
	private ServiceRegistration<RoadContext> roadContextServiceReference;

	public RoadContextMonitor (BundleContext context, RoadStatusProbe roadStatusProbe, RoadTypeProbe roadTypeProbe)
	{
		this.context = context;
		this.roadStatusProbe = roadStatusProbe;
		this.roadTypeProbe = roadTypeProbe;

		this.roadContext = new RoadContext(this.roadStatusProbe.getMeasurement(), this.roadTypeProbe.getMeasurement());

		// Register the load Road Context class.
		this.roadContextServiceReference = this.context.registerService(RoadContext.class, roadContext, roadContext.getProperties());
	}

	@Override
	public void monitorize() {
		this.roadContext.setStatus(this.roadStatusProbe.getMeasurement());
		this.roadContext.setType(this.roadTypeProbe.getMeasurement());

		this.updateProperties(this.roadContext.getProperties());
	}

	/**
	 * Updates the properties of the road context. Will notify all the listeners that are watching this property.
	 * @param properties The properties collection to update.
	 */
	private void updateProperties(Dictionary<String, Object> properties) {
		if (this.roadContextServiceReference != null)
		{
			this.roadContextServiceReference.setProperties(properties);
		}
	}
}
