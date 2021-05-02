package sua.autonomouscar.controller.probes.car;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.car.NotificationServiceHealthMonitor;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interaction.interfaces.INotificationService;

public class NotificationServiceHealthCheckProbe implements IProbe<INotificationService>, ServiceListener {

    private BundleContext context;

    public NotificationServiceHealthCheckProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
            case ServiceEvent.REGISTERED:
            case ServiceEvent.MODIFIED:
            case ServiceEvent.UNREGISTERING:
                this.registerMeasurement(OSGiUtils.getService(context, INotificationService.class));

                break;
            default:
                break;
        }
    }

    @Override
    public void registerMeasurement(INotificationService component) {
        NotificationServiceHealthMonitor notificationServiceHealthMonitor = OSGiUtils.getService(context, NotificationServiceHealthMonitor.class);

        if (notificationServiceHealthMonitor == null)
        {
            return;
        }

        notificationServiceHealthMonitor.registerHealthCheck(component != null);
    }
}
