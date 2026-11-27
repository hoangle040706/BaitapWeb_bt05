package vn.iotstar.repository.spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;

public final class UserSpecifications {

    private UserSpecifications() {
    }

    public static Specification<User> withFilters(String keyword, Role role, Integer status) {
        return hasKeyword(keyword).and(hasRole(role)).and(hasStatus(status));
    }

    public static Specification<User> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("username")), pattern),
                    cb.like(cb.lower(root.get("fullname")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };
    }

    public static Specification<User> hasRole(Role role) {
        return (root, query, cb) -> role == null ? cb.conjunction() : cb.equal(root.get("role"), role);
    }

    public static Specification<User> hasStatus(Integer status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }
}
