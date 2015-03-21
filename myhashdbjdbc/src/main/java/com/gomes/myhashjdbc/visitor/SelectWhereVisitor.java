package com.gomes.myhashjdbc.visitor;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 *
 * Assumindo que a clausula where é a primary key
 *
 * Created by gomes on 21/03/15.
 */
public class SelectWhereVisitor implements Visitor{

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectWhereVisitor.class);

    String targetTable;
    HashMap<String,String> mapValues = new HashMap<>();

    @Override
    public Visitable visit(Visitable visitable) throws StandardException {
        LOGGER.debug(visitable.getClass().getSimpleName() + " " + visitable.toString().replace("\n"," "));

        if (visitable instanceof SelectNode){
            SelectNode sn = (SelectNode) visitable;

            FromList frl = sn.getFromList();
            assert(frl.size()==1);
            targetTable = frl.get(0).getTableName().getTableName();

        } else if(visitable instanceof BinaryRelationalOperatorNode){
            BinaryRelationalOperatorNode bron = (BinaryRelationalOperatorNode) visitable;
            if (bron.getMethodName().equals("equals")){

                String column = bron.getLeftOperand().getColumnName();

                if(bron.getRightOperand() instanceof NumericConstantNode){
                    Object x = ((NumericConstantNode) bron.getRightOperand()).getValue();
                    mapValues.put(column, x.toString());
                }
            }
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
