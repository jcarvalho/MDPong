package pt.ist.cm.game.wall;

import pt.ist.cm.game.ball.Ball;
import pt.ist.cm.game.ball.BallListener;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class NeighborView extends View implements BallListener {

	private Neighbor wall;
	private final Paint paint = new Paint();

	public NeighborView(Context context) {
		super(context);
		NotificationManager
				.addListenerForEvent(this, EventType.BALL_LOST_EVENT);
		NotificationManager.addListenerForEvent(this,
				EventType.BALL_ENTERING_EVENT);
		NotificationManager.addListenerForEvent(this,
				EventType.BALL_ENTRY_EVENT);
	}

	public NeighborView(Context context, Neighbor neighbor) {
		this(context);
		this.wall = neighbor;
		this.paint.setColor(Color.TRANSPARENT);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int left = wall.getX();
		int top = wall.getY();
		int right = left + wall.getWidth();
		int bottom = top + wall.getHeight();
		canvas.drawRect(left, top, right, bottom, paint);
	}

	public void ballFell(Ball ball) {

	}

	public void ballEntry(Ball ball) {
		if (wall.isWarning()) {
			wall.setWarning(false);
			Log.d(this.getClass().getName(),
					"Setting neighbor color to transparent!");
			paint.setColor(Color.TRANSPARENT);
			this.invalidate();
		}
	}

	public void ballEntering(Ball ball) {
		if (wall.isWarning()) {
			Log.d(this.getClass().getName(), "Setting neighbor color to RED!");
			paint.setColor(Color.RED);
			this.invalidate();
		}
	}

	public void ballCreated(Ball ball) {
	}

}
