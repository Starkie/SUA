package sua.autonomouscar.controller.monitors.road;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IMonitor;
import sua.autonomouscar.controller.probes.road.RoadStatusProbe;
import sua.autonomouscar.controller.probes.road.RoadTypeProbe;
import sua.autonomouscar.properties.RoadContext;

public class RoadContextMonitor implements IMonitor {
	private BundleContext context;
	private RoadStatusProbe roadStatusProbe;
	private RoadTypeProbe roadTypeProbe;
	private RoadContext roadContext;

	public RoadContextMonitor (BundleContext context, RoadStatusProbe roadStatusProbe, RoadTypeProbe roadTypeProbe)
	{
		this.context = context;
		this.roadStatusProbe = roadStatusProbe;
		this.roadTypeProbe = roadTypeProbe;
		
		this.roadContext = new RoadContext(this.roadStatusProbe.getMeasurement(), this.roadTypeProbe.getMeasurement());
		
		// Register the load Road Context class.
		this.context.registerService(RoadContext.class, roadContext, null);
	}

	@Override
	public void monitorize() {
		this.roadContext.setStatus(this.roadStatusProbe.getMeasurement());
		this.roadContext.setType(this.roadTypeProbe.getMeasurement());
	}
}
