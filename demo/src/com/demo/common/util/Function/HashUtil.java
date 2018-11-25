package com.demo.common.util.Function;

public class HashUtil {
	
	private static final long[] byteTable = createLookupTable();
	private static final long HSTART = 0xBB40E64DA205B064L;
	private static final long HMULT = 7664345821815920749L;

	private static final long[] createLookupTable() {
		long[] byteTable = new long[256];
		long h = 0x544B2FBACAAF1684L;
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 31; j++) {
				h = (h >>> 7) ^ h;
				h = (h << 11) ^ h;
				h = (h >>> 10) ^ h;
			}
			byteTable[i] = h;
		}
		return byteTable;
	}
	
	public static long hashCode(CharSequence cs) {
		long h = HSTART;
		final int len = cs.length();
		for (int i = 0; i < len; i++) {
			char ch = cs.charAt(i);
			h = (h * HMULT) ^ byteTable[ch & 0xff];
			h = (h * HMULT) ^ byteTable[(ch >>> 8) & 0xff];
		}
		h = h < 0 ? -h : h;
		return h;
	}
	/**
	 * 对name + cidType + cid进行hash(算法优化减少重复值)
	 * @param name
	 * @param cidType
	 * @param cid
	 * @return
	 */
	public static long hashCode(String name, String cidType, String cid){
		name = name == null ? "" : name.trim();
		cidType = cidType == null ? "" : cidType.trim();
		cid = cid == null ? "" : cid.trim();
//		name = "".equals(name) ? "UNDEFINE" : name.toUpperCase();
//		cidType = "".equals(cidType) ? "UNDEFINE" : cidType.toUpperCase();
//		cid = "".equals(cid) ? "UNDEFINE" : cid.toUpperCase();
		name = "".equals(name) ? "UNDEFINE" : name;
		cidType = "".equals(cidType) ? "UNDEFINE" : cidType;
		cid = "".equals(cid) ? "UNDEFINE" : cid;
		String str = name + cidType + cid;
		return hashCode(str);
	}

}
