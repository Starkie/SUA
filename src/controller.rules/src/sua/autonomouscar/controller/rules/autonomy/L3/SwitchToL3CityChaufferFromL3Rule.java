package sua.autonomouscar.controller.rules.autonomy.L3;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.utils.L3ConfigurationUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL3_CityChauffer;
import sua.autonomouscar.driving.l3.citychauffer.L3_CityChauffer;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module to {@link IL3_CityChauffer}.
 */
public class SwitchToL3CityChaufferFromL3Rule extends SwitchToL3CityChaufferRuleBase {
    public SwitchToL3CityChaufferFromL3Rule(BundleContext context) {
        super(context);
    }

    @Override
    protected boolean evaluateRuleCondition(
            CurrentDrivingServiceStatus currentDrivingServiceStatus,
            RoadContext roadContext)
    {
        // All the L3 required services must be available.
        boolean areAllServicesAvailable = L3ConfigurationUtils.areAllL3RequiredServicesAvailable(this.context);

        // The current autonomy level must be L3 but not of the L3_CityChauffer.
        DrivingAutonomyLevel autonomyLevel = currentDrivingServiceStatus.getAutonomyLevel();

        boolean canSwitchFromCurrentDrivingService = autonomyLevel == DrivingAutonomyLevel.L3
            && !L3_CityChauffer.class.isAssignableFrom(currentDrivingServiceStatus.getDrivingServiceClass());

        // The road type must be City.
        boolean roadType = roadContext.getType() == ERoadType.CITY;

        // TODO: Add is-driver-ready-status
        return canSwitchFromCurrentDrivingService
            && areAllServicesAvailable
            && roadType;
    }
}
