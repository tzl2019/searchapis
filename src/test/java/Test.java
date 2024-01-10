import java.util.HashMap;
import java.util.Map;
import com.pku.model.*;;;
public class Test {
    public static void main(String[] args) {
        Map<Edge, String> edgeMap = new HashMap<>();

        Edge edge1 = new Edge("A", "B", 10);
        Edge edge2 = new Edge("B", "A", 10);
        Edge edge3 = new Edge("B", "A", 1);

        System.out.println(edge1.hashCode());
        System.out.println(edge2.hashCode());
        System.out.println(edge3.hashCode());

        edgeMap.put(edge1, "Edge 1");   
        System.out.println(edgeMap.get(edge2)); 
    }
}
