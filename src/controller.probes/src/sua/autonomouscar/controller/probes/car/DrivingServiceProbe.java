package sua.autonomouscar.controller.probes.car;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IProbe;
import sua.autonomouscar.controller.monitors.car.DrivingServiceMonitor;
import sua.autonomouscar.controller.probes.utils.DrivingServiceUtils;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class DrivingServiceProbe implements IProbe<IDrivingService>, ServiceListener {
    private BundleContext context;

    public DrivingServiceProbe(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerMeasurement(IDrivingService drivingService) {
        DrivingServiceMonitor drivingServiceMonitor = OSGiUtils.getService(context, DrivingServiceMonitor.class);

        if (drivingService != null && drivingService.isDriving())
        {
            DrivingAutonomyLevel autonomyLevel = GetAutonomyLevel(drivingService);
            Class drivingServiceClass = drivingService.getClass();

            drivingServiceMonitor.registerAutonomyLevelChange(autonomyLevel, drivingServiceClass);
        }
    }


    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType()) {
        case ServiceEvent.REGISTERED:
        case ServiceEvent.MODIFIED:
            IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);

            this.registerMeasurement(currentDrivingService);

            break;
        default:
            break;
        }
    }

    private DrivingAutonomyLevel GetAutonomyLevel(IDrivingService drivingService) {
        if (DrivingServiceUtils.isL0DrivingService(drivingService))
        {
            return DrivingAutonomyLevel.L0;
        }
        else if (DrivingServiceUtils.isL1DrivingService(drivingService))
        {
            return DrivingAutonomyLevel.L1;
        }
        else if (DrivingServiceUtils.isL2DrivingService(drivingService))
        {
            return DrivingAutonomyLevel.L2;
        }
        else if (DrivingServiceUtils.isL3DrivingService(drivingService))
        {
            return DrivingAutonomyLevel.L3;
        }

        return null;
    }
}
