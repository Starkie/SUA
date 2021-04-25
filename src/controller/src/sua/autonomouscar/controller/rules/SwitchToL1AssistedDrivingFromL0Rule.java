package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.interfaces.IAdaptionRule;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DrivingServiceUtils;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL1_AssistedDriving;

public class SwitchToL1AssistedDrivingFromL0Rule implements IAdaptionRule {
	
	private BundleContext context;

	public SwitchToL1AssistedDrivingFromL0Rule(BundleContext context) {
		this.context = context;
	}

	@Override
	public void evaluateAndExecute() {
		IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);
		
		// TODO: Add a condition to check that the LineSensors and the FrontDistanceSensor are available.
		if (!DrivingServiceUtils.isL0DrivingService(currentDrivingService))
		{
			return; 
		}
		
		ServiceReference<IL1_AssistedDriving> l1DrivingServiceReference = context.getServiceReference(IL1_AssistedDriving.class);
		IL1_AssistedDriving l1DrivingService = context.getService(l1DrivingServiceReference);
		
		if (l1DrivingService == null)
		{
			return;
		}
		
		System.out.println("[Controller] Executing the " + this.getClass().getSimpleName() + " rule.");
		
		currentDrivingService.stopDriving();
		l1DrivingService.startDriving();
	}
}
