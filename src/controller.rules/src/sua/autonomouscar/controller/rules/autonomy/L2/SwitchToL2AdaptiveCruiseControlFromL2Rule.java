package sua.autonomouscar.controller.rules.autonomy.L2;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.utils.L3ConfigurationUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.driving.interfaces.IL2_DrivingService;
import sua.autonomouscar.driving.l2.acc.L2_AdaptiveCruiseControl;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module from {@link IL2_DrivingService} to {@link IL2_AdaptiveCruiseControl}.
 */
public class SwitchToL2AdaptiveCruiseControlFromL2Rule extends SwitchToL2AdaptiveCruiseControlRuleBase {
    public SwitchToL2AdaptiveCruiseControlFromL2Rule(BundleContext context) {
        super(context);
    }

    @Override
    protected boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, RoadContext roadContext, DistanceSensorHealthStatus frontDistanceSensorHealthStatus, EngineHealthStatus engineHealthStatus) {
        // All the L2 required services must be available.
        boolean areAllServicesAvailable = frontDistanceSensorHealthStatus.isAvailable()
            && engineHealthStatus.isAvailable()
            && !L3ConfigurationUtils.areAllL3RequiredServicesAvailable(this.context);

        // The current autonomy level must be L2, but not of the type IL2_AdaptiveCruiseControl.
        DrivingAutonomyLevel autonomyLevel = currentDrivingServiceStatus.getAutonomyLevel();

        boolean canSwitchFromCurrentDrivingService = autonomyLevel == DrivingAutonomyLevel.L2
            && !L2_AdaptiveCruiseControl.class.isAssignableFrom(currentDrivingServiceStatus.getDrivingServiceClass());

        // The road type must be highway and fluid.
        boolean roadTypeAndStatus = roadContext.getType() == ERoadType.HIGHWAY;

        return canSwitchFromCurrentDrivingService
            && areAllServicesAvailable
            && roadTypeAndStatus;
    }
}
