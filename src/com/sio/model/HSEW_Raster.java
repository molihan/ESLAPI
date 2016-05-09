package com.sio.model;

import java.awt.image.BufferedImage;
import com.sio.graphics.AbstractPixelRaster;
import com.sio.graphics.ImageCaster;

public class HSEW_Raster extends AbstractPixelRaster {

	public HSEW_Raster() {
	}

	@Override
	public byte[] getPixels(BufferedImage img, int direction) {
		byte[] dst = null;
		if(direction == ImageCaster.DIRECTION_HSEW){
			
		} else {
			getNext().getPixels(img, direction);
		}
		return dst;
	}
}
