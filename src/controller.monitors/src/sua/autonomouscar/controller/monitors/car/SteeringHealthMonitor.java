package sua.autonomouscar.controller.monitors.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IHealthMonitor;
import sua.autonomouscar.controller.properties.car.SteeringHealthStatus;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class SteeringHealthMonitor implements IHealthMonitor
{
    private BundleContext context;

    public SteeringHealthMonitor(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerHealthCheck(boolean isSteeringAvailable) {
        SteeringHealthStatus steeringHealthStatus = OSGiUtils.getService(context, SteeringHealthStatus.class);

        // Only update it if the value has changed.
        if (steeringHealthStatus != null && steeringHealthStatus.isAvailable() != isSteeringAvailable) {
            System.out.println("[ Steering Health Monitor ] -  Updating the HealthStatus to " + isSteeringAvailable);

            steeringHealthStatus.setIsAvailable(isSteeringAvailable);
        }
    }
}
