package sua.autonomouscar.controller.rules.autonomy.L2;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.SteeringHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.rules.AdaptionRuleBase;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL2_LaneKeepingAssist;
import sua.autonomouscar.driving.l2.lka.L2_LaneKeepingAssist;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;

public abstract class SwitchToL2LaneKeepingAssistBase extends AdaptionRuleBase {
    protected BundleContext context;

    public SwitchToL2LaneKeepingAssistBase(BundleContext context) {
        this.context = context;
    }

    @Override
    public void evaluateAndExecute() {
        CurrentDrivingServiceStatus currentDrivingServiceStatus = OSGiUtils.getService(context, CurrentDrivingServiceStatus.class);
        LineSensorsHealthStatus leftLineSensorsHealthStatus = OSGiUtils.getService(context, LineSensorsHealthStatus.class, String.format("(%s=%s)", "position", LineSensorPosition.LEFT));
        LineSensorsHealthStatus rightLineSensorsHealthStatus = OSGiUtils.getService(context, LineSensorsHealthStatus.class, String.format("(%s=%s)", "position", LineSensorPosition.RIGHT));
        RoadContext roadContext = OSGiUtils.getService(context, RoadContext.class);
        SteeringHealthStatus steeringHealthStatus = OSGiUtils.getService(context, SteeringHealthStatus.class);

        if (currentDrivingServiceStatus == null
            || leftLineSensorsHealthStatus == null
            || rightLineSensorsHealthStatus == null
            || roadContext == null
            || steeringHealthStatus == null
            || !evaluateRuleCondition(currentDrivingServiceStatus, leftLineSensorsHealthStatus, rightLineSensorsHealthStatus, roadContext, steeringHealthStatus))
        {
            return;
        }

        System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

        IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);

        ServiceReference<IL2_LaneKeepingAssist> l2DrivingServiceReference = context.getServiceReference(IL2_LaneKeepingAssist.class);

        IL2_LaneKeepingAssist l2DrivingService;

        if (l2DrivingServiceReference != null)
        {
            l2DrivingService = context.getService(l2DrivingServiceReference);
        }
        else
        {
            l2DrivingService = initializeL2LaneKeepingAssist();
        }

        ILineSensor leftLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.LEFT);
        ILineSensor rightLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.RIGHT);
        ISteering steering = OSGiUtils.getService(context, ISteering.class);

        l2DrivingService.setLeftLineSensor(((Thing)leftLineSensor).getId());
        l2DrivingService.setRightLineSensor(((Thing)rightLineSensor).getId());
        l2DrivingService.setSteering(((Thing)steering).getId());

        // Unregister the current driving service and replace it with the IL2_LaneKeepingAssist.
        if (currentDrivingService != null)
        {
            ((Thing)currentDrivingService).unregisterThing();
        }

        l2DrivingService.startDriving();
    }

    protected abstract boolean evaluateRuleCondition(
        CurrentDrivingServiceStatus currentDrivingServiceStatus,
        LineSensorsHealthStatus leftLineSensorsHealthStatus,
        LineSensorsHealthStatus rightLineSensorsHealthStatus,
        RoadContext roadContext,
        SteeringHealthStatus steeringHealthStatus);

    private IL2_LaneKeepingAssist initializeL2LaneKeepingAssist() {
        L2_LaneKeepingAssist laneKeepingAssist = new L2_LaneKeepingAssist(context, "L2_LaneKeepingAssist");
        laneKeepingAssist.registerThing();

        return laneKeepingAssist;
    }
}
