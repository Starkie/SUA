package sua.autonomouscar.controller.monitors;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.monitors.car.DistanceSensorsHealthMonitor;
import sua.autonomouscar.controller.monitors.car.DrivingServiceMonitor;
import sua.autonomouscar.controller.monitors.car.EngineHealthMonitor;
import sua.autonomouscar.controller.monitors.car.LineSensorsHealthMonitor;
import sua.autonomouscar.controller.monitors.car.NotificationServiceHealthMonitor;
import sua.autonomouscar.controller.monitors.car.SteeringHealthMonitor;
import sua.autonomouscar.controller.monitors.road.IRoadContextMonitor;
import sua.autonomouscar.controller.monitors.road.RoadContextMonitor;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.LineSensorPosition;

public class Activator implements BundleActivator {

	private static BundleContext context;

    private ServiceRegistration<DrivingServiceMonitor> drivingServiceMonitorRegistration;
    private ServiceRegistration<DistanceSensorsHealthMonitor> frontDistanceSensorsHealthMonitorServiceRegistration;
    private ServiceRegistration<DistanceSensorsHealthMonitor> rearDistanceSensorsHealthMonitorServiceRegistration;
    private ServiceRegistration<DistanceSensorsHealthMonitor> leftDistanceSensorsHealthMonitorServiceRegistration;
    private ServiceRegistration<DistanceSensorsHealthMonitor> rightDistanceSensorsHealthMonitorServiceRegistration;
    private ServiceRegistration<EngineHealthMonitor> engineHealthMonitor;
    private ServiceRegistration<LineSensorsHealthMonitor> leftLineSensorHealthMonitorServiceRegistration;
    private ServiceRegistration<LineSensorsHealthMonitor> rightLineSensorHealthMonitorServiceRegistration;
    private ServiceRegistration<NotificationServiceHealthMonitor> notificationServiceHealthMonitorRegistration;
    private ServiceRegistration<IRoadContextMonitor> roadContextServiceRegistration;
    private ServiceRegistration<SteeringHealthMonitor> steeringContextServiceRegistration;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		DrivingServiceMonitor drivingServiceMonitor = new DrivingServiceMonitor(bundleContext);
        this.drivingServiceMonitorRegistration = context.registerService(DrivingServiceMonitor.class, drivingServiceMonitor, null);

        EngineHealthMonitor engineHealthMonitor = new EngineHealthMonitor(bundleContext);
        this.engineHealthMonitor = context.registerService(EngineHealthMonitor.class, engineHealthMonitor, null);

        // Front distance sensor.
        Dictionary<String, Object> frontDistanceSensorProps = new Hashtable<String, Object>();
        frontDistanceSensorProps.put("position", DistanceSensorPositon.FRONT);

        DistanceSensorsHealthMonitor frontDistanceSensorsHealthMonitor = new DistanceSensorsHealthMonitor(bundleContext, DistanceSensorPositon.FRONT);
        this.frontDistanceSensorsHealthMonitorServiceRegistration = context.registerService(DistanceSensorsHealthMonitor.class, frontDistanceSensorsHealthMonitor, frontDistanceSensorProps);

        // Rear distance sensor.
        Dictionary<String, Object> rearDistanceSensorProps = new Hashtable<String, Object>();
        rearDistanceSensorProps.put("position", DistanceSensorPositon.REAR);

        DistanceSensorsHealthMonitor rearDistanceSensorsHealthMonitor = new DistanceSensorsHealthMonitor(bundleContext, DistanceSensorPositon.REAR);
        this.rearDistanceSensorsHealthMonitorServiceRegistration = context.registerService(DistanceSensorsHealthMonitor.class, rearDistanceSensorsHealthMonitor, rearDistanceSensorProps);

        // Left distance sensor.
        Dictionary<String, Object> leftDistanceSensorProps = new Hashtable<String, Object>();
        leftDistanceSensorProps.put("position", DistanceSensorPositon.LEFT);

        DistanceSensorsHealthMonitor leftDistanceSensorsHealthMonitor = new DistanceSensorsHealthMonitor(bundleContext, DistanceSensorPositon.LEFT);
        this.leftDistanceSensorsHealthMonitorServiceRegistration = context.registerService(DistanceSensorsHealthMonitor.class, leftDistanceSensorsHealthMonitor, leftDistanceSensorProps);

        // Right distance sensor.
        Dictionary<String, Object> rightDistanceSensorProps = new Hashtable<String, Object>();
        rightDistanceSensorProps.put("position", DistanceSensorPositon.RIGHT);

        DistanceSensorsHealthMonitor rightDistanceSensorsHealthMonitor = new DistanceSensorsHealthMonitor(bundleContext, DistanceSensorPositon.RIGHT);
        this.rightDistanceSensorsHealthMonitorServiceRegistration = context.registerService(DistanceSensorsHealthMonitor.class, rightDistanceSensorsHealthMonitor, rightDistanceSensorProps);

        // Left line sensor.
        Dictionary<String, Object> leftLineSensorProps = new Hashtable<String, Object>();
        leftLineSensorProps.put("position", LineSensorPosition.LEFT);

        LineSensorsHealthMonitor leftLineSensorsHealthMonitor = new LineSensorsHealthMonitor(bundleContext, LineSensorPosition.LEFT);
        this.leftLineSensorHealthMonitorServiceRegistration = context.registerService(LineSensorsHealthMonitor.class, leftLineSensorsHealthMonitor, leftLineSensorProps);

        // Right line sensor.
        Dictionary<String, Object> rightLineSensorProps = new Hashtable<String, Object>();
        rightLineSensorProps.put("position", LineSensorPosition.RIGHT);

        LineSensorsHealthMonitor rightLineSensorsHealthMonitor = new LineSensorsHealthMonitor(bundleContext, LineSensorPosition.RIGHT);
        this.rightLineSensorHealthMonitorServiceRegistration = context.registerService(LineSensorsHealthMonitor.class, rightLineSensorsHealthMonitor, rightLineSensorProps);

        NotificationServiceHealthMonitor notificationServiceHealthMonitor = new NotificationServiceHealthMonitor(bundleContext);
        this.notificationServiceHealthMonitorRegistration = context.registerService(NotificationServiceHealthMonitor.class, notificationServiceHealthMonitor, null);

        IRoadContextMonitor roadContextMonitor = new RoadContextMonitor(bundleContext);
        this.roadContextServiceRegistration = context.registerService(IRoadContextMonitor.class, roadContextMonitor, null);

        SteeringHealthMonitor steeringContextMonitor = new SteeringHealthMonitor(bundleContext);
        this.steeringContextServiceRegistration = context.registerService(SteeringHealthMonitor.class, steeringContextMonitor, null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.drivingServiceMonitorRegistration.unregister();
		this.drivingServiceMonitorRegistration = null;

		this.engineHealthMonitor.unregister();
		this.engineHealthMonitor = null;

		this.frontDistanceSensorsHealthMonitorServiceRegistration.unregister();
        this.frontDistanceSensorsHealthMonitorServiceRegistration = null;

        this.rearDistanceSensorsHealthMonitorServiceRegistration.unregister();
        this.rearDistanceSensorsHealthMonitorServiceRegistration = null;

        this.leftDistanceSensorsHealthMonitorServiceRegistration.unregister();
        this.leftDistanceSensorsHealthMonitorServiceRegistration = null;

        this.rightDistanceSensorsHealthMonitorServiceRegistration.unregister();
        this.rightDistanceSensorsHealthMonitorServiceRegistration = null;

		this.leftLineSensorHealthMonitorServiceRegistration.unregister();
		this.leftDistanceSensorsHealthMonitorServiceRegistration = null;

		this.rightLineSensorHealthMonitorServiceRegistration.unregister();
        this.rightLineSensorHealthMonitorServiceRegistration = null;

		this.notificationServiceHealthMonitorRegistration.unregister();
		this.notificationServiceHealthMonitorRegistration = null;

		this.roadContextServiceRegistration.unregister();
		this.roadContextServiceRegistration = null;

		this.steeringContextServiceRegistration.unregister();
        this.steeringContextServiceRegistration = null;

		Activator.context = null;
	}
}
