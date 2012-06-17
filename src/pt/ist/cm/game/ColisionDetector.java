package pt.ist.cm.game;

import pt.ist.cm.game.ball.Referential;
import pt.ist.cm.game.ball.Vector;
import android.util.Log;

public class ColisionDetector {

	public static ColisionPredicate getPredicateForRect(final int originalX,
			final int originalY, final int height, final int width) {

		Referential ref = Referential.getInstance();

		final float x = (originalX / Referential.xRatio) + ref.getStartX();
		final float y = (originalY / Referential.yRatio) + ref.getStartY();

		Log.d(ColisionDetector.class.getName(), "Creating predicate x: " + x
				+ " y: " + y + "wi: " + width + " hei: " + height);

		if (width > height) {
			if (originalY < 20) {
				return new ColisionPredicate() {

					public ColisionType evaluate(Vector object) {

						return object.getY() <= y + height
								&& isInRangeX(object, x, width) ? ColisionType.HORIZONTAL
								: ColisionType.NONE;

					}
				};
			} else {
				return new ColisionPredicate() {

					public ColisionType evaluate(Vector object) {

						return object.getY() >= y
								&& isInRangeX(object, x, width) ? ColisionType.HORIZONTAL
								: ColisionType.NONE;
					}
				};
			}
		} else {
			if (originalX < 20) {
				return new ColisionPredicate() {

					public ColisionType evaluate(Vector object) {

						return object.getX() <= x + width
								&& isInRangeY(object, y, height) ? ColisionType.VERTICAL
								: ColisionType.NONE;
					}
				};
			} else {
				return new ColisionPredicate() {

					public ColisionType evaluate(Vector object) {

						return object.getX() >= x
								&& isInRangeY(object, y, height) ? ColisionType.VERTICAL
								: ColisionType.NONE;
					}
				};
			}
		}

	}

	private static boolean isInRangeX(Vector toCheck, float x, int width) {
		return toCheck.getX() >= x && toCheck.getX() <= x + width;
	}

	private static boolean isInRangeY(Vector toCheck, float y, int height) {
		return toCheck.getY() >= y && toCheck.getY() <= y + height;
	}

}
