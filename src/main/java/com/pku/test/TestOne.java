package com.pku.test;

import com.pku.algorithm.MiniSteinerTree;
import com.pku.model.Graph;
import com.pku.model.Node;

import java.util.Set;

public class TestOne {
    private Graph graph;//源数据形成的图

    private String output;//保存斯坦纳树算法所生成的webapis

    private int numberOfNodes;//返回的Web APIs个数(越小越好)
    private long weight;//最小群Steiner Tree的权重和(越小越好)
    private int hit;//是否命中，1为命中，0为没命中
    //算法的收敛性分析：
    private long computationTime;//时间复杂度分析：生成一个Web APIs结果的时间开销
    private long spaceUsed;//空间复杂度分析：生成一个Web APIs结果的空间开销
    //long convergence=;//算法的收敛性

    private double score;//单个输入的得分

    public TestOne(Graph graph, String[] apis) {
        this.graph=graph;
        String[] keywords = getCategoriesFromApis(apis);

        //各评分项的评分权重
        double weight_numberOfNodes = 1;
        double weight_weight = 1;
        double weight_hit = 100;
        double weight_computationTime = -0.2;
        double weight_spaceUsed = -0.000002;

        long startTime = System.currentTimeMillis();
        long startMemory = Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory();
        MiniSteinerTree miniSteinerTree = new MiniSteinerTree(graph, keywords);
        miniSteinerTree.getMiniSteinerTree();
        long endTime = System.currentTimeMillis();
        long endMemory = Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory();

        output="";
        for(Node node:miniSteinerTree.getNodes()){
            output+= node.getName()+";";
        }

        numberOfNodes = miniSteinerTree.getNodes().size();
        weight = miniSteinerTree.getMinimumValue();
        hit = hit(apis, getApisFromNodes(miniSteinerTree.getNodes()));
        computationTime = endTime - startTime;
        spaceUsed = endMemory - startMemory;

        //输出调试：
        //System.out.println(numberOfNodes + " " + weight + " " + hit);
        //System.out.println(computationTime + " " + spaceUsed);

        score = numberOfNodes * weight_numberOfNodes
                + weight * weight_weight
                + hit * weight_hit
                + computationTime * weight_computationTime
                + spaceUsed * weight_spaceUsed
                + 0.00;
    }
    //以下函数只是用于构造函数里
    public String[] getCategoriesFromApis(String[] apis) {
        String[] keywords = new String[apis.length];
        for (int i = 0; i < apis.length; i++) {
            keywords[i] = graph.getApiNodeMap().get(apis[i]).getCategory();
        }
        return keywords;
    }
    public static String[] getApisFromNodes(Set<Node> nodes) {
        String[] keywords = new String[nodes.size()];
        int i = 0;
        for (Node node : nodes) {
            keywords[i++] = node.getName();
        }
        return keywords;
    }
    public int hit(String[] apis, String[] webApis) {
        //2个api的category相同的话，只输出了其中一个api也算hit
        if(graph.getApiNodeMap().get(apis[0]).getCategory().
                equals(graph.getApiNodeMap().get(apis[1]).getCategory())){
            for (String api : apis){
                for (String webApi : webApis) {
                    if (api.equals(webApi)) {
                        return 1;
                    }
                }
            }
            return 0;
        }

        for (String api : apis) {
            boolean found = false;
            for (String webApi : webApis) {
                if (api.equals(webApi)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return 0;
            }
        }
        return 1;
    }

    //getter：
    public String getOutput() {
        return output;
    }
    public int getNumberOfNodes() {
        return numberOfNodes;
    }
    public long getWeight() {
        return weight;
    }
    public int getHit() {
        return hit;
    }
    public long getComputationTime() {
        return computationTime;
    }
    public long getSpaceUsed() {
        return spaceUsed;
    }
    public double getScore() {
        return score;
    }

    public static void run(Graph graph){
        String[] apis1 = {"Twitter","Facebook"};
        String[] apis2 = {"Google Ad Manager","YouTube"};
        String[] apis3 = {"Google Webmaster Tools","Amazon Product Advertising"};

        System.out.println("此次测试的api对为"+apis1[0]+"和"+apis1[1]+"：");
        TestOne testOne1=new TestOne(graph,apis1);
        System.out.println();

        System.out.println("此次测试的api对为"+apis2[0]+"和"+apis2[1]+"：");
        TestOne testOne2=new TestOne(graph,apis2);
        System.out.println();

        System.out.println("此次测试的api对为"+apis3[0]+"和"+apis3[1]+"：");
        TestOne testOne3=new TestOne(graph,apis3);
        System.out.println();

    }
}
