package com.java8.suanfa;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Objects;

public class BianliErChaShun {

    public static void main(String[] args) {
        Node grandpa = new Node("爷爷");
        Node father = new Node("爸爸");
        Node uncle = new Node("叔叔");

        Node oneself = new Node("自己");
        Node elderSister = new Node("姐姐");

        Node elderBrother = new Node("哥哥");
        Node youngerSister = new Node("妹妹");

        grandpa.setLeft(father);
        grandpa.setRight(uncle);

        father.setLeft(oneself);
        father.setRight(elderSister);

        uncle.setLeft(elderBrother);
        uncle.setRight(youngerSister);

        System.out.println("前序遍历=========");
        preOrderIter(grandpa);
        System.out.println("中序遍历=========");
        middleOrderIter(grandpa);
        System.out.println("后序遍历=========");
        postOrderIter(grandpa);
        System.out.println("查询节点=========");
        queryNode(grandpa, "自己");
        System.out.println("查询节点=========");
        middleOrderSearch(grandpa);
        System.out.println("删除节点=========");
        deleteNode(grandpa, "哥哥");
        System.out.println(grandpa);

    }

    /**
     * 前序遍历
     * 先遍历当前节点，然后遍历左侧节点，最后遍历右侧节点
     *
     * @param node
     */
    public static void preOrderIter(Node node) {
        if (node == null) {
            return;
        }
        // 1.输出当前节点
        System.out.println(node.getName());
        // 2.遍历左侧节点
        preOrderIter(node.getLeft());
        // 3.遍历右侧节点
        preOrderIter(node.getRight());
    }

    /**
     * 中序遍历
     */
    public static void middleOrderIter(Node node) {
        if (Objects.isNull(node)) {
            return;
        }
        // 1.遍历左侧节点
        middleOrderIter(node.getLeft());
        // 2.输出当前节点
        System.out.println(node.getName());
        // 3.遍历右侧节点
        middleOrderIter(node.getRight());
    }

    /**
     * 后序遍历
     */
    public static void postOrderIter(Node node) {
        if (Objects.isNull(node)) {
            return;
        }
        // 1.遍历左侧节点
        postOrderIter(node.getLeft());
        // 2.遍历右侧节点
        postOrderIter(node.getRight());
        // 3.输出当前节点
        System.out.println(node.getName());
    }

    /**
     * 查询指定节点
     */
    public static boolean queryNode(Node node, String name) {
        // 1.判断当前节点是否为空，主要判断是不是叶子节点
        if (node == null) {
            return false;
        }
        if (Objects.equals(node.getName(), name)) {
            System.out.println(node);
            // 结束整个方法，不要后面的递归执行
            return true;
        } else {
            // 2.遍历左侧节点
            boolean foundLeft = queryNode(node.getLeft(), name);
            if (foundLeft) {
                return true;
            }
            // 3.遍历右侧节点
            return queryNode(node.getRight(), name);
        }
    }

    public static Node middleOrderSearch(Node node) {
        if (Objects.nonNull(node.getLeft())) {
            if (Objects.equals(node.getName(), "自己")) {
                System.out.println(node);
                return node;
            } else {
                return middleOrderSearch(node.getLeft());
            }
        }

        if (Objects.equals(node.getName(), "自己")) {
            System.out.println(node);
            return node;
        } else {
            return middleOrderSearch(node.getRight());
        }
    }

    /**
     * 删除指定的节点或子树
     *
     * @param node 二叉树根节点
     * @param name 节点的名称
     */
    public static void deleteNode(Node node, String name) {
        // 如果根节点为null，则结束
        if (Objects.isNull(node) || Objects.equals(node.getName(), name)) {
            node = null;
            return;
        }
        Node left = node.getLeft();
        Node right = node.getRight();

        if (Objects.nonNull(left) && Objects.equals(left.getName(), name)) {
            node.setLeft(null);
            return;
        }
        if (Objects.nonNull(right) && Objects.equals(right.getName(), name)) {
            node.setRight(null);
            return;
        }
        deleteNode(left, name);
        deleteNode(right, name);
    }
}

class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    String name;
    Node left;
    Node right;

    public Node() {
    }

    public Node(String name) {
        this.name = name;
    }

    public Node(String name, Node left, Node right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getLeft() {
        return left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}