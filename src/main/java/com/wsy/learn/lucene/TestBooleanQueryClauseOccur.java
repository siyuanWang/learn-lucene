package com.wsy.learn.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * 1．MUST和MUST：取得连个查询子句的交集。
 * 2．MUST和MUST_NOT：表示查询结果中不能包含MUST_NOT所对应得查询子句的检索结果。
 * 3．SHOULD与MUST_NOT：连用时，功能同MUST和MUST_NOT。
 * 4．SHOULD与MUST连用时，结果为MUST子句的检索结果,但是SHOULD可影响排序。
 * 5．SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。
 * 6．MUST_NOT和MUST_NOT：无意义，检索无结果。
 */
public class TestBooleanQueryClauseOccur {
    private IndexSearcher searcher;
    private IndexReader reader;
    private Directory directory;
    private IndexWriter writer;

    private static final String DOC_TEXT_LINES[] = {
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

    protected void setUp() throws Exception {
        Analyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        directory = new RAMDirectory();

        writer = new IndexWriter(directory, iwc);
        for (String line : DOC_TEXT_LINES) {
            Document doc = new Document();
            doc.add(new TextField("title", line, Field.Store.YES));

            writer.addDocument(doc);
        }
        writer.commit();
        reader = DirectoryReader.open(directory);


        IndexReader reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader);
    }

    protected void shutdown() throws IOException {
        writer.close();
    }

    /**
     * single must
     * @throws Exception
     */
    public void testOccurCombination1() throws Exception {


        TermQuery termQuery = new TermQuery(new Term("title", "康师傅"));
        BooleanClause bc1 = new BooleanClause(termQuery, BooleanClause.Occur.MUST);
        BooleanQuery query = new BooleanQuery.Builder().add(bc1).build();

        ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;

        System.out.println("=== simgle must ===");
        for (ScoreDoc doc : docs) {
            //Explanation explanation = searcher.explain(query, scoreDoc.doc);
            System.out.println("docID : " + doc.doc + ", score: " + doc.score + ", content :" + reader.document(doc.doc).get("title").toString());
        }
    }

    /**
     * mul-must
     * @throws Exception
     */
    public void testOccurCombination2() throws Exception {


        TermQuery termQuery = new TermQuery(new Term("title", "康师傅"));
        BooleanClause bc = new BooleanClause(termQuery, BooleanClause.Occur.MUST);
        TermQuery termQuery1 = new TermQuery(new Term("title", "蛋"));
        BooleanClause bc1 = new BooleanClause(termQuery1, BooleanClause.Occur.MUST);

        BooleanQuery query = new BooleanQuery.Builder().add(bc).add(bc1).build();

        ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;

        System.out.println("=== mul-must ===");
        for (ScoreDoc doc : docs) {
            //Explanation explanation = searcher.explain(query, scoreDoc.doc);
            System.out.println("docID : " + doc.doc + ", score: " + doc.score + ", content :" + reader.document(doc.doc).get("title").toString());
        }
    }

    /**
     * must should
     * @throws Exception
     */
    public void testOccurCombination3() throws Exception {


        TermQuery termQuery = new TermQuery(new Term("title", "康师傅"));
        BooleanClause bc = new BooleanClause(termQuery, BooleanClause.Occur.MUST);
        //TermQuery termQuery1 = new TermQuery(new Term("title", "煮"));
        TermQuery termQuery1 = new TermQuery(new Term("title", "香肠"));
        BooleanClause bc1 = new BooleanClause(termQuery1, BooleanClause.Occur.SHOULD);

        BooleanQuery query = new BooleanQuery.Builder().add(bc).add(bc1).build();

        ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;

        System.out.println("=== mul-must ===");
        for (ScoreDoc doc : docs) {
            //Explanation explanation = searcher.explain(query, scoreDoc.doc);
            System.out.println("docID : " + doc.doc + ", score: " + doc.score + ", content :" + reader.document(doc.doc).get("title").toString());
        }
    }

    /**
     * must must_not
     * @throws Exception
     */
    public void testOccurCombination4() throws Exception {


        TermQuery termQuery = new TermQuery(new Term("title", "康师傅"));
        BooleanClause bc = new BooleanClause(termQuery, BooleanClause.Occur.MUST);

        TermQuery termQuery1 = new TermQuery(new Term("title", "香肠"));
        BooleanClause bc1 = new BooleanClause(termQuery1, BooleanClause.Occur.MUST_NOT);

        BooleanQuery query = new BooleanQuery.Builder().add(bc).add(bc1).build();

        ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;

        System.out.println("=== mul-must ===");
        for (ScoreDoc doc : docs) {
            Explanation explanation = searcher.explain(query, doc.doc);
            System.out.println("explain:" + explanation.toString());
            System.out.println("docID : " + doc.doc + ", score: " + doc.score + ", content :" + reader.document(doc.doc).get("title").toString());
        }
    }

    /**
     * mul-must_not
     * @throws Exception
     */
    public void testOccurCombination5() throws Exception {


        TermQuery termQuery = new TermQuery(new Term("title", "康师傅"));
        BooleanClause bc = new BooleanClause(termQuery, BooleanClause.Occur.MUST_NOT);

        TermQuery termQuery1 = new TermQuery(new Term("title", "香肠"));
        BooleanClause bc1 = new BooleanClause(termQuery1, BooleanClause.Occur.MUST_NOT);

        BooleanQuery query = new BooleanQuery.Builder().add(bc).add(bc1).build();

        ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;

        System.out.println("=== mul-mul-must ===");
        for (ScoreDoc doc : docs) {
            //Explanation explanation = searcher.explain(query, scoreDoc.doc);
            System.out.println("docID : " + doc.doc + ", score: " + doc.score + ", content :" + reader.document(doc.doc).get("title").toString());
        }
    }

    /**
     * must_not
     * @throws Exception
     */
    public void testOccurCombination6() throws Exception {


        TermQuery termQuery = new TermQuery(new Term("title", "康师傅"));
        BooleanClause bc = new BooleanClause(termQuery, BooleanClause.Occur.MUST_NOT);

        BooleanQuery query = new BooleanQuery.Builder().add(bc).build();

        ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;

        System.out.println("=== must_not ===");
        for (ScoreDoc doc : docs) {
            System.out.println("docID : " + doc.doc + ", score: " + doc.score + ", content :" + reader.document(doc.doc).get("title").toString());
        }
    }

    /**
     * should
     * 单独使用 SHOULD 效果和 MUST 一样
     * @throws Exception
     */
    public void testOccurCombination7() throws Exception {


        TermQuery termQuery = new TermQuery(new Term("title", "康师傅"));
        BooleanClause bc = new BooleanClause(termQuery, BooleanClause.Occur.SHOULD);

        BooleanQuery query = new BooleanQuery.Builder().add(bc).build();

        ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;

        System.out.println("=== should ===");
        for (ScoreDoc doc : docs) {
            System.out.println("docID : " + doc.doc + ", score: " + doc.score + ", content :" + reader.document(doc.doc).get("title").toString());
        }
    }

    /**
     * mul-should
     * 相当于must should
     * @throws Exception
     */
    public void testOccurCombination8() throws Exception {


        TermQuery termQuery = new TermQuery(new Term("title", "康师傅"));
        BooleanClause bc = new BooleanClause(termQuery, BooleanClause.Occur.SHOULD);

        TermQuery termQuery1 = new TermQuery(new Term("title", "蛋"));
        BooleanClause bc1 = new BooleanClause(termQuery1, BooleanClause.Occur.SHOULD);

        BooleanQuery query = new BooleanQuery.Builder().add(bc).add(bc1).build();

        ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;

        System.out.println("=== should ===");
        for (ScoreDoc doc : docs) {
            System.out.println("docID : " + doc.doc + ", score: " + doc.score + ", content :" + reader.document(doc.doc).get("title").toString());
        }
    }

    /**
     * filter 不参与评分
     * @throws Exception
     */
    public void testOccurCombination9() throws Exception {


        TermQuery termQuery = new TermQuery(new Term("title", "康师傅"));
        BooleanClause bc = new BooleanClause(termQuery, BooleanClause.Occur.FILTER);

        TermQuery termQuery1 = new TermQuery(new Term("title", "蛋"));
        BooleanClause bc1 = new BooleanClause(termQuery1, BooleanClause.Occur.SHOULD);

        BooleanQuery query = new BooleanQuery.Builder().add(bc).add(bc1).build();

        ScoreDoc[] docs = searcher.search(query, 10).scoreDocs;

        System.out.println("=== should ===");
        for (ScoreDoc doc : docs) {
            System.out.println("docID : " + doc.doc + ", score: " + doc.score + ", content :" + reader.document(doc.doc).get("title").toString());
        }
    }

    public static void main(String[] args) {
        TestBooleanQueryClauseOccur test = new TestBooleanQueryClauseOccur();
        try {
            test.setUp();
            test.testOccurCombination9();
            test.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
