package sua.autonomouscar.controller.probes.car;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.car.HumanSensorsHealthMonitor;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class HumanSensorsHealthCheckProbe implements IProbe<IHumanSensors>, ServiceListener {

    private BundleContext context;

    public HumanSensorsHealthCheckProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
            case ServiceEvent.REGISTERED:
            case ServiceEvent.MODIFIED:
            case ServiceEvent.UNREGISTERING:
                this.registerMeasurement(OSGiUtils.getService(context, IHumanSensors.class));

                break;
            default:
                break;
        }
    }

    @Override
    public void registerMeasurement(IHumanSensors component) {
        HumanSensorsHealthMonitor humanSensorsHealthMonitor = OSGiUtils.getService(context, HumanSensorsHealthMonitor.class);

        if (humanSensorsHealthMonitor == null)
        {
            return;
        }

        humanSensorsHealthMonitor.registerHealthCheck(component != null);
    }
}
