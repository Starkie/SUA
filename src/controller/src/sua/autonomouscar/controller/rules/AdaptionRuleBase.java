package sua.autonomouscar.controller.rules;

import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IAdaptionRule;

public abstract class AdaptionRuleBase implements ServiceListener, IAdaptionRule {

    @Override
    public void serviceChanged(ServiceEvent event) {
        switch (event.getType())
        {
            // By default, evaluate any change in the knowledge property.
            case ServiceEvent.REGISTERED:
            case ServiceEvent.MODIFIED:
            case ServiceEvent.UNREGISTERING:
                this.evaluateAndExecute();
            default:
                return;
        }
    }
}
