package pt.ist.cm.game.wall;

import pt.ist.cm.game.wall.Wall.WallType;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class WallView extends View {

	private final Wall wall;
	private final Paint paint = new Paint();

	public WallView(Context context) {
		super(context);
		this.wall = null;
	}

	public WallView(Context context, WallType type) {
		super(context);

		this.wall = WallManager.createWall(type);
		paint.setColor(wall.getColor());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int left = wall.getX();
		int top = wall.getY();
		int right = left + wall.getWidth();
		int bottom = top + wall.getHeight();
		canvas.drawRect(left, top, right, bottom, paint);
	}

}
