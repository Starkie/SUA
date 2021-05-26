package sua.autonomouscar.controller.rules.notification.steeringwheel;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.driver.DriverContext;
import sua.autonomouscar.controller.rules.AdaptionRuleBase;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.interaction.HapticVibration;
import sua.autonomouscar.interaction.interfaces.INotificationService;

/**
 * This rule disables the {@link HapticVibration} of the steering wheel.
 */
public class DisableSteeringWheelHapticVibrationRule extends AdaptionRuleBase {

	private String steeringWheelHapticId = "SteeringWheel";

	private BundleContext context;

	public DisableSteeringWheelHapticVibrationRule(BundleContext context) {
		this.context = context;
	}

	@Override
	public void evaluateAndExecute() {
        DriverContext driverContext = OSGiUtils.getService(this.context, DriverContext.class);
        NotificationServiceHealthStatus notificationServiceHealthStatus = OSGiUtils.getService(this.context, NotificationServiceHealthStatus.class);

        if (driverContext == null
            || notificationServiceHealthStatus == null
            || !evaluateRuleCondition(driverContext, notificationServiceHealthStatus))
        {
            return;
        }

        System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

		HapticVibration steeringWheelHapticVibration = OSGiUtils.getService(context, HapticVibration.class, "(id=" + this.steeringWheelHapticId + ")");

        INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);

        if (notificationService != null)
        {
        	notificationService.removeInteractionMechanism(this.steeringWheelHapticId);
        }

        if (steeringWheelHapticVibration != null)
        {
        	steeringWheelHapticVibration.unregisterThing();
        }
	}

	private boolean evaluateRuleCondition(DriverContext driverContext,NotificationServiceHealthStatus notificationServiceHealthStatus)
	{
		return !driverContext.getHasHandsOnWheel() || !notificationServiceHealthStatus.isAvailable();
	}
}
