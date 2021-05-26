package sua.autonomouscar.controller.probes.driver;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.driver.ISeatStatusMonitor;
import sua.autonomouscar.devices.interfaces.ISeatSensor;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class SeatSensorProbe implements IProbe<ISeatSensor>, ServiceListener{
	private BundleContext context;
	private String seatSensorMonitorClassName;

    public SeatSensorProbe(BundleContext context, String seatSensorMonitorClassName) {
        this.context = context;
		this.seatSensorMonitorClassName = seatSensorMonitorClassName;
    }

    @Override
    public void registerMeasurement(ISeatSensor sensor) {
    	ServiceReference<?> monitorServiceReference = this.context.getServiceReference(this.seatSensorMonitorClassName);
    	
    	if (monitorServiceReference == null)
    	{
    		return;
    	}
    
        ISeatStatusMonitor monitor = (ISeatStatusMonitor) this.context.getService(monitorServiceReference);

        if (monitor == null) {
            return;
        }

        monitor.registerSeatChange(sensor.isSeatOccuppied());
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
        case ServiceEvent.UNREGISTERING:
            ServiceReference<?> serviceReference = event.getServiceReference();
            ISeatSensor sensor = (ISeatSensor) this.context.getService(serviceReference);
			this.registerMeasurement(sensor);

            break;
        default:
            break;
        }
    }
}
