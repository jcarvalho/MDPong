package pt.ist.cm.hardware;

import pt.ist.cm.game.KeyDirection;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Class that is responsible for interacting with the accelerometer to generate
 * generic keystroke events.
 * 
 * 
 * 
 * @author joaocarvalho
 * 
 */
public class SensorMapper implements SensorEventListener {

	// X, Y, Z
	final float values[] = new float[3];

	private final SensorManager mSensorManager;
	private final Sensor mAccelerometer;

	private SensorChange listener = null;

	private boolean running = false;

	private final KeyConverter converter = new KeyConverter();

	public SensorMapper(Context context) {
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	public SensorMapper(Context context, SensorChange listener) {
		this(context);
		this.listener = listener;
	}

	public void start() {
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_GAME);
		running = true;
		Log.d(this.getClass().getName(), "Starting sensor!");
	}

	public void stop() {
		mSensorManager.unregisterListener(this);
		running = false;
		Log.d(this.getClass().getName(), "Stopping sensor!");
	}

	public boolean isRunning() {
		return running;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing to do here...
	}

	public void onSensorChanged(SensorEvent event) {

		final float alpha = 0.0f;

		update(0, alpha * values[0] + (1 - alpha) * event.values[0], event);
		update(1, alpha * values[1] + (1 - alpha) * event.values[1], event);
		update(2, alpha * values[2] + (1 - alpha) * event.values[2], event);

	}

	public void update(int index, float value, SensorEvent event) {
		this.values[index] = value;

		if (listener != null)
			listener.sensorChanged(index, value);

		converter.updateWithEvent(event, index, value);

		if (converter.hasKeyStroke()) {
			KeyDirection direction = converter.getKeyStroke();
			NotificationManager.notifyListeners(EventType.KEY_EVENT,
					SensorMapper.class, direction);
		}

	}

}