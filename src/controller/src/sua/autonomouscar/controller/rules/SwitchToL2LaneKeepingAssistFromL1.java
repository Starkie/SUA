package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.interfaces.IAdaptionRule;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.DrivingServiceUtils;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL2_LaneKeepingAssist;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.properties.RoadContext;

public class SwitchToL2LaneKeepingAssistFromL1 implements IAdaptionRule {
    
    private BundleContext context;

    public SwitchToL2LaneKeepingAssistFromL1(BundleContext context) {
        this.context = context;
    }

    @Override
    public void evaluateAndExecute() {
        IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);
        
        if (!DrivingServiceUtils.isL1DrivingService(currentDrivingService))
        {
            return; 
        }
        
        ServiceReference<IL2_LaneKeepingAssist> l2DrivingServiceReference = context.getServiceReference(IL2_LaneKeepingAssist.class);
        IL2_LaneKeepingAssist l2DrivingService = context.getService(l2DrivingServiceReference);
        
        IDistanceSensor distanceSensor = AutonomousVehicleContextUtils.findDistanceSensor(context, DistanceSensorPositon.FRONT);
        
        ILineSensor leftLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.LEFT);
        ILineSensor rightLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.RIGHT);
        
        ISteering steering = OSGiUtils.getService(context, ISteering.class);
        
        RoadContext roadContext = OSGiUtils.getService(context, RoadContext.class);
        ERoadType currentRoadType = roadContext.getType();
        
        if (l2DrivingService == null 
            || steering == null
            || leftLineSensor == null
            || rightLineSensor == null
            || !(currentRoadType == ERoadType.STD_ROAD 
                || currentRoadType == ERoadType.CITY))
        {
            return;
        }
        
        System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

        l2DrivingService.setSteering(((Thing)steering).getId());
        
        l2DrivingService.setLeftLineSensor(((Thing)leftLineSensor).getId());
        l2DrivingService.setRightLineSensor(((Thing)rightLineSensor).getId());
        
        currentDrivingService.stopDriving();
        l2DrivingService.startDriving();
    }
}
