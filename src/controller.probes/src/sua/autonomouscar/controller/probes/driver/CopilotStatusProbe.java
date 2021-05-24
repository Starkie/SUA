package sua.autonomouscar.controller.probes.driver;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.driver.ICopilotStatusMonitor;
import sua.autonomouscar.devices.interfaces.ICopilotSensor;
import sua.autonomouscar.devices.interfaces.IDriverSensor;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class CopilotStatusProbe implements IProbe<ICopilotSensor>, ServiceListener{
	private BundleContext context;

    public CopilotStatusProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerMeasurement(ICopilotSensor sensor) {
        ICopilotStatusMonitor monitor = OSGiUtils.getService(context, ICopilotStatusMonitor.class);

        if (monitor == null) {
            return;
        }

        monitor.registerCopilotSeatChange(sensor.isCopilotSeatOccupied());

    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
            this.registerMeasurement(OSGiUtils.getService(context, ICopilotSensor.class));

            break;
        default:
            break;
        }
    }
}
