package yuquiz.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.BasicType;
import org.hibernate.type.StandardBasicTypes;

public class FunctionContributorConfig implements FunctionContributor {
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        BasicType<Boolean> type = functionContributions
                .getTypeConfiguration()
                .getBasicTypeRegistry()
                .resolve(StandardBasicTypes.BOOLEAN);

        functionContributions.getFunctionRegistry()
                .registerPattern("match","match(?1,?2) against (?3 in natural language mode)", type);
    }
}
