package tz.basis.core.dataset;

import java.util.ArrayList;
import java.util.List;

import tz.basis.core.exception.RowStatusException;
import tz.basis.data.GridData;
import tz.basis.data.RowStatus;
import tz.basis.data.RowStatusCallback;

/**
 *
 * row data와 row status를 group로 가지는 class
 * Grid를 loop 돌면서 하나씩 insert, update, delete 하는 대신 data와 status를 가지는 list 객체로 한꺼번에 DAO로 넘길 수 있음.
 *
 * @author TZ
 *
 */
public class GridDataImpl<E> implements GridData<E> {

    private List<E> list;

    private List<E> orgList;

    private List<RowStatus> rowStatusList;

    /**
     * Method description : Constructor
     */
    public GridDataImpl(DS dataSet, Class<E> clazz, String filter) {
        this.list = new ArrayList<E>();
        this.orgList = new ArrayList<E>();
        this.rowStatusList = new ArrayList<RowStatus>();

        if(dataSet != null){
            for(int i = 0; i < dataSet.getRowCount(); i++){
                E bean = dataSet.getBean(clazz, i, filter);

                this.list.add(bean);
                this.orgList.add(dataSet.getOrgDataBean(clazz, i));

                RowStatus rowStatus = dataSet.getRowStatus(i);

                if(rowStatus == null){
                    if(rowStatus == null){
                        throw new RowStatusException("DS={" + dataSet.getId() + "}의 rowStatus가 누락 되었습니다.");
                    }
                }

                this.rowStatusList.add(dataSet.getRowStatus(i));
            }
        }
    }

    /* (non-Javadoc)
     * @see tz.basis.data.GridData#get(int)
     */
    public E get(int rowIndex){
        return list.get(rowIndex);
    }

    /* (non-Javadoc)
     * @see tz.basis.data.GridData#getList()
     */
    public List<E> getList(){
        return list;
    }

    /* (non-Javadoc)
     * @see tz.basis.data.GridData#getStatusOf(int)
     */
    public RowStatus getStatusOf(int rowIndex){
        return rowStatusList.get(rowIndex);
    }

    /* (non-Javadoc)
     * @see tz.basis.data.GridData#size()
     */
    public int size(){
        return list == null ? 0 : list.size();
    }

    /* (non-Javadoc)
     * @see tz.basis.data.GridData#forEachRow()
     */
    public void forEachRow(RowStatusCallback<E> callback){
        for(int i = 0; i < list.size(); ++i){
            switch(getStatusOf(i)){
                case INSERT:
                    callback.insert(get(i), i);
                    break;
                case DELETE:
                    callback.delete(get(i), i);
                    break;
                case UPDATE:
                    callback.update(get(i), orgList.get(i), i);
                    break;
            }
        }
    }
}
