package com.concurrent.data.structure;

import com.concurrent.utils.PrintUtils;

public class MyQueueTest {

    static Node<String> head =new Node<>("");
    static Node tail;

    private static class Node<T>{
        public Node(T data) {
            this.data = data;
        }

        public T data;
        public Node prev;
        public Node next;

    }

    public static void main(String[] args) {
        Node<String> node_1=new Node<>("a");
        Node<String> node_2=new Node<>("b");
        Node<String> node_3=new Node<>("c");
        tail=head;
        head.next=node_1;
        head.next.prev=head;
        node_1.next=node_2;
        node_1.next.prev=node_1;
        node_2.next=node_3;
        node_2.next.prev=node_2;
        node_3.next=tail;
        node_3.next.prev=node_3;

        for (Node<String> q =head;q.next!=null&&q.next!=tail;q=q.next){
            PrintUtils.log("节点"+q.data);
        }

        for (Node<String> q =tail;q.prev!=head;q=q.prev){
            PrintUtils.log("节点"+q.data);
        }
    }
}
