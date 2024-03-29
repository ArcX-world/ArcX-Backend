package avatar.util.excel;

@FunctionalInterface
public interface ExcelObjectBuilder<T> {

	T build(ExcelResultSet ers);

}