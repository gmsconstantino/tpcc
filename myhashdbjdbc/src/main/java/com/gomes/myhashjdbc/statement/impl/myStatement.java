package com.gomes.myhashjdbc.statement.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.*;
import com.gomes.myhashjdbc.db.DBSingleton;
import com.gomes.myhashjdbc.resultset.impl.myResultSet;
import com.gomes.myhashjdbc.statement.StatementAdapter;
import com.gomes.myhashjdbc.visitor.CreateKeyVisitor;
import com.gomes.myhashjdbc.visitor.InsertVisitor;
import com.gomes.myhashjdbc.visitor.SelectWhereVisitor;
import database.Transaction;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class does the actual work of the Generic... classes. It tries to open a CSV file for the table name in the
 * query and parses the contained data.
 * 
 * @author Kai Winter
 */
public final class myStatement extends StatementAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(myStatement.class);

	/**
	 * Constructs a new {@link myStatement}.
	 *
	 */
	public myStatement() {

	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
        LOGGER.debug("ExecuteQuery : "+sql);
        SQLParser parser = new SQLParser();
        StatementNode stmt;

        try {
            stmt = parser.parseStatement(sql);

//            stmt.treePrint();
            SelectWhereVisitor swv = new SelectWhereVisitor();
            stmt.accept(swv);

            DBSingleton dbSingleton = DBSingleton.getInstance();
            String keyPattern = dbSingleton.getTable(swv.getTargetTable()).getKeyPattern();

            StrSubstitutor sub = new StrSubstitutor(swv.getMapValues());
            String key = sub.replace(keyPattern);

            Transaction<String,HashMap<String,String>> t = dbSingleton.newTransaction();
            HashMap<String,String> x = t.get(key);
//            t.put(key, iv.getMapValues());
            t.commit();

            ArrayList<HashMap<String,String>> arr = new ArrayList<>();
            arr.add(x);
            return new myResultSet(swv.getTargetTable(), arr);
        } catch (StandardException e) {
            LOGGER.error("Error execute parser",e);
            return null;
        }
	}

    // Esta a chamar isto para fazer os insert
    @Override
    public boolean execute(String sql) throws SQLException {
        LOGGER.debug("Execute : "+sql);
        SQLParser parser = new SQLParser();
        StatementNode stmt;

        try {
            stmt = parser.parseStatement(sql);

            if (stmt instanceof CreateTableNode) {
                CreateKeyVisitor mv = new CreateKeyVisitor();
                stmt.accept(mv);

                DBSingleton.getInstance().addTable(mv.getNameTable(), mv.getKeyPattern());
            } else if( stmt instanceof InsertNode){
                InsertVisitor iv = new InsertVisitor();
                stmt.accept(iv);

                DBSingleton dbSingleton = DBSingleton.getInstance();
                String keyPattern = dbSingleton.getTable(iv.getTargetTable()).getKeyPattern();

                StrSubstitutor sub = new StrSubstitutor(iv.getMapValues());
                String key = sub.replace(keyPattern);

                Transaction t = dbSingleton.newTransaction();
                t.put(key, iv.getMapValues());
                if(t.commit())
                    return true;
                else
                    return false;
            } else {
                stmt.treePrint();
            }
        } catch (StandardException e) {
            LOGGER.error("Error execute parser",e);
            return false;
        }

        return false;
    }



}
