package pt.ist.cm.hardware;

public interface SensorChange {

	final static int X = 0;
	final static int Y = 1;
	final static int Z = 2;

	public void sensorChanged(int index, float value);

}
