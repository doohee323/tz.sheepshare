package tz.common.cache;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ignudiks
 *
 * LRU 캐시
 *
 */
public class LruCache extends Cache {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(LruCache.class);

    private final LinkedList lru;

    /**
     * 기본 생성자
     *
     * @param name
     *            캐시 식별자
     * @param timeoutMilliSeconds
     *            Time out
     * @param maxSize
     *            캐시 최대 크기
     */

    public LruCache(String name, long timeoutMilliSeconds, int maxSize) {
        super(name, timeoutMilliSeconds, maxSize);
        lru = new LinkedList();
    }

    protected void createNode(Object key, Object value) {
        CacheNode node = new CacheNode(key, value, System.currentTimeMillis() + getTimeoutMilliSeconds());
        getMap().put(key, node);
        fifo.addFirst(node);
        lru.addFirst(node);
    }

    protected void delete(CacheNode node) {
        if(! fifo.isEmpty() ) fifo.remove(node);
        if(! lru.isEmpty() ) lru.remove(node);
        if(! getMap().isEmpty() ) getMap().remove(node.getKey());
    }

    protected void resetNode(CacheNode node) {
        if(! fifo.isEmpty() ) fifo.remove(node);
        if(! lru.isEmpty() ) lru.addFirst(node);
    }

    protected void removefirstRankGarbageElements() {
        if (isEmpty(lru))
            return;
        delete((CacheNode) lru.getLast());
    }

    /**
     * 디버깅 용도로 사용
     *
     * @return
     */

    public String getFifo() {

        StringBuffer strbuf = new StringBuffer();
        final int size = fifo.size();
        strbuf.append("{");
        for (int inx = 0; inx < size; inx++) {
            strbuf.append("[" + fifo.get(inx) + "]");
        }
        strbuf.append("}");
        return strbuf.toString();

    }

    /**
     * 디버깅 용도로 사용
     *
     * @return
     */

    public String getLru() {

        StringBuffer strbuf = new StringBuffer();
        final int size = lru.size();
        strbuf.append("{");
        for (int inx = 0; inx < size; inx++) {
            strbuf.append("[" + lru.get(inx) + "]");
        }
        strbuf.append("}");
        return strbuf.toString();

    }

    public static void main(String[] args) {
        LruCache lru = new LruCache("test", 3000000, 10);
        for (int inx = 0; inx < 12; inx++) {
            lru.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(lru.getFifo());
            logger.debug(lru.getLru());
        }

        for (int inx = 0; inx < 12; inx++) {
            lru.get("A" + inx);
            logger.debug("get " + "A" + inx);
            logger.debug(lru.getFifo());
            logger.debug(lru.getLru());
        }

        for (int inx = 12; inx >= 0; inx--) {
            lru.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(lru.getFifo());
            logger.debug(lru.getLru());
        }
        lru.clear();

    }

}


