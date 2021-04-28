package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.properties.RoadContext;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.DrivingServiceUtils;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interfaces.ERoadType;

public class SwithToL2AdaptiveCruiseControlFromL1Rule extends AdaptionRuleBase {
    // The default lateral security distance is of 2.5m (250 cm).
    private static final int LATERAL_SECURITY_DISTANCE = 250;
    
    private BundleContext context;

    public SwithToL2AdaptiveCruiseControlFromL1Rule(BundleContext context) {
        this.context = context;
    }
    
    @Override
    public void evaluateAndExecute() {
        IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);

        if (!DrivingServiceUtils.isL1DrivingService(currentDrivingService)) {
            return;
        }

        ServiceReference<IL2_AdaptiveCruiseControl> l2DrivingServiceReference = context
                .getServiceReference(IL2_AdaptiveCruiseControl.class);
        IL2_AdaptiveCruiseControl l2DrivingService = context.getService(l2DrivingServiceReference);

        // TODO: How to disable sensors?
        ILineSensor leftLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.LEFT);
        ILineSensor rightLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.RIGHT);

        IDistanceSensor distanceSensor = AutonomousVehicleContextUtils.findDistanceSensor(context,
                DistanceSensorPositon.FRONT);

        IEngine engine = OSGiUtils.getService(context, IEngine.class);

        RoadContext roadContext = OSGiUtils.getService(context, RoadContext.class);
        ERoadType currentRoadType = roadContext.getType();

        if (l2DrivingService == null 
                || engine == null 
                || distanceSensor == null
                || !(currentRoadType == ERoadType.HIGHWAY)) {
            return;
        }

        System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

        l2DrivingService.setEngine(((Thing) engine).getId());
        l2DrivingService.setFrontDistanceSensor(((Thing) distanceSensor).getId());

        l2DrivingService.setLateralSecurityDistance(LATERAL_SECURITY_DISTANCE);

        currentDrivingService.stopDriving();
        l2DrivingService.startDriving();
    }
}
