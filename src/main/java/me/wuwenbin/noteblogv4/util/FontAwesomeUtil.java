package me.wuwenbin.noteblogv4.util;

import com.steadystate.css.parser.CSSOMParser;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * FontAwesome工具类
 * created by Wuwenbin on 2018/7/19 at 17:10
 *
 * @author wuwenbin
 */
public class FontAwesomeUtil {

    /**
     * 获取所有的字体图标样式
     *
     * @param filePathName css的绝对路径
     * @return
     */
    public static List<String> getAllFonts(String filePathName) {
        List<String> fonts = new ArrayList<>(500);
        try {
            File file = new File(filePathName);
            InputStream inStream = new FileInputStream(file);
            InputSource source = new InputSource();
            source.setByteStream(inStream);
            source.setEncoding("UTF-8");
            final CSSOMParser parser = new CSSOMParser();
            CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
            CSSRuleList rules = sheet.getCssRules();
            if (rules.getLength() == 0) {
                return Collections.emptyList();
            }
            for (int i = 0; i < rules.getLength(); i++) {
                final CSSRule rule = rules.item(i);
                //获取样式名称
                if (rule instanceof CSSStyleRule) {
                    String selectorText = ((CSSStyleRule) rule).getSelectorText();
                    if (selectorText.contains(",")) {
                        if (selectorText.startsWith(".fa-") && selectorText.endsWith(":before")) {
                            String[] selectorTexts = selectorText.split(",");
                            for (String s : selectorTexts) {
                                s = s.replace(".fa-", "fa-").replace(":before", "");
                                fonts.add(s);
                            }
                        }
                    } else if (selectorText.startsWith(".fa-") && selectorText.endsWith(":before")) {
                        selectorText = selectorText.replace(":before", "");
                        selectorText = selectorText.substring(1);
                        fonts.add(selectorText);
                    }
                }
            }
            return fonts;
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }


}
