package sua.autonomouscar.controller.rules.autonomy.L3;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.AdaptionRuleBase;
import sua.autonomouscar.controller.rules.utils.L3ConfigurationUtils;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL2_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_HighwayChauffer;
import sua.autonomouscar.driving.l3.highwaychauffer.L3_HighwayChauffer;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module to {@link IL3_HighwayChauffer}.
 */
public class SwitchToL3HighwayChauffer extends AdaptionRuleBase {
    // The default lateral security distance is of 2.5m (250 cm).
    private static final int LATERAL_SECURITY_DISTANCE = 250;

    // The default longitudinal security distance is of 100m (10000 cm).
    private static final int LONGITUDINAL_SECURITY_DISTANCE = 10000;

    // The reference speed is of 60km/h.
    private static final int REFERENCE_SPEED = 120;

    private BundleContext context;

    public SwitchToL3HighwayChauffer(BundleContext context) {
        this.context = context;
    }

    @Override
    public void evaluateAndExecute() {
        CurrentDrivingServiceStatus currentDrivingServiceStatus = OSGiUtils.getService(context, CurrentDrivingServiceStatus.class);
        RoadContext roadContext = OSGiUtils.getService(context, RoadContext.class);

        // TODO: ¿Add the is driver ready?
        if (currentDrivingServiceStatus == null
                || roadContext == null
                || !evaluateRuleCondition(currentDrivingServiceStatus, roadContext))
        {
            return;
        }

        System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

        IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);

        ServiceReference<IL3_HighwayChauffer> l3DrivingServiceReference = context.getServiceReference(IL3_HighwayChauffer.class);

        IL3_HighwayChauffer l3DrivingService;

        if (l3DrivingServiceReference != null)
        {
            l3DrivingService = context.getService(l3DrivingServiceReference);
        }
        else
        {
            l3DrivingService = initializeL3HighwayChauffer();
        }

        L3ConfigurationUtils.configureL3DrivingService(
            l3DrivingService,
            this.context,
            REFERENCE_SPEED,
            LATERAL_SECURITY_DISTANCE,
            LONGITUDINAL_SECURITY_DISTANCE);

        // Unregister the current driving service and replace it with the IL3_HighwayChauffer.
        if (currentDrivingService != null)
        {
            ((Thing)currentDrivingService).unregisterThing();
        }

        l3DrivingService.startDriving();
    }

    private boolean evaluateRuleCondition(
            CurrentDrivingServiceStatus currentDrivingServiceStatus,
            RoadContext roadContext)
    {
        // All the L3 required services must be available.
        boolean areAllServicesAvailable = L3ConfigurationUtils.areAllL3RequiredServicesAvailable(this.context);

        // The current autonomy level must be L2, or L3 but not of the HighwayChauffer.
        DrivingAutonomyLevel autonomyLevel = currentDrivingServiceStatus.getAutonomyLevel();

        boolean canSwitchFromCurrentDrivingService =
            autonomyLevel == DrivingAutonomyLevel.L2
                || (autonomyLevel == DrivingAutonomyLevel.L3
                    && !L3_HighwayChauffer.class.isAssignableFrom(currentDrivingServiceStatus.getDrivingServiceClass()));

        // The road type must be highway and fluid.
        boolean roadTypeAndStatus = roadContext.getType() == ERoadType.HIGHWAY
                    && roadContext.getStatus() == ERoadStatus.FLUID;

        return canSwitchFromCurrentDrivingService
            && areAllServicesAvailable
            && roadTypeAndStatus;
    }


    private IL3_HighwayChauffer initializeL3HighwayChauffer() {
        L3_HighwayChauffer highwayChauffer = new L3_HighwayChauffer(context, "L3_HighwayChauffer");
        highwayChauffer.registerThing();

        return highwayChauffer;
    }
}
