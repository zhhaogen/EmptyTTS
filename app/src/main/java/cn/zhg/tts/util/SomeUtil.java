package cn.zhg.tts.util;

import java.util.*;


public final class SomeUtil {
    private static Locale[] availableLocales;

    private SomeUtil() {
    }

    /**
     * 获取正常可用的区域,排除掉Locale.getAvailableLocales()的一些错误选项
     */
    public static Locale[] getAvailableLocales() {
        if (availableLocales != null) {
            return availableLocales;
        }
        List<Locale> localeList = new ArrayList<>();
        //去除重复的
        Set<String> tagList = new HashSet<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            String lang = locale.getLanguage();
            if (lang == null || lang.isEmpty()) {
                continue;
            }
            try {
                locale.getISO3Language();
                locale.getISO3Country();
            } catch (Throwable igr) {
                continue;
            }
            String tag = getLanguageISO3Tag(locale);
            if (tagList.contains(tag)) {
                continue;
            }
            localeList.add(locale);
            tagList.add(tag);
        }
        availableLocales = localeList.toArray(new Locale[0]);
        return availableLocales;
    }

    /**
     * lang3-COUNTRY-variant3拼接
     *
     * @param locale
     * @return
     */
    public static String getLanguageISO3Tag(Locale locale) {
        String variant = locale.getVariant();
        String country = locale.getCountry();
        String lang = locale.getLanguage();
        if (variant != null && !variant.isEmpty()) {
            return locale.getISO3Language() + "-" + locale.getISO3Country() + "-" + variant;
        }
        if (country != null && !country.isEmpty()) {
            return locale.getISO3Language() + "-" + locale.getISO3Country();
        }
        return locale.getISO3Language();
    }
//    /**
//     * lang-COUNTRY-variant拼接
//     *
//     * @param locale
//     * @return
//     */
//    public static String getLanguageTag(Locale locale) {
//        String variant = locale.getVariant();
//        String country = locale.getCountry();
//        String lang = locale.getLanguage();
//        if (variant != null && !variant.isEmpty()) {
//            return lang + "-" + country + "-" + variant;
//        }
//        if (country != null && !country.isEmpty()) {
//            return lang + "-" + country;
//        }
//        return lang;
//    }

    /**
     * 创建Locale
     */
    public static Locale getLocale(String lang, String country, String variant) {
        if (country == null) {
            country = "";
        }
        if (variant == null) {
            variant = "";
        }
        return new Locale(lang, country, variant);
    }

    /**
     * 获取可用区域,不可用返回null
     */
    public static Locale getAvailableLocale(String lang, String country, String variant) {
        Locale[] availableLocales = getAvailableLocales();
        for (Locale locale : availableLocales) {
            if (localeStringEquals(locale.getISO3Language(), lang)
                    && localeStringEquals(locale.getISO3Country(), country)
                    && localeStringEquals(locale.getVariant(), variant)
            ) {
                return locale;
            }
            if (localeStringEquals(locale.getLanguage(), lang)
                    && localeStringEquals(locale.getCountry(), country)
                    && localeStringEquals(locale.getVariant(), variant)
            ) {
                return locale;
            }
        }
        return null;
    }

    /**
     * local字符串是否相等
     *
     * @param s1
     * @param s2
     * @return
     */
    private static boolean localeStringEquals(String s1, String s2) {
        if (s1 == null || s1.isEmpty()) {
            return s2 == null || s2.isEmpty();
        }
        return s1.equals(s2);
    }

}
