package tz.basis.query.core.evaluator;

/**
 *
 * @author TZ
 *
 */
public class CallMetaData {

    private String schemaName;
    private String catalogName;
	private String procedureName;
	private boolean isFunction;

	public CallMetaData(String schemaName, String catalogName, String procedureName, boolean isFunction) {
		this.schemaName = schemaName;
        this.catalogName = catalogName;
        this.procedureName = procedureName;
		this.isFunction = isFunction;
	}

    public String getSchemaName() {
        return schemaName;
    }

    public String getCatalogName() {
        return catalogName;
    }

	public String getProcedureName() {
		return procedureName;
	}

	public boolean isFunction() {
		return isFunction;
	}

}
