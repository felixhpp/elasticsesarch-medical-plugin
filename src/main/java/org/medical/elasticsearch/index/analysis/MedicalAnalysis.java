package org.medical.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;
import org.medical.elasticsearch.analysis.MedicalConfig;

/**
 *  注册索引级别分析组件
 * @author felix
 */
public class MedicalAnalysis extends Analyzer {
    private MedicalConfig config;

    public MedicalAnalysis(MedicalConfig config) {
        super();
        this.config=config;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(new MedicalTokenizer(config));
    }
}
