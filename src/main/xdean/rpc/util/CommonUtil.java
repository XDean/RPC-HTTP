package xdean.rpc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Stream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import xdean.rpc.api.InnerSerializable;

public enum CommonUtil {
	;

	public static Object serializeIfPossiable(Object value) {
		try {
			if (value == null)
				return null;
			else if (value instanceof InnerSerializable)
				return ((InnerSerializable) value).serialize();
			else if (value.getClass().getPackage().getName().contains("java.lang"))
				return value;
			else if ((value instanceof Collection))
				return value;
			else if (value.getClass().isEnum())
				return value;
			else if (value instanceof Serializable)
				return serialize(value);
			else
				return value;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String serialize(Object s) throws IOException {
		AssertUtil.assertNotNull(s);
		if (!(s instanceof Serializable))
			throw new NotSerializableException(s.getClass().getName());
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bao);
		oos.writeObject(s);
		oos.close();
		byte[] bs = bao.toByteArray();
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(bs);
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String s) throws IOException, ClassNotFoundException {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bs = decoder.decodeBuffer(s);
		ByteArrayInputStream bis = new ByteArrayInputStream(bs);
		ObjectInputStream ois = new ObjectInputStream(bis);
		T readObject = (T) ois.readObject();
		ois.close();
		return readObject;
	}

	private static final Random random = new Random();

	public static int getRandomInt(int bound) {
		return random.nextInt(bound);
	}

	public static int byteToInt(byte b) {
		int i = (int) b;
		if (i < 0)
			return i + 256;
		else
			return i;
	}

	public static final int SECONDS_IN_DAY = 60 * 60 * 24;
	public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

	public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
		final long interval = ms1 - ms2;
		return interval < MILLIS_IN_DAY && interval > -1L * MILLIS_IN_DAY && toDay(ms1) == toDay(ms2);
	}

	private static long toDay(long millis) {
		return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
	}

	public static <T extends Enum<T>, V> Map<T, V> convertMapToEnumMap(Map<String, V> map, Class<T> clz) {
		EnumMap<T, V> eMap = new EnumMap<>(clz);
		T[] values = clz.getEnumConstants();
		Stream.of(values).forEach(v -> {
			if (map.containsKey(v.toString()))
				eMap.put(v, map.get(v.toString()));
		});
		return eMap;
	}
}
