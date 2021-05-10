package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.infrastructure.interaction.NotificationService;
import sua.autonomouscar.interaction.interfaces.INotificationService;

/**
 * Adds the {@link INotificationService} to the {@link IL1_DrivingService} if it is available.
 */
public class EnableNotificationsInL1Rule extends AdaptionRuleBase {
	private BundleContext context;

	public EnableNotificationsInL1Rule(BundleContext context) {
		this.context = context;
	}

	@Override
	public void evaluateAndExecute() {
	    CurrentDrivingServiceStatus currentDrivingServiceStatus = OSGiUtils.getService(context, CurrentDrivingServiceStatus.class);
        NotificationServiceHealthStatus notificationServiceHealthStatus = OSGiUtils.getService(context, NotificationServiceHealthStatus.class);

        if (currentDrivingServiceStatus == null
                || notificationServiceHealthStatus == null
                || !evaluateRuleCondition(currentDrivingServiceStatus, notificationServiceHealthStatus))
        {
            return;
        }

		IL1_DrivingService l1DrivingService = (IL1_DrivingService) AutonomousVehicleContextUtils.findCurrentDrivingService(context);
		
		if (l1DrivingService == null)
		{
		    // Have we tried to execute the rule while the driving service was switching?
		    return;
		}
		
		INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);

		if (notificationService == null)
		{
			notificationService = initializeNotificationService();
		}

		System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

		l1DrivingService.setNotificationService(((Thing)notificationService).getId());
	}

	private boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, NotificationServiceHealthStatus notificationServiceHealthStatus) {
	    return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L1
	            && notificationServiceHealthStatus.isAvailable();
	}

    private INotificationService initializeNotificationService() {
        NotificationService notificationService = new NotificationService(context, "NotificationService");

        return notificationService;
    }

}
