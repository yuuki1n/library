package library.dataStructure.rangeData.segmentTree;

//public abstract class PersistentSegmentTree<V extends BaseV, F> {
//  private TreeMap<Integer, Node> rootMap;
//
//  public PersistentSegmentTree(int n){ (rootMap = new TreeMap<>()).put(-Util.infI,build(0,n,this::init)); }
//
//  private Node build(int i,int n,IntFunction<V> init){
//    return n < 2 ? new Node(init.apply(i),null,null)
//        : new Node(e(),build(i,n /2,init),build(i +n /2,n -n /2,init)).merge();
//  }
//
//  public void upd(int i,F f,int t){ rootMap.put(t,upd(rootMap.floorEntry(t).getValue(),i,f)); }
//
//  private Node upd(Node nd,int i,F f){
//    if (nd.sz == 1) {
//      var v = e();
//      agg(v,nd.val,e());
//      map(v,f);
//      return new Node(v,null,null);
//    }
//    return new Node(e(),i < nd.lft.sz ? upd(nd.lft,i,f) : nd.lft,
//        nd.lft.sz <= i ? upd(nd.rht,i -nd.lft.sz,f) : nd.rht).merge();
//  }
//
//  public V get(int l,int r,int t){
//    var ret = e();
//    MyStack<Node> stk = new MyStack<>();
//    stk.add(rootMap.floorEntry(t).getValue());
//    for (Node nd;;) {
//      if ((nd = stk.pop()).sz <= l) {
//        l = max(0,l -nd.sz);
//        r = max(0,r -nd.sz);
//        continue;
//      }
//      if (l == 0 && nd.sz <= r) {
//        agg(ret,ret,nd.val);
//        ret.sz += nd.sz;
//        if ((r = max(0,r -nd.sz)) == 0)
//          return ret;
//        continue;
//      }
//      stk.add(nd.rht);
//      stk.add(nd.lft);
//    }
//  }
//
//  protected V init(int i){ return e(); }
//
//  protected abstract V e();
//  protected abstract void agg(V v,V a,V b);
//  protected abstract void map(V v,F f);
//
//  public class Node{
//    private int sz;
//    private V val;
//    private Node lft,rht;
//
//    public Node(V val,Node lft,Node rht){
//      this.val = val;
//      this.lft = lft;
//      this.rht = rht;
//      sz = val.sz = 1;
//    }
//
//    private Node merge(){
//      agg(val,lft.val,rht.val);
//      sz = val.sz = lft.val.sz +rht.val.sz;
//      return this;
//    }
//  }
//}