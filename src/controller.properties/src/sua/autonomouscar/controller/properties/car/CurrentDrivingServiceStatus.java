package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.KnowledgeBase;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;

public class CurrentDrivingServiceStatus extends KnowledgeBase {
    private static final String DRIVING_SERVICE = "drivingServiceClass";
    private static final String AUTONOMY_LEVEL = "autonomyLevel";

    public CurrentDrivingServiceStatus(BundleContext context) {
        super(context);

        this.addImplementedInterface(CurrentDrivingServiceStatus.class.getName());
    }

    public DrivingAutonomyLevel getAutonomyLevel() {
        return (DrivingAutonomyLevel) this.properties.get(AUTONOMY_LEVEL);
    }

    public Class getDrivingServiceClass() {
        return (Class) this.properties.get(DRIVING_SERVICE);
    }

    public void setAutonomyLevel(DrivingAutonomyLevel autonomyLevel, Class drivingServiceClass) {
        if (autonomyLevel != null && autonomyLevel != this.getAutonomyLevel()
            && drivingServiceClass != null && drivingServiceClass != this.getDrivingServiceClass()) {
            updateProperty(AUTONOMY_LEVEL, autonomyLevel, false);
            updateProperty(DRIVING_SERVICE, drivingServiceClass, true);
        }
    }

    public void setDrivingService(Class drivingServiceClass) {

    }
}
