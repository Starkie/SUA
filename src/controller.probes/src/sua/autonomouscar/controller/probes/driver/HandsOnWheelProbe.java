package sua.autonomouscar.controller.probes.driver;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.driver.IDriverStatusMonitor;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.devices.interfaces.ISeatSensor;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class HandsOnWheelProbe implements IProbe<IHumanSensors>, ServiceListener{
	private BundleContext context;

    public HandsOnWheelProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerMeasurement(IHumanSensors sensor) {
        IDriverStatusMonitor monitor = OSGiUtils.getService(context, IDriverStatusMonitor.class);

        if (monitor == null) {
            return;
        }

        monitor.registerHandsOnWheelChange(sensor.areTheHandsOnTheWheel());
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
        case ServiceEvent.UNREGISTERING:
            this.registerMeasurement(OSGiUtils.getService(context, IHumanSensors.class));

//        	ServiceReference<?> serviceReference = event.getServiceReference();
//        	IHumanSensors sensor = (IHumanSensors) this.context.getService(serviceReference);
//			this.registerMeasurement(sensor);
        	
            break;
        default:
            break;
        }
    }
}
