package cn.zhg.tts;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import cn.zhg.tts.util.SomeUtil;

public class LocalesTest {
    @Test
    @Ignore
    public void testToLanguageTag() {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (locale.getLanguage().isEmpty()) {
                continue;
            }
            String s1 = locale.toLanguageTag();
            String s2 = SomeUtil.getLanguageISO3Tag(locale);
            System.out.println(s1 + " <=> " + s2);
            assertEquals(s1, s2);
        }
    }

    @Test
    public void testGetAvailableLocale() {
        Locale[] locales = SomeUtil.getAvailableLocales();
        for (Locale locale : locales) {
            String variant = locale.getVariant();
            String country = locale.getCountry();
            String lang = locale.getLanguage();
            System.out.println("lang="+lang+",country="+country+",variant="+variant);
            Locale l = SomeUtil.getAvailableLocale(lang, country, variant);
            assertNotNull(l);
        }
    }

}
