//
// Generated by JTB 1.3.2
//

package kanga.syntaxtree;

/**
 * Grammar production:
 * f0 -> "PRINT"
 * f1 -> SimpleExp()
 */
public class PrintStmt implements Node {
   public NodeToken f0;
   public SimpleExp f1;

   public PrintStmt(NodeToken n0, SimpleExp n1) {
      f0 = n0;
      f1 = n1;
   }

   public PrintStmt(SimpleExp n0) {
      f0 = new NodeToken("PRINT");
      f1 = n0;
   }

   public void accept(kanga.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(kanga.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(kanga.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(kanga.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

