package xdean.rpc.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum AssertUtil {
	;

	public static void assertTrue(boolean b, RuntimeException e) {
		if (!b)
			throw e;
	}

	public static void assertNotNull(Object obj) {
		Objects.requireNonNull(obj, "ASSERT NOTNULL FAIL");
	}

	public static void assertContainsAll(Map<?, ?> map, Object... keys) {
		if (!assertAllTrue(map::containsKey, keys))
			throw new RuntimeException("The map should has following keys: " + Arrays.toString(keys));
	}

	public static void assertContainsAll(List<?> list, Object... elements) {
		if (!assertAllTrue(list::contains, elements))
			throw new RuntimeException("The list should has following elements: " + Arrays.toString(elements));
	}

	@SafeVarargs
	private static <T> boolean assertAllTrue(Predicate<? super T> predicate, T... elements) {
		return !Stream.of(elements).filter(predicate.negate()).findFirst().isPresent();
	}

	public static boolean assertInstanceOf(Object object, String expectClass, boolean throwException) {
		Class<?> clz = ReflectUtil.getClass(expectClass);
		boolean b = clz.isInstance(object);
		if (throwException && (b == false))
			throw new ClassCastException(String.format("The expect class is %d, but actually %d.", expectClass,
					object.getClass().getName()));
		return b;
	}
}
