package org.medical.elasticsearch.index.analysis;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;

import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;
import org.medical.elasticsearch.analysis.MedicalConfig;

public class MedicalAnalysisProvider extends AbstractIndexAnalyzerProvider<MedicalAnalysis> {
    private MedicalConfig config;
    private final MedicalAnalysis medicalAnalyzer;

    @Inject
    public MedicalAnalysisProvider(Index index, IndexSettingsService indexSettings, Environment environment,
                                   @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings.getSettings(), name, settings);
        config = new MedicalConfig(environment, settings);
        medicalAnalyzer = new MedicalAnalysis(config);
    }

    @Override
    public MedicalAnalysis get() {
        return medicalAnalyzer;
    }
}
