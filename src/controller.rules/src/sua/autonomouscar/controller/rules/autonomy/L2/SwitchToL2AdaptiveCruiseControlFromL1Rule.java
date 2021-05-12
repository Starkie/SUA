package sua.autonomouscar.controller.rules.autonomy.L2;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module from {@link IL1_DrivingService} to {@link IL2_AdaptiveCruiseControl}.
 */
public class SwitchToL2AdaptiveCruiseControlFromL1Rule extends SwitchToL2AdaptiveCruiseControlRuleBase {
    public SwitchToL2AdaptiveCruiseControlFromL1Rule(BundleContext context) {
        super(context);
    }

    @Override
    protected boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, RoadContext roadContext, DistanceSensorHealthStatus frontDistanceSensorHealthStatus, EngineHealthStatus engineHealthStatus) {
        return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L1
                && frontDistanceSensorHealthStatus.isAvailable()
                && roadContext.getType() == ERoadType.HIGHWAY
                && engineHealthStatus.isAvailable();
    }
}
