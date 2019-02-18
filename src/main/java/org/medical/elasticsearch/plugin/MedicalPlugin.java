package org.medical.elasticsearch.plugin;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.Plugin;
import org.medical.elasticsearch.index.analysis.MedicalAnalysisBinderProcessor;
import org.medical.elasticsearch.analysis.MedicalIndicesAnalysisModule;
import org.medical.elasticsearch.plugin.MedicalPlugin;

import java.util.Collection;
import java.util.Collections;

public class MedicalPlugin extends Plugin {
    public static String PLUGIN_NAME = "analysis-medical";
    public static final ESLogger logger = Loggers.getLogger(MedicalPlugin.class);
    @Override
    public String name() {
        return PLUGIN_NAME;
    }

    @Override
    public String description() {
        return PLUGIN_NAME;
    }

    public MedicalPlugin() {
        super();
        logger.info("{} installed into elasticsearch", PLUGIN_NAME);
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module>singletonList(new MedicalIndicesAnalysisModule());
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new MedicalAnalysisBinderProcessor());
    }
}
