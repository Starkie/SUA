package sua.autonomouscar.controller.properties;

import org.osgi.framework.BundleContext;

/**
 * Base class for implement health status properties.
 */
public abstract class HealthStatusBase extends KnowledgeBase {

    private static final String IS_AVAILABLE = "is_available";

    protected HealthStatusBase(BundleContext context) {
        super(context);
    }
    
    /**
     * Indicates if the component is currently available.
     * @return True if it is available. Otherwise, returns false.
     */
    public boolean isAvailable() {
        return (boolean) this.properties.get(IS_AVAILABLE);
    }

    /**
     * Updates the status of the component.
     */
    public void setIsAvailable(boolean value) {
        if (value != this.isAvailable()) {
            updateProperty(IS_AVAILABLE, value);
        }
    }  
}
