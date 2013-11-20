package tz.basis.core.dataset;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tz.basis.core.dataset.converter.StringToDateConverter;
import tz.basis.core.exception.TzException;
import tz.basis.data.RowStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.StringUtils;

/**
 *
 * UI에서 전달된 데이터를 공통된 형태로 추상화하여 제공한다.
 *
 * @author TZ
 *
 */
@SuppressWarnings("unchecked")
public class DSImpl implements DS {

    private static final Logger logger = LoggerFactory.getLogger(DSImpl.class);

    protected String id;

    protected List<DSColumn> columns;

    protected List<DSRow> rows;

    public DSImpl() {}

    public DSImpl(String id, List<DSColumn> cols, List<DSRow> rows) {
        this.id = id;
        this.columns = cols;
        this.rows = rows;
    }

    public final String getId(){
        return id;
    }

    public final int getColumnCount(){
        return columns == null ? 0 : columns.size();
    }

    public final int getRowCount(){
        return rows == null ? 0 : rows.size();
    }

    public final RowStatus getRowStatus(int row){
        if(row >= getRowCount()){
            throw new ArrayIndexOutOfBoundsException(row);
        }

        String rowStatus = rows.get(row).getRowStatus();

        if(rowStatus == null){
            return null;
        }

        return RowStatus.valueOf(rowStatus.toUpperCase());
    }

    public final DSRow getDSRow(int row){
        if(row >= getRowCount()){
            throw new ArrayIndexOutOfBoundsException(row);
        }

        return rows.get(row);
    }

    public final DSColumn getDSColumn(int col){
        if(col >= getColumnCount()){
            throw new ArrayIndexOutOfBoundsException(col);
        }

        return columns.get(col);
    }

    public final <E> E getOrgDataBean(Class<E> clazz, int row){
        DSRow dataSetRow = null;

        if(getRowStatus(row) == RowStatus.UPDATE){
            dataSetRow = getDSRow(row).getOrgDSRow();
        }

        if(dataSetRow == null){
            return null;
        }

        return findBeanGroup(clazz, dataSetRow, null);
    }

    public final <E> E getBean(Class<E> clazz, int row){
        return getBean(clazz, row, null);
    }

    public final <E> E getBean(Class<E> clazz, int row, String filter){
        return findBeanGroup(clazz, getDSRow(row), filter);
    }

    protected <E> E findBeanGroup(Class<E> clazz, DSRow dataSetRow, String filter){
        final E bean = newInstance(clazz);

        int columnCount = getColumnCount();

        if(Map.class.isAssignableFrom(clazz)){
            for(int col = 0; col < columnCount; col++){
                String columnName = getDataColunmName(col);

                if(StringUtils.hasText(filter)){
                    logger.debug("[TZ] 사용자 요청에 의한 유효성 체크. Expression={}", filter);

                    if(!isSelectedProperty(filter, columnName)){
                        continue;
                    }

                    if(isRequiredProperty(filter, columnName)){
                        Object value = dataSetRow.get(columnName);

                        if(value == null || (value instanceof String && !StringUtils.hasText((String)value))){
                            throw new TzException("Column[" + columnName + "] 는 요청[" + filter + "]에 의해 필수값으로 설정되었습니다.");
                        }
                    }
                }

                ((Map<String, Object>)bean).put(columnName, dataSetRow.get(columnName));
            }
        }else{
            ConfigurablePropertyAccessor propertyAccessor = PropertyAccessorFactory.forDirectFieldAccess(bean);
            GenericConversionService conversionService = ConversionServiceFactory.createDefaultConversionService();
            conversionService.addConverter(new StringToDateConverter());
            propertyAccessor.setConversionService(conversionService);

            for(int col = 0; col < columnCount; col++){
                String columnName = getDataColunmName(col);

                if(StringUtils.hasText(filter)){
                    logger.debug("[TZ] 사용자 요청에 의한 유효성 체크. Expression={}", filter);

                    if(!isSelectedProperty(filter, columnName)){
                        continue;
                    }

                    if(isRequiredProperty(filter, columnName)){
                        Object value = dataSetRow.get(columnName);

                        if(value == null || (value instanceof String && !StringUtils.hasText((String)value))){
                            throw new TzException("Column[" + columnName + "] 는 요청[" + filter + "]에 의해 필수값으로 설정되었습니다.");
                        }
                    }
                }

                try{
                    propertyAccessor.setPropertyValue(getDataColunmName(col), dataSetRow.get(getDataColunmName(col)));
                }catch(Exception e){
                    logger.debug("[TZ] DSImpl - getBean() : Class={" + clazz.getName() + "}, field:"
                            + getDataColunmName(col) + " 가 존재하지 않습니다.");
                }
            }
        }

        return bean;
    }

    protected final <E> E newInstance(Class<E> clazz){
        E bean;

        try{
            bean = clazz.newInstance();
        }catch(InstantiationException e){
            throw new TzException("InstantiationException", e);
        }catch(IllegalAccessException e){
            throw new TzException("IllegalAccessException", e);
        }

        return bean;
    }

    protected final String getDataColunmName(int index){
        return columns.get(index).getColumnName();
    }

    protected final Object getDSValue(int rowIndex, int colIndex){
        return rows.get(rowIndex).get(getDataColunmName(colIndex));
    }

    protected void setDataColumn(Object object, Class<?> dataType){
        if(columns != null && columns.size() > 0){
            return;
        }

        columns = new ArrayList<DSColumn>();

        if(dataType != null && !Map.class.isAssignableFrom(dataType)){
            Field[] fields = dataType.getDeclaredFields();

            for(Field field : fields){
                columns.add(new DSColumn(field.getName(), field.getType()));
            }
        }else{
            if(object != null){
                if(Map.class.isAssignableFrom(object.getClass())){
                    Iterator<String> it = ((Map<String, ?>)object).keySet().iterator();

                    while(it.hasNext()){
                        String columnName = it.next();
                        Class<?> columnType = null;

                        if(((Map<String, ?>)object).get(columnName) == null){
                            columnType = String.class;
                        }else{
                            columnType = ((Map<String, ?>)object).get(columnName).getClass();
                        }

                        columns.add(new DSColumn(columnName, columnType));
                    }
                }else{
                    Field[] fields = object.getClass().getDeclaredFields();

                    for(Field field : fields){
                        columns.add(new DSColumn(field.getName(), field.getType()));
                    }
                }
            }
        }
    }

    public void addRowBean(Object bean, Class<?> clazz){
        setDataColumn(bean, clazz);

        if(rows == null){
            rows = new ArrayList<DSRow>();
        }

        if(bean != null){
            rows.add(newDSRowFromBean(bean));
        }
    }

    public void addRowMap(Map<String, ?> map, Class<?> clazz){
        setDataColumn(map, clazz);

        if(rows == null){
            rows = new ArrayList<DSRow>();
        }

        if(map != null){
            rows.add(newDSRowFromMap(map));
        }
    }

    protected DSRow newDSRowFromBean(Object bean){
        DSRow row = new DSRowImpl();

        if(bean != null){
            ConfigurablePropertyAccessor propertyAccessor = PropertyAccessorFactory.forDirectFieldAccess(bean);

            GenericConversionService conversionService = ConversionServiceFactory.createDefaultConversionService();
            propertyAccessor.setConversionService(conversionService);

            for(int i = 0; i < this.getColumnCount(); i++){
                row.add(getDataColunmName(i), propertyAccessor.getPropertyValue(getDataColunmName(i)));
            }
        }

        return row;
    }

    protected DSRow newDSRowFromMap(Map<String, ?> map){
        DSRow row = new DSRowImpl();

        if(map != null){
            for(int i = 0; i < this.getColumnCount(); i++){
                row.add(getDataColunmName(i), map.get(getDataColunmName(i)));
            }
        }

        return row;
    }

    protected boolean isSelectedProperty(String expr, String property){
        boolean result = true;

        if(expr != null){
            result = expr.contains(property);
        }

        return result;
    }

    protected boolean isRequiredProperty(String expr, String property){
        return expr.contains(String.format("%s+", property));
    }
}