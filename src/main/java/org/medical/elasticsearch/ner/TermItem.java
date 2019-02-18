package org.medical.elasticsearch.ner;

/**
 * medical 词元对象
 */
public class TermItem {
    public String term;
    //词元的起始位移
    public int startOffset;
    //词元的结束位置
    public int endOffset;
    //词元类型
    public String nerType;

    public TermItem(String term, int startOffset, int endOffset, String nerType){
        this.term=term;
        if(startOffset<0){
            startOffset=0;
        }
        if(endOffset<startOffset){
            endOffset=startOffset;
        }
        this.startOffset=startOffset;
        this.endOffset=endOffset;

        this.nerType = nerType;
    }

    @Override
    public String toString() {
        return term;
    }
}
