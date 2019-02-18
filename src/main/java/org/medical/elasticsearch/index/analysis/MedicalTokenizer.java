package org.medical.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.medical.elasticsearch.analysis.MedicalConfig;
import org.medical.elasticsearch.ner.HttpHelper;
import org.medical.elasticsearch.ner.TermItem;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 *  Medical分词器 - 实现自定义分词过程
 *  通过重写incrementToken方法来实现自定义分词功能
 * @author felix
 */
public class MedicalTokenizer extends Tokenizer {
    private static final int DEFAULT_BUFFER_SIZE = 256;
    private final ESLogger logger = ESLoggerFactory.getLogger("medical-analyzer");

    //词元文本属性
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    //词元位移属性
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    //词元分类属性
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    protected int position = 0;
    //记录最后一个词元的结束位置
    protected int lastPosition = 0;
    private boolean done = false;
    private boolean processedCandidate = false;
    private MedicalConfig config;
    ArrayList<TermItem> candidate;
    String source;

    /**
     * Tokenizer适配器类构造函数
     * @param config
     */
    public MedicalTokenizer(MedicalConfig config) {
        this(DEFAULT_BUFFER_SIZE);
        this.config = config;

        candidate = new ArrayList<>();
    }
    public MedicalTokenizer(int bufferSize) {
        super();
        termAtt.resizeBuffer(bufferSize);
        candidate = new ArrayList<>();
    }

    /**
     * 设置词元属性
     * @param term 词元内容
     * @param startOffset 词元起始位置
     * @param endOffset 词元结束位置
     * @param type 词元词性
     */
    void setTerm(String term, int startOffset, int endOffset, String type) {
        termAtt.setEmpty();
        termAtt.append(term);
        if(startOffset<0){
            startOffset=0;
        }
        if(endOffset < startOffset){
            endOffset=startOffset+term.length();
        }
        offsetAtt.setOffset(correctOffset(startOffset), correctOffset(endOffset));
        typeAtt.setType(type);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        //清除所有的词元属性
        clearAttributes();
        if(!done) {
            int upto = 0;
            char[] buffer = termAtt.buffer();

            while (true) {
                final int length = input.read(buffer, upto, buffer.length - upto);
                if (length == -1) break;
                upto += length;
                if (upto == buffer.length)
                    buffer = termAtt.resizeBuffer(1 + buffer.length);
            }
            termAtt.setLength(upto);
            source = termAtt.toString();

            // 获取分词
            StringBuilder sb = new StringBuilder(config.getNerServer());
            Map<String, String> params = new HashMap<String, String>();
            params.put("data", source);
            String result = HttpHelper.GetPostUrl(sb.toString(), params, "POST",null, 0, 0);
            System.out.println(result);
            ArrayList<TermItem> terms = HttpHelper.GetNerTerm(result);
            candidate = terms;

            done = true;
        }

        if (position < candidate.size()) {
            TermItem item = candidate.get(position);
            position++;
            setTerm(item.term, item.startOffset, item.endOffset, item.nerType);

            //返会true告知还有下个词元
            return true;
        }
        //返会false告知词元输出完毕
        return false;
    }

    @Override
    public final void end() throws IOException {
        super.end();
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        position = 0;
        this.done = false;
        this.processedCandidate = false;
        candidate.clear();
        source = null;
    }
}
