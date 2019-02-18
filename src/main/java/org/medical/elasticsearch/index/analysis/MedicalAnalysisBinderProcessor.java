package org.medical.elasticsearch.index.analysis;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

public class MedicalAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {
    public static final ESLogger LOG = Loggers.getLogger(MedicalAnalysisBinderProcessor.class);
    public static final String ANALYZER = "medical";
    @Override
    public void processAnalyzers(AnalyzersBindings analyzersBindings) {
        //以名字为medical为分析器的名字注入到es容器中
        analyzersBindings.processAnalyzer(ANALYZER, MedicalAnalysisProvider.class);
        LOG.debug("regedit analyzer provider named : {}", ANALYZER);
    }

    @Override
    public void processTokenizers(TokenizersBindings tokenizersBindings) {
        //以名字为medical为分词器的名字注入到es容器中
        tokenizersBindings.processTokenizer(ANALYZER, MedicalTokenizerFactory.class);
        LOG.debug("regedit analyzer tokenizer named : {}", ANALYZER);
    }

}
