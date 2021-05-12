package sua.autonomouscar.controller.rules.autonomy.L3;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.utils.L3ConfigurationUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL3_HighwayChauffer;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module to {@link IL3_HighwayChauffer}.
 */
public class SwitchToL3HighwayChaufferFromL2Rule extends SwitchToL3HighwayChaufferRuleBase {
    public SwitchToL3HighwayChaufferFromL2Rule(BundleContext context) {
        super(context);
    }

    @Override
    protected boolean evaluateRuleCondition(
            CurrentDrivingServiceStatus currentDrivingServiceStatus,
            RoadContext roadContext)
    {
        // All the L3 required services must be available.
        boolean areAllServicesAvailable = L3ConfigurationUtils.areAllL3RequiredServicesAvailable(this.context);

        // The current autonomy level must be L2.
        boolean canSwitchFromCurrentDrivingService = currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L2;

        // The road type must be highway and fluid.
        boolean roadTypeAndStatus = roadContext.getType() == ERoadType.HIGHWAY
            && roadContext.getStatus() == ERoadStatus.FLUID;

        return canSwitchFromCurrentDrivingService
            && areAllServicesAvailable
            && roadTypeAndStatus;
    }
}
