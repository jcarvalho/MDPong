package pt.ist.cm.game.player;

import pt.ist.cm.game.GameConfiguration;
import pt.ist.cm.game.ScreenConfiguration;
import pt.ist.cm.game.ball.Ball;
import pt.ist.cm.game.ball.BallManager;
import pt.ist.cm.game.ball.Referential;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MiniMapView extends View {

	private final Paint paint = new Paint();
	private final Paint blackPaint = new Paint();
	private final Paint textPaint = new Paint();
	private final int factor = 5;

	public MiniMapView(Context context) {
		super(context);
		this.paint.setColor(0xffffffff);
		this.blackPaint.setColor(Color.GREEN);
		BallManager.setMiniMapView(this);

	}

	public MiniMapView(Context context, Ball ball) {
		super(context);

		this.paint.setColor(0xffcccccc);

		BallManager.setMiniMapView(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		int numconfs = ScreenConfiguration.getConfig();
		if (BallManager.numBallsInside > 0 || numconfs == 0)
			return;

		GameConfiguration conf = ScreenConfiguration.getGeneralConfig();

		for (int i = 0; i <= numconfs; i++) {
			// canvas.drawCircle(10 + ((i + 1) * 2), 10 + ((i + 1) * 2),
			// i + 1 * 4, paint);
			// canvas.drawRect(left, top, right, bottom, paint);
			Referential temp = conf.getConfigurationForPlayer("" + i)
					.getReferential();
			canvas.drawRect(temp.getStartX() / factor, temp.getStartY()
					/ factor, temp.getEndX() / factor, temp.getEndY() / factor,
					paint);
			// canvas.drawText(BallManager.getBallsWithinPlayer("" + i),
			// temp.getStartX() / 10, temp.getStartY() / 10, blackPaint);
			textPaint.setStyle(Paint.Style.STROKE);
			textPaint.setStrokeWidth(1);
			textPaint.setColor(Color.BLACK);
			textPaint.setTextSize(300 / factor);
			canvas.drawText(BallManager.getBallsWithinPlayer("" + i),
					temp.getStartX() / factor + 10, temp.getStartY() / factor
							+ 50, textPaint);

		}

		for (int j = 0; j <= numconfs; j++) {
			Referential temp = conf.getConfigurationForPlayer("" + j)
					.getReferential();
			canvas.drawRect(temp.getStartX() / factor, temp.getStartY()
					/ factor, temp.getStartX() / factor + 2, temp.getEndY()
					/ factor, blackPaint);
			canvas.drawRect(temp.getEndX() / factor, temp.getStartY() / factor,
					temp.getEndX() / factor + 2, temp.getEndY() / factor,
					blackPaint);
			canvas.drawRect(temp.getStartX() / factor, temp.getEndY() / factor
					- 2, temp.getEndX() / factor, temp.getEndY() / factor,
					blackPaint);
			canvas.drawRect(temp.getStartX() / factor, temp.getStartY()
					/ factor, temp.getEndX() / factor, temp.getStartY()
					/ factor + 2, blackPaint);

		}

	}

	public void ballMoved() {
		this.invalidate();
	}

}
