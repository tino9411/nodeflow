package com.nodeflow;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
			Node node = new Node(NodeType.DATA_FETCH);
			
		node.addBehaviour(new DataFetcher());

		node.run();

		System.out.println("Final output: " + node.getOutput());

    }
}
