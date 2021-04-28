package sua.autonomouscar.controller.properties;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.interfaces.IKnowledge;

/**
 * Base class for implementing adaption properties.
 *
 */
public abstract class KnowledgeBase implements IKnowledge {

    private final BundleContext context;

    protected final Dictionary<String, Object> properties;
    private final ServiceRegistration<?> serviceRegistration;

    /**
     * Creates a new instance of the class {@link RoadContext}. It also registers
     * the property.
     * 
     * @param context The context to register the property.
     * @param status  The initial road status.
     * @param type    The initial road type.
     */
    public KnowledgeBase(BundleContext context) {
        this.context = context;

        this.properties = new Hashtable<>();

        String[] implementedInterfaces = new String[] { RoadContext.class.getName(), IKnowledge.class.getName() };
        this.serviceRegistration = this.context.registerService(implementedInterfaces, this, this.properties);
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
