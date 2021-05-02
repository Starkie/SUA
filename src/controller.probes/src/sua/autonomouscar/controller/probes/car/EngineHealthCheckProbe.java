package sua.autonomouscar.controller.probes.car;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.car.EngineHealthMonitor;
import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class EngineHealthCheckProbe implements IProbe<IEngine>, ServiceListener {

    private BundleContext context;

    public EngineHealthCheckProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
        case ServiceEvent.UNREGISTERING:
            this.registerMeasurement(OSGiUtils.getService(context, IEngine.class));

            break;
        default:
            break;
        }
    }

    @Override
    public void registerMeasurement(IEngine component) {
        EngineHealthMonitor engineHealthMonitor = OSGiUtils.getService(context, EngineHealthMonitor.class);
        
        if (engineHealthMonitor == null)
        {
            return;
        }
        
        engineHealthMonitor.registerHealthCheck(component != null);
    }
}