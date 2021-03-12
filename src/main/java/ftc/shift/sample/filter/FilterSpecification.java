package ftc.shift.sample.filter;

import ftc.shift.sample.filter.handler.Handler;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Set;

public class FilterSpecification<T> implements Specification<T> {

    private final SearchCriteria criteria;

    public FilterSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @SneakyThrows
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Reflections reflections = new Reflections("ftc.shift.sample.filter.handler");
        Set<Class<? extends Handler>> handlers = reflections.getSubTypesOf(Handler.class);
        for(Class<? extends Handler> handler : handlers){
            Predicate predicate = handler.getDeclaredConstructor().newInstance().handle(criteria, root, query, criteriaBuilder);
            if(predicate != null) return predicate;
        }
        return null;
    }
}
