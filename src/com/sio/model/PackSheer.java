package com.sio.model;
/**
 * #Chain, #Iterator, #Factory
 * @author S
 *
 */
public interface PackSheer {
	public static final int DEFAULT_HEAD_SIZE = 9;
	public static final int DEFAULT_PACK_SIZE = 512;
	/**
	 * 
	 * @param raw source data.
	 */
	public void putData(byte flag,byte[] raw);
	/**
	 * 
	 * @return if true call getPack() to get next pack.
	 */
	public boolean hasNext();
	/**
	 * Solid data:
	 * <h5>B1 = 0xFE, B2 = LEN_H, B3 = LEN_M, B4 = LEN_L, </h5><h5>B5 = TOTAL_PACK_H, B6 = TOTAL_PACK_L, B7 = CURR_PACK_H, B8 = CURR_PACK_L</h5>
	 * @return get a formated data pack. <h3><b>Only this pack can be accepted by AccessPoint </b></h3>
	 */
	public byte[] getPack();
	/**
	 * regenerate data packs.
	 */
	public void reset();
	
}
