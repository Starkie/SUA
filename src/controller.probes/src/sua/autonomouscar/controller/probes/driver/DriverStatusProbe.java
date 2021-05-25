package sua.autonomouscar.controller.probes.driver;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.driver.IDriverStatusMonitor;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class DriverStatusProbe implements IProbe<IHumanSensors>, ServiceListener{
	private BundleContext context;

    public DriverStatusProbe(BundleContext context) {    	
        this.context = context;
    }

    @Override
    public void registerMeasurement(IHumanSensors sensor) {
        IDriverStatusMonitor monitor = OSGiUtils.getService(context, IDriverStatusMonitor.class);

        if (monitor == null) {
            return;
        }

        monitor.registerDriverStatusChange(sensor.getFaceStatus());
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
            this.registerMeasurement(OSGiUtils.getService(context, IHumanSensors.class));

            break;
        default:
            break;
        }
    }
}
