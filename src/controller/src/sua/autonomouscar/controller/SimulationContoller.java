package sua.autonomouscar.controller;

import java.util.List;

import sua.autonomouscar.controller.interfaces.IAdaptionRule;
import sua.autonomouscar.controller.interfaces.IMonitor;
import sua.autonomouscar.simulation.interfaces.ISimulationElement;

/**
 * An extension of the controller that works as a {@link ISimulationElement}.
 * Every simulation step will execute a controller iteration.
 */
public class SimulationContoller extends Controller implements ISimulationElement {

	public SimulationContoller(List<IMonitor> monitors, List<IAdaptionRule> rules) {
		super(monitors, rules);
	}

	@Override
	public void onSimulationStep(Integer step, long time_lapse_millis) {
		this.execute();
	}
}
