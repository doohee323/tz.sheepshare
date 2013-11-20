package tz.common.cache;

/**
 * @author ignudiks
 *
 * 캐시 Object를 저장하기 위한 객체이다.
 */
public class CacheNode {

	protected long timeoutTime;

	private final Object key;

	private Object value;

	protected int hitCount;

	/**
	 * 기본 생성자
	 *
	 * @param key
	 *            저장할 Object의 key 값
	 * @param value
	 *            저장할 Object
	 * @param timeoutTime
	 *            저장시점의 시간
	 */
	public CacheNode(Object key, Object value, long timeoutTime) {
		this.timeoutTime = timeoutTime;
		this.key = key;
		this.value = value;
		this.hitCount = 0;
	}

	/**
	 * 저장시점의 시간 비교를 통해 소멸 여부를 판별한다.
	 *
	 * @return
	 */
	public final boolean isExpired() {
		long remnantTime = timeoutTime - System.currentTimeMillis();
		return (remnantTime <= 0);
	}

	/**
	 * 저장한 Object에 대한 Getter
	 *
	 * @return
	 */

	public Object getValue() {
		return value;
	}

	/**
	 * 저장할 Object에 대한 Setter
	 *
	 * @param value
	 */

	public void setValue(Object value) {
		this.value = value;
	}

	public String toString() {
		return (String) key + ":" + value;
	}

	/**
	 * 비교를 위한 메소드이다. 비교 수행시 key 값과 value 값이 동일할 경우 같은 값으로 취급한다.
	 */

	public boolean equals(Object obj) {
		if (obj instanceof CacheNode) {
			CacheNode node = (CacheNode) obj;
			return (node.getKey().equals(key)) && (node.getValue().equals(value));
		}
		return false;
	}

	/**
	 * 저장한 key 값에 대한 Getter
	 *
	 * @return
	 */
	public Object getKey() {
		return key;
	}
}


