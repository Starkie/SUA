package sua.autonomouscar.controller.monitors.road;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

public class RoadContextMonitor implements IRoadContextMonitor {
    private BundleContext context;

    public RoadContextMonitor(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerStatusChange(ERoadStatus status) {
        RoadContext roadContext = OSGiUtils.getService(context, RoadContext.class);

        // Only update it if the value has changed.
        if (roadContext != null && roadContext.getStatus() != status) {
            System.out.println("[Road Context Monitor] -  Updating the Road Status to " + status);

            roadContext.setStatus(status);
        }
    }

    @Override
    public void registerTypeChange(ERoadType type) {
        RoadContext roadContext = OSGiUtils.getService(context, RoadContext.class);

        // Only update it if the value has changed.
        if (roadContext != null && roadContext.getType() != type) {
            System.out.println("[Road Context Monitor] -  Updating the Road Type to " + type);

            roadContext.setType(type);
        }
    }
}
