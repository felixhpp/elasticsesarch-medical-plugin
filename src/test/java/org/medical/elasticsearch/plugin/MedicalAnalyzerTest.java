package org.medical.elasticsearch.plugin;

import junit.framework.Assert;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.common.settings.Settings;
import org.medical.elasticsearch.index.analysis.MedicalAnalysis;
import org.medical.elasticsearch.analysis.MedicalConfig;
import org.elasticsearch.env.Environment;
import org.junit.Test;

import java.util.*;

public class MedicalAnalyzerTest {
    @Test
    public void testAnalyzer() throws Exception {
        MedicalConfig config = new MedicalConfig();
        config.setNerServer("http://114.251.235.51:8080/ner");
        MedicalAnalysis analyzer = new MedicalAnalysis(config);
        TokenStream ts = analyzer.tokenStream("text", "执行膀胱颈Ｖ形切除术，术后腹胀，腹痛");
        CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
        List<String> terms = new ArrayList<String>();
        ts.reset();
        while (ts.incrementToken()) {
            terms.add(term.toString());
            System.out.println(term.toString());
        }

        Assert.assertEquals(3, terms.size());
        Assert.assertEquals("膀胱颈v形切除术", terms.get(0));


        ts.end();
        ts.close();
    }

}
