package pt.ist.cm.game.paddle;

import pt.ist.cm.game.wall.Wall.WallType;
import pt.ist.cm.hardware.ScreenOrientation;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class PaddleView extends View {

	private final Paddle paddle;
	private final Paint paint = new Paint();
	private String message = null;

	public PaddleView(Context context) {
		super(context);
		this.paddle = null;
	}

	public PaddleView(Context context, WallType type,
			ScreenOrientation currentOrientation) {
		super(context);

		this.paddle = new Paddle(type);
		paint.setColor(0xffffffff);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		int x = paddle.getX();
		int y = paddle.getY();
		canvas.drawRect(x, y, x + paddle.getWidth(), y + paddle.getHeight(),
				paint);

		if (message != null) {
			canvas.drawText(message, x, y - 200, paint);
			Log.d(this.getClass().getSimpleName(), message);
		}
	}

	public void updatePaddle(int direction) {
		paddle.movePaddle(direction);
		this.invalidate();
	}

	public void printSomething(String text) {
		this.message = text;
		this.invalidate();
	}
}