package sua.autonomouscar.controller.probes.car;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.car.DistanceSensorsHealthMonitor;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class DistanceSensorHealthCheckProbe implements IProbe<IDistanceSensor>, ServiceListener {

    private BundleContext context;
    private DistanceSensorPositon position;

    public DistanceSensorHealthCheckProbe(BundleContext context, DistanceSensorPositon position) {
        this.context = context;
        this.position = position;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
            case ServiceEvent.REGISTERED:
            case ServiceEvent.MODIFIED:
            case ServiceEvent.UNREGISTERING:
                // Get any distance sensor available in the given position. If none is avaiable, it will return null.
                this.registerMeasurement(AutonomousVehicleContextUtils.findDistanceSensor(this.context, this.position));

                break;
            default:
                break;
        }
    }

    @Override
    public void registerMeasurement(IDistanceSensor component) {
        DistanceSensorsHealthMonitor distanceSensorsHealthMonitor = OSGiUtils.getService(context, DistanceSensorsHealthMonitor.class, String.format("(%s=%s)", "position", this.position));

        if (distanceSensorsHealthMonitor == null)
        {
            return;
        }

        distanceSensorsHealthMonitor.registerHealthCheck(component != null);
    }
}
