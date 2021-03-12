package ftc.shift.sample.filter;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecificationBuilder<T> {

    private final List<SearchCriteria> params;

    public SpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public SpecificationBuilder<T> with(SearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

    public Specification<T> build() {
        if (params.isEmpty()) return null;
        List<Specification<T>> specs = params.stream()
                .map(FilterSpecification<T>::new)
                .collect(Collectors.toList());
        Specification<T> result = specs.get(0);

        for(int i = 1; i < params.size(); i++){
            result = result.and(specs.get(i));
        }
        return result;
    }
}
