package sua.autonomouscar.controller.interfaces;

/**
 * The interface to implement a health status monitor.
 */
public interface IHealthMonitor {

    /**
     * Registers the health status of a component.
     * @param isComponentAvailable A value indicating whether the component is available.
     */
    void registerHealthCheck(boolean isComponentAvailable);
}