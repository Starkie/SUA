package ads.l2.adaptivecruisecontrol;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import ADS.interfaces.IL2_ACC;
import devices.interfaces.ESensorPosition;
import devices.interfaces.IDistanceSensor;
import devices.interfaces.IEngine;

public class ACC implements IL2_ACC {
	
	protected boolean isStarted = false;
	protected BundleContext context = null;
	protected IEngine engine = null;
	protected IDistanceSensor sensorDistancia = null;
	
	public ACC(BundleContext context) {
		this.context = context;
	}

	@Override
	public void start() {
		System.out.println("[ACC] Starting ...");
		this.isStarted = true;
		this.context.registerService(IL2_ACC.class, this, null);
		
		ServiceReference ref = this.context.getServiceReference(IEngine.class);
		if ( ref != null ) {
			System.out.println("[ACC] Updating Engine RPM ...");
			engine = (IEngine) this.context.getService(ref);
			System.out.println("[ACC] The engine current RPM: " + engine.getRPM());
		} else {
			System.out.println("[ACC] Engine not found!");	
		}
		
		String filtroSensorDistFrontal = "(position=" + ESensorPosition.FRONT + ")";
		try {
			ServiceReference[] refs = this.context.getServiceReferences(IDistanceSensor.class.getName(), filtroSensorDistFrontal);
			if ( refs == null ) {
				System.out.println("[ACC] No front distance sensor found!");
			} else {
				this.sensorDistancia = (IDistanceSensor) this.context.getService(refs[0]);
				
				
				int distanciaFrontal = this.sensorDistancia.getDistance();
				System.out.println("[ACC] Front distance (m): " + distanciaFrontal);
				if ( distanciaFrontal > 150 && this.engine != null ) {
					this.engine.setRPM(this.engine.getRPM() + 50);
				} else if ( distanciaFrontal < 100 && this.engine != null ) {
					this.engine.setRPM(this.engine.getRPM() - 500);
				}
			 
			}
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void stop() {
		System.out.println("[ACC] Stopping ...");
		this.isStarted = false;
		
	}

	@Override
	public boolean isStarted() {
		return this.isStarted;
	}

}
