package com.pku.test;

import com.pku.model.Edge;
import com.pku.model.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Test{
    Graph graph;//源数据形成的图
    String[] mashup;//等同于mashup.csv的数据集
    private int n;//=mashup.length

    //以下都是TestOne中对应属性的合集
    private String[] output;
    private int[] numberOfNodes;
    private long[] weight;
    private int[] hit;
    private long[] computationTime;
    private long[] spaceUsed;
    private double[] score;
    //以下都是TestOne中对应属性的平均值
    private double avgNumberOfNodes;
    private double avgWeight;
    private double avgHit;
    private double avgComputationTime;
    private double avgSpaceUsed;
    private double avgScore;


    public Test(Graph graph){
        this.graph=graph;

        mashup=new String[graph.getEdgeWeightMap().keySet().size()];
        int i=0;
        for (Edge edge : graph.getEdgeWeightMap().keySet()) {
            mashup[i++]=edge.getSource().getName()+","+edge.getTarget().getName();
        }

        initialize();
    }
    //以下函数只是用于构造函数里
    private void initialize(){
        n=mashup.length;
        output=new String[n];
        numberOfNodes=new int[n];
        weight = new long[n];
        hit = new int[n];
        computationTime = new long[n];
        spaceUsed = new long[n];
        score = new double[n];

        double totalNumberOfNodes=0.0;
        double totalWeight=0.0;
        double totalHit=0.0;
        double totalComputationTime=0.0;
        double totalSpaceUsed=0.0;
        double totalScore=0.0;

        int i=0;
        for(String apis:mashup){
            TestOne testOne=new TestOne(graph,mashup[i].split(","));
            output[i]=testOne.getOutput();
            numberOfNodes[i]=testOne.getNumberOfNodes();
            weight[i]=testOne.getWeight();
            hit[i]=testOne.getHit();
            computationTime[i]=testOne.getComputationTime();
            spaceUsed[i]=testOne.getSpaceUsed();
            score[i]=testOne.getScore();

            totalNumberOfNodes+=numberOfNodes[i];
            totalWeight+=weight[i];
            totalHit+=hit[i];
            totalComputationTime+=computationTime[i];
            totalSpaceUsed+=spaceUsed[i];
            totalScore+=score[i];

            i++;
        }

        avgNumberOfNodes=totalNumberOfNodes/ n;
        avgWeight=totalWeight/ n;
        avgHit=totalHit/ n;
        avgComputationTime=totalComputationTime/ n;
        avgSpaceUsed=totalSpaceUsed/ n;
        avgScore=totalScore/ n;
    }

    //getter：
    public int getN(){
        return n;
    }
    public String[] getOutput(){
        return output;
    }
    public int[] getNumberOfNodes() {
        return numberOfNodes;
    }
    public long[] getWeight() {
        return weight;
    }
    public int[] getHit() {
        return hit;
    }
    public long[] getComputationTime() {
        return computationTime;
    }
    public long[] getSpaceUsed() {
        return spaceUsed;
    }
    public double[] getScore() {
        return score;
    }
    public double getAvgNumberOfNodes() {
        return avgNumberOfNodes;
    }
    public double getAvgWeight() {
        return avgWeight;
    }
    public double getAvgHit() {
        return avgHit;
    }
    public double getAvgComputationTime() {
        return avgComputationTime;
    }
    public double getAvgSpaceUsed() {
        return avgSpaceUsed;
    }
    public double getAvgScore() {
        return avgScore;
    }

    public boolean recordToCsv(){
        String outputFile = "src/main/resources/data/results.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("api1,category1,api2,category2,outputs," +
                    "numberOfNodes,weight,hit,computationTime,spaceUsed,score");
            writer.newLine();
            for (int i = 0; i < mashup.length; i++) {
                String api1=mashup[i].split(",")[0];
                String category1=graph.getApiNodeMap().get(api1).getCategory();
                String api2=mashup[i].split(",")[1];
                String category2=graph.getApiNodeMap().get(api2).getCategory();
                writer.write(api1+","+category1+","+api2+","+category2+","+getOutput()[i]+","+
                        getNumberOfNodes()[i]+","+getWeight()[i]+","+getHit()[i]+","+getComputationTime()[i]+","+getSpaceUsed()[i]+","+getScore()[i]);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void run(Graph graph){
        Test test=new Test(graph);
        System.out.println("最小斯坦纳树的平均节点数：" + test.getAvgNumberOfNodes());
        System.out.println("最小斯坦纳树的平均权重和: " + test.getAvgWeight());
        System.out.println("测试数据的hit rate：" + test.getAvgHit());
        System.out.println("算法的平均时间消耗（以毫秒为单位）：" + test.getAvgComputationTime());
        System.out.println("算法的平均空间消耗（以字节为单位）：" + test.getAvgSpaceUsed());
        System.out.println("所有测试数据的平均得分: " + test.getAvgScore());
        System.out.println("测试数据的总条数: " + test.getN());
        if(test.recordToCsv()){
            System.out.println("results.csv文件写入完成！");
        }
    }
}
