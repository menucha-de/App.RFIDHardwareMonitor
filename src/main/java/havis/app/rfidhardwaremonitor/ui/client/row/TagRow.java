package havis.app.rfidhardwaremonitor.ui.client.row;

import havis.app.rfidhardwaremonitor.Tag;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TagRow {
	private static final Widget[] WIDGET_TYPE = new Widget[] {};
	private static final int ANTENNA_COUNT = 2;

	private Label epcLabel;
	private ArrayList<RSSIScalePanel> rssiPanelList = new ArrayList<>();

	public TagRow(Tag tag) {
		if (tag != null) {
			String epc = tag.getEpc();
			epcLabel = new Label(epc);
			epcLabel.setTitle(epc);

			for (int i = 1; i <= ANTENNA_COUNT; i++) {
				if (tag.getAntennaID() == i)
					rssiPanelList.add(new RSSIScalePanel(tag.getRssi(), true));
				else
					rssiPanelList.add(new RSSIScalePanel(0, false));
			}
		}
	}

	public void setRSSI(int antennaID, int rssiValue) {
		RSSIScalePanel rssi = rssiPanelList.get(antennaID - 1);
		rssi.setFound(true);
		rssi.setValue(rssiValue);
	}

	public Widget[] getWidgets() {
		ArrayList<Widget> widgets = new ArrayList<Widget>();
		widgets.add(epcLabel);
		for (RSSIScalePanel p : rssiPanelList)
			widgets.add(p);
		return widgets.toArray(WIDGET_TYPE);
	}
}
