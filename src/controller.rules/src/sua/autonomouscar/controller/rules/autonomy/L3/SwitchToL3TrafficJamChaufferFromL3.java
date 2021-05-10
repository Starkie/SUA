package sua.autonomouscar.controller.rules.autonomy.L3;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.utils.L3ConfigurationUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_TrafficJamChauffer;
import sua.autonomouscar.driving.l3.trafficjamchauffer.L3_TrafficJamChauffer;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module to {@link IL3_TrafficJamChauffer} from another {@link IL3_DrivingService}.
 */
public class SwitchToL3TrafficJamChaufferFromL3 extends SwitchToL3TrafficJamChauffer {
    public SwitchToL3TrafficJamChaufferFromL3(BundleContext context) {
        super(context);
    }

    protected boolean evaluateRuleCondition(
            CurrentDrivingServiceStatus currentDrivingServiceStatus,
            RoadContext roadContext)
    {
        // All the L3 required services must be available.
        boolean areAllServicesAvailable = L3ConfigurationUtils.areAllL3RequiredServicesAvailable(this.context);

        // The current autonomy level must be L3 but not of the TrafficJamChauffer.
        DrivingAutonomyLevel autonomyLevel = currentDrivingServiceStatus.getAutonomyLevel();

        boolean canSwitchFromCurrentDrivingService = autonomyLevel == DrivingAutonomyLevel.L3
            && !L3_TrafficJamChauffer.class.isAssignableFrom(currentDrivingServiceStatus.getDrivingServiceClass());

        // The road type must be highway and the status jam or collapsed.
        boolean roadTypeAndStatus = roadContext.getType() == ERoadType.HIGHWAY
                    && (roadContext.getStatus() == ERoadStatus.JAM
                        || roadContext.getStatus() == ERoadStatus.COLLAPSED);

        return canSwitchFromCurrentDrivingService
            && areAllServicesAvailable
            && roadTypeAndStatus;
    }
}
