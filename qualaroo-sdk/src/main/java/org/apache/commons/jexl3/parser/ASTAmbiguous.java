/* Generated By:JJTree: Do not edit this line. ASTAmbiguous.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.apache.commons.jexl3.parser;

public
class ASTAmbiguous extends JexlNode {
  public ASTAmbiguous(int id) {
    super(id);
  }

  public ASTAmbiguous(Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=22a497d3a414cd2f8f3ed8acc08fe5de (do not edit this line) */