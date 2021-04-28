package sua.autonomouscar.controller.properties;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * The adaption property that represents the road context. Includes the values
 * of the status and type.
 */
public class RoadContext extends KnowledgeBase {
    private static final String STATUS = "status";
    private static final String TYPE = "type";

    /**
     * Creates a new instance of the class {@link RoadContext}. It also registers
     * the property.
     * 
     * @param context The context to register the property.
     * @param status  The initial road status.
     * @param type    The initial road type.
     */
    public RoadContext(BundleContext context, ERoadStatus status, ERoadType type) {
        super(context);

        this.setStatus(status);
        this.setType(type);
    }

    public ERoadType getType() {
        return (ERoadType) this.properties.get(TYPE);
    }

    public ERoadStatus getStatus() {
        return (ERoadStatus) this.properties.get(STATUS);
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
}
