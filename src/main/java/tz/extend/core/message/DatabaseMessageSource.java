package tz.extend.core.message;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tz.extend.config.Constants;
import tz.extend.core.message.ux.MessageSourceGenerator;
import tz.extend.query.CommonDao;
import tz.extend.util.StringUtil;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : DATABASE에 관리되는 메세지 정보를 원천으로 하여 Message ResourceBundle 을 생성한다.
 * 작 성 자 : TZ
 * 작성일자 : 2013-02-01
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-02-01             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class DatabaseMessageSource extends AbstractMessageSource implements InitializingBean {

    /**
     * DB 처리를 위한 공통 dao
     */
    @Autowired
    @Qualifier("mainDB")
    private CommonDao dao;

    private final Map<Locale, ResourceBundle> cachedResourceBundles = new HashMap<Locale, ResourceBundle>();

    private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats = new HashMap<ResourceBundle, Map<String, Map<Locale, MessageFormat>>>();

    /**
     * <pre>
     * 주어진 코드와 매핑되는 메세지를 반환한다.
     * </pre>
     *
     * @param code message code
     * @param locale locale code
     */
    @Override
    protected MessageFormat resolveCode(String code, Locale locale){
        MessageFormat messageFormat = null;

        ResourceBundle bundle = getResourceBundle(locale);

        if(bundle != null){
            messageFormat = getMessageFormat(bundle, code, locale);
        }

        return messageFormat;
    }

    /**
     *
     * <pre>
     *  국가별 메세지를 조회한다.
     * </pre>
     *
     * @param locale locale code
     * @return resource bundle
     */
    protected ResourceBundle getResourceBundle(Locale locale){
        synchronized(this.cachedResourceBundles){
            ResourceBundle bundle = this.cachedResourceBundles.get(locale);

            if(bundle != null){
                return bundle;
            }

            if(logger.isWarnEnabled()){
                logger.warn("ResourceBundle [Locale : " + locale + "] not found for MessageSource");
            }

            return null;
        }
    }

    /**
     *
     * <pre>
     *  주어진 리소스 번들을 이용하여 각 국가별 정의되어 있는 메세지 포멧을 조회한다.
     * </pre>
     *
     * @param bundle resource bundle
     * @param code message code
     * @param locale locale code
     * @return 메세지 포멧
     * @throws MissingResourceException 메세지 포멧 조회시 발생할 수 있는 예외
     */
    protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale)
            throws MissingResourceException{

        synchronized(this.cachedBundleMessageFormats){
            Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedBundleMessageFormats.get(bundle);

            Map<Locale, MessageFormat> localeMap = null;

            if(codeMap != null){
                localeMap = codeMap.get(code);

                if(localeMap != null){
                    MessageFormat result = localeMap.get(locale);

                    if(result != null){
                        return result;
                    }
                }
            }

            String msg = getStringOrNull(bundle, code);

            if(msg != null){
                if(codeMap == null){
                    codeMap = new HashMap<String, Map<Locale, MessageFormat>>();
                    this.cachedBundleMessageFormats.put(bundle, codeMap);
                }

                if(localeMap == null){
                    localeMap = new HashMap<Locale, MessageFormat>();
                    codeMap.put(code, localeMap);
                }

                MessageFormat result = createMessageFormat(convertMessageFormat(msg), locale);
                localeMap.put(locale, result);
                return result;
            }

            return null;
        }
    }

    private static final Pattern pattern = Pattern.compile("@(\\d*)*");

    /**
     *
     * <pre>
     *  메세지 매핑구분자를 변경한다. (TZ의 메세지 매핑 구분자인 @를 {} 매핑으로 변경함)
     * </pre>
     *
     * @param message 리소스번들로 사용할 표준메세지 포멧
     * @return 프레임워크 표준으로 변경된 메세지 포멧
     */
    private String convertMessageFormat(String message){
        Matcher matcher = pattern.matcher(message);

        StringBuffer builder = new StringBuffer();

        int index = 0;

        while(matcher.find()){
            String part = matcher.group(1).toLowerCase();

            if(StringUtils.hasText(part)){
                part = String.format("{%s}", part);
            }else{
                part = String.format("{%d}", index++);

            }

            matcher.appendReplacement(builder, part);
        }

        matcher.appendTail(builder);

        return builder.toString();
    }

    private String getStringOrNull(ResourceBundle bundle, String key){
        try{
            return bundle.getString(key);
        }catch(MissingResourceException ex){
            return null;
        }
    }

    public void afterPropertiesSet() throws Exception{
        synchronized(cachedResourceBundles){
            loadResourceBundles();
        }
    }

    /**
     * 메시지를 리로드한다.
     * @throws Exception
     */
    public void reloadMessage() throws Exception{
        afterPropertiesSet();
    }

    /**
     *
     * <pre>
     *  저장소에 있는 메세지 포멧을 로드한다.
     * </pre>
     *
     */
    protected void loadResourceBundles(){
        List<DefaultMessage> messageResources = new ArrayList<DefaultMessage>();

        // ibatis 처리
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("sysCd", Constants.sysCd);
        List<Map<String, Object>> mData = dao.queryForMapList("message.retrieveDefaultMessage", input);
        for(Map<String, Object> e : mData){
            DefaultMessage messageResource = new DefaultMessage();
            messageResource.setLocale(StringUtil.getText(e.get("locale")));
            messageResource.setCode(StringUtil.getText(e.get("code")));
            messageResource.setMessage(StringUtil.getText(e.get("message")));
            messageResources.add(messageResource);
        }

        Map<String, List<String[]>> resources = new HashMap<String, List<String[]>>();

        for(DefaultMessage message : messageResources){
            List<String[]> messages = resources.get(message.getLocale());

            if(messages == null){
                messages = new ArrayList<String[]>();
                resources.put(message.getLocale(), messages);
            }

            messages.add(new String[] { message.getCode(), message.getMessage() });
        }

        for(final Map.Entry<String, List<String[]>> e : resources.entrySet()){
            String[] locale = e.getKey().split("_");

            if(locale.length != 2){
                continue;
            }

            cachedResourceBundles.put(new Locale(locale[0].trim(), locale[1].trim()), new ListResourceBundle() {

                @Override
                protected Object[][] getContents(){
                    Object[][] contents = new Object[e.getValue().size()][];

                    for(int i = 0; i < contents.length; ++i){
                        contents[i] = e.getValue().get(i);
                    }

                    return contents;
                }
            });
        }
    }

    /**
     *
     * <pre>
     *  주어진 code와 argument로 메세지를 생성하여 반환한다.
     * </pre>
     *
     * @param code message code
     * @param args resource bundle로 전달될 argument
     * @return 메세지 포멧과 argument를 조합하여 생성된 메세지
     * @throws NoSuchMessageException 사용되지 않음.
     */
    public final String getMessage(String code, Object[] args) throws NoSuchMessageException{
        Locale locale = null;
        try{
            locale = LocaleManager.getUserLocale();
        }catch(Exception e){
            locale = Locale.getDefault();
        }

        String msg = getMessageInternal(code, args, locale);

        if(msg != null){
            return msg;
        }

        String fallback = getDefaultMessage(code);

        if(fallback != null){
            return fallback;
        }

        // 메시지가 없다고 해서 에러를 내서는 안됨.
        return code;
    }
}
