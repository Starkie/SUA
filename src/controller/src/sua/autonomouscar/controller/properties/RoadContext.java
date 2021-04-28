package sua.autonomouscar.controller.properties;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.interfaces.IKnowledge;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * The adaption property that represents the road context. Includes the values
 * of the status and type.
 */
public class RoadContext implements IKnowledge {
    private static final String STATUS = "status";
    private static final String TYPE = "type";

    private final BundleContext context;

    private final Dictionary<String, Object> properties;
    private final ServiceRegistration<?> serviceRegistration;

    /**
     * Creates a new instance of the class {@link RoadContext}. It alsor registers
     * the property.
     * 
     * @param context The context to register the property.
     * @param status  The initial road status.
     * @param type    The initial road type.
     */
    public RoadContext(BundleContext context, ERoadStatus status, ERoadType type) {
        this.context = context;

        this.properties = new Hashtable<>();

        String[] implementedInterfaces = new String[] { RoadContext.class.getName(), IKnowledge.class.getName() };
        this.serviceRegistration = context.registerService(implementedInterfaces, this, this.properties);

        this.setStatus(status);
        this.setType(type);
    }

    public ERoadType getType() {
        return (ERoadType) properties.get(TYPE);
    }

    public ERoadStatus getStatus() {
        return (ERoadStatus) properties.get(STATUS);
    }

    public void setType(ERoadType type) {
        if (type != null && type != this.getType()) {
            updateProperty(TYPE, type);
        }
    }

    public void setStatus(ERoadStatus status) {
        if (status != null && status != this.getStatus()) {
            updateProperty(STATUS, status);
        }
    }

    /**
     * Updates the properties on the {@link ServiceRegistration} of the current
     * service.
     * 
     * @param propertyName The name of the property to update.
     * @param value        The new value of the property.
     */
    protected void updateProperty(String propertyName, Object value) {
        this.properties.put(propertyName, value);
        this.serviceRegistration.setProperties(properties);
    }
}
