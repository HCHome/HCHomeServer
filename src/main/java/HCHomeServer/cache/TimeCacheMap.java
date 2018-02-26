
package HCHomeServer.cache;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;



/**
 * 自定义的缓存映射，具有定时清除数据的功能。
 * 为了便于项目后来者代码阅读，拷贝了源码而不是导入jar包。
 * 该基础类库github地址：https://github.com/SupermanCj/utils.git
 * @author cj
 * @version 1.0.3
 */
public class TimeCacheMap<K, V> implements Map<K, V>{
	/**
	 * minimum and default of buckets
	 */
    private static final int DEFAULT_BUCKET_NUM = 3;
    
    /**
     * callback function, called when key-value expired and was cleaned
     * @author cj
     *
     */
    public static interface ExpiredCallback<K, V> {

        public void execute(K key, V val);
    }
    /**
     * bucket list, every bucket stores a hashMap, in order to conveniently cleaned expired key-value
     */
    private LinkedList<HashMap<K, V>> buckets;
    
    private final Object lock = new Object();
    private Thread cleaner;
    private ExpiredCallback<K, V> expiredCallback;
    /**
     * 
     * @param expirationSeconds 
     * @param numBuckets it should be larger than or equal to three
     * @param callback	it should extends interface ExpiredCallback
     */
    public TimeCacheMap(int expirationSeconds, int numBuckets, ExpiredCallback<K, V> callback) {
    	numBuckets = (numBuckets > 3) ? numBuckets : DEFAULT_BUCKET_NUM;
        buckets = new LinkedList<HashMap<K, V>>();
        for(int i=0; i<numBuckets; i++) {
            buckets.add(new HashMap<K, V>());
        }

        expiredCallback = callback;
        final long expirationMillis = expirationSeconds * 1000L;
        final long sleepTime = expirationMillis / (numBuckets-1);
        cleaner = new Thread(new Runnable() {
            public void run() {
                try {
                    while(true) {
                        Map<K, V> tailBucket = null;
                        Thread.sleep(sleepTime);
                        synchronized(lock) {
                            tailBucket = buckets.removeLast();
                            buckets.addFirst(new HashMap<K, V>());
                        }
                        if(expiredCallback!=null) {
                            for(Entry<K, V> entry: tailBucket.entrySet()) {
                                expiredCallback.execute(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                } catch (InterruptedException ex) {

                }
            }
        });
        cleaner.setDaemon(true);
        cleaner.start();
    }

    public TimeCacheMap(int expirationSecs, ExpiredCallback<K, V> callback) {
        this(expirationSecs, DEFAULT_BUCKET_NUM, callback);
    }

    public TimeCacheMap(int expirationSecs) {
        this(expirationSecs, DEFAULT_BUCKET_NUM);
    }

    public TimeCacheMap(int expirationSecs, int numBuckets) {
    	this(expirationSecs, numBuckets, null);
    }

    @Override
    public boolean containsKey(Object key) {
        synchronized(lock) {
            for(HashMap<K, V> bucket: buckets) {
                if(bucket.containsKey(key)) {
                    return true;
                }
            }
            return false;
        }
    }
    @Override
    public V get(Object key) {
        synchronized(lock) {
            for(HashMap<K, V> bucket: buckets) {
                if(bucket.containsKey(key)) {
                    return bucket.get(key);
                }
            }
            return null;
        }
    }
    @Override
    public V put(K key, V value) {
    	V result;
        synchronized(lock) {
            Iterator<HashMap<K, V>> it = buckets.iterator();
            HashMap<K, V> bucket = it.next();
            result = bucket.put(key, value);
            if(result == null) {
	            while(it.hasNext()) {
	                bucket = it.next();
	                result = bucket.remove(key);
	            }
            }
        }
        return result;
    }
    
    @Override
    /**
     * �����ڸü������Ƴ������򷵻�null
     */
    public V remove(Object key) {
        synchronized(lock) {
            for(HashMap<K, V> bucket: buckets) {
                if(bucket.containsKey(key)) {
                    return bucket.remove(key);
                }
            }
            return null;
        }
    }
    @Override
    public int size() {
        synchronized(lock) {
            int size = 0;
            for(HashMap<K, V> bucket: buckets) {
                size+=bucket.size();
            }
            return size;
        }
    }
    
    /**
     * 停止清除数据
     */
    public void stopClean() {
        cleaner.interrupt();
    }

	@Override
	public void clear() {
		synchronized (lock) {
			Iterator<HashMap<K, V>> iterator = buckets.iterator();
			while(iterator.hasNext()) {
				iterator.next().clear();
			}
		}
	}

	@Override
	public boolean containsValue(Object value) {
		synchronized (lock) {
			for(HashMap<K, V> bucket : buckets) {
				if(bucket.containsValue(value)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		synchronized (lock) {
			for(HashMap<K, V> bucket : buckets) {
				if(!bucket.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		synchronized (lock) {
			buckets.getFirst().putAll(m);
		}
	}
	
	
	@Override
	public Set<K> keySet() {
		return new AbstractSet<K>() {
			private List<Set<K>> sets = _init();
			private final List<Set<K>> _init() {
				List<Set<K>> sets = new ArrayList<>();
				for(HashMap<K, V> bucket : buckets) {
					sets.add(bucket.keySet());
				}
				return sets;
			}
			@Override
			public final Iterator<K> iterator() {
				return new Iterator<K>() {
					private LinkedList<Iterator<K>> iterators = init();
					private final LinkedList<Iterator<K>> init() {
						LinkedList<Iterator<K>> iterators = new LinkedList<>();
						for(Set<K> set : sets) {
							iterators.add(set.iterator());
						}
						return iterators;
					}
					@Override
					public final boolean hasNext() {
						while(!iterators.isEmpty()) {
							if(iterators.getFirst().hasNext()) {
								return true;
							}else {
								iterators.removeFirst();
							}
						}
						return false;
					}

					@Override
					public final K next() {
						if(iterators.isEmpty()) {
							throw new NoSuchElementException();
						}else {
							return iterators.getFirst().next();
						}
					}
				};
			}
			
			@Override
			public final int size() {
				int num = 0;
				for(Set<K> set : sets) {
					num += set.size();
				}
				return num;
			}
			
		};
		
	}

	
	@Override
	public Collection<V> values() {
		return new AbstractCollection<V>() {
			private List<Collection<V>> collections = _init();

			private final List<Collection<V>> _init() {
				List<Collection<V>> collections = new ArrayList<>();
				for(HashMap<K, V> bucket : buckets) {
					collections.add(bucket.values());
				}
				return collections;
			}
			
			@Override
			public final Iterator<V> iterator() {
				return new Iterator<V>() {
					private LinkedList<Iterator<V>> iterators = _init();
					private final LinkedList<Iterator<V>> _init(){
						LinkedList<Iterator<V>> iterators = new LinkedList<>();
						for(Collection<V> collection : collections) {
							iterators.add(collection.iterator());
						}
						return iterators;
					}
					@Override
					public final boolean hasNext() {
						while(!iterators.isEmpty()) {
							if(iterators.getFirst().hasNext()) {
								return true;
							}else {
								iterators.removeFirst();
							}
						}
						return false;
					}

					@Override
					public final V next() {
						if(iterators.isEmpty()) {
							throw new NoSuchElementException();
						}else {
							return iterators.getFirst().next();
						}
					}
					
				};
			}


			@Override
			public final int size() {
				int num = 0;
				for(HashMap<K, V> bucket : buckets) {
					num += bucket.size();
				}
				return num;
			}
			
		};
	}
	
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new AbstractSet<Map.Entry<K,V>>() {
			private List<Set<Entry<K, V>>> sets = _init();

			private List<Set<Entry<K, V>>> _init() {
				List<Set<Entry<K, V>>> sets = new ArrayList<>();
				for (HashMap<K, V> bucket : buckets) {
					sets.add(bucket.entrySet());
				}
				return sets;
			}
			
			@Override
			public final Iterator<Entry<K, V>> iterator() {
				return new Iterator<Entry<K,V>>() {
					private LinkedList<Iterator<Entry<K, V>>> iterators = _init();

					private LinkedList<Iterator<Entry<K, V>>> _init() {
						LinkedList<Iterator<Entry<K, V>>> iterators = new LinkedList<>();
						for (Set<Entry<K, V>> set : sets) {
							iterators.add(set.iterator());
						}
						return iterators;
					}

					@Override
					public final boolean hasNext() {
						while(!iterators.isEmpty()) {
							if(iterators.getFirst().hasNext()) {
								return true;
							}else {
								iterators.removeFirst();
							}
						}
						return false;
					}

					@Override
					public final Entry<K, V> next() {
						if(iterators.isEmpty())throw new NoSuchElementException();
						else return iterators.getFirst().next();
					}
				};
			}


			@Override
			public final int size() {
				int num = 0;
				for (Set<Entry<K, V>> set: sets) {
					num += set.size();
				}
				return num;
			}
			
		};
	}

}