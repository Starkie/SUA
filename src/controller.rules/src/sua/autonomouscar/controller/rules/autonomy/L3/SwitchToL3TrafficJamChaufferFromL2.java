package sua.autonomouscar.controller.rules.autonomy.L3;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.utils.L3ConfigurationUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL3_TrafficJamChauffer;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module to {@link IL3_TrafficJamChauffer}.
 */
public class SwitchToL3TrafficJamChaufferFromL2 extends SwitchToL3TrafficJamChauffer {
    public SwitchToL3TrafficJamChaufferFromL2(BundleContext context) {
        super(context);
    }

    protected boolean evaluateRuleCondition(
            CurrentDrivingServiceStatus currentDrivingServiceStatus,
            RoadContext roadContext)
    {
        // All the L3 required services must be available, the current autonomy level must be L2.
        boolean areAllServicesAvailable = L3ConfigurationUtils.areAllL3RequiredServicesAvailable(this.context);

        // The road type must be highway and the status jam or collapsed.
        boolean roadTypeAndStatus = roadContext.getType() == ERoadType.HIGHWAY
            && (roadContext.getStatus() == ERoadStatus.JAM
                || roadContext.getStatus() == ERoadStatus.COLLAPSED);

        return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L2
            && areAllServicesAvailable
            && roadTypeAndStatus;
    }
}
