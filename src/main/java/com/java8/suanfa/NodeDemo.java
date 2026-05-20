package com.java8.suanfa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class NodeDemo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int number;
    private NodeDemo left;
    private NodeDemo right;

    private int leftType; // 0-左子树 1-前驱节点
    private int rightType; // 0-右子树 1-后继节点

    // 初始化一些数据
    public static NodeDemo init() {
        NodeDemo node1 = NodeDemo.builder().name("1").number(1).build();
        NodeDemo node2 = NodeDemo.builder().name("2").number(2).build();
        NodeDemo node3 = NodeDemo.builder().name("3").number(3).build();
        NodeDemo node4 = NodeDemo.builder().name("4").number(4).build();
        NodeDemo node5 = NodeDemo.builder().name("5").number(5).build();
        NodeDemo node6 = NodeDemo.builder().name("6").number(6).build();
        NodeDemo node7 = NodeDemo.builder().name("7").number(7).build();

        node1.setLeft(node2);
        node1.setRight(node3);

        node2.setLeft(node4);
        node2.setRight(node5);

        node3.setLeft(node6);
        node3.setRight(node7);
        return node1;
    }

    // 把对象转为 json
    public String toJson() {
        return JSON.toJSONString(this,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.PrettyFormat,
                SerializerFeature.SortField
        );
    }

    public void preOrder() {
        preOrder(this);
    }

    public static void preOrder(NodeDemo node) {
        if (node == null) return;
        System.out.println("打印当前");
        System.out.println(node.getName());
        if (node.getLeft() != null) {
            System.out.println("迭代左边");
            preOrder(node.getLeft());
        }
        if (node.getRight() != null) {
            System.out.println("迭代右边");
            preOrder(node.getRight());
        }
    }


    //public static void main(String[] args) {
    //    NodeDemo nodeDemo = NodeDemo.init();
    //    System.out.println(nodeDemo.toJson());
    //}

    public void threadedNodeDemo(NodeDemo nodeDemo) {
        // 如果nodeDemo为空，不能线索化
        if (Objects.isNull(nodeDemo)) {
            return;
        }

        // 只有叶子节点才能线索化


    }
}
