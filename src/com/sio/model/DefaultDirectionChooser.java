package com.sio.model;

public class DefaultDirectionChooser implements com.sio.graphics.DirectionChooser{
	private static final int[][] DIRECTION_TABLE = new int[][]{};
	
	public DefaultDirectionChooser() {
	}

	@Override
	public int chooseDirection(int modal_type) {
		return DIRECTION_TABLE[modal_type][0];
	}
}
