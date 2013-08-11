package tz.basis.core.dataset.io;

import tz.basis.core.mvc.TzDSAccessor;

/**
 *
 * @author TZ
 *
 * DSMap의 data를 반환하여 output stream으로 전송
 */
public interface DSWriter {

    void write();

    void setDSAccessor(TzDSAccessor accessor);
}
