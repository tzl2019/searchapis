package com.pku.algorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.pku.model.*;

public class MiniSteinerTree {
    Graph graph; // Web API构成的图
    Set<Node> miniSteinerTree; //最小斯坦纳树构成的API集合
    DpRecord[][] dpRecord; // dpRecord(i,S) 记录dp(i,S) 是由哪个状态转移过来的。
    // dp[i][S]表示以i为根，连通状态是S的连通图最小权重和。S是一个二进制数，表示连通图中是否包含k个关键词，如果连通图中包含第i个关键词，那么S第i位是1。
    long[][] dp;
    String[] keywords; //表示一组关键词
    long minimumValue = Integer.MAX_VALUE; //最小斯坦纳树的边权之和

    public MiniSteinerTree(Graph graph, String[] keywords) {
        this.graph = graph;
        this.keywords = keywords;
        this.miniSteinerTree = new HashSet<>();
        int n = graph.nodeCount, k = keywords.length;
        this.dp = new long[n][1 << k]; // 存储状态压缩DP的结果，表示以i为根节点状态S时的最小权重和
        this.dpRecord = new DpRecord[n][1 << k];
    }

    /*
     * 求解最小斯坦纳树
     */
    public void getMiniSteinerTree() {
        int n = graph.nodeCount, k = keywords.length;

        // 初始化距离和状态数组
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE); // 初始化状态DP为最大值
        }

        // 标记关键节点，并初始化状态DP值
        for (int i = 0; i < k; i++) {
            List<Node> wepAPIList = graph.categoryNodeMap.get(keywords[i]);
            for (Node node : wepAPIList) {
                int x = graph.apiNodeMap.get(node.getName()).id;
                dp[x][1 << i] = 0; // 以输入的关键点为根，二进制中对应关键节点位置为1的dp总权重为0
            }
        }

        // 使用状态压缩DP求解最小路径和
        // 遍历从单个节点开始，直到满足所有关键节点都连同的所有可能状态
        for (int s = 1; s < 1 << k; s++) {
            // 遍历以节点i为根节点，状态为s时的情况
            for (int i = 0; i < n; i++) {
                // 遍历s二进制的所有子集，执行i的度数大于1时的操作
                for (int t = s & (s - 1); t > 0; t = (t - 1) & s) {
                    if (dp[i][t] + dp[i][s ^ t] < dp[i][s]) {
                        dp[i][s] = dp[i][t] + dp[i][s ^ t];
                        dpRecord[i][s] = new DpRecord(new DpState(graph.nodeList.get(i), t, dp[i][t]),
                                new DpState(graph.nodeList.get(i), s ^ t, dp[i][s ^ t]));
                    }
                }
            }
            // 处理状态为s的情况下最短路径
            deal(s);
        }
        calculateEdgeSum();
    }

    /*
     * 计算最小斯坦纳树的边权之和
     */
    private void calculateEdgeSum() {
        int s = (1 << keywords.length) - 1;

        DpState state = null;
        for (int i = 0; i < graph.nodeCount; i++) {
            if (minimumValue > dp[i][s]) {
                minimumValue = dp[i][s];
                state = new DpState(graph.nodeList.get(i), s, dp[i][s]);
            }
            minimumValue = Math.min(minimumValue, dp[i][s]);

        }
        System.out.println("最小斯坦纳树权重和: " + minimumValue); // 输出结果
        findNodes(state);
        System.out.println("最小斯坦纳树包含"+miniSteinerTree.size()+"个Web API，分别是：");
        for (Node node : miniSteinerTree) {
            System.out.println(node.getName());
        }

    }

    /*
     * 找到最小斯坦纳树的点集
     */
    private void findNodes(DpState state) {
        if (state == null)
            return;
        miniSteinerTree.add(state.node);

        DpRecord record = dpRecord[state.node.id][state.visited];
        if (record == null)
            return;
        findNodes(record.fromState1);
        findNodes(record.fromState2);
    }

    /*
     * 处理状态s下的最短路径情况,实现dp(e.source,S)+e.weight→dp(e.target,S)状态转移方程
     */
    private void deal(int s) {  
        int n = graph.nodeCount;
        // 建立优先队列，数组中索引值为1的数字越小，优先级越高
        PriorityQueue<DpState> pq = new PriorityQueue<>((o1, o2) -> Long.compare(o1.value, o2.value));
        boolean[] vis = new boolean[n]; // 记录已经访问的节点

        // 遍历所有节点，将所有满足当前状态的节点加入优先队列
        for (int i = 0; i < graph.nodeCount; i++) {
            if (dp[i][s] < Integer.MAX_VALUE) {
                pq.add(new DpState(graph.nodeList.get(i), s, dp[i][s]));
            }
        }

        // 使用Dijkstra算法求解最短路径
        // 即求解以每个节点为顶点，满足状态s（包含对应的点）的权重和的最小值
        while (!pq.isEmpty()) {
            DpState tmp = pq.poll();
            Node source = tmp.node;
            // 如果已经访问，则跳过
            if (vis[source.id]) {
                continue;
            }
            vis[source.id] = true;

            source.neighborEdge.forEach((edge) -> {
                int i = edge.getTarget().id;
                if (tmp.value + edge.getWeight() < dp[i][s]) {
                    dp[i][s] = (tmp.value + edge.getWeight());
                    dpRecord[i][s] = new DpRecord(tmp, null);
                    pq.add(new DpState(edge.getTarget(), s, dp[i][s]));
                }
            });
        }
    }
}

/*
 * 将dp(i,S)封装成DpState（i,S,dp(i,S)）
 */
class DpState {
    Node node;
    int visited;
    long value;

    DpState(Node node, int visited, long value) {
        this.node = node;
        this.visited = visited;
        this.value = value;
    }
}

/*
 * dpRecord(i,S) 记录dp(i,S) 是由哪个状态转移过来的
 */
class DpRecord {
    DpState fromState1;
    DpState fromState2;

    DpRecord(DpState fromState1, DpState fromState2) {
        this.fromState1 = fromState1;
        this.fromState2 = fromState2;
    }
}