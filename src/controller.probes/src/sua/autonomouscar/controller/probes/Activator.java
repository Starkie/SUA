package sua.autonomouscar.controller.probes;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.probes.car.DrivingServiceProbe;
import sua.autonomouscar.controller.probes.road.RoadContextProbe;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;

public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    private RoadContextProbe roadContextProbe;
    private DrivingServiceProbe drivingServiceProbe;

    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;

        // Add the IRoadSensor probe.
        this.roadContextProbe = new RoadContextProbe(context);
        context.registerService(
                new String[] { RoadContextProbe.class.getName(), IProbe.class.getName() },
                roadContextProbe, 
                null);
        
        String roadContextProbeListenerFilter = "(objectclass=" + IRoadSensor.class.getName() + ")";
        context.addServiceListener(this.roadContextProbe, roadContextProbeListenerFilter);
        
        // Add the Driving Service probe.
        this.drivingServiceProbe = new DrivingServiceProbe(context);
        context.registerService(
                new String[] { DrivingServiceProbe.class.getName(), IProbe.class.getName() },
                drivingServiceProbe, 
                null);

        String drivingServiceProbeListenerFilter = "(objectclass=" + IDrivingService.class.getName() + ")";
        context.addServiceListener(this.drivingServiceProbe, drivingServiceProbeListenerFilter);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        this.context.removeServiceListener(roadContextProbe);
        this.roadContextProbe = null;
        
        this.context.removeServiceListener(drivingServiceProbe);
        this.drivingServiceProbe = null;
        
        Activator.context = null;
    }
}
