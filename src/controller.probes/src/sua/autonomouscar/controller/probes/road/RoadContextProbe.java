package sua.autonomouscar.controller.probes.road;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.IRoadContextMonitor;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.ERoadStatus;

/**
 * A probe that measures the {@link ERoadStatus}.
 */
public class RoadContextProbe implements IProbe<IRoadSensor>, ServiceListener {

    private BundleContext context;

    public RoadContextProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerMeasurement(IRoadSensor sensor) {
        IRoadContextMonitor monitor = OSGiUtils.getService(context, IRoadContextMonitor.class);

        if (monitor == null) {
            return;
        }

        monitor.registerStatusChange(sensor.getRoadStatus());
        monitor.registerTypeChange(sensor.getRoadType());
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
            this.registerMeasurement(OSGiUtils.getService(context, IRoadSensor.class));

            break;
        default:
            break;
        }
    }
}
