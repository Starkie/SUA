package sua.autonomouscar.controller.properties;

import org.osgi.framework.BundleContext;

public class CurrentDrivingServiceStatus extends KnowledgeBase {
    private static final String AUTONOMY_LEVEL = "autonomyLevel";

    public CurrentDrivingServiceStatus(BundleContext context) {
        super(context);
        
        this.addImplementedInterface(CurrentDrivingServiceStatus.class.getName());
        
        this.registerKnowledge();
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
