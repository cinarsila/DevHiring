package com.trendyol.testcasews.repository;


import com.trendyol.testcasews.model.LinkCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkCollectionRepository extends JpaRepository<LinkCollection, Long> {
	Optional<LinkCollection> findByUrl(String url);

	Optional<LinkCollection> findByDeepLink(String deepLink);
}