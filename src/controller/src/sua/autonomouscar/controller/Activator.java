package sua.autonomouscar.controller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.listeners.NotificationServiceServiceListener;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interaction.interfaces.INotificationService;
import sua.autonomouscar.simulation.interfaces.ISimulationElement;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private SimulationContoller controller;
	private ServiceRegistration<?> serviceReference;
	private NotificationServiceServiceListener notificationsServiceListener; 

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		// Register the simulation controller, that will execute a loop iteration on each step.
		this.controller = new SimulationContoller(null, null);

		this.serviceReference = bundleContext.registerService(
				new String[] {ISimulationElement.class.getName(), Controller.class.getName()},
				this.controller,
				null);
		
		this.notificationsServiceListener = new NotificationServiceServiceListener();
		context.registerService(NotificationServiceServiceListener.class, notificationsServiceListener, null);
		
		// Register the notificatiom service listener for when the L1 driving service or notification service change.s
		String notificationSystemListenerFilter = "(objectclass=" + INotificationService.class.getName() + ")"; 
		context.addServiceListener(notificationsServiceListener, notificationSystemListenerFilter);
		
		String notificationL1SystemListenerFilter = "(objectclass=" + IL1_DrivingService.class.getName() + ")"; 
		context.addServiceListener(notificationsServiceListener, notificationL1SystemListenerFilter);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.serviceReference.unregister();
		this.controller = null;
		
		context.removeServiceListener(notificationsServiceListener);
		this.notificationsServiceListener = null;
		
		Activator.context = null;
	}
}
