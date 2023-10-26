package com.unirutas.repository;

import com.unirutas.core.database.repository.CrudRepository;
import com.unirutas.core.dependency.annotations.Repository;
import com.unirutas.models.StudentSubscription;

@Repository
public class StudentSubscriptionRepository extends CrudRepository<StudentSubscription> {
}
