package tz.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ignudiks
 *
 * FIFO 캐시
 */
public class FifoCache extends Cache {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(FifoCache.class);

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

    public FifoCache(String name, long timeoutMilliSeconds, int maxSize) {
        super(name, timeoutMilliSeconds, maxSize);
    }

    protected void createNode(Object key, Object value) {
        CacheNode node = new CacheNode(key, value, System.currentTimeMillis() + getTimeoutMilliSeconds());
        getMap().put(key, node);
        fifo.addFirst(node);
    }

    /**
     * 특정 Object를 삭제한다.
     */

    protected void delete(CacheNode node) {
        if(! fifo.isEmpty() ) fifo.remove(node);
        if(! getMap().isEmpty() ) getMap().remove(node.getKey());
        logger.debug("delete " + node);
    }

    protected void resetNode(CacheNode node) {
        // nothing
    }

    /**
     * FIFO 로직에 따라서 삭제 대상 Object를 삭제한다.
     */

    protected void removefirstRankGarbageElements() {
        if (isEmpty(fifo))
            return;

        delete((CacheNode) fifo.getLast());
    }

    /**
     * 디버깅 용도로 사용
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

    public static void main(String[] args) {

        FifoCache fifo = new FifoCache("test", 30, 5);

        for (int inx = 0; inx < 63; inx++) {
            fifo.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(fifo.getFifo());
        }

        for (int inx = 0; inx < 36; inx++) {
            fifo.get("A" + inx);
            logger.debug("get " + "A" + inx);
            logger.debug(fifo.getFifo());
        }

        for (int inx = 30; inx >= 0; inx--) {
            fifo.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(fifo.getFifo());
        }
        fifo.clear();
    }
}


