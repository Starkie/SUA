package controlador.tipocarretera;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import ADS.interfaces.IL2_ACC;
import context.interfaces.ERoadType;
import context.interfaces.IRoadContext;

public class ControladorTipoCarretera implements ServiceListener {
	
	protected BundleContext context = null;
	
	public ControladorTipoCarretera(BundleContext context) {
		this.context = context;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {

		IRoadContext contextoCarretera = (IRoadContext) this.context.getService(event.getServiceReference());
		ERoadType tipoCarretera = contextoCarretera.getType();
		
		ServiceReference ref = this.context.getServiceReference(IL2_ACC.class);
		if ( ref == null ) {
			System.out.println("[Controlador] No ACC found, nothing to do!");
			return;
		}
		
		IL2_ACC theACC = (IL2_ACC) this.context.getService(ref);
		
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			
				switch (tipoCarretera) {
				case HIGHWAY:
					
					if ( !theACC.isStarted() ) {
						System.out.println("[Controlador] Auto-starting the ACC when in a HIGHWAY");
						theACC.start();
					}
					
					break;

				default:
					
					if ( theACC.isStarted() ) {
						System.out.println("[Controlador] De-activating the ACC when NOT in a HIGHWAY");
						theACC.stop();
					}
					
					break;
				}
			
			break;

		default:
			break;
		}
		
	}

}
