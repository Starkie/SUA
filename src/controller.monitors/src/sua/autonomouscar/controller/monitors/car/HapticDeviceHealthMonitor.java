package sua.autonomouscar.controller.monitors.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IHealthMonitor;
import sua.autonomouscar.controller.properties.car.HapticDeviceHealthStatus;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class HapticDeviceHealthMonitor implements IHealthMonitor
{
    private BundleContext context;

    public HapticDeviceHealthMonitor(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerHealthCheck(boolean isHapticDeviceAvailable) {
        HapticDeviceHealthStatus hapticDeviceHealthStatus = OSGiUtils.getService(context, HapticDeviceHealthStatus.class);

        // Only update it if the value has changed.
        if (hapticDeviceHealthStatus != null && hapticDeviceHealthStatus.isAvailable() != isHapticDeviceAvailable) {
            System.out.println("[ Steering Health Monitor ] -  Updating the HealthStatus to " + isHapticDeviceAvailable);

            hapticDeviceHealthStatus.setIsAvailable(isHapticDeviceAvailable);
        }
    }
}
