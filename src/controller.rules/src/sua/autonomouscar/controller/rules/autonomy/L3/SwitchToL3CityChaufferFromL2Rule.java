package sua.autonomouscar.controller.rules.autonomy.L3;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.utils.L3ConfigurationUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL2_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_CityChauffer;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module to {@link IL3_CityChauffer} from {@link IL2_DrivingService}.
 */
public class SwitchToL3CityChaufferFromL2Rule extends SwitchToL3CityChaufferRuleBase {
    public SwitchToL3CityChaufferFromL2Rule(BundleContext context) {
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
        DrivingAutonomyLevel autonomyLevel = currentDrivingServiceStatus.getAutonomyLevel();

        boolean canSwitchFromCurrentDrivingService = autonomyLevel == DrivingAutonomyLevel.L2;

        // The road type must be City.
        boolean roadType = roadContext.getType() == ERoadType.CITY;

        return canSwitchFromCurrentDrivingService
            && areAllServicesAvailable
            && roadType;
    }
}
