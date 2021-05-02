package sua.autonomouscar.controller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.listeners.NotificationServiceServiceListener;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.interaction.interfaces.INotificationService;

public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    private NotificationServiceServiceListener notificationsServiceListener;

    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        
        this.notificationsServiceListener = new NotificationServiceServiceListener();
        context.registerService(NotificationServiceServiceListener.class, notificationsServiceListener, null);

        // Register the notification service listener for when the L1 driving service or
        // notification service changes.
        String notificationSystemListenerFilter = "(objectclass=" + INotificationService.class.getName() + ")";
        context.addServiceListener(notificationsServiceListener, notificationSystemListenerFilter);

        String notificationL1SystemListenerFilter = "(objectclass=" + IL1_DrivingService.class.getName() + ")";
        context.addServiceListener(notificationsServiceListener, notificationL1SystemListenerFilter);
    }

    public void stop(BundleContext bundleContext) throws Exception {             
        context.removeServiceListener(notificationsServiceListener);
        this.notificationsServiceListener = null;

        Activator.context = null;
    }
}
