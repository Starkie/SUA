package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.infrastructure.devices.Steering;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private EnableNotificationsInL1Rule enableNotificationsInL1Rule;
	private SwitchToL0ManualDrivingFromL1 switchToL0ManualDrivingFromL1;
	private SwitchToL1AssistedDrivingFromL0Rule switchToL1AssistedDrivingFromL0Rule;
    private SwithToL2AdaptiveCruiseControlFromL1Rule swithToL2AdaptiveCruiseControlFromL1Rule;
    private SwitchToL2LaneKeepingAssistFromL1 swithToL2LaneKeepingAssistFromL1Rule;

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		this.enableNotificationsInL1Rule = new EnableNotificationsInL1Rule(bundleContext);
		String enableNotificationSystemInL1ServiceFilter = createFilter(CurrentDrivingServiceStatus.class, NotificationServiceHealthStatus.class);
		context.addServiceListener(enableNotificationsInL1Rule, enableNotificationSystemInL1ServiceFilter);

		this.switchToL0ManualDrivingFromL1 = new SwitchToL0ManualDrivingFromL1(context);
		String swithToL0FromL1Filter = createFilter(CurrentDrivingServiceStatus.class, LineSensorsHealthStatus.class, DistanceSensorHealthStatus.class);
        context.addServiceListener(this.switchToL0ManualDrivingFromL1, swithToL0FromL1Filter);

		this.switchToL1AssistedDrivingFromL0Rule = new SwitchToL1AssistedDrivingFromL0Rule(context);
        String swithToL1FromL0Filter = createFilter(CurrentDrivingServiceStatus.class, LineSensorsHealthStatus.class, DistanceSensorHealthStatus.class);
        context.addServiceListener(this.switchToL1AssistedDrivingFromL0Rule, swithToL1FromL0Filter);

        this.swithToL2AdaptiveCruiseControlFromL1Rule = new SwithToL2AdaptiveCruiseControlFromL1Rule(context);
        String swithToL2AccFromL1Filter = createFilter(RoadContext.class, CurrentDrivingServiceStatus.class, EngineHealthStatus.class, DistanceSensorHealthStatus.class);
        context.addServiceListener(this.swithToL2AdaptiveCruiseControlFromL1Rule, swithToL2AccFromL1Filter);
        
        this.swithToL2LaneKeepingAssistFromL1Rule = new SwitchToL2LaneKeepingAssistFromL1(context);
        String swithToL2LaneFromL1Filter = createFilter(CurrentDrivingServiceStatus.class, LineSensorsHealthStatus.class, RoadContext.class, Steering.class);
        context.addServiceListener(this.swithToL2LaneKeepingAssistFromL1Rule, swithToL2LaneFromL1Filter);
	}

	public void stop(BundleContext bundleContext) throws Exception {
	    context.removeServiceListener(this.enableNotificationsInL1Rule);
	    this.enableNotificationsInL1Rule = null;

	    context.removeServiceListener(this.switchToL0ManualDrivingFromL1);
        this.switchToL0ManualDrivingFromL1 = null;

	    context.removeServiceListener(this.switchToL1AssistedDrivingFromL0Rule);
        this.switchToL1AssistedDrivingFromL0Rule = null;

	    context.removeServiceListener(this.swithToL2AdaptiveCruiseControlFromL1Rule);
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
