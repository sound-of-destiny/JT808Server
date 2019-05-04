package cn.edu.sdu.JT808Server.util;

import java.util.Arrays;
import java.util.List;

public final class BitOperator {

	public static byte integerTo1Byte(int value) {
		return (byte) (value & 0xFF);
	}

	public static byte[] integerTo1Bytes(int value) {
		byte[] result = new byte[1];
		result[0] = (byte) (value & 0xFF);
		return result;
	}

	public static byte[] integerTo2Bytes(int value) {
		byte[] result = new byte[2];
		result[0] = (byte) ((value >>> 8) & 0xFF);
		result[1] = (byte) (value & 0xFF);
		return result;
	}

	public static byte[] integerTo3Bytes(int value) {
		byte[] result = new byte[3];
		result[0] = (byte) ((value >>> 16) & 0xFF);
		result[1] = (byte) ((value >>> 8) & 0xFF);
		result[2] = (byte) (value & 0xFF);
		return result;
	}

	public static byte[] integerTo4Bytes(int value){
		byte[] result = new byte[4];
		result[0] = (byte) ((value >>> 24) & 0xFF);
		result[1] = (byte) ((value >>> 16) & 0xFF);
		result[2] = (byte) ((value >>> 8) & 0xFF);
		result[3] = (byte) (value & 0xFF);
		return result;
	}

	public static int byteToInteger(byte[] value) {
		int result;
		if (value.length == 1) {
			result = oneByteToInteger(value[0]);
		} else if (value.length == 2) {
			result = twoBytesToInteger(value);
		} else if (value.length == 3) {
			result = threeBytesToInteger(value);
		} else if (value.length == 4) {
			result = fourBytesToInteger(value);
		} else {
			result = fourBytesToInteger(value);
		}
		return result;
	}

	public static int oneByteToInteger(byte value) {
		return (int) value & 0xFF;
	}


	public static int twoBytesToInteger(byte[] value) {
		// if (value.length < 2) {
		// throw new Exception("Byte array too short!");
		// }
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		return ((temp0 << 8) + temp1);
	}

	public static int threeBytesToInteger(byte[] value) {
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		return ((temp0 << 16) + (temp1 << 8) + temp2);
	}

	public static int fourBytesToInteger(byte[] value) {
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		int temp3 = value[3] & 0xFF;
		return ((temp0 << 24) + (temp1 << 16) + (temp2 << 8) + temp3);
	}

	public static byte[] generateTransactionID() throws Exception {
		byte[] id = new byte[16];
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 0, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 2, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 4, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 6, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 8, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 10, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 12, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 14, 2);
		return id;
	}

	/**
	 * 把IP拆分为int数组
	 * 
	 * @param ip ip
	 * @return int[]
	 * @throws Exception e
	 */
	public static int[] getIntIPValue(String ip) throws Exception {
		String[] sip = ip.split("[.]");
		// if (sip.length != 4) {
		// throw new Exception("error IPAddress");
		// }
		return new int[] { Integer.parseInt(sip[0]), Integer.parseInt(sip[1]), Integer.parseInt(sip[2]),
				Integer.parseInt(sip[3]) };
	}

	/**
	 * 把byte类型IP地址转化位字符串
	 * 
	 * @param address ip
	 * @return String
	 * @throws Exception e
	 */
	public static String getStringIPValue(byte[] address) throws Exception {
		int first = oneByteToInteger(address[0]);
		int second = oneByteToInteger(address[1]);
		int third = oneByteToInteger(address[2]);
		int fourth = oneByteToInteger(address[3]);

		return first + "." + second + "." + third + "." + fourth;
	}

	public static byte[] concatAll(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			if (array != null) {
				totalLength += array.length;
			}
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			if (array != null) {
				System.arraycopy(array, 0, result, offset, array.length);
				offset += array.length;
			}
		}
		return result;
	}

	public static byte[] concatAll(List<byte[]> rest) {
		int totalLength = 0;
		for (byte[] array : rest) {
			if (array != null) {
				totalLength += array.length;
			}
		}
		byte[] result = new byte[totalLength];
		int offset = 0;
		for (byte[] array : rest) {
			if (array != null) {
				System.arraycopy(array, 0, result, offset, array.length);
				offset += array.length;
			}
		}
		return result;
	}

	public static float byte2Float(byte[] bs) {
		return Float.intBitsToFloat(
				(((bs[0] & 0xFF) << 24) + ((bs[1] & 0xFF) << 16) + ((bs[2] & 0xFF) << 8) + (bs[3] & 0xFF)));
	}
	public static float byte2Float2(byte[] bs) {
		byte b = 0;
		return Float.intBitsToFloat(
				(((bs[0] & 0xFF) << 24) + ((bs[1] & 0xFF) << 16) + ((b & 0xFF) << 8) + (b & 0xFF)));
	}

	public static double byte2Double(byte[] bs) {
		return Double.longBitsToDouble((((bs[0] & 0xFF) << 24) + ((bs[1] & 0xFF) << 16) + ((bs[2] & 0xFF) << 8) + (bs[3] & 0xFF)));
	}
	public static double byte2Double2(byte[] bs) {
		byte b = 0;
		return Double.longBitsToDouble(
				(((bs[0] & 0xFF) << 24) + ((bs[1] & 0xFF) << 16) + ((b & 0xFF) << 8) + (b & 0xFF)));
	}

	public static float byteBE2Float(byte[] bytes) {
		int l;
		l = bytes[0];
		l &= 0xff;
		l |= ((long) bytes[1] << 8);
		l &= 0xffff;
		l |= ((long) bytes[2] << 16);
		l &= 0xffffff;
		l |= ((long) bytes[3] << 24);
		return Float.intBitsToFloat(l);
	}

	public static int getCheckSum4JT808(byte[] bs, int start, int end) {
		if (start < 0 || end > bs.length)
			throw new ArrayIndexOutOfBoundsException("getCheckSum4JT808 error : index out of bounds(start=" + start
					+ ",end=" + end + ",bytes length=" + bs.length + ")");
		int cs = 0;
		for (int i = start; i < end; i++) {
			cs ^= bs[i];
		}
		return cs;
	}

	public static int getBitAt(int number, int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException("min index is 0,but " + index);
		if (index >= Integer.SIZE)
			throw new IndexOutOfBoundsException("max index is " + (Integer.SIZE - 1) + ",but " + index);

		return ((1 << index) & number) >> index;
	}

	public static int getBitAtS(int number, int index) {
		String s = Integer.toBinaryString(number);
		return Integer.parseInt(s.charAt(index) + "");
	}

	public static String byteToBit(byte[] bList) {
		StringBuilder bs = new StringBuilder();
		for (byte b : bList) {
			bs.append((byte) ((b >> 7) & 0x1))
					.append((byte) ((b >> 6) & 0x1))
					.append((byte) ((b >> 5) & 0x1))
					.append((byte) ((b >> 4) & 0x1))
					.append((byte) ((b >> 3) & 0x1))
					.append((byte) ((b >> 2) & 0x1))
					.append((byte) ((b >> 1) & 0x1))
					.append((byte) ((b) & 0x1));
		}
		return bs.toString();
	}

	public static boolean getBoolAtBytes(byte[] b, int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException("min index is 0, but " + index);
		if (index > 32)
			throw new IndexOutOfBoundsException("max index is 32, but " + index);
		if (index < 9) {
			return ((b[0] >> index) & 0x1) == 0x1;
		} else if (index < 17) {
			return ((b[1] >> index) & 0x1) == 0x1;
		} else if (index < 25) {
			return ((b[2] >> index) & 0x1) == 0x1;
		} else  {
			return ((b[3] >> index) & 0x1) == 0x1;
		}
	}
}
