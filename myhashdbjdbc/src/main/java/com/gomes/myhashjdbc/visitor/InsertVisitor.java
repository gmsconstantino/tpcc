package com.gomes.myhashjdbc.visitor;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.*;
import com.yahoo.ycsb.ByteArrayByteIterator;
import com.yahoo.ycsb.ByteIterator;
import com.yahoo.ycsb.StringByteIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by gomes on 21/03/15.
 */
public class InsertVisitor implements Visitor{

    String targetTable;
    String[] listColumns;
    HashMap<String,String> mapValues = new HashMap<>();
    int i = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertVisitor.class);

    @Override
    public Visitable visit(Visitable visitable) throws StandardException {
        LOGGER.debug(visitable.getClass().getSimpleName() + " " + visitable.toString().replace("\n"," "));

        if (visitable instanceof InsertNode){
            InsertNode in = (InsertNode) visitable;
            targetTable = in.getTargetTableName().getTableName();
            listColumns = in.getTargetColumnList().getColumnNames();
        } else if(visitable instanceof NumericConstantNode){
            NumericConstantNode cn = (NumericConstantNode) visitable;
//            System.out.println(listColumns[i] +" "+ cn.getValue());

//            switch (cn.getNodeType()){
//                case NodeTypes.INT_CONSTANT_NODE:
//                    ByteBuffer bb = ByteBuffer.allocate(4);
//                    bb.putInt((Integer) cn.getValue());
//                    mapValues.put(listColumns[i], new ByteArrayByteIterator(bb.array()));
//                    break;
//                case NodeTypes.DECIMAL_CONSTANT_NODE:
//                    BigDecimal bd = (BigDecimal) cn.getValue();
//                    mapValues.put(listColumns[i], new StringByteIterator(bd.toString()));
//                    break;
//                default:
//                    LOGGER.error("Can't do cast conversion value.");
//            }
            mapValues.put(listColumns[i],cn.getValue().toString());
            i++;
        } else if(visitable instanceof CharConstantNode){
            CharConstantNode cn = (CharConstantNode) visitable;
            mapValues.put(listColumns[i], cn.getString());
            i++;
        }

        return visitable;
    }

    @Override
    public boolean visitChildrenFirst(Visitable visitable) {
        return false;
    }

    @Override
    public boolean stopTraversal() {
        return false;
    }

    @Override
    public boolean skipChildren(Visitable visitable) throws StandardException {
        return false;
    }

    public HashMap<String, String> getMapValues() {
        return mapValues;
    }

    public String getTargetTable() {
        return targetTable;
    }
}
