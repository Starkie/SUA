package sua.autonomouscar.controller.rules.autonomy.L3;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.utils.L3ConfigurationUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_HighwayChauffer;
import sua.autonomouscar.driving.l3.highwaychauffer.L3_HighwayChauffer;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module to {@link IL3_HighwayChauffer} from {@link IL3_DrivingService}.
 */
public class SwitchToL3HighwayChaufferFromL3Rule extends SwitchToL3HighwayChaufferRuleBase {
    public SwitchToL3HighwayChaufferFromL3Rule(BundleContext context) {
        super(context);
    }

    @Override
    protected boolean evaluateRuleCondition(
            CurrentDrivingServiceStatus currentDrivingServiceStatus,
            RoadContext roadContext)
    {
        // All the L3 required services must be available.
        boolean areAllServicesAvailable = L3ConfigurationUtils.areAllL3RequiredServicesAvailable(this.context);

        // The autonomy level must be L3 but not of the HighwayChauffer.
        DrivingAutonomyLevel autonomyLevel = currentDrivingServiceStatus.getAutonomyLevel();

        boolean canSwitchFromCurrentDrivingService = autonomyLevel == DrivingAutonomyLevel.L3
            && !L3_HighwayChauffer.class.isAssignableFrom(currentDrivingServiceStatus.getDrivingServiceClass());

        // The road type must be highway and fluid.
        boolean roadTypeAndStatus = roadContext.getType() == ERoadType.HIGHWAY
            && roadContext.getStatus() == ERoadStatus.FLUID;

        // TODO: Add is-driver-ready-status
        return canSwitchFromCurrentDrivingService
            && areAllServicesAvailable
            && roadTypeAndStatus;
    }
}
