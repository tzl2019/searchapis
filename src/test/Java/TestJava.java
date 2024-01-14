import com.pku.model.Graph;
import com.pku.test.Test;
import com.pku.test.TestOne;
import com.pku.util.CsvParser;

public class TestJava {
    public static void main(String[] args) {
        String dataDir = "src/main/resources/data/";
        String mashupInput = dataDir + "mashup.csv";
        String mashupOutput = dataDir + "api_relationship.csv";
        String apisInput = dataDir + "api.csv";
        String apisOutput = dataDir + "api_category.csv";

        CsvParser csvParser = new CsvParser();
        csvParser.getAPICalls(mashupInput, mashupOutput);
        csvParser.getAPICats(apisInput, apisOutput);

        //--------------------测试部分--------------------
        Graph graph = csvParser.getGraph(mashupOutput, apisOutput);
        //TestOne.run(graph);//单次测试
        Test.run(graph);//测试mashup.csv中的所有的category属性存在的api对
    }
}
