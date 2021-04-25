package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.interfaces.IAdaptionRule;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.DrivingServiceUtils;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL0_DrivingService;
import sua.autonomouscar.driving.interfaces.IL0_ManualDriving;
import sua.autonomouscar.driving.interfaces.IL1_AssistedDriving;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.infrastructure.driving.L1_DrivingService;

/**
 * Rule to switch from an active {@link IL1_DrivingService} to a {@link IL0_DrivingService} if any of the required sensors stops being available.
 */
public class SwitchToL0ManualDrivingAssistedDrivingFromL1 implements IAdaptionRule {
	
	private BundleContext context;

	public SwitchToL0ManualDrivingAssistedDrivingFromL1(BundleContext context) {
		this.context = context;
	}

	/**
	 * The condition to execute the rule: currentDrivingService instanceof IL1_DrivingService && is-driver-ready && !(are-line-sensors-available && (is-front-distance-sensor-available || is-lidar-available)
	 */
	@Override
	public void evaluateAndExecute() {
		IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);
		
		if (!DrivingServiceUtils.isL1DrivingService(currentDrivingService))
		{
			return; 
		}
		
		ServiceReference<IL0_ManualDriving> l0DrivingServiceReference = context.getServiceReference(IL0_ManualDriving.class);
		IL0_ManualDriving l0DrivingService = context.getService(l0DrivingServiceReference);
		
		// If no available replacements are available for the required sensors by the L1 driving service, it must fall back to L0 level
		IDistanceSensor distanceSensor = AutonomousVehicleContextUtils.findDistanceSensor(context, DistanceSensorPositon.FRONT);
		
		ILineSensor leftLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.LEFT);
		ILineSensor rightLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.RIGHT);
		
		if (l0DrivingService == null 
				|| (distanceSensor != null 
					&& leftLineSensor != null
					&& rightLineSensor != null)) 
		{
			return;
		}

		System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");
					
		currentDrivingService.stopDriving();
		l0DrivingService.startDriving();
	}
}
