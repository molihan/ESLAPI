package com.sio.util;

import com.sio.model.DefaultCastSettingSelector;
import com.sio.model.DefaultDimensionSelector;
import com.sio.model.EWHS_Raster;
import com.sio.model.HSEW_Raster;
import com.sio.model.SWHE_Raster;

public class DefaultImageCasterDelegatesFactory extends
		ImageCasterDelegatesFactory {

	public DefaultImageCasterDelegatesFactory() {
		initialPixelRaster();
		initialCastSettingSelector();
		initialDimensionSelector();
	}

	@Override
	protected void initialPixelRaster() {
		/*
			PixelRaster raster = null;
			AbstractPixelRaster swhe = new SWHE_Raster();
			AbstractPixelRaster hsew = new HSEW_Raster();
			AbstractPixelRaster ewhs = new EWHS_Raster();
			swhe.setNext(hsew);
			hsew.setNext(ewhs);
			raster = swhe;
		*/
		//equal upon codes.
		pixelRaster = new SWHE_Raster().setNext(new HSEW_Raster().setNext(new EWHS_Raster()));
	}

	@Override
	protected void initialCastSettingSelector() {
		castSettingSelector = new DefaultCastSettingSelector();		
	}

	@Override
	protected void initialDimensionSelector() {
		dimensionSelector = new DefaultDimensionSelector();
	}

}
