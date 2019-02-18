package org.medical.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.medical.elasticsearch.analysis.MedicalConfig;

public class MedicalTokenizerFactory extends AbstractTokenizerFactory {
    private MedicalConfig config;
    @Inject
    public MedicalTokenizerFactory(Index index, IndexSettingsService indexSettings, Environment environment,
                                   @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings.getSettings(), name, settings);
        config=new MedicalConfig(environment, settings);
    }

    @Override
    public Tokenizer create() {
        return new MedicalTokenizer(config);
    }
}
