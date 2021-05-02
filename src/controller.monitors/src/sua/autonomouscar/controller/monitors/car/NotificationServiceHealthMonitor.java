package sua.autonomouscar.controller.monitors.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IHealthMonitor;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class NotificationServiceHealthMonitor implements IHealthMonitor
{
    private BundleContext context;

    public NotificationServiceHealthMonitor(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerHealthCheck(boolean isNotificationServiceAvailable) {
        NotificationServiceHealthStatus notificationServiceHealthStatus = OSGiUtils.getService(context, NotificationServiceHealthStatus.class);

        // Only update it if the value has changed.
        if (notificationServiceHealthStatus != null && notificationServiceHealthStatus.isAvailable() != isNotificationServiceAvailable) {
            System.out.println("[ Notifications Health Monitor ] -  Updating the HealthStatus to " + isNotificationServiceAvailable);

            notificationServiceHealthStatus.setIsAvailable(isNotificationServiceAvailable);
        }
    }
}
