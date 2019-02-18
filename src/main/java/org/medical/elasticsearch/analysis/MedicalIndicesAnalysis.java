package org.medical.elasticsearch.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.medical.elasticsearch.index.analysis.*;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;

public class MedicalIndicesAnalysis extends AbstractComponent {
    @Inject
    public MedicalIndicesAnalysis(final Settings settings,
                                 IndicesAnalysisService indicesAnalysisService, Environment env) {
        super(settings);
        final MedicalConfig config = new MedicalConfig(env, settings);
        // 分析器名称
        final String name = "medical";
        //analyzers
        indicesAnalysisService.analyzerProviderFactories().put(name,
                new PreBuiltAnalyzerProviderFactory(name, AnalyzerScope.GLOBAL,
                        new MedicalAnalysis(config)));

        //tokenizers
        indicesAnalysisService.tokenizerFactories().put(name,
                new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
                    @Override
                    public String name() {
                        return name;
                    }

                    @Override
                    public Tokenizer create() {
                        return new MedicalTokenizer(config);
                    }
                }));
    }
}
