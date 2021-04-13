package contexto.carretera;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import context.interfaces.IRoadContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected IRoadContext contextoCarretera = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		this.contextoCarretera = new ContextoCarretera(bundleContext);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
