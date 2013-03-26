import net.sf.sahi.client.Browser;
import net.sf.sahi.client.BrowserCondition;
import net.sf.sahi.client.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.twist.core.execution.TwistScenarioDataStore;

import pageClasses.MapForSavingLocation;

import static com.thoughtworks.twist.core.execution.TwistVerification.verifyEquals;
import static junit.framework.Assert.assertTrue;

public class OnMapPage {

	private Browser browser;

//	@Autowired
//	private TwistScenarioDataStore scenarioStore;
	private MapForSavingLocation map;

	public OnMapPage(Browser browser) {
		this.browser = browser;
		map = new MapForSavingLocation(browser);  
	}

	public void verifyDisplayedMap() throws Exception {
		assertTrue(map.isLocationPinVisible());
	}
	
	public void cancelMap() throws Exception {
		browser.button("Cancel").near(browser.button("Save")).click();
	}

	public void verifyMessageForCancel() throws Exception {
		verifyEquals(map.returnAlertText(), "Do you wish to proceed without specifying exact location?");
	}

	public void saveLocation() throws Exception {
		browser.button("Save").click();
	}
}