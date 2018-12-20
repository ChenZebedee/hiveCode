package com.zebedee.hive.permission;

import org.apache.hadoop.hive.ql.parse.*;
import org.apache.hadoop.hive.ql.session.SessionState;

/**
 * Created by shaodi.chen on 2018/12/20.
 */
public class MyAuthHook extends AbstractSemanticAnalyzerHook {
    /**
     * Hive 管理员配置
     */
    private static String[] admin = {"root", "hadoop", "hive"};

    @Override
    public ASTNode preAnalyze(HiveSemanticAnalyzerHookContext context,
                              ASTNode ast) throws SemanticException {
        switch (ast.getToken().getType()) {
            case HiveParser.TOK_CREATEDATABASE:
            case HiveParser.TOK_DROPDATABASE:
            case HiveParser.TOK_CREATEROLE:
            case HiveParser.TOK_DROPROLE:
            case HiveParser.TOK_GRANT:
            case HiveParser.TOK_REVOKE:
            case HiveParser.TOK_GRANT_ROLE:
            case HiveParser.TOK_REVOKE_ROLE:
                String userName = null;
                if (SessionState.get() != null
                        && SessionState.get().getAuthenticator() != null) {
                    userName = SessionState.get().getAuthenticator().getUserName();
                }
                if (!admin[0].equalsIgnoreCase(userName)
                        && !admin[1].equalsIgnoreCase(userName) && !admin[2].equalsIgnoreCase(userName)) {
                    throw new SemanticException(userName
                            + " can't use ADMIN options, except " + admin[0] + "," + admin[1] + ","
                            + admin[2] + ".");
                }
                break;
            default:
                break;
        }
        return ast;
    }

   /* public static void main(String[] args) throws SemanticException {
        String[] admin = {"admin", "root"};
        String userName = "admin";
        for (String tmp : admin) {
            System.out.println(tmp);
            if (!admin[0].equalsIgnoreCase(userName) && !admin[1].equalsIgnoreCase(userName)) {
                throw new SemanticException(userName
                        + " can't use ADMIN options, except " + admin[0] + ","
                        + admin[1] + ".");
            }
        }
    }*/
}
