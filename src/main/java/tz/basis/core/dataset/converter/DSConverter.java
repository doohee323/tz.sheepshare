package tz.basis.core.dataset.converter;

import tz.basis.core.dataset.DS;

/**
 *
 * @author TZ
 *
 */
public interface DSConverter {

	boolean support(Object object);

	void convert(DS dataSet, Object object);

}
