package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private EnableNotificationsInL1Rule enableNotificationsInL1Rule;
    private SwithToL2AdaptiveCruiseControlFromL1Rule swithToL2AdaptiveCruiseControlFromL1Rule;

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		this.enableNotificationsInL1Rule = new EnableNotificationsInL1Rule(bundleContext);
		String enableNotificationSystemInL1ServiceFilter = createFilter(CurrentDrivingServiceStatus.class, NotificationServiceHealthStatus.class);
		context.addServiceListener(enableNotificationsInL1Rule, enableNotificationSystemInL1ServiceFilter);

        this.swithToL2AdaptiveCruiseControlFromL1Rule = new SwithToL2AdaptiveCruiseControlFromL1Rule(context);
        String sithToL2AccFromL1Filter = createFilter(RoadContext.class, CurrentDrivingServiceStatus.class, EngineHealthStatus.class);
        context.addServiceListener(swithToL2AdaptiveCruiseControlFromL1Rule, sithToL2AccFromL1Filter);
	}

	public void stop(BundleContext bundleContext) throws Exception {
	    context.removeServiceListener(enableNotificationsInL1Rule);
	    this.enableNotificationsInL1Rule = null;

	    context.removeServiceListener(swithToL2AdaptiveCruiseControlFromL1Rule);
	    this.swithToL2AdaptiveCruiseControlFromL1Rule = null;

		Activator.context = null;
	}

	// TODO: Move to an utilities class.
	// Obtained from: https://gist.github.com/toelen/1370316
	private static String createFilter(Class<?>... interfaces) {
        StringBuilder builder = new StringBuilder();

        builder.append("( |");
        for (Class<?> clazz : interfaces) {
            builder.append("(objectclass=" + clazz.getName()
                    + ") ");
        }

        builder.append(" ) ");
        return builder.toString();
	}

}
