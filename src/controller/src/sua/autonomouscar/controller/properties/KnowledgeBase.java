package sua.autonomouscar.controller.properties;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.interfaces.IKnowledge;

/**
 * Base class for implementing adaption properties.
 *
 */
public abstract class KnowledgeBase implements IKnowledge {

    private final BundleContext context;
    private ServiceRegistration<?> serviceRegistration;

    private List<String> implementedInterfaces;
    protected final Dictionary<String, Object> properties;


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

        this.implementedInterfaces = new ArrayList<String>();
        this.implementedInterfaces.add(KnowledgeBase.class.getName());
        this.implementedInterfaces.add(IKnowledge.class.getName());
    }

    public ServiceRegistration<?> registerKnowledge() {
        this.serviceRegistration = this.context.registerService(implementedInterfaces.toArray(new String[0]), this, this.properties);

        return this.serviceRegistration;
    }

    /**
     * Updates the properties on the {@link ServiceRegistration} of the current
     * service.
     *
     * @param propertyName The name of the property to update.
     * @param value        The new value of the property.
     */
    protected void updateProperty(String propertyName, Object value) {
        this.updateProperty(propertyName, value, true);
    }

    /**
     * Updates the properties on the {@link ServiceRegistration} of the current
     * service.
     *
     * @param propertyName The name of the property to update.
     * @param value        The new value of the property.
     * @param notifyListeners Value indicating whether the listeners should be notified of this change. Useful for when more changes to the properties must be done, and the listeners should not be notified yet.
     */
    protected void updateProperty(String propertyName, Object value, boolean notifyListeners) {
        this.properties.put(propertyName, value);

        if (notifyListeners)
        {
            this.serviceRegistration.setProperties(properties);
        }
    }

    protected void addImplementedInterface(String interfaceName) {
        this.implementedInterfaces.add(interfaceName);
    }
}
