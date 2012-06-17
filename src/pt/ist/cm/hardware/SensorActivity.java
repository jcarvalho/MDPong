package pt.ist.cm.hardware;

import pt.ist.cm.R;
import pt.ist.cm.game.KeyDirection;
import pt.ist.cm.game.KeyEventListener;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorActivity extends Activity implements SensorChange,
		KeyEventListener {

	TextView views[] = new TextView[3];

	SensorMapper mapper;

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor);
		this.mapper = new SensorMapper(this, this);
		this.views[X] = (TextView) findViewById(R.id.xAxisValue);
		this.views[Y] = (TextView) findViewById(R.id.yAxisValue);
		this.views[Z] = (TextView) findViewById(R.id.zAxisValue);
	}

	@Override
	protected void onResume() {
		super.onResume();
		NotificationManager.addListenerForEvent(this, EventType.KEY_EVENT);
		mapper.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		NotificationManager.removeListener(this, EventType.KEY_EVENT);
		mapper.stop();
	}

	public void sensorChanged(int index, float value) {
		this.views[index].setText("" + value);
	}

	public void keyPressed(KeyDirection direction, Class<?> sender) {
		if (sender.equals(SensorMapper.class))
			Log.d(this.getClass().getName(), "Key pressed in direction: "
					+ direction);
	}

}
