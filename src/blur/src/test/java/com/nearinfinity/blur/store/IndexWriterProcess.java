package com.nearinfinity.blur.store;

import java.io.File;
import java.util.Random;
import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.nearinfinity.blur.lucene.store.ZookeeperWrapperDirectory;
import com.nearinfinity.blur.lucene.store.policy.ZookeeperIndexDeletionPolicy;

public class IndexWriterProcess {

	private static Random random = new Random();

	public static void main(String[] args) throws Exception {
		final String indexRefPath = "/blur/refs/testing";
		FSDirectory dir = FSDirectory.open(new File("./index"));
		final ZookeeperWrapperDirectory directory = new ZookeeperWrapperDirectory(dir, indexRefPath);
		final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		IndexWriter indexWriter = new IndexWriter(directory, analyzer, new ZookeeperIndexDeletionPolicy(indexRefPath), MaxFieldLength.UNLIMITED);
		indexWriter.setUseCompoundFile(false);
		while (true) {
			for (int i = 0; i < 10000; i++) {
				indexWriter.addDocument(genDoc());
			}
			indexWriter.commit();
			Thread.sleep(10);
		}

	}

	private static Document genDoc() {
		Document document = new Document();
		document.add(new Field("test", "test", Store.YES, Index.ANALYZED_NO_NORMS));
		document.add(new Field("id", UUID.randomUUID().toString(), Store.YES, Index.ANALYZED_NO_NORMS));
		document.add(new Field("random10", Integer.toString(random.nextInt(10)), Store.YES, Index.ANALYZED_NO_NORMS));
		document.add(new Field("random100", Integer.toString(random.nextInt(100)), Store.YES, Index.ANALYZED_NO_NORMS));
		document.add(new Field("random1000", Integer.toString(random.nextInt(1000)), Store.YES, Index.ANALYZED_NO_NORMS));
		document.add(new Field("random10000", Integer.toString(random.nextInt(10000)), Store.YES, Index.ANALYZED_NO_NORMS));
		document.add(new Field("random100000", Integer.toString(random.nextInt(100000)), Store.YES, Index.ANALYZED_NO_NORMS));
		document.add(new Field("random1000000", Integer.toString(random.nextInt(1000000)), Store.YES, Index.ANALYZED_NO_NORMS));
		return document;
	}

}
