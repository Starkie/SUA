package device.engine;

import devices.interfaces.IEngine;

public class Engine implements IEngine {

	protected int rpm = 0;
	
	@Override
	public void setRPM(int rpm) {
		this.rpm = rpm;
		System.out.println("[Engine] Setting to " + rpm + " rpm");
	}

	@Override
	public int getRPM() {
		return this.rpm;
	}

}
