package sua.autonomouscar.controller.probes.car;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.car.SteeringHealthMonitor;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class SteeringHealthCheckProbe implements IProbe<ISteering>, ServiceListener {

    private BundleContext context;

    public SteeringHealthCheckProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
        case ServiceEvent.UNREGISTERING:
            this.registerMeasurement(OSGiUtils.getService(context, ISteering.class));

            break;
        default:
            break;
        }
    }

    @Override
    public void registerMeasurement(ISteering component) {
        SteeringHealthMonitor steeringHealthMonitor = OSGiUtils.getService(context, SteeringHealthMonitor.class);

        if (steeringHealthMonitor == null)
        {
            return;
        }

        steeringHealthMonitor.registerHealthCheck(component != null);
    }
}
