package sua.autonomouscar.controller.rules.configuration;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.rules.AdaptionRuleBase;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.driving.l2.lka.L2_LaneKeepingAssist;
import sua.autonomouscar.infrastructure.OSGiUtils;

/**
 * Base class for implementing a rule that replaces the given distance sensor in the current driving service.
 * Useful for when a better distance sensor becomes available that can replace the current one.
 */
public abstract class ReplaceDistanceSensorRuleBase extends AdaptionRuleBase {

    private DistanceSensorPositon position;
    protected BundleContext context;

    protected ReplaceDistanceSensorRuleBase(BundleContext context, DistanceSensorPositon position) {
        this.context = context;
        this.position = position;
    }

    @Override
    public void evaluateAndExecute() {
        CurrentDrivingServiceStatus currentDrivingServiceStatus = OSGiUtils.getService(context, CurrentDrivingServiceStatus.class);

        DistanceSensorHealthStatus distanceSensorHealthStatus =
            OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", this.position));

        if (currentDrivingServiceStatus == null
            || distanceSensorHealthStatus == null
            || !evaluateRuleCondition(currentDrivingServiceStatus, distanceSensorHealthStatus))
        {
            return;
        }

        System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

        IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);

        this.replaceDistanceSensor(currentDrivingService, distanceSensorHealthStatus);
    }

    /**
     * Evaluate if the distance sensor in this position can be replaced in this driving service.
     * @param currentDrivingServiceStatus The current driving service property.
     * @param distanceSensorHealthStatus The distance sensor health status.
     * @return True if the distance sensor can be replaced. False otherwise.
     */
    protected abstract boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, DistanceSensorHealthStatus distanceSensorHealthStatus);

    /**
     * Replaces the active distance sensor in the current driving service with a better one.
     * @param currentDrivingService The current driving service.
     * @param distanceSensorHealthStatus The distance sensor health status.
     */
    protected abstract void replaceDistanceSensor(IDrivingService currentDrivingService, DistanceSensorHealthStatus distanceSensorHealthStatus);
}
