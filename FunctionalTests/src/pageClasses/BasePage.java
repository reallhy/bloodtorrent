package pageClasses;

import net.sf.sahi.client.Browser;

public class BasePage {
	protected Browser browser;
	
	public BasePage(Browser browser) {
		this.browser = browser;
	}
}
