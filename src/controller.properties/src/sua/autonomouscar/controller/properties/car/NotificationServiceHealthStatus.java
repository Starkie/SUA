package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;
import sua.autonomouscar.interaction.interfaces.INotificationService;

/**
 * Represents the status of the {@link INotificationService}.
 */
public class NotificationServiceHealthStatus extends HealthStatusBase {
    public NotificationServiceHealthStatus(BundleContext context) {
        super(context);
        
        this.addImplementedInterface(NotificationServiceHealthStatus.class.getName());
    }
}
