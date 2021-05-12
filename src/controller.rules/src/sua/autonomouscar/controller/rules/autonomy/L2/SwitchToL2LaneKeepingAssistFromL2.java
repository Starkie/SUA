package sua.autonomouscar.controller.rules.autonomy.L2;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.SteeringHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.l2.lka.L2_LaneKeepingAssist;
import sua.autonomouscar.interfaces.ERoadType;

public class SwitchToL2LaneKeepingAssistFromL2 extends SwitchToL2LaneKeepingAssistBase {
    public SwitchToL2LaneKeepingAssistFromL2(BundleContext context) {
        super(context);
    }

    protected boolean evaluateRuleCondition(
        CurrentDrivingServiceStatus currentDrivingServiceStatus,
        LineSensorsHealthStatus leftLineSensorsHealthStatus,
        LineSensorsHealthStatus rightLineSensorsHealthStatus,
        RoadContext roadContext,
        SteeringHealthStatus steeringHealthStatus)
    {
        boolean areLineSensorsAvailable = leftLineSensorsHealthStatus.isAvailable()
            && rightLineSensorsHealthStatus.isAvailable();

        boolean canSwitchFromCurrentDrivingService = currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L2
            && !L2_LaneKeepingAssist.class.isAssignableFrom(currentDrivingServiceStatus.getDrivingServiceClass());

        boolean roadTypeAndStatus = roadContext.getType() == ERoadType.STD_ROAD
            || roadContext.getType() == ERoadType.CITY;

        return canSwitchFromCurrentDrivingService
            && roadTypeAndStatus
            && areLineSensorsAvailable
            && steeringHealthStatus.isAvailable();
    }
}
