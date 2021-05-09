package sua.autonomouscar.controller.rules.autonomy.L3;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.AdaptionRuleBase;
import sua.autonomouscar.controller.rules.utils.L3ConfigurationUtils;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL3_TrafficJamChauffer;
import sua.autonomouscar.driving.l3.trafficjamchauffer.L3_TrafficJamChauffer;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;

/**
 * This rule changes the autonomous driving module to {@link IL3_TrafficJamChauffer}.
 */
public abstract class SwitchToL3TrafficJamChauffer extends AdaptionRuleBase {
    // The default lateral security distance is of 2.5m (250 cm).
    private static final int LATERAL_SECURITY_DISTANCE = 250;

    // The default longitudinal security distance is of 50m (50000 cm).
    private static final int LONGITUDINAL_SECURITY_DISTANCE = 50000;

    // The reference speed is of 60km/h.
    private static final int REFERENCE_SPEED = 60;

    protected BundleContext context;

    protected SwitchToL3TrafficJamChauffer(BundleContext context) {
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

        ServiceReference<IL3_TrafficJamChauffer> l3DrivingServiceReference = context.getServiceReference(IL3_TrafficJamChauffer.class);

        IL3_TrafficJamChauffer l3DrivingService;

        if (l3DrivingServiceReference != null)
        {
            l3DrivingService = context.getService(l3DrivingServiceReference);
        }
        else
        {
            l3DrivingService = initializeL3TrafficJamChauffer();
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

    protected abstract boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, RoadContext roadContext);

    private IL3_TrafficJamChauffer initializeL3TrafficJamChauffer() {
        L3_TrafficJamChauffer trafficJamChauffer = new L3_TrafficJamChauffer(context, "L3_TrafficJamChauffer");
        trafficJamChauffer.registerThing();

        return trafficJamChauffer;
    }
}
