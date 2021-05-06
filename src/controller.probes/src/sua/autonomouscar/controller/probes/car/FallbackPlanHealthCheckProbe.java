package sua.autonomouscar.controller.probes.car;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.car.FallBackPlanHealthMonitor;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class FallbackPlanHealthCheckProbe implements IProbe<IFallbackPlan>, ServiceListener {

    private BundleContext context;

    public FallbackPlanHealthCheckProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
            case ServiceEvent.REGISTERED:
            case ServiceEvent.MODIFIED:
            case ServiceEvent.UNREGISTERING:
                this.registerMeasurement(OSGiUtils.getService(context, IFallbackPlan.class));

                break;
            default:
                break;
        }
    }

    @Override
    public void registerMeasurement(IFallbackPlan component) {
        FallBackPlanHealthMonitor fallbackPlanHealthMonitor = OSGiUtils.getService(context, FallBackPlanHealthMonitor.class);

        if (fallbackPlanHealthMonitor == null)
        {
            return;
        }

        fallbackPlanHealthMonitor.registerHealthCheck(component != null);
    }
}
