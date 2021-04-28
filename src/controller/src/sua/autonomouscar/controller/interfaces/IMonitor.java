package sua.autonomouscar.controller.interfaces;

/**
 * Indicates that the component monitors the changes on properties of the type
 * {@link T}.
 */
public interface IMonitor<T> {

    /**
     * Method to be called to register the value from the probe. If it is relevant,
     * it might update the value of a knowledge property.
     * 
     * @param value
     */
    void registerChange(T value);
}
