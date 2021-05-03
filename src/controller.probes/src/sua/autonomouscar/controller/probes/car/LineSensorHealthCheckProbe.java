package sua.autonomouscar.controller.probes.car;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.car.LineSensorsHealthMonitor;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class LineSensorHealthCheckProbe implements IProbe<ILineSensor>, ServiceListener {

    private BundleContext context;
    private LineSensorPosition position;

    public LineSensorHealthCheckProbe(BundleContext context, LineSensorPosition position) {
        this.context = context;
        this.position = position;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
            case ServiceEvent.REGISTERED:
            case ServiceEvent.MODIFIED:
            case ServiceEvent.UNREGISTERING:
                this.registerMeasurement(AutonomousVehicleContextUtils.findLineSensor(this.context, this.position));

                break;
            default:
                break;
        }
    }

    @Override
    public void registerMeasurement(ILineSensor component) {
        LineSensorsHealthMonitor lineSensorsHealthMonitor = OSGiUtils.getService(context, LineSensorsHealthMonitor.class, String.format("(%s=%s)", "position", this.position));

        if (lineSensorsHealthMonitor == null)
        {
            return;
        }

        lineSensorsHealthMonitor.registerHealthCheck(component != null);
    }
}
