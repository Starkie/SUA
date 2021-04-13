package controlador.tipocarretera;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import context.interfaces.IRoadContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected ControladorTipoCarretera controlador = null;
	
	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.controlador = new ControladorTipoCarretera(bundleContext);
		
		String listenerFiltro = "(objectclass=" + IRoadContext.class.getName() + ")";
		this.context.addServiceListener(controlador, listenerFiltro);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.context.removeServiceListener(controlador);
		Activator.context = null;
	}

}
