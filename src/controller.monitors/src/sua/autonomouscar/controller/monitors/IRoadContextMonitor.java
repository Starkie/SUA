package sua.autonomouscar.controller.monitors;

import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;

public interface IRoadContextMonitor {

    void registerStatusChange(ERoadStatus status);

    void registerTypeChange(ERoadType status);

}