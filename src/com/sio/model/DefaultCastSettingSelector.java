package com.sio.model;

import com.sio.graphics.DefaultImageCaster;

public class DefaultCastSettingSelector implements com.sio.graphics.CastSettingSelector{
	public static final int DIRECTION = 0;
	public static final int STARTING = 1;
	public static final int MSB = 2;
	public static final int INVERSE = 3;
	public static final int BPP = 4;
	private static final int[][] DIRECTION_TABLE = new int[][]{
																	{},{},{},	//0,1,2
																	{},{},{},	//3,4,5
																	{DefaultImageCaster.HORIZONTAL,DefaultImageCaster.BUTTONLEFT,1,0,1},{},{},	//6,7,8
																	{},{},{},	//9,10,11
																};

	@Override
	public int selectDirection(int modal_type) {
		return DIRECTION_TABLE[modal_type][DIRECTION];
	}
	
	@Override
	public int selectStartingPoint(int modal_type) {
		return  DIRECTION_TABLE[modal_type][STARTING];
	}
	
	@Override
	public boolean selectMSB(int modal_type) {
		return  DIRECTION_TABLE[modal_type][MSB]==1?true:false;
	}
	
	@Override
	public boolean selectInversed(int modal_type) {
		return DIRECTION_TABLE[modal_type][INVERSE]==1?true:false;
	}
	
	@Override
	public int selectBitPerPixel(int modal_type) {
		return DIRECTION_TABLE[modal_type][BPP];
	}
	
}
