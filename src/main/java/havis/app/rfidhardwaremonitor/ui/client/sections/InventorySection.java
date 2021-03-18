package havis.app.rfidhardwaremonitor.ui.client.sections;

import havis.app.rfidhardwaremonitor.ui.resourcebundle.AppResources;
import havis.net.ui.shared.client.list.WidgetList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class InventorySection extends Composite implements InventoryPresenter.View {

	private static final String[] HEADER_LABELS = new String[] { "EPC", "RSSI Antenna 1",
			"RSSI Antenna 2" };

	private static InventorySectionUiBinder uiBinder = GWT.create(InventorySectionUiBinder.class);

	interface InventorySectionUiBinder extends UiBinder<Widget, InventorySection> {
	}

	@UiField
	HTMLPanel monitorTagCounter;

	@UiField
	ToggleButton monitorButton;

	@UiField
	InlineLabel tagCount;

	@UiField
	WidgetList tagList;

	private AppResources res = AppResources.INSTANCE;
	private InventoryPresenter presenter;

	public InventorySection() {
		initWidget(uiBinder.createAndBindUi(this));
		res.css().ensureInjected();

		setListHeader();
		monitorTagCounter.setStyleName(res.css().observe(), true);
	}

	private void setListHeader() {
		for (String item : HEADER_LABELS) {
			tagList.addHeaderCell(item);
		}
	}

	@UiHandler("monitorButton")
	public void onObserveClick(ValueChangeEvent<Boolean> e) {
		presenter.onObserve();
	}

	@Override
	public void setPresenter(InventoryPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public WidgetList getTagList() {
		return tagList;
	}

	@Override
	public ToggleButton getMonitorButton() {
		return monitorButton;
	}

	@Override
	public InlineLabel getTagCount() {
		return tagCount;
	}

	@Override
	public HTMLPanel getTagCountPanel() {
		return monitorTagCounter;
	}

}
