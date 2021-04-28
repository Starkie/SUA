package sua.autonomouscar.controller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.listeners.NotificationServiceServiceListener;
import sua.autonomouscar.controller.properties.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.RoadContext;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.interaction.interfaces.INotificationService;

public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    private NotificationServiceServiceListener notificationsServiceListener;
    private RoadContext roadContext;
    private CurrentDrivingServiceStatus currentDrivingLevel;

    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;

        // TODO: See how to unregister the knowledge properties. 
        
        // The knowledge properties register themselves.
        this.roadContext = new RoadContext(context);
        this.currentDrivingLevel = new CurrentDrivingServiceStatus(context);

        this.notificationsServiceListener = new NotificationServiceServiceListener();
        context.registerService(NotificationServiceServiceListener.class, notificationsServiceListener, null);

        // Register the notificatiom service listener for when the L1 driving service or
        // notification service change.s
        String notificationSystemListenerFilter = "(objectclass=" + INotificationService.class.getName() + ")";
        context.addServiceListener(notificationsServiceListener, notificationSystemListenerFilter);

        String notificationL1SystemListenerFilter = "(objectclass=" + IL1_DrivingService.class.getName() + ")";
        context.addServiceListener(notificationsServiceListener, notificationL1SystemListenerFilter);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        this.roadContext = null;
        this.currentDrivingLevel = null;
        
        context.removeServiceListener(notificationsServiceListener);
        this.notificationsServiceListener = null;

        Activator.context = null;
    }
}
