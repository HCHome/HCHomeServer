package HCHomeServer.listener;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.*;
import org.springframework.stereotype.Component;

import HCHomeServer.cache.UnReadCount;

@Component
public class ApplicationEventListener implements ApplicationListener<ContextClosedEvent> {

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		if(UnReadCount.getInstance()!=null)
    		UnReadCount.getInstance().saveMap();	
	}

}
