package com.electro.repository.inventory;

import com.electro.entity.inventory.Docket;
import com.electro.entity.inventory.DocketVariant;
import com.electro.entity.product.Product;
import com.electro.entity.product.Variant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Join;

public interface DocketRepository extends JpaRepository<Docket, Long>, JpaSpecificationExecutor<Docket> {
    Docket findByCode(String code);

    default Page<Docket> findDockets(Pageable pageable) {
        Specification<Docket> spec = (root, query, cb) -> {
//            Join<Docket, Variant> variant = root.join("variants");
//            Join<Variant, DocketVariant> docketVariant = variant.join("docketVariants");
//
//            query.distinct(true);
//            query.orderBy(cb.desc(docketVariant.get("docket").get("id")));

            return query.getRestriction();
        };

        return findAll(spec, pageable);
    }
}
