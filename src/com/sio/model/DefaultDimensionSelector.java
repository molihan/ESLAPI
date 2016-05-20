package com.sio.model;

public class DefaultDimensionSelector implements com.sio.graphics.DimensionSelector {
	public static final int[][] DIMENSION_TABLE = new int[][]{
																{},{250,128},{296,128},
																{},{},{250,128},
																{296,128},{},{}
															};
	
	public DefaultDimensionSelector() {
	}

	@Override
	public int getWidth(int modal_type) {
		int width = 0;
		width = DIMENSION_TABLE[modal_type][0];
		return width;
	}

	@Override
	public int getHeight(int modal_type) {
		int height = 0;
		height = DIMENSION_TABLE[modal_type][1]; 
		return height;
	}

}
