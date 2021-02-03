package havis.custom.harting.rfidhardwaremonitor.ui.client.sections;

import havis.custom.harting.rfidhardwaremonitor.Tag;
import havis.custom.harting.rfidhardwaremonitor.rest.async.RFIDHardwareMonitorServiceAsync;
import havis.custom.harting.rfidhardwaremonitor.ui.client.row.TagRow;
import havis.custom.harting.rfidhardwaremonitor.ui.resourcebundle.AppResources;
import havis.net.ui.shared.client.event.MessageEvent.MessageType;
import havis.net.ui.shared.client.list.WidgetList;
import havis.net.ui.shared.client.widgets.CustomMessageWidget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ToggleButton;

public class InventoryPresenter {

	private static final int POLLING_INTERVAL = 1000;

	interface View extends IsWidget {
		void setPresenter(InventoryPresenter presenter);

		ToggleButton getMonitorButton();

		InlineLabel getTagCount();

		HTMLPanel getTagCountPanel();

		WidgetList getTagList();
	}

	private AppResources res = AppResources.INSTANCE;
	private RFIDHardwareMonitorServiceAsync service = GWT
			.create(RFIDHardwareMonitorServiceAsync.class);
	private View view;
	private Timer timer = new Timer() {

		@Override
		public void run() {
			poll();
		}
	};

	public InventoryPresenter(View view) {
		if (view != null) {
			this.view = view;
			this.view.setPresenter(this);
		}
	}

	/**
	 * Starts or stops an observation
	 */
	public void onObserve() {
		if (view != null) {
			final Boolean shouldObserve = view.getMonitorButton().getValue();
			service.setObserving(shouldObserve, new MethodCallback<Void>() {

				@Override
				public void onSuccess(Method method, Void response) {
					if (shouldObserve) {
						clearTagList();
						timer.schedule(POLLING_INTERVAL);
					} else {
						timer.cancel();
					}
					adjustTagCounter();
				}

				@Override
				public void onFailure(Method method, Throwable exception) {
					CustomMessageWidget.show("Could not " + (shouldObserve ? "start" : "stop")
							+ " observation", MessageType.ERROR);
				}
			});
		}
	}

	private void clearTagList() {
		view.getTagCount().setText("0");
		view.getTagList().clear();
	}

	private void adjustTagCounter() {
		HTMLPanel tagCounter = view.getTagCountPanel();
		if (view.getMonitorButton().getValue()) {
			tagCounter.setStyleName(res.css().observe(), false);
			tagCounter.setStyleName(res.css().observing(), true);
		} else {
			tagCounter.setStyleName(res.css().observing(), false);
			tagCounter.setStyleName(res.css().observe(), true);
		}
	}

	private void poll() {
		service.getTags(new MethodCallback<List<Tag>>() {

			@Override
			public void onSuccess(Method method, List<Tag> response) {
				if (response != null)
					addTags(response);
				timer.schedule(POLLING_INTERVAL);
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				CustomMessageWidget.show("Could not get list of tags", MessageType.ERROR);
				timer.schedule(POLLING_INTERVAL);
			}
		});
	}

	private void addTags(List<Tag> tags) {
		HashSet<String> uniqueTags = new HashSet<>();
		HashMap<String, TagRow> tagMap = new HashMap<>();
		clearTagList();
		if (tags != null) {
			for (Tag t : tags) {
				String epcString = t.getEpc();
				TagRow row = tagMap.get(epcString);
				uniqueTags.add(epcString);

				if (row == null) {
					row = new TagRow(t);
					view.getTagList().addItem(row.getWidgets());
					tagMap.put(epcString, row);
				} else {
					row.setRSSI(t.getAntennaID(), t.getRssi());
				}
			}
		}
		view.getTagCount().setText(String.valueOf(uniqueTags.size()));
	}
}
