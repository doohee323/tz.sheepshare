package tz.basis.core.mvc.context;

import org.springframework.core.NamedInheritableThreadLocal;

/**
 *
 * @author TZ
 * TzRequestContext를 주고받는 통신을 Thread로 처리한다.
 */
public class TzRequestContextHolder {

    private static final String TZ_CONTEXT_NAME = "TZ Request Context";

    private static final ThreadLocal<TzRequestContext> INHERIABLE_TZ_CONTEXT_HOLDER = new NamedInheritableThreadLocal<TzRequestContext>(
            TZ_CONTEXT_NAME);

    private TzRequestContextHolder() {}

    /**
     * <pre>
     * 현재 쓰레드에 {@link TzRequestContext}를 바인딩.
     * 만약 현재 쓰레드에 이미 바인딩 되어 있는 객체가 있는 경우에는 덮어쓰게 되니 주의 요망.
     * </pre>
     *
     * @param context
     */
    public static void set(TzRequestContext context){
        INHERIABLE_TZ_CONTEXT_HOLDER.set(context);
    }

    /**
     * <pre>
     * 현재 쓰레드에 바인딩 되어 있는 {@link TzRequestContext} 객체 반환.
     * 만약 없을 경우에는 내부적으로 {@link TzRequestContext}을 생성하여 쓰레드에 바인딩 하고 객체를 반환 함
     * </pre>
     *
     * @return 현재 쓰레드에 바인딩 되어 있는 {@link TzRequestContext} 객체
     */
    public static TzRequestContext get(){
        TzRequestContext tzRequestContext = INHERIABLE_TZ_CONTEXT_HOLDER.get();

        if(tzRequestContext == null){
            tzRequestContext = new TzRequestContext();
            INHERIABLE_TZ_CONTEXT_HOLDER.set(tzRequestContext);
        }

        return tzRequestContext;
    }

    /**
     * ThreadLocal 리소스를 반환
     */
    public static void clear(){
        if(INHERIABLE_TZ_CONTEXT_HOLDER.get() != null){
            INHERIABLE_TZ_CONTEXT_HOLDER.remove();
        }
    }
}
