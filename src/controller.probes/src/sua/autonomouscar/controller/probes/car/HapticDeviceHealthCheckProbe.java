package sua.autonomouscar.controller.probes.car;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.car.HapticDeviceHealthMonitor;
import sua.autonomouscar.controller.monitors.car.SteeringHealthMonitor;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interaction.interfaces.IInteractionMechanism;

public class HapticDeviceHealthCheckProbe implements IProbe<IInteractionMechanism>, ServiceListener {

    private BundleContext context;

    public HapticDeviceHealthCheckProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
        case ServiceEvent.UNREGISTERING:
            this.registerMeasurement(OSGiUtils.getService(context, IInteractionMechanism.class));

            break;
        default:
            break;
        }
    }

    @Override
    public void registerMeasurement(IInteractionMechanism component) {
        HapticDeviceHealthMonitor hapticDeviceHealthMonitor = OSGiUtils.getService(context, HapticDeviceHealthMonitor.class);

        if (hapticDeviceHealthMonitor == null)
        {
            return;
        }

        hapticDeviceHealthMonitor.registerHealthCheck(component != null);
    }
}
