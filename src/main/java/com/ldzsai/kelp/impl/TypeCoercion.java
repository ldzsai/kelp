package com.ldzsai.kelp.impl;

/**
 * 表达式求值的类型转换工具类。
 * 为整个引擎提供一致的类型转换规则。
 */
public final class TypeCoercion {

    private TypeCoercion() {}

    /**
     * 按照一致的规则将任意值转换为布尔值：
     * - null → false
     * - Boolean → 保持原值
     * - Number → 0 或 NaN 时为 false，否则为 true
     * - String → 空字符串时为 false，否则为 true
     * - 其他类型 → true
     */
    public static boolean toBoolean(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof Number) {
            double d = ((Number) value).doubleValue();
            return d != 0 && !Double.isNaN(d);
        }
        if (value instanceof String) return !((String) value).isEmpty();
        return true;
    }

    /**
     * 将任意值转换为双精度浮点数。
     *
     * @throws KelpException 无法转换时抛出异常
     */
    public static double toDouble(Object value) {
        if (value == null) {
            throw new com.ldzsai.kelp.exception.KelpEvaluationException("Cannot convert null to double");
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? 1.0 : 0.0;
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                throw new com.ldzsai.kelp.exception.KelpEvaluationException(
                    "Cannot convert string '" + value + "' to double");
            }
        }
        throw new com.ldzsai.kelp.exception.KelpEvaluationException(
            "Cannot convert " + value.getClass().getSimpleName() + " to double");
    }

    /**
     * 将任意值转换为整数。
     */
    public static int toInt(Object value) {
        return (int) toDouble(value);
    }

    /**
     * 按照一致的规则判断两个值是否相等。
     */
    public static boolean equals(Object left, Object right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        return left.equals(right);
    }
}
