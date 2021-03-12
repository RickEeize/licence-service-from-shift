package ftc.shift.sample.filter.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import ftc.shift.sample.entity.Customer;
import ftc.shift.sample.filter.SearchCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CustomerHandler implements Handler {
    @Override
    public <T> Predicate handle(SearchCriteria criteria, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getType() == null || !criteria.getType().equals("customer")) return null;
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-[M]M-[d]d").create();
            Customer customer = gson.fromJson(criteria.getValue().toString(), Customer.class);
            if (criteria.getOperation().equals("="))
                return criteriaBuilder.equal(root.get(criteria.getKey()), customer);
            else
                return null;
        } catch (JsonSyntaxException e) {
            return null;
        }
    }
}
