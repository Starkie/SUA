package sua.autonomouscar.controller.rules.utils;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.car.FallbackPlanHealthStatus;
import sua.autonomouscar.controller.properties.car.HumanSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.car.SteeringHealthStatus;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interaction.interfaces.INotificationService;

/**
 * Contains utility methods for configuring {@link IL3_DrivingService}.
 */
public class L3ConfigurationUtils {
    /**
     * Checks if all the required services to instantiate a L3 Driving service are available.
     * @return True if the required services are available. Otherwise, returns false.
     */
    public static boolean areAllL3RequiredServicesAvailable(BundleContext context) {       
        DistanceSensorHealthStatus frontDistanceSensorHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", DistanceSensorPositon.FRONT));
        DistanceSensorHealthStatus leftDistanceSensorHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", DistanceSensorPositon.LEFT));
        DistanceSensorHealthStatus rightDistanceSensorHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", DistanceSensorPositon.RIGHT));
        DistanceSensorHealthStatus rearDistanceSensorHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", DistanceSensorPositon.REAR));
        
        LineSensorsHealthStatus leftLineSensorsHealthStatus = OSGiUtils.getService(context, LineSensorsHealthStatus.class, String.format("(%s=%s)", "position", LineSensorPosition.LEFT));
        LineSensorsHealthStatus rightLineSensorsHealthStatus = OSGiUtils.getService(context, LineSensorsHealthStatus.class, String.format("(%s=%s)", "position", LineSensorPosition.RIGHT));
        
        HumanSensorsHealthStatus humanSensorsHealthStatus = OSGiUtils.getService(context, HumanSensorsHealthStatus.class);
        
        EngineHealthStatus engineHealthStatus = OSGiUtils.getService(context, EngineHealthStatus.class);
        SteeringHealthStatus steeringHealthStatus = OSGiUtils.getService(context, SteeringHealthStatus.class);
        FallbackPlanHealthStatus fallbackPlanHealthStatus = OSGiUtils.getService(context, FallbackPlanHealthStatus.class);
        
        NotificationServiceHealthStatus notificationServiceHealthStatus = OSGiUtils.getService(context, NotificationServiceHealthStatus.class);
        
        boolean areAllDistanceSensorsAvailable = frontDistanceSensorHealthStatus != null && frontDistanceSensorHealthStatus.isAvailable()
            && leftDistanceSensorHealthStatus != null && leftDistanceSensorHealthStatus.isAvailable()
            && rightDistanceSensorHealthStatus != null && rightDistanceSensorHealthStatus.isAvailable()
            && rearDistanceSensorHealthStatus != null && rearDistanceSensorHealthStatus.isAvailable();
        
        boolean areLineSensorsAvailable = leftLineSensorsHealthStatus != null && leftLineSensorsHealthStatus.isAvailable() 
            && rightLineSensorsHealthStatus != null && rightLineSensorsHealthStatus.isAvailable();
        
        boolean areAllSensorsAvailable = areAllDistanceSensorsAvailable
                    && areLineSensorsAvailable
                    && engineHealthStatus != null && engineHealthStatus.isAvailable()
                    && fallbackPlanHealthStatus != null && fallbackPlanHealthStatus.isAvailable()
                    && humanSensorsHealthStatus != null && humanSensorsHealthStatus.isAvailable()
                    && notificationServiceHealthStatus != null && notificationServiceHealthStatus.isAvailable()
                    && steeringHealthStatus != null && steeringHealthStatus.isAvailable();
        
        return areAllSensorsAvailable;
    }
    
    /**
     * Configures the given {@link IL3_DrivingService} with all its required services and parameters.
     * @param l3DrivingService The L3 driving service to configure.
     * @return The configured L3 driving service.
     */
    public static IL3_DrivingService configureL3DrivingService(IL3_DrivingService l3DrivingService, BundleContext context, int referenceSpeed, int lateralSecurityDistance, int longitudinalSecurityDistance) {
        IDistanceSensor frontDistanceSensor = AutonomousVehicleContextUtils.findDistanceSensor(context, DistanceSensorPositon.FRONT);
        IDistanceSensor leftDistanceSensor = AutonomousVehicleContextUtils.findDistanceSensor(context, DistanceSensorPositon.LEFT);
        IDistanceSensor rightDistanceSensor = AutonomousVehicleContextUtils.findDistanceSensor(context, DistanceSensorPositon.RIGHT);
        IDistanceSensor rearDistanceSensor = AutonomousVehicleContextUtils.findDistanceSensor(context, DistanceSensorPositon.REAR);
        
        IEngine engine = OSGiUtils.getService(context, IEngine.class);
        
        // TODO: Get best fallback plan.
        IFallbackPlan fallbackPlan = OSGiUtils.getService(context, IFallbackPlan.class);
        
        IHumanSensors humanSensors = OSGiUtils.getService(context, IHumanSensors.class);

        ILineSensor leftLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.LEFT);
        ILineSensor rightLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.RIGHT);
        
        INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);
        
        IRoadSensor roadSensors = OSGiUtils.getService(context, IRoadSensor.class);
                
        ISteering steering = OSGiUtils.getService(context, ISteering.class);
        
        l3DrivingService.setFrontDistanceSensor(((Thing) frontDistanceSensor).getId());
        l3DrivingService.setLeftDistanceSensor(((Thing) leftDistanceSensor).getId());
        l3DrivingService.setRightDistanceSensor(((Thing) rightDistanceSensor).getId());
        l3DrivingService.setRearDistanceSensor(((Thing) rearDistanceSensor).getId());
        
        l3DrivingService.setEngine(((Thing) engine).getId());
        
        l3DrivingService.setFallbackPlan(((Thing) fallbackPlan).getId());
        l3DrivingService.setHumanSensors(((Thing) humanSensors).getId());
        
        l3DrivingService.setLeftLineSensor(((Thing)leftLineSensor).getId());
        l3DrivingService.setRightLineSensor(((Thing)rightLineSensor).getId());
        
        l3DrivingService.setNotificationService(((Thing)notificationService).getId());
        
        l3DrivingService.setRoadSensor(((Thing)roadSensors).getId());
        l3DrivingService.setSteering(((Thing)steering).getId());
        
        l3DrivingService.setLateralSecurityDistance(lateralSecurityDistance);
        l3DrivingService.setLongitudinalSecurityDistance(longitudinalSecurityDistance);
        l3DrivingService.setReferenceSpeed(referenceSpeed);
        
        return l3DrivingService;
    }
}
