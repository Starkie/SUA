package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.car.FallbackPlanHealthStatus;
import sua.autonomouscar.controller.properties.car.HumanSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.car.SteeringHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.driving.interfaces.IL2_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_CityChauffer;
import sua.autonomouscar.driving.l3.citychauffer.L3_CityChauffer;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interaction.interfaces.INotificationService;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * This rule changes the autonomous driving module from {@link IL2_DrivingService} to {@link IL3_CityChauffer}.
 */
public class SwitchToL3CityChaufferFromL2Rule extends AdaptionRuleBase {
    // The default lateral security distance is of 2.5m (250 cm).
    private static final int LATERAL_SECURITY_DISTANCE = 250;
    
    // The default longitudinal security distance is of 100m (10000 cm).
    private static final int LONGITUDINAL_SECURITY_DISTANCE = 10000;
    
    // The reference speed is of 60km/h.
    private static final int REFERENCE_SPEED = 60;

    private BundleContext context;

    public SwitchToL3CityChaufferFromL2Rule(BundleContext context) {
        this.context = context;
    }

    @Override
    public void evaluateAndExecute() {
        CurrentDrivingServiceStatus currentDrivingServiceStatus = OSGiUtils.getService(context, CurrentDrivingServiceStatus.class);
        DistanceSensorHealthStatus frontDistanceSensorHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", DistanceSensorPositon.FRONT));
        DistanceSensorHealthStatus leftDistanceSensorHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", DistanceSensorPositon.LEFT));
        DistanceSensorHealthStatus rightDistanceSensorHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", DistanceSensorPositon.RIGHT));
        DistanceSensorHealthStatus rearDistanceSensorHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", DistanceSensorPositon.REAR));
        EngineHealthStatus engineHealthStatus = OSGiUtils.getService(context, EngineHealthStatus.class);
        FallbackPlanHealthStatus fallbackPlanHealthStatus = OSGiUtils.getService(context, FallbackPlanHealthStatus.class);
        HumanSensorsHealthStatus humanSensorsHealthStatus = OSGiUtils.getService(context, HumanSensorsHealthStatus.class);
        LineSensorsHealthStatus leftLineSensorsHealthStatus = OSGiUtils.getService(context, LineSensorsHealthStatus.class, String.format("(%s=%s)", "position", LineSensorPosition.LEFT));
        LineSensorsHealthStatus rightLineSensorsHealthStatus = OSGiUtils.getService(context, LineSensorsHealthStatus.class, String.format("(%s=%s)", "position", LineSensorPosition.RIGHT));
        NotificationServiceHealthStatus notificationServiceHealthStatus = OSGiUtils.getService(context, NotificationServiceHealthStatus.class);
        RoadContext roadContext = OSGiUtils.getService(context, RoadContext.class);
        SteeringHealthStatus steeringHealthStatus = OSGiUtils.getService(context, SteeringHealthStatus.class);

        // TODO: ¿Is driver ready?
        if (currentDrivingServiceStatus == null
                || frontDistanceSensorHealthStatus == null 
                || leftDistanceSensorHealthStatus == null 
                || rightDistanceSensorHealthStatus == null 
                || rearDistanceSensorHealthStatus == null
                || engineHealthStatus == null
                || fallbackPlanHealthStatus == null
                || humanSensorsHealthStatus == null
                || leftLineSensorsHealthStatus == null
                || rightLineSensorsHealthStatus == null
                || notificationServiceHealthStatus == null
                || roadContext == null
                || steeringHealthStatus == null
                || !evaluateRuleCondition(currentDrivingServiceStatus, 
                        frontDistanceSensorHealthStatus, 
                        leftDistanceSensorHealthStatus, 
                        rightDistanceSensorHealthStatus, 
                        rearDistanceSensorHealthStatus,
                        engineHealthStatus,
                        fallbackPlanHealthStatus,
                        humanSensorsHealthStatus,
                        leftLineSensorsHealthStatus,
                        rightLineSensorsHealthStatus,
                        notificationServiceHealthStatus,
                        roadContext,
                        steeringHealthStatus))
        {
            return;
        }

        System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

        IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);

        ServiceReference<IL3_CityChauffer> l3DrivingServiceReference = context.getServiceReference(IL3_CityChauffer.class);

        IL3_CityChauffer l3DrivingService;

        if (l3DrivingServiceReference != null)
        {
            l3DrivingService = context.getService(l3DrivingServiceReference);
        }
        else
        {
            l3DrivingService = initializeL3CityChauffer();
        }

        // TODO: EXTRACT TO A CONFIGURE L3_DRIVINGSERVICE METHOD
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
        
        l3DrivingService.setLateralSecurityDistance(LATERAL_SECURITY_DISTANCE);
        l3DrivingService.setLongitudinalSecurityDistance(LONGITUDINAL_SECURITY_DISTANCE);
        l3DrivingService.setReferenceSpeed(REFERENCE_SPEED);

        // Unregister the current driving service and replace it with the L3_CityChauffer.
        if (currentDrivingService != null)
        {
            ((Thing)currentDrivingService).unregisterThing();
        }

        l3DrivingService.startDriving();
    }

    private boolean evaluateRuleCondition(
            CurrentDrivingServiceStatus currentDrivingServiceStatus,
            DistanceSensorHealthStatus frontDistanceSensorHealthStatus,
            DistanceSensorHealthStatus leftDistanceSensorHealthStatus,
            DistanceSensorHealthStatus rightDistanceSensorHealthStatus,
            DistanceSensorHealthStatus rearDistanceSensorHealthStatus,
            EngineHealthStatus engineHealthStatus,
            FallbackPlanHealthStatus fallbackPlanHealthStatus,
            HumanSensorsHealthStatus humanSensorsHealthStatus,
            LineSensorsHealthStatus leftLineSensorsHealthStatus,
            LineSensorsHealthStatus rightLineSensorsHealthStatus,
            NotificationServiceHealthStatus notificationServiceHealthStatus,
            RoadContext roadContext,
            SteeringHealthStatus steeringHealthStatus) 
    {
        // All the required sensors must be available, the autonomy level must be L2 
        // and the current road type must be CITY.
        boolean areAllSensorsAvailable = areAllL3RequiredServicesAvailable(frontDistanceSensorHealthStatus, leftDistanceSensorHealthStatus,
                rightDistanceSensorHealthStatus, rearDistanceSensorHealthStatus, engineHealthStatus,
                fallbackPlanHealthStatus, humanSensorsHealthStatus, leftLineSensorsHealthStatus,
                rightLineSensorsHealthStatus, notificationServiceHealthStatus, steeringHealthStatus);
        
        return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L2
            && roadContext.getType() == ERoadType.CITY
            && areAllSensorsAvailable;
    }

    // TODO: Move to a utility class.
    private boolean areAllL3RequiredServicesAvailable(DistanceSensorHealthStatus frontDistanceSensorHealthStatus,
            DistanceSensorHealthStatus leftDistanceSensorHealthStatus,
            DistanceSensorHealthStatus rightDistanceSensorHealthStatus,
            DistanceSensorHealthStatus rearDistanceSensorHealthStatus, EngineHealthStatus engineHealthStatus,
            FallbackPlanHealthStatus fallbackPlanHealthStatus, HumanSensorsHealthStatus humanSensorsHealthStatus,
            LineSensorsHealthStatus leftLineSensorsHealthStatus, LineSensorsHealthStatus rightLineSensorsHealthStatus,
            NotificationServiceHealthStatus notificationServiceHealthStatus,
            SteeringHealthStatus steeringHealthStatus) {
        
        boolean areAllDistanceSensorsAvailable = frontDistanceSensorHealthStatus.isAvailable()
            && leftDistanceSensorHealthStatus.isAvailable()
            && rightDistanceSensorHealthStatus.isAvailable()
            && rearDistanceSensorHealthStatus.isAvailable();
        
        boolean areLineSensorsAvailable = leftLineSensorsHealthStatus.isAvailable() && rightLineSensorsHealthStatus.isAvailable();
        
        boolean areAllSensorsAvailable = areAllDistanceSensorsAvailable
                    && areLineSensorsAvailable
                    && engineHealthStatus.isAvailable()
                    && fallbackPlanHealthStatus.isAvailable()
                    && humanSensorsHealthStatus.isAvailable()
                    && notificationServiceHealthStatus.isAvailable()
                    && steeringHealthStatus.isAvailable();
        
        return areAllSensorsAvailable;
    }

    private IL3_CityChauffer initializeL3CityChauffer() {
        L3_CityChauffer cityChauffer = new L3_CityChauffer(context, "L3_CityChauffer");
        cityChauffer.registerThing();

        return cityChauffer;
    }
}
