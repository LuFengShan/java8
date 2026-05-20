package com.java8.sf;

import lombok.Data;

import java.util.*;

public class ErChaShu {
    public static void main(String[] args) {
        ErTree root = generateTree();
        // System.out.println("--------levelOrder------");
        // levelOrder(root);
        // System.out.println("--------preOrder------");
        // preOrder(root);
        // System.out.println("--------inOrder------");
        // inOrder(root);
        // System.out.println("--------postOrder------");
        // postOrder(root);

        // selectAllLeafNode(root);
        int h = getHeightRecursiveByLevel(root);
        System.out.println(h);

    }

    /**
     * 输出二叉树的高度，本示例是按照节点来算，不是边数来算：二叉树的高度是从要节点到最远叶子节点所经过的边数，基于层充遍历实现，高度=(层数-1)
     *
     * @param root
     */
    private static int getHeightRecursive(ErTree root) {
        if (root == null) {
            return 0;
        }
        System.out.println("当前节点：" + root.getVal());
        int leftHeight = getHeightRecursive(root.left);
        int rightHeight = getHeightRecursive(root.right);
        int currentRecursive = Math.max(leftHeight, rightHeight) + 1;
        System.out.println("当前节点：" + root.getVal() + "的高度是：" + currentRecursive);
        return currentRecursive;
    }

    /**
     * 输出二叉树的高度，基于层序遍历，本示例是按照节点来算，不是边数来算：二叉树的高度是从要节点到最远叶子节点所经过的边数，基于层充遍历实现，高度=(层数-1)
     *
     * @param root
     */
    private static int getHeightRecursiveByLevel(ErTree root) {
        Queue<ErTree> queue = new LinkedList<>();
        queue.offer(root);
        int height = 0;
        while (!queue.isEmpty()) {
            height ++;
            ErTree node = queue.poll();
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }
        return height;
    }

    /**
     * 筛选出所有的叶子节点
     *
     * @param root
     * @return
     */
    private static void selectAllLeafNode(ErTree root) {
        Queue<ErTree> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            // 安全的取出第一个元素并删除队列中的值
            ErTree node = queue.poll();
            if (Objects.isNull(node.getLeft()) && Objects.isNull(node.getRight())) {
                System.out.println("ErChaShu.selectAllLeafNode(),leaf node:" + node.getVal());
            }
            if (Objects.nonNull(node.getLeft())) {
                queue.offer(node.getLeft());
            }
            if (Objects.nonNull(node.getRight())) {
                queue.offer(node.getRight());
            }
        }

    }

    /**
     * 层遍历
     *
     * @param root
     * @return
     */
    private static List<Integer> levelOrder(ErTree root) {
        Queue<ErTree> queue = new LinkedList<>();
        queue.offer(root);

        List<Integer> list = new ArrayList<>();
        while (!queue.isEmpty()) {
            ErTree node = queue.poll(); // 取队首，并删除
            list.add(node.val);

            if (Objects.nonNull(node.left)) {
                queue.offer(node.left); // 入队尾，FIFO
            }
            if (Objects.nonNull(node.right)) {
                queue.offer(node.right);
            }
        }
        System.out.println(list);
        return list;
    }

    /**
     * 前序便利
     *
     * @param root
     */
    private static void preOrder(ErTree root) {
        if (Objects.isNull(root)) {
            return;
        }
        Queue<Integer> queue = new LinkedList<>();
        preOrderHandle(root, queue);
        System.out.println(queue);
    }

    private static Queue<Integer> preOrderHandle(ErTree root, Queue<Integer> queue) {
        if (Objects.isNull(root)) {
            return queue;
        }
        queue.offer(root.val);
        preOrderHandle(root.left, queue);
        preOrderHandle(root.right, queue);
        return queue;
    }

    private static void inOrder(ErTree root) {
        if (Objects.isNull(root)) {
            return;
        }
        Queue<Integer> queue = new LinkedList<>();
        inOrderHandle(root, queue);
        System.out.println(queue);
    }

    private static Queue<Integer> inOrderHandle(ErTree root, Queue<Integer> queue) {
        if (Objects.isNull(root)) {
            return queue;
        }
        inOrderHandle(root.left, queue);
        queue.offer(root.val);
        inOrderHandle(root.right, queue);
        return queue;
    }

    private static void postOrder(ErTree root) {
        if (Objects.isNull(root)) {
            return;
        }
        Queue<Integer> queue = new LinkedList<>();
        postOrderHandle(root, queue);
        System.out.println(queue);
    }

    private static Queue<Integer> postOrderHandle(ErTree root, Queue<Integer> queue) {
        if (Objects.isNull(root)) {
            return queue;
        }
        postOrderHandle(root.left, queue);
        postOrderHandle(root.right, queue);
        queue.offer(root.val);
        return queue;
    }

    private static ErTree generateTree() {
        ErTree e1 = new ErTree(1);
        ErTree e2 = new ErTree(2);
        ErTree e3 = new ErTree(3);
        ErTree e4 = new ErTree(4);
        ErTree e5 = new ErTree(5);
        ErTree e6 = new ErTree(6);
        ErTree e7 = new ErTree(7);
        ErTree e8 = new ErTree(8);
        ErTree e9 = new ErTree(9);
        ErTree e10 = new ErTree(10);
        ErTree e11 = new ErTree(11);
        ErTree e12 = new ErTree(12);

        e1.left = e2;
        e1.right = e3;

        e2.left = e4;
        //e2.right = e5;
        //
        //e3.left = e6;
        //e3.right = e7;
        //
        //e4.left = e8;
        //e4.right = e9;
        //
        //e5.left = e10;
        //e5.right = e11;
        //
        //e6.left = e12;

        return e1;
    }

}

@Data
class ErTree {
    int val;// 节点值
    ErTree left; // 左子节点
    ErTree right; // 右子节点

    public ErTree(int val) {
        this.val = val;
    }
}