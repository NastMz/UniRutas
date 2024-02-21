package com.unirutas.repository;

import com.flexcore.database.repository.CrudRepository;
import com.flexcore.dependency.annotations.Repository;
import com.unirutas.models.StudentSubscription;

@Repository
public class StudentSubscriptionRepository extends CrudRepository<StudentSubscription> {
}
