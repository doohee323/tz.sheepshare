package tz.basis.core.ux.comweb.mvc;

import tz.basis.core.mvc.adaptor.AbstractTzRequest;
import tz.basis.core.ux.comweb.dataset.ComWebDSReader;

/**
 *
 * @author TZ
 *
 */
public class ComWebRequest extends AbstractTzRequest {

    public ComWebRequest(ComWebDSReader reader) {
        attachments = reader.getAttachments();
        paramMap = reader.getParamMap();
    }
}
