package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleContext;
import sua.autonomouscar.controller.interfaces.IAdaptionRule;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DrivingServiceUtils;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interaction.interfaces.INotificationService;

public class EnableNotificationsInL1Rule implements IAdaptionRule {
	private BundleContext context;

	public EnableNotificationsInL1Rule(BundleContext context) {
		this.context = context;
	}

	@Override
	public void evaluateAndExecute() {
		IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);

		if (!DrivingServiceUtils.isL1DrivingService(currentDrivingService))
		{
			return;
		}

		IL1_DrivingService l1DrivingService = (IL1_DrivingService)currentDrivingService;

		INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);

		if (notificationService == null)
		{
			return;
		}

		System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

		l1DrivingService.setNotificationService(((Thing)notificationService).getId());
	}
}
