package tz.basis.core.dataset;

/**
*
* @author TZ
* column name과 type group를 가지는 class
*/
public class DSColumn {

    private String columnName;

    private Class<?> columnType;

    /**
     *
     * Method description : Constructor
     */
    public DSColumn() {}

    /**
     *
     * Method description : Constructor
     */
    public DSColumn(String columnName, Class<?> columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    /**
     * column 이름 반환
     */
    public String getColumnName(){
        return columnName;
    }

    /**
     * column 이름 지정
     */
    public void setColumnName(String columnName){
        this.columnName = columnName;
    }

    /**
     * column type 반환
     */
    public Class<?> getColumnType(){
        return columnType;
    }

    /**
     * column type 지정
     */
    public void setColumnType(Class<?> columnType){
        this.columnType = columnType;
    }
}