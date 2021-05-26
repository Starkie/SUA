package sua.autonomouscar.controller.probes.driver;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.driver.IDriverStatusMonitor;
import sua.autonomouscar.devices.interfaces.IFaceMonitor;
import sua.autonomouscar.devices.interfaces.IHumanSensors;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.EFaceStatus;

public class DriverStatusProbe implements IProbe<IFaceMonitor>, ServiceListener{
	private BundleContext context;

    public DriverStatusProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerMeasurement(IFaceMonitor sensor) {
        IDriverStatusMonitor monitor = OSGiUtils.getService(context, IDriverStatusMonitor.class);

        if (monitor == null) {
            return;
        }

        EFaceStatus driverStatus = EFaceStatus.UNKNOWN;

        if (sensor != null)
        {
            driverStatus = sensor.getFaceStatus();
        }

        monitor.registerDriverStatusChange(driverStatus);
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
        case ServiceEvent.UNREGISTERING:
            this.registerMeasurement(OSGiUtils.getService(context, IFaceMonitor.class));

            break;
        default:
            break;
        }
    }
}
