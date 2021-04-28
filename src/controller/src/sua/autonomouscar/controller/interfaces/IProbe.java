package sua.autonomouscar.controller.interfaces;

/**
 * The interface of a probe. Used to measure parameters of the controlled system
 * or its context.
 *
 * @param <T> The type of the measurement obtained.
 */
public interface IProbe<T> {
    /**
     * Obtains a measurement from the given sensor. If the value is relevant, it
     * might register it to its associated monitor.
     */
    void registerMeasurement(T sensor);
}
