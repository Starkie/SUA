package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.KnowledgeBase;

public class CurrentDrivingServiceStatus extends KnowledgeBase {
    private static final String AUTONOMY_LEVEL = "autonomyLevel";

    public CurrentDrivingServiceStatus(BundleContext context) {
        super(context);
        
        this.addImplementedInterface(CurrentDrivingServiceStatus.class.getName());
    }
    
    public DrivingAutonomyLevel getAutonomyLevel() {
        return (DrivingAutonomyLevel) this.properties.get(AUTONOMY_LEVEL);
    }

    public void setAutonomyLevel(DrivingAutonomyLevel autonomyLevel) {
        if (autonomyLevel != null && autonomyLevel != this.getAutonomyLevel()) {
            updateProperty(AUTONOMY_LEVEL, autonomyLevel);
        }
    }    
}
