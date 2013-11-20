package tz.common.cache;

import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ignudiks
 *
 * 로직에 따라 캐시기능을 사용하게 하기 위한 Factory 클래스<BR>
 * <BR>
 * 현재 3가지 타입의 캐시 로직을 제공한다.<BR>
 * <BR>
 * 1. FIFO<BR>
 * 2. LFU<BR>
 * 3. LRU<BR>
 */
public class CacheFactory {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(CacheFactory.class);

    private final static Map<String,Object> map = new Hashtable<String,Object>();

    public static final int FIFO = 0;

    public static final int LFU = 1;

    public static final int LRU = 2;

    /**
     * 기존에 사용된 캐시를 특정 이름값에 대해 가져온다. 없으면 NULL을 리턴한다.
     *
     * @param name
     *            캐시 식별자
     * @return Cache
     */

    public static synchronized Cache getInstance(String name) {
        Cache cache = (Cache) map.get(name);
        return cache;
    }

    /**
     * 새로운 캐시를 하나 생성하여 리턴한다.
     *
     * @param logictype
     *            로직유형
     * @param name
     *            캐시 식별자
     * @param timeoutMinutes
     *            Time out
     * @param maxSize
     *            캐시 최대 크기
     * @return Cache
     */

    public static synchronized Cache newInstance(int logictype, String name, long timeoutMinutes, int maxSize) {
        long timeoutMilliSeconds = timeoutMinutes * 1000 * 60;
        Cache cache = null;

        switch (logictype) {

        case CacheFactory.FIFO:
            cache = new FifoCache(name, timeoutMilliSeconds, maxSize);
            map.put(name, cache);
            break;

        case CacheFactory.LFU:
            cache = new LfuCache(name, timeoutMilliSeconds, maxSize);
            map.put(name, cache);
            break;

        case CacheFactory.LRU:
            cache = new LruCache(name, timeoutMilliSeconds, maxSize);
            map.put(name, cache);
            break;
        }
        return cache;
    }

    public static void main(String[] args) {

        Cache lfu = CacheFactory.newInstance(CacheFactory.LFU, "testName", 3, 50);

        for (int inx = 0; inx < 70; inx++) {
            lfu.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(((LfuCache) lfu).getFifo());
            logger.debug(((LfuCache) lfu).getLfu());
        }

        for (int inx = 0; inx < 70; inx++) {
            lfu.get("A" + inx);
            logger.debug("get " + "A" + inx);
            logger.debug(((LfuCache) lfu).getFifo());
            logger.debug(((LfuCache) lfu).getLfu());
        }

        for (int inx = 70; inx >= 0; inx--) {
            lfu.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(((LfuCache) lfu).getFifo());
            logger.debug(((LfuCache) lfu).getLfu());
        }

        lfu.clear();

        lfu = CacheFactory.getInstance("testName");

        for (int inx = 0; inx < 70; inx++) {
            lfu.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(((LfuCache) lfu).getFifo());
            logger.debug(((LfuCache) lfu).getLfu());
        }

        for (int inx = 0; inx < 70; inx++) {
            lfu.get("A" + inx);
            logger.debug("get " + "A" + inx);
            logger.debug(((LfuCache) lfu).getFifo());
            logger.debug(((LfuCache) lfu).getLfu());
        }

        for (int inx = 70; inx >= 0; inx--) {
            lfu.add("A" + inx, "A");
            logger.debug("add " + "A" + inx);
            logger.debug(((LfuCache) lfu).getFifo());
            logger.debug(((LfuCache) lfu).getLfu());
        }

        lfu.clear();

    }
}


