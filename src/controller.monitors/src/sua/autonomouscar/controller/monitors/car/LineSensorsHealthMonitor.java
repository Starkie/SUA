package sua.autonomouscar.controller.monitors.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IHealthMonitor;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class LineSensorsHealthMonitor implements IHealthMonitor
{
    private BundleContext context;
    private LineSensorPosition position;

    public LineSensorsHealthMonitor(BundleContext context, LineSensorPosition position) {
        this.context = context;
        this.position = position;
    }

    @Override
    public void registerHealthCheck(boolean isLineSensorAvailable) {
        LineSensorsHealthStatus lineSensorsHealthStatus = OSGiUtils.getService(context, LineSensorsHealthStatus.class, String.format("(%s=%s)", "position", this.position));
        
        // Only update it if the value has changed.
        if (lineSensorsHealthStatus != null && lineSensorsHealthStatus.isAvailable() != isLineSensorAvailable) {
            System.out.println("[ Line Sensor Health Monitor ] -  Updating the HealthStatus of the " + this.position + " sensor to " + isLineSensorAvailable);

            lineSensorsHealthStatus.setIsAvailable(isLineSensorAvailable);
        }
    }
}
