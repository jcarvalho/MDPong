package pt.ist.cm.game.ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class BallView extends View {

	private final static int RADIUS = 3;

	private final Paint paint = new Paint();

	private final Ball ball;

	private final Vector actualPosition = new Vector();

	public BallView(Context context) {
		super(context);
		this.ball = null;
	}

	public BallView(Context context, Ball ball) {
		super(context);

		this.ball = ball;
		this.paint.setColor(0xffcccccc);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (!ball.isDead()) {

			Vector position = ball.getPosition();

			Referential.updatePosition(actualPosition, position);

			canvas.drawCircle(actualPosition.getX(), actualPosition.getY(),
					RADIUS, paint);
		}
	}

	public void ballMoved() {
		this.invalidate();
	}

}
