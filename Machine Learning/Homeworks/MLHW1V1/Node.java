import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Node implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nodeName; //Node name
	private int nodeIndex; //Node Index
	private Node lChild; // 0 attribute value
	private Node rChild; // 1 attribute value
	private Node pNode;  // root node
	private int lClass;  // if left leaf is class, assign 0 
	private int rClass;  // if right leaf is class, assign 1
	private int level;
	private double pExZero;
	private double pExOne;
	
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public Node getlChild() {
		return lChild;
	}
	public void setlChild(Node lChild) {
		this.lChild = lChild;
	}
	public Node getrChild() {
		return rChild;
	}
	public void setrChild(Node rChild) {
		this.rChild = rChild;
	}
	public Node getpNode() {
		return pNode;
	}
	public void setpNode(Node pNode) {
		this.pNode = pNode;
	}
	public int getrClass() {
		return rClass;
	}
	public void setrClass(int rClass) {
		this.rClass = rClass;
	}
	public int getlClass() {
		return lClass;
	}
	public void setlClass(int lclass) {
		this.lClass = lclass;
	}
	public int getNodeIndex() {
		return nodeIndex;
	}
	public void setNodeIndex(int nodeIndex) {
		this.nodeIndex = nodeIndex;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public double getpExZero() {
		return pExZero;
	}
	public void setpExZero(double pExZero) {
		this.pExZero = pExZero;
	}
	public double getpExOne() {
		return pExOne;
	}
	public void setpExOne(double pExOne) {
		this.pExOne = pExOne;
	}
	
	public Node copy() { // deep copy
	        Node newNode = null;  //new node
	        try {
	        	Node oldNode = this;
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ObjectOutputStream obOutStream = new ObjectOutputStream(baos);
	            obOutStream.writeObject(oldNode);
	            obOutStream.flush();
	            obOutStream.close();

	            
	            ObjectInputStream obInputStream = new ObjectInputStream(
	                new ByteArrayInputStream(baos.toByteArray()));
	            newNode = (Node)obInputStream.readObject();
	        }
	        catch(IOException e) {
	            e.printStackTrace();
	        }
	        catch(ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	        return newNode;
	 }
	
	
}
