package vn.edu.iuh.fit.smartwarehousebe.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A builder class for creating a composite {@link Specification} from multiple {@link Specification} instances.
 *
 * @param <T> the type of the entity for which the specifications are built
 * @description
 * @author: vie
 * @date: 3/3/25
 */
public class SpecificationBuilder<T> {
    private final List<Specification<T>> specifications = new ArrayList<>();

    public SpecificationBuilder<T> with(Specification<T> spec) {
        Optional.ofNullable(spec).ifPresent(specifications::add);
        return this;
    }

    public Specification<T> build() {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(Specification.where(null));
    }

    public static <T> SpecificationBuilder<T> builder() {
        return new SpecificationBuilder<>();
    }
}