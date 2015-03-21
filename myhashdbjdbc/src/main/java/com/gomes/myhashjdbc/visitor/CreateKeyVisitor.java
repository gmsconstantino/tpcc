package com.gomes.myhashjdbc.visitor;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gomes on 21/03/15.
 */
public class CreateKeyVisitor implements Visitor{

    String nameTable;
    String key = "";
    String firstcolumn;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateKeyVisitor.class);

    @Override
    public Visitable visit(Visitable visitable) throws StandardException {
        LOGGER.debug(visitable.getClass().getSimpleName() + " " + visitable.toString().replace("\n"," "));

        if (visitable instanceof CreateTableNode){
            nameTable =  ((CreateTableNode) visitable).getFullName();
            key = nameTable;
        } else if (visitable instanceof ColumnDefinitionNode){
            ColumnDefinitionNode cdn = (ColumnDefinitionNode) visitable;

            if (firstcolumn == null){
                firstcolumn = cdn.getColumnName();
            }
            return null;
        } else if (visitable instanceof ConstraintDefinitionNode){
            ConstraintDefinitionNode cdn = (ConstraintDefinitionNode) visitable;
            if (cdn.getConstraintType() == ConstraintDefinitionNode.ConstraintType.PRIMARY_KEY) {
                int l = cdn.getColumnList().getColumnNames().length;
                for(int i = 0; i < l; i++) {
                    key += String.format(".${%s}", cdn.getColumnList().get(i).getName());
                }
            }
            else if (cdn.getConstraintType() == ConstraintDefinitionNode.ConstraintType.UNIQUE) {

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

    public String getKeyPattern() {
        if(key.equals(nameTable)){
            return nameTable+".${"+firstcolumn+"}";
        }

        return key;
    }

    public String getNameTable(){
        return nameTable;
    }

}
