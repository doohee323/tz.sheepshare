package tz.basis.core.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import tz.basis.core.dataset.io.DSWriter;
import tz.basis.core.mvc.context.TzRequestContextHolder;

/**
*
* 요청 처리중 생성된 데이터를 클라이언트로 전송한다.
*
* @author TZ
*
*/
public class TzView extends AbstractView {

    private static final Logger logger = LoggerFactory.getLogger(TzView.class);

    /*
     * (non-Javadoc)
     * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception{

        if(logger.isDebugEnabled()){
            logger.trace("[TZ] {} 응답 데이터 렌더링 작업을 수행합니다.", new Object[] { TzRequestContextHolder.get()
                    .getTzChnlType() });
        }

        if(!response.isCommitted()){
            DSWriter writer = TzRequestContextHolder.get().getDSWriter();

            if(writer != null){
                writer.write();
            }
        }
    }

}
