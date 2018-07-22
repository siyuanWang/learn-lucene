package com.wsy.learn.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.StringReader;
import java.math.BigDecimal;

public class AnalyzerTest {
    public static void main(String[] args) {
        try {
            String[] texts = {
                    "统一鲜橙多", // 0
                    "统一小当家", // 1
                    "康师傅比小当家好吃", // 2
                    "小当家最好吃", // 3
                    "小当家和康师傅谁好吃", // 4
                    "小当家不用煮，康师傅要煮着吃",// 5
                    "小当家5毛钱一袋", // 6
                    "康师傅2.5一袋", // 7
                    "康师傅有调料包", //8
                    "小当家没有调料包", //9
                    "小当家要吃5袋才能吃饱", //10
                    "康师傅吃的时候，要加个蛋，再来根香肠"// 11
            };
            int length = 0;
            for(String text: texts) {
                length += test(text);
            }
            System.out.println("---------");
            BigDecimal bigDecimal = new BigDecimal(length);
            BigDecimal bigDecimal1 = new BigDecimal("12");

            System.out.println(bigDecimal.divide(bigDecimal1,2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    static int test(String text) throws Exception {
        Analyzer analyzer = new SmartChineseAnalyzer();
        StringReader rrr = new StringReader(text);
        TokenStream stream = analyzer.tokenStream("", rrr);
        stream.reset();
        CharTermAttribute term = stream.getAttribute(CharTermAttribute.class);
        int l = 0;
        //输出分词器和处理结果
        System.out.println("");
        int length = 0;
        while(stream.incrementToken()) {
            System.out.print(term.toString() + "|");
            l += term.toString().length();
            //如果一行输出的字数大于30，就换行输出
            if (l > 30) {
                System.out.println();
                l = 0;
            }
            length++;
        }
        System.out.print(",length=" + length);
        return length;
    }
}
