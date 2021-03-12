package ftc.shift.sample.filter.handler;

import ftc.shift.sample.filter.SearchCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BaseHandler implements Handler {
    @Override
    public <T> Predicate handle(SearchCriteria criteria, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getType() != null) return null;
        try {
            switch (criteria.getOperation()) {
                case ">":
                    return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                case "<":
                    return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
                case "=":
                    return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue().toString());
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }
}
