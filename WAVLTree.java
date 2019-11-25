/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree.
 * (Haupler, Sen & Tarajan ‘15)
 *
 */


//user name 1:ofekdavid id:205517865 name:Ofek David
//user name2:drori1 id:312535917 name:Tal Drori



public class WAVLTree {

	public WAVLNode root;
	public WAVLNode EXT_NODE;
	public WAVLTree() {// constructor. WAVLTree with the WAVLNode EXT_NODE in root
		EXT_NODE = new WAVLNode();
		root=EXT_NODE;
	}
	
	
    public boolean empty() {  //checks if the tree is empty, if the root is EXT_NODE
    if(root==EXT_NODE) {
    	return true;
    }
    return false;
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k) // search for node with key k in the tree
  {
	  	WAVLNode x = root;	
        while (x != EXT_NODE) {  // continue looking until arriving to EXT_NODE
        	if (k==x.key) {
        		return x.info;
        	}
        	else if(k<x.key) {
        		x=x.left;
        	}
        	else {
        		x=x.right;
        	}
        }
        return x.info;
  }
  private void reFix(WAVLNode x) {  // in a case of insertion of a node that exist in the tree, the method will fix the size all the way to the root
	  x.sizen--;
	  while(x.parent!=null) {
		  x=x.parent;
		  x.sizen--;
	  }
  }
  private WAVLNode treePos (WAVLTree tree,int key) { // the method finds the correct position to insert the node in insertion and correct the sizes
	  WAVLNode x=tree.root;
	  WAVLNode y=EXT_NODE;
	  while (x!=EXT_NODE) { 
		  y=x;
		  y.sizen++;
		  if (key==x.key){
			  reFix(x);
			  return x;
		  }
		  else if (key<x.key) {
			  x=x.left;
		  }
		  else {
			  x=x.right;
		  }
	  }
	  return y;
  }
  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) { // insert a node with a key of k and info of i, the method returns the number of rebalance operation
          WAVLNode x = new WAVLNode(k,i);// create the new node to insert
          x.left=EXT_NODE;
          x.right=EXT_NODE;
          if(root==EXT_NODE) { // checking if the tree is empty, in that case put x as a root
        	  root=x;
        	  return 0;
          }
          WAVLNode y = treePos(this, k); // find the correct position to insert the node
          int [] f = {x.key,y.key};
          if (f[0]==f[1]) { // in case the key is already exists in the tree, return -1 and dosent insert anything
        	  
        	  return -1;
          }
          x.parent=y; 
          if(y.rank!=0) {// if y is onary do:
        	  if(x.key<y.key) {// check if x should be left son of y
        		  y.left=x;
        	  }
        	  else { // x should be right son of y
        		  y.right=x;
        	  }
        	  return 0; // no rebalance operation was needed, return 0
          }
          if(x.key<y.key) { // if y is a leaf
        	  y.left=x;
          }
          else {
        	  y.right=x;
          }
          int cnt=0;//rebalance operation to return
          while(x.parent!=null&&x.parent.rank-x.rank==0) { // while the tree is not balanced continue to balance the tree
        	  if(parentside(x.parent, x).equals("left")) { // check if x is a left son of x's parent
        		  if (x.parent.rank-x.parent.right.rank==1) {//check the conditions for promotion
        			  promote(x.parent);
        			  x=x.parent;
        			  cnt++;
        		  }
        		  else if(x.rank-x.left.rank==1) { // check the conditions for rotate
        			  rightRotate(x);
        			  cnt+=2;
        		  }
        		  else {
        			  doubleRotateleft(x); // check the conditions for double rotate
        			  cnt+=5;
        		  }
        	  }
        	  else { // x is a right son of x's parent, all conditions and actions are symmetric to the left side
        		  if (x.parent.rank-x.parent.left.rank==1) {
        			  promote(x.parent);
        			  x=x.parent;
        			  cnt++;
        		  }
        		  else if(x.rank-x.right.rank==1) {
        			  leftRotate(x);
        			  cnt+=2;
        		  }
        		  else {
        			  
        			  doubleRotateright(x);
        			  cnt+=5;
        		  }
        	  }
          }
          return cnt;
          
   }
   
   
   private void doubleRotateleft(WAVLNode x) {//double rotation for insert when the problem comes from the left side
	   WAVLNode z=x.right;
	   leftRotate(z);
	   z.rank+=1;
	   rightRotate(z);
   }
   private void doubleRotateright(WAVLNode x) { // double rotation for insert when the problem comes from the right side
	   WAVLNode z=x.left;
	   rightRotate(z);
	   z.rank+=1;
	   leftRotate(z);
   }
   private void promote(WAVLNode x) { // promoting x and enrolling the problem
	   x.rank++;
   }
   private void rightRotate(WAVLNode x) { // rotation for insert when the problem comes from the right side
	   // pointers and changing pointers and ranks like in slide 26 (symmetric case) in the presentation
	  WAVLNode a=x.left;
	  WAVLNode b=x.right;
	  WAVLNode y=x.parent;
	  WAVLNode c=y.right;
	  x.right=y;
	  y.left=b;
	  if (y.parent!=null) {
		  WAVLNode d=y.parent;
		  String side=parentside(d,y);

		  if (side.equals("right")) {
			  d.right=x;
			  x.parent=d;
		  }
		  else {
			  d.left=x;
			  x.parent=d;
		  }
	  }
	  else {
		  x.parent=null;
		  this.root=x;
	  }
	  y.parent=x;
	  b.parent=y;
	  y.rank=y.rank-1;
	  
	  y.sizen=b.sizen+c.sizen+1;
	  x.sizen=a.sizen+y.sizen+1;
	  
   }
   
   private String parentside(WAVLNode d,WAVLNode y) { // check if y is a left son of d or right son
	   if(d.right==y) {
		   return "right";
	   }
	   else {
		   return "left";
	   }
   }
   
   private void leftRotate(WAVLNode y) {  // rotation for insert when the problem comes from the left side, symmetric to right rotate
	// pointers and changing pointers and ranks like in slide 26 in the presentation
	   WAVLNode x=y.parent;
	   WAVLNode a=x.left;
	   WAVLNode b=y.left;
	   WAVLNode c = y.right;
	   y.left=x;
	   x.right=b;
	   
	  if (x.parent!=null) {
		  WAVLNode d=x.parent;
		  String side=parentside(d,x);

		  if (side.equals("right")) {
			  d.right=y;
			  y.parent=d;
		  }
		  else {
			  d.left=y;
			  y.parent=d;
		  }
	  }
	  else {
		  y.parent=null;
		  this.root=y;
	  }
	  x.parent=y;
	  b.parent=x;
	  x.rank=x.rank-1;
	  x.sizen=a.sizen+b.sizen+1;
	  y.sizen=x.sizen+c.sizen+1;
	  
   }
   
   
   
   
   
   /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   private void reFixForDel(WAVLNode y) { // in a case of deletion of a node that doesnt exist in the tree, the method will fix the size all the way to the root
	   y.sizen++;
	   while(y.parent!=null) {
		   y=y.parent;
		   y.sizen++;
	   }
   }
   
   private WAVLNode treePosForDel (WAVLTree tree,int key) { //the method finds the node with the right key to delete and correct the sizes
		  WAVLNode x=tree.root;
		  WAVLNode y=EXT_NODE;
		  if(x==EXT_NODE) { // if the tree happens to be empty return a node with a different key then expected
			  return new WAVLNode(key-1,"i");
		  }
		  while (x!=EXT_NODE) {
			  y=x;
			  y.sizen--;
			  if (key==x.key){
				  return x;
			  }
			  else if (key<x.key) {
				  x=x.left;
			  }
			  else {
				  x=x.right;
			  }
		  }
		  if(y.key!=key) { // if the node is not found, the method call refixfordel to fix the sizes
			  reFixForDel(y);
		  }
		  return y;
	  }
   
   
   public int delete(int k){//delete a node with a key of k, during the all algorithm x is the child and z is x's parent
	   WAVLNode x = treePosForDel(this, k); // find the node to delete
	   int cnt = 0; // rebalance operations counter
	   if(x.key!=k) { // if a node with a key of k doesnt exist in the tree there is nothing to delete, return -1
		   return -1;
	   }
	   if(this.root.key==x.key&&this.root.rank==0) {//root is a leaf
		   this.root=EXT_NODE;// change the root pointer to EXT_NODE
		   return 0;
	   }
	   else if(this.root.key==x.key&&(this.root.right==EXT_NODE||this.root.left==EXT_NODE)) {//root is onary
		   if(x.left!=EXT_NODE) { // x is a root with a left child
			   x.left.parent = null;
			   this.root = x.left; // change the root pointer to the root's left child
			   x.left.sizen=1;
			   return 0;
		   }
		   x.right.parent = null; // x is a root with a right child 
		   x.right.sizen=1;
		   this.root = x.right; // change the root pointer to the root's right child
		   return 0;
	   }
	   WAVLNode z;
	   if(isInnerNode(x)) {// x is an inner node, requires a call for successor and swap
		   z = successorForDel(x); // find the successor of x
		   x = swap(x,z); // put x's successor instead of x and delete successor 
	   }
		  if(x.rank == 0) {//x is a leaf
			  if(parentside(x.parent, x).equals("left")) { // x is a left child of it's parent, requires change in pointers 
				  x.parent.left = EXT_NODE;
				  z = x.parent;
				  x = x.parent.left;
			  }
			else { // x is a right child of it's parent, requires change in pointers 
				  x.parent.right = EXT_NODE;
				  z = x.parent;
				  x = x.parent.right; 
			  }
		  }
		  else {//x is onary
			  boolean onaryside = onaryLeft(x); // check if x is onary with a left child
			  WAVLNode x_child = EXT_NODE;
			   if (onaryside) {
				   x_child=x.left; // get a pointer for x child
			   }
			   else {
				   x_child=x.right;
			   }
			  if(parentside(x.parent, x).equals("left")) { // x is a left child of its parent, change pointers for delete and rebalance loop
				  x.parent.left=x_child;
				  x_child.parent=x.parent;
				  z=x.parent;
				  x=x_child;
				  
			  }
			  else {// x is a left child of its parent, change pointers for delete and rebalance loop
				  x.parent.right=x_child;
				  x_child.parent=x.parent;
				  z=x.parent;
				  x=x_child;
			  }
		  }
	   if (z.left.rank==-1&&z.right.rank==-1) {//special case, z becomes a leaf, change pointers and demote
		   demote(z);
		   x=z;
		   z=z.parent;
		   cnt++;
	   }
	   
	   while(z!=null&&z.rank-x.rank==3) { //  while the tree is not balanced continue to balance the tree
		   if(parentside(z, x).equals("left")) { // x is z's left son
			   if(z.rank-z.right.rank==2) {//condition for demote

				   demote(z);

				   x=z;
				   z=z.parent;
				   cnt++;
			   }
			   else {
				   if(z.right.rank-z.right.left.rank==2&&z.right.rank-z.right.right.rank==2) {//condition for doubledemote left
					   doubleDemoteLeft(z);
					   x=z;
					   z=z.parent;
					   cnt+=2;
					   
				   }
				   else if((z.right.rank-z.right.left.rank==1||z.right.rank-z.right.left.rank==2)&&z.right.rank-z.right.right.rank==1) {// condition for rotate left for del
					   rotateLeftDel(z);
					   cnt+=3;
					   break; // tree is balanced
				   }
				   else {//condition for doublerotate left for del
					   doubleRotateLeftDel(z);
					   
					   cnt=cnt+5;
					   break;  // tree is balanced
				   }
			   }
		   }
		   else { // x is z's right son, conditions are symmetric to left side
			   if(z.rank-z.left.rank==2) {
				   demote(z);
				   x=z;
				   z=z.parent;
				   cnt++;
				   
			   }
			   else {
				   if(z.left.rank-z.left.right.rank==2&&z.left.rank-z.left.left.rank==2) {
					   doubleDemoteright(z);
					   x=z;
					   z=z.parent;
					   cnt+=2;
				   }
				   else if((z.left.rank-z.left.right.rank==1||z.left.rank-z.left.right.rank==2)&&z.left.rank-z.left.left.rank==1) {
					   rotateRightDel(z);
					   cnt+=3;
					   break;
				   }
				   else {

					   doubleRotateRightDel(z);
					   cnt+=5;
					   break;
				   }
				   
			   }
		   }
	   }
	   return cnt;
   }
   private void doubleRotateRightDel (WAVLNode z) {//double rotation for rebalance after deletion when problem comes from right side
	// pointers and changing pointers and ranks like in slide 58(symmetric case) in the presentation
	   WAVLNode a = z.left.right;
	   WAVLNode y = z.left;
	   WAVLNode c = z.left.right.right;
	   WAVLNode d = z.left.right.left;
	   if(z.parent!=null) {
		   if(parentside(z.parent, z).equals("left")) {
			   z.parent.left=a;

	   }
	   else {
			   z.parent.right=a;

	   }
		   
	   }
	   else {
		   this.root=a;
	   }
	   a.right=z;
	   a.left=y;
	   a.rank=a.rank+2;
	   a.parent=z.parent;
	   y.parent=a;
	   y.right=d;
	   y.rank--;
	   z.parent=a;
	   z.left=c;
	   z.rank=z.rank-2;

	   c.parent=z;
	   d.parent=y;
	   z.sizen=z.right.sizen+z.left.sizen+1;
	   y.sizen=y.right.sizen+y.left.sizen+1;
	   a.sizen=a.right.sizen+a.left.sizen+1;
   }
   private void doubleRotateLeftDel (WAVLNode z) {//double rotation for rebalance after deletion when problem comes from left side
	// pointers and changing pointers and ranks like in slide 58 in the presentation
	   WAVLNode a = z.right.left;
	   WAVLNode y = z.right;
	   WAVLNode c = z.right.left.left;
	   WAVLNode d = z.right.left.right;
	   if(z.parent!=null) {
		   if(parentside(z.parent, z).equals("left")) {
			   z.parent.left=a;

	   }
	   else {
			   z.parent.right=a;

	   }
		   
	   }
	   else {
		   this.root=a;
	   }
	   a.left=z;
	   a.right=y;
	   a.rank=a.rank+2;
	   a.parent=z.parent;
	   y.parent=a;
	   y.left=d;
	   y.rank--;
	   z.parent=a;
	   z.right=c;
	   z.rank=z.rank-2;
	   c.parent=z;
	   d.parent=y;
	   z.sizen=z.right.sizen+z.left.sizen+1;
	   y.sizen=y.right.sizen+y.left.sizen+1;
	   a.sizen=a.right.sizen+a.left.sizen+1;
   }

   private void rotateLeftDel (WAVLNode z) {//rotation for rebalance after deletion when problem comes from left side
	// pointers and changing pointers and ranks like in slide 57 in the presentation
	   WAVLNode y = z.right;
	   WAVLNode a = y.left;
	   y.parent=z.parent;
	   if(z.parent!=null) {
		   if(parentside(z.parent, z).equals("left")) {
			   z.parent.left=y;

	   }
	   else {
			   z.parent.right=y;

	   }
	   }

	   else {
		   this.root=y;
	   }
	   
	   y.left = z;
	   y.rank++;
	   z.parent=y;
	   z.right = a;
	   z.rank--;
	   if(a.rank>=0) {
		   a.parent=z;  
	   }
	   if(z.left==EXT_NODE&&z.right==EXT_NODE&&z.rank!=0) {
		   z.rank--;
	   }
	   z.sizen = z.left.sizen+a.sizen+1;
	   y.sizen = z.sizen+y.right.sizen+1;
   }
   
   private void rotateRightDel (WAVLNode z) {//rotation for rebalance after deletion when problem comes from right side
		// pointers and changing pointers and ranks like in slide 57(symmetric case) in the presentation
	   WAVLNode x = z.left;
	   WAVLNode d = x.right;
	   x.parent=z.parent;
	   if(z.parent!=null) {
		   if(parentside(z.parent, z).equals("left")) {
			   z.parent.left=x;

	   }
	   else {
			   z.parent.right=x;

	   }
	   }

	   else {
		   this.root=x;
	   }
	   x.right = z;
	   x.rank++;
	   z.parent=x;
	   z.left = d;
	   z.rank--;
	   if(d.rank>=0) {
		   d.parent=z;  
	   }
	   if(z.left==EXT_NODE&&z.right==EXT_NODE&&z.rank!=0) {
		   z.rank--;
	   }
	   z.sizen = z.right.sizen+d.sizen+1;
	   x.sizen = z.sizen+x.left.sizen+1;
   }
   
   private void doubleDemoteLeft (WAVLNode z) { // double demote for rebalance after deletion when problem comes from left side
	   z.rank--;
	   z.right.rank--;
   }
   private void doubleDemoteright (WAVLNode z) {// double demote for rebalance after deletion when problem comes from right side
	   z.rank--;
	   z.left.rank--;
   }
   
   private void demote(WAVLNode z) {// demote for rebalance after deletion
	   z.rank--;
   }
   
   private boolean isInnerNode(WAVLNode x) { // check if x is an inner node
   		if(x.left!=EXT_NODE&&x.right!=EXT_NODE) {
   			return true;
   		}
   		return false;
   	}
   	
   	
   private WAVLNode successorForDel(WAVLNode x) { // finds the successor of x and fix sizes on the way there for post deletion status of the tree
	   x.right.sizen--;
	   x=x.right;
	   while(x.left!=EXT_NODE) {
		   x=x.left;
		   x.sizen--;
	   }
	   return x;
   }
   
   private WAVLNode swap (WAVLNode x, WAVLNode z) {// copy z (m), the successor of x, and put it instead of z, put z instead of x, delete m
	   WAVLNode ret = EXT_NODE;
	   WAVLNode m = new WAVLNode(); // create a copy of z
	   m.right=z.right;
	   m.left=z.left;
	   m.rank=z.rank;
	   m.parent=z.parent;
	   m.sizen=z.sizen;
	   m.info=z.info;
	   m.key=z.key;
	   WAVLNode aba = x.parent;
	   String side=null;
	   if (x.parent!=null) { //if x is not the root
		   side = parentside(x.parent, x); // keep the side of x related to x's parent
	   }
	   if(parentside(z.parent, z).equals("left")) { // put the copy of z in the correct side of z's parent
		   z.parent.left=m;
	   }
	   else {
		   z.parent.right=m;
	   }
	   // replace x with z
	   x.left.parent=z; 
	   x.right.parent=z;
	   z.right=x.right;
	   z.left=x.left;
	   z.sizen=x.sizen;
	   z.rank=x.rank;
	   z.parent=x.parent;
	   if(aba!=null) { // if x is not the root
		   if(side.equals("left")) {// put z in the correct size of x's parent
			   aba.left=z;
		   }
		   else {
			   aba.right=z;
		   }
	   }
	   else { // x is the root, change root pointer to z
		   this.root=z;
	   }
	   return m;
   }
   private boolean onaryLeft(WAVLNode z) { // check if z is onary with a left child
	   if(z.left.rank!=-1) {
		   return true;
	   }
	   return false;
   }

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min() // return the value of the minimum key in the tree
   {
	   	if(empty()) {
	   		return null;
	   	}
        return min(root).info;
   }
   private WAVLNode min(WAVLNode x) { // return a node with the minimum key in a subtree when x is the root of the sub tree
	   while(x.left!=EXT_NODE) {
       	x=x.left;
       }
       return x;
   }
   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()// return the value of the maximum key in the tree
   {
	   if(empty()) {
	    return null;
	   }
	   WAVLNode x = root;
       while(x.right!=EXT_NODE) {
       	x=x.right;
       }
       return x.info;
  }


   /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   public int[] keysToArray() // return a sorted array of the tree's keys, using the recursion method keysToArray2
   {
        int[] arr = new int[root.sizen]; 
        if (empty()) {
            return arr;
        }
        int index = 0;
        keysToArray2(this.root, arr, index);
        return arr;
   }
   private int keysToArray2(WAVLNode x,int []arr ,int index) {// a recursion for a in order getting of keys, the method get a WAVLNode, a current index and a result array
 	   if (x==EXT_NODE) {
 		   return index;
 	   }
 	   index=keysToArray2(x.left, arr, index); // left son recursion
 	   arr[index] = x.key; // insert the current key
 	   index++;
 	   index = keysToArray2(x.right, arr, index);// right son recursion
 	   return index;
    }

   /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
   public String[] infoToArray()// return an array of the tree's value, using the recursion method infoToArray2
   {
	   String [] arr = new String[root.sizen]; 
       if (empty()) {
           return arr;
       }
       int index = 0;
       infoToArray2(this.root, arr, index);
       return arr;
   }
   private int infoToArray2(WAVLNode x,String []arr ,int index) {// a recursion for a in order getting of values, the method get a WAVLNode, a current index and a result array
 	   if (x==EXT_NODE) {
 		   return index;
 	   }
 	   index=infoToArray2(x.left, arr, index);// left son recursion
 	   arr[index] = x.info;// the current value
 	   index++;
 	   index = infoToArray2(x.right, arr, index); // right son recursion
 	   return index;
    }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    */
   public int size() // return the number of nodes in the tree
   {
           return root.sizen;
   }
   
     /**
    * public WAVLNode getRoot()
    *
    * Returns the root WAVL node, or null if the tree is empty
    *
    */
   public WAVLNode getRoot() // return the root of the tree
   {
           return root;
   }
     /**
    * public int select(int i)
    *
    * Returns the value of the i'th smallest key (return -1 if tree is empty)
    * Example 1: select(1) returns the value of the node with minimal key 
        * Example 2: select(size()) returns the value of the node with maximal key 
        * Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor  
    *
    */   
   public String select(int i) // find i's smallest key in the tree, using the recursion method select_rec
   {
	   	if (this.root==EXT_NODE||i>root.sizen||i<1) { // check if i is a valid number to select
	   		return null;
	   	}
	   	return select_rec(this.root, i-1);
           
   }
   public String select_rec (WAVLNode root,int i) {// find i's smallest key in the subtree with the WAVLNode root
	   int size = root.left.sizen;
  		if(i==size) { // the root is the i's smallest key in the sub tree
  			return root.getValue();
  		}
  		else if(i<size) {// check at the left side of the tree for the i's smallest key
  			return select_rec(root.left,i);
  		}
  		else{
  			return select_rec(root.right,i-size-1);// check at the right side of the tree for the (i-size-1)'s smallest key
  		}
   }

   /**
   * public class WAVLNode
   */
  public class WAVLNode{
	  public WAVLNode left;
	  public WAVLNode right;
	  public WAVLNode parent;
	  public int rank;
	  public Integer key;
	  public int sizen;
	  public String info;
	  public WAVLNode(int key1, String info1) { // constructor. WAVLNode with the key1 as a key and info1 as info
		  rank=0;
		  right=null;
		  left=null;
		  parent=null;
		  key=key1;
		  info=info1;
		  sizen=1;
		  
	  }
	  public WAVLNode() {// constructor. vacuity WAVLNode
		  left=null;
		  right=null;
		  parent=null;
		  rank = -1;
		  key = null;
		  sizen=0;
		  info=null;
	  }

	  			
                public int getKey() // return the key of the WAVLNode
                {
                        return key;
                }
                public String getValue()// return the value of the WAVLNode
                {
                        return info;
                }
                public WAVLNode getLeft()// return the left child of the WAVLNode
                {
                        return left;
                }
                public WAVLNode getRight()// return the right child of the WAVLNode
                {
                        return right;
                }
                public boolean isInnerNode()// check if the WAVLNode is an inner node
                {
                	if (key==-1) {
                		return false;
                	}
                    return true;
                }

                public int getSubtreeSize() // return the size of the subtree with the WAVLNode as a root
                {
              	  return sizen;
                }


               
  }

}