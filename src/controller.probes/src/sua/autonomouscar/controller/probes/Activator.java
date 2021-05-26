package sua.autonomouscar.controller.probes;

import java.sql.Driver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.driver.CopilotStatusMonitor;
import sua.autonomouscar.controller.monitors.driver.DriverStatusMonitor;
import sua.autonomouscar.controller.monitors.driver.IDriverStatusMonitor;
import sua.autonomouscar.controller.monitors.driver.ISeatStatusMonitor;
import sua.autonomouscar.controller.probes.car.DistanceSensorHealthCheckProbe;
import sua.autonomouscar.controller.probes.car.DrivingServiceProbe;
import sua.autonomouscar.controller.probes.car.EngineHealthCheckProbe;
import sua.autonomouscar.controller.probes.car.FallbackPlanHealthCheckProbe;
import sua.autonomouscar.controller.probes.car.HumanSensorsHealthCheckProbe;
import sua.autonomouscar.controller.probes.car.LineSensorHealthCheckProbe;
import sua.autonomouscar.controller.probes.car.NotificationServiceHealthCheckProbe;
import sua.autonomouscar.controller.probes.car.SteeringHealthCheckProbe;
import sua.autonomouscar.controller.probes.driver.DriverStatusProbe;
import sua.autonomouscar.controller.probes.driver.HandsOnWheelProbe;
import sua.autonomouscar.controller.probes.driver.SeatSensorProbe;
import sua.autonomouscar.controller.probes.road.RoadContextProbe;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.devices.interfaces.ISeatSensor;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.interaction.interfaces.INotificationService;

public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    private RoadContextProbe roadContextProbe;
    private DrivingServiceProbe drivingServiceProbe;
    private EngineHealthCheckProbe engineHealthCheckProbe;
    private FallbackPlanHealthCheckProbe fallbackPlanHealthCheckProbe;
    private HumanSensorsHealthCheckProbe humanSensorsHealthCheckProbe;
    private LineSensorHealthCheckProbe leftLineSensorHealthCheckProbe;
    private LineSensorHealthCheckProbe rightLineSensorHealthCheckProbe;
    private NotificationServiceHealthCheckProbe notificationServiceHealthCheckProbe;
    private DistanceSensorHealthCheckProbe leftDistanceSensorHealthCheckProbe;
    private DistanceSensorHealthCheckProbe frontDistanceSensorHealthCheckProbe;
    private DistanceSensorHealthCheckProbe rightDistanceSensorHealthCheckProbe;
    private DistanceSensorHealthCheckProbe rearDistanceSensorHealthCheckProbe;
    private SteeringHealthCheckProbe steeringHealthCheckProbe;
	private SeatSensorProbe driverSeatSensorProbe;
	private SeatSensorProbe copilotSeatSensorProbe;
	private HandsOnWheelProbe handsOnWheelProbe;
	private DriverStatusProbe driverStatusProbe;

    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;


        // Add the Driving Service probe.
        this.drivingServiceProbe = new DrivingServiceProbe(context);
        context.registerService(
                new String[] { DrivingServiceProbe.class.getName(), IProbe.class.getName() },
                this.drivingServiceProbe,
                null);

        String drivingServiceProbeListenerFilter = "(objectclass=" + IDrivingService.class.getName() + ")";
        context.addServiceListener(this.drivingServiceProbe, drivingServiceProbeListenerFilter);

        // Add the Engine Health Check probe.
        this.engineHealthCheckProbe = new EngineHealthCheckProbe(context);
        context.registerService(
                new String[] { EngineHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.engineHealthCheckProbe,
                null);

        String engineHealthCheckProbeListenerFilter = "(objectclass=" + IEngine.class.getName() + ")";
        context.addServiceListener(this.engineHealthCheckProbe, engineHealthCheckProbeListenerFilter);

        // Add the Fallback Plan Health Check probe.
        this.fallbackPlanHealthCheckProbe = new FallbackPlanHealthCheckProbe(context);
        context.registerService(
                new String[] { FallbackPlanHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.fallbackPlanHealthCheckProbe,
                null);

        String fallbackPlanHealthCheckProbeListenerFilter = "(objectclass=" + IFallbackPlan.class.getName() + ")";
        context.addServiceListener(this.fallbackPlanHealthCheckProbe, fallbackPlanHealthCheckProbeListenerFilter);

        // Add the Human Sensors Health Check probe.
        this.humanSensorsHealthCheckProbe = new HumanSensorsHealthCheckProbe(context);
        context.registerService(
                new String[] { HumanSensorsHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.humanSensorsHealthCheckProbe,
                null);

        String humanSensorsHealthCheckProbeListenerFilter = "(objectclass=" + IHumanSensors.class.getName() + ")";
        context.addServiceListener(this.humanSensorsHealthCheckProbe, humanSensorsHealthCheckProbeListenerFilter);

        // Add the Front Distance Sensor Health Check probe.
        this.frontDistanceSensorHealthCheckProbe = new DistanceSensorHealthCheckProbe(context, DistanceSensorPositon.FRONT);
        context.registerService(
                new String[] { DistanceSensorHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.frontDistanceSensorHealthCheckProbe,
                null);

        String frontDistanceSensorProbeListenerFilter = "(&(objectclass=" + IDistanceSensor.class.getName() + ")(|(id=" + DistanceSensorPositon.FRONT.getNormalSensorId() + ")(id=" + DistanceSensorPositon.FRONT.getLidarSensorId() + ")))";
        context.addServiceListener(this.frontDistanceSensorHealthCheckProbe, frontDistanceSensorProbeListenerFilter);

        // Add the Left Distance Sensor Health Check probe.
        this.leftDistanceSensorHealthCheckProbe = new DistanceSensorHealthCheckProbe(context, DistanceSensorPositon.LEFT);
        context.registerService(
                new String[] { DistanceSensorHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.leftDistanceSensorHealthCheckProbe,
                null);

        String leftDistanceSensorProbeListenerFilter = "(&(objectclass=" + IDistanceSensor.class.getName() + ")(|(id=" + DistanceSensorPositon.LEFT.getNormalSensorId() + ")(id=" + DistanceSensorPositon.LEFT.getLidarSensorId() + ")))";
        context.addServiceListener(this.leftDistanceSensorHealthCheckProbe, leftDistanceSensorProbeListenerFilter);

        // Add the Right Distance Sensor Health Check probe.
        this.rightDistanceSensorHealthCheckProbe = new DistanceSensorHealthCheckProbe(context, DistanceSensorPositon.RIGHT);
        context.registerService(
                new String[] { DistanceSensorHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.rightDistanceSensorHealthCheckProbe,
                null);

        String rightDistanceSensorProbeListenerFilter = "(&(objectclass=" + IDistanceSensor.class.getName() + ")(|(id=" + DistanceSensorPositon.RIGHT.getNormalSensorId() + ")(id=" + DistanceSensorPositon.RIGHT.getLidarSensorId() + ")))";
        context.addServiceListener(this.rightDistanceSensorHealthCheckProbe, rightDistanceSensorProbeListenerFilter);

        // Add the Rear Distance Sensor Health Check probe.
        this.rearDistanceSensorHealthCheckProbe = new DistanceSensorHealthCheckProbe(context, DistanceSensorPositon.REAR);
        context.registerService(
                new String[] { DistanceSensorHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.rearDistanceSensorHealthCheckProbe,
                null);

        String rearDistanceSensorProbeListenerFilter = "(&(objectclass=" + IDistanceSensor.class.getName() + ")(|(id=" + DistanceSensorPositon.REAR.getNormalSensorId() + ")(id=" + DistanceSensorPositon.REAR.getLidarSensorId() + ")))";
        context.addServiceListener(this.rearDistanceSensorHealthCheckProbe, rearDistanceSensorProbeListenerFilter);

        // Add the Left Line Sensor Health Check probe.
        this.leftLineSensorHealthCheckProbe = new LineSensorHealthCheckProbe(context, LineSensorPosition.LEFT);
        context.registerService(
                new String[] { LineSensorHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.leftLineSensorHealthCheckProbe,
                null);

        String leftLineSensorProbeListenerFilter = "(&(objectclass=" + ILineSensor.class.getName() + ")(id=" + LineSensorPosition.LEFT.getSensorId() + "))";
        context.addServiceListener(this.leftLineSensorHealthCheckProbe, leftLineSensorProbeListenerFilter);

        // Add the Right Line Sensor Health Check probe.
        this.rightLineSensorHealthCheckProbe = new LineSensorHealthCheckProbe(context, LineSensorPosition.RIGHT);
        context.registerService(
                new String[] { LineSensorHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.rightLineSensorHealthCheckProbe,
                null);

        String rightLineSensorProbeListenerFilter = "(&(objectclass=" + ILineSensor.class.getName() + ")(id=" + LineSensorPosition.RIGHT.getSensorId() + "))";
        context.addServiceListener(this.rightLineSensorHealthCheckProbe, rightLineSensorProbeListenerFilter);

        // Add the Notification Service Health Check probe.
        this.notificationServiceHealthCheckProbe = new NotificationServiceHealthCheckProbe(context);
        context.registerService(
                new String[] { NotificationServiceHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.notificationServiceHealthCheckProbe,
                null);

        String notificationServiceHealthCheckProbeListenerFilter = "(objectclass=" + INotificationService.class.getName() + ")";
        context.addServiceListener(this.notificationServiceHealthCheckProbe, notificationServiceHealthCheckProbeListenerFilter);

        // Add the IRoadSensor probe.
        this.roadContextProbe = new RoadContextProbe(context);
        context.registerService(
                new String[] { RoadContextProbe.class.getName(), IProbe.class.getName() },
                this.roadContextProbe,
                null);

        String roadContextProbeListenerFilter = "(objectclass=" + IRoadSensor.class.getName() + ")";
        context.addServiceListener(this.roadContextProbe, roadContextProbeListenerFilter);

        // Add the Steering Health Check probe.
        this.steeringHealthCheckProbe = new SteeringHealthCheckProbe(context);
        context.registerService(
                new String[] { SteeringHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.steeringHealthCheckProbe,
                null);

        String steeringHealthCheckProbeListenerFilter = "(objectclass=" + ISteering.class.getName() + ")";
        context.addServiceListener(this.steeringHealthCheckProbe, steeringHealthCheckProbeListenerFilter);
        
        // Seat sensor probes.
        this.driverSeatSensorProbe = new SeatSensorProbe(bundleContext, IDriverStatusMonitor.class.getName());
        
        String driverSeatSensorProbeListenerFilter = "(&(objectclass=" + ISeatSensor.class.getName() + ")(id=DriverSeatSensor))";
        context.addServiceListener(this.driverSeatSensorProbe, driverSeatSensorProbeListenerFilter);
        
        this.copilotSeatSensorProbe = new SeatSensorProbe(bundleContext, ISeatStatusMonitor.class.getName());
        
        String copilotSeatSensorProbeListenerFilter1 = "(&(objectclass=" + ISeatSensor.class.getName() + ")(id=CopilotSeatSensor))";
        context.addServiceListener(this.copilotSeatSensorProbe, copilotSeatSensorProbeListenerFilter1);
        
     // Add the Hands On Wheel probe.
        this.handsOnWheelProbe = new HandsOnWheelProbe(context);
        context.registerService(
                new String[] { HandsOnWheelProbe.class.getName(), IProbe.class.getName() },
                this.handsOnWheelProbe,
                null);
//TODO IHumanSensors correcto? La clase tiene las funciones de handsOnWheel y faceStatus
        String handsOnWheelProbeListenerFilter = "(objectclass=" + IHumanSensors.class.getName() + ")";
        context.addServiceListener(this.humanSensorsHealthCheckProbe, handsOnWheelProbeListenerFilter);
        
     // Add the Driver Status probe.
        this.driverStatusProbe = new DriverStatusProbe(context);
        context.registerService(
                new String[] { DriverStatusProbe.class.getName(), IProbe.class.getName() },
                this.driverStatusProbe,
                null);

        String driverStatusProbeListenerFilter = "(objectclass=" + IHumanSensors.class.getName() + ")";
        context.addServiceListener(this.driverStatusProbe, driverStatusProbeListenerFilter);
        
        //TODO Borrar si funciona
//        this.handsOnWheelProbe = new HandsOnWheelProbe(bundleContext);
//        
//        String handsOnWheelProbeListenerFilter = "(&(objectclass=" + IHumanSensors.class.getName() + ")(id=DriverSeatSensor))";
//        context.addServiceListener(this.handsOnWheelProbe, handsOnWheelProbeListenerFilter);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        context.removeServiceListener(this.drivingServiceProbe);
        this.drivingServiceProbe = null;

        context.removeServiceListener(this.engineHealthCheckProbe);
        this.engineHealthCheckProbe = null;

        context.removeServiceListener(this.fallbackPlanHealthCheckProbe);
        this.fallbackPlanHealthCheckProbe = null;

        context.removeServiceListener(this.humanSensorsHealthCheckProbe);
        this.humanSensorsHealthCheckProbe = null;

        context.removeServiceListener(this.frontDistanceSensorHealthCheckProbe);
        this.frontDistanceSensorHealthCheckProbe = null;

        context.removeServiceListener(this.rearDistanceSensorHealthCheckProbe);
        this.rearDistanceSensorHealthCheckProbe = null;

        context.removeServiceListener(this.leftDistanceSensorHealthCheckProbe);
        this.leftDistanceSensorHealthCheckProbe = null;

        context.removeServiceListener(this.rightDistanceSensorHealthCheckProbe);
        this.rightDistanceSensorHealthCheckProbe = null;

        context.removeServiceListener(this.leftLineSensorHealthCheckProbe);
        this.leftLineSensorHealthCheckProbe = null;

        context.removeServiceListener(this.rightLineSensorHealthCheckProbe);
        this.rightLineSensorHealthCheckProbe = null;

        context.removeServiceListener(this.notificationServiceHealthCheckProbe);
        this.engineHealthCheckProbe = null;

        context.removeServiceListener(this.roadContextProbe);
        this.roadContextProbe = null;

        context.removeServiceListener(this.steeringHealthCheckProbe);
        this.steeringHealthCheckProbe = null;
        
        context.removeServiceListener(this.driverSeatSensorProbe);
        this.driverSeatSensorProbe = null;
        
        context.removeServiceListener(this.copilotSeatSensorProbe);
        this.copilotSeatSensorProbe = null;
        
        context.removeServiceListener(this.handsOnWheelProbe);
        this.handsOnWheelProbe = null;
        
        context.removeServiceListener(this.driverStatusProbe);
        this.driverStatusProbe = null;

        Activator.context = null;
    }
}
