package havis.app.rfidhardwaremonitor.ui.client;

import havis.app.rfidhardwaremonitor.rest.async.RFIDHardwareMonitorServiceAsync;
import havis.app.rfidhardwaremonitor.ui.client.sections.InventoryPresenter;
import havis.app.rfidhardwaremonitor.ui.client.sections.InventorySection;
import havis.app.rfidhardwaremonitor.ui.resourcebundle.AppResources;
import havis.net.ui.shared.client.event.MessageEvent.MessageType;
import havis.net.ui.shared.client.widgets.CustomMessageWidget;
import havis.net.ui.shared.resourcebundle.ResourceBundle;

import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class WebUI extends Composite implements EntryPoint {

	private static WebUIUiBinder uiBinder = GWT.create(WebUIUiBinder.class);

	@UiTemplate("WebUI.ui.xml")
	interface WebUIUiBinder extends UiBinder<Widget, WebUI> {
	}

	@UiField
	InventorySection tags;

	private ResourceBundle res = ResourceBundle.INSTANCE;
	private AppResources appRes = AppResources.INSTANCE;

	private RFIDHardwareMonitorServiceAsync service = GWT
			.create(RFIDHardwareMonitorServiceAsync.class);

	private InventoryPresenter inventoryPresenter;

	public WebUI() {
		initWidget(uiBinder.createAndBindUi(this));
		Defaults.setDateFormat(null);
		ensureInjection();

		inventoryPresenter = new InventoryPresenter(tags);
	}

	@Override
	public void onModuleLoad() {
		RootLayoutPanel.get().add(this);

		// TODO: Disable UI until async call is done
		service.isObserving(new MethodCallback<Boolean>() {

			@Override
			public void onSuccess(Method method, Boolean response) {
				if (response) {
					tags.getMonitorButton().setValue(true, false);
					inventoryPresenter.onObserve();
				}
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				CustomMessageWidget.show("Could not check the app's state", MessageType.WARNING);
			}
		});
	}

	private void ensureInjection() {
		res.css().ensureInjected();
		appRes.css().ensureInjected();
	}
}