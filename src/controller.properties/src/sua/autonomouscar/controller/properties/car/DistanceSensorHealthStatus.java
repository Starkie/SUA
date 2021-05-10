package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;

/**
 * Represents the status of the {@link IDistanceSensor}.
 */
public class DistanceSensorHealthStatus extends HealthStatusBase {
    private static final String ACTIVE_SENSOR_ID = "activeSensorId";
    private static final String BEST_DISTANCE_SENSOR_AVAILABLE = "bestDistanceSensorAvailable";

    public DistanceSensorHealthStatus(BundleContext context, DistanceSensorPositon position) {
        super(context);

        this.addImplementedInterface(DistanceSensorHealthStatus.class.getName());

        this.properties.put("position", position);
    }

    /**
     * Updates the identifier of the best distance sensor available.
     * Used to track which is the best distance sensor for this {@link DistanceSensorPositon}.
     * @param distanceSensorClass The identifier of the distance sensor.
     */
    public void setBestDistanceSensorId(String distanceSensorId)
    {
        this.updateProperty(BEST_DISTANCE_SENSOR_AVAILABLE, distanceSensorId);
    }

    /**
     * Returns the identifier of the best distance sensor available for this {@link DistanceSensorPositon}.
     * @return The identifier of the distance sensor.
     */
    public String getBestDistanceSensorId()
    {
        try
        {
            return (String) this.properties.get(BEST_DISTANCE_SENSOR_AVAILABLE);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Updates the identifier of the last active distance sensor.
     * @param distanceSensorClass The identifier of the distance sensor.
     */
    public void setActiveDistanceSensorId(String distanceSensorId)
    {
        this.updateProperty(ACTIVE_SENSOR_ID, distanceSensorId);
    }

    /**
     * Returns the identifier of the last active distance sensor for this {@link DistanceSensorPositon}.
     * @return The identifier of the distance sensor.
     */
    public String getActiveDistanceSensorId()
    {
        try
        {
            return (String) this.properties.get(ACTIVE_SENSOR_ID);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
