package sua.autonomouscar.controller.monitors.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IHealthMonitor;
import sua.autonomouscar.controller.properties.car.HumanSensorsHealthStatus;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class HumanSensorsHealthMonitor implements IHealthMonitor
{
    private BundleContext context;

    public HumanSensorsHealthMonitor(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerHealthCheck(boolean areHumanSensorsAvailable) {
        HumanSensorsHealthStatus humanSensorsHealthStatus = OSGiUtils.getService(context, HumanSensorsHealthStatus.class);

        // Only update it if the value has changed.
        if (humanSensorsHealthStatus != null && humanSensorsHealthStatus.isAvailable() != areHumanSensorsAvailable) {
            System.out.println("[ Human Sensors Health Monitor ] -  Updating the HealthStatus to " + areHumanSensorsAvailable);

            humanSensorsHealthStatus.setIsAvailable(areHumanSensorsAvailable);
        }
    }
}
