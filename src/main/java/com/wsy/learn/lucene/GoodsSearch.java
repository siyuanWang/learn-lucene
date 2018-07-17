package com.wsy.learn.lucene;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class GoodsSearch {
    public static void main(String[] args) {
        try {
            String indexPath = "/Users/wangsiyuan1/workspace/learnlucene/src/main/resources/goods";
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            IndexSearcher searcher = new IndexSearcher(reader);

            for (ScoreDoc doc : query(searcher)) {
                System.out.println(doc.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ScoreDoc[] query(IndexSearcher searcher) throws IOException {
        Query query1 = new TermQuery(new Term("title", "男装"));
        BooleanClause bc1 = new BooleanClause(query1, BooleanClause.Occur.MUST);
        BooleanQuery boolQuery = new BooleanQuery.Builder().add(bc1).build();
        // 返回前10条
        TopDocs topDocs = searcher.search(boolQuery, 10);
        return topDocs.scoreDocs;
    }
}
