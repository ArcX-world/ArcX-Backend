package avatar.util.excel;

public class ScalarExcelBuilder
{
	public final static ExcelObjectBuilder<Integer> intResultBuilder;
	public final static ExcelObjectBuilder<String> stringResultBuilder;

	static {
		intResultBuilder = new ExcelObjectBuilder<Integer>() {
			public Integer build(ExcelResultSet rs){
				return rs.getInt(0);
			}
		};

		stringResultBuilder = new ExcelObjectBuilder<String>() {
			public String build(ExcelResultSet rs) {
				return rs.getString(0);
			}
		};
	}
}
