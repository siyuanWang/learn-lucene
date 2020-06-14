package com.wsy.learn.lucene;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.nio.file.Paths;

public class GoodsIndex {
    public static void main(String[] args) {

        try {
            String home = "/Users/wangsiyuan/source/learn-lucene/";
            String indexPath = home + "src/main/resources/goods";
            Analyzer analyzer = new SmartChineseAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriter indexWriter = new IndexWriter(dir, iwc);

            String jsonStr = FileUtils.readFileToString(new File(home + "src/main/resources/doc/goods.json"));
            JSONArray array = JSON.parseArray(jsonStr);
            for (int i = 0, length = array.size(); i < length; i++) {
                JSONObject json = array.getJSONObject(i);

                Document doc = new Document();
                doc.add(new TextField("title", json.getString("title"), Field.Store.NO));
                doc.add(new LongPoint("skuId", json.getLong("skuId")));
                doc.add(new NumericDocValuesField("skuId", json.getLong("skuId")));
                doc.add(new StoredField("skuId", json.getLong("skuId")));
                doc.add(new IntPoint("cid1", json.getInteger("cid1")));
                doc.add(new StringField("cid1Name", json.getString("cid1Name"), Field.Store.NO));
                doc.add(new IntPoint("cid2", json.getInteger("cid2")));
                doc.add(new StringField("cid2Name", json.getString("cid2Name"), Field.Store.NO));
                doc.add(new IntPoint("cid3", json.getInteger("cid3")));
                doc.add(new StringField("cid3Name", json.getString("cid3Name"), Field.Store.NO));
                doc.add(new DoublePoint("price", json.getDoubleValue("price")));
                doc.add(new LongPoint("createTime", json.getLongValue("createTime")));
                indexWriter.addDocument(doc);
            }
            indexWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
