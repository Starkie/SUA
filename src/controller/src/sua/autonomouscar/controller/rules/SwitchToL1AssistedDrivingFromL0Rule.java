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
import sua.autonomouscar.driving.interfaces.IL1_AssistedDriving;
import sua.autonomouscar.infrastructure.Thing;

public class SwitchToL1AssistedDrivingFromL0Rule implements IAdaptionRule {
	
	private BundleContext context;

	public SwitchToL1AssistedDrivingFromL0Rule(BundleContext context) {
		this.context = context;
	}

	/**
	 * The condition to execute the rule: currentDrivingService instanceof IL0_DrivingService && is-front-distance-sensor-available && are-line-sensors-available
	 */
	@Override
	public void evaluateAndExecute() {
		IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);
		
		if (!DrivingServiceUtils.isL0DrivingService(currentDrivingService))
		{
			return; 
		}
		
		ServiceReference<IL1_AssistedDriving> l1DrivingServiceReference = context.getServiceReference(IL1_AssistedDriving.class);
		IL1_AssistedDriving l1DrivingService = context.getService(l1DrivingServiceReference);
		
		IDistanceSensor distanceSensor = AutonomousVehicleContextUtils.findDistanceSensor(context, DistanceSensorPositon.FRONT);
		
		ILineSensor leftLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.LEFT);
		ILineSensor rightLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.RIGHT);
		
		if (l1DrivingService == null 
			|| distanceSensor == null
			|| leftLineSensor == null
			|| rightLineSensor == null)
		{
			return;
		}
		
		System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");
		
		l1DrivingService.setFrontDistanceSensor(((Thing)distanceSensor).getId());
		l1DrivingService.setLeftLineSensor(((Thing)leftLineSensor).getId());
		l1DrivingService.setRightLineSensor(((Thing)rightLineSensor).getId());
		
		currentDrivingService.stopDriving();
		l1DrivingService.startDriving();
	}
}
