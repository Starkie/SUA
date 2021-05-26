package sua.autonomouscar.controller.rules.notification.steeringwheel;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.driver.DriverContext;
import sua.autonomouscar.controller.rules.AdaptionRuleBase;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.interaction.HapticVibration;
import sua.autonomouscar.interaction.interfaces.INotificationService;

/**
 * This rule enables the {@link HapticVibration} of the steering wheel.
 */
public class EnableSteeringWheelHapticVibrationRule extends AdaptionRuleBase {

	private BundleContext context;

	public EnableSteeringWheelHapticVibrationRule(BundleContext context) {
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

        HapticVibration steeringWheelHapticVibration = OSGiUtils.getService(context, HapticVibration.class, "(id=SteeringWheel)");

        if (steeringWheelHapticVibration != null)
        {
        	return;
        }
        
        steeringWheelHapticVibration = this.initializeSteeringWheelHapticVibration();

        INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);

        if (notificationService != null)
        {
        	notificationService.addInteractionMechanism(steeringWheelHapticVibration.getId());
        }
	}

	private boolean evaluateRuleCondition(DriverContext driverContext,NotificationServiceHealthStatus notificationServiceHealthStatus)
	{
		return driverContext.getHasHandsOnWheel() && notificationServiceHealthStatus.isAvailable();
	}

    private HapticVibration initializeSteeringWheelHapticVibration() {
        HapticVibration steeringWheelHapticVibration = new HapticVibration(context, "SteeringWheel");
        steeringWheelHapticVibration.registerThing();

        return steeringWheelHapticVibration;
    }
}
