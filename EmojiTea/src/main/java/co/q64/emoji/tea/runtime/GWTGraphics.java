package co.q64.emoji.tea.runtime;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.q64.jstx.lang.value.Value;
import co.q64.jstx.runtime.Graphics;
import co.q64.jstx.util.Color;

@Singleton
public class GWTGraphics implements Graphics {

	protected @Inject GWTGraphics() {}

	@Override
	public Value createImage(int x, int y, Color color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value createImage(String encoded) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setColor(Value image, Color color) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStroke(Value image, int stroke) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawEllipse(Value image, int x, int y, int i, int j, boolean fill) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawRectangle(Value image, int x, int y, int w, int h, boolean fill) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawText(Value image, String text, int x, int y, boolean fill) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getWidth(Value image) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight(Value image) {
		// TODO Auto-generated method stub
		return 0;
	}

}
