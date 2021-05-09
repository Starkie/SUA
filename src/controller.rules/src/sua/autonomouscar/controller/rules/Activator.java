package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.car.FallbackPlanHealthStatus;
import sua.autonomouscar.controller.properties.car.HumanSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.autonomy.L2.SwitchToL2LaneKeepingAssistFromL1;
import sua.autonomouscar.controller.rules.autonomy.L2.SwitchToL2LaneKeepingAssistFromL3;
import sua.autonomouscar.controller.rules.autonomy.L3.SwitchToL3CityChauffer;
import sua.autonomouscar.controller.rules.autonomy.L3.SwitchToL3HighwayChauffer;
import sua.autonomouscar.controller.rules.autonomy.L3.SwitchToL3TrafficJamChaufferFromL2;
import sua.autonomouscar.controller.rules.autonomy.L3.SwitchToL3TrafficJamChaufferFromL3;
import sua.autonomouscar.infrastructure.devices.Steering;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private EnableNotificationsInL1Rule enableNotificationsInL1Rule;
	private SwitchToL0ManualDrivingFromL1 switchToL0ManualDrivingFromL1;
	private SwitchToL1AssistedDrivingFromL0Rule switchToL1AssistedDrivingFromL0Rule;
    private SwitchToL2AdaptiveCruiseControlFromL1Rule switchToL2AdaptiveCruiseControlFromL1Rule;
    private SwitchToL2LaneKeepingAssistFromL1 switchToL2LaneKeepingAssistFromL1Rule;
    private SwitchToL3CityChauffer swithToL3CityChauffer;
    private SwitchToL3HighwayChauffer swithToL3HighwayChauffer;
    private SwitchToL3TrafficJamChaufferFromL2 swithToL3TrafficJamChaufferFromL2;
    private SwitchToL3TrafficJamChaufferFromL3 swithToL3TrafficJamChaufferFromL3;
    private SwitchToL2LaneKeepingAssistFromL3 switchToL2LaneKeepingAssistFromL3Rule;

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

        this.switchToL2AdaptiveCruiseControlFromL1Rule = new SwitchToL2AdaptiveCruiseControlFromL1Rule(context);
        String swithToL2AccFromL1Filter = createFilter(RoadContext.class, CurrentDrivingServiceStatus.class, EngineHealthStatus.class, DistanceSensorHealthStatus.class);
        context.addServiceListener(this.switchToL2AdaptiveCruiseControlFromL1Rule, swithToL2AccFromL1Filter);

        this.switchToL2LaneKeepingAssistFromL1Rule = new SwitchToL2LaneKeepingAssistFromL1(context);
        String swithToL2LaneFilter = createFilter(CurrentDrivingServiceStatus.class, LineSensorsHealthStatus.class, RoadContext.class, Steering.class);
        context.addServiceListener(this.switchToL2LaneKeepingAssistFromL1Rule, swithToL2LaneFilter);

        this.switchToL2LaneKeepingAssistFromL3Rule = new SwitchToL2LaneKeepingAssistFromL3(context);
        context.addServiceListener(this.switchToL2LaneKeepingAssistFromL3Rule, swithToL2LaneFilter);

        String swithToL3RuleFilter = createFilter(
                CurrentDrivingServiceStatus.class,
                DistanceSensorHealthStatus.class,
                EngineHealthStatus.class,
                FallbackPlanHealthStatus.class,
                HumanSensorsHealthStatus.class,
                LineSensorsHealthStatus.class,
                NotificationServiceHealthStatus.class,
                RoadContext.class,
                Steering.class);

        this.swithToL3CityChauffer = new SwitchToL3CityChauffer(context);
        context.addServiceListener(this.swithToL3CityChauffer, swithToL3RuleFilter);

        this.swithToL3HighwayChauffer = new SwitchToL3HighwayChauffer(context);
        context.addServiceListener(this.swithToL3HighwayChauffer, swithToL3RuleFilter);

        this.swithToL3TrafficJamChaufferFromL2 = new SwitchToL3TrafficJamChaufferFromL2(context);
        context.addServiceListener(this.swithToL3TrafficJamChaufferFromL2, swithToL3RuleFilter);

        this.swithToL3TrafficJamChaufferFromL3 = new SwitchToL3TrafficJamChaufferFromL3(context);
        context.addServiceListener(this.swithToL3TrafficJamChaufferFromL3, swithToL3RuleFilter);
	}

	public void stop(BundleContext bundleContext) throws Exception {
	    context.removeServiceListener(this.enableNotificationsInL1Rule);
	    this.enableNotificationsInL1Rule = null;

	    context.removeServiceListener(this.switchToL0ManualDrivingFromL1);
        this.switchToL0ManualDrivingFromL1 = null;

	    context.removeServiceListener(this.switchToL1AssistedDrivingFromL0Rule);
        this.switchToL1AssistedDrivingFromL0Rule = null;

	    context.removeServiceListener(this.switchToL2AdaptiveCruiseControlFromL1Rule);
	    this.switchToL2AdaptiveCruiseControlFromL1Rule = null;

	    context.removeServiceListener(this.switchToL2LaneKeepingAssistFromL1Rule);
	    this.switchToL2LaneKeepingAssistFromL1Rule = null;

	    context.removeServiceListener(this.switchToL2LaneKeepingAssistFromL3Rule);
        this.switchToL2LaneKeepingAssistFromL3Rule = null;

	    context.removeServiceListener(this.swithToL3CityChauffer);
        this.swithToL3CityChauffer = null;

        context.removeServiceListener(this.swithToL3HighwayChauffer);
        this.swithToL3HighwayChauffer = null;

        context.removeServiceListener(this.swithToL3TrafficJamChaufferFromL2);
        this.swithToL3TrafficJamChaufferFromL2 = null;

        context.removeServiceListener(this.swithToL3TrafficJamChaufferFromL3);
        this.swithToL3TrafficJamChaufferFromL3 = null;

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
