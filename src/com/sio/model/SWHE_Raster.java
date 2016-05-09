package com.sio.model;

import java.awt.image.BufferedImage;
import com.sio.graphics.AbstractPixelRaster;
import com.sio.graphics.ImageCaster;

public class SWHE_Raster extends AbstractPixelRaster {

	public SWHE_Raster() {
	}

	@Override
	public byte[] getPixels(BufferedImage img, int direction) {
		byte[] dst = null;
		if(direction == ImageCaster.DIRECTION_SWHE){
			
		} else {
			getNext().getPixels(img, direction);
		}
		return dst;
	}

}
