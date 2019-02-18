package org.medical.elasticsearch.analysis;

import org.elasticsearch.common.inject.AbstractModule;
public class MedicalIndicesAnalysisModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MedicalIndicesAnalysis.class).asEagerSingleton();
    }
}
