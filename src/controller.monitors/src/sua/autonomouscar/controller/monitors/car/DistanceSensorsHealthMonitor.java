package sua.autonomouscar.controller.monitors.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IHealthMonitor;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class DistanceSensorsHealthMonitor implements IHealthMonitor
{
    private BundleContext context;
    private DistanceSensorPositon position;

    public DistanceSensorsHealthMonitor(BundleContext context, DistanceSensorPositon position) {
        this.context = context;
        this.position = position;
    }

    @Override
    public void registerHealthCheck(boolean isDistanceSensorAvailable) {
        DistanceSensorHealthStatus distanceSensorsHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", this.position));

        // Only update it if the value has changed.
        if (distanceSensorsHealthStatus != null && distanceSensorsHealthStatus.isAvailable() != isDistanceSensorAvailable) {
            System.out.println("[ Distance Sensor Health Monitor ] -  Updating the HealthStatus of the " + this.position + " sensor to " + isDistanceSensorAvailable);

            distanceSensorsHealthStatus.setIsAvailable(isDistanceSensorAvailable);
        }
    }
}
