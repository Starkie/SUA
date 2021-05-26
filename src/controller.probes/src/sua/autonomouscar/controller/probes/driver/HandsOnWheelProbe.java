package sua.autonomouscar.controller.probes.driver;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.driver.IDriverStatusMonitor;
import sua.autonomouscar.devices.interfaces.IHandsOnWheelSensor;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.ISeatSensor;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class HandsOnWheelProbe implements IProbe<IHandsOnWheelSensor>, ServiceListener{
	private BundleContext context;

    public HandsOnWheelProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerMeasurement(IHandsOnWheelSensor sensor) {
        IDriverStatusMonitor monitor = OSGiUtils.getService(context, IDriverStatusMonitor.class);

        if (monitor == null) {
            return;
        }

        monitor.registerHandsOnWheelChange(sensor.areTheHandsOnTheSteeringWheel());
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
        case ServiceEvent.UNREGISTERING:
            this.registerMeasurement(OSGiUtils.getService(context, IHandsOnWheelSensor.class));

            break;
        default:
            break;
        }
    }
}
