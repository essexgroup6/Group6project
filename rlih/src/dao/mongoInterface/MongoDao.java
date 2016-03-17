package dao.mongoInterface;

import java.net.UnknownHostException;
import java.util.ArrayList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

public class MongoDao implements MongoInterface {
	private MongoClient mongoClient = null;

	/**
	 * author��Rui
	 */
	private MongoDao() {
		if (mongoClient == null) {
			MongoClientOptions.Builder build = new MongoClientOptions.Builder();
			build.connectionsPerHost(50); // ��Ŀ�����ݿ��ܹ����������connection����Ϊ50
			build.autoConnectRetry(true); // �Զ��������ݿ�����
			build.threadsAllowedToBlockForConnectionMultiplier(50); // �����ǰ���е�connection����ʹ���У���ÿ��connection�Ͽ�����50���߳��Ŷӵȴ�
			/*
			 * һ���̷߳������ݿ��ʱ���ڳɹ���ȡ��һ���������ݿ�����֮ǰ����ȴ�ʱ��Ϊ2����
			 * ����Ƚ�Σ�գ��������maxWaitTime��û�л�ȡ��������ӵĻ������߳̾ͻ��׳�Exception
			 * ���������õ�maxWaitTimeӦ���㹻�����������Ŷ��̹߳�����ɵ����ݿ����ʧ��
			 */
			build.maxWaitTime(1000 * 60 * 2);
			build.connectTimeout(1000 * 60 * 1); // �����ݿ⽨�����ӵ�timeout����Ϊ1����

			MongoClientOptions myOptions = build.build();
			try {
				// ���ݿ�����ʵ��
				mongoClient = new MongoClient("127.0.0.1", myOptions);
			} catch (UnknownHostException e) {
				// TODO ����д�쳣����Ĵ���
				e.printStackTrace();
			} catch (MongoException e) {
				e.printStackTrace();
			}

		}
	}

	/******** ����ģʽ������ʼ�����ö���ʽ��ʽ���ɣ���֤�̰߳�ȫ ********************/

	// ���ʼ��ʱ������ʵ����������ʽ����ģʽ
	private static final MongoDao mongoDao = new MongoDao();

	/**
	 * 
	 * ��������getMongoDBDaoImplInstance ���ߣ�Rui ����ʱ�䣺2016 
	 * �����������ľ�̬��������
	 * 
	 * @return
	 */
	public static MongoDao getMongoDBDaoImplInstance() {
		return mongoDao;
	}

	/************************ ����ģʽ�������� *************************************/

	@Override
	public boolean delete(String dbName, String collectionName, String[] keys,
			Object[] values) {
		DB db = null;
		DBCollection dbCollection = null;
		if (keys != null && values != null) {
			if (keys.length != values.length) { // ���keys��values���Եȣ�ֱ�ӷ���false
				return false;
			} else {
				try {
					db = mongoClient.getDB(dbName); // ��ȡָ�������ݿ�
					dbCollection = db.getCollection(collectionName); // ��ȡָ����collectionName����

					BasicDBObject doc = new BasicDBObject(); // ����ɾ������
					WriteResult result = null; // ɾ�����ؽ��
					String resultString = null;

					for (int i = 0; i < keys.length; i++) {
						doc.put(keys[i], values[i]); // ���ɾ��������
					}
					result = dbCollection.remove(doc); // ִ��ɾ������

					resultString = result.getError();

					if (null != db) {
						try {
							db.requestDone(); // ���������ر�db
							db = null;
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					return (resultString != null) ? false : true; // ����ɾ��ִ�н�������жϺ󷵻ؽ��
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (null != db) {
						db.requestDone(); // �ر�db
						db = null;
					}
				}

			}
		}
		return false;
	}

	@Override
	public ArrayList<DBObject> find(String dbName, String collectionName,
			String[] keys, Object[] values, int num) {
		ArrayList<DBObject> resultList = new ArrayList<DBObject>(); // �������صĽ����
		DB db = null;
		DBCollection dbCollection = null;
		DBCursor cursor = null;
		if (keys != null && values != null) {
			if (keys.length != values.length) {
				return resultList; // ��������Ĳ�ѯ�����Բ��ԣ�ֱ�ӷ��ؿյĽ����
			} else {
				try {
					db = mongoClient.getDB(dbName); // ��ȡ���ݿ�ʵ��
					dbCollection = db.getCollection(collectionName); // ��ȡ���ݿ���ָ����collection����

					BasicDBObject queryObj = new BasicDBObject(); // ������ѯ����

					for (int i = 0; i < keys.length; i++) { // ����ѯ����
						queryObj.put(keys[i], values[i]);
					}
					cursor = dbCollection.find(queryObj); // ��ѯ��ȡ����
					int count = 0;
					if (num != -1) { // �ж��Ƿ��Ƿ���ȫ�����ݣ�num=-1���ز�ѯȫ�����ݣ�num!=-1�򷵻�ָ����num����
						while (count < num && cursor.hasNext()) {
							resultList.add(cursor.next());
							count++;
						}
						return resultList;
					} else {
						while (cursor.hasNext()) {
							resultList.add(cursor.next());
						}
						return resultList;
					}
				} catch (Exception e) {
				} finally {
					if (null != cursor) {
						cursor.close();
					}
					if (null != db) {
						db.requestDone(); // �ر����ݿ�����
					}
				}
			}
		}

		return resultList;
	}

	@Override
	public DBCollection getCollection(String dbName, String collectionName) {
		// TODO Auto-generated method stub
		return mongoClient.getDB(dbName).getCollection(collectionName);
	}

	@Override
	public DB getDb(String dbName) {
		// TODO Auto-generated method stub
		return mongoClient.getDB(dbName);
	}

	@Override
	public boolean inSert(String dbName, String collectionName, String[] keys,
			Object[] values) {
		DB db = null;
		DBCollection dbCollection = null;
		WriteResult result = null;
		String resultString = null;
		if (keys != null && values != null) {
			if (keys.length != values.length) {
				return false;
			} else {
				db = mongoClient.getDB(dbName); // ��ȡ���ݿ�ʵ��
				dbCollection = db.getCollection(collectionName); // ��ȡ���ݿ���ָ����collection����
				BasicDBObject insertObj = new BasicDBObject();
				for (int i = 0; i < keys.length; i++) { // �����������
					insertObj.put(keys[i], values[i]);
				}

				try {
					result = dbCollection.insert(insertObj);
					resultString = result.getError();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					if (null != db) {
						db.requestDone(); // ���������ر�db
					}
				}
				return (resultString != null) ? false : true;
			}
		}
		return false;
	}

	@Override
	public boolean isExit(String dbName, String collectionName, String key,
			Object value) {
		// TODO Auto-generated method stub
		DB db = null;
		DBCollection dbCollection = null;
		if (key != null && value != null) {
			try {
				db = mongoClient.getDB(dbName); // ��ȡ���ݿ�ʵ��
				dbCollection = db.getCollection(collectionName); // ��ȡ���ݿ���ָ����collection����
				BasicDBObject obj = new BasicDBObject(); // ������ѯ����
				obj.put(key, value);

				if (dbCollection.count(obj) > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if (null != db) {
					db.requestDone(); // �ر�db
					db = null;
				}
			}

		}
		return false;
	}

	@Override
	public boolean update(String dbName, String collectionName,
			DBObject oldValue, DBObject newValue) {
		DB db = null;
		DBCollection dbCollection = null;
		WriteResult result = null;
		String resultString = null;

		if (oldValue.equals(newValue)) {
			return true;
		} else {
			try {
				db = mongoClient.getDB(dbName); // ��ȡ���ݿ�ʵ��
				dbCollection = db.getCollection(collectionName); // ��ȡ���ݿ���ָ����collection����

				result = dbCollection.update(oldValue, newValue);
				resultString = result.getError();

				return (resultString != null) ? false : true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if (null != db) {
					db.requestDone(); // �ر�db
					db = null;
				}
			}

		}

		return false;
	}

	/**
	 * Rui 2016
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
