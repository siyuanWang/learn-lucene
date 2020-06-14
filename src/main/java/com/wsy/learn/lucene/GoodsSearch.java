package com.wsy.learn.lucene;

import org.apache.lucene.document.Document;
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
            String home = "/Users/wangsiyuan/source/learn-lucene/";
            String indexPath = home + "src/main/resources/goods";
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            IndexSearcher searcher = new IndexSearcher(reader);
            for (ScoreDoc doc : query(searcher)) {
                Document document = searcher.doc(doc.doc);
                System.out.println(document.getField("skuId").numericValue().longValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ScoreDoc[] query(IndexSearcher searcher) throws IOException {
        Query query1 = new TermQuery(new Term("title", "南极"));
        Query query2 = new TermQuery(new Term("cid1", "13152"));
        BooleanClause bc1 = new BooleanClause(query1, BooleanClause.Occur.MUST);
        BooleanClause bc2 = new BooleanClause(query2, BooleanClause.Occur.SHOULD);
        BooleanQuery boolQuery = new BooleanQuery.Builder().add(bc1).add(bc2).build();
        // 返回前10条
        TopDocs topDocs = searcher.search(boolQuery, 10);
        System.out.println("hit num:" + topDocs.totalHits);
        return topDocs.scoreDocs;
    }
}
