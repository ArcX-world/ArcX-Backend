package avatar.util.excel;

/**
 * 测试excel
 */
public class TestExcel {
    public static void main(String[] args){
        testExcel();
    }

    static  String SHEET_NAME = "活动";

    public static void testExcel(){

        MyXlsReader reader = ExcelManager.getInstance().buildReader("测试.xls");

        for (int rowIndex = 2; rowIndex < reader.getSheet(SHEET_NAME).getRows(); rowIndex++) {
            TestConfig config = reader.getScalarRowData(SHEET_NAME, rowIndex, XML_BUILDER);
            System.out.println("id ======= " + config.id);
            System.out.println("name ======= " + config.name);
        }

        reader.close();
    }



    private static ExcelObjectBuilder<TestConfig> XML_BUILDER = new ExcelObjectBuilder<TestConfig>() {
        @Override
        public  TestConfig build(ExcelResultSet rs) {
            int id = rs.getInt("Id");
            String name = rs.getString("字段");
            TestConfig config = new TestConfig(id, name);
            return config;
        }
    };


    private static class TestConfig{
        private int id;
        private String name;

        TestConfig(int id , String name){
            this.id = id;
            this.name = name;
        }
    }
}


