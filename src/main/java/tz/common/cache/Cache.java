package tz.common.cache;

import java.util.LinkedList;
import java.util.Map;


/**
 * @author ignudiks
 *
 * 캐시 최상위 클래스
 *
 */
abstract public class Cache {

    private final String name;

    private final int maxSize;

    private final long timeoutMilliSeconds;

    private Map map;

    protected final LinkedList fifo;

    /**
     * 캐시 생성자
     *
     * @param name
     *            캐시 식별자
     * @param timeoutMilliSeconds
     *            Time out
     * @param maxSize
     *            캐시 최대 크기
     */

    protected Cache(String name, long timeoutMilliSeconds, int maxSize) {
        this.name = name;
        this.timeoutMilliSeconds = timeoutMilliSeconds;
        this.maxSize = maxSize;

        map = MapFactory.createMap(maxSize);
        fifo = new LinkedList();
    }

    /**
     * 캐시에 새로운 Object를 저장한다.<BR>
     * 내부적으로 값을 저장하기 위해 각각의 캐시에 해당하는 로직을 수행한다.
     *
     * @param key
     *            저장 Object의 key 값.
     * @param value
     *            저장 Object
     */

    public void add(Object key, Object value) {
        CacheNode node = (CacheNode) map.get(key);
        if (node != null) {
            node.setValue(value);
            resetNode(node);
        } else {
            trimToSize(maxSize - 1);
            createNode(key, value);
        }
        removeExpiredElements();
    }

    /**
     * 캐시에서 특정 key 값에 해당하는 값을 얻어온다.
     *
     * @param key
     *            저장 Object의 key 값
     * @return 저장 Object
     */

    public Object get(Object key) {
        removeExpiredElements();
        CacheNode node = (CacheNode) map.get(key);

        if (node != null) {
            resetNode(node);
            return node.getValue();
        }
        return null;
    }

    /**
     * 캐시에서 특정 key 값에 해당하는 값을 삭제한다.
     * @param key 저장 Object의 key 값 *
     */

    public void remove(Object key) {
        if(map.get(key) == null) return;
        CacheNode node = (CacheNode) map.get(key);
        if (node != null) {
            delete(node);
        }
        removeExpiredElements();
    }

    /**
     * 캐시 사이즈를 주어진 값과 비교하여 클경우 우선순위에 따라 캐시 내부 데이터를 일부 삭제한다.
     *
     * @param size
     *            한계 사이즈
     */

    protected void trimToSize(int size) {
        while (map.size() > size) {
            removefirstRankGarbageElements();
        }
    }

    /**
     * 캐시의 값을 모두 지운다.
     *
     */

    public void clear() {
        // trimToSize(0);
        if(! map.isEmpty()) map.clear();
        if(! fifo.isEmpty() ) fifo.clear();
    }

    protected boolean isEmpty(LinkedList list) {
        return (list.size() == 0) ? true : false;
    }

    abstract protected void createNode(Object key, Object value);

    abstract protected void delete(CacheNode node);

    abstract protected void resetNode(CacheNode node);

    abstract protected void removefirstRankGarbageElements();

    /**
     * Timeout 시간과 비교하여 시간이 초과한 경우 캐시 내부 데이터를 우선순위에 따라 일부 삭제한다.
     *
     */
    protected void removeExpiredElements() {
        if (isEmpty(fifo))
            return;

        CacheNode node;
        while ((node = (CacheNode) fifo.getLast()) != null) {
            if (node.isExpired()) {
                delete(node);
                if (isEmpty(fifo))
                    return;
            } else {
                break;
            }
        }
    }

    public long getTimeoutMilliSeconds() {
        return timeoutMilliSeconds;
    }

    public Map getMap() {
        return map;
    }

    public String getName() {
        return name;
    }
}


