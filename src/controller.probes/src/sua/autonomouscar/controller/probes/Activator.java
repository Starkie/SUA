package sua.autonomouscar.controller.probes;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.probes.car.DrivingServiceProbe;
import sua.autonomouscar.controller.probes.car.EngineHealthCheckProbe;
import sua.autonomouscar.controller.probes.car.LineSensorHealthCheckProbe;
import sua.autonomouscar.controller.probes.car.NotificationServiceHealthCheckProbe;
import sua.autonomouscar.controller.probes.road.RoadContextProbe;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.interaction.interfaces.INotificationService;

public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    private RoadContextProbe roadContextProbe;
    private DrivingServiceProbe drivingServiceProbe;
    private EngineHealthCheckProbe engineHealthCheckProbe;
    private LineSensorHealthCheckProbe leftLineSensorHealthCheckProbe;
    private LineSensorHealthCheckProbe rightLineSensorHealthCheckProbe;
    private NotificationServiceHealthCheckProbe notificationServiceHealthCheckProbe;

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
        
        // Add the Left Line Sensor Health Check probe.
        this.leftLineSensorHealthCheckProbe = new LineSensorHealthCheckProbe(context, LineSensorPosition.LEFT);
        context.registerService(
                new String[] { LineSensorHealthCheckProbe.class.getName(), IProbe.class.getName() },
                this.leftLineSensorHealthCheckProbe,
                null);

        String leftLineSensorProbeListenerFilter = "(&(objectclass=" + ILineSensor.class.getName() + ")(id=" + LineSensorPosition.LEFT.getSensorId() + "))";
        context.addServiceListener(this.leftLineSensorHealthCheckProbe, leftLineSensorProbeListenerFilter);
        
        // Add the Left Line Sensor Health Check probe.
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
    }

    public void stop(BundleContext bundleContext) throws Exception {
        context.removeServiceListener(this.drivingServiceProbe);
        this.drivingServiceProbe = null;

        context.removeServiceListener(this.engineHealthCheckProbe);
        this.engineHealthCheckProbe = null;
        
        context.removeServiceListener(this.leftLineSensorHealthCheckProbe);
        this.leftLineSensorHealthCheckProbe = null;
        
        context.removeServiceListener(this.rightLineSensorHealthCheckProbe);
        this.rightLineSensorHealthCheckProbe = null;

        context.removeServiceListener(this.notificationServiceHealthCheckProbe);
        this.engineHealthCheckProbe = null;

        context.removeServiceListener(this.roadContextProbe);
        this.roadContextProbe = null;

        Activator.context = null;
    }
}
