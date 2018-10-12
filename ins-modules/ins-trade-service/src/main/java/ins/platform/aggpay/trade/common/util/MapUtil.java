package ins.platform.aggpay.trade.common.util;

import ins.platform.aggpay.trade.vo.RegisterQueryVo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.time.DateUtils;

public class MapUtil {

	final private static String BOOLEAN_OBJECT_TYPE = "Boolean";
	final private static String INTEGER_OBJECT_TYPE = "Integer";
	final private static String LONG_OBJECT_TYPE = "Long";
	final private static String STRING_TYPE = "String";
	final private static String DOUBLE_OBJECT_TYPE = "Double";
	final private static String FLOAT_OBJECT_TYPE = "Float";
	final private static String DATE_TYPE = "Date";
	final private static String BIGDECIMAL_TYPE = "BigDecimal";
	final private static String LONG_BASIC_TYPE = "long";
	final private static String DOUBLE_BASIC_TYPE = "double";
	final private static String FLOAT_BASIC_TYPE = "float";
	final private static String INT_TYPE = "int";
	final private static String BOOLEAN_BASIC_TYPE = "boolean";

	/**
	 * 返回一个Class的实例
	 * 实例的属性值取决于Map
	 *
	 * @param map
	 * @param cls
	 * @return
	 */
	public static <T> T map2Obj(Map<?, ?> map, Class<?> cls) {
		Object obj = null;
		try {
			obj = cls.newInstance();
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				int mod = field.getModifiers();
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}
				Object value = map.get(field.getName());
				field.setAccessible(true);
				if (map.get(field.getName()) instanceof Map<?, ?>) {
					field.set(obj, map2Obj((Map<?, ?>) map.get(field.getName()), field.getType()));
				} else if (field.getType().equals(Integer.class)) {
					if (value != null) {
						field.set(obj, Integer.parseInt((String) map.get(field.getName())));
					}
				} else if (field.getType().equals(Date.class)) {
					if (value != null) {
						field.set(obj, DateUtils.parseDate((String) value, "yyyy-MM-dd HH:mm:ss"));
					}
				} else {
					field.set(obj, map.get(field.getName()));
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return (T) obj;
	}

	private static Object changeType(Class<?>[] classes, Object value) {
		if (classes.length <= 0) {
			return null;
		}
		Object rs = null;
		String simpleName = classes[0].getSimpleName();
		if (STRING_TYPE.equals(simpleName)) {
			return value;
		} else if (value == null) {
			return null;
		} else if (INTEGER_OBJECT_TYPE.equals(simpleName) || INT_TYPE.equals(simpleName)) {
			rs = Integer.parseInt(value.toString());
		} else if (BOOLEAN_OBJECT_TYPE.equals(simpleName) || BOOLEAN_BASIC_TYPE.equals(simpleName)) {
			if (BOOLEAN_OBJECT_TYPE.equals(value.getClass().getSimpleName()) || BOOLEAN_BASIC_TYPE.equals(value.getClass().getSimpleName())) {
				return (Boolean) value;
			}
			rs = Boolean.valueOf((String) value);
		} else if (LONG_OBJECT_TYPE.equals(simpleName) || LONG_BASIC_TYPE.equals(simpleName)) {
			rs = Long.valueOf(value.toString());
		} else if (DOUBLE_OBJECT_TYPE.equals(simpleName) || DOUBLE_BASIC_TYPE.equals(simpleName)) {
			rs = Double.valueOf((String) value);
		} else if (FLOAT_OBJECT_TYPE.equals(simpleName) || FLOAT_BASIC_TYPE.equals(simpleName)) {
			rs = Float.valueOf((String) value);
		} else if (DATE_TYPE.equals(simpleName)) {
			rs = value;
		} else if (BIGDECIMAL_TYPE.equals(simpleName)) {
			rs = (BigDecimal) value;
		} else {
			System.out.println("还不支持这个类型，请自行添加");
		}
		return rs;
	}

	/**
	 * 更改obj属性的值
	 * 取决于map的值
	 * 会覆盖旧值
	 *
	 * @param map
	 * @param obj
	 */
	public static void map2Obj(Map map, Object obj) {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				int mod = field.getModifiers();
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}
				field.setAccessible(true);
				if (map.get(field.getName()) instanceof Map<?, ?>) {
					field.set(obj, map2Obj((Map<?, ?>) map.get(field.getName()), field.getDeclaringClass()));
				} else {
					field.set(obj, map.get(field.getName()));
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		RegisterQueryVo a = new RegisterQueryVo();
		a.setAccountNo("232");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("accountNo", "1111");
		map1.put("resultMsg", "dsdsadsa");
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("accountNo", "2222");
		map1.put("a", map2);

		RegisterQueryVo b = map2Obj(map1, RegisterQueryVo.class);
		map2Obj(map1, a);
		System.out.println(a.toString());
		System.out.println(b.toString());
	}

}
