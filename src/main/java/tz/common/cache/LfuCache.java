package tz.common.cache;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ignudiks
 *
 * LFU 캐시
 */
public class LfuCache extends Cache {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(LfuCache.class);

    private final List lfuList;

    private final int maxLfuBuckets;

    private int firstIndexOfNonEmptyInLruList;

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
    public LfuCache(String name, long timeoutMilliSeconds, int maxSize) {
        super(name, timeoutMilliSeconds, maxSize);
        lfuList = new ArrayList();
        maxLfuBuckets = 3 * maxSize;
    }

    private final LinkedList getLfu(int index) {

        final LinkedList lfu;
        int lfuIndex = Math.min(maxLfuBuckets, index);

        if (lfuIndex >= lfuList.size()) {
            lfu = new LinkedList();
            lfuList.add(lfuIndex, lfu);
        } else {
            lfu = (LinkedList) lfuList.get(lfuIndex);
        }
        return lfu;
    }

    protected void createNode(Object key, Object value) {

        CacheNode node = new CacheNode(key, value, System.currentTimeMillis() + getTimeoutMilliSeconds());
        getMap().put(key, node);
        fifo.addFirst(node);
        getLfu(0).addFirst(node);
        firstIndexOfNonEmptyInLruList = 0;
    }

    protected void delete(CacheNode node) {
        if(! fifo.isEmpty() ) fifo.remove(node);
        if(! getLfu(node.hitCount).isEmpty() )  getLfu(node.hitCount).remove(node);
        if(! getMap().isEmpty() ) getMap().remove(node.getKey());
    }

    protected void resetNode(CacheNode node) {
        LinkedList currBucket = getLfu(node.hitCount);
        LinkedList nextBucket = getLfu(++node.hitCount);
        if(! currBucket.isEmpty() ) currBucket.remove(node);
        nextBucket.addFirst(node);
    }

    protected void removefirstRankGarbageElements() {

        LinkedList lfu = getFirstLfuOfNonEmpty();
        if (lfu != null) {
            delete((CacheNode) lfu.getLast());
        }
    }

    private final LinkedList getFirstLfuOfNonEmpty() {
        LinkedList lfu = null;
        final int size = lfuList.size();
        for (int inx = firstIndexOfNonEmptyInLruList; inx < size; inx++) {
            lfu = (LinkedList) lfuList.get(inx);
            if (lfu.size() != 0) {
                firstIndexOfNonEmptyInLruList = inx;
                return lfu;
            }
        }
        return null;
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

    /**
     * 디버깅 용도로 사용
     * @return
     */

    public String getLfu() {
        StringBuffer strbuf = new StringBuffer();
        final int size = lfuList.size();
        strbuf.append("{");
        for (int inx = 0; inx < size; inx++) {
            strbuf.append(lfuList.get(inx));
        }
        strbuf.append("}");
        return strbuf.toString();

    }

    public static void main(String[] args) {
        LfuCache lfu = new LfuCache("test", 3, 50);

        for (int inx = 0; inx < 70; inx++) {
            lfu.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(lfu.getFifo());
            logger.debug(lfu.getLfu());
        }

        for (int inx = 0; inx < 70; inx++) {
            lfu.get("A" + inx);
            logger.debug("get " + "A" + inx);
            logger.debug(lfu.getFifo());
            logger.debug(lfu.getLfu());
        }

        for (int inx = 70; inx >= 0; inx--) {
            lfu.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(lfu.getFifo());
            logger.debug(lfu.getLfu());
        }

        lfu.clear();
    }
}


